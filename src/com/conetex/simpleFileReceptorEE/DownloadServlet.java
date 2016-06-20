package com.conetex.simpleFileReceptorEE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadServlet
 */
@WebServlet({ "/download" })
public class DownloadServlet extends AbstractServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String folderName = request.getParameter("folder");
		String fileName = request.getParameter("file");
		if(folderName == null || fileName == null){
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		
		File folder = null;
		if(folderName.equals("in")){
			folder = super.getDataFolder();
		}
		else if(folderName.equals("zip")){
			folder = super.getZipFolder();
		}
		else if(folderName.equals("xml")){
			folder = super.getResFolder();
		}		
		else{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;			
		}
		
		String typ = "";
		String fileNameLowerCase = fileName.toLowerCase();
		if(fileNameLowerCase.endsWith(".xml")){
			typ = "text/xml";
		}
		else if(fileNameLowerCase.endsWith(".tiff") || fileNameLowerCase.endsWith(".tif")){
			typ = "image/tiff";
		}
		else if(fileNameLowerCase.endsWith(".jpg") || fileNameLowerCase.endsWith(".jpeg")){
			typ = "image/jpeg";
		}		
		else if(fileNameLowerCase.endsWith(".pdf")){
			typ = "application/pdf";
		}
		else if(fileNameLowerCase.endsWith(".zip")){
			typ = "application/gzip";
		}		 
		else{
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;			
		}
		 
		File file = new File(folder, fileName);
		if( ! file.exists() || ! file.canRead() ){
			response.getWriter().println( file.getAbsolutePath() + " ist nicht vorhanden!" );
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			//response.sendError(HttpServletResponse.SC_NOT_FOUND);
			System.out.println( "file: " + file.getAbsolutePath() + " nich da!" );
			return;			
		}
		
        // You must tell the browser the file type you are going to send
        // for example application/pdf, text/plain, text/html, image/jpg
        response.setContentType(typ);

        // Make sure to show the download dialog
        response.setHeader("Content-disposition","attachment; filename=" + fileName);
	
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);
		byte[] buffer = new byte[4096];
		int length;
		while ((length = in.read(buffer)) > 0){
		    out.write(buffer, 0, length);
		}
		in.close();
		out.flush();		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
