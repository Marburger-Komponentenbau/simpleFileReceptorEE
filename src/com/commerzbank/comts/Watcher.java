package com.commerzbank.comts;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.util.Vector;

public class Watcher 
{
	protected String id;
	protected String dir;
	protected int interval;
	protected Path dirPath;
	protected Vector<Kind<?>> events;
	protected Vector<String> fileFilter;
	protected Vector<WatcherAction> actions;
	
	public Watcher()
	{
		id = "No Set";
		events = new Vector<Kind<?>>();
		fileFilter = new Vector<String>();
		actions = new Vector<WatcherAction>();
	}
		
	public String getId() 
	{
		return id;
	}

	public void setId(String id) 
	{
		this.id = id;
	}

	public Path getDirPath()
	{
		return dirPath;
	}
	
	public String getDir() 
	{
		return dir;
	}
	
	public void setDir(String dir) 
	{
		this.dir = dir;
		this.dirPath = new File( dir ).toPath();
	}
	
	public int getInterval() 
	{
		return interval;
	}

	public void setInterval( int interval ) 
	{
		this.interval = interval;
	}
	
	public Vector<Kind<?>> getEvents() 
	{
		return events;
	}
	
	public void addEvents( Kind<?> events) 
	{
		this.events.add( events );
	}
	
	public Vector<String> getFileFilter() 
	{
		return fileFilter;
	}
	
	public void addFileFilter( String fileFilter) 
	{
		this.fileFilter.add( fileFilter );
	}
	
	public Vector<WatcherAction> getActions() 
	{
		return actions;
	}
	
	public void addActions( WatcherAction actions ) 
	{
		this.actions.add( actions );
	}
}
