package org.jasig.portal.plugin.mojo;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * @author jdb53
 * $Revision$
 *
 * @goal clean-shared
 */
public class CleanSharedMojo extends AbstractTomcatMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
		File shared = new File(getJarDir());
		
		// delete each file in the shared directory
		File[] files = shared.listFiles();
		for (File file : files) {
			file.delete();
		}
	}

}
