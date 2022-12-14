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

import static com.adobe.cq.export.json.ExporterConstants.SLING_MODEL_EXTENSION;
import static com.adobe.cq.export.json.ExporterConstants.SLING_MODEL_SELECTOR;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.Page;

import io.wcm.handler.link.LinkHandler;
import io.wcm.siteapi.handler.testcontext.AppAemContext;
import io.wcm.sling.commons.adapter.AdaptTo;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class SiteApiLinkPreProcessorTest {

  private AemContext context = AppAemContext.newAemContext();

  private Page page;
  private LinkHandler linkHandler;

  @BeforeEach
  void setUp() {
    page = context.currentPage(context.create().page("/content/test"));
    linkHandler = AdaptTo.notNull(context.request(), LinkHandler.class);
  }

  @Test
  void testSiteApiRequest() {
    assertEquals("/content/test.site.api/content.json", linkHandler.get(page).buildUrl());
  }

  @Test
  void testSiteApiRequest_CustomSuffix() {
    assertEquals("/content/test.site.api/suffix1.json", linkHandler.get(page)
        .property(SiteApiLinkPreProcessor.SUFFIX_PROPERTY, "suffix1")
        .buildUrl());
  }

  @Test
  void testModelJsonRequest() {
    context.requestPathInfo().setSelectorString(SLING_MODEL_SELECTOR);
    context.requestPathInfo().setExtension(SLING_MODEL_EXTENSION);

    assertEquals("/content/test.site.api/content.json", linkHandler.get(page).buildUrl());
  }

  @Test
  void testHtmlRequest() {
    context.requestPathInfo().setSelectorString(null);
    context.requestPathInfo().setExtension(null);

    assertEquals("/content/test.html", linkHandler.get(page).buildUrl());
  }

}
