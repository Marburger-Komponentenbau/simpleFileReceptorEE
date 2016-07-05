package com.commerzbank.simpleFileReceptorEE;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;

public class StaticUtils { 

	private static String defaultFolderName = "data//";
	private static String defaultSubFolderName = "dft";
	
	private static StaticUtils instance = null;
	
	public static StaticUtils getInstance() {
		if(instance == null){
			instance = new StaticUtils();
		}
		return instance;
	}
	
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
	
	private String keySeparator = "_GHNW";
	private String runIdSeparator = ".";
	
	private String i_view_Path = "";
	private String imageSubFolderJpg = "jpg";
	
	public String getRunIdSeparator() {
		return runIdSeparator;
	}

	public String getKeySeparator() {
		return keySeparator;
	}

	public String getI_view_Path() {
		return i_view_Path;
	}
	
	public String getImageSubFolderJpg() {
		return imageSubFolderJpg;
	}
	
	public StaticUtils() {
        super();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("servlet.properties");
        Properties properties = new Properties();
		try {
			properties.load(input);
		} catch (IOException e) {
			System.out.println("Error reading Properties...");
			e.printStackTrace();
		}
		this.dataFolderName = properties.getProperty("imageFolderParent", this.dataFolderName);
		this.zipArchivFolderName = properties.getProperty("zipArchivFolderParent", this.zipArchivFolderName);
		this.zipFolderName = properties.getProperty("zipFolder", this.zipFolderName);
		this.resultFolderName = properties.getProperty("resultFolder", this.resultFolderName);
		this.keySeparator = properties.getProperty("keySeparator", this.keySeparator);
		this.runIdSeparator = properties.getProperty("runIdSeparator", runIdSeparator);
		this.i_view_Path = properties.getProperty("i_view_Path", i_view_Path);
		if(! (new File(this.i_view_Path).exists()) ){
			this.i_view_Path = "C:\\dev\\Programme\\IrfanView\\i_view32.exe";
			if(! (new File(this.i_view_Path).exists()) ){
				this.i_view_Path = "";
			}					
		}
		this.imageSubFolderJpg = properties.getProperty("imageSubFolderJpg", imageSubFolderJpg);
    }
    
	public File getZipArchivFolder(String c) {
		return getContextFolder(this.getParentFolderZipArchiv(), c);
	}	
	
	private File getParentFolderZipArchiv(){
		if(this.zipArchivFolder == null){
			this.zipArchivFolder = createFolder(this.zipArchivFolderName);
		}
		return this.zipArchivFolder;
	}	
	
	public File getDataFolder(String c){
        return getContextFolder(this.getParentFolderData(), c);
	}

	public File getDataConvFolder(String c){
        return new File(this.getDataFolder(c), this.imageSubFolderJpg);
	}	
	
	public File[] getFiles(final Set<String> allJpgFiles, final String imgFilename, String c){
		File folder = this.getDataConvFolder(c);
		if(!folder.exists() || !folder.isDirectory()){
			return new File[] {};
		}
		final String imgName = imgFilename.replace(".TIFF", "").replace(".TIF", "").replace(".tiff", "").replace(".tif", "");
		File[] re = folder.listFiles(
				new FilenameFilter() {
				    public boolean accept(File dir, String name) {
				    	if( name.startsWith(imgName) ){// name.toLowerCase().endsWith(".jpg");
				    		return true;
				    	}
		    			return false;
				    }
				}
			);
		Arrays.sort(re);
		return re;
	}
	
	private File getParentFolderData(){
		if(this.dataFolder == null){
			this.dataFolder = createFolder(this.dataFolderName);
		}
		return this.dataFolder;
	}	
	
	public File getZipFolder(){
		if(this.zipFolder == null){
			this.zipFolder = createFolder(this.zipFolderName);
		}
		return this.zipFolder;
	}
	
	public File getResFolder(){
		if(this.resultFolder == null){
			this.resultFolder = createFolder(this.resultFolderName);
		}
		return this.resultFolder;
	}
	
	private File getContextFolder(File parentFolder, String c){
		return createSubFolder(parentFolder, c);
	}
	
	private static File createSubFolder(File parent, String folderName){
		File folder = new File(parent, folderName);
        if ( !folder.exists() && !folder.mkdir() ) {
        	folder = new File(parent, defaultSubFolderName);
        	if (! folder.exists()){
        		if(! folder.mkdir() ){
        			System.out.println("SubFolder " + folder.getAbsolutePath() + " can not be created!");
        		}
        	}
        }			
		return folder;
	}
	
	private static File createFolder(String folderName){
		File folder = new File(folderName);
        if ( !folder.exists() && !folder.mkdir() ) {
        	folder = new File(defaultFolderName);
        	if (! folder.exists() ){
        		if(! folder.mkdir() ){
        			System.out.println("Folder " + folder.getAbsolutePath() + " can not be created!");
        		}
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
				file = new File(folder, fnameBegin + runIdSeparator + Integer.toString(i++) + fnameEnd);
			}
		}
		return file;
	}	
	
	public String getKeyOfFile(String fnameOrg) {
		String fname = fnameOrg;	
		int endIndex = fname.lastIndexOf(".");
		// -1: kein treffer, 0: treffer ganz links also kein key, 1: kein Platz fur keyseparator
		if (endIndex > 1) {
			fname = fname.substring(0, endIndex);
		}
		int endIndexDel = fname.lastIndexOf(this.runIdSeparator);
		// -1: kein treffer, 0: treffer ganz links also kein key
		if (endIndexDel > 0) {
			fname = fname.substring(0, endIndexDel);
		}	
		return fname;
	}	
	
	public File deleteFile(File folder, String fname) {
		File file = new File(folder, fname);
		if(file.exists()){
			file.deleteOnExit();
			if(file.exists()){
				System.out.println("failed deleting: " + file.getAbsolutePath());
			}
			else{
				//System.out.println("deleting: " + file.getAbsolutePath());
			}
		}
		return file;
	}
	
}