## Site API Handler Extensions usage

### Configure Link and Media Decorators

It is recommended to define the link and media reference representation within you project via custom Java classes, and use this thorough the project (especially in Sling Models) to have a consistent representation of the reference. By default, the `Link` and `Media` instances returned by Link and Media Handler are used for this, but they may not fulfill your needs.

By implementing the `LinkDecorator` and `MediaDecorator` service interface you can map those instances to project-specific Java objects.


### Configure Link Handling

Customize the link handler configuration in your project:

```java
@Component(service = LinkHandlerConfig.class)
public class LinkHandlerConfigImpl extends LinkHandlerConfig {

  private static final List<Class<? extends LinkProcessor>> PRE_PROCESSORS = List.of(
      SiteApiLinkPreProcessor.class);

  private static final List<Class<? extends LinkMarkupBuilder>> LINK_MARKUP_BUILDERS = List.of(
      SiteApiLinkMarkupBuilder.class,
      SimpleLinkMarkupBuilder.class);

  @Override
  public @NotNull List<Class<? extends LinkProcessor>> getPreProcessors() {
    return PRE_PROCESSORS;
  }

  @Override
  public @NotNull List<Class<? extends LinkMarkupBuilder>> getMarkupBuilders() {
    return LINK_MARKUP_BUILDERS;
  }

  ...

}
```

The `SiteApiLinkPreProcessor` steps in when a link is build via Link Handler in context of a Site API request (using Site API selector/extension/suffix). It rewrites the URL to the target page pointing to the `content` JSON endpoint. It works for every url generated with Link Handler, regardless if this takes place in a processor implementation or a Sling Model.

The `SiteApiLinkMarkupBuilder` is useful when you return HTML fragments in the JSON API for rich text content. If you have customized your link representation by implementing a `LinkDecorator`, this markup builder generates the anchor element with the link URL and additional `data-` properties reflecting the properties of the Java object returned by the decorator.


### Configuring built-in processors

You can enable the built-in processors by providing an OSGi configuration:

```
  io.wcm.siteapi.handler.processor.impl.navigation.NavigationProcessor
    enabled=B"true"
```


### Map Context-Aware Configurations with Link and Media references

The Site API Handler Extensions provide with [ContentPathPropertyMapper][ContentPathPropertyMapper] a default implementation to customize the serialization of Context-Aware Configurations. It detects link and media references and maps them to the configured link and media decoration.

This may be enough for your project. If you need more control about the mapping e.g. based on additional custom properties in the context-aware configuration definitions you can implement your own mapper with a higher service ranking.


[ContentPathPropertyMapper]: https://github.com/wcm-io/io.wcm.site-api.handler/blob/develop/src/main/java/io/wcm/siteapi/handler/caconfig/impl/property/ContentPathPropertyMapper.java
