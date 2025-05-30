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
package io.wcm.siteapi.handler.testcontext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;

import io.wcm.handler.link.spi.LinkHandlerConfig;
import io.wcm.handler.link.spi.LinkMarkupBuilder;
import io.wcm.handler.link.spi.LinkProcessor;
import io.wcm.siteapi.handler.link.SiteApiLinkMarkupBuilder;
import io.wcm.siteapi.handler.link.SiteApiLinkPreProcessor;

public class LinkHandlerConfigImpl extends LinkHandlerConfig {

  private final List<Class<? extends LinkProcessor>> preProcessors = Stream.concat(
      Stream.of(SiteApiLinkPreProcessor.class), super.getPreProcessors().stream())
      .collect(Collectors.toList());

  private final List<Class<? extends LinkMarkupBuilder>> linkMarkupBuilders = Stream.concat(
      Stream.of(SiteApiLinkMarkupBuilder.class), super.getMarkupBuilders().stream())
      .collect(Collectors.toList());

  @Override
  public @NotNull List<Class<? extends LinkProcessor>> getPreProcessors() {
    return preProcessors;
  }

  @Override
  public @NotNull List<Class<? extends LinkMarkupBuilder>> getMarkupBuilders() {
    return linkMarkupBuilders;
  }

}
