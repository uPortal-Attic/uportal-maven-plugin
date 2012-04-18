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
package org.jasig.portal.plugin.deployer;

import java.io.File;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Base config object for the {@link AbstractEarDeployer}. Specifies where the EAR resides,
 * {@link AbstractEarDeployer} impls will likely extend this class to provide container specific
 * deployment configuration.
 * <br>
 * <br>
 * 
 * Properties:
 * <ul>
 *  <li>earLocation - The location of the EAR file to deploy.</li>
 *  <li>extractWars - If the WARs should be extracted when deployed.</li>
 *  <li>removeExistingDirectories - If extracted WAR directories exist should they be removed before deploying.</li>
 * </ul>
 *  
 * @author Eric Dalquist
 * @version $Revision$
 */
public class DeployerConfig {
    private File earLocation;
    private boolean extractWars = false;
    private boolean removeExistingDirectories = false;

    public File getEarLocation() {
        return this.earLocation;
    }
    public void setEarLocation(File earLocation) {
        this.earLocation = earLocation;
    }
    public boolean isExtractWars() {
        return this.extractWars;
    }
    public void setExtractWars(boolean extractWars) {
        this.extractWars = extractWars;
    }
    public boolean isRemoveExistingDirectories() {
        return this.removeExistingDirectories;
    }
    public void setRemoveExistingDirectories(boolean removeExistingDirectories) {
        this.removeExistingDirectories = removeExistingDirectories;
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
        DeployerConfig rhs = (DeployerConfig)object;
        return new EqualsBuilder()
            .append(this.earLocation, rhs.earLocation)
            .append(this.extractWars, rhs.extractWars)
            .append(this.removeExistingDirectories, rhs.removeExistingDirectories)
            .isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(-110713495, -1544877739)
            .append(this.earLocation)
            .append(this.extractWars)
            .append(this.removeExistingDirectories)
            .toHashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("earLocation", this.earLocation)
            .append("extractWars", this.extractWars)
            .append("removeExistingDirectories", this.removeExistingDirectories)
            .toString();
    }
}
