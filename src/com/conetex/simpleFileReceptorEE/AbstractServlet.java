package com.conetex.simpleFileReceptorEE;

import java.io.File;

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
	
	public AbstractServlet() {
        super();
    }
    
	public File getFolderData(){
        return getContextFolder(getParentFolderData());
	}
	
	public File getZipFolder(){
        return getContextFolder(getParentFolderZip());
	}
	
	public static File getParentFolderData(){
		if(dataFolder == null){
			dataFolder = createFolder(dataFolderName);
		}
		return dataFolder;
	}
	
	public static File getParentFolderZip(){
		if(zipFolder == null){
			zipFolder = createFolder(zipFolderName);
		}
		return zipFolder;
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
	
	
	
}
