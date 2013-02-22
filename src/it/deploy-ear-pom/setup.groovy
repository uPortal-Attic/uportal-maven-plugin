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

File sourceFile = new File(basedir, "shared-1.0.0.jar");
File installedFile = new File(localRepositoryPath, "org/jasig/uportal/maven/it/shared/1.0.0/shared-1.0.0.jar");
org.codehaus.plexus.util.FileUtils.copyFile(sourceFile, installedFile);

sourceFile = new File(basedir, "WarDeployerTestPortlet1-1.0.0.war");
installedFile = new File(localRepositoryPath, "org/jasig/uportal/maven/it/WarDeployerTestPortlet1/1.0.0/WarDeployerTestPortlet1-1.0.0.war");
org.codehaus.plexus.util.FileUtils.copyFile(sourceFile, installedFile);

sourceFile = new File(basedir, "WarDeployerTestPortlet2-2.3.12.war");
installedFile = new File(localRepositoryPath, "org/jasig/uportal/maven/it/WarDeployerTestPortlet2/2.3.12/WarDeployerTestPortlet2-2.3.12.war");
org.codehaus.plexus.util.FileUtils.copyFile(sourceFile, installedFile);

return true;
