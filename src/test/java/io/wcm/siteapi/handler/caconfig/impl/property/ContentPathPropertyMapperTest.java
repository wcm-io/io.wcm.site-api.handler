/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2022 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.siteapi.handler.caconfig.impl.property;

import static io.wcm.handler.media.MediaNameConstants.PN_COMPONENT_MEDIA_AUTOCROP;
import static io.wcm.handler.media.MediaNameConstants.PN_COMPONENT_MEDIA_FORMATS_MANDATORY_NAMES;
import static io.wcm.siteapi.handler.caconfig.impl.property.ContentPathPropertyMapper.PROPERTY_WIDGET_TYPE;
import static io.wcm.siteapi.handler.caconfig.impl.property.ContentPathPropertyMapper.WIDGET_TYPE_PATHBROWSER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.spi.metadata.PropertyMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.dam.api.Asset;

import io.wcm.handler.link.Link;
import io.wcm.handler.media.Media;
import io.wcm.handler.media.Rendition;
import io.wcm.handler.media.format.MediaFormat;
import io.wcm.handler.media.format.MediaFormatBuilder;
import io.wcm.handler.media.spi.MediaFormatProvider;
import io.wcm.siteapi.handler.link.impl.LinkDecoratorManagerImpl;
import io.wcm.siteapi.handler.media.impl.MediaDecoratorManagerImpl;
import io.wcm.siteapi.handler.testcontext.AppAemContext;
import io.wcm.siteapi.processor.caconfig.ContextAwareConfigurationPropertyMapper;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import io.wcm.wcm.commons.contenttype.ContentType;

@ExtendWith(AemContextExtension.class)
class ContentPathPropertyMapperTest {

  private AemContext context = AppAemContext.newAemContext();

  private ContextAwareConfigurationPropertyMapper<?> underTest;
  private PropertyMetadata<String> propertyMetadata;

  @BeforeEach
  @SuppressWarnings("null")
  void setUp() {
    context.registerInjectActivateService(LinkDecoratorManagerImpl.class);
    context.registerInjectActivateService(MediaDecoratorManagerImpl.class);
    underTest = context.registerInjectActivateService(ContentPathPropertyMapper.class);

    context.currentPage(context.create().page("/content/test"));
    propertyMetadata = new PropertyMetadata<String>("dummy", String.class);
  }

  @Test
  void testAccept_NoMatch() {
    assertFalse(underTest.accept("/path", propertyMetadata, context.request()));
  }

  @Test
  void testAccept_Match() {
    propertyMetadata.properties(Map.of(PROPERTY_WIDGET_TYPE, WIDGET_TYPE_PATHBROWSER));
    assertTrue(underTest.accept("/path", propertyMetadata, context.request()));
  }

  @Test
  void testMap_InvalidPath() {
    assertNull(underTest.map("/invalid", propertyMetadata, context.request()));
  }

  @Test
  void testMap_ResourceNotPageOrAsset() {
    Resource resource = context.create().resource("/content/xyz");
    assertNull(underTest.map(resource.getPath(), propertyMetadata, context.request()));
  }

  @Test
  void testMap_Page() {
    Object result = underTest.map("/content/test", propertyMetadata, context.request());
    assertTrue(result instanceof Link);
    assertEquals("/content/test.site.api/content.json", ((Link)result).getUrl());
  }

  @Test
  @SuppressWarnings("null")
  void testMap_Asset() {
    Asset asset = context.create().asset("/content/dam/test.png", 10, 10, ContentType.PNG);

    Object result = underTest.map(asset.getPath(), propertyMetadata, context.request());
    assertTrue(result instanceof Media);
    Media media = (Media)result;
    assertEquals(asset.getPath(), media.getAsset().getPath());
    assertTrue(media.isValid());
  }

  @Test
  @SuppressWarnings("null")
  void testMap_Asset_MediaFormat() {
    Asset asset = context.create().asset("/content/dam/test.png", 10, 10, ContentType.PNG);
    context.create().assetRenditionWebEnabled(asset);

    MediaFormat mfLandscape = MediaFormatBuilder.create("2-1")
        .ratio(2, 1)
        .extensions("png")
        .build();
    MediaFormat mfSquare = MediaFormatBuilder.create("1-1")
        .ratio(1, 1)
        .extensions("png")
        .build();

    context.registerService(MediaFormatProvider.class, new MediaFormatProvider(Set.of(mfLandscape, mfSquare)) {
      // provide media formats
    });

    propertyMetadata.properties(Map.<String, String>of(
        PN_COMPONENT_MEDIA_FORMATS_MANDATORY_NAMES, mfLandscape.getName() + "," + mfSquare.getName(),
        PN_COMPONENT_MEDIA_AUTOCROP, "true"));

    Object result = underTest.map(asset.getPath(), propertyMetadata, context.request());
    assertTrue(result instanceof Media);
    Media media = (Media)result;
    assertEquals(asset.getPath(), media.getAsset().getPath());
    assertTrue(media.isValid());

    List<Rendition> renditions = List.copyOf(media.getRenditions());
    assertEquals(2, renditions.size());
    assertEquals(mfLandscape, renditions.get(0).getMediaFormat());
    assertEquals(mfSquare, renditions.get(1).getMediaFormat());
  }

}
