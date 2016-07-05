package com.commerzbank.simpleFileReceptorEE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ZipAndSendServlet
 */
@WebServlet("/Calculate")
public class CalculateServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CalculateServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String files = request.getParameter("uploadedFiles");
		ServletContext c = this.getServletContext();
		String context = c.getContextPath();
		if(files == null){
			request.setAttribute("createdZips", "");
		    RequestDispatcher dispatcher = c.getRequestDispatcher("/scanResult.jsp");
	        dispatcher.forward(request, response);				
			return;
		}
		
		StringTokenizer st = new StringTokenizer( files, "|" );		
		StaticUtils helper = StaticUtils.getInstance();
		// tiffFiles zipFiles zuordnen
		Map<String, Map<String,File>> zipFiles2TiffFiles = new HashMap<String, Map<String,File>>();
		while ( st.hasMoreTokens() ){
			String filename = st.nextToken();
			if( filename.length() < 5 ){
				continue;
			}			
			File tiffFile = new File(helper.getDataFolder(context), filename);
			if(! tiffFile.exists() || ! tiffFile.canRead() ){
				continue;
			}
			// tiffFile vorhanden...
			String key = helper.getKeyOfFile(filename);
		    int idx = filename.indexOf( helper.getKeySeparator() );
			if( idx > 0 ){
				key = filename.substring( 0, idx );
			}
			if( zipFiles2TiffFiles.containsKey( key ) ){
				Map<String,File> tiffFiles = zipFiles2TiffFiles.get( key );
				tiffFiles.put(tiffFile.getName(), tiffFile);
			}
			else{
				Map<String,File> tiffFiles = new HashMap<String,File>();
				zipFiles2TiffFiles.put( key, tiffFiles );
				tiffFiles.put(tiffFile.getName(), tiffFile);
			}				
		}
		
		// zipFiles generieren
		Iterator<String> keys = zipFiles2TiffFiles.keySet().iterator();
		String createdZips = "";
		while( keys.hasNext() ){
			String key = keys.next();			
			File zipFile = helper.getNewRenamedFile( helper.getZipArchivFolder(context), key + ".zip.tmp" );
			File zipFileTarget = null;
			if(helper.getZipArchivFolder(context).equals(helper.getZipFolder())){
				zipFileTarget = helper.getNewRenamedFile( helper.getZipFolder(), "t_" + key + ".zip" );
			}
			else{
				zipFileTarget = helper.getNewRenamedFile( helper.getZipFolder(), key + ".zip" );
			}
			if( zipFile.exists() ){
				System.out.println( "Zip file: " + zipFile.getAbsolutePath() + " exists" );
				continue;
			}
			FileOutputStream fos = new FileOutputStream( zipFile );
	    	ZipOutputStream zos = new ZipOutputStream( fos );
	    	
	    	byte[] buffer = new byte[16384];
	    	List<String> zipped = new ArrayList<String>();
	    	Map<String,File> sub = zipFiles2TiffFiles.get( key );
			for( String tiffname : sub.keySet() ){
				ZipEntry entry = new ZipEntry( tiffname );
				zos.putNextEntry( entry );				
				FileInputStream fin = new FileInputStream( sub.get(tiffname) );
				int len;
				while( ( len = fin.read( buffer ) ) > 0 ){
					zos.write( buffer, 0, len );
				}
				fin.close();
				zipped.add(tiffname);
				//tiffFile.delete();
			}
			zos.closeEntry();
			zos.close();
			File zipFileFin = helper.getNewRenamedFile( helper.getZipArchivFolder(context), key + ".zip" );
			if( zipFileFin.exists() ){
				System.out.println( "Zip file: " + zipFileFin.getAbsolutePath() + " exists" );
				continue;
			}			
			zipFile.renameTo(zipFileFin);
			
			//move to target (Captiva)
			Files.copy(zipFileFin.toPath(), zipFileTarget.toPath());
			
			Collections.sort(zipped);
			String zippedFiles = "";
			for( String tiffname : zipped ){
				zippedFiles = zippedFiles + tiffname + "|";
			}
			createdZips = createdZips + zipFileFin.getName() + "|";
			request.setAttribute(zipFileFin.getName(), zippedFiles);
		}		 
		
		request.setAttribute("createdZips", createdZips);
	    RequestDispatcher dispatcher = c.getRequestDispatcher("/result.jsp");
        dispatcher.forward(request, response);			
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}