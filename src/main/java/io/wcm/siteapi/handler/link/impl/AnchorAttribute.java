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

import static io.wcm.siteapi.handler.link.impl.PropertyName.toKebabCase;

import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.wcm.siteapi.handler.link.SiteApiLinkMarkupBuilder;

/**
 * Represents an anchor attribute set by {@link SiteApiLinkMarkupBuilder}.
 */
public class AnchorAttribute implements Comparable<AnchorAttribute> {

  // properties which are implicitly set on anchor and do not need to be set again, or should be otherwise ignored
  private static final Pattern IGNORE_ANCHOR_ATTRIBUTE_PATTERN = Pattern.compile("^(url|href|:.*)$");

  // possible names for standard "target" attribute
  private static final Set<String> TARGET_ANCHOR_ATTRIBUTES = Set.of("target", "windowTarget");

  private final String name;
  private final String value;
  private final boolean ignore;

  /**
   * @param name Property name
   * @param value Property value
   */
  public AnchorAttribute(@NotNull String name, @NotNull Object value) {
    this.value = value.toString();
    if (isTargetAttribute(name)) {
      this.name = "target";
      this.ignore = isTargetSelf(this.value);
    }
    else {
      this.name = "data-" + toKebabCase(name);
      this.ignore = IGNORE_ANCHOR_ATTRIBUTE_PATTERN.matcher(name).matches();
    }
  }

  public @NotNull String getName() {
    return this.name;
  }

  public @NotNull String getValue() {
    return this.value;
  }

  public boolean isIgnore() {
    return ignore;
  }

  @Override
  public int compareTo(AnchorAttribute other) {
    return name.compareTo(other.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof AnchorAttribute) {
      return name.equals(((AnchorAttribute)other).name);
    }
    return false;
  }

  private static boolean isTargetAttribute(@NotNull String name) {
    return TARGET_ANCHOR_ATTRIBUTES.contains(name);
  }

  private static boolean isTargetSelf(@Nullable String value) {
    return StringUtils.equals(value, "_self");
  }

}
