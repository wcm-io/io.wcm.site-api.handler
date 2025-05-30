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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AnchorAttributeTest {

  @Test
  void testIgnore() {
    assertTrue(new AnchorAttribute("url", "dummy").isIgnore());
    assertTrue(new AnchorAttribute("href", "dummy").isIgnore());
    assertTrue(new AnchorAttribute(":type", "dummy").isIgnore());
    assertFalse(new AnchorAttribute("other", "dummy").isIgnore());
  }

  @Test
  void testTargetBlank() {
    AnchorAttribute attr = new AnchorAttribute("target", "_blank");
    assertEquals("target", attr.getName());
    assertEquals("_blank", attr.getValue());
    assertFalse(attr.isIgnore());
  }

  @Test
  void testTargetSelf() {
    AnchorAttribute attr = new AnchorAttribute("target", "_self");
    assertEquals("target", attr.getName());
    assertEquals("_self", attr.getValue());
    assertTrue(attr.isIgnore());
  }

  @Test
  void testWindowTarketBlank() {
    AnchorAttribute attr = new AnchorAttribute("windowTarget", "_blank");
    assertEquals("target", attr.getName());
    assertEquals("_blank", attr.getValue());
    assertFalse(attr.isIgnore());
  }

}
