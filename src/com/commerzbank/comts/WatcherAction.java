package com.commerzbank.comts;

import java.nio.file.Path;
import java.util.Vector;

public abstract class WatcherAction 
{
	public abstract void work( Vector<Path> files ) throws Throwable;
}
