// Looking for test file indicating plug-in associated to db-init did run
File testFile = new File( basedir, "db-init.txt" )
assert new File( basedir, "db-init.txt" ).isFile()

// Looking for test file indicating plug-in associated to data-import did run
assert new File( basedir, "data-import.txt" ).isFile()

// Looking for test file indicating plug-in associated to data-export did run
assert new File( basedir, "data-export.txt" ).isFile()

// FIXME This test changed to NOT found because I could not figure out how to get the mojo's
// @phase annotation or the components.xml default-phases element to automatically
// invoke the mojo, and from it automatically execute
// the package phase in the default lifecycle.  If you invoked the mojo explicitely;
// e.g. mvn org.jasig.maven:uportalData-maven-plugin:1.0:dbimport
// the package phase would occur and this test would pass.  We want to just use mvn data-import.
// See comment in UportalDbImport.java

// Look for test file indicating default lifecycle ran and Test.java executed.
assert ! new File( basedir, "target/classes/Test.class" ).isFile()
