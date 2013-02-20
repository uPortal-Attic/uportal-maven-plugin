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
package org.jasig.portal.plugin.mojo;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;

public abstract class AbstractTomcatMojo extends AbstractMojo {
	
    /**
     * @parameter expression="${maven.tomcat.home}/webapps"
     */
    private String webAppsDir;
    
    /**
     * @parameter expression="${maven.tomcat.home}/shared/lib"
     */
    private String jarDir;
    
    /**
     * @parameter expression="${maven.tomcat.home}"
     * @required
     */
    private File catalinaBase;
    
    //TODO seperate out home vs base, look at uportal build for how home vs base are currently being used

	public String getWebAppsDir() {
		return this.webAppsDir;
	}

	public void setWebAppsDir(String webAppsDir) {
		this.webAppsDir = webAppsDir;
	}

	public String getJarDir() {
		return this.jarDir;
	}

	public void setJarDir(String jarDir) {
		this.jarDir = jarDir;
	}

	public File getCatalinaBase() {
		return this.catalinaBase;
	}

	public void setCatalinaBase(File catalinaBase) {
		this.catalinaBase = catalinaBase;
	}
    
}
