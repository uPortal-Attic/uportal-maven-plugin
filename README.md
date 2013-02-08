uportalData-maven-plugin
=================

Maven Plugin to add several custom phases to Maven for uPortal database operations to
initialize database tables, import data, or export data.  Allows uPortal pom or
a portlet pom to do an operation such as
mvn -Ddir=src/main/data data-import

and in the pom associate any type of plug-in you want (hibernate3, groovy, ant, etc.)
with the custom phase to allow it to execute.  For example see the integration test
pom in this project at src/it/simple-it/pom.xml


