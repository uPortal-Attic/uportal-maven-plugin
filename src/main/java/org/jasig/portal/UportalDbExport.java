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

// FIXME See comment in UportalDbImport

/**
 * Goal which spawns standard lifecycle up to package phase to insure all files are
 * prepared to begin exporting data from the db.
 *
 * @goal dbexport
 * @phase data-export
 * @execute lifecycle="default" phase="package"
 */
public class UportalDbExport
    extends AbstractMojo
{
    public void execute()
        throws MojoExecutionException
    {
        getLog().debug( "UportalDbExport - doing nothing here" );
    }
}
