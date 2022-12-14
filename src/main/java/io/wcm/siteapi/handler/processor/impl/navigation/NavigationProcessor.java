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

import static io.wcm.siteapi.processor.ProcessorConstants.PROPERTY_SUFFIX;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;

import io.wcm.handler.link.Link;
import io.wcm.handler.link.LinkHandler;
import io.wcm.handler.url.ui.SiteRoot;
import io.wcm.siteapi.handler.link.LinkDecorator;
import io.wcm.siteapi.handler.link.LinkDecoratorManager;
import io.wcm.siteapi.processor.JsonObjectProcessor;
import io.wcm.siteapi.processor.Processor;
import io.wcm.siteapi.processor.ProcessorRequestContext;
import io.wcm.sling.commons.adapter.AdaptTo;

/**
 * Generates site navigation structure starting with site root.
 */
@Designate(ocd = NavigationProcessor.Config.class)
@Component(service = Processor.class, configurationPolicy = ConfigurationPolicy.REQUIRE,
    property = PROPERTY_SUFFIX + "=" + NavigationProcessor.SUFFIX)
@ServiceRanking(-200)
public class NavigationProcessor implements JsonObjectProcessor<NavigationItem> {

  @ObjectClassDefinition(
      name = "wcm.io Site API Navigation Processor",
      description = "Generates navigations structure for current site.")
  @interface Config {

    @AttributeDefinition(
        name = "Enabled",
        description = "Processor is enabled.")
    boolean enabled() default false;

  }

  static final String SUFFIX = "navigation";

  @Reference
  private LinkDecoratorManager linkDecoratorManager;

  @Override
  public @Nullable NavigationItem process(@NotNull ProcessorRequestContext context) {
    Page siteRootPage = getSiteRootPage(context.getResource());
    if (siteRootPage == null) {
      return null;
    }
    LinkHandler linkHandler = AdaptTo.notNull(context.getRequest(), LinkHandler.class);
    LinkDecorator<Object> linkDecorator = linkDecoratorManager.getDecorator(context.getResource());
    return buildNavItem(siteRootPage, linkHandler, linkDecorator);
  }

  private @Nullable Page getSiteRootPage(@NotNull Resource currentResource) {
    SiteRoot siteRoot = AdaptTo.notNull(currentResource, SiteRoot.class);
    return siteRoot.getRootPage();
  }

  /**
   * Builds navigation item including children for navigation structure.
   * @param page Page
   * @return Navigation item or null if page is invalid for navigation
   */
  private @Nullable NavigationItem buildNavItem(@NotNull Page page,
      @NotNull LinkHandler linkHandler, @NotNull LinkDecorator<Object> linkDecorator) {
    if (page.isHideInNav()) {
      return null;
    }

    Link link = linkHandler.get(page).build();
    Object linkObject = linkDecorator.apply(link);
    NavigationItem item = new NavigationItem(page, linkObject);

    Iterator<Page> children = page.listChildren(new PageFilter(false, false));
    while (children.hasNext()) {
      NavigationItem child = buildNavItem(children.next(), linkHandler, linkDecorator);
      if (child != null) {
        item.addChild(child);
      }
    }

    return item;
  }

}
