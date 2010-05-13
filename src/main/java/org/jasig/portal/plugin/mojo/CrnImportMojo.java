package org.jasig.portal.plugin.mojo;

//import java.io.File;
//import java.net.URL;
//import java.util.List;
//
//import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
//import org.apache.maven.project.MavenProject;
//import org.codehaus.classworlds.ClassRealm;
//import org.codehaus.classworlds.ClassWorld;
import org.danann.cernunnos.runtime.PojoTask;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Supports data import and migration within a Jasig portlet project.  
 * 
 * @author awills
 * 
 * @goal crn-import
 * @requiresDependencyResolution
 * @requiresProject true
 * @since 1.0.0
 */
public class CrnImportMojo extends AbstractMojo {
    
//    /** 
//     * @parameter expression="${project}" 
//     */
//    private MavenProject project;
    
    /**
     * @parameter
     * @required
     * @since 1.0.0
     */
    private String[] configLocations;
    
    /**
     * @parameter
     * @required
     * @since 1.0.0
     */
    private String taskId;
    
    /**
     * @parameter
     * @required
     * @since 1.0.0
     */
    private String[] args;

    /*
     * Public API.
     */

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        
        // Verify parameters...
        if (configLocations == null || configLocations.length == 0) {
            String msg = "This mojo relies on a specified Spring app context;  "
            		        + "you must provide 'configLocations' parameter";
            throw new MojoExecutionException(msg);
        }
        
//        try {
//            extendPluginClasspath((List<String>) project.getCompileClasspathElements());
//        } catch (DependencyResolutionRequiredException drre) {
//            throw new RuntimeException(drre);
//        }

        // Launch the Spring context...
        ApplicationContext ctx = new FileSystemXmlApplicationContext(configLocations);
        
        // Obtain the task & invoke it...
        PojoTask k = (PojoTask) ctx.getBean(taskId);
        Object[] params = (Object[]) args;
        k.perform(params);
        
    }

//    /**
//     * Based off article at:  http://teleal.org/weblog/Extending%20the%20Maven%20plugin%20classpath.html
//     */
//    private void extendPluginClasspath(List<String> elements) throws MojoExecutionException {
//System.out.println("## ELEMENT COUNT:  " + elements.size());
//        // I found most of this on pastebin
//        ClassWorld world = new ClassWorld();
//        ClassRealm realm;
//        try {
//            realm = world.newRealm(
//                    "maven.plugin." + getClass().getSimpleName(),
//                    Thread.currentThread().getContextClassLoader()
//            );
//
//            for (String element : elements) {
//System.out.println("## ELEMENT:  " + element);
//                File elementFile = new File(element);
//                getLog().debug("Adding element to plugin classpath" + elementFile.getPath());
//                URL url = new URL("file:///" + elementFile.getPath() + (elementFile.isDirectory() ? "/" : ""));
//                realm.addConstituent(url);
//            }
//        } catch (Exception ex) {
//            throw new MojoExecutionException(ex.toString(), ex);
//        }
//        Thread.currentThread().setContextClassLoader(realm.getClassLoader());
//    }

}
