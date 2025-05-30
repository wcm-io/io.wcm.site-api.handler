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
package io.wcm.siteapi.handler.media.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.dam.api.Asset;

import io.wcm.handler.media.Media;
import io.wcm.handler.media.MediaHandler;
import io.wcm.siteapi.handler.media.MediaDecorator;
import io.wcm.siteapi.handler.media.MediaDecoratorManager;
import io.wcm.siteapi.handler.testcontext.AppAemContext;
import io.wcm.sling.commons.adapter.AdaptTo;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import io.wcm.wcm.commons.contenttype.ContentType;

@ExtendWith(AemContextExtension.class)
class MediaDecoratorManagerImplTest {

  private AemContext context = AppAemContext.newAemContext();

  private MediaDecoratorManager underTest;
  private MediaHandler mediaHandler;
  private Asset asset;

  @BeforeEach
  void setUp() throws Exception {
    underTest = context.registerInjectActivateService(MediaDecoratorManagerImpl.class);

    context.currentPage(context.create().page("/content/test"));
    mediaHandler = AdaptTo.notNull(context.request(), MediaHandler.class);

    asset = context.create().asset("/content/dam/test.png", 10, 10, ContentType.PNG);
  }

  @Test
  @SuppressWarnings("null")
  void testNoDecorator() {
    Media media = mediaHandler.get(asset.getPath()).build();
    Object result = underTest.decorate(media, context.currentResource());
    assertEquals(media, result);
  }

  @Test
  @SuppressWarnings("null")
  void testDefaultDecorator() {
    context.registerService(MediaDecorator.class, new UrlMediaDecorator());

    Media media = mediaHandler.get(asset.getPath()).build();
    Object result = underTest.decorate(media, context.currentResource());
    assertEquals(media.getUrl(), result);
  }

  @Test
  @SuppressWarnings("null")
  void testDefaultDecorator_Invalid() {
    context.registerService(MediaDecorator.class, new UrlMediaDecorator());

    Media media = mediaHandler.invalid();
    Object result = underTest.decorate(media, context.currentResource());
    assertNull(result);
  }

}
