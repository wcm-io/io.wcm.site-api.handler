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

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.caconfig.spi.metadata.PropertyMetadata;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceRanking;

import com.day.cq.dam.api.DamConstants;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;

import io.wcm.handler.link.Link;
import io.wcm.handler.link.LinkHandler;
import io.wcm.handler.media.Media;
import io.wcm.handler.media.MediaHandler;
import io.wcm.siteapi.handler.link.LinkDecoratorManager;
import io.wcm.siteapi.handler.media.MediaDecoratorManager;
import io.wcm.siteapi.processor.caconfig.ContextAwareConfigurationPropertyMapper;
import io.wcm.sling.commons.adapter.AdaptTo;

/**
 * Detects properties that use widgetType=pathbrowser.
 * Depending on the actual path, those are either references to content pages or assets.
 * A corresponding link or media object is resolved, and mapped to it's JSON representation.
 */
@Component(service = ContextAwareConfigurationPropertyMapper.class)
@ServiceRanking(-250)
public class ContentPathPropertyMapper implements ContextAwareConfigurationPropertyMapper<Object> {

  // copied from io.wcm.caconfig.editor.EditorProperties to avoid direct dependency
  static final String PROPERTY_WIDGET_TYPE = "widgetType";
  static final String WIDGET_TYPE_PATHBROWSER = "pathbrowser";

  @Reference
  private LinkDecoratorManager linkDecoratorManager;
  @Reference
  private MediaDecoratorManager mediaDecoratorManager;

  @Override
  public boolean accept(@NotNull Object value,
      @NotNull PropertyMetadata<?> metadata, @NotNull SlingHttpServletRequest request) {
    Map<String, String> properties = metadata.getProperties();
    if (properties == null) {
      return false;
    }
    return StringUtils.equals(properties.get(PROPERTY_WIDGET_TYPE), WIDGET_TYPE_PATHBROWSER);
  }

  @Override
  public @Nullable Object map(@NotNull Object value,
      @NotNull PropertyMetadata<?> metadata, @NotNull SlingHttpServletRequest request) {
    Resource resource = getResource(value, request.getResourceResolver());
    if (resource != null) {
      if (isPage(resource)) {
        return buildPageReference(resource, request);
      }
      else if (isAsset(resource)) {
        return buildMediaReference(resource, request);
      }
    }
    return null;
  }

  /**
   * Check if the given path points to a valid resource.
   */
  private @Nullable Resource getResource(@NotNull Object value, @NotNull ResourceResolver resourceResolver) {
    if (value instanceof String) {
      return resourceResolver.getResource((String)value);
    }
    else {
      return null;
    }
  }

  /**
   * Checks if the resource is a AEM page.
   */
  private boolean isPage(@NotNull Resource resource) {
    return StringUtils.equals(resource.getResourceType(), NameConstants.NT_PAGE);
  }

  /**
   * Resolves a link to the page using link handler and returns the decorated reference.
   */
  private @Nullable Object buildPageReference(@NotNull Resource resource, @NotNull SlingHttpServletRequest request) {
    Page page = resource.adaptTo(Page.class);
    if (page == null) {
      return null;
    }
    LinkHandler linkHandler = AdaptTo.notNull(request, LinkHandler.class);
    Link link = linkHandler.get(page).build();
    return linkDecoratorManager.decorate(link, request.getResource());
  }

  /**
   * Checks if the resource is an AEM asset.
   */
  private boolean isAsset(@NotNull Resource resource) {
    return StringUtils.equals(resource.getResourceType(), DamConstants.NT_DAM_ASSET);
  }

  /**
   * Resolves a reference to the asset using media handler and returns the decorated reference.
   */
  private @Nullable Object buildMediaReference(@NotNull Resource resource, @NotNull SlingHttpServletRequest request) {
    MediaHandler mediaHandler = AdaptTo.notNull(request, MediaHandler.class);
    Media media = mediaHandler.get(resource.getPath()).build();
    return mediaDecoratorManager.decorate(media, request.getResource());
  }

}
