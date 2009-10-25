/**
 * 
 */
package org.jasig.portal.plugin.mojo;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jasig.portal.web.skin.AggregationException;
import org.jasig.portal.web.skin.IResourcesAggregator;
import org.jasig.portal.web.skin.ResourcesAggregatorImpl;

/**
 * @author Nicholas Blair, nblair@doit.wisc.edu
 *
 */
public class SkinResourcesAggregatorMojo extends AbstractMojo {

	/**
	 * @parameter 
	 * @required
	 */
	private File skinConfigurationFile;
	
	/**
	 * @parameter expression ${project.build.outputDirectory}
	 */
	private File outputRootDirectory;

	/* (non-Javadoc)
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			IResourcesAggregator aggr = new ResourcesAggregatorImpl();
			aggr.aggregate(skinConfigurationFile, outputRootDirectory);
			
		} catch (AggregationException e) {
			throw new MojoExecutionException("aggregation failed", e);
		} catch (IOException e) {
			throw new MojoExecutionException("IOException occurred", e);
		}


	}

}
