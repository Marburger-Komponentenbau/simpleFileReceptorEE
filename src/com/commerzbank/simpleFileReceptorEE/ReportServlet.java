package com.commerzbank.simpleFileReceptorEE;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Files;

import javax.xml.bind.DatatypeConverter;

import org.w3c.dom.Document;

import com.commerzbank.generator.Response2Excel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Servlet implementation class DownloadServlet
 */
@WebServlet({ "/report", "/report64" })
public class ReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReportServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String folderName = request.getParameter("folder");
		String fileName = request.getParameter("file");
		// encode data on your side using BASE64
		if ("/report64".equalsIgnoreCase(request.getServletPath())) {
			fileName = new String(DatatypeConverter.parseBase64Binary(fileName));
		}
		if (folderName == null || fileName == null) {
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}

		StaticUtils helper = StaticUtils.getInstance();
		File folder = null;
		if (folderName.equals("in")) {
			folder = helper.getDataFolder(this.getServletContext().getContextPath());
		} else if (folderName.equals("inCon")) {
			folder = helper.getDataConvFolder(this.getServletContext().getContextPath());
		} else if (folderName.equals("zip")) {
			folder = helper.getZipArchivFolder(this.getServletContext().getContextPath());
		} else if (folderName.equals("xml")) {
			folder = helper.getResFolder();
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		
		String fileNameLowerCase = fileName.toLowerCase();
		
		File file = new File(folder, fileName);
		if (!file.exists() || !file.canRead()) {
			response.getWriter().println(file.getAbsolutePath() + " ist nicht vorhanden!");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		response.setContentType("application/vnd.ms-excel");
		
		OutputStream out = response.getOutputStream();
		
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		fac.setNamespaceAware( true );		
		
		try {
			
			//File dir = new File( "C:\\dev\\Programme\\eclipse-jee-mars-2-win32-x86_64\\eclipse\\data" );
			//file = new File(dir, "RKENTW_SCAN185400_2016-06-14_16-13-57_354_response.xml");
			/*
			PipedInputStream pin = new PipedInputStream();
			PipedOutputStream pout = new PipedOutputStream(pin);
			OutputStreamWriter wout = new OutputStreamWriter(pout);
			wout.write(
					  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:SCAN=\"http://WSDLEntities.scan.zce.commerzbank.com/rk/Service.xsd\">"
					+ "<SOAP-ENV:Header />"
					+ "<SOAP-ENV:Body>"			
					);
	        FileInputStream fin = new FileInputStream(file);
			byte[] buffer = new byte[65536];
			int length;
			while ((length = fin.read(buffer)) > 0) {
				pout.write(buffer, 0, length);
			}
			fin.close();		
			wout.write(
					  "</SOAP-ENV:Body>"
					+ "</SOAP-ENV:Envelope>"			
					);			
			*/
			
			/*
	        StringBuilder sb = new StringBuilder();
	        sb.append(
					  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:SCAN=\"http://WSDLEntities.scan.zce.commerzbank.com/rk/Service.xsd\">"
					+ "<SOAP-ENV:Header />"
					+ "<SOAP-ENV:Body>"		        		
	        		);
	        BufferedReader br = new BufferedReader(new FileReader(file));
	        String line = br.readLine();
	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }			
	        sb.append(
					  "</SOAP-ENV:Body>"
					+ "</SOAP-ENV:Envelope>"	        		
	        		);			
	        //new ByteArrayInputStream(exampleString.getBytes(StandardCharsets.UTF_8));
	        */
	        
			byte[] pre =
			(
			  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:SCAN=\"http://WSDLEntities.scan.zce.commerzbank.com/rk/Service.xsd\">"
			+ "<SOAP-ENV:Header />"
			+ "<SOAP-ENV:Body>"
			).getBytes();			
	        byte[] data = Files.readAllBytes(file.toPath());//"INHALT".getBytes();//
			byte[] post =
			(
					  "</SOAP-ENV:Body>"
					+ "</SOAP-ENV:Envelope>"
			).getBytes();	        
	        
			   byte[] all = new byte[pre.length + data.length + post.length];
			   System.arraycopy(pre , 0, all, 0                       , pre.length );
			   System.arraycopy(data, 0, all, pre.length              , data.length);			
			   System.arraycopy(post, 0, all, pre.length + data.length, post.length);			
			   ByteArrayInputStream bis = new ByteArrayInputStream(all);
			  
			   //String test = new String(all);
			   
			Document reDoc = fac.newDocumentBuilder().parse( bis );
			
			Document reDoc1 = fac.newDocumentBuilder().parse( file );
			
			Response2Excel rr2Excel = new Response2Excel( reDoc );
			response.setHeader("Content-disposition", "attachment; filename=" + fileName.replace(".xml", ".xls"));
			rr2Excel.generate(out);			
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().println(file.getAbsolutePath() + " ist nicht vorhanden: SAXException!");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			out.flush();
			return;			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().println(file.getAbsolutePath() + " ist nicht vorhanden: ParserConfigurationException!");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			out.flush();
			return;			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().println(file.getAbsolutePath() + " ist nicht vorhanden: Throwable!");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			out.flush();
			return;			
		}
		
		
		// You must tell the browser the file type you are going to send
		// for example application/pdf, text/plain, text/html, image/jpg
		// Make sure to show the download dialog
		

		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

		
	
}