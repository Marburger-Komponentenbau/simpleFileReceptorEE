package com.conetex.simpleFileReceptorEE;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

public abstract class AbstractServlet extends HttpServlet { 

    /**
	 * 
	 */
	private static final long serialVersionUID = -2081219318396543572L;

	private static String defaultFolderName = "data//";
	private static String defaultSubFolderName = "dft";
	
	private static String dataFolderName = "E://Apps//RechenkernMain//fileReceptorEE//data//";
	private static File dataFolder = null;

	private static String zipFolderName = "E://Apps//RechenkernMain//fileReceptorEE//data//";
	private static File zipFolder = null;
	
	private static String resultFolderName = "E://Apps//RechenkernMain//fileReceptorEE//data//";
	private static File resultFolder = null;
	
	public AbstractServlet() {
        super();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("servlet.properties");
        // ...
		Properties properties = new Properties();
		try {
			properties.load(input);
		} catch (IOException e) {
			System.out.println("Error reading...");
			e.printStackTrace();
		}
		System.out.println("Properties found...");
    }
    
	public File getDataFolder(){
        return getContextFolder(getParentFolderData());
	}

	private static File getParentFolderData(){
		if(dataFolder == null){
			dataFolder = createFolder(dataFolderName);
		}
		return dataFolder;
	}	
	
	public File getZipFolder(){
        return getContextFolder(getParentFolderZip());
	}
		
	private static File getParentFolderZip(){
		if(zipFolder == null){
			zipFolder = createFolder(zipFolderName);
		}
		return zipFolder;
	}	
	
	public File getResFolder(){
        return getContextFolder(getParentFolderRes());
	}

	private static File getParentFolderRes(){
		if(resultFolder == null){
			resultFolder = createFolder(resultFolderName);
		}
		return resultFolder;
	}	
	
	
	
	private File getContextFolder(File parentFolder){
		ServletContext c = this.getServletContext();
        return createSubFolder(parentFolder, c.getContextPath());
	}
	
	private static File createSubFolder(File parent, String folderName){
		File folder = new File(parent, folderName);
        if ( !folder.exists() && !folder.mkdir() ) {
        	folder = new File(parent, defaultSubFolderName);
        	if (! folder.exists()){
        		folder.mkdir();
        		// TODO FEHLER garnix geht ...
        	}
        }			
		return folder;
	}
	
	private static File createFolder(String folderName){
		File folder = new File(folderName);
        if ( !folder.exists() && !folder.mkdir() ) {
        	folder = new File(defaultFolderName);
        	if (! folder.exists()){
        		folder.mkdir();
        		// TODO FEHLER garnix geht ...
        	}
        }			
		return folder;
	}
	
	public static File getFile(File folder, String fname) {
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
