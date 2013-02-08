// Looking for test file indicating plug-in associated to db-init did run
assert ! new File( basedir, "db-init.txt" ).isFile()
//assert !testFile.isFile()

// Looking for test file indicating plug-in associated to data-import did run
assert new File( basedir, "data-import.txt" ).isFile()

// Looking for test file indicating plug-in associated to data-export did run
assert ! new File( basedir, "data-export.txt" ).isFile()

// Look for test file indicating default lifecycle ran and Test.java was compiled.
assert new File( basedir, "target/classes/Test.class" ).isFile()
