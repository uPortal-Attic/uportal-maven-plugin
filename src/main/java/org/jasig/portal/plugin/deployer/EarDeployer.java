package org.jasig.portal.plugin.deployer;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


public interface EarDeployer {
    void deploy(DeployerConfig config) throws MojoExecutionException, MojoFailureException;
}
