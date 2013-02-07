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

/**
 * Goal which spawns standard lifecycle up to package phase to import database into the db
 *
 * @goal dbimport
 * @execute lifecycle="default" phase="package"
 */
//* @execute lifecycle="uportalDb" phase="data-import"
//@Mojo( name = "dbimport")
public class UportalDbMojo
    extends AbstractMojo
{
    public void execute()
        throws MojoExecutionException
    {
        getLog().info( "UportalDbMojo - doing nothing here" );
    }
}
