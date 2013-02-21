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
import java.io.IOException;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.component.annotations.Component;

/**
 * Encapsulates Tomcat specific logic for deploying WARs and JARs from the uPortal EAR. This {@link EarDeployer}
 * implementation treats the <code>deployDestination</code> as CATALINA_BASE
 * <br/>
 * Supports the following <code>deployerParameters</code>:
 * <ul>
 *  <li>webAppsDir - Defaults to <code>${deployDestination}/shared/lib</code></li>
 *  <li>sharedLibDir - Defaults to <code>${deployDestination}/webapps</code></li>
 * </ul>
 * 
 * 
 * @author Eric Dalquist
 */
@Component(role=EarDeployer.class, hint="tomcat")
public class TomcatEarDeployer extends AbstractExtractingEarDeployer {
    private static final String WEB_APPS_DIR_PARAM = "webAppsDir";
    private static final String SHARED_LIB_DIR_PARAM = "sharedLibDir";

    private File getWebAppsDir(DeployerConfig config) {
        return getFileParam(config, WEB_APPS_DIR_PARAM, "webapps");
    }
    private File getSharedLibDir(DeployerConfig config) {
        return getFileParam(config, SHARED_LIB_DIR_PARAM, "shared/lib");
    }
    private File getFileParam(DeployerConfig config, String param, String defaultLocation) {
        final Map<String, String> deployerParameters = config.getDeployerParameters();
        String paramLocation = deployerParameters.get(param);
        if (paramLocation == null) {
            paramLocation = defaultLocation;
        }
        
        if (paramLocation.startsWith("/")) {
            return new File(paramLocation);
        }
        
        return new File(config.getDeployDestination(), paramLocation);
    }
    
    /**
     * Writes the WAR to Tomcat's webapps directory, as specified by {@link TomcatDeployerConfig#getCatalinaWebapps()}.
     */
    @Override
    protected final void deployWar(WebModule webModule, JarFile earFile, DeployerConfig deployerConfig) throws MojoFailureException {
        final String webUri = webModule.getWebUri();
        final JarEntry warEntry = earFile.getJarEntry(webUri);
        final File webappsDir = getWebAppsDir(deployerConfig);
        
        String contextName = webModule.getContextRoot();
        if (contextName.endsWith(".war")) {
            contextName = contextName.substring(contextName.length() - 4);
        }
        if (contextName.startsWith("/")) {
            contextName = contextName.substring(1);
        }
        
        if (deployerConfig.isRemoveExistingWebappDirectories()) {
            final File contextDir = new File(webappsDir, contextName);
            
            if (contextDir.exists()) {
                try {
                    FileUtils.deleteDirectory(contextDir);
                }
                catch (IOException e) {
                    throw new MojoFailureException("Failed to remove webapp context directory: " + contextDir, e);
                }
            }
        }
        
        if (deployerConfig.isExtractWars()) {
            final File contextDir = new File(webappsDir, contextName);
            this.extractWar(earFile, warEntry, contextDir);
        }
        else {
            final String warName = contextName += ".war";
            File warDest;
            try {
                warDest = this.createSafeFile(webappsDir, warName);
            }
            catch (IOException e) {
                throw new MojoFailureException("Failed to setup File to deploy '" + warName + "' to '" + webappsDir + "'", e);
            }
            this.copyAndClose(warEntry, earFile, warDest);
        }
    }

    /**
     * Writes the JAR to Tomcat's shared/lib directory, as specified by {@link TomcatDeployerConfig#getCatalinaShared()}.
     */
    @Override
    protected final void deployJar(JarEntry jarEntry, JarFile earFile, DeployerConfig deployerConfig) throws MojoFailureException {
        final String jarName = jarEntry.getName();
        
        if (jarName.contains("/")) {
            throw new IllegalArgumentException("The EAR contains a JAR entry in a folder, this is not supported. Bad Jar: '" + jarName + "'");
        }
        
        final File sharedLibDir = getSharedLibDir(deployerConfig);
        final File jarDest;
        try {
            jarDest = this.createSafeFile(sharedLibDir, jarName);
        }
        catch (IOException e) {
            throw new MojoFailureException("Failed to setup File to deploy '" + jarName + "' to '" + sharedLibDir + "'", e);
        }
        
        this.copyAndClose(jarEntry, earFile, jarDest);
    }
}
