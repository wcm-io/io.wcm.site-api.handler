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
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PropertyNameTest {

  @Test
  void testToKebabCase() {
    assertEquals("word", toKebabCase("word"));
    assertEquals("word", toKebabCase("Word"));
    assertEquals("two-words", toKebabCase("twoWords"));
    assertEquals("two-words", toKebabCase("TwoWords"));
    assertEquals("three-words-total", toKebabCase("threeWordsTotal"));
  }

}
