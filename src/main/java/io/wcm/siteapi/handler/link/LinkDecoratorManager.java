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

import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ProviderType;

import io.wcm.handler.link.Link;

/**
 * Transform a link to it's representation in the API based on the matching
 * context-aware service implementation of {@link LinkDecorator}.
 * If no custom decorator service is registered, the link object itself is returned as representation.
 */
@ProviderType
public interface LinkDecoratorManager {

  /**
   * Get a link decorator for given context.
   * @param contextResource Context resource
   * @return Link decorator
   */
  @NotNull
  LinkDecorator<Object> getDecorator(@NotNull Resource contextResource);

  /**
   * Maps a link object to a representation in Site API.
   * @param link Link object.
   * @param contextResource Context resource
   * @return Link representation or null if the link is invalid or there is no valid representation.
   */
  @Nullable
  Object decorate(@NotNull Link link, @NotNull Resource contextResource);

}
