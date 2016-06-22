package com.conetex.simpleFileReceptorEE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
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
@WebServlet("/ZipSendScan")
public class ZipAndSendServlet extends AbstractServlet {
	private static final long serialVersionUID = 1L;
       
	
	
	String zipFileName = "";//zipFileName	
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ZipAndSendServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String files = request.getParameter("uploadedFiles");
		if(files == null){
			request.setAttribute("createdZips", "");
		    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/scanResult.jsp");
	        dispatcher.forward(request, response);				
			return;
		}
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		//response.getWriter().append(" - files: ").append(files);
		//response.getWriter().append("<br>");
		
		StringTokenizer st = new StringTokenizer( files, "|" );		
		
		// tiffFiles zipFiles zuordnen
		Map<String, Map<String,File>> zipFiles2TiffFiles = new HashMap<String, Map<String,File>> ();
		while ( st.hasMoreTokens() ){
			String filename = st.nextToken();
			File tiffFile = new File(super.getDataFolder(), filename);
			if(! tiffFile.exists() || ! tiffFile.canRead() ){
				continue;
			}
			// tiffFile vorhanden...
			String key = filename;
		    int idx = filename.indexOf( super.getKeySeparator() );
			if( idx != -1 )
			{
				key = filename.substring( 0, idx );
			}
			if( zipFiles2TiffFiles.containsKey( key ) )
			{
				Map<String,File> tiffFiles = zipFiles2TiffFiles.get( key );
				tiffFiles.put(tiffFile.getName(), tiffFile);
			}
			else{
				Map<String,File> tiffFiles = new HashMap<String,File>();
				zipFiles2TiffFiles.put( key, tiffFiles );
				tiffFiles.put(tiffFile.getName(), tiffFile);
			}				
		    //response.getWriter().append( filename );
		    //response.getWriter().append("<br>");
		}
		
		// zipFiles generieren
		Iterator<String> keys = zipFiles2TiffFiles.keySet().iterator();
		String createdZips = "";
		while( keys.hasNext() )
		{
			String key = keys.next();			
			File zipFile = super.getNewRenamedFile( this.getZipArchivFolder(), zipFileName + key + ".zip.tmp" );
			File zipFileFin = super.getNewRenamedFile( this.getZipArchivFolder(), zipFileName + key + ".zip" );
			File zipFileTarget = super.getNewRenamedFile( this.getZipFolder(), zipFileName + key + ".zip" );
			if( zipFile.exists() )
			{
				System.out.println( "Zip file: " + zipFile.getAbsolutePath() + " exists" );
				continue;
			}
			FileOutputStream fos = new FileOutputStream( zipFile );
	    	ZipOutputStream zos = new ZipOutputStream( fos );
	    	System.out.println( "Create Zip file: "+ zipFile.getAbsolutePath() );
	    	String zippedFiles = "";
	    	byte[] buffer = new byte[1024];
	    	Map<String,File> sub = zipFiles2TiffFiles.get( key );
			for( File tiffFile : sub.values() )
			{
				System.out.println( "Add Zip entry: "+ tiffFile.getName() );
				ZipEntry entry = new ZipEntry( tiffFile.getName() );
				zos.putNextEntry( entry );				
				FileInputStream fin = new FileInputStream( tiffFile );
				int len;
				while( ( len = fin.read( buffer ) ) > 0 )
				{
					zos.write( buffer, 0, len );
				}
				fin.close();
				zippedFiles = zippedFiles + tiffFile.getName() + "|";
				//tiffFile.delete();
			}
			zos.closeEntry();
			zos.close();
			if( zipFileFin.exists() )
			{
				System.out.println( "Zip file: " + zipFileFin.getAbsolutePath() + " exists" );
				continue;
			}			
			zipFile.renameTo(zipFileFin);
			
			//move to target (Captiva)
			Files.copy(zipFileFin.toPath(), zipFileTarget.toPath());
			
			createdZips = createdZips + zipFileFin.getName() + "|";
			request.setAttribute(zipFileFin.getName(), zippedFiles);
			//response.getWriter().append(zipFileFin.getName()+"|");
		}		 
		
		request.setAttribute("createdZips", createdZips);
	    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/scanResult.jsp");
        dispatcher.forward(request, response);			
		
		
	}

	
	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
