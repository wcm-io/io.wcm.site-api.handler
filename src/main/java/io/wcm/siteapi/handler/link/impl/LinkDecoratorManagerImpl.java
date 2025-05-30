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

import java.util.Collections;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.FieldOption;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import io.wcm.handler.link.Link;
import io.wcm.siteapi.handler.link.LinkDecorator;
import io.wcm.siteapi.handler.link.LinkDecoratorManager;
import io.wcm.sling.commons.caservice.ContextAwareServiceCollectionResolver;
import io.wcm.sling.commons.caservice.ContextAwareServiceResolver;

/**
 * Implements {@link LinkDecoratorManager}.
 */
@Component(service = LinkDecoratorManager.class)
public class LinkDecoratorManagerImpl implements LinkDecoratorManager {

  @Reference(cardinality = ReferenceCardinality.MULTIPLE, fieldOption = FieldOption.UPDATE,
      policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY)
  private SortedSet<ServiceReference<LinkDecorator<Object>>> services = new ConcurrentSkipListSet<>(Collections.reverseOrder());

  @Reference
  private ContextAwareServiceResolver serviceResolver;
  private ContextAwareServiceCollectionResolver<LinkDecorator<Object>, Void> serviceCollectionResolver;

  @Activate
  private void activate() {
    this.serviceCollectionResolver = serviceResolver.getCollectionResolver(this.services);
  }

  @Deactivate
  private void deactivate() {
    this.serviceCollectionResolver.close();
  }

  @Override
  @NotNull
  public LinkDecorator<Object> getDecorator(@NotNull Resource contextResource) {
    LinkDecorator<Object> decorator = serviceCollectionResolver.resolve(contextResource);
    if (decorator != null) {
      return decorator;
    }
    else {
      // if no decorator is present return link object
      return link -> link;
    }
  }

  @Override
  public @Nullable Object decorate(@NotNull Link link, @NotNull Resource contextResource) {
    if (link.isValid()) {
      return getDecorator(contextResource).apply(link);
    }
    return null;
  }

}
