package com.conetex.simpleFileReceptorEE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


/**
 * Servlet implementation class UploadServlet   
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {    
	
	private static final long serialVersionUID = 1L;

	//private static final String dataFolder = "C://_//02 Eclipse JEE Workspace//_GitHub//simpleFileReceptorEE//data//";
	//private static final String dataFolder = "C://dev//Projekte//EclipseEE_WS//data//";
	//private static final String dataFolder = "data//";
	private static String dataFolderName = "E://Apps//RechenkernMain//fileReceptorEE//data//";
	
	private static File dataFolder = null; 
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
        
        dataFolder = new File(dataFolderName);
        if ( !dataFolder.exists() && !dataFolder.mkdir() ) {
        	dataFolderName = "data//";
        	dataFolder = new File(dataFolderName);
        	if (! dataFolder.exists()){
        		dataFolder.mkdir();
        	}
        }        
        System.out.println("running");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    
        ServletContext c = this.getServletContext();
        File folder = new File(dataFolder, c.getContextPath());
        if (! folder.exists()) {
        	if (! folder.mkdir()){
        		folder = new File("default");
        	}
        }
        
	    //String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
	    Part filePart1 = request.getPart("file"); // Retrieves <input type="file" name="file">
	    if(filePart1 != null){
	    	String fileName1 = filePart1.getSubmittedFileName();
	    	//InputStream fileContent = filePart.getInputStream();
	    	System.out.println(fileName1);
	    }
//		List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList()); // Retrieves <input type="file" name="file" multiple="true">
		Collection<Part> fileParts = request.getParts();//.stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList()); // Retrieves <input type="file" name="file" multiple="true">
	    // image/jpeg - file[0]
		String fileName = "?";
		File outFile = null;
	    for (Part filePart : fileParts) {
	        fileName = filePart.getSubmittedFileName();
	        System.out.println(filePart.getContentType() + " - " + filePart.getName());
	        System.out.println(fileName);
	        InputStream fileContent = filePart.getInputStream();
	        outFile = writeToFileSystem(folder, fileName, fileContent);
	        if(outFile != null){
	    	    response.getWriter().append(outFile.getName()+"|");	        	
	        }
	    }
	    
	}

	private static File writeToFileSystem(File folder, String fname, InputStream in){
		File outFile = getFile(folder, fname);
		OutputStream out = getOutputStream(outFile);
		if(out == null || in == null){
			return null;
		}
		
		byte[] buffer = new byte[32768];        
        int len;
        try {
			while( (len = in.read(buffer)) > 0 ){
				out.write(buffer, 0, len);
			}
			out.flush();
			out.close();
			return outFile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private static FileOutputStream getOutputStream(File file) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fos;
	}
	
	private static File getFile(File folder, String fname) {
		File file = new File(folder, fname);
		if (file.exists()) {
			String fnameEnd = fname;
			String fnameBegin = "";
			
			int endIndex = fname.lastIndexOf(".");
			if (endIndex != -1) {
				fnameEnd = fname.substring(endIndex, fname.length());
				fnameBegin = fname.substring(0, endIndex);
			}
			
			int i = 0;
			while (file.exists()) {
				// fname.substring(0, endIndex);
				file = new File(folder, fnameBegin + "." + Integer.toString(i++) + fnameEnd);
			}
		}
		return file;
	}	
	
	
}
