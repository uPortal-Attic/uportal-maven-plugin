package org.jasig.portal.plugin.mojo;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;

public abstract class AbstractTomcatMojo extends AbstractMojo {
	
    /**
     * @parameter expression="${maven.tomcat.home}/webapps"
     */
    private String webAppsDir;
    
    /**
     * @parameter expression="${maven.tomcat.home}/shared/lib"
     */
    private String jarDir;
    
    /**
     * @parameter expression="${maven.tomcat.home}"
     * @required
     */
    private File catalinaBase;

	public String getWebAppsDir() {
		return this.webAppsDir;
	}

	public void setWebAppsDir(String webAppsDir) {
		this.webAppsDir = webAppsDir;
	}

	public String getJarDir() {
		return this.jarDir;
	}

	public void setJarDir(String jarDir) {
		this.jarDir = jarDir;
	}

	public File getCatalinaBase() {
		return this.catalinaBase;
	}

	public void setCatalinaBase(File catalinaBase) {
		this.catalinaBase = catalinaBase;
	}
    
}
