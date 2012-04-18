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
package org.jasig.portal.plugin.mojo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;

public abstract class AbstractTomcatWarDeployerMojo extends AbstractTomcatMojo {

    /** @component */
    protected ArtifactFactory artifactFactory;

    /** @component */
    protected ArtifactResolver resolver;

    /**@parameter expression="${localRepository}" */
    protected ArtifactRepository localRepository;

    /** @parameter expression="${project.remoteArtifactRepositories}" */
    protected List remoteRepositories;

    /**
     * To look up Archiver/UnArchiver implementations
     * 
     * @parameter expression="${component.org.codehaus.plexus.archiver.manager.ArchiverManager}"
     * @required
     */
    protected ArchiverManager archiverManager;

	protected void extractWar(File warfile, File contextDirectory, String contextName) throws NoSuchArchiverException, ArchiverException, IOException {
        getLog().info("Extracting war for context " + contextName);
        UnArchiver unArchiver = archiverManager.getUnArchiver("war");
        unArchiver.setSourceFile(warfile);
        unArchiver.setDestDirectory(contextDirectory);
        unArchiver.setOverwrite(true);
        unArchiver.extract();
	}
	
	protected void removeExisting(File dir, String contextName) {
		try {
			// delete the webapp context directory
	        final File contextDir = new File(dir, contextName);
	        if (contextDir.exists()) {
	            FileUtils.deleteDirectory(contextDir);
	        }
	        
	        // delete the deployed war file
	        final File warFile = new File(dir, contextName.concat(".war"));
	        if (warFile.exists()) {
	        	warFile.delete();
	        }
		} catch (IOException ex) {
			getLog().warn("Error deleting existing item", ex);
		}
	}


}
