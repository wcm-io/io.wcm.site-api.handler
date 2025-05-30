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

import static io.wcm.siteapi.handler.testcontext.AppAemContext.CONTENT_ROOT;
import static io.wcm.testing.mock.wcmio.siteapi.processor.MockProcessorRequestContext.processorRequestContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;

import io.wcm.siteapi.handler.link.LinkDecorator;
import io.wcm.siteapi.handler.link.impl.LinkDecoratorManagerImpl;
import io.wcm.siteapi.handler.link.impl.UrlLinkDecorator;
import io.wcm.siteapi.handler.testcontext.AppAemContext;
import io.wcm.siteapi.processor.ProcessorRequestContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class NavigationProcessorTest {

  private AemContext context = AppAemContext.newAemContext();

  private NavigationProcessor underTest;
  private Page rootPage;

  @BeforeEach
  void setUp() throws Exception {
    context.registerService(LinkDecorator.class, new UrlLinkDecorator());
    context.registerInjectActivateService(LinkDecoratorManagerImpl.class);
    underTest = context.registerInjectActivateService(NavigationProcessor.class);

    rootPage = context.create().page(CONTENT_ROOT, null,
        NameConstants.PN_NAV_TITLE, "Home");
    context.create().page(rootPage, "child1");
    context.create().page(rootPage, "child2");
    context.create().page(rootPage, "child3", null,
        NameConstants.PN_HIDE_IN_NAV, true);
  }

  @Test
  void testOutsideSiteRoot() {
    context.currentPage(context.create().page("/content/elsewhere"));
    ProcessorRequestContext processorRequestContext = processorRequestContext(context.request(), NavigationProcessor.SUFFIX);

    assertNull(underTest.process(processorRequestContext));
  }

  @Test
  void testSiteRoot() {
    context.currentPage(rootPage);
    ProcessorRequestContext processorRequestContext = processorRequestContext(context.request(), NavigationProcessor.SUFFIX);

    NavigationItem root = underTest.process(processorRequestContext);
    assertNotNull(root);
    assertEquals("Home", root.getTitle());
    assertEquals("/content/sample/en.site.api/content.json", root.getLink());

    List<NavigationItem> children = root.getChildren();
    assertNotNull(children);
    assertEquals(2, children.size());

    NavigationItem child1 = children.get(0);
    assertEquals("child1", child1.getTitle());
    assertEquals("/content/sample/en/child1.site.api/content.json", child1.getLink());
    assertNull(child1.getChildren());

    NavigationItem child2 = children.get(1);
    assertEquals("child2", child2.getTitle());
    assertEquals("/content/sample/en/child2.site.api/content.json", child2.getLink());
    assertNull(child2.getChildren());
  }

}
