---
layout: tutorials
title: Generating the SEMP Client Library
summary: Generate a SEMP management client library for any of the supported programming languages
icon: generate-semp-client-lib.svg
icon-height: 70px
icon-width: 228px
---

Programmatic management of the Solace Messaging through the SEMP API can be done directly through accessing the system's HTTP REST protocol API (see the related [Basic Operations - curl tutorial]({{ site.baseurl }}/curl/)) or more conveniently, using a local SEMP client library which is:
<br><br>
* In a programming language that is native to your management code
* Hides the REST protocol details
* Enables to focus on the objects to be managed.

## Goals

In this tutorial we will show and explain the steps to generate the latest version of the SEMP client library for any of the supported programming languages.

## Trying it yourself

This tutorial is available in [GitHub]({{ site.repository }}){:target="_blank"} along with the other Solace SEMP Getting Started Examples.

You can manually follow the steps in the next sections to generate the SEMP client library for any supported programming language or use the provided automated Gradle script for `Java`, `Ruby` or `Python`.

As a pre-requisite, you will need to have Java installed.

## Overview

The Solace Element Management Protocol (SEMP) API is based on the [OpenAPI (a.k.a. Swagger) specification]({{ site.swagger-spec }}){:target="_top"}, which enables an object oriented approach to the management of Solace Messaging by specifying management objects with valid operations and properties.

* The API is defined as a JSON or YAML formatted specification
* From this specification it is possible to programmatically generate client libraries in most popular languages
* Documentation can also be generated similarly, so it is always up-to-date

When a new version of Solace Messaging is released with new management capabilities, an updated API specification will reflect that. Then a new backwards-compatible version of the client library can be generated with added support to the latest features.

Following steps are required to generate the SEMP client library:

1. Getting the API specification supporting at least the targeted version of the Solace Messaging.
2. Getting the tool that can convert the API spec to client code on the targeted language.
3. Setting customization rules, such as the naming of the generated library.
4. Generating the library.


## Step 1: Getting the API specification

There are two ways to obtain the API specification:

* The latest version from the Solace Developer Portal here: [SEMP Schema]({{ site.docs-schema }}){:target="_top"}
* From the Solace Messaging directly via a GET call to the following URI: [http://solacevmr:8080/SEMP/v2/config/spec](http://solacevmr:8080/SEMP/v2/config/spec) - Remember to update the host and port to match your Solace Messaging system.

It is generally recommended to download the latest API specification from the Solace developer portal, which will support all the versions of your currently used Solace messaging routers.


## Step 2: Getting the tool

We recommend using [Swagger Codegen]({{ site.swagger-codegen-site }}){:target="_top"} out of the several tools the Swagger community  [project website]({{ site.swagger-tools-site }}){:target="_top"} lists, because it provides the best customization.

Swagger Codegen is an easy-to-use Java based command-line tool. It takes the API specification, target programming language and customization parameters as input and generates the native API client library for that language, API reference documentation and getting started documentation. It supports building an API client library for most popular programming languages. For full documentation including the list of available client programming languages, check the [Swagger Codegen GitHub project site]({{ site.swagger-codegen-github-overview }}).

The tool itself can be downloaded from Maven Central as [swagger-codegen-cli-2.2.2.jar]({{ site.swagger-codegenjar-from-mvncentral }}) or obtained other ways, as described at the above project site. Ensure you also have a Java JRE installed.

## Step 3: Customization

Running just the tool without any parameters will provide the list of supported languages. To get language-specific options for customization, execute:

```
java -jar swagger-codegen-cli-2.2.2.jar config-help -l <supported programming language>
```

Customization options vary for each programming language but it is generally possible to at least specify the name of the SEMP client library. In all the provided getting-started samples and the automated Gradle build script we assume that the library's name will be a language specific variant of the name "Semp client sample lib".

It is recommended to review the additional customization options provided by above command. For example for Java,  be aware what networking library the client will be using.

For the `Java`, `Python` and `Ruby` samples we have provided example customizations in the `codegen_config_<language>.json` files in the language specific subdirectories.


## Step 4: Generating the SEMP client library

We recommend to generate the SEMP client library in a temporary work folder as it generates a lot of files, some of which will generally not be required for development or production use.

With the JSON or YAML formatted API specification and codegen_config JSON file available, run:

```
java -jar swagger-codegen-cli-2.2.2.jar generate \
    -l <language> -i <API spec swagger json or yaml file> \
    -c <codegen_config json file>
```

This will generate the SEMP client library in a language specific directory, a README.md getting started documentation, client API documentation in the docs directory, and several other artifacts.

## Automated generation

You may have got here by manually executing the steps above. For automated execution of all above, create a local clone of our GitHub project and execute the provided Gradle script:

```
git clone https://github.com/SolaceSamples/solace-samples-semp
cd solace-samples-semp/<language>       # java, python or ruby

# Generate the latest library and place subset of it in the local directory
# The Swagger codgen tool and the latest swagger.yaml api spec will be placed in the parent dir.
../gradlew buildSempLib                 
```

## Summary

We have shown how to generate the latest version of the SEMP client library, which is now ready to use. Use it in your project structure, adjust for any library names as required.

If using it for the first time for a new client language, the getting started documentation (filtered out by default by the automated Gradle script) includes help to get to a basic working SEMP management example and it will advise of any additional specific steps for the language you are using. Reviewing the [Solace SEMP API Reference]({{ site.docs-api }}){:target="_top"} documentation{% if jekyll.environment != 'solaceCloud' %} and the provided [Java]({{ site.baseurl }}/messagevpn-with-queue-java/), [Python]({{ site.baseurl }}/messagevpn-with-queue-python/) or [Ruby]({{ site.baseurl }}/messagevpn-with-queue-ruby/) samples{% endif %} will help to understand some of the common ideas.

Our [Message VPN with Queue series]({{ site.baseurl }}/messagevpn-with-queue-java/) of samples also show how to create a high level library for common management tasks for Solace Messaging.
