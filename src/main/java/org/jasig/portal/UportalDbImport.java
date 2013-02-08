package org.jasig.portal;

/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

// FIXME I could not figure out how to automatically get an invocation of
// mvn data-import
// to automatically build the project up to the package phase.  I believe this is
// because I could not get the mojo associated to the data-import phase.
// From what I read the mojo's @phase annotation or the components.xml
// default-phases element should tie them together.  Then I expect the mojo's
// @execute would cause the dependency upon
// the package phase in the default lifecycle.  If you invoked the mojo explicitely;
// e.g. mvn org.jasig.portal:uportalData-maven-plugin:1.0:dbimport
// the dependency upon package is respected.
//
// For now users can use
// mvn package data-import

/**
 * Goal which spawns standard lifecycle up to package phase to insure all files are
 * prepared to begin importing data into the db.
 *
 * @goal dbimport
 * @phase data-import
 * @execute lifecycle="default" phase="package"
 */
public class UportalDbImport
    extends AbstractMojo
{
    public void execute()
        throws MojoExecutionException
    {
        getLog().debug( "UportalDbImport - doing nothing here" );
    }
}
