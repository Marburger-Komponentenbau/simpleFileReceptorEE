package com.conetex.simpleFileReceptorEE;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/uploadGet")
@MultipartConfig
public class UploadServletGet extends UploadServlet{

	private static String uploadPageHtml = "";
	//private static final String uploadPageHtmlLocation = "C://_//02 Eclipse JEE Workspace//_GitHub//simpleFileReceptorEE//the simpleFileReceptor dropzone.htm";
	//private static final String uploadPageHtmlLocation = "C://dev//Projekte//EclipseEE_WS//simpleFileReceptorEE//the simpleFileReceptor dropzone.htm";
	private static final String uploadPageHtmlLocation = "the simpleFileReceptor dropzone.htm";
	
	public UploadServletGet() {
		File idxFile = new File(uploadPageHtmlLocation);
		if (idxFile.exists()) {
			if (!idxFile.canWrite()) {
				System.err.println("can not read uploadPage! Shut Down ...");
				return;
			}
		} else {
			System.err.println("can not find " + idxFile.getAbsolutePath() + "! Shut Down ...");
			return;
		}
		try {
			byte[] encoded = Files.readAllBytes(idxFile.toPath());
			uploadPageHtml = new String(encoded, Charset.defaultCharset());
		} catch (IOException e1) {
			System.err.println(e1.getMessage() + e1);
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		} 
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		response.getWriter().append(uploadPageHtml);
		//response.getWriter().append("Served XXX at: ").append(request.getContextPath());
	}
	
}
