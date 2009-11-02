/**
 * 
 */
package org.jasig.portal.web.skin;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test harness for JAXB marshalling/unmarshalling of {@link Resources}
 * 
 * @author Nicholas Blair, npblair@wisc.edu
 *
 */
public class JAXBTest {

	/**
	 * Marshal an empty skinConfiguration to System.out, test passes if no exception thrown.
	 */
	@Test
	public void testMarshalEmptyConfiguration() throws Exception {
		Resources config = new Resources();
		JAXBContext context = JAXBContext.newInstance("org.jasig.portal.web.skin");
		Marshaller m = context.createMarshaller();
		m.marshal(config, System.out);
		System.out.println();
	}
	
	/**
	 * Unmarshal an empty skinConfiguration.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUnmarshalEmptyConfiguration() throws Exception {
		JAXBContext context = JAXBContext.newInstance("org.jasig.portal.web.skin");
		Unmarshaller u = context.createUnmarshaller();
		Resources config = (Resources) u.unmarshal(
				new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><resources xmlns=\"http://www.jasig.org/uportal/web/skin\"/>"));
		Assert.assertNotNull(config);
		Assert.assertNotNull(config.getJs());
		Assert.assertNotNull(config.getCss());
		Assert.assertEquals(0, config.getJs().size());
		Assert.assertEquals(0, config.getCss().size());
	}
	
	/**
	 * Marshal an basic skinConfiguration to System.out, test passes if no exception thrown.
	 */
	@Test
	public void testMarshalControl() throws Exception {
		Resources config = new Resources();
		Js js = new Js();
		js.setValue("/path/to/some/javascript.js");
		config.getJs().add(js);
		Css css = new Css();
		css.setValue("/path/to/some/stylesheet.css");
		css.setMedia("screen");
		config.getCss().add(css);
		JAXBContext context = JAXBContext.newInstance("org.jasig.portal.web.skin");
		Marshaller m = context.createMarshaller();
		m.marshal(config, System.out);
		System.out.println();
	}
	
	/**
	 * Unmarshal a basic skinConfiguration.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUnmarshalControl() throws Exception {
		JAXBContext context = JAXBContext.newInstance("org.jasig.portal.web.skin");
		Unmarshaller u = context.createUnmarshaller();
		Resources config = (Resources) u.unmarshal(
				new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:resources xmlns:ns2=\"http://www.jasig.org/uportal/web/skin\"><css media=\"screen\">/path/to/some/stylesheet.css</css><js>/path/to/some/javascript.js</js></ns2:resources>"));
	
		Assert.assertNotNull(config);
		Assert.assertNotNull(config.getJs());
		Assert.assertNotNull(config.getCss());
		Assert.assertEquals(1, config.getJs().size());
		Assert.assertEquals(1, config.getCss().size());
		Assert.assertEquals("/path/to/some/javascript.js", config.getJs().get(0).getValue());
		Css expected = new Css();
		expected.setValue("/path/to/some/stylesheet.css");
		config.getCss().add(expected);
		Assert.assertEquals(expected.getValue(), config.getCss().get(0).getValue());
		Assert.assertEquals("screen", config.getCss().get(0).getMedia());
	}
	
	/**
	 * Marshal an basic skinConfiguration to System.out, test passes if no exception thrown.
	 */
	@Test
	public void testMarshalCssWithConditional() throws Exception {
		Resources config = new Resources();
		Css css = new Css();
		css.setValue("/path/to/some/stylesheet.css");
		css.setConditional("if IE lt 7");
		config.getCss().add(css);
		JAXBContext context = JAXBContext.newInstance("org.jasig.portal.web.skin");
		Marshaller m = context.createMarshaller();
		m.marshal(config, System.out);
		System.out.println();
	}
	/**
	 * Marshal an basic skinConfiguration to System.out, test passes if no exception thrown.
	 */
	@Test
	public void testMarshalJsWithConditional() throws Exception {
		Resources config = new Resources();
		Js css = new Js();
		css.setValue("/path/to/some/javascript.js");
		css.setConditional("if IE lt 7");
		config.getJs().add(css);
		JAXBContext context = JAXBContext.newInstance("org.jasig.portal.web.skin");
		Marshaller m = context.createMarshaller();
		m.marshal(config, System.out);
		System.out.println();
	}
	
	/**
	 * Unmarshal a resources containing a CSS with the conditional attribute
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUnmarshalCssWithConditional() throws Exception {
		JAXBContext context = JAXBContext.newInstance("org.jasig.portal.web.skin");
		Unmarshaller u = context.createUnmarshaller();
		Resources config = (Resources) u.unmarshal(
				new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:resources xmlns:ns2=\"http://www.jasig.org/uportal/web/skin\"><css conditional=\"if IE lt 7\">ie6hack.css</css></ns2:resources>"));
	
		Assert.assertNotNull(config);
		Assert.assertNotNull(config.getJs());
		Assert.assertNotNull(config.getCss());
		Assert.assertEquals(0, config.getJs().size());
		Assert.assertEquals(1, config.getCss().size());

		Assert.assertEquals("ie6hack.css", config.getCss().get(0).getValue());
		Assert.assertEquals("if IE lt 7", config.getCss().get(0).getConditional());
	}
	
	/**
	 * Unmarshal a resources containing a Js with the conditional attribute
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUnmarshalJsWithConditional() throws Exception {
		JAXBContext context = JAXBContext.newInstance("org.jasig.portal.web.skin");
		Unmarshaller u = context.createUnmarshaller();
		Resources config = (Resources) u.unmarshal(
				new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:resources xmlns:ns2=\"http://www.jasig.org/uportal/web/skin\"><js conditional=\"if IE lt 7\">ie6hack.js</js></ns2:resources>"));
	
		Assert.assertNotNull(config);
		Assert.assertNotNull(config.getJs());
		Assert.assertNotNull(config.getCss());
		Assert.assertEquals(1, config.getJs().size());
		Assert.assertEquals(0, config.getCss().size());

		Assert.assertEquals("ie6hack.js", config.getJs().get(0).getValue());
		Assert.assertEquals("if IE lt 7", config.getJs().get(0).getConditional());
	}
	
	/**
	 * Order is important in resources, assert order of multiple elements
	 * is preserved after JAXB unmarshalling.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUnmarshalOrderPreserved() throws Exception {
		JAXBContext context = JAXBContext.newInstance("org.jasig.portal.web.skin");
		Unmarshaller u = context.createUnmarshaller();
		Resources config = (Resources) u.unmarshal(
				new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:resources xmlns:ns2=\"http://www.jasig.org/uportal/web/skin\">" +
						"<css>/path/to/some/stylesheet1.css</css>" +
						"<css>/path/to/some/stylesheet2.css</css>" +
						"<css>/path/to/some/stylesheet3.css</css>" +
						"<css>/path/to/some/stylesheet4.css</css>" +
						"<css>/path/to/some/stylesheet5.css</css>" +
						"<css>/path/to/some/stylesheet6.css</css>" +
						"<js>/path/to/some/javascript1.js</js>" + 
						"<js>/path/to/some/javascript2.js</js>" + 
						"<js>/path/to/some/javascript3.js</js>" + 
						"<js>/path/to/some/javascript4.js</js>" + 
						"<js>/path/to/some/javascript5.js</js>" + 
						"<js>/path/to/some/javascript6.js</js>" +
						"</ns2:resources>"
				));
		
		Assert.assertNotNull(config);
		Assert.assertNotNull(config.getJs());
		Assert.assertNotNull(config.getCss());
		Assert.assertEquals(6, config.getJs().size());
		Assert.assertEquals(6, config.getCss().size());
		
		for(int i = 1; i <= 6; i++) {
			Assert.assertEquals("/path/to/some/stylesheet" + i + ".css", config.getCss().get(i - 1).getValue());
		}
		
		for(int i = 1; i <= 6; i++) { 
			Assert.assertEquals("/path/to/some/javascript" + i + ".js", config.getJs().get(i - 1).getValue());
		}
		
	}
}
