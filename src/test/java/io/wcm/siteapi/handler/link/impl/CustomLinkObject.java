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
package io.wcm.siteapi.handler.link.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.wcm.handler.link.Link;

public class CustomLinkObject {

  private final Link link;

  CustomLinkObject(Link link) {
    this.link = link;
  }

  public @NotNull String getUrl() {
    return link.getUrl();
  }

  public @NotNull String getType() {
    return link.getLinkType().getId();
  }

  public @Nullable String getTarget() {
    return link.getLinkRequest().getLinkArgs().getWindowTarget();
  }

  public @Nullable String getPagePath() {
    if (link.getTargetPage() != null) {
      return link.getTargetPage().getPath();
    }
    return null;
  }

}
