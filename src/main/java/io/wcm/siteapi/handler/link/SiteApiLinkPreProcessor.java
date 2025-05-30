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

import static io.wcm.siteapi.processor.ProcessorConstants.PROCESSOR_CONTENT;
import static io.wcm.siteapi.processor.util.SiteApiRequest.isSiteApiRequest;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.jetbrains.annotations.NotNull;
import org.osgi.annotation.versioning.ProviderType;

import io.wcm.handler.link.Link;
import io.wcm.handler.link.LinkArgs;
import io.wcm.handler.link.spi.LinkProcessor;
import io.wcm.handler.link.type.InternalLinkType;
import io.wcm.siteapi.processor.url.JsonSuffix;
import io.wcm.siteapi.processor.url.SiteApiConfiguration;

/**
 * Adapts link handling within Site API requests: Links to internal pages are rewritten to Site API "content" links.
 */
@ProviderType
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class },
    adapters = LinkProcessor.class)
public final class SiteApiLinkPreProcessor implements LinkProcessor {

  /**
   * Link handler property to define the suffix of the processor to link upon.
   */
  public static final String SUFFIX_PROPERTY = "siteapi:suffix";

  /**
   * Link handler property to define a suffix extension (optional).
   */
  public static final String SUFFIX_EXTENSION_PROPERTY = "siteapi:suffixExtension";

  @SlingObject(injectionStrategy = InjectionStrategy.OPTIONAL)
  private SlingHttpServletRequest request;
  @OSGiService
  private SiteApiConfiguration siteApiConfiguration;

  @Override
  public @NotNull Link process(@NotNull Link link) {
    if (isSiteApiRequest(request, siteApiConfiguration) && isInternalLink(link)) {
      processInternalLink(link);
    }
    return link;
  }

  private boolean isInternalLink(@NotNull Link link) {
    return link.getLinkType() instanceof InternalLinkType;
  }

  private void processInternalLink(@NotNull Link link) {
    LinkArgs linkArgs = link.getLinkRequest().getLinkArgs();

    // disable "suffix" selector
    linkArgs.disableSuffixSelector(true);

    // add Site API selector and extension
    linkArgs.selectors(siteApiConfiguration.getSelector());
    linkArgs.extension(siteApiConfiguration.getExtension());

    // Processor suffix can be specified via property, defaults to "content"
    String suffix = linkArgs.getProperties().get(SUFFIX_PROPERTY, PROCESSOR_CONTENT);
    String suffixExtension = linkArgs.getProperties().get(SUFFIX_EXTENSION_PROPERTY, String.class);
    linkArgs.suffix(JsonSuffix.build(suffix, suffixExtension));
  }

}
