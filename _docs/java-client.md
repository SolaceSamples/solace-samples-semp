---
layout: tutorials
title: Basic Operations - Java
summary: The basics of how to create, read, update, and delete a SEMP object using a Java client library
icon: java-logo.svg
icon-height: 90px
icon-width: 72px
---

This is a quick tutorial to help you get started with the SEMP Java client library. You can learn more about the SEMP API using the [Online Documentation]({{ site.docs-semp-home }}){:target="_top"}. These documents explain the API concepts and details about the REST API. Additionally developers will find the [SEMP API Reference]({{ site.docs-api }}){:target="_top"} useful in understanding how to apply the concepts in this tutorial to other Solace Messaging objects.

**Note**: This tutorial applies to the SEMP API starting in version 2. For older versions of the SEMP API, you can see the [Legacy SEMP Documentation]({{ site.docs-semp-legacy }}){:target="_top"}

The following examples use a Java API generated from the SEMP specification and include an overview of the steps involved in creating this API. There is extensive support for creating SEMP client APIs in many programming languages. It should be easy enough to adapt the instructions and examples below to the programming language of your choosing.

Alternatively, it is also possible to use SEMP directly through HTTP. If you are interested in using the API directly through HTTP, you can look at this [Quick Start tutorial]({{ site.baseurl }}/curl/) which walks you through the basics of making SEMP calls directly through HTTP.

## Assumptions

The examples below make a few assumptions for simplicity:

* These examples will work with a Client Username object to illustrate the various concepts. The concepts apply generally to all objects in SEMP.
* The SEMP username and password are `user:password`
* The Solace Messaging host and port are `solacevmr:8080`
* The Solace message-VPN is `default`

{% include_relative assets/solaceMessaging.md %}

## Generating the SEMP Client Library

The [Generating the SEMP Client Library tutorial]({{ site.baseurl }}/generate-semp-client-lib/) provides details about generating the latest version of the library. The Gradle build script at the end of this tutorial will automatically generate the required SEMP Java Client Library for the first time if it doesn't exist yet, as part of the build.

## Client Library Basics

Before jumping to specific tasks like creating a Client Username, we will first introduce a few basic concepts common to the SEMP client library. If you have not already, you can learn more about these concepts and SEMP in general by checking out the [SEMP Concepts]({{ site.docs-architecture }}){:target="_top"}.

### Initializing the SEMP API

Before sending any commands, you must initialize the client library. There is a close relationship between the tags used in the SEMP specification and the SEMP client library classes offered. Each tag, like `msgVpn`, is compiled into a class in the `com.solace.labs.sempclient.samplelib.api` package. Because of this, you have the option to work with the full SEMP API or subsets as controlled by the tags in the SEMP specification. You should determine which tag best fits your use case and use this client API class. For this introduction, we will use the `msgVpn` tag and the `MsgVpnApi` class because configuring a Solace Messaging message-VPN is a very common task in a dev-ops workflow.

Before you can send any commands to the Solace Messaging system, you need an instance of the SEMP API. The following code shows you how to create such an instance and set the SEMP username and password.

```
import com.solace.labs.sempclient.samplelib.ApiClient;
import com.solace.labs.sempclient.samplelib.api.MsgVpnApi;

ApiClient thisClient = new ApiClient();
thisClient.setBasePath("http://solacevmr:8080/SEMP/v2/config");
thisClient.setUsername("user");
thisClient.setPassword("password");
MsgVpnApi sempApiInstance = new MsgVpnApi(thisClient);
```

Remember to update the values in the above example to match your environment.

[*Source Reference: initialize() from BasicOperationsSample*]({{ site.repository }}/blob/master/java/src/main/java/com/solace/samples/BasicOperationsSample.java){:target="_blank"}

### Error handling

There are many different errors that can occur when trying to connect with and manage the Solace Messaging. In the client library, the `ApiException` class represents all of these errors. This `ApiException` wraps the HTTP errors and provides access to the HTTP response code, response headers, response body, etc.

The Solace messaging system will always respond with a JSON payload of the following format for all errors.

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
            "\nSEMP Error Descriptions: " + errorInfo.getDescription());
}
```

The code uses the `Gson` library to parse the JSON response, which is used internally by the Swagger client library. There's no reason why you can't use your own favourite JSON parser, of course. It then simply prints the relevant information to the console.

[*Source Reference: handleError() from BasicOperationsSample*]({{ site.repository }}/blob/master/java/src/main/java/com/solace/samples/BasicOperationsSample.java){:target="_blank"}

## Creating an Object Using POST

You create a Client Username from the `clientUsername` collection within the message-VPN. The Client Username has only one required attribute, its name. In this example I’ve chosen `tutorialUser`. For interest, I will also enable the new Client Username so it is ready for messaging clients to use. During creation, any attributes that are not specified will be created using default values.

To create a Client Username, you use the `createMsgVpnClientUsername()` method. If you understand how the resource names are composed in the SEMP API, the method names should be easy to derive and understand. For details on resource name composition, you can check out the [SEMP Concepts - URI Structure]({{ site.docs-concepts-uri }}){:target="_top"}.

The new Client Username is represented by the `MsgVpnClientUsername` class from the model. Using this class, you can set any Client Username attributes you would like during creation.
The `msgVpn` is set by the message-VPN name argument when you start this sample.
The response will contain the newly created Client Username in the data portion.

```java
public void createObjectUsingPost() {

    String clientUsername = "tutorialUser";
    System.out.format("Creating Object: %s \n", clientUsername);

    MsgVpnClientUsername newClientUsername = new MsgVpnClientUsername();
    newClientUsername.setClientUsername(clientUsername);
    newClientUsername.setEnabled(true);
    MsgVpnClientUsernameResponse resp = null;
    try {
            resp = sempApiInstance.createMsgVpnClientUsername(msgVpn, newClientUsername, null);
    } catch (ApiException e) {
        handleError(e);
        return;
    }

    MsgVpnClientUsername createdClientUsername = resp.getData();
    System.out.println("Created Client Username: " + createdClientUsername);
}
```

[*Source Reference: createObjectUsingPost() from BasicOperationsSample*]({{ site.repository }}/blob/master/java/src/main/java/com/solace/samples/BasicOperationsSample.java){:target="_blank"}

## Retrieving an Object Using GET

Now that you have created a Client Username, you can retrieve the object using `getMsgVpnClientUsername()`. The following code shows you how to retrieve a Client Username and print it to the console.

```java
public void retrievingObjectUsingGet() {
    try {
        String clientUsername = "tutorialUser";
        MsgVpnClientUsernameResponse resp = sempApiInstance.getMsgVpnClientUsername(msgVpn, clientUsername, null);
        System.out.println("Retrieved Client Username: " + resp.getData());
    } catch (ApiException e) {
        handleError(e);
    }
}
```

[*Source Reference: retrievingObjectUsingGet() from BasicOperationsSample*]({{ site.repository }}/blob/master/java/src/main/java/com/solace/samples/BasicOperationsSample.java){:target="_blank"}

## Retrieving a Collection of Objects Using GET

You can also retrieve all of the Client Usernames within the specified Message VPN and you will see the newly created `tutorialUser` object as well as any others. For this, you would use the `getMsgVpnClientUsernames()` method which will execute a GET on the actual `clientUsername` collection.

The following code will retrieve a list of all the Client Usernames in the specified message-VPN and print the count to the console.

```java
public void retrievingCollectionUsingGet() {
    try {
        // Ignore paging and selectors in this example. So set to null.
        MsgVpnClientUsernamesResponse resp = sempApiInstance.getMsgVpnClientUsernames(msgVpn, null, null, null, null);
        List<MsgVpnClientUsername> clientUsernamesList = resp.getData();
        System.out.println("Retrieved " + clientUsernamesList.size() + " Client Usernames.");
    } catch (ApiException e) {
        handleError(e);
    }
}
```

For large collections, the response will be paged. See [SEMP paging]({{ site.docs-concepts-paging }}){:target="_top"} for details.

[*Source Reference: retrievingCollectionUsingGet() from BasicOperationsSample*]({{ site.repository}}/blob/master/java/src/main/java/com/solace/samples/BasicOperationsSample.java){:target="_blank"}

## Partially Updating an Object Using PATCH

The PATCH method allows you to partially update a SEMP object, only the attributes that are specified are updated. So let’s disable the `tutorialUser` Client Username as an example of how PATCH can be used.

The following code shows how to disable a Client Username. To do this, you create a `MsgVpnClientUsername` and update the enabled state to false. Then call the PATCH method.

```java
public void partialObjectUpdateUsingPatch() {
    try {
        String clientUsername = "tutorialUser";
        MsgVpnClientUsername updatedClientUsername = new MsgVpnClientUsername();
        updatedClientUsername.setEnabled(false);
        MsgVpnClientUsernameResponse resp = sempApiInstance.updateMsgVpnClientUsername(
                msgVpn, clientUsername, updatedClientUsername, null);
        System.out.println("Updated: " + resp.getData());
    } catch (ApiException e) {
        handleError(e);
    }
}
```

[*Source Reference: partialObjectUpdateUsingPatch() from BasicOperationsSample*]({{ site.repository }}/blob/master/java/src/main/java/com/solace/samples/BasicOperationsSample.java){:target="_blank"}

## Fully Updating an Object Using PUT

The PUT method is used to replace an existing object. The method for replacing a Client Username via a PUT call is `replaceMsgVpnClientUsername()`. For the purposes of an example, let’s replace the existing `MsgVpnClientUsername` (`tutorialUser`) with a new one. Default values are used for all parameters not provided. The following code would do this.

```java
public void replaceObjectUpdateUsingPut() {
    try {
        String clientUsername = "tutorialUser";
        MsgVpnClientUsername updatedClientUsername = new MsgVpnClientUsername();
        updatedClientUsername.setEnabled(true);
        MsgVpnClientUsernameResponse resp = sempApiInstance.replaceMsgVpnClientUsername(
                        msgVpn, clientUsername, updatedClientUsername, null);
        System.out.println("Updated: " + resp.getData());

    } catch (ApiException e) {
        handleError(e);
    }
}
```

[*Source Reference: fullObjectUpdateUsingPut() from BasicOperationsSample*]({{ site.repository }}/blob/master/java/src/main/java/com/solace/samples/BasicOperationsSample.java){:target="_blank"}

## Removing an Object Using DELETE

The DELETE method is used to remove an object which is accessed through the `deleteMsgVpnClientUsername()` method. This method requires only the VPN and Client Username strings to identify the object to delete. The following code deletes the `tutorialUser` Client Username.

```java
public void removingObjectUsingDelete() {

    try {
        String clientUsername = "tutorialUser";
        SempMetaOnlyResponse resp = sempApiInstance.deleteMsgVpnClientUsername(msgVpn, clientUsername);
        System.out.println("Client Username delete. Resp: " + resp.getMeta().getResponseCode());

    } catch (ApiException e) {
        handleError(e);
    }
}
```

[*Source Reference: removingObjectUsingDelete() from BasicOperationsSample*]({{ site.repository }}/blob/master/java/src/main/java/com/solace/samples/BasicOperationsSample.java){:target="_blank"}

## Summary

The full source code for this example is available in [GitHub]({{ site.repository }}){:target="_blank"}. If you combine the example source code shown above results in the following source:

*   [BasicOperationsSample.java]({{ site.repository }}/blob/master/java/src/main/java/com/solace/samples/BasicOperationsSample.java){:target="_blank"}

### Getting the Source

Clone the GitHub repository containing the Solace samples.

```
git clone {{ site.repository }}
cd {{ site.repository | split: '/' | last}}/java
```

### Building

The project uses Gradle. To build, execute the following command.

```
../gradlew build
```

This builds all of the Java Samples with OS specific launch scripts. The files are staged in the `build/staged` directory.

### Running the Sample

You start the `BasicOperationsSample` with four arguments:

1. The SEMP Base Path. For example: `http://solacevmr:8080/SEMP/v2/config`.
2. The Message VPN.
3. The Management Username.
4. The Management Password.

For example:

```
$ ./build/staged/bin/basicOperationsSample <SEMP_BASE_PATH> <MESSAGE_VPN> <MANAGEMENT_USER> <MANAGEMENT_PASSWORD>
BasicOperationsSample initializing...
SEMP initializing: <SEMP_BASE_PATH>, <MANAGEMENT_USER>
Creating Object: tutorialUser
...
Client Username delete. Resp: 200
```

At this point, you have created, retrieved, updated and deleted a Client Username object using SEMP. The examples used a generated client library in Java to interact with the Solace Messaging, but you can adapt the steps to any programming language of your choice.

SEMP is an extensive API that lets you configure anything on your Solace Messaging so there is a lot more to understand. If you want to know more, you can either get more familiar with the SEMP concepts by checking out the [Concepts Guide]({{ site.docs-concepts }}){:target="_top"} or you can see the full [developer documentation for the API]({{ site.docs-api }}){:target="_top"}.
