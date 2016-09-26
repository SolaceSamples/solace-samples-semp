---
layout: tutorials
title: Java Client Library 
summary: How to get started with SEMP using the OpenAPI generated Java client library.
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

Before jumping to specific tasks like creating a client username, we will first introduce a few basic concepts that common to the SEMP client library. If you have not already, you can learn more about these concepts and SEMP in general by checking out the API overview <<LINK to API overview>>.[API overview]({{ site.docs-overview }}){:target="_top"}.

### Initializing the SEMP API

Before sending any commands, you must initialize the client library. There is a close relationship between the tags used in the SEMP specification and the SEMP client library classes offered. Each tag, like `MsgVpns`, is compiled into a class in the `io.swagger.client.api` package. Because of this, you have the option to work with the full SEMP API or subsets as controlled by the tags in the SEMP specification. You should determine which tag best fits your use case and use this client API class. For this introduction, we will use the `MsgVpns` tag and the `MsgVpnsApi` class because configuring a Solace message router message-VPN is a very common task in a dev-ops workflow.

Before you can send any commands to the Solace message router, you need an instance of the SEMP API. The following code shows you how to create such an instance and set the SEMP username and password. This will connect of HTTP. Secure connections are also supported and outlined below:

```
import io.swagger.client.ApiClient;
import io.swagger.client.api.MsgVpnsApi;

ApiClient thisClient = new ApiClient();
thisClient.setBasePath(“http://solacevmr:8080/SEMP/v2/config”);
thisClient.setUsername(“user”);
thisClient.setPassword(“password”);
sempApiInstance = new MsgVpnsApi(thisClient);
```

Remember to update the values in the above to match your environment.

### Using a secure connection to the Solace Message Router

TODO – Need details here.

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

In this way, client applications, can parse for the `error` structure and learn details of what went wrong. If we ignore other types of errors like connection timeout, etc, the following code shows you how to parse the Solace response body and extract the details using the client library `MetaResponse` object from the model.

```java
private void handleError(ApiException ae) {
    ObjectMapper mapper = new ObjectMapper();
    String responseString = ae.getResponseBody();
    try {
        MetaResponse respObj = mapper.readValue(responseString, MetaResponse.class);
        io.swagger.client.model.Error errorInfo = respObj.getMeta().getError();
        System.out.println("Error during operation. Details..." + 
                "\nHTTP Status Code: " + ae.getCode() + 
                "\nSEMP Error Code: " + errorInfo.getCode() + 
                "\nSEMP Error Status: " + errorInfo.getStatus() + 
                "\nSEMP Error Descriptions: " + errorInfo.getDescription()) ;
    } catch (IOException e) {
        System.out.println("Unexpected response format: " + responseString);
    }
}
```

The code uses the `ObjectMapper` from the Jackson library, which is used internally by the Swagger client library. It then simply prints the relevant information to the console. 

## Creating an object using POST

You create a client username from the `clientUsernames` collection within the message-VPN. The client username has only one required attribute, its name. In this example I’ve chosen `tutorialUser`. For interest, I will also enable the new client username so it is ready for messaging clients to use. During creation, any attributes that are not specified will be created using default values. 

To create a client username, you use the `msgVpnsVidClientUsernamesPost()` method. If you understand how the resource names are composed in the SEMP API, the method names should be easy to derive and understand. For details on resource name composition, you can check out the [SEMP Concepts – URI Structure]({{ site.docs-docs-concepts-uri }}){:target="_top"}. 

The new client username is represented by the `MsgVpnClientUsername` class from the model. Using this class, you can set any client username attributes you would like during creation.

```java
String vpn = “default”;
String clientUsername = “tutorialUser”;
MsgVpnClientUsername newClientUsername = new MsgVpnClientUsername();
newClientUsername.setClientUsername(clientUsername);
newClientUsername.setEnabled(true);
MsgVpnClientUsernameResponse resp = null;
try {
    resp = sempApiInstance.msgVpnsVidClientUsernamesPost(vpn, newClientUsername);
} catch (ApiException e) {
    handleError(e);
    return;
}
```

The response will contain the newly created client username in the data portion. You can extract and print this new client username as follows.

```java
MsgVpnClientUsername createdClientUsername = resp.getData();
System.out.println("Created client username: " + createdClientUsername);
```

## Retrieving an object using GET

Now that you have created a client username, you can retrieve the object using an `msgVpnsVidClientUsernamesCidGet()`. The following code shows you how to retrieve a client username and print it to the console.

```java
try {
    String vpn = “default”;
    String clientUsername = “tutorialUser”;
    MsgVpnClientUsernameResponse resp = sempApiInstance.msgVpnsVidClientUsernamesCidGet(vpn, clientUsername);
    System.out.println("Retrieved client username: " + resp.getData());
} catch (ApiException e) {
    handleError(e);
}
```

## Retrieving a collection of objects using GET

You can also retrieve all of the client usernames within the `default` message VPN and you will see the newly created `tutorialUser` object as well as any others. For this, you would use the `msgVpnsVidClientUsernamesGet()` method which will execute an HTTP GET on the actual `clientUsernames` collection.

The following code will retrieve a list of all the client usernames in the `default` message-VPN and print the count to the console.

```java
try {
    MsgVpnClientUsernamesResponse resp = sempApiInstance.msgVpnsVidClientUsernamesGet(vpn);
    List<MsgVpnClientUsername> clientUsernamesList = resp.getData();
    System.out.println("Retrieved " + clientUsernamesList.size() + " client usernames.");
} catch (ApiException e) {
    handleError(e);
}
```

For large collections, the response will be paged. See [SEMP paging]({{ site.docs-docs-concepts-paging }}){:target="_top"} for details.

## Partially updating an object using PATCH

The HTTP PATCH method allows you to partially update a SEMP object, only the attributes that are specified are updated. So let’s disable the `tutorialUser` client username as an example of how PATCH can be used. 

The following code shows how to disable a client username. To do this, you create a `MsgVpnClientUsername` and update the enabled state to false. Then call the PATCH method. 

```java
try {
    String vpn = “default”;
    String clientUsername = “tutorialUser”;
    MsgVpnClientUsername updatedClientUsername = new MsgVpnClientUsername();
    updatedClientUsername.setEnabled(false);
    MsgVpnClientUsernameResponse resp = sempApiInstance.msgVpnsVidClientUsernamesCidPatch(
          vpn,
          clientUsername,
          updatedClientUsername);
} catch (ApiException e) {
    handleError(e);
}
```

## Fully updating an object using PUT

The HTTP PUT method is used to update an object to match the attributes specified. All attributes not specified are reset to default values. The method for updating a client username via a PUT call is `msgVpnsVidClientUsernamesCidPut()`. For the purposes of an example, let’s reset the `tutorialUser` so that it is enabled and all other attributes are defaulted. The following code would do this:

```java
try {
    String vpn = “default”;
    String clientUsername = “tutorialUser”;
    MsgVpnClientUsername updatedClientUsername = new MsgVpnClientUsername();
    updatedClientUsername.setEnabled(true);
    MsgVpnClientUsernameResponse resp = sempApiInstance. msgVpnsVidClientUsernamesCidPut(
          vpn,
          clientUsername,
          updatedClientUsername);
} catch (ApiException e) {
    handleError(e);
}
```

## Removing an object using DELETE

The HTTP DELETE method is used to remove an object which is accessed through the `msgVpnsVidClientUsernamesCidDelete()` method. This method requires only the VPN and client username strings to identify the object to delete. The following code deletes the `tutorialUser` client username.

```java
try {
    String vpn = “default”;
    String clientUsername = “tutorialUser”;
    MetaResponse resp = sempApiInstance.msgVpnsVidClientUsernamesCidDelete(vpn, clientUsername);
} catch (ApiException e) {
    handleError(e);
}
```

## Summary

At this point, you have created, retrieved, updated and deleted a client username object using SEMP. The examples used a generated client library in Java to interact with the Solace message router, but you can adapt the steps to any programming language of your choice. 

SEMP is an extensive API that lets you configure anything on your Solace message router so there is a lot more to understand. If you want to know more, either you can get more familiar with the SEMP concepts by checking out the [user guide]({{ site.docs-overview }}){:target="_top"} or you can see the full [developer documentation for the API]({{ site.docs-api }}){:target="_top"}.
