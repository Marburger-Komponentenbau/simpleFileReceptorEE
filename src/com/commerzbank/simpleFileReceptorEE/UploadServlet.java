package com.commerzbank.simpleFileReceptorEE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadServlet() {
		super();
	}

	public static void main(String[] a) {
		convert(new File("C:\\dev\\Projekte\\EclipseEE_WS\\simpleFileReceptorEE\\test.TIF"), StaticUtils.getInstance());
	}

	private static void convert(File file, StaticUtils helper) {
		String i_view_Path = helper.getI_view_Path();
		if (i_view_Path.length() < 3 || file == null || 
				(
				!file.getName().toLowerCase().endsWith(".tif") && 
				!file.getName().toLowerCase().endsWith(".tiff")
				)
			) {
			return;
		}

		int re = new ProcessWithTimeout(
						new ProcessBuilder(i_view_Path, 
							file.getAbsolutePath(),
							"/extract=(" + helper.getImageSubFolderJpg() + ",jpg)",
							"/killmesoftly")
					).waitForProcess(5000);

		System.out.println("convert " + file.getAbsolutePath() + " returns " + re);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		StaticUtils helper = StaticUtils.getInstance();
		String context = this.getServletContext().getContextPath();
		File folder = null;
		String folderName = request.getParameter("absTargetPath");
		if (folderName == null || folderName.length() < 3) {
			folder = helper.getDataFolder(context);
		} else {
			folder = new File(folderName);
			if (!folder.exists() || !folder.isDirectory()) {
				folder = helper.getDataFolder(context);
			}
		}

		Collection<Part> fileParts = request.getParts();
		String fileName = "?";
		File outFile = null;
		for (Part filePart : fileParts) {
			fileName = filePart.getSubmittedFileName();
			boolean doWrite = true;
			outFile = new File(folder, fileName);
			if(outFile.exists()){
//System.out.println("uploaded: " + filePart.getSize() + " | existing: " + outFile.length());			
				doWrite = false;
				if( filePart.getSize() != outFile.length() ){
					if( outFile.delete() ){
						doWrite = true;
					}
				}
			}
			if( doWrite ){
				InputStream fileContent = filePart.getInputStream();
				outFile = StaticUtils.writeToFileSystem(outFile, fileContent);
				convert(outFile, helper);			
				fileContent.close();
			}
			if (outFile != null) {
				response.getWriter().append(outFile.getName() + "|");
			}
		}
	}

	private File _writeToFileSystem(File folder, String fname, InputStream in, StaticUtils helper) {
		File outFile = new File(folder, fname);
		if (outFile.exists()) {
			System.out.println("File exists: " + outFile.getAbsolutePath());
			return outFile;
		}
		OutputStream out = _getOutputStream(outFile);
		if (out == null || in == null) {
			return null;
		}

		byte[] buffer = new byte[32768];
		int len;
		try {
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			out.flush();
			out.close();
			convert(outFile, helper);
			return outFile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private static FileOutputStream _getOutputStream(File file) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fos;
	}

}