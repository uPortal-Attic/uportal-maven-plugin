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
 * Maven {@link AbstractMojo} to invoke {@link IResourcesAggregator#aggregate(File, File)}.
 * 
 * You must specify the skinConfigurationFile property, points to the "skin.xml"
 * file you wish to aggregate.
 *
 * @goal aggregate
 * @author Nicholas Blair, npblair@wisc.edu
 *
 */
public class SkinResourcesAggregatorMojo extends AbstractMojo {

	/**
	 * @parameter 
	 * @required
	 */
	private File skinConfigurationFile;
	
	/**
	 * @parameter
	 * @required
	 */
	private File outputRootDirectory;

	/* (non-Javadoc)
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			ResourcesAggregatorImpl aggr = new ResourcesAggregatorImpl();
			aggr.aggregate(skinConfigurationFile, outputRootDirectory);
			
		} catch (AggregationException e) {
			throw new MojoExecutionException("aggregation failed", e);
		} catch (IOException e) {
			throw new MojoExecutionException("IOException occurred", e);
		}


	}

}
