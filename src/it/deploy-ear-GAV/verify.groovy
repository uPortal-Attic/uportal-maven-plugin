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

import org.codehaus.plexus.util.*;

File testTomcat = new File( basedir, "target/tomcat" );

// Verify shared/lib
assert new File(testTomcat, "shared/lib/shared.jar").isFile();

// Verify portlet's exist
assert new File(testTomcat, "webapps/WarDeployerTestPortlet1").isDirectory();
assert new File(testTomcat, "webapps/WarDeployerTestPortlet2").isDirectory();

File targetDir = new File( basedir, "target");
FileUtils.deleteDirectory( targetDir );
