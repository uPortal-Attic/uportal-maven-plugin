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

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class TomcatEarDeployerTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testDeployDirect() throws Exception {
        final DeployerConfig config = new DeployerConfig();

        final URL testEarUrl = this.getClass().getResource("/org/jasig/portal/tools/deployer/test.ear");
        final File testEar = new File(testEarUrl.getFile());
        config.setEarLocation(testEar);
        
        final File tomcatDest = folder.newFolder("tomcat");
        config.setDeployDestination(tomcatDest);
        
        final TomcatEarDeployer deployer = new TomcatEarDeployer();
        //TODO use logback hook instead of system.out
        deployer.enableLogging(new ConsoleLogger());
        deployer.deploy(config);
            
        final File expectedSharedJar = new File(tomcatDest, "/shared/lib/shared.jar");
        assertTrue("Expected file '" + expectedSharedJar + "' does not exist.", expectedSharedJar.exists());

        final File expectedWar1 = new File(tomcatDest, "webapps/WarDeployerTestPortlet1");
        assertTrue("Expected file '" + expectedWar1 + "' does not exist.", expectedWar1.exists());

        final File expectedWar2 = new File(tomcatDest, "webapps/WarDeployerTestPortlet2");
        assertTrue("Expected file '" + expectedWar2 + "' does not exist.", expectedWar2.exists());
    }
}
