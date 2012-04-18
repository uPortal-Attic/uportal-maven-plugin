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
/**
 * 
 */
package org.jasig.portal.web.skin;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * Test harness for {@link ResourcesAggregatorImpl}.
 * 
 * @author Nicholas Blair, npblair@wisc.edu
 *
 */
public class ResourcesAggregatorImplTest {

	@Test
	public void testControl() throws Exception {
		String tempPath = getTestOutputRoot() + "/skin-test1";

		File outputDirectory = new File(tempPath);
		outputDirectory.mkdirs();
		Assert.assertTrue(outputDirectory.exists());

		File skinXml = new ClassPathResource("skin-test1/skin.xml").getFile();
		Assert.assertTrue(skinXml.exists());

		ResourcesAggregatorImpl impl = new ResourcesAggregatorImpl();
		Resources result = impl.aggregate(skinXml, outputDirectory);
		Assert.assertEquals(1, result.getCss().size());
		Assert.assertEquals(1, result.getJs().size());
		Css aggrCss = result.getCss().get(0);
		Assert.assertTrue(aggrCss.getValue().startsWith("uportal3_1"));
		Js aggrJs = result.getJs().get(0);
		Assert.assertTrue(aggrJs.getValue().startsWith("uportal3_2"));

		File outputCss = new File(outputDirectory, aggrCss.getValue());
		Assert.assertTrue(outputCss.exists());
		String outputCssContent = FileUtils.readFileToString(outputCss);
		Assert.assertEquals(".selector{color:red;}.otherselector{color:green;}", outputCssContent);
		
		File outputJs = new File(outputDirectory, aggrJs.getValue());
		Assert.assertTrue(outputJs.exists());
	}

	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAllAbsolute() throws Exception {
		String tempPath = getTestOutputRoot() + "/skin-testAllAbsolute";

		File outputDirectory = new File(tempPath);
		outputDirectory.mkdirs();
		Assert.assertTrue(outputDirectory.exists());

		File skinXml = new ClassPathResource("skin-testAllAbsolute/skin.xml").getFile();
		Assert.assertTrue(skinXml.exists());

		ResourcesAggregatorImpl impl = new ResourcesAggregatorImpl();

		Resources result = impl.aggregate(skinXml, outputDirectory);
		Assert.assertEquals(2, result.getCss().size());
		Assert.assertEquals(2, result.getJs().size());
		Assert.assertEquals("/a.css", result.getCss().get(0).getValue());
		Assert.assertEquals("/b.css", result.getCss().get(1).getValue());
		Assert.assertEquals("/a.js", result.getJs().get(0).getValue());
		Assert.assertEquals("/b.js", result.getJs().get(1).getValue());

		// there should only be one output file - the skin-aggr.xml 
		Assert.assertEquals(1, outputDirectory.list().length);
		Assert.assertEquals("uportal3_aggr.skin.xml", outputDirectory.list()[0]);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testComplex() throws Exception {
		String tempPath = getTestOutputRoot() + "/skin-complex/superskin";
		
		File outputDirectory = new File(tempPath);
		outputDirectory.mkdirs();
		Assert.assertTrue(outputDirectory.exists());

		File skinXml = new ClassPathResource("skin-complex/superskin/skin.xml").getFile();
		Assert.assertTrue(skinXml.exists());

		ResourcesAggregatorImpl impl = new ResourcesAggregatorImpl();
		Resources result = impl.aggregate(skinXml, outputDirectory);
		
		List<Css> cssList = result.getCss();
		Assert.assertEquals(5, result.getCss().size());
		// 1st element was aggregated
		Assert.assertTrue(new File(outputDirectory, cssList.get(0).getValue()).exists());
		Assert.assertTrue(cssList.get(0).getValue().endsWith(".aggr.min.css"));
		
		// 2nd was aggregated, conditional
		Assert.assertTrue(new File(outputDirectory, cssList.get(1).getValue()).exists());
		Assert.assertTrue(cssList.get(1).isConditional());
		Assert.assertEquals("condition!", cssList.get(1).getConditional());
		Assert.assertTrue(cssList.get(1).getValue().endsWith(".aggr.min.css"));
		
		// 3rd and 4th are absolutes
		Assert.assertTrue(result.getCss().get(2).isAbsolute());
		Assert.assertEquals("/ResourceServingWebapp/rs/1.css", cssList.get(2).getValue());
		Assert.assertTrue(cssList.get(3).isAbsolute());
		Assert.assertEquals("/ResourceServingWebapp/rs/2.css", cssList.get(3).getValue());
		
		// 5th element was aggregated and has media set
		Assert.assertTrue(new File(outputDirectory, cssList.get(4).getValue()).exists());
		Assert.assertTrue(cssList.get(4).getValue().endsWith(".aggr.min.css"));
		Assert.assertEquals("alternate", cssList.get(4).getMedia());
		
		List<Js> jsList = result.getJs();
		Assert.assertEquals(4, jsList.size());
		// 1st aggregated
		Assert.assertTrue(new File(outputDirectory, jsList.get(0).getValue()).exists());
		Assert.assertTrue(result.getJs().get(0).getValue().endsWith("aggr.min.js"));
		// 2nd absolute
		Assert.assertTrue(result.getJs().get(1).isAbsolute());
		Assert.assertEquals("/universal.js", result.getJs().get(1).getValue());
		// 3rd aggregated
		Assert.assertTrue(new File(outputDirectory, jsList.get(2).getValue()).exists());
		Assert.assertTrue(result.getJs().get(2).getValue().endsWith("aggr.min.js"));
		// 4th is already compressed
		Assert.assertTrue(result.getJs().get(3).getValue().contains("js/c-compressed.js"));
		Assert.assertTrue(result.getJs().get(3).isCompressed());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUniversality() throws Exception {
		String tempPath = getTestOutputRoot() + "/skin-universality/uportal3";
		
		File outputDirectory = new File(tempPath);
		outputDirectory.mkdirs();
		Assert.assertTrue(outputDirectory.exists());

		File skinXml = new ClassPathResource("skin-universality/uportal3/skin.xml").getFile();
		Assert.assertTrue(skinXml.exists());
		
		ResourcesAggregatorImpl impl = new ResourcesAggregatorImpl();
		Resources result = impl.aggregate(skinXml, outputDirectory);
		Assert.assertNotNull(result);
		List<Css> cssList = result.getCss();
		Assert.assertEquals(6, cssList.size());
		List<Js> jsList = result.getJs();
		Assert.assertEquals(1, jsList.size());
		Assert.assertTrue(new File(outputDirectory, cssList.get(0).getValue()).exists());
		Assert.assertTrue(new File(outputDirectory, cssList.get(1).getValue()).exists());
		Assert.assertTrue(new File(outputDirectory, cssList.get(2).getValue()).exists());
		// index 3 is absolute
		Assert.assertTrue(new File(outputDirectory, cssList.get(4).getValue()).exists());
		Assert.assertTrue(new File(outputDirectory, cssList.get(5).getValue()).exists());
		Assert.assertTrue(new File(outputDirectory, jsList.get(0).getValue()).exists());
	}

	/**
	 * Delete our temp directory after test execution.
	 * @throws Exception
	 */
	@After
	public void cleanupTempDir() throws Exception {
		File testOutputDirectory = new File(getTestOutputRoot());
		FileUtils.cleanDirectory(testOutputDirectory);
		FileUtils.deleteDirectory(testOutputDirectory);
	}

	/**
	 * Shortcut to get a temporary directory underneath java.io.tmpdir.
	 * 
	 * @return
	 */
	private String getTestOutputRoot() {
		String tempPath = System.getProperty("java.io.tmpdir");
		if(!tempPath.endsWith("/")) {
			tempPath += "/";
		}
		tempPath = tempPath + "resources-aggregator-impl-test-output";
		return tempPath;
	}
}
