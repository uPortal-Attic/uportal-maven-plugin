/**
 * 
 */
package org.jasig.portal.web.skin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * {@link IResourcesAggregator} implementation.
 * 
 * @author Nicholas Blair, npblair@wisc.edu
 *
 */
public class ResourcesAggregatorImpl implements IResourcesAggregator {

	private static final Log LOG = LogFactory.getLog(ResourcesAggregatorImpl.class);
	private final static String CSS = ".css";
	private final static String JS = ".js";
	protected final static String AGGREGATE_FILE_PREFIX = "uportal3_aggr";

	private int cssLineBreakColumnNumber = 10000;
	private int jsLineBreakColumnNumber = 10000;

	private boolean obfuscateJs = true;
	private boolean displayJsWarnings = true;
	private boolean preserveAllSemiColons = true;
	private boolean disableJsOptimizations = true;

	/**
	 * @return the cssLineBreakColumnNumber
	 */
	public int getCssLineBreakColumnNumber() {
		return cssLineBreakColumnNumber;
	}

	/**
	 * @param cssLineBreakColumnNumber the cssLineBreakColumnNumber to set
	 */
	public void setCssLineBreakColumnNumber(int cssLineBreakColumnNumber) {
		this.cssLineBreakColumnNumber = cssLineBreakColumnNumber;
	}

	/**
	 * @return the jsLineBreakColumnNumber
	 */
	public int getJsLineBreakColumnNumber() {
		return jsLineBreakColumnNumber;
	}

	/**
	 * @param jsLineBreakColumnNumber the jsLineBreakColumnNumber to set
	 */
	public void setJsLineBreakColumnNumber(int jsLineBreakColumnNumber) {
		this.jsLineBreakColumnNumber = jsLineBreakColumnNumber;
	}

	/**
	 * @return the obfuscateJs
	 */
	public boolean isObfuscateJs() {
		return obfuscateJs;
	}

	/**
	 * @param obfuscateJs the obfuscateJs to set
	 */
	public void setObfuscateJs(boolean obfuscateJs) {
		this.obfuscateJs = obfuscateJs;
	}

	/**
	 * @return the displayJsWarnings
	 */
	public boolean isDisplayJsWarnings() {
		return displayJsWarnings;
	}

	/**
	 * @param displayJsWarnings the displayJsWarnings to set
	 */
	public void setDisplayJsWarnings(boolean displayJsWarnings) {
		this.displayJsWarnings = displayJsWarnings;
	}

	/**
	 * @return the preserveAllSemiColons
	 */
	public boolean isPreserveAllSemiColons() {
		return preserveAllSemiColons;
	}

	/**
	 * @param preserveAllSemiColons the preserveAllSemiColons to set
	 */
	public void setPreserveAllSemiColons(boolean preserveAllSemiColons) {
		this.preserveAllSemiColons = preserveAllSemiColons;
	}

	/**
	 * @return the disableJsOptimizations
	 */
	public boolean isDisableJsOptimizations() {
		return disableJsOptimizations;
	}

	/**
	 * @param disableJsOptimizations the disableJsOptimizations to set
	 */
	public void setDisableJsOptimizations(boolean disableJsOptimizations) {
		this.disableJsOptimizations = disableJsOptimizations;
	}

	/**
	 * Aggregate the {@link Resources} object from the first {@link File} argument, placing
	 * all generated CSS and Javascript in the directory denoted in the second {@link File} argument.
	 * 
	 * Will generate an aggregated version of the resourcesXml file in the outputBaseDirectory, with the filename
	 * similar to the resourcesXml.
	 * Example:
	 * 
	 * resourcesXml filename: skin.xml; output filename: uportal3_aggr.skin.xml
	 * 
	 * @see org.jasig.portal.web.skin.IResourcesAggregator#aggregate(java.io.File, java.io.File)
	 * @throws IllegalArgumentException if outputBaseDirectory (2nd file argument) is not a writable directory
	 */
	public Resources aggregate(File resourcesXml,
			File outputBaseDirectory) throws IOException, AggregationException {
		if(null == outputBaseDirectory || !outputBaseDirectory.isDirectory() || !outputBaseDirectory.canWrite()) {
			throw new IllegalArgumentException("outputBaseDirectory ("+ (null == outputBaseDirectory ? null : outputBaseDirectory.getAbsolutePath()) + ") must be a directory AND writable");
		}
		
		// parse the resourcesXml input
		Resources original = loadResourcesXml(resourcesXml);
		File resourcesParentDir = resourcesXml.getParentFile();
		final String resourcesXmlBaseName = FilenameUtils.getBaseName(resourcesXml.getName());
		
		// aggregate CSS elements
		List<Css> cssResult = new ArrayList<Css>();
		List<Css> currentCssAggregateList = new ArrayList<Css>();
		int aggregateIndex = 1;
		for(Css originalCssElement : original.getCss()) {			
			// handle first loop iteration
			if(currentCssAggregateList.isEmpty() ) {
				currentCssAggregateList.add(originalCssElement);
			} else {
				// test if 'originalCssElement' will aggregate with head Css element in currentAggregate 
				if(originalCssElement.willAggregateWith(currentCssAggregateList.get(0))) {
					// matches current criteria, add to currentAggregate
					currentCssAggregateList.add(originalCssElement);
				} else {
					// doesn't match criteria
					// generate new single Css from currentAggregateList
					Css aggregate = aggregateCssList(currentCssAggregateList, aggregateIndex++, resourcesParentDir, outputBaseDirectory);
					// push result to cssResult
					cssResult.add(aggregate);

					// zero out currentAggregateList
					currentCssAggregateList = new ArrayList<Css>();

					// add originalCssElement to empty list
					currentCssAggregateList.add(originalCssElement);
				}
			}
		}
		// flush the currentAggregateList
		Css lastCssAggregate = aggregateCssList(currentCssAggregateList, aggregateIndex++, resourcesParentDir, outputBaseDirectory);
		if(null != lastCssAggregate) {
			cssResult.add(lastCssAggregate);
		}

		// aggregate JS elements
		List<Js> jsResult = new ArrayList<Js>();
		List<Js> currentJsAggregateList = new ArrayList<Js>();
		for(Js originalJsElement : original.getJs()) {
			// handle first loop iteration
			if(currentJsAggregateList.isEmpty()) {
				currentJsAggregateList.add(originalJsElement);
			} else {
				// test if 'originalJsElement' will aggregate with head Js element in currentAggregate 
				if(originalJsElement.willAggregateWith(currentJsAggregateList.get(0))) {
					// matches current criteria, add to currentAggregate
					currentJsAggregateList.add(originalJsElement);
				} else {
					// doesn't match criteria
					// generate new single Js from currentAggregateList
					Js aggregate = aggregateJsList(currentJsAggregateList, aggregateIndex++, resourcesParentDir, outputBaseDirectory);
					// push result to cssResult
					jsResult.add(aggregate);

					// zero out current AggregateList
					currentJsAggregateList = new ArrayList<Js>();

					// add originalJsElement to empty list
					currentJsAggregateList.add(originalJsElement);
				}
			}
		}
		// flush the js aggregatelist
		Js lastJsAggregate = aggregateJsList(currentJsAggregateList, aggregateIndex++, resourcesParentDir, outputBaseDirectory);
		if(null != lastJsAggregate) {
			jsResult.add(lastJsAggregate);
		}

		// build aggregated form result
		Resources aggregatedForm = new Resources();
		aggregatedForm.getCss().addAll(cssResult);
		aggregatedForm.getJs().addAll(jsResult);
		
		// dump aggregated form out to output directory
		//StringBuilder aggregatedFormOutputFileName = new StringBuilder(resourcesXmlBaseName);
		StringBuilder aggregatedFormOutputFileName = new StringBuilder(AGGREGATE_FILE_PREFIX);
		aggregatedFormOutputFileName.append(".").append(resourcesXmlBaseName);
		aggregatedFormOutputFileName.append(".").append(FilenameUtils.getExtension(resourcesXml.getName()));
		File aggregatedOutputFile = new File(outputBaseDirectory, aggregatedFormOutputFileName.toString());
		writeResourcesXmlToFile(aggregatedForm, aggregatedOutputFile);
		
		return aggregatedForm;
	}

	/**
	 * Use JAXB to unmarshal a {@link Resources} from the specified file.
	 * 
	 * @param resourcesXml
	 * @return
	 * @throws AggregationException if an problem occurred
	 */
	protected Resources loadResourcesXml(final File resourcesXml) throws AggregationException {	
		try {
			JAXBContext context = JAXBContext.newInstance("org.jasig.portal.web.skin");
			Unmarshaller u = context.createUnmarshaller();
			Resources result = (Resources) u.unmarshal(resourcesXml);
			return result;
		} catch (JAXBException e) {
			throw new AggregationException("an error occurred loading " + resourcesXml, e);
		}
	}
	
	/**
	 * Use JAXB to marshal a {@link Resources} out to a {@link File}.
	 * 
	 * @param resources
	 * @param file
	 * @throws AggregationException
	 */
	protected void writeResourcesXmlToFile(final Resources resources, final File file) throws AggregationException {
		try {
			JAXBContext context = JAXBContext.newInstance("org.jasig.portal.web.skin");
			Marshaller m = context.createMarshaller();
			m.marshal(resources, file);
			
		} catch (JAXBException e) {
			throw new AggregationException("an error writing resources to file " + file, e);
		}
	}

	/**
	 * Aggregate a {@link List} of {@link Css} elements into 1 single {@link Css}.
	 * 
	 * Only aggregates if {@link List} element has more than 1 element.
	 * If only 1 element exists in the {@link List} and is absolute, the single element is returned as is.
	 * 
	 * Returns null for null or empty list.
	 * 
	 * @param elements
	 * @return 
	 * @throws IOException 
	 */
	protected Css aggregateCssList(final List<Css> elements, final int aggregateIndex, final File skinDirectory, final File outputRoot) throws IOException {
		if(null == elements || elements.size() == 0) {
			return null;
		}

		// reference to the head of the list
		final Css headElement = elements.get(0);

		if(elements.size() == 1 && headElement.isAbsolute()) {
			return headElement;
		}

		final String cssElementRelativePath = FilenameUtils.getFullPath(headElement.getValue());
		final File cssDirectoryInOutputRoot = resolvePath(outputRoot, cssElementRelativePath);
		// create the same directory structure in the output root
		cssDirectoryInOutputRoot.mkdirs();

		// push all of the content from each css file into 1 temporary file.
		File tempFile = new File(cssDirectoryInOutputRoot, AGGREGATE_FILE_PREFIX + aggregateIndex + "_temp" + CSS);
		FileOutputStream out = new FileOutputStream(tempFile);
		for(Css css: elements) {
			File cssFile = resolvePath(skinDirectory, css.getValue());
			FileInputStream cssIn = new FileInputStream(cssFile);
			try {
				IOUtils.copy(cssIn, out);
				out.write(System.getProperty("line.separator").charAt(0));
			} finally {
				IOUtils.closeQuietly(cssIn);
			}
		}
		IOUtils.closeQuietly(out);

		// temp file is created, get checksum before compression
		final String checksum = checksum(tempFile);

		// create a new file name
		String newFileName = buildAggregateFileName(aggregateIndex, checksum, CSS);

		// new file for aggregated output (delete prior file if present)
		File aggregateOutputFile = new File(cssDirectoryInOutputRoot, newFileName);
		aggregateOutputFile.delete();

		// new FileWriter to the aggregate output file for Css compressor
		FileWriter compressWriter = new FileWriter(aggregateOutputFile);
		FileReader tempFileReader = new FileReader(tempFile);
		CssCompressor cssCompressor = new CssCompressor(tempFileReader);
		try {
			cssCompressor.compress(compressWriter, cssLineBreakColumnNumber);
		} finally {
			IOUtils.closeQuietly(tempFileReader);
			IOUtils.closeQuietly(compressWriter);
		}

		// delete the temp file now that we've written compressed version
		tempFile.delete();

		// return value 
		Css result = new Css();
		StringBuilder newResultValue = new StringBuilder();
		if(StringUtils.isNotBlank(cssElementRelativePath)) {
			newResultValue.append(cssElementRelativePath);
			if(!cssElementRelativePath.endsWith("/")) {
				newResultValue.append("/");
			}
		}
		newResultValue.append(newFileName);
		result.setValue(newResultValue.toString());
		result.setConditional(headElement.getConditional());
		result.setMedia(headElement.getMedia());
		return result;
	}

	/**
	 * Only aggregates if {@link List} argument has more than 1 element.
	 * If only 1 element exists in the {@link List} and is absolute, the single element is returned as is.
	 * 
	 * Returns null for null or empty list.
	 * 
	 * @param elements
	 * @return
	 * @throws IOException 
	 */
	protected Js aggregateJsList(final List<Js> elements, final int aggregateIndex, final File skinDirectory, final File outputRoot) throws IOException {
		if(null == elements || elements.size() == 0) {
			return null;
		}

		final Js headElement = elements.get(0);

		if(elements.size() == 1 && (headElement.isAbsolute() || headElement.isCompressed())) {
			return headElement;
		}

		final String jsElementRelativePath = FilenameUtils.getFullPath(headElement.getValue());
		final File jsDirectoryInOutputRoot = resolvePath(outputRoot, jsElementRelativePath);
		// create the same directory structure in the output root
		jsDirectoryInOutputRoot.mkdirs();

		// push all of the content from each css file into 1 temporary file.
		File tempFile = new File(jsDirectoryInOutputRoot, AGGREGATE_FILE_PREFIX + aggregateIndex + "_temp" + JS);
		FileOutputStream out = new FileOutputStream(tempFile);
		for(Js js: elements) {
			File jsFile = resolvePath(skinDirectory, js.getValue());
			FileInputStream jsIn = new FileInputStream(jsFile);
			try {
				IOUtils.copy(jsIn, out);
				out.write(System.getProperty("line.separator").charAt(0));
			} finally {
				IOUtils.closeQuietly(jsIn);
			}
		}
		IOUtils.closeQuietly(out);

		// temp file is created, get checksum before compression
		final String checksum = checksum(tempFile);

		// create a new file name
		String newFileName = buildAggregateFileName(aggregateIndex, checksum, JS);

		// new file for aggregated output (delete prior file if present)
		File aggregateOutputFile = new File(jsDirectoryInOutputRoot, newFileName);
		aggregateOutputFile.delete();

		// only compress if not already marked as compressed
		if(!headElement.isCompressed()) {
			// new FileWriter to the aggregate output file for JavaScript compressor

			FileWriter compressWriter = new FileWriter(aggregateOutputFile);
			FileReader tempFileReader = new FileReader(tempFile);
			JavaScriptCompressor jsCompressor = new JavaScriptCompressor(tempFileReader, new JavaScriptErrorReporterImpl());
			try {
				jsCompressor.compress(compressWriter, jsLineBreakColumnNumber, obfuscateJs, displayJsWarnings, preserveAllSemiColons, disableJsOptimizations);
			} finally {
				IOUtils.closeQuietly(tempFileReader);
				IOUtils.closeQuietly(compressWriter);
			}

			// delete the temp file now that we've written compressed version
			tempFile.delete();
		} else {
			FileUtils.moveFile(tempFile, aggregateOutputFile);
		}

		StringBuilder newResultValue = new StringBuilder();
		if(StringUtils.isNotBlank(jsElementRelativePath)) {
			newResultValue.append(jsElementRelativePath);
			if(!jsElementRelativePath.endsWith("/")) {
				newResultValue.append("/");
			}
		}
		newResultValue.append(newFileName);
		Js result = new Js();
		result.setValue(newResultValue.toString());
		result.setConditional(headElement.getConditional());
		result.setCompressed(headElement.isCompressed());
		return result;
	}

	/**
	 * Helper method to generate the aggregated file's name.
	 * 
	 * @param aggregateIndex
	 * @param checksum
	 * @return
	 */
	protected String buildAggregateFileName(int aggregateIndex, final String checksum, final String suffix) {
		StringBuilder result = new StringBuilder(AGGREGATE_FILE_PREFIX);
		result.append(aggregateIndex);
		result.append("_");
		result.append(checksum);
		result.append(suffix);
		return result.toString();
	}

	/**
	 * Resolve the specified path to find the specified File under directory.
	 * @param path
	 * @return
	 */
	protected File resolvePath(final File directory, final String path) {
		File resolved = new File(directory, path);
		return resolved;
	}


	/**
	 * Calculate MD5 checksum of the specified {@link File}.
	 * 
	 * @param file
	 * @return
	 * @throws IOException 
	 */
	protected String checksum(final File file) throws IOException {
		try {
			InputStream fin = new FileInputStream(file);
			java.security.MessageDigest md5 =
				MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[1024];
			int read;
			do {
				read = fin.read(buffer);
				if (read > 0) {
					md5.update(buffer, 0, read);
				}
			} while (read != -1);
			fin.close();
			byte[] digest = md5.digest();
			if (digest == null) {
				return null;
			}

			StringBuilder strDigest = new StringBuilder();
			for (int i = 0; i < digest.length; i++) {
				strDigest.append(Integer.toString((digest[i] & 0xff)+ 0x100, 16).substring(1).toUpperCase());
			}
			return strDigest.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("NoSuchAlgorithmException thrown when trying to initialize MD5", e);
		}
	}

	/**
	 * {@link ErrorReporter} implementation that builds an error message and prints it using
	 * the parent class' Commons Logging {@link Log}.
	 * 
	 * @author Nicholas Blair, npblair@wisc.edu
	 *
	 */
	protected static class JavaScriptErrorReporterImpl implements ErrorReporter {
		public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
			StringBuilder mesg = new StringBuilder("JavaScriptCompressor ERROR, ");
			mesg.append("message: ").append(message);
			mesg.append(", sourceName: ").append(sourceName);
			mesg.append(", line: ").append(line);
			mesg.append(", lineSource: ").append(lineSource);
			mesg.append(", lineOffset: ").append(lineOffset);
			LOG.error(mesg);
		}
		public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
			error(message, sourceName, line, lineSource, lineOffset);
			return new EvaluatorException(message, sourceName, line, lineSource, lineOffset);
		}
		public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
			StringBuilder mesg = new StringBuilder("JavaScriptCompressor WARNING, ");
			mesg.append("message: ").append(message);
			mesg.append(", sourceName: ").append(sourceName);
			mesg.append(", line: ").append(line);
			mesg.append(", lineSource: ").append(lineSource);
			mesg.append(", lineOffset: ").append(lineOffset);
			LOG.warn(mesg);
		}
	}
}
