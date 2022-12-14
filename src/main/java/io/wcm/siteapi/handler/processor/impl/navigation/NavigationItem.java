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
package io.wcm.siteapi.handler.processor.impl.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.day.cq.wcm.api.Page;

class NavigationItem {

  private final Page page;
  private final Object link;
  private final List<NavigationItem> children = new ArrayList<>();

  NavigationItem(Page page, Object link) {
    this.page = page;
    this.link = link;
  }

  public @NotNull String getTitle() {
    return StringUtils.defaultString(this.page.getNavigationTitle(), this.page.getTitle());
  }

  public @NotNull Object getLink() {
    return this.link;
  }

  public @Nullable List<NavigationItem> getChildren() {
    if (this.children.isEmpty()) {
      return null;
    }
    return Collections.unmodifiableList(this.children);
  }

  void addChild(NavigationItem child) {
    this.children.add(child);
  }

}
