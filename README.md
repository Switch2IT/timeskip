Timeskip: A RESTful API for Timesheet Management
==============================================================================================
Author: Christophe Devos, Stephan Ghequiere, Timothy Pecceu, Patrick Van den Bussche, Guillaume Vandecasteele, Olivier Vanderstede
Level: Intermediate
Technologies: EAR, JPA, Swagger
Summary: Based on the Wildfly EE Web Application archetype

What is it?
-----------

A Java RESTful API for timesheet management.

System requirements
-------------------

All you need to build this project is Java 7.0 (Java SDK 1.7) or better, Maven 3.1 or better.

The application this project produces is designed to be run on JBoss WildFly.

 
Configure Maven
---------------

If you have not yet done so, you must [Configure Maven](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/CONFIGURE_MAVEN.md) before testing the quickstarts.


Start JBoss WildFly with the Web Profile
-------------------------

1. Open a command line and navigate to the root of the JBoss server directory.
2. The following shows the command line to start the server with the web profile:

        For Linux:   JBOSS_HOME/bin/standalone.sh
        For Windows: JBOSS_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

_NOTE: The following build command assumes you have configured your Maven user settings. If you have not, you must include Maven setting arguments on the command line. See [Build and Deploy the Quickstarts](https://github.com/jboss-developer/jboss-eap-quickstarts#build-and-deploy-the-quickstarts) for complete instructions and additional options._

1. Make sure you have started the JBoss Server as described above.
2. Open a command line and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install

4. Copy the /target/timeskip-ear.ear to the deployment folder of the Wildfly server


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/timeskip-web>.
        
## Release Notes

### v0.3

#### Upgrade path

When upgrading from v0.2, please execute the `v0.3-update.ddl` script that can be found in `timeskip-ejb/src/main/resources/ddl` folder

#### Additions

* Mail service
* Configuration endpoints
* User CRUD
* Membership CRUD
* Mail templates
* Startup service
* Server restart now sends mail to configurable e-mail address


#### Bugfixes

* Added missing auto-increment property to primary keys
* Fixed issue where user could log work hours on unassigned project
* Fixed access issue when querying numerical ID not belonging to containing entity

### v0.2

* Added CRUD endpoints for organizations, projects, activities and worklogs
* Added user-related endpoints (get current user, parse JWT, create user)
* Keycloak IDP integration
* System status endpoint

### v0.1

Initial release