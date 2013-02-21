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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Config object for the {@link EarDeployer}. Specifies where the EAR resides.
 *  
 * @author Eric Dalquist
 */
public class DeployerConfig {
    private File earLocation;
    private String deployDestination;
    private boolean extractWars = true;
    private boolean removeExistingWebappDirectories = false;
    private boolean cleanSharedDirectory = true;
    private Map<String, String> deployerParameters = new LinkedHashMap<String, String>(0);

    public File getEarLocation() {
        return this.earLocation;
    }
    /**
     * @param earLocation EAR File to deploy
     */
    public void setEarLocation(File earLocation) {
        this.earLocation = earLocation;
    }
    
    public boolean isExtractWars() {
        return this.extractWars;
    }
    /**
     * @param extractWars If true WAR files will be extracted during deployment, defaults to true
     */
    public void setExtractWars(boolean extractWars) {
        this.extractWars = extractWars;
    }
    
    public String getDeployDestination() {
        return deployDestination;
    }
    /**
     * @param deployDestination Destination to deploy the EAR to
     */
    public void setDeployDestination(String deployDestination) {
        this.deployDestination = deployDestination;
    }
    
    public boolean isRemoveExistingWebappDirectories() {
        return removeExistingWebappDirectories;
    }
    /**
     * @param removeExistingWebappDirectories If conflicting webapp directories should be removed during deployment, defaults to true
     */
    public void setRemoveExistingWebappDirectories(boolean removeExistingWebappDirectories) {
        this.removeExistingWebappDirectories = removeExistingWebappDirectories;
    }
    
    public boolean isCleanSharedDirectory() {
        return cleanSharedDirectory;
    }
    /**
     * @param cleanSharedDirectory If the shared library directory should be purged before deployment, defaults to true
     */
    public void setCleanSharedDirectory(boolean cleanSharedDirectory) {
        this.cleanSharedDirectory = cleanSharedDirectory;
    }
    
    public Map<String, String> getDeployerParameters() {
        return deployerParameters;
    }
    /**
     * @param deployerParameters Parameters specific to the {@link EarDeployer} implementation
     */
    public void setDeployerParameters(Map<String, String> deployerParameters) {
        this.deployerParameters = deployerParameters;
    }
    
    
    @Override
    public String toString() {
        return "DeployerConfig [earLocation=" + earLocation
                + ", deployDestination=" + deployDestination + ", extractWars="
                + extractWars + ", removeExistingWebappDirectories="
                + removeExistingWebappDirectories + ", cleanSharedDirectory="
                + cleanSharedDirectory + ", deployerParameters="
                + deployerParameters + "]";
    }
}
