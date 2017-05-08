# Getting Started Examples - Python

## Solace Element Management Protocol (SEMP)

These tutorials will get you up to speed and managing Solace message routers with SEMP as quickly as possible using the SEMP Python Client Library. 

If you are new to SEMP, you can learn more here for full documentation and API reference:

* http://docs.solace.com/SEMP/SEMP-Home.htm

## Contents

This directory contains code for different basic scenarios in Java. It is best to view the associated [tutorials home page](http://dev.solace.com/get-started/semp-tutorials/).

## Prerequisites

The samples use Python 2.7. Building and installing use the Python [setuptools]({{ site.python-setuptools }}){:target="_top"}.

## Building and Installing the Samples

Starting from the current `python` directory:    

```
python setup.py install --user
```
(or `sudo python setup.py install` to install the package for all users)

## Running the Samples

To try the samples, run them like the following:

```
python samples/manage_vpn.py create <host:port> myNewVPN
```

See the individual tutorials linked from the [tutorials home page](http://dev.solace.com/get-started/semp-tutorials/) for full details which can walk you through the samples, what they do, and how to correctly run them to explore SEMP.

## Contributing

Refer to the README.md in the parent folder on how to contribute to the project.