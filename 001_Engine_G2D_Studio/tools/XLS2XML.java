import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class XLS2XML 
{
	public static void main(String[] args) throws Exception
	{
		if (args.length >= 2)
		{
			new XLS2XML().convert(new File(args[0]), new File(args[1]));
		}
	}
	
	public void convert(File xls, File xml) throws Exception
	{
		FileInputStream xml_is = new FileInputStream(xls);
		FileOutputStream xml_os = new FileOutputStream(xml);
		try {
			Workbook rwb = Workbook.getWorkbook(xml_is);
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory
					.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element e = doc.createElement(xls.getName());
			doc.appendChild(e);
			for (Sheet sheet : rwb.getSheets())
			{
				Element sa = writeSheet(sheet, doc);
				e.appendChild(sa);
			}
			doc.normalizeDocument();
			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(new DOMSource(doc), new StreamResult(xml_os));
		} finally {
			try {
				xml_is.close();
			} catch (Exception e) {
			}
			try {
				xml_os.flush();
				xml_os.close();
			} catch (Exception e) {
			}
		}
	}
	
	private Element writeSheet(Sheet sheet, Document doc)
	{
		Element sa = doc.createElement("sheet");
		sa.setAttribute("name", sheet.getName());
		sa.setAttribute("sheet_columns", sheet.getColumns()+"");
		sa.setAttribute("sheet_rows", sheet.getRows()+"");
		for (int r = 0; r < sheet.getRows(); r++) {
			Element sr = doc.createElement("row");
			sa.appendChild(sr);
			for (int c = 0; c < sheet.getColumns(); c++) {
				Element sc = doc.createElement("cell");
				sc.setTextContent(sheet.getCell(c, r).getContents());
				sr.appendChild(sc);
			}
		}
		return sa;
	}
	
}
