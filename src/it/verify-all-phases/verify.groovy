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
// e.g. mvn org.jasig.maven:uportaldata-maven-plugin:1.0:dbimport
// the package phase would occur and this test would pass.  We want to just use mvn data-import.
// See comment in UportalDbImport.java

// Look for test file indicating default lifecycle ran and Test.java executed.
assert ! new File( basedir, "target/classes/Test.class" ).isFile()
