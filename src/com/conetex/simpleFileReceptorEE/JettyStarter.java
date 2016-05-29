package com.conetex.simpleFileReceptorEE;

import java.io.File;
import java.lang.management.ManagementFactory;

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




public class JettyStarter {

	public static void main(String[] args) {
		//main8080(args);
		try {
			main8083JSP(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//main8082(args);
	}
	
	public static void main8080(String[] args) {
		
        Server server = new Server(8080);
        server.setAttribute("org.eclipse.jetty.server.Request.maxFormContentSize", 2000000);
        
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

	
    public static void main8083JSP( String[] args ) throws Exception
    {
        // Create a basic jetty server object that will listen on port 8080.
        // Note that if you set this to port 0 then
        // a randomly available port will be assigned that you can either look
        // in the logs for the port,
        // or programmatically obtain it for use in test cases.
        Server server = new Server( 8083 );

        // Setup JMX
        //MBeanServer mbContainer = new MBeanServer(
          //      ManagementFactory.getPlatformMBeanServer() );
        //server.addBean( mbContainer );

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
        webapp.setContextPath( "/" );
        File warFile = new File(
                "C:/_Programs_Regfree/jetty-distribution-9.3.8.v20160314/webapps/simpleFileReceptorEE2.war" );
        if (!warFile.exists())
        {
            throw new RuntimeException( "Unable to find WAR File: "
                    + warFile.getAbsolutePath() );
        }
        webapp.setWar( warFile.getAbsolutePath() );

        // This webapp will use jsps and jstl. We need to enable the
        // AnnotationConfiguration in order to correctly
        // set up the jsp container
        Configuration.ClassList classlist = Configuration.ClassList
                .setServerDefault( server );
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

        // Configure a LoginService.
        // Since this example is for our test webapp, we need to setup a
        // LoginService so this shows how to create a very simple hashmap based
        // one. The name of the LoginService needs to correspond to what is
        // configured in the webapp's web.xml and since it has a lifecycle of
        // its own we register it as a bean with the Jetty server object so it
        // can be started and stopped according to the lifecycle of the server
        // itself.
        /*
        HashLoginService loginService = new HashLoginService();
        loginService.setName( "Test Realm" );
        loginService.setConfig( "src/test/resources/realm.properties" );
        server.addBean( loginService );
        */

        // Start things up!
        server.start();

        // The use of server.join() the will make the current thread join and
        // wait until the server is done executing.
        // See http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()
        server.join();
    }	
	
}
