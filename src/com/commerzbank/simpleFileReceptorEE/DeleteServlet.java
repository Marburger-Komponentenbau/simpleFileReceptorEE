package com.commerzbank.simpleFileReceptorEE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

/**
 * Servlet implementation class ZipAndSendServlet
 */
@WebServlet("/Delete")
public class DeleteServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String forwardTarget = request.getParameter("forwardTarget");
		String files = request.getParameter("files2Delete");
		String folderName = request.getParameter("folder");
		
		ServletContext c = this.getServletContext();
		String context = c.getContextPath();
		
		StaticUtils helper = StaticUtils.getInstance();
		File folder = null;
		if (folderName.equals("in")) {
			folder = helper.getDataFolder(this.getServletContext().getContextPath());
		} else if (folderName.equals("inCon")) {
			folder = helper.getDataConvFolder(this.getServletContext().getContextPath());
		} else if (folderName.equals("zip")) {
			folder = helper.getZipArchivFolder(this.getServletContext().getContextPath());
		} else if (folderName.equals("xml")) {
			folder = helper.getResFolder();
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}		
		
		StringTokenizer st = new StringTokenizer( files, "|" );		
		while ( st.hasMoreTokens() ){
			String filename = st.nextToken();
			File f = new File(folder, filename);		
			if(f.exists()){
				f.delete();
			}
		}		
		
	    RequestDispatcher dispatcher = c.getRequestDispatcher(forwardTarget);
        dispatcher.forward(request, response);			
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}