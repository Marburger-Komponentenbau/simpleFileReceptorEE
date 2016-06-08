package com.commerzbank.comts;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FolderSync 
{	
	protected List<Watcher> watchersList;

	public FolderSync( String configFilename )
	{	
		watchersList = new ArrayList<Watcher>();
		
		init( configFilename );
	}
	
	public static void shutdown() 
	{
		System.exit(0);
	}

	public static void shutdownNow() 
	{
		System.exit(0);
	}
	
	protected void init( String configFilename )
	{
		try
		{
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( configFilename );
			
			NodeList watchers = doc.getElementsByTagName( "watcher" );
			
			for( int w = 0; w < watchers.getLength(); w++ )
			{
				Watcher watch = new Watcher();
				
				Element watcher = (Element) watchers.item( w );
				
				if( watcher.hasAttribute( "id" ) )
				{
					watch.setId( watcher.getAttribute( "id" ) );
				}
				
				watch.setDir( watcher.getElementsByTagName( "dir" ).item( 0 ).getTextContent() );
				watch.setInterval( new Integer( watcher.getElementsByTagName( "interval" ).item( 0 ).getTextContent() ) );
								
				NodeList filters = watcher.getElementsByTagName( "filter" );
				
				for( int f = 0; f < filters.getLength(); f++ )
				{
					watch.addFileFilter( filters.item( f ).getTextContent() );
				}
								
				NodeList zipMoveActions = watcher.getElementsByTagName( "zipMoveTo" );
				
				for( int ma = 0; ma < zipMoveActions.getLength(); ma++ )
				{
					Node child = zipMoveActions.item( ma );
					
					String filename = ( child.hasAttributes() && child.getAttributes().getNamedItem( "zipfilename" ) != null ) ? child.getAttributes().getNamedItem( "zipfilename" ).getTextContent() : "temp.zip";
					
					ZipMoveTo zipMoveAction = new ZipMoveTo( child.getTextContent(), filename );
					
					watch.addActions( zipMoveAction );
				}
				
				System.out.println( "Init watcher:"+ watch.getId() );
				watchersList.add( watch );				
			}
		}
		catch( Throwable t )
		{
			System.out.println( t.getMessage() );
		}
	}
	
	public void run()
	{
		for( Watcher watcher : watchersList )
		{
			new WatcherThread( watcher ).run();
		}
	}
	
	public class WatcherThread extends Thread implements Runnable
	{
		protected Watcher watch;
		
		public WatcherThread( Watcher watch )
		{
			this.watch = watch;
		}
		
		public void run()
		{
			try
			{
				while( true )
				{
					System.out.println( "Running WatcherThread:"+ watch.getId() +" watching dir:"+ watch.getDir() + " interval:"+ watch.getInterval() );
					
					Vector<Path> files = new Vector<Path>();
					
					for( File file : watch.getDirPath().toFile().listFiles() )
					{	
						if( file != null && file.isFile() )
						{
							Path child = file.toPath();
							
							for( String filter : watch.getFileFilter() )
							{
								if( ( Files.probeContentType( child ) != null ) && ( Files.probeContentType( child ).compareToIgnoreCase( filter ) == 0 ) )
								{
									files.add( child );
									break;
								}
							}
						}
					}
					
					if( files.size() > 0 )
					{
						for( WatcherAction action : watch.getActions() )
						{
							action.work( files );
						}
					}
					
					System.out.println( "WatcherThread:"+ watch.getId() +" sleep for:"+ watch.getInterval() + " ms" );
					Thread.sleep( watch.getInterval() );
				}
			}
			catch( Throwable t )
			{
				System.out.println( "WatcherThread:"+ watch.getId() + t.getMessage() );
			}
		}
		
		/*
		public void run()
		{
			try
			{
				System.out.println( "Running WatcherThread:"+ watch.getId() +" watching dir:"+ watch.getDir() );
				
				WatchService watcher = FileSystems.getDefault().newWatchService();
							
				for( ;; )
				{
					WatchKey key = null;
					Vector<Kind<?>> events = watch.getEvents();
					
					if( events.size() == 1 )
					{
						key = watch.getDirPath().register( watcher, events.get( 0 ) );
					}
					else if( events.size() == 2 )
					{
						key = watch.getDirPath().register( watcher, events.get( 0 ), events.get( 1 ) );
					}
					else if( events.size() == 3 )
					{
						key = watch.getDirPath().register( watcher, events.get( 0 ), events.get( 1 ), events.get( 2 ) );
					}
					
					try
					{
						key = watcher.take();
					}
					catch( Throwable t )
					{
						System.out.println( t.getMessage() );
						
						return;
					}
					
					for( WatchEvent<?> event : key.pollEvents() )
					{
						WatchEvent.Kind<?> kind = event.kind();
												
						if( kind == StandardWatchEventKinds.OVERFLOW )
						{
							Vector<Path> cleanUp = new Vector<Path>();
												
							for( File file : watch.getDirPath().toFile().listFiles() )
							{	
								if( file != null )
								{
									Path child = file.toPath();
									
									for( String filter : watch.getFileFilter() )
									{
										if( Files.probeContentType( child ).compareToIgnoreCase( filter ) == 0 )
										{
											cleanUp.add( child );
											break;
										}
									}
								}
							}
							
							for( Path child : cleanUp )
							{
								for( WatcherAction action : watch.getActions() )
								{
									action.work( child );
								}
							}
							
							System.out.println( "Overflow: starting cleanUp" );
							continue;
						}
						
						WatchEvent<Path> ev = (WatchEvent<Path>)event;
						
						Path filename = ev.context();
						
						Path child = watch.getDirPath().resolve( filename );
												
						boolean doAction = false;
						
						for( String filter : watch.getFileFilter() )
						{
							if( Files.probeContentType( child ).compareToIgnoreCase( filter ) == 0 )
							{
								doAction = true;
								break;
							}
						}
						
						if( doAction )
						{
							for( WatcherAction action : watch.getActions() )
							{
								action.work( child );
							}
						}								
					}
					
					boolean valid = key.reset();
				    
					if( !valid ) 
				    {
						break;
				    }
				}
			}
			catch( Throwable t )
			{
				System.out.println( t.getMessage() );
			}
		}
		*/
	}
		
	public static void main( String[] args ) 
	{
		//args = new String[]{ "C:\\Apps\\dev\\Projekte\\java\\ComTS\\config.xml" };
		
		if( args != null && args.length != 1 )
		{
			System.out.println( "Wrong parameter. Except config.xml filename:" );
			
			System.exit( 1 );
		}
		
		FolderSync sync = new FolderSync( args[ 0 ] );
		sync.run();
		
		System.out.println( "Have a nice day!!!" );
	}

}
