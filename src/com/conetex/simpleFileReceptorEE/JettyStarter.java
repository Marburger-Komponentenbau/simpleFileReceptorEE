package com.conetex.simpleFileReceptorEE;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
//import org.eclipse.jetty.jmx.MBeanContainer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import java.util.logging.Formatter;


public class JettyStarter {

	private static final int port = 8097;
	
	//private static final String logFolder = "logs\\";
	private static final String logFolder = "E://Apps//RechenkernMain//fileReceptorEE//logs//";
	
	private static final Logger log = Logger.getLogger(UploadServlet.class.getName());
	
	private static final String LINE_SEPARATOR = System.getProperty("/r/n");	
	
	private static final String logEntryDateFormat = "yyyy.MM.dd HH.mm.ss";
	
	private static Server server;
	
	public static void shutdown() {
		System.exit(0);
	}	
	
	public static void shutdownNow() {
		System.exit(0);
	}
	
	public static void main(String[] args) {
		/*
		Thread a = new Thread(
				new Runnable(){
					@Override
					public void run() {
						start();
					}}
				);
		a.start();	
		*/
		System.out.println("Hi welt");	
		start();
	}
	
	public static void start() {
/*
		File logFolderFile = new File(logFolder);
		if (logFolderFile.exists()) {
			if (!logFolderFile.canWrite()) {
				System.err.println("can not write in log folder! Shut Down ...");
				return;
			}
		} else {
			if (!logFolderFile.mkdir()) {
				System.err.println("can not create log folder! Shut Down ...");
				return;
			}
			if (!logFolderFile.canWrite()) {
				System.err.println("can not write in log folder! Shut Down ...");
				return;
			}
		}
		Handler handler = null;
		try {
			handler = new FileHandler(logFolder + "%g.log", 5242880, 30, true);
		} catch (SecurityException e2) {
			System.err.println(e2.getMessage());
			e2.printStackTrace();
			return;
		} catch (IOException e2) {
			System.err.println(e2.getMessage());
			e2.printStackTrace();
			return;
		}
		handler.setFormatter(new LogFormatter());
		log.addHandler(handler);

		System.out.println("L O G G I N G   I N I T I A L I Z E D");	
*/
//        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        
		/*
		Thread a = new Thread(
				new Runnable(){
					@Override
					public void run() {
				        
					}}
				);
		a.start();
		*/

        // Create a basic jetty server object that will listen on port 8080.
        // Note that if you set this to port 0 then
        // a randomly available port will be assigned that you can either look
        // in the logs for the port,
        // or programmatically obtain it for use in test cases.
        server = new Server(port);
                
        //ServletContextHandler handler1 = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //handler1.setContextPath("/upload");
        ServletHolder fileUploadServletHolder = new ServletHolder(new UploadServlet());
        fileUploadServletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement("data/tmp"));
        //handler1.addServlet(fileUploadServletHolder, "/*");
        //server.setHandler(handler1);

        

        File warFile = new File(
//              "C:/_Programs_Regfree/jetty-distribution-9.3.8.v20160314/webapps/simpleFileReceptorEE2.war"
//        		"C:/dev/Projekte/EclipseEE_WS/simpleFileReceptorEE/simpleFileReceptorEE.war"
//        		"simpleFileReceptorEE.war"
        		"E:/Apps/RechenkernMain/fileReceptorEE/simpleFileReceptorEE.war"        		
        		);
        if (!warFile.exists())
        {
        	System.out.println("Error 0");	
        	
            ServletContextHandler handler1 = new ServletContextHandler(ServletContextHandler.SESSIONS);
            handler1.setContextPath("/upload");
            handler1.addServlet(fileUploadServletHolder, "/*");
            server.setHandler(handler1);        	
        	
//            throw new RuntimeException( "Unable to find WAR File: " + warFile.getAbsolutePath() );
        }
        else{
        	
            // The WebAppContext is the entity that controls the environment in
            // which a web application lives and
            // breathes. In this example the context path is being set to "/" so it
            // is suitable for serving root context
            // requests and then we see it setting the location of the war. A whole
            // host of other configurations are
            // available, ranging from configuring to support annotation scanning in
            // the webapp (through
            // PlusConfiguration) to choosing where the webapp will unpack itself.
            WebAppContext webapp = new WebAppContext();
            
            webapp.addServlet(fileUploadServletHolder, "/data/*");
            
            
            webapp.setContextPath( "/" );        	
        	
        	webapp.setWar( warFile.getAbsolutePath() );
        	
            // This webapp will use jsps and jstl. We need to enable the
            // AnnotationConfiguration in order to correctly
            // set up the jsp container
            Configuration.ClassList classlist = Configuration.ClassList.setServerDefault( server );
                    classlist.addBefore(
                            "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
                            "org.eclipse.jetty.annotations.AnnotationConfiguration" );

            // Set the ContainerIncludeJarPattern so that jetty examines these
            // container-path jars for tlds, web-fragments etc.
            // If you omit the jar that contains the jstl .tlds, the jsp engine will
            // scan for them instead.
            webapp.setAttribute(
                            "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                            ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/[^/]*taglibs.*\\.jar$" );        	

            // A WebAppContext is a ContextHandler as well so it needs to be set to
            // the server so it is aware of where to
            // send the appropriate requests.
            server.setHandler( webapp );            
	    }





        try {
        	// Start things up!
			server.start();
//			server.dumpStdErr();
	        // The use of server.join() the will make the current thread join and
	        // wait until the server is done executing.
	        // See http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()							
			server.join();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
        	System.out.println("Error 1");	
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
        	System.out.println("Error 2");	
			e.printStackTrace();
		}							
		
	}
	
	
	
	public static final class LogFormatter extends Formatter {

		private DateFormat logEntryDateFormatter = new SimpleDateFormat(
				logEntryDateFormat);

		@Override
		public String format(LogRecord record) {
			StringBuilder sb = new StringBuilder();

			sb.append(
					this.logEntryDateFormatter.format(new Date(record
							.getMillis()))).append(" ")
					.append(Thread.currentThread().getId()).append(" ")
					.append(record.getLevel().getName())
					// .getLocalizedName())
					.append(": ").append(formatMessage(record))
					.append(LINE_SEPARATOR);

			if (record.getThrown() != null) {
				try {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					record.getThrown().printStackTrace(pw);
					pw.close();
					sb.append(sw.toString());
				} catch (Exception ex) {
					// ignore
				}
			}

			return sb.toString();
		}
	}	
	
}
