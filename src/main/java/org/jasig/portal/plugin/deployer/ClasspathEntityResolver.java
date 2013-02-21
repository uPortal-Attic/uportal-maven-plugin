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

import java.io.IOException;
import java.io.InputStream;

import org.apache.maven.plugin.logging.Log;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Entity resolver that uses the file name of the systemId URL to locate the
 * classpath resource to resolve.
 * 
 * @author Eric Dalquist
 * @version $Revision$
 */
public class ClasspathEntityResolver implements EntityResolver {
    protected final Log logger;
    
    public ClasspathEntityResolver(Log logger) {
        this.logger = logger;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
     */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        final String entityName = getEntityName(systemId);
        final InputStream entityStream = getClass().getResourceAsStream(entityName);
        
        if (this.logger.isDebugEnabled()) {
            this.logger.debug((entityStream == null ? "Failed to resolve" : "Resolved") + " Entity for publicId: '" + publicId + "', systemId: '" + systemId + "'");
        }
        
        if (entityStream != null) {
            return new InputSource(entityStream);
        }
        
        return null;
    }

    /**
     * @param systemId The ID to parse
     * @return The path and name of the entity to return.
     */
    protected String getEntityName(String systemId) {
        final int lastSlashIndex = systemId.lastIndexOf('/');
        
        if (lastSlashIndex < 0) {
            return systemId;
        }
        
        final String entityName = systemId.substring(lastSlashIndex);
        return entityName;
    }
}
