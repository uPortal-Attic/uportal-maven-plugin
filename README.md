uportalData-maven-plugin
=================

Maven Plugin to add several custom lifecycle phases to Maven for uPortal database
operations such as
initialize database tables, import data, or export data.  Allows uPortal pom or
a portlet pom to do an operation such as
mvn -Ddir=src/main/data data-import

In the pom you can reference any type of plug-in you want (hibernate3, groovy, ant, etc.)
with the custom lifecycle phase to execute the plug-in.  For example see the integration test
pom in this project at src/it/simple-it/pom.xml


