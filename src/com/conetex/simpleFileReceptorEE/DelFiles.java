package com.conetex.simpleFileReceptorEE;

import java.io.File;

public class DelFiles {

	public static void main(String[] args) {
		
		File folder = new File("C:\\dev\\_openFolder\\BWA_Testdaten\\");
		File[] files = folder.listFiles();
		if (files != null) {
			for (File file : files) {
				String fname = file.getName();
				String fnameLc = fname.toLowerCase();
				if(fnameLc.contains("datev")){
					file.delete();
				}
			}
		}			
		
		
	}

}
