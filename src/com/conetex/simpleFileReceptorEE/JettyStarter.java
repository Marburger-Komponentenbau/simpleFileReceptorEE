package com.conetex.simpleFileReceptorEE;

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

public class JettyStarter {

	public static void main(String[] args) {
		//main8080(args);
		main8081(args);
		//main8082(args);
	}
	
	public static void main8080(String[] args) {
		
        Server server = new Server(8080);
        
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(UploadServlet.class, "/*");        
       
        server.setHandler(handler);
        
        try {
			server.start();
			server.dumpStdErr();
			server.join();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main8081(String[] args) {
		
        Server server = new Server(8081);
        /*
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(UploadServlet.class, "/*");        
        ServletContext context = handler..getServletContext();
		*/
        
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        ServletHolder fileUploadServletHolder = new ServletHolder(new UploadServlet());
        fileUploadServletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement("data/tmp"));
        handler.addServlet(fileUploadServletHolder, "/*");
        
        server.setHandler(handler);
        
        try {
			server.start();
			server.dumpStdErr();
			server.join();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main8082(String[] args) {
		int port = 8082;
		Server server = new Server(port);
		
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(UploadServlet.class, "/*"); 		
		
		WebAppContext context = new WebAppContext();
		context.setServletHandler(handler);

		context.setConfigurations(new Configuration[] {
				new AnnotationConfiguration(), new WebXmlConfiguration(),
				new WebInfConfiguration(), //new TagLibConfiguration(),
				new PlusConfiguration(), new MetaInfConfiguration(),
				new FragmentConfiguration(), new EnvConfiguration() });

		context.setContextPath("/");
		context.setParentLoaderPriority(true);
		server.setHandler(context);
		
		
		
		try {
			server.start();
			server.dump(System.err);
			server.join();			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
