package com.commerzbank.simpleFileReceptorEE.desktopClient;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sun.net.www.http.HttpClient;

public class Main {

	private Map<File, byte[]> processedFiles;
	
	private String domain = null;
	
	private File folder = null;

	private Main(String domain, File folder){
		this.processedFiles = new HashMap<File, byte[]>();
		this.domain = domain;
		this.folder = folder;
	}
	
	public void processFolder(){
		String uploadedFiles = "";
		File[] tiffFiles = this.folder.listFiles(
				new FilenameFilter() {
				    public boolean accept(File dir, String name) {
				    	if( name.toLowerCase().endsWith(".tif") || name.toLowerCase().endsWith(".tiff") ){
				    		return true;
				    	}
		    			return false;
				    }
				}
			);		
		for(File f : tiffFiles){
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
				InputStream is = Files.newInputStream(f.toPath());
				DigestInputStream dis = new DigestInputStream(is, md);
				int r = dis.read();
				while(r != -1){
					r = dis.read();
				}
				byte[] digest = md.digest();
				if( this.processedFiles.containsKey(f) ){
					byte[] knownDigest = md.digest();
					if (! Arrays.equals(digest, knownDigest))
					{
						this.processedFiles.put(f, digest);
						uploadedFiles = uploadedFiles.concat( uploadFile(f) ).concat( "|" );
					}	
				}
				else{
					this.processedFiles.put(f, digest);
					uploadedFiles = uploadedFiles.concat( uploadFile(f) ).concat( "|" );
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//"uploadedFiles" /Calculate
		calcFiles(uploadedFiles);
	}
	
	private boolean calcFiles(String uploadedFiles){
		//String url = "http://zce-rktu-sys.zit.commerzbank.com:8080/DiGestTest/Calculate";
		String url = this.domain + "/Calculate";
		String charset = "UTF-8";
		String body;
		try {
			body = "uploadedFiles=" + URLEncoder.encode( uploadedFiles, "UTF-8" );
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return false;
		}
		//File textFile = new File("/path/to/file.txt");
		//File binaryFile = f;
		String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
		String CRLF = "\r\n"; // Line separator required by multipart/form-data.
	
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection )new URL(url).openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		try {
			connection.setRequestMethod( "POST" );
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		connection.setDoInput( true );
		connection.setDoOutput( true );
		connection.setUseCaches( false );		
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty( "Content-Length", String.valueOf(body.length()) );

		OutputStreamWriter writer;
		try {
			writer = new OutputStreamWriter( connection.getOutputStream() );
			writer.write( body );
			writer.flush();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
	    
		// Request is lazily fired whenever you need to obtain information about response.
		int responseCode;
		try {
			responseCode = connection.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println(responseCode); // Should be 200	
		if(responseCode == 200){
			return true;
		}
		return false;
	}
	
	private String uploadFile(File f){
		//String url = "/upload";
		String url = this.domain + "/upload";
		String charset = "UTF-8";
		//String param = "value";
		//File textFile = new File("/path/to/file.txt");
		File binaryFile = f;
		String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
		String CRLF = "\r\n"; // Line separator required by multipart/form-data.
	
		URLConnection connection;
		try {
			connection = new URL(url).openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
	
	    OutputStream output;
		try {
			output = connection.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	    PrintWriter writer;
		try {
			writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	    // Send normal param.
		//writer.append("--" + boundary).append(CRLF);
		//writer.append("Content-Disposition: form-data; name=\"param\"").append(CRLF);
		//writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
		//writer.append(CRLF).append(param).append(CRLF).flush();

	    /*
	    // Send text file.
	    writer.append("--" + boundary).append(CRLF);
	    writer.append("Content-Disposition: form-data; name=\"textFile\"; filename=\"" + textFile.getName() + "\"").append(CRLF);
	    writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF); // Text file itself must be saved in this charset!
	    writer.append(CRLF).flush();
	    Files.copy(textFile.toPath(), output);
	    output.flush(); // Important before continuing with writer!
	    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
		*/
	    // Send binary file.
	    writer.append("--" + boundary).append(CRLF);
	    writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
	    writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
	    writer.append("Content-Transfer-Encoding: binary").append(CRLF);
	    writer.append(CRLF).flush();
	    try {
			Files.copy(binaryFile.toPath(), output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Important before continuing with writer!
	    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

	    // End of multipart/form-data.
	    writer.append("--" + boundary + "--").append(CRLF).flush();
		
		// Request is lazily fired whenever you need to obtain information about response.
		int responseCode;
		try {
			responseCode = ((HttpURLConnection) connection).getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		System.out.println(responseCode); // Should be 200	
		if(responseCode == 200){
			return f.getName();
		}
		return null;
	}
	
	private void uploadFile_(File f){
		URL url;
		HttpClient httpclient;
		try {
			url = new URL("http://zce-rktu-sys.zit.commerzbank.com:8080/DiGest/upload");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		try {
			httpclient = HttpClient.New(url);
			return;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//.createDefault();
		
		//HttpEntity entity = MultipartEntityBuilder.create().addTextBody("field1", "value1").addBinaryBody("myfile", new File("/path/file1.txt"), ContentType.create("application/octet-stream"), "file1.txt").build();		
        /*
		String error = "";
        HttpPost httppost = new HttpPost(txtUrl);
        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntity.addPart("Image", new FileBody(file));
        httppost.setEntity(multipartEntity);
        httpclient..execute(httppost, new PhotoUploadResponseHandler());		
		*/
        
        
		/*
		HttpPost httppost = new HttpPost("http://www.a-domain.com/foo/");

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("param-1", "12345"));
		params.add(new BasicNameValuePair("param-2", "Hello!"));
		httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

		//Execute and get the response.
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
		    InputStream instream = entity.getContent();
		    try {
		        // do something useful
		    } finally {
		        instream.close();
		    }
		}
		*/		
	}
	
	
	public static void main(String[] args) {
		File folder = new File("O:\\DE-O-01\\GRM-CC\\CalcKernels_Transfer\\Test\\");
		// http://zce-rktu-sys.zit.commerzbank.com:8080/DiGestTest
		Main m = new Main("http://localhost:8080/simpleFileReceptorEE", folder);
		
//		m.processFolder(f);
		
//calcFiles(
//"3001946078_7313674_0012648652_GHNW.TIF|3001946078_7313674_0012648652_GHNW_2.TIF|3280450577_7313397_0012649444_GHNW.TIF|3280450577_7313397_0012649444_GHNW_2.TIF|3233929056_7313952_0012652696_GHNW.TIF|3233929056_7313952_0012652696_GHNW_2.TIF|3233929056_7313952_0012652696_GHNW_3.TIF|3233929056_7313952_0012652696_GHNW_4.TIF|3233929056_7313952_0012652696_GHNW_5.TIF|2003462454_7314031_0012655046_GHNW.TIF|2003462454_7314031_0012655046_GHNW_2.TIF|2003462454_7314031_0012655046_GHNW_3.TIF|2705993415_7314560_0012654073_GHNW.TIF|2705993415_7314560_0012654073_GHNW_2.TIF|2705993415_7314560_0012654073_GHNW_3.TIF|2705993415_7314560_0012654073_GHNW_4.TIF|2706212823_7314150_0012650043_GHNW.TIF|2805559828_7313957_0012654185_GHNW.TIF|2805559828_7313957_0012654185_GHNW_2.TIF|3006105308_7312764_0012650603_GHNW.TIF|3006105308_7312764_0012650603_GHNW_2.TIF|3006105308_7312764_0012650603_GHNW_3.TIF|3006105308_7312764_0012650603_GHNW_4.TIF|3108731176_7314162_0012654407_GHNW.TIF|3108731176_7314162_0012654407_GHNW_2.TIF|3108731176_7314162_0012654407_GHNW_3.TIF|3108731176_7314162_0012654407_GHNW_4.TIF|3225839873_7314341_0012653915_GHNW.TIF|3225839873_7314341_0012653915_GHNW_2.TIF|3233928629_7313921_0012643860_GHNW.TIF|3233929130_7315847_0012654293_GHNW.TIF|3233929197_7315803_0012654903_GHNW.TIF|3233929197_7315803_0012654903_GHNW_2.TIF|3233930104_7314609_0012642566_GHNW.TIF|3233930104_7314609_0012642566_GHNW_2.TIF|3233930104_7314609_0012642566_GHNW_3.TIF|3233930104_7314609_0012642566_GHNW_4.TIF|3244330601_7313149_0012651038_GHNW.TIF|3244330601_7313149_0012651038_GHNW_2.TIF|3246909220_7315922_0012657904_GHNW.TIF|3257032287_7314140_0012655270_GHNW.TIF|3257032287_7314140_0012655270_GHNW_2.TIF|3257032287_7314140_0012655270_GHNW_3.TIF|3257032287_7314140_0012655270_GHNW_4.TIF|3268738882_7314223_0012651249_GHNW.TIF|3268738882_7314223_0012651249_GHNW_2.TIF|3296686521_7315898_0012658141_GHNW.TIF|3296686521_7315898_0012658141_GHNW_2.TIF|3318605439_7314382_0012654869_GHNW.TIF|3318605439_7314382_0012654869_GHNW_2.TIF|3348063072_7316318_0012660379_GHNW.TIF|3348063072_7316318_0012660379_GHNW_2.TIF|3348063072_7316318_0012660379_GHNW_3.TIF|3408010171_7315730_0012653346_GHNW.TIF|3408010171_7315730_0012653346_GHNW_2.TIF|3408010171_7315730_0012653346_GHNW_3.TIF|3408010171_7315730_0012653346_GHNW_4.TIF|3412731460_7316133_0012656359_GHNW.TIF|3412731460_7316133_0012656359_GHNW_2.TIF|3412731460_7316133_0012656359_GHNW_3.TIF|3468850835_7314145_0012652124_GHNW.TIF|3468850835_7314145_0012652124_GHNW_2.TIF|3468850835_7314145_0012652124_GHNW_3.TIF|3498935932_7314498_0012653343_GHNW.TIF|3498935932_7314498_0012653343_GHNW_2.TIF|4004159869_7316254_0012645903_GHNW.TIF|4004159869_7316254_0012645903_GHNW_2.TIF|4007154453_7314404_0012651360_GHNW.TIF|4007154453_7314404_0012651360_GHNW_2.TIF|4007154453_7314404_0012651360_GHNW_3.TIF|4007154453_7314404_0012651360_GHNW_4.TIF|4212851640_7315941_0012658975_GHNW.TIF|4212851640_7315941_0012658975_GHNW_2.TIF|4212851640_7315941_0012658975_GHNW_3.TIF|4714171757_7314674_0012651372_GHNW.TIF|4714171757_7314674_0012651372_GHNW_2.TIF|4714171757_7314674_0012651372_GHNW_3.TIF|4714171757_7314674_0012651372_GHNW_4.TIF|4717949928_7313931_0012653342_GHNW.TIF|4717949928_7313931_0012653342_GHNW_2.TIF|4719179953_7313750_0012653336_GHNW.TIF|4719179953_7313750_0012653336_GHNW_2.TIF|4719179953_7313750_0012653336_GHNW_3.TIF|4719179953_7313750_0012653336_GHNW_4.TIF|5131334614_7315807_0012657879_GHNW.TIF|5131334614_7315807_0012657879_GHNW_2.TIF|5182878254_7312217_0012645407_GHNW.TIF|5182878254_7312217_0012645407_GHNW_2.TIF|5221377308_7315831_0012658127_GHNW.TIF|5221377308_7315831_0012658127_GHNW_2.TIF|5225208194_7314545_0012646439_GHNW.TIF|6131210624_7312672_0012645161_GHNW.TIF|6131210624_7312672_0012645161_GHNW_2.TIF|6134363289_7313030_0012652442_GHNW.TIF|6134363289_7313030_0012652442_GHNW_2.TIF|6134453353_7312785_0012652095_GHNW.TIF|6331584838_7316476_0012657508_GHNW.TIF|6331584838_7316476_0012657508_GHNW_2.TIF|6332227973_7316021_0012657967_GHNW.TIF|6332227973_7316021_0012657967_GHNW_2.TIF|6332227973_7316021_0012657967_GHNW_3.TIF|6332470037_7314509_0012649626_GHNW.TIF|6397678279_7314458_0012652240_GHNW.TIF|6397678279_7314458_0012652240_GHNW_2.TIF|6397678279_7314458_0012652240_GHNW_3.TIF|6397678279_7314458_0012652240_GHNW_4.TIF|7073324753_7315756_0012646369_GHNW.TIF|7073324753_7315756_0012646369_GHNW_2.TIF|7241285584_7316513_0012659150_GHNW.TIF|7241285584_7316513_0012659150_GHNW_2.TIF|7242231066_7313868_0012652991_GHNW.TIF|7242231066_7313868_0012652991_GHNW_2.TIF|8102537033_7313844_0012650787_GHNW.TIF|8102537033_7313844_0012650787_GHNW_2.TIF|8102537033_7313844_0012650787_GHNW_3.TIF|8102537033_7313844_0012650787_GHNW_4.TIF|8207338148_7314441_0012655660_GHNW.TIF|8207338148_7314441_0012655660_GHNW_2.TIF|8207338148_7314441_0012655660_GHNW_3.TIF|8207338148_7314441_0012655660_GHNW_4.TIF|8405585682_7306799_0012599313_GHNW.TIF|8405585682_7306799_0012599313_GHNW_2.TIF|8405585682_7306799_0012599313_GHNW_3.TIF|8405585682_7306799_0012599313_GHNW_4.TIF|8405585682_7306799_0012599313_GHNW_5.TIF|8405585682_7306799_0012599313_GHNW_6.TIF|8405585682_7306799_0012599313_GHNW_7.TIF|8902590339_7316280_0012657785_GHNW.TIF|8902590339_7316280_0012657785_GHNW_2.TIF|8902590339_7316280_0012657785_GHNW_3.TIF|8902590339_7316280_0012657785_GHNW_4.TIF|8905536107_7314312_0012650937_GHNW.TIF|"
//);
		
		/*
		File f = new File("O:\\DE-O-01\\GRM-CC\\CalcKernels_Transfer\\2003462454_7314031_0012655046_GHNW.TIF");
		Main.uploadFile(f);
		*/
	}

}
