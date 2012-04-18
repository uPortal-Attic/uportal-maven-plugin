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
package org.jasig.portal.plugin.deployer.config;

import java.io.File;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jasig.portal.plugin.deployer.DeployerConfig;

/**
 * Deployer configuration with tomcat specific properties. tomcatHome is
 * where 'common' JARs go, tomcatBase is where WARs and 'shared' JARs go.
 * 
 * @author Eric Dalquist
 * @version $Revision$
 */
public class TomcatDeployerConfig extends DeployerConfig {
    private File webAppsDir;
    private File jarDir;
    

    /**
     * @return the webAppsDir
     */
    public File getWebAppsDir() {
        return webAppsDir;
    }
    /**
     * @param webAppsDir the webAppsDir to set
     */
    public void setWebAppsDir(File webAppsDir) {
        this.webAppsDir = webAppsDir;
    }
    /**
     * @return the jarDir
     */
    public File getJarDir() {
        return jarDir;
    }
    /**
     * @param jarDir the jarDir to set
     */
    public void setJarDir(File jarDir) {
        this.jarDir = jarDir;
    }
    

    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof DeployerConfig)) {
            return false;
        }
        TomcatDeployerConfig rhs = (TomcatDeployerConfig)object;
        return new EqualsBuilder()
            .appendSuper(super.equals(object))
            .append(this.webAppsDir, rhs.webAppsDir)
            .append(this.jarDir, rhs.jarDir)
            .isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(-110713495, -1544877739)
            .appendSuper(super.hashCode())
            .append(this.webAppsDir)
            .append(this.jarDir)
            .toHashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("webAppsDir", this.webAppsDir)
            .append("jarDir", this.jarDir)
            .appendSuper(super.toString())
            .toString();
    }
}
