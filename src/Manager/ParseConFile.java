package Manager;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This class is used to generate a Document object and update a ConXMLFile
 * 
 * @author Stubborn
 *
 */
public class ParseConFile {
	private static Logger log = LoggerFactory.getLogger(ConFileProcess.class);
	private static final String CONF_FILE = "C:/Java/eclipse/workspace/LogManager/conf/MachineInfo.xml";
	private static final File file = new File(CONF_FILE);

	/**
	 * 
	 * @return a Document object
	 */
	public static Document getXMLFile() {
		DocumentBuilderFactory aDocumentBuilderFactory = null;
		DocumentBuilder aDocumentBuilder = null;
		try {
			aDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
			aDocumentBuilder = aDocumentBuilderFactory.newDocumentBuilder();
			Document aDocument = aDocumentBuilder.parse(file);
			return aDocument;
		} catch (ParserConfigurationException e) {
			log.error(" ParserConfigurationException occurred to the method of getXMLFile",e);
			return null;
		} catch (IOException e) {
			log.error("IOException occurred to the method of getXMLFile", e);
			return null;
		} catch (SAXException e) {
			log.error("SAXException occurred to the method of getXMLFile", e);
			return null;
		}
	}

	/**
	 * 
	 * @param aDocument
	 */
	public static void updateXMLFile(Document aDocument) {
		TransformerFactory aTransformerFactory = TransformerFactory
				.newInstance();
		Transformer aTransformer = null;
		try {
			aTransformer = aTransformerFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			log.error(" TransformerConfigurationException occurred to the method of updateXMLFile",e);
		}
		aTransformer.setOutputProperty(OutputKeys.ENCODING, "GBK");
		DOMSource source = new DOMSource(aDocument);
		StreamResult result = new StreamResult(file);
		try {
			aTransformer.transform(source, result);
		} catch (TransformerException e) {
			log.error(" TransformerException occurred to the method of updateXMLFile",e);
		}
	}
}
