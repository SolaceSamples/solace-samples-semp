# Getting Started Examples
## Solace Element Management Protocol (SEMP)

These tutorials will get you up to speed and managing Solace message routers with SEMP as quickly as possible. 

If you are new to SEMP, you can learn more here for full documentation and API reference:

* http://docs.solacesystems.com/SEMP/SEMP-Home.htm

These tutorials apply to the SEMP API starting in version 2. For older versions of the SEMP API, you can see the [Legacy SEMP Documentation](http://docs.solacesystems.com/SEMP/Using-SEMP-to-Manage-and-Monitor-Routers.htm)

**Note**: Currently SEMP v2 is only supported on Solace message router appliances running SolOS version 7.2.2 and above. Solace virtual message router will add SEMP v2 support in the near future.

## Contents

This repository contains code and matching tutorial walk throughs for different basic scenarios. It is best to view the associated [tutorials landing page](https://solacesamples.github.io/solace-samples-semp/).

## Prerequisites

There are no prerequisites. 

## Build the Samples

    ./gradlew build

## Running the Samples

To try individual samples, build the project from source and then run samples like the following:

    ./build/staged/bin/javaClientSample <SEMP_BASE_PATH> <SEMP_USER> <SEMP_PASSWORD>

See the individual tutorials linked from the [tutorials landing page](https://solacesamples.github.io/solace-samples-semp/) for full details which can walk you through the samples, what they do, and how to correctly run them to explore SEMP.

## Exploring the Samples

### Setting up your preferred IDE

Using a modern Java IDE provides cool productivity features like auto-completion, on-the-fly compilation, assisted refactoring and debugging which can be useful when you're exploring the samples and even modifying the samples. Follow the steps below for your preferred IDE.

#### Using Eclipse

To generate Eclipse metadata (.classpath and .project files), do the following:

    ./gradlew eclipse

Once complete, you may then import the projects into Eclipse as usual:

 *File -> Import -> Existing projects into workspace*

Browse to the *'solace-samples-semp'* root directory. All projects should import
free of errors.

#### Using IntelliJ IDEA

To generate IDEA metadata (.iml and .ipr files), do the following:

    ./gradlew idea

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

<<<<<<< HEAD
See the list of [contributors](https://github.com/SolaceSamples/solace-samples-semp/contributors) who participated in this project.
=======
See the list of [contributors](https://github.com/SolaceSamples/solace-samples-template/contributors) who participated in this project.
>>>>>>> samples-template/master

## License

This project is licensed under the Apache License, Version 2.0. - See the [LICENSE](LICENSE) file for details.

## Resources

For more information try these resources:

<<<<<<< HEAD
- SEMP Documentation: http://docs.solacesystems.com/SEMP/SEMP-Home.htm
- The Solace Developer Portal website at: [http://dev.solacesystems.com](http://dev.solacesystems.com/)
- Get a better understanding of [Solace technology.](http://dev.solacesystems.com/tech/)
- Check out the [Solace blog](http://dev.solacesystems.com/blog/) for other interesting discussions around Solace technology
- Ask the [Solace community.](http://dev.solacesystems.com/community/)
=======
- The Solace Developer Portal website at: http://dev.solace.com
- Get a better understanding of [Solace technology](http://dev.solace.com/tech/).
- Check out the [Solace blog](http://dev.solace.com/blog/) for other interesting discussions around Solace technology
- Ask the [Solace community.](http://dev.solace.com/community/)
>>>>>>> samples-template/master
