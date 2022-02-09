# Getting Started Examples - Ruby

## Solace Element Management Protocol (SEMP)

These tutorials will get you up to speed and managing Solace message routers with SEMP as quickly as possible using the SEMP Ruby Client Library. 

If you are new to SEMP, you can learn more here for full documentation and API reference:

* https://docs.solace.com/SEMP/SEMP-Concepts.htm

## Contents

This directory contains code for different basic scenarios in Java. It is best to view the associated [tutorials home page](http://dev.solace.com/get-started/semp-tutorials/).

## Prerequisites

The only prerequisite is Ruby installed.

## Building and Installing the Samples

Starting from the current `ruby` directory:    

Generate the latest version of the SEMP Ruby Client Library if needed (this step requires Java installed):

```
../gradlew build
```

Then build and install the library:

```
gem build sempclient_samplelib.gemspec
gem install --dev ./sempclient_samplelib-1.0.0.gem
```
(or `sudo gem install --dev ./sempclient_samplelib-1.0.0.gem` to install the gem for all users)

## Running the Samples

To try the samples, run them like the following:

```
ruby -Ilib samples/manage_vpn.rb create <host:port> <semp-username> <semp-password> myNewVPN
```
E.g.:
```
ruby -Ilib samples/manage_vpn.rb create http://localhost:8080/SEMP/v2/config admin admin test
```

See the individual tutorials linked from the [tutorials home page](https://tutorials.solace.dev/semp) for full details which can walk you through the samples, what they do, and how to correctly run them to explore SEMP.

## Contributing

Refer to the README.md in the parent folder on how to contribute to the project.
