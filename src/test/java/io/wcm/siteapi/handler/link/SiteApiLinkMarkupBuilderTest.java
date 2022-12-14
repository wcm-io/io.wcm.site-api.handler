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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.Page;

import io.wcm.handler.link.Link;
import io.wcm.handler.link.LinkHandler;
import io.wcm.siteapi.handler.link.impl.CustomObjectLinkDecorator;
import io.wcm.siteapi.handler.link.impl.LinkDecoratorManagerImpl;
import io.wcm.siteapi.handler.testcontext.AppAemContext;
import io.wcm.siteapi.processor.util.impl.JsonObjectMapperImpl;
import io.wcm.sling.commons.adapter.AdaptTo;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class SiteApiLinkMarkupBuilderTest {

  private AemContext context = AppAemContext.newAemContext();

  private Page page;
  private LinkHandler linkHandler;

  @BeforeEach
  void setUp() {
    context.registerInjectActivateService(JsonObjectMapperImpl.class);
    context.registerInjectActivateService(LinkDecoratorManagerImpl.class);
    context.registerService(LinkDecorator.class, new CustomObjectLinkDecorator());

    page = context.currentPage(context.create().page("/content/test"));
    linkHandler = AdaptTo.notNull(context.request(), LinkHandler.class);
  }

  @Test
  void testSiteApiRequest() {
    Link link = linkHandler.get(page)
        .windowTarget("_blank")
        .build();
    assertEquals("<a href=\"/content/test.site.api/content.json\" "
        + "data-page-path=\"/content/test\" "
        + "data-type=\"internal\" "
        + "target=\"_blank\">",
        link.getMarkup());
  }

  @Test
  void testNonSiteApiRequest() {
    context.requestPathInfo().setSelectorString(null);
    context.requestPathInfo().setExtension(null);

    Link link = linkHandler.get(page)
        .windowTarget("_blank")
        .build();

    assertEquals("<a href=\"/content/test.html\" target=\"_blank\">",
        link.getMarkup());
  }

}
