# Liferay Faces ADF

This is a library that contains ADF compatibility features for use with
[Liferay Faces](http://www.liferay.com/community/liferay-projects/liferay-faces/overview).

## License

Liferay Enterprise Subscription License 

## News

For the latest news and updates, follow [@liferayfaces](https://twitter.com/liferayfaces).

## Modules

This project contains the following modules that can be used as dependencies in a portlet:

1) Base module that is required for all ADF portlets:

    groupId: com.liferay.faces.adf
    artifactId: com.liferay.faces.adf.base

2) Binding module that is required for portlets that require ADF TaskFlows:

    groupId: com.liferay.faces.adf
    artifactId: com.liferay.faces.adf.binding

## Installing Dependencies

In order to build this project, it is necessary to download and install the ADF [Application Development
Runtime](https://www.oracle.com/tools/downloads/application-development-framework-downloads.html) and then to populate
your local $HOME/.m2/repository according to the instructions found in section 5.3 of the document titled [Populating
the Maven Repository Manager](https://docs.oracle.com/middleware/1212/core/MAVEN/config_maven.htm#MAVEN311).

## Building From Source

Using [Maven](https://maven.apache.org/) 3.x:

	mvn clean install
