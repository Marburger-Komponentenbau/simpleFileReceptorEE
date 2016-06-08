package com.commerzbank.comts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipMoveTo extends WatcherAction 
{
	protected String directory;
	protected Vector<Path> files2Zip;
	protected String zipFileName;
	
	public ZipMoveTo( String directory, String zipFileName )
	{
		this.directory = directory;
		this.zipFileName = zipFileName;
	}

	public Map<String,Vector<Path>> prepare( Vector<Path> files )
	{
		Map<String,Vector<Path>> map = new HashMap<String,Vector<Path>>();
		
		for( Path file : files )
		{
			String filename = file.getFileName().toString();
			int idx = filename.indexOf( "_GHNW" );
			
			if( idx != -1 )
			{
				String key = filename.substring( 0, idx );
			
				Vector<Path> sub = new Vector<Path>();
								
				if( map.containsKey( key ) )
				{
					sub = map.get( key );
				}
				
				sub.add( file );
				
				map.put( key, sub );			
			}			
		}
		
		return map;
	}
	
	@Override
	public void work( Vector<Path> files ) throws Throwable 
	{
		try
		{
			Map<String,Vector<Path>> map = prepare( files );
			
			if( map.size() == 0 )
			{
				return;
			}
			
			Iterator<String> keys = map.keySet().iterator();
			
			while( keys.hasNext() )
			{
				String key = keys.next();
				
				Vector<Path> sub = map.get( key );
			
				String newZipFilename = directory + File.separator + zipFileName +"_"+ key + ".zip";
				
				if( new File( newZipFilename ).exists() )
				{
					System.out.println( "Zip file:"+ newZipFilename +" exists" );
					
					return;
				}
				
				FileOutputStream fos = new FileOutputStream( newZipFilename );
		    	ZipOutputStream zos = new ZipOutputStream( fos );
			
		    	System.out.println( "Create Zip file: "+ newZipFilename );
		    	
		    	byte[] buffer = new byte[1024];
		    			    	
				for( Path path : sub )
				{
					System.out.println( "Add Zip entry:"+ path.getFileName().toString() );
					
					ZipEntry entry = new ZipEntry( path.getFileName().toString() );
					zos.putNextEntry( entry );
					
					FileInputStream in = new FileInputStream( path.toString() );
					
					int len;
					
					while( ( len = in.read( buffer ) ) > 0 )
					{
						zos.write( buffer, 0, len );
					}
					
					in.close();
					
					path.toFile().delete();
				}
				
				zos.closeEntry();
				
				zos.close();
			}
		}
		catch( Throwable t )
		{
			System.out.println( t.getMessage() );
		}
	}	
}
