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
package io.wcm.siteapi.handler.media;

import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ProviderType;

import io.wcm.handler.media.Media;

/**
 * Transform a media to it's representation in the API based on the matching
 * context-aware service implementation of {@link MediaDecorator}.
 * If no custom decorator service is registered, the media object itself is returned as representation.
 */
@ProviderType
public interface MediaDecoratorManager {

  /**
   * Get a media decorator for given context.
   * @param contextResource Context resource
   * @return Media decorator
   */
  @NotNull
  MediaDecorator<Object> getDecorator(@NotNull Resource contextResource);

  /**
   * Maps a media object to a representation in Site API.
   * @param media Media object.
   * @param contextResource Context resource
   * @return Media representation or null if the media is invalid or there is no valid representation.
   */
  @Nullable
  Object decorate(@NotNull Media media, @NotNull Resource contextResource);

}
