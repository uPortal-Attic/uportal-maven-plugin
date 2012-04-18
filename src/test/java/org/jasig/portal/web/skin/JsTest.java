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

import org.junit.Assert;
import org.junit.Test;

/**
 * Simple test cases for {@link Js}, specifically the 
 * {@link Js#willAggregateWith(Js)}, {@link Js#isAbsolute()}, 
 * and {@link Js#isConditional()} methods.
 * 
 * @author Nicholas Blair, npblair@wisc.edu
 *
 */
public class JsTest {

	@Test
	public void testWillAggregateWithControl() throws Exception {
		Js j1 = new Js();
		Js j2 = new Js();
		
		j1.setValue("1.js");
		j2.setValue("2.js");
		Assert.assertTrue(j1.willAggregateWith(j2));
		Assert.assertTrue(j2.willAggregateWith(j1));
	}
	
	@Test
	public void testWillAggregateWithSubdirectory() throws Exception {
		Js j1 = new Js();
		Js j2 = new Js();
		
		j1.setValue("subdirectory/1.js");
		j2.setValue("subdirectory/2.js");
		Assert.assertTrue(j1.willAggregateWith(j2));
		Assert.assertTrue(j2.willAggregateWith(j1));
	}
	
	@Test
	public void testWillAggregateWithMultipleSubs() throws Exception {
		Js j1 = new Js();
		Js j2 = new Js();
		
		j1.setValue("sub/sub/sub/sub/1.js");
		j2.setValue("sub/sub/sub/sub/2.js");
		Assert.assertTrue(j1.willAggregateWith(j2));
		Assert.assertTrue(j2.willAggregateWith(j1));
	}
	
	@Test
	public void testWillAggregateWithOtherDir() throws Exception {
		Js j1 = new Js();
		Js j2 = new Js();
		j1.setValue("1.js");
		j2.setValue("otherdirectory/2.js");
		Assert.assertFalse(j1.willAggregateWith(j2));
		Assert.assertFalse(j2.willAggregateWith(j1));
		
		j1.setValue("otherdirectory/1.js");
		j2.setValue("2.js");
		Assert.assertFalse(j1.willAggregateWith(j2));
		Assert.assertFalse(j2.willAggregateWith(j1));
	}
	
	@Test
	public void testWillAggregateWithConditional() throws Exception {
		Js j1 = new Js();
		Js j2 = new Js();
		
		j1.setValue("subdirectory/1.js");
		j1.setConditional("condition1");
		j2.setValue("subdirectory/2.js");
		Assert.assertFalse(j1.willAggregateWith(j2));
		Assert.assertFalse(j2.willAggregateWith(j1));
		
		j2.setConditional("condition2");
		Assert.assertFalse(j1.willAggregateWith(j2));
		Assert.assertFalse(j2.willAggregateWith(j1));
		
		// set both to same condition, should aggregate
		j1.setConditional("condition2");
		Assert.assertTrue(j1.willAggregateWith(j2));
		Assert.assertTrue(j2.willAggregateWith(j1));
	}
	
	@Test
	public void testWillAggregateWithCompressed() throws Exception {
		Js j1 = new Js();
		Js j2 = new Js();
		
		j1.setValue("subdirectory/1.js");
		j1.setCompressed(true);
		j2.setValue("subdirectory/2.js");
		Assert.assertFalse(j1.willAggregateWith(j2));
		Assert.assertFalse(j2.willAggregateWith(j1));
		j2.setCompressed(true);
		Assert.assertFalse(j1.willAggregateWith(j2));
		Assert.assertFalse(j2.willAggregateWith(j1));

		
		// set both not compressed, should aggregate
		j1.setCompressed(false);
		j2.setCompressed(false);
		Assert.assertTrue(j1.willAggregateWith(j2));
		Assert.assertTrue(j2.willAggregateWith(j1));
	}
	
	
	@Test
	public void testIsConditional() throws Exception {
		Js c = new Js();
		c.setValue("a.js");
		
		Assert.assertFalse(c.isConditional());
		c.setConditional("some condition");
		Assert.assertTrue(c.isConditional());
	}
	
	@Test
	public void testIsAbsolute() throws Exception {
		Js c = new Js();
		c.setValue("subdirectory/1.js");
		Assert.assertFalse(c.isAbsolute());
		c.setValue("/subdirectory/1.js");
		Assert.assertTrue(c.isAbsolute());
	}
}
