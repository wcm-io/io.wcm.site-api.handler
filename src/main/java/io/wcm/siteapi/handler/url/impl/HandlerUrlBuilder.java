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
package io.wcm.siteapi.handler.url.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.service.component.annotations.Component;

import com.day.cq.wcm.api.Page;

import io.wcm.handler.link.LinkHandler;
import io.wcm.siteapi.handler.link.SiteApiLinkPreProcessor;
import io.wcm.siteapi.processor.url.UrlBuilder;
import io.wcm.sling.commons.adapter.AdaptTo;

/**
 * Builds Site API URLs based on Link Handler and URL Handler.
 */
@Component(service = UrlBuilder.class)
public class HandlerUrlBuilder implements UrlBuilder {

  @Override
  public String build(@NotNull Page page, @NotNull String suffix, @Nullable String suffixExtension,
      @NotNull SlingHttpServletRequest request) {
    LinkHandler linkHandler = AdaptTo.notNull(request, LinkHandler.class);
    // we do not need to specify Site API selector/extension here - this applied by SiteApiLinkPreProcessor
    return linkHandler.get(page)
        .property(SiteApiLinkPreProcessor.SUFFIX_PROPERTY, suffix)
        .property(SiteApiLinkPreProcessor.SUFFIX_EXTENSION_PROPERTY, suffixExtension)
        .buildUrl();
  }

}
