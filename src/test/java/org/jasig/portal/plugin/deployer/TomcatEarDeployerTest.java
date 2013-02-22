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
