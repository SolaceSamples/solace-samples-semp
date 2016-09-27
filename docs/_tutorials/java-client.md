---
layout: tutorials
title: Basic CRUD - Java Client Library 
summary: The basics of how to create, read, update, and delete a SEMP object using a Java client library
icon: java-logo.jpg
---

This is a quick tutorial to help you get started with the SEMP. You can find detailed reference of the SEMP resources by checking out the [API reference]({{ site.docs-api }}){:target="_top"}.

The following examples use a Java API generated from the SEMP specification and include an overview of the steps involved in creating this API. There is extensive support for creating SEMP client APIs in many programming languages. It should be easy enough to adapt the instructions and examples below to the programming language of your choosing.

Alternatively, it is also possible to use SEMP through directly through HTTP. If you are interested using the API directly through HTTP, you can look at this [this Quick Start tutorial]({{ site.baseurl }}/curl) walks you through the basics of making SEMP calls directly through HTTP.

## Contents

* [Assumptions](#assumptions)
* [Generating the Client Library](#generating-the-semp-client-library)
* [Client Library Basics](#client-library-basics)
* [Creating an Object](#creating-an-object-using-post)
* [Retrieving an Object](#retrieving-an-object-using-get)
* [Retrieving a Collection](#retrieving-a-collection-of-objects-using-get)
* [Partially Updating an Object](#partially-updating-an-object-using-patch)
* [Fully Updating an Object](#fully-updating-an-object-using-put)
* [Removing an Object](#removing-an-object-using-delete)
* [Summary](#summary)

## Assumptions

The examples below make a few assumptions for simplicity:

* These examples will work with a Client Username object to illustrate the various concepts. The concepts apply generally to all objects in SEMP.
* The SEMP username and password are `user:password`
* The Solace message router host and port are `solacevmr:8080`

## Generating the SEMP Client Library

Client libraries can be generate using the SEMP `swagger.json`. This file can be obtained in two ways:

1.	From the Solace developer portal here: [SEMP Schema]({{ site.docs-schema }}){:target="_top"}
2.	From the Solace message router directly via a GET call to the following URI: `http://solacevmr:8080/SEMP/v2/config/spec` - Remember to update the host and port to match your Solace message router.

The Swagger community provides a couple of different ways to create client libraries:

1.	The Swagger Editor - http://editor.swagger.io/#/
2.	The Swagger Code Generator - https://github.com/swagger-api/swagger-codegen

You can find links to these tools and other useful tools online here: http://swagger.io/tools/

One quick and easy way is to use the online editor. The following steps walk you through the creation of a client library using this online editor:

1.	Open the online Swagger editor: http://editor.swagger.io/#/
2.	In the editor’s **File** menu, select **Import File ...**
3.	Using the pop-up dialog, **Browse...** to the location of the `swagger.json` and **Open** the file
4.	In the editor’s **Generate Client** menu, select the desired language from the list provided
5.	A generated client library will start to download.

## Client Library Basics

Before jumping to specific tasks like creating a Client Username, we will first introduce a few basic concepts that common to the SEMP client library. If you have not already, you can learn more about these concepts and SEMP in general by checking out the API overview <<LINK to API overview>>.[API overview]({{ site.docs-overview }}){:target="_top"}.

### Initializing the SEMP API

Before sending any commands, you must initialize the client library. There is a close relationship between the tags used in the SEMP specification and the SEMP client library classes offered. Each tag, like `msgVpn`, is compiled into a class in the `io.swagger.client.api` package. Because of this, you have the option to work with the full SEMP API or subsets as controlled by the tags in the SEMP specification. You should determine which tag best fits your use case and use this client API class. For this introduction, we will use the `msgVpn` tag and the `MsgVpnApi` class because configuring a Solace message router message-VPN is a very common task in a dev-ops workflow.

Before you can send any commands to the Solace message router, you need an instance of the SEMP API. The following code shows you how to create such an instance and set the SEMP username and password. This will connect of HTTP. Secure connections are also supported and outlined below:

```
import io.swagger.client.ApiClient;
import io.swagger.client.api.MsgVpnApi;

ApiClient thisClient = new ApiClient();
thisClient.setBasePath(“http://solacevmr:8080/SEMP/v2/config”);
thisClient.setUsername(“user”);
thisClient.setPassword(“password”);
MsgVpnApi SEMPsempApiInstance = new MsgVpnApi(thisClient);
```

Remember to update the values in the above to match your environment.

[*Source Reference: JavaClientSample.initialize()*]({{ site.repository }}/blob/master/src/main/java/com/solace/samples/JavaClientSample.java){:target="_blank"}

### Error handling

There are many different errors that can occur when trying to connect with and manage the Solace message routers. In the client library, the `ApiException` class represents all of these errors. This `ApiException` wraps the HTTP errors and provides access to the HTTP response code, response headers, response body, etc.

The Solace message router will always respond with a JSON payload of the following format for all errors.

```
{
    "meta":{
        "request":{
            "method":<string>,
            "uri":<string>
        },
        "error":{
            "code": <number>,
            "description": <string>,
            "status": <string>
        },
        "responseCode":<number>
    }
}
```

In this way, client applications, can parse for the `SempError` structure and learn details of what went wrong. If we ignore other types of errors like connection timeout, etc, the following code shows you how to parse the Solace response body and extract the details using the client library `SempMetaOnlyResponse` object from the model.

```java
private void handleError(ApiException ae) {
    Gson gson = new Gson();
    String responseString = ae.getResponseBody();
    SempMetaOnlyResponse respObj = gson.fromJson(responseString, SempMetaOnlyResponse.class);
    SempError errorInfo = respObj.getMeta().getError();
    System.out.println("Error during operation. Details..." + 
            "\nHTTP Status Code: " + ae.getCode() + 
            "\nSEMP Error Code: " + errorInfo.getCode() + 
            "\nSEMP Error Status: " + errorInfo.getStatus() + 
            "\nSEMP Error Descriptions: " + errorInfo.getDescription()) ;
}
```

The code uses the `Gson` library to parse the JSON response, which is used internally by the Swagger client library. It then simply prints the relevant information to the console. 

[*Source Reference: JavaClientSample.handleError()*]({{ site.repository }}/blob/master/src/main/java/com/solace/samples/JavaClientSample.java){:target="_blank"}

## Creating an Object Using POST

You create a Client Username from the `clientUsernames` collection within the message-VPN. The Client Username has only one required attribute, its name. In this example I’ve chosen `tutorialUser`. For interest, I will also enable the new Client Username so it is ready for messaging clients to use. During creation, any attributes that are not specified will be created using default values. 

To create a Client Username, you use the `msgVpnsVidClientUsernamesPost()` method. If you understand how the resource names are composed in the SEMP API, the method names should be easy to derive and understand. For details on resource name composition, you can check out the [SEMP Concepts – URI Structure]({{ site.docs-docs-concepts-uri }}){:target="_top"}. 

The new Client Username is represented by the `MsgVpnClientUsername` class from the model. Using this class, you can set any Client Username attributes you would like during creation.

```java
    String msgVpn = "default";
    String clientUsername = "tutorialUser";
    MsgVpnClientUsername newClientUsername = new MsgVpnClientUsername();
    newClientUsername.setClientUsername(clientUsername);
    newClientUsername.setEnabled(true);
    MsgVpnClientUsernameResponse resp = null;
    List<String> nullSelector = null;
    try {
        resp = sempApiInstance.createMsgVpnClientUsername(msgVpn, newClientUsername, nullSelector);
    } catch (ApiException e) {
        handleError(e);
        return;
    }
```

The response will contain the newly created Client Username in the data portion. You can extract and print this new Client Username as follows.

```java
    MsgVpnClientUsername createdClientUsername = resp.getData();
    System.out.println("Created Client Username: " + createdClientUsername);
```

[*Source Reference: JavaClientSample.createObjectUsingPost()*]({{ site.repository }}/blob/master/src/main/java/com/solace/samples/JavaClientSample.java){:target="_blank"}

## Retrieving an Object Using GET

Now that you have created a Client Username, you can retrieve the object using an `msgVpnsVidClientUsernamesCidGet()`. The following code shows you how to retrieve a Client Username and print it to the console.

```java
try {
    String msgVpn = "default";
    String clientUsername = "tutorialUser";
    List<String> nullSelector = null;
    MsgVpnClientUsernameResponse resp = sempApiInstance.getMsgVpnClientUsername(msgVpn, clientUsername, nullSelector);
    System.out.println("Retrieved Client Username: " + resp.getData());
} catch (ApiException e) {
    handleError(e);
}
```

[*Source Reference: JavaClientSample.retrievingObjectUsingGet()*]({{ site.repository }}/blob/master/src/main/java/com/solace/samples/JavaClientSample.java){:target="_blank"}

## Retrieving a Collection of Objects Using GET

You can also retrieve all of the Client Usernames within the `default` Message VPN and you will see the newly created `tutorialUser` object as well as any others. For this, you would use the `msgVpnsVidClientUsernamesGet()` method which will execute an HTTP GET on the actual `clientUsernames` collection.

The following code will retrieve a list of all the Client Usernames in the `default` message-VPN and print the count to the console.

```java
try {
    String msgVpn = "default";
    // Ignore paging and selectors in this example. So set to null.
    MsgVpnClientUsernamesResponse resp = sempApiInstance.getMsgVpnClientUsernames(msgVpn, null, null, null, null);
    List<MsgVpnClientUsername> clientUsernamesList = resp.getData();
    System.out.println("Retrieved " + clientUsernamesList.size() + " Client Usernames.");
} catch (ApiException e) {
    handleError(e);
}
```

For large collections, the response will be paged. See [SEMP paging]({{ site.docs-concepts-paging }}){:target="_top"} for details.

[*Source Reference: JavaClientSample.retrievingCollectionUsingGet()*]({{ site.repository }}/blob/master/src/main/java/com/solace/samples/JavaClientSample.java){:target="_blank"}

## Partially Updating an Object Using PATCH

The HTTP PATCH method allows you to partially update a SEMP object, only the attributes that are specified are updated. So let’s disable the `tutorialUser` Client Username as an example of how PATCH can be used. 

The following code shows how to disable a Client Username. To do this, you create a `MsgVpnClientUsername` and update the enabled state to false. Then call the PATCH method. 

```java
try {
    String msgVpn = "default";
    String clientUsername = "tutorialUser";
    MsgVpnClientUsername updatedClientUsername = new MsgVpnClientUsername();
    updatedClientUsername.setEnabled(false);
    MsgVpnClientUsernameResponse resp = sempApiInstance.msgVpnsVidClientUsernamesCidPatch(
          msgVpn,
          clientUsername,
          updatedClientUsername);
} catch (ApiException e) {
    handleError(e);
}
```

[*Source Reference: JavaClientSample.partialObjectUpdateUsingPatch()*]({{ site.repository }}/blob/master/src/main/java/com/solace/samples/JavaClientSample.java){:target="_blank"}

## Fully Updating an Object Using PUT

The HTTP PUT method is used to update an object to match the attributes specified. All attributes not specified are reset to default values. The method for updating a Client Username via a PUT call is `msgVpnsVidClientUsernamesCidPut()`. For the purposes of an example, let’s reset the `tutorialUser` so that it is enabled and all other attributes are defaulted. The following code would do this:

```java
try {
    String msgVpn = "default";
    String clientUsername = "tutorialUser";
    MsgVpnClientUsername updatedClientUsername = new MsgVpnClientUsername();
    updatedClientUsername.setEnabled(true);
    MsgVpnClientUsernameResponse resp = sempApiInstance.replaceMsgVpnClientUsername(
                            msgVpn, clientUsername, updatedClientUsername, null);
} catch (ApiException e) {
    handleError(e);
}
```

[*Source Reference: JavaClientSample.fullObjectUpdateUsingPut()*]({{ site.repository }}/blob/master/src/main/java/com/solace/samples/JavaClientSample.java){:target="_blank"}

## Removing an Object Using DELETE

The HTTP DELETE method is used to remove an object which is accessed through the `msgVpnsVidClientUsernamesCidDelete()` method. This method requires only the VPN and Client Username strings to identify the object to delete. The following code deletes the `tutorialUser` Client Username.

```java
try {
    String msgVpn = "default";
    String clientUsername = "tutorialUser";
    SempMetaOnlyResponse resp = sempApiInstance.deleteMsgVpnClientUsername(msgVpn, clientUsername);
} catch (ApiException e) {
    handleError(e);
}
```

[*Source Reference: JavaClientSample.removingObjectUsingDelete()*]({{ site.repository }}/blob/master/src/main/java/com/solace/samples/JavaClientSample.java){:target="_blank"}

## Summary

The full source code for this example is available in [GitHub]({{ site.repository }}){:target="_blank"}. If you combine the example source code shown above results in the following source:

*   [JavaClientSample.java]({{ site.repository }}/blob/master/src/main/java/com/solace/samples/JavaClientSample.java){:target="_blank"}

### Getting the Source

Clone the GitHub repository containing the Solace samples.

```
git clone {{ site.repository }}
cd {{ site.baseurl | remove: '/'}}
```

### Building

The project uses Gradle. To build, execute the following command.

```
./gradlew build
```

This builds all of the Java Samples with OS specific launch scripts. The files are staged in the `build/staged` directory.

### Running the Sample

You start the `JavaClientSample` with a three arguments:

1. The SEMP Base Path. For example: `http://solacevmr:8080/SEMP/v2/config`
2. The SEMP Username.
3. The SEMP Password. 

For example: 

```
$ ./build/staged/bin/javaClientSample <SEMP_BASE_PATH> <SEMP_USER> <SEMP_PASSWORD>
JavaClientSample initializing...
SEMP initializing: <SEMP_BASE_PATH>, <SEMP_USER>
Creating Object: tutorialUser
...
Client Username delete. Resp: 200
```

At this point, you have created, retrieved, updated and deleted a Client Username object using SEMP. The examples used a generated client library in Java to interact with the Solace message router, but you can adapt the steps to any programming language of your choice. 

SEMP is an extensive API that lets you configure anything on your Solace message router so there is a lot more to understand. If you want to know more, either you can get more familiar with the SEMP concepts by checking out the [user guide]({{ site.docs-overview }}){:target="_top"} or you can see the full [developer documentation for the API]({{ site.docs-api }}){:target="_top"}.
