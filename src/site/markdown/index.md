## About Site API Handler Extensions

Support wcm.io Handler infrastructure in Site API.

[![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.site-api.handler)](https://repo1.maven.org/maven2/io/wcm/io.wcm.site-api.handler/)


### Documentation

* [Usage][usage]
* [API documentation][apidocs]
* [Changelog][changelog]


### Overview

The Site API Handler extensions provide:

* Link Handler Pre-Processor that rewrites internal links to Site API URL-links when rendered within a Site API request (including serving content via Sling Models Exporter)
* Link Handler Markup Builder that customizes the generated anchor markup in rich text HTML fragments by applying the custom attributes from project-specific link representations
* Decorate links and media references with project-specific Java objects
* Detect content path references in context-aware configurations and map them to link and media reference decorations
* Built-in `navigation` processor building navigation structure starting from site root (as configured via URL handler/Site configuration)
* Based on [Site API General Concepts][siteapi-general-concepts] and [Site API Processor][siteapi-processor]
* Based on [Context-Aware Services][wcmio-sling-context-aware-services] for provider type service interfaces


### AEM Version Support Matrix

|Site API Handler version |AEM version supported
|---------------------------|----------------------
|1.0.0 or higher            |AEM 6.5.7+, AEMaaCS


### Dependencies

To use this module you have to deploy also:

|---|---|---|
| [wcm.io Sling Commons](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.commons/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.sling.commons)](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.commons/) |
| [wcm.io AEM Sling Models Extensions](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.models/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.sling.models)](https://repo1.maven.org/maven2/io/wcm/io.wcm.sling.models/) |
| [wcm.io WCM Commons](https://repo1.maven.org/maven2/io/wcm/io.wcm.wcm.commons/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.wcm.commons)](https://repo1.maven.org/maven2/io/wcm/io.wcm.wcm.commons/) |
| [wcm.io WCM Granite UI Extensions](https://repo1.maven.org/maven2/io/wcm/io.wcm.wcm.ui.granite/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.wcm.ui.granite)](https://repo1.maven.org/maven2/io/wcm/io.wcm.wcm.ui.granite/) |
| [wcm.io Handler Commons](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.commons/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.handler.commons)](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.commons/) |
| [wcm.io URL Handler](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.url/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.handler.url)](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.url/) |
| [wcm.io Media Handler](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.media/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.handler.media)](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.media/) |
| [wcm.io Link Handler](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.link/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.handler.link)](https://repo1.maven.org/maven2/io/wcm/io.wcm.handler.link/) |
| [wcm.io Site API Processor](https://repo1.maven.org/maven2/io/wcm/io.wcm.site-api.processor/) | [![Maven Central](https://img.shields.io/maven-central/v/io.wcm/io.wcm.site-api.processor)](https://repo1.maven.org/maven2/io/wcm/io.wcm.site-api.processor/) |


### GitHub Repository

Sources: https://github.com/wcm-io/io.wcm.site-api.handler


[usage]: usage.html
[apidocs]: apidocs/
[changelog]: changes-report.html
[siteapi-general-concepts]: https://wcm.io/site-api/general-concepts.html
[siteapi-processor]: https://wcm.io/site-api/processor/
[wcmio-sling-context-aware-services]: https://wcm.io/sling/commons/context-aware-services.html
