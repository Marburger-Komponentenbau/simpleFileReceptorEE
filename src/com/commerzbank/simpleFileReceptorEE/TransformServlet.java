package com.commerzbank.simpleFileReceptorEE;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Servlet implementation class DownloadServlet
 */
@WebServlet({ "/transform" })
public class TransformServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	private Transformer transformer = null;
	
	private static Transformer createTransformer(String xsltStr){
        StringReader reader = new StringReader(xsltStr);
        
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource( reader );
        try {
			return factory.newTransformer(xslt);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
		return null;
	}
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransformServlet() {
        super();
		this.transformer = createTransformer(
						    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						  + "<xsl:stylesheet version=\"1.0\""
						  + "	xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\""
						  + "	xmlns:SCAN=\"http://WSDLEntities.scan.zce.commerzbank.com/rk/Service.xsd\""
						  + "	exclude-result-prefixes=\"SCAN\""
						  + "	>"

						  + "<xsl:output"
						  + "	method=\"text\""
						  + "	encoding=\"UTF-8\""
						  + "	omit-xml-declaration=\"yes\""
						  + "	standalone=\"yes\""
						  + "	indent=\"yes\""
						  + "	media-type=\"text/plain\" />"

						  + "<xsl:template match=\"/\">"
						  //+ "	<xsl:text>&lt;nav&gt;</xsl:text>"
						  + "	<xsl:text>&lt;ul class=\"entities\"&gt;</xsl:text>"
						  + "	   <xsl:apply-templates select=\"SCAN:GetScoreResponse/SCAN:kernelStructureOutput\" />"
						  + "	   <xsl:apply-templates select=\"SCAN:GetScoreResponse/SCAN:kernelStructureOutput/SCAN:entities\" />"
						  + "	   <xsl:apply-templates select=\"SCAN:GetScoreRequest/SCAN:kernelStructureInput/SCAN:entities\" />"
						  + "	   <xsl:apply-templates select=\"SCAN:GetScoreResponse/SCAN:kernelStructureOutput/SCAN:faults\" />"
						  + "	<xsl:text>&lt;/ul&gt;</xsl:text>"
						  //+ "	<xsl:text>&lt;/nav&gt;</xsl:text>"
						  + "</xsl:template>"				  
						  
						  + "<xsl:template match=\"SCAN:entities\">"
						  //+ "		<xsl:for-each select=\"*//SCAN:entities\">"
						  //+ "		<xsl:for-each select=\"SCAN:GetScoreResponse/SCAN:kernelStructureOutput/SCAN:entities\">" 
						  //+ "		<xsl:for-each select=\"SCAN:GetScoreRequest/SCAN:kernelStructureInput/SCAN:entities\">"
						  + "			 <xsl:text>&lt;li class=\"entity\"&gt;</xsl:text>"
						  + "			    <xsl:text>&lt;a href=\"javascript:void(0)\" onclick=\"display(this,this);\" &gt;</xsl:text>"
						  + "				   <xsl:value-of select=\"SCAN:name\"/>"
						  + "			    <xsl:text>&lt;/a&gt;</xsl:text>"
						  + "				<xsl:text>&lt;table class=\"values\"&gt;</xsl:text>"
						  + "					<xsl:for-each select=\"SCAN:values\">"
						  + "						<xsl:text>&lt;tr&gt;</xsl:text>"
						  + "							<xsl:text>&lt;td&gt;</xsl:text>"
						  + "								<xsl:value-of select=\"SCAN:name\"/>"
						  + "							<xsl:text>&lt;/td&gt;</xsl:text>"
						  + "							<xsl:text>&lt;td&gt;</xsl:text>"
						  + "								<xsl:value-of select=\"SCAN:value\"/>"
						  + "							<xsl:text>&lt;/td&gt;</xsl:text>"
						  + "						<xsl:text>&lt;/tr&gt;</xsl:text>"
						  + "					</xsl:for-each>"
						  + "				<xsl:text>&lt;/table&gt;</xsl:text>"
						  + "	            <xsl:text>&lt;ul class=\"entities\"&gt;</xsl:text>"
						  + "			       <xsl:apply-templates select=\"SCAN:entities\" />"
						  + "	            <xsl:text>&lt;/ul&gt;</xsl:text>"
						  + "			<xsl:text>&lt;/li&gt;</xsl:text>"
						  //+ "		</xsl:for-each>"
						  + "</xsl:template>"
						  
						  + "<xsl:template match=\"SCAN:faults\">"
						  + "			 <xsl:text>&lt;li class=\"entity\"&gt;</xsl:text>"
						  + "			    <xsl:text>&lt;a href=\"javascript:void(0)\" onclick=\"display(this,this);\" &gt;</xsl:text>"
						  + "			       <xsl:text>Fault</xsl:text>"
						  + "				   <xsl:value-of select=\"SCAN:name\"/>"
						  + "			    <xsl:text>&lt;/a&gt;</xsl:text>"
						  + "				<xsl:text>&lt;table class=\"values\"&gt;</xsl:text>"
						  + "						<xsl:text>&lt;tr&gt;</xsl:text>"
						  + "							<xsl:text>&lt;td&gt;</xsl:text>"
						  + "								<xsl:text>Message</xsl:text>"
						  + "							<xsl:text>&lt;/td&gt;</xsl:text>"
						  + "							<xsl:text>&lt;td&gt;</xsl:text>"
						  + "								<xsl:value-of select=\"SCAN:message\"/>"
						  + "							<xsl:text>&lt;/td&gt;</xsl:text>"
						  + "						<xsl:text>&lt;/tr&gt;</xsl:text>"
						  + "				<xsl:text>&lt;/table&gt;</xsl:text>"
						  + "			<xsl:text>&lt;/li&gt;</xsl:text>"
						  + "</xsl:template>"						  

						  + "<xsl:template match=\"SCAN:kernelStructureOutput\">"
						  + "			 <xsl:text>&lt;li class=\"entity\"&gt;</xsl:text>"
						  + "			    <xsl:text>&lt;a href=\"javascript:void(0)\" onclick=\"display(this,this);\" &gt;</xsl:text>"
						  + "			       <xsl:text>Version</xsl:text>"
						  + "				   <xsl:value-of select=\"SCAN:name\"/>"
						  + "			    <xsl:text>&lt;/a&gt;</xsl:text>"
						  + "				<xsl:text>&lt;table class=\"values\"&gt;</xsl:text>"
						  + "						<xsl:text>&lt;tr&gt;</xsl:text>"
						  + "							<xsl:text>&lt;td&gt;</xsl:text>"
						  + "								<xsl:text> </xsl:text>"
						  + "							<xsl:text>&lt;/td&gt;</xsl:text>"
						  + "							<xsl:text>&lt;td&gt;</xsl:text>"
						  + "								<xsl:value-of select=\"SCAN:kernelVersion\"/>"
						  + "							<xsl:text>&lt;/td&gt;</xsl:text>"
						  + "						<xsl:text>&lt;/tr&gt;</xsl:text>"
						  + "				<xsl:text>&lt;/table&gt;</xsl:text>"
						  + "			<xsl:text>&lt;/li&gt;</xsl:text>"
						  + "</xsl:template>"	

						  + "</xsl:stylesheet>"
				);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String folderName = request.getParameter("folder");
		String fileName = request.getParameter("file");
		if(folderName == null || fileName == null || ! fileName.endsWith(".xml") || ! folderName.equals("xml")){
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		
		StaticUtils helper = StaticUtils.getInstance();
		File file = new File(helper.getResFolder(), fileName);
		for(int i = 0; ! file.exists() && i < 7; i++){
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if( ! file.exists() || ! file.canRead() ){
			response.getWriter().println( file.getAbsolutePath() + " ist nicht vorhanden!" );
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			System.out.println( "nicht vorhanden: " + file.getAbsolutePath() );
			return;			
		}
//System.out.println( "vorhanden: " + file.getAbsolutePath() );		
        // You must tell the browser the file type you are going to send
        // for example application/pdf, text/plain, text/html, image/jpg
        response.setContentType("text/plain");
        // Make sure to show the download dialog
        response.setHeader("Content-disposition","attachment; filename=" + fileName);
	
        OutputStream out = response.getOutputStream();
        Source text = new StreamSource( file );
        StreamResult streamRes = new StreamResult( out );
        try {
			transformer.transform(text, streamRes);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
		out.flush();		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}