package com.conetex.simpleFileReceptorEE;   
lkjhölk
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

public class JettyStarter {

	public static void main(String[] args) {
		
        Server server = new Server(8081);
        
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(UploadServlet.class, "/*");        
        
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

}
