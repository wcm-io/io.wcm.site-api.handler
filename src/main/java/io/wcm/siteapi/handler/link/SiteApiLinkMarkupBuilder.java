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
package io.wcm.siteapi.handler.link;

import static io.wcm.siteapi.processor.util.SiteApiRequest.isSiteApiRequest;
import static java.util.function.Predicate.not;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ProviderType;

import io.wcm.handler.commons.dom.Anchor;
import io.wcm.handler.link.Link;
import io.wcm.handler.link.spi.LinkMarkupBuilder;
import io.wcm.siteapi.handler.link.impl.AnchorAttribute;
import io.wcm.siteapi.processor.url.SiteApiConfiguration;
import io.wcm.siteapi.processor.util.JsonObjectMapper;

/**
 * Link markup builder that kicks in when a Site API request is served: Instad of the standard anchor tag
 * serialization, an anchor with the URL generated with custom attributes based on the link decoration
 * provided by {@link LinkDecorator} implementation.
 */
@ProviderType
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class },
    adapters = LinkMarkupBuilder.class)
public final class SiteApiLinkMarkupBuilder implements LinkMarkupBuilder {

  @SlingObject
  private Resource currentResource;
  @SlingObject(injectionStrategy = InjectionStrategy.OPTIONAL)
  private SlingHttpServletRequest request;
  @OSGiService
  private SiteApiConfiguration siteApiConfiguration;
  @OSGiService
  private JsonObjectMapper jsonObjectMapper;
  @OSGiService
  private LinkDecoratorManager linkDecoratorManager;

  @Override
  public boolean accepts(@NotNull Link link) {
    return isSiteApiRequest(request, siteApiConfiguration)
        && link.isValid()
        && StringUtils.isNotEmpty(link.getUrl());
  }

  @Override
  public @Nullable Anchor build(@NotNull Link link) {
    // build anchor
    Anchor anchor = new Anchor(link.getUrl());

    // apply properties from link decoration as data properties
    getAnchorAttributes(link).forEach(attr -> anchor.setAttribute(attr.getName(), attr.getValue()));

    return anchor;
  }

  /**
   * Get attributes to be set on anchor element.
   */
  @SuppressWarnings("null")
  private @NotNull Stream<AnchorAttribute> getAnchorAttributes(@NotNull Link link) {
    Object decorated = linkDecoratorManager.decorate(link, currentResource);
    if (decorated == null) {
      return Stream.empty();
    }
    return jsonObjectMapper.toMap(decorated).entrySet().stream()
        .filter(entry -> entry.getValue() != null)
        .map(entry -> new AnchorAttribute(entry.getKey(), entry.getValue()))
        .filter(not(AnchorAttribute::isIgnore))
        .sorted();
  }

}
