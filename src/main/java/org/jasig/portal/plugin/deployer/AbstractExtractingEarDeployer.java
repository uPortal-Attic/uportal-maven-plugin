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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.component.annotations.Requirement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Base class for taking uPortal3 and portlets, packaged as an EAR, and deploying it to a container.
 * 
 * @author Eric Dalquist
 * @version $Revision$
 */
public abstract class AbstractExtractingEarDeployer implements EarDeployer {
    private static final String DESCRIPTOR_PATH        = "META-INF/application.xml";
    private static final String WEB_MODULE_XPATH       = "//application/module/web";
    private static final String WEB_URI_NODE_NAME      = "web-uri";
    private static final String CONTEXT_ROOT_NODE_NAME = "context-root";

    @Requirement
    private Log logger;
    
    protected void setLogger(Log logger) {
        this.logger = logger;
    }

    protected final Log getLogger() {
        return logger;
    }
    
    /**
     * Deployes an EAR to the container specified in the DeployerConfig. The EAR's
     * applicationContext.xml is parsed and the module/web entries are deployed using
     * {@link #deployWar(WebModule, JarFile, DeployerConfig)}. Then all JARs in the EAR are
     * deployed using {@link #deployJar(JarEntry, JarFile, DeployerConfig)}.
     * 
     * @param deployerConfig
     * @throws MojoFailureException 
     * @throws Exception
     */
    public final void deploy(DeployerConfig deployerConfig) throws MojoFailureException {
        final JarFile earFile = this.getEarFile(deployerConfig);
        final Document descriptorDom = this.getDescriptorDom(earFile);
        final NodeList webModules = this.getWebModules(descriptorDom);

        //Iterate through the WebModules, deploying each
        for (int index = 0; index < webModules.getLength(); index++) {
            final Node webModuleNode = webModules.item(index);
            final WebModule webModuleInfo = this.getWebModuleInfo(webModuleNode);

            this.deployWar(webModuleInfo, earFile, deployerConfig);
        }

        //Iterate through all the entries in the EAR, deploying each that ends in .jar
        for (final Enumeration<JarEntry> earEntries = earFile.entries(); earEntries.hasMoreElements();) {
            final JarEntry entry = earEntries.nextElement();

            if (entry.getName().endsWith(".jar")) {
                this.deployJar(entry, earFile, deployerConfig);
            }
        }
    }

    /**
     * Sub-classes must implement this to deploy the specified WAR file from the EAR to the appropriate
     * location for the container.
     * 
     * @param webModule Information about the WAR to deploy.
     * @param earFile The EAR.
     * @param deployerConfig Deployer configuration, sub-classes will likely us a DeployerConfig sub-class to pass container specific information
     * @throws IOException If an IO related error occures while deploying the WAR.
     */
    protected abstract void deployWar(WebModule webModule, JarFile earFile, DeployerConfig deployerConfig) throws MojoFailureException;

    /**
     * Sub-classes must implement this to deploy the specified JAR file from the EAR to the appropriate
     * location for the container.
     * 
     * @param jarEntry The entry in the EAR that contains the JAR to deploy.
     * @param earFile The EAR.
     * @param deployerConfig Deployer configuration, sub-classes will likely us a DeployerConfig sub-class to pass container specific information
     * @throws IOException If an IO related error occures while deploying the JAR.
     */
    protected abstract void deployJar(JarEntry jarEntry, JarFile earFile, DeployerConfig deployerConfig) throws MojoFailureException;

    /**
     * Gets the EAR from the configuration in the {@link DeployerConfig}.
     * 
     * @param deployerConfig The configuration with information about the EAR
     * @return The JarFile for the EAR.
     * @throws IOException If there was a problem finding or opening the EAR.
     */
    protected JarFile getEarFile(DeployerConfig deployerConfig) throws MojoFailureException {
        final File earLocation = deployerConfig.getEarLocation();
        try {
            return new JarFile(earLocation);
        }
        catch (IOException e) {
            throw new MojoFailureException("Failed to open '" + earLocation + " as a JAR file", e);
        }
    }

    /**
     * Gets the EAR descriptor from the {@link JarFile}.
     * 
     * @param earFile The EAR to get the descriptor from.
     * @return The descriptor DOM for the EAR.
     * @throws IOException If there is any problem reading the descriptor from the EAR.
     */
    protected Document getDescriptorDom(final JarFile earFile) throws MojoFailureException {
        final ZipEntry descriptorEntry = earFile.getEntry(DESCRIPTOR_PATH);
        if (descriptorEntry == null) {
            throw new IllegalArgumentException("JarFile '" + earFile + "' does not contain a descriptor at '" + DESCRIPTOR_PATH + "'");
        }
        

        InputStream descriptorStream = null;
        try {
            descriptorStream = earFile.getInputStream(descriptorEntry);
            
            final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            
            final DocumentBuilder docBuilder;
            try {
                docBuilder = docBuilderFactory.newDocumentBuilder();
            }
            catch (ParserConfigurationException pce) {
                throw new RuntimeException("Failed to create DocumentBuilder to parse EAR descriptor.", pce);
            }
            
            
            docBuilder.setEntityResolver(new ClasspathEntityResolver(this.logger));
            
            final Document descriptorDom;
            try {
                descriptorDom = docBuilder.parse(descriptorStream);
                return descriptorDom;
            }
            catch (SAXException e) {
                throw new MojoFailureException("Failed to parse descriptor '" + DESCRIPTOR_PATH + "' from EAR '" + earFile.getName() + "'", e);
            }
        }
        catch (IOException e) {
            throw new MojoFailureException("Failed to read descriptor '" + DESCRIPTOR_PATH + "' from EAR '" + earFile.getName() + "'", e);
        }
        finally {
            IOUtils.closeQuietly(descriptorStream);
        }
    }

    /**
     * Gets a {@link NodeList} of {@link Node}s that contain information about the web
     * modules in the EAR.
     * 
     * @param descriptorDom The EAR descriptor.
     * @return A NodeList of web module nodes.
     */
    protected NodeList getWebModules(Document descriptorDom) {
        final XPathFactory xpathFactory = XPathFactory.newInstance();
        final XPath xpath = xpathFactory.newXPath();

        final XPathExpression xpathExpr;
        try {
            xpathExpr = xpath.compile(WEB_MODULE_XPATH);
        }
        catch (XPathExpressionException xpee) {
            throw new RuntimeException("Failed to compile XPathExpression from '" + WEB_MODULE_XPATH + "'", xpee);
        }

        try {
            final NodeList nodes = (NodeList)xpathExpr.evaluate(descriptorDom, XPathConstants.NODESET);

            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Found " + nodes.getLength() + " '" + WEB_MODULE_XPATH + "' nodes in descriptor.");
            }

            return nodes;
        }
        catch (XPathExpressionException xpee) {
            throw new RuntimeException("Failed to evaluate XPathExpression='" + xpathExpr + "'", xpee);
        }
    }

    
    /**
     * Creates a {@link WebModule} from a {@link Node} from the descriptor. The {@link #WEB_URI_NODE_NAME}
     * and {@link #CONTEXT_ROOT_NODE_NAME} child nodes are used to populate the respective properties on
     * the {@link WebModule}.
     * 
     * @param webModuleNode The 'web' Node that has the information needed to create a WebModule.
     * @return A WebModule that represents the data contained in the passed Node.
     */
    protected WebModule getWebModuleInfo(Node webModuleNode) {
        if (!"web".equals(this.getNodeName(webModuleNode))) {
            throw new IllegalArgumentException("webModuleNode must be a 'web' Node");
        }
        
        String webUri = null;
        String contextRoot = null;

        //Iterate through the children looking for needed elements
        final NodeList childNodes = webModuleNode.getChildNodes();
        for (int index = 0; index < childNodes.getLength() && (webUri == null || contextRoot == null); index++) {
            final Node node = childNodes.item(index);
            final String nodeName = this.getNodeName(node);

            if (WEB_URI_NODE_NAME.equals(nodeName)) {
                webUri = StringUtils.strip(node.getTextContent());
            }
            else if (CONTEXT_ROOT_NODE_NAME.equals(nodeName)) {
                contextRoot = StringUtils.strip(node.getTextContent());
            }
        }
        
        //Check that the node had all the right info
        if (webUri == null || contextRoot == null) {
            throw new IllegalArgumentException("Node '" + webModuleNode + "' did not contain the nessesary information to create a WebModule. webUri='" + webUri + "', contextRoot='" + contextRoot + "'");
        }
        
        //Create the WebModule object
        final WebModule webModule = new WebModule();
        webModule.setWebUri(webUri);
        webModule.setContextRoot(contextRoot);

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Found WebModule='" + webModule + "'");
        }

        return webModule;
    }

    /**
     * Creates a File for the specified directory and name, if a file already exists
     * at the location it is deleted and all parent directories are verfied to exist. 
     * 
     * @param baseDir The directory to base the file in
     * @param fileName The name for the file
     * @return A new File object that has its parent directories and an existing file deleted. 
     * @throws IOException 
     */
    protected File createSafeFile(final File baseDir, final String fileName) throws IOException {
        final File safeFile = new File(baseDir, fileName);
        if (safeFile.exists()) {
            safeFile.delete();
        }
        else {
            FileUtils.forceMkdir(baseDir);
        }
        
        return safeFile;
    }

    /**
     * Reads the specified {@link JarEntry} from the {@link JarFile} and writes its contents
     * to the specified {@link File}.
     * 
     * @param earEntry The JarEntry for the file to read from the archive.
     * @param earFile The JarFile to get the {@link InputStream} for the file from.
     * @param destinationFile The File to write to, all parent directories should exist and no file should already exist at this location.
     * @throws IOException If the copying of data from the JarEntry to the File fails.
     */
    protected void copyAndClose(JarEntry earEntry, JarFile earFile, File destinationFile) throws MojoFailureException {
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Copying EAR entry '" + earFile.getName() + "!" + earEntry.getName() + "' to '" + destinationFile + "'");
        }

        InputStream jarEntryStream = null;
        try {
            jarEntryStream = earFile.getInputStream(earEntry);
            final OutputStream jarOutStream = new FileOutputStream(destinationFile);
            try {
                IOUtils.copy(jarEntryStream, jarOutStream);
            }
            finally {
                IOUtils.closeQuietly(jarOutStream);
            }
        }
        catch (IOException e) {
            throw new MojoFailureException("Failed to copy EAR entry '" + earEntry.getName() + "' out of '" + earFile.getName() + "' to '" + destinationFile + "'", e);
        }
        finally {
            IOUtils.closeQuietly(jarEntryStream);
        }
    }

    /**
     * Reads the specified {@link JarEntry} from the {@link JarFile} assuming that the
     * entry represents another a JAR file. The files in the {@link JarEntry} will be
     * extracted using the contextDir as the base directory. 
     * 
     * @param earFile The JarEntry for the JAR to read from the archive.
     * @param earEntry The JarFile to get the {@link InputStream} for the file from.
     * @param contextDir The directory to extract the JAR to.
     * @throws IOException If the extracting of data from the JarEntry fails.
     */
    protected void extractWar(JarFile earFile, final JarEntry earEntry, final File contextDir) throws MojoFailureException {
        if (this.logger.isInfoEnabled()) {
            this.logger.info("Extracting EAR entry '" + earFile.getName() + "!" + earEntry.getName() + "' to '" + contextDir + "'");
        }
        
        if (!contextDir.exists()) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Creating context directory entry '" + contextDir + "'");
            }

            try {
                FileUtils.forceMkdir(contextDir);
            }
            catch (IOException e) {
                throw new MojoFailureException("Failed to create '" + contextDir + "' to extract '" + earEntry.getName() + "' out of '" + earFile.getName() + "' into", e);
            }
        }
        
        JarInputStream warInputStream = null;
        try {
            warInputStream = new JarInputStream(earFile.getInputStream(earEntry));
            
            //TODO write manifest
            
            JarEntry warEntry;
            while ((warEntry = warInputStream.getNextJarEntry()) != null) {
                final File warEntryFile = new File(contextDir, warEntry.getName());
                
                if (warEntry.isDirectory()) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Creating WAR directory entry '" + earEntry.getName() + "!" + warEntry.getName() + "' as '" + warEntryFile + "'");
                    }
                    
                    FileUtils.forceMkdir(warEntryFile);
                }
                else {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Extracting WAR entry '" + earEntry.getName() + "!" + warEntry.getName() + "' to '" + warEntryFile + "'");
                    }
                    
                    FileUtils.forceMkdir(warEntryFile.getParentFile());
                    
                    final FileOutputStream jarEntryFileOutputStream = new FileOutputStream(warEntryFile);
                    try {
                        IOUtils.copy(warInputStream, jarEntryFileOutputStream);
                    }
                    finally {
                        IOUtils.closeQuietly(jarEntryFileOutputStream);
                    }
                }
            }
        }
        catch (IOException e) {
            throw new MojoFailureException("Failed to extract EAR entry '" + earEntry.getName() + "' out of '" + earFile.getName() + "' to '" + contextDir + "'", e);
        }
        finally {
            IOUtils.closeQuietly(warInputStream);
        }
    }

    private String getNodeName(Node node) {
        if (node.getNamespaceURI() == null) {
            return node.getNodeName();
        }
        
        return node.getLocalName();
    }
}
