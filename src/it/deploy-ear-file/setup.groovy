import org.codehaus.plexus.util.*;

File targetDir = new File( basedir, "target");
FileUtils.deleteDirectory( targetDir );

return true;
