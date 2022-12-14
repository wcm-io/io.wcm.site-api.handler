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

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ConsumerType;

import io.wcm.handler.link.Link;
import io.wcm.sling.commons.caservice.ContextAwareService;

/**
 * Maps a link object to a representation in Site API.
 * Allows projects to customize how links are represented in the API.
 * @param <T> Type representing link
 */
@ConsumerType
public interface LinkDecorator<T> extends Function<@NotNull Link, @Nullable T>, ContextAwareService {

  /**
   * @param link Link object. The link is valid.
   * @return Link representation or null if there is no valid representation.
   */
  @Override
  @Nullable
  T apply(@NotNull Link link);

}
