package org.jasig.portal.plugin.mojo;

import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.component.annotations.Requirement;
import org.jasig.portal.plugin.deployer.DeployerConfig;
import org.jasig.portal.plugin.deployer.EarDeployer;

/**
 * Base class for ear deployment mojos 
 * 
 * @author Eric Dalquist
 */
public abstract class AbstractDeployEarMojo extends AbstractMojo {
    /**
     * Component to deploy ear files
     */
    @Requirement(role = EarDeployer.class)
    private Map<String, EarDeployer> earDeployers;
    
    /**
     * Role hint of the {@link org.jasig.portal.plugin.deployer.EarDeployer} implementation to use.
     */
    @Parameter(defaultValue = "tomcat", property = "earDeployerId")
    private String earDeployerId;
    
    /**
     * Destination for EAR deployment
     */
    @Parameter(required = true)
    private String deployDestination;
    
    /**
     * If the war files within the ear should be extracted during deploy
     */
    @Parameter(defaultValue = "true")
    private boolean extractWars = true;
    
    /**
     * If true remove conflicting webapp directories before deploying instead of deploying on top of them 
     */
    @Parameter(defaultValue = "true")
    private boolean removeExistingWebappDirectories = false;
    
    /**
     * If the shared library directory should have all existing files removed before deploying 
     */
    @Parameter(defaultValue = "true")
    private boolean cleanSharedDirectory = true;
    
    /**
     * Arbitrary deployer parameters, see the implementation of {@link EarDeployer} you are using for a parameter
     * reference. 
     */
    @Parameter
    private Map<String, String> deployerParameters;

    protected final EarDeployer getEarDeployer() throws MojoExecutionException {
        final EarDeployer earDeployer = this.earDeployers.get(this.earDeployerId);
        if (earDeployer == null) {
            throw new MojoExecutionException("No " + EarDeployer.class.getSimpleName() + " found for 'earDeployerId'=" + this.earDeployerId);
        }
        return earDeployer;
    }

    protected final DeployerConfig getDeployerConfig() {
        final DeployerConfig deployerConfig = new DeployerConfig();
        deployerConfig.setCleanSharedDirectory(cleanSharedDirectory);
        deployerConfig.setDeployDestination(deployDestination);
        deployerConfig.setDeployerParameters(deployerParameters);
        deployerConfig.setExtractWars(extractWars);
        deployerConfig.setRemoveExistingWebappDirectories(removeExistingWebappDirectories);
        return deployerConfig;
    }
}