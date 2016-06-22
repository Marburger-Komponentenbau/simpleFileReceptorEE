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
	
	// owner: Rechenkern
	private String dataFolderName = "E://Apps//RechenkernMain//fileReceptorEE//data//";
	private File dataFolder = null;

	private String zipArchivFolderName = "E://Apps//RechenkernMain//fileReceptorEE//dataZip//";
	private File zipArchivFolder = null;
	
	// owner: Captiva
	private String zipFolderName = "E://Apps//RechenkernMain//fileReceptorEE//data//";
	private File zipFolder = null;
	
	private String resultFolderName = "E://Apps//RechenkernMain//fileReceptorEE//data//";
	private File resultFolder = null;
	
	private String keySeparator = "_GHNW";//;
	private static String runIdSeparator = ".";
	
	public String getRunIdSeparator() {
		return runIdSeparator;
	}

	public String getKeySeparator() {
		return keySeparator;
	}

	public AbstractServlet() {
        super();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("servlet.properties");
        // ...
		Properties properties = new Properties();
		try {
			properties.load(input);
		} catch (IOException e) {
			System.out.println("Error reading Properties...");
			e.printStackTrace();
		}
		System.out.println("Properties found...");
		this.dataFolderName = properties.getProperty("imageFolderParent", this.dataFolderName);
		this.zipArchivFolderName = properties.getProperty("zipArchivFolderParent", this.zipArchivFolderName);
		this.zipFolderName = properties.getProperty("zipFolderParent", this.zipFolderName);
		this.resultFolderName = properties.getProperty("resultFolderParent", this.resultFolderName);
		this.keySeparator = properties.getProperty("keySeparator", this.keySeparator);
		runIdSeparator = properties.getProperty("runIdSeparator", runIdSeparator);
    }
    
	public File getZipArchivFolder() {
		return getContextFolder(this.getParentFolderZipArchiv());
	}	
	
	private File getParentFolderZipArchiv(){
		if(this.zipArchivFolder == null){
			this.zipArchivFolder = createFolder(this.zipArchivFolderName);
		}
		return this.zipArchivFolder;
	}	
	
	public File getDataFolder(){
        return getContextFolder(this.getParentFolderData());
	}

	private File getParentFolderData(){
		if(this.dataFolder == null){
			this.dataFolder = createFolder(this.dataFolderName);
		}
		return this.dataFolder;
	}	
	
	public File getZipFolder(){
        return getContextFolder(this.getParentFolderZip());
	}
		
	private File getParentFolderZip(){
		if(this.zipFolder == null){
			this.zipFolder = createFolder(this.zipFolderName);
		}
		return this.zipFolder;
	}	
	
	public File getResFolder(){
        return this.getContextFolder(this.getParentFolderRes());
	}

	private File getParentFolderRes(){
		if(this.resultFolder == null){
			this.resultFolder = createFolder(this.resultFolderName);
		}
		return this.resultFolder;
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
	
	public File getNewRenamedFile(File folder, String fname) {
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
				file = new File(folder, fnameBegin + runIdSeparator + Integer.toString(i++) + fnameEnd);
			}
		}
		return file;
	}	
	
	public static String getKeyOfFile(String fname) {

			String fnameBegin = fname;
			
			int endIndex = fname.lastIndexOf(".");
			if (endIndex != -1) {
				fnameBegin = fname.substring(0, endIndex);
			}

			int endIndexDel = fname.lastIndexOf(runIdSeparator);
			if (endIndexDel != -1) {
				fnameBegin = fname.substring(0, endIndexDel);
			}
			
			return fnameBegin;

	}	
	
	
	public File deleteFile(File folder, String fname) {
		File file = new File(folder, fname);
		file.deleteOnExit();
		System.out.println("DEL: " + file.getAbsolutePath());
		return file;
	}


	
}
