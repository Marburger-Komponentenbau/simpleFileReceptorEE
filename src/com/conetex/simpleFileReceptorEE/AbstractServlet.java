package com.conetex.simpleFileReceptorEE;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

public abstract class AbstractServlet extends HttpServlet { 

    /**
	 * 
	 */
	private static final long serialVersionUID = -2081219318396543572L;


	public AbstractServlet() {
        super();
    }
    
	public File getFolder(){
		
		File dataFolder = DataFolder.getInstance().getFolder();
		
        ServletContext c = this.getServletContext();
        File folder = new File(dataFolder, c.getContextPath());
        if (! folder.exists()) {
        	if (! folder.mkdir()){
        		folder = new File("default");
        	}
        }
        return folder;
	}
	
	
	public static class DataFolder{
		
		private static String dataFolderName = "E://Apps//RechenkernMain//fileReceptorEE//data//";
		
		private static DataFolder dataFolder = null;
		
		private File file = null; 	
		
		public static DataFolder getInstance(){
			if(dataFolder == null){
				dataFolder = new DataFolder();
				
				dataFolder.file = new File(dataFolderName);
		        if ( !dataFolder.file.exists() && !dataFolder.file.mkdir() ) {
		        	dataFolderName = "data//";
		        	dataFolder.file = new File(dataFolderName);
		        	if (! dataFolder.file.exists()){
		        		dataFolder.file.mkdir();
		        	}
		        }			
				
			}
			return dataFolder;
		}
		
		public File getFolder(){
			return this.file;
		}
		
	}
	
}
