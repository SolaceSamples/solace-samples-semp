# Getting Started Examples - Java

## Solace Element Management Protocol (SEMP)

These tutorials will get you up to speed and managing Solace message routers with SEMP as quickly as possible using the SEMP Java Client Library. 

If you are new to SEMP, you can learn more here for full documentation and API reference:

* https://docs.solace.com/SEMP/SEMP-Concepts.htm

## Contents

This directory contains code for different basic scenarios in Java. It is best to view the associated [tutorials home page](http://dev.solace.com/get-started/semp-tutorials/).

## Prerequisites

There are no prerequisites. 

## Build the the project from source

Starting from the current `java` directory:    

```
../gradlew build
```

This will generate the required SEMP Java Client Library if it doesn't exist yet and build the samples.

## Running the Samples

To try individual samples, build the project from source and then run samples like the following:

```
./build/staged/bin/basicOperationsSample <SEMP_BASE_PATH> <SEMP_USER> <SEMP_PASSWORD>
```

See the tutorials linked from the [tutorials home page](http://dev.solace.com/get-started/semp-tutorials/) for full details which can walk you through the samples, what they do, and how to correctly run them to explore SEMP.

## Exploring the Samples

### Setting up your preferred IDE

Using a modern Java IDE provides cool productivity features like auto-completion, on-the-fly compilation, assisted refactoring and debugging which can be useful when you're exploring the samples and even modifying the samples. Follow the steps below for your preferred IDE.

#### Using Eclipse

To generate Eclipse metadata (.classpath and .project files), do the following:

```
./gradlew eclipse
```

Once complete, you may then import the projects into Eclipse as usual:

 *File -> Import -> Existing projects into workspace*

Browse to the *'solace-samples-semp'* root directory. All projects should import
free of errors.

#### Using IntelliJ IDEA

To generate IDEA metadata (.iml and .ipr files), do the following:

```
./gradlew idea
```

## Contributing

Refer to the README.md in the parent folder on how to contribute to the project.