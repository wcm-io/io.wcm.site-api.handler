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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.handler.link.Link;
import io.wcm.handler.link.LinkHandler;
import io.wcm.siteapi.handler.link.LinkDecorator;
import io.wcm.siteapi.handler.link.LinkDecoratorManager;
import io.wcm.siteapi.handler.testcontext.AppAemContext;
import io.wcm.sling.commons.adapter.AdaptTo;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class LinkDecoratorManagerImplTest {

  private AemContext context = AppAemContext.newAemContext();

  private LinkDecoratorManager underTest;
  private LinkHandler linkHandler;

  @BeforeEach
  void setUp() throws Exception {
    underTest = context.registerInjectActivateService(LinkDecoratorManagerImpl.class);

    context.currentPage(context.create().page("/content/test"));
    linkHandler = AdaptTo.notNull(context.request(), LinkHandler.class);
  }

  @Test
  @SuppressWarnings("null")
  void testNoDecorator() {
    Link link = linkHandler.get("http://dummy").build();
    Object result = underTest.decorate(link, context.currentResource());
    assertEquals(link, result);
  }

  @Test
  @SuppressWarnings("null")
  void testDecorator() {
    context.registerService(LinkDecorator.class, new UrlLinkDecorator());

    Link link = linkHandler.get("http://dummy").build();
    Object result = underTest.decorate(link, context.currentResource());
    assertEquals(link.getUrl(), result);
  }

  @Test
  @SuppressWarnings("null")
  void testDecorator_Invalid() {
    context.registerService(LinkDecorator.class, new UrlLinkDecorator());

    Link link = linkHandler.invalid();
    Object result = underTest.decorate(link, context.currentResource());
    assertNull(result);
  }

}
