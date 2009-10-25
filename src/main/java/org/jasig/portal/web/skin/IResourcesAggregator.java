/**
 * 
 */
package org.jasig.portal.web.skin;

import java.io.File;
import java.io.IOException;

/**
 * Interface defines operations for aggregating {@link Resources}.
 * 
 * @author Nicholas Blair, npblair@wisc.edu
 *
 */
public interface IResourcesAggregator {

	/**
	 * 
	 * @param resourcesXml
	 * @param outputBaseDirectory
	 * @return
	 * @throws IOException
	 * @throws AggregationException
	 */
	Resources aggregate(File resourcesXml, File outputBaseDirectory) throws IOException, AggregationException;
}
