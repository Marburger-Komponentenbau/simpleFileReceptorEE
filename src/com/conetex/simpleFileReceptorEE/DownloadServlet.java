package com.conetex.simpleFileReceptorEE;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.bind.DatatypeConverter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadServlet
 */
@WebServlet({ "/download", "/download64" })
public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		String folderName = request.getParameter("folder");
		String fileName = request.getParameter("file");
		// encode data on your side using BASE64
		if ("/download64".equalsIgnoreCase(request.getServletPath())) {
			fileName = new String(DatatypeConverter.parseBase64Binary(fileName));
		}
		if (folderName == null || fileName == null) {
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}

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

		String typ = "text/plain";
		String fileNameLowerCase = fileName.toLowerCase();
		if (fileNameLowerCase.endsWith(".xml")) {
			typ = "text/xml";
		} else if (fileNameLowerCase.endsWith(".tiff") || fileNameLowerCase.endsWith(".tif")) {
			typ = "image/tiff";
		} else if (fileNameLowerCase.endsWith(".jpg") || fileNameLowerCase.endsWith(".jpeg")) {
			typ = "image/jpeg";
		} else if (fileNameLowerCase.endsWith(".pdf")) {
			typ = "application/pdf";
		} else if (fileNameLowerCase.endsWith(".zip")) {
			typ = "application/gzip";
		} else {
			// response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			// return;
		}

		File file = new File(folder, fileName);
		if (!file.exists() || !file.canRead()) {
			response.getWriter().println(file.getAbsolutePath() + " ist nicht vorhanden!");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// You must tell the browser the file type you are going to send
		// for example application/pdf, text/plain, text/html, image/jpg
		response.setContentType(typ);
		// Make sure to show the download dialog
		response.setHeader("Content-disposition", "attachment; filename=" + fileName);

		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);
		byte[] buffer = new byte[65536];
		int length;
		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		in.close();
		out.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}