Timeskip: A RESTful API for Timesheet Management
==============================================================================================

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/e3b950eaefc3405db14fc9d2f55a6707)](https://www.codacy.com/app/Switch2IT/timeskip-api?utm_source=github.com&utm_medium=referral&utm_content=Switch2IT/timeskip-api&utm_campaign=badger)

Author: Christophe Devos, Stephan Ghequiere, Timothy Pecceu, Patrick Van den Bussche, Guillaume Vandecasteele, Olivier Vanderstede.
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

4. Copy the /target/timeskip-ear.ear to the deployment folder of the Wildfly server.


Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/timeskip-web/api>. The Swagger UI can be found at <http://localhost:8080/timeskip-web/api> and the Swagger JSON definition can be downloaded at <http://localhost:8080/timeskip-web/api/swagger.json>
        
## Release Notes

### v0.6.1

#### Bugfixes

* Adjusted basic import script for errors with auto-increment values
* Fixed permission bug where permission was wrongfully denied for projects

### v0.6

#### Upgrade path

Execute the v0.6-update.ddl script in the `timeskip-ejb/src/main/resources/ddl` folder

#### Additions

* Scheduling service for monthly reminder e-mail & prefilling timesheets with user defaults.
* JUnit tests.
* JWT now allowed as query string parameter in addition of previous headers.
* Additional endpoints for querying worklogs on a user basis.

#### Bugfixes

* Storage casting issue between Data and LocalDate.
* Validation that from-date comes before to-date.
* To-date is now inclusive.
* Logged Minutes cannot exceed amount of minutes in a day.
* Loosened CORS filter to allow PATCH method and additional headers.
* Updating user in API or on IDP now updates the other as well.
* When changing the user's default activity, the user is automatically assigned to that activity's project.

### v0.5

#### Upgrade path

Adjust the `application.conf` file in your configuration folder to mirror the example in the `timeskip-ejb/src/main/resources/` folder. If you want PDF reports to bear a watermark, place the logo image in a folder of your choosing, and set the path in the configuration file.

#### Additions

* Roles CRUD endpoints.
* `GET` `/users` now allows filtering the results with querystring parameters. Accepted parameters are `organization`, `id`, `role`, `firstname`, `lastname` and `email`.
* PDF Report endpoints are now functional. Possibility to set an image as a logo.
* JWT token can now be sent along requests in multiple headers: `keycloak-token`, `access_token` and `Authorization`.

#### Bugfixes

* Converting String to Date no longers uses dummy data.
* Added `keycloak-token`, `access_token` and `Authorization` to the CORS filter's `Access-Control-Allow-Headers` header

### v0.4

#### Upgrade path

When upgrading from v0.3, please execute the `v0.4-update.ddl` script that can be found in the `timeskip-ejb/src/main/resources/ddl` folder

#### Additions

* Backup/Restore endpoints.
* Billing report endpoint.
* Logged time report endpoint.
* User logged time report endpoint.
* Current user logged time report endpoint.
* Overtime report endpoint.
* Undertime report endpoint.

#### Bugfixes

* User now deleted on IDP when deleted from the Timeskip datastore.
* Foreign keys update, changes now cascade on delete as well as update.

### v0.3

#### Upgrade path

When upgrading from v0.2, please execute the `v0.3-update.ddl` script that can be found in `timeskip-ejb/src/main/resources/ddl` folder.

#### Additions

* Mail service.
* Configuration endpoints.
* User CRUD.
* Membership CRUD.
* Mail templates.
* Startup service.
* Server restart now sends mail to configurable e-mail address.


#### Bugfixes

* Added missing auto-increment property to primary keys.
* Fixed issue where user could log work hours on unassigned project.
* Fixed access issue when querying numerical ID not belonging to containing entity.

### v0.2

* Added CRUD endpoints for organizations, projects, activities and worklogs.
* Added user-related endpoints (get current user, parse JWT, create user).
* Keycloak IDP integration.
* System status endpoint.

### v0.1

Initial release.