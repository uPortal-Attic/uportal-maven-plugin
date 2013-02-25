# uportal-maven-plugin

Adds goals and lifecycle phases used by uPortal's build and deploy process.

## Goals

[Maven Site](http://developer.jasig.org/projects/uportal-maven-plugin/1.0.0/plugin-info.html)

## Lifecycle Phases

Adds `db-init`, `data-import`, `data-export` lifecycle phases. Allows for portlet overlays to bind plugin executions to these phases.

Example: `mvn -Ddir=src/main/data package data-import`

Example Project: `src/it/simple-it/pom.xml`

NOTE:  The 'package' goal is needed at this point to insure the build occurs.  The
plug-in currently does not spawn the package goal to build the project if just the
data-import or other new custom lifecycle goals are invoked.
