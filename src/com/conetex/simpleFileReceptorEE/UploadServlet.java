package com.conetex.simpleFileReceptorEE;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
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
public class UploadServlet extends AbstractServlet {    
	
	private static final long serialVersionUID = 1L;

	//private static final String dataFolder = "C://_//02 Eclipse JEE Workspace//_GitHub//simpleFileReceptorEE//data//";
	//private static final String dataFolder = "C://dev//Projekte//EclipseEE_WS//data//";
	//private static final String dataFolder = "data//";
	//private static String dataFolderName = "E://Apps//RechenkernMain//fileReceptorEE//data//";
	
	//private static File dataFolder = null; 
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServlet() {
        super();
    }

    private void convert(File file){
    	if(file == null){
    		return;
    	}
    	String fname = file.getName();
    	String fnamel = fname.toLowerCase();
    	if(fnamel.endsWith(".tif") || fnamel.endsWith(".tiff")){
        	File outFile = super.getNewRenamedFile( this.getDataFolder(), fname + ".c.jpg" );
        	BufferedImage image = null;
    		try {
    			image = ImageIO.read(file);
    			
    			ImageIO.write(image, "jpg", outFile);
    			System.out.println(outFile.getAbsolutePath());
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}         	
    	}
    }
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		File folder = null; 
				
		String folderName = request.getParameter("absTargetPath");
		if(folderName == null || folderName.length() < 3){
			folder = super.getDataFolder();
		}	
		else{
			folder = new File(folderName);
	        if ( !folder.exists() || !folder.isDirectory() ) {
	        	folder = super.getDataFolder();
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
	        Collection<String> hn = filePart.getHeaderNames();
	        System.out.println(filePart.getContentType() + " - " + filePart.getName());
	        System.out.println(folder.getAbsolutePath() + " - " + fileName);
	        InputStream fileContent = filePart.getInputStream();
	        if( folderName != null && !(folderName.length() < 3) ){
	        	// OVERWRITE // TODO funktioniert irgendwie nicht ...
	        	super.deleteFile(folder, fileName);
	        }
	       	outFile = writeToFileSystem(folder, fileName, fileContent);
	        fileContent.close();// TODO funzts jetzt noch?
	        if(outFile != null){
	    	    response.getWriter().append(outFile.getName()+"|");	        	
	        }
	    }
	    
	}

	private File writeToFileSystem(File folder, String fname, InputStream in){
		File outFile = new File(folder, fname);
		if(outFile.exists() ){
			return outFile;
		}
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
			//convert(outFile);
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
	
	
	
	
}
