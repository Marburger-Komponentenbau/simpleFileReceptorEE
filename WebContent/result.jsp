<%@ page 
	language="java" 
	contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="java.util.StringTokenizer, java.io.File, javax.xml.bind.DatatypeConverter, com.commerzbank.simpleFileReceptorEE.StaticUtils" 
    %>
<!DOCTYPE html>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">   -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
String contextPath = request.getContextPath();
if(contextPath == null || contextPath.length() < 1) {
	contextPath = "Receptor";
}
else{
	contextPath = contextPath.replace("/","");
}
%>
<title><% out.print(contextPath); %>  -  OCR Results</title>

<script type="text/javascript" src="./jquery.js"></script>
<script type="text/javascript" src="./splitter.js"></script>

<script type="text/javascript">

function loadXMLTransformed(btn, file, target, repLink) {
    var xmlhttp;
	var node = document.getElementById(target);
    var btnNode = document.getElementById(btn);
    if(node.childNodes != null && node.childNodes.length > 0 && node.firstChild.className !== "msg" ){
    	if(node.firstChild.className !== "status"){
			if(node.style.display !== 'none'){
				node.style.display = 'none';
				btnNode.style.opacity = 0.5;
			}
			else{
				node.style.display = 'block';
				btnNode.style.opacity = 1;
			}
    	}
    	return;
    }
    if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest(); // code for IE7+, Firefox, Chrome, Opera, Safari
    } 
    else {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); // code for IE6, IE5
    }
    node.innerHTML = "<p class=\"status\">loading ...</p>";
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
           if(node.style.display !== 'block'){
  			  node.style.display = 'block';
  			  btnNode.style.opacity = 1;
  		   }
           if(xmlhttp.status == 200){
        	   if(repLink){
            	   node.innerHTML = "<p class=\"center\"><a href=\"./report?folder=xml&file=".concat(file).concat("\" class=\"xmlLoadButton\" target=\"_blank\" download>download report</a></p>").concat(xmlhttp.responseText);        		   
        	   }
        	   else{
            	   node.innerHTML = xmlhttp.responseText;        		           		   
        	   }
           }
           else {
              node.innerHTML = "<p class=\"msg\">bin noch nicht so weit! <br/>versuchen Sie es bitte gleich nochmal...</p>";
           }
        }
    };
    xmlhttp.open("GET", "/<% out.print(contextPath); %>/transform?folder=xml&file=".concat(file).concat("&t=").concat( new Date().getTime() ), true);
    xmlhttp.send();
}

function display(node, btnNode){
    if(node == null){
		return;
	}
	if(btnNode.style.opacity != 0.5){
	  btnNode.style.opacity = 0.5;
	}
	else{
	  btnNode.style.opacity = 1;
	}
	var node = node.nextSibling;
    while(node != null){
    	if(node.nodeType != 3){
          if(node.style.display !== 'none'){
            node.style.display = 'none';
          }
          else{
            node.style.display = 'block';
          }
    	}
		node = node.nextSibling;		
    }
}
</script>

<style>
.Splitter {
    min-width: 770px;
    min-height: 810px;
	margin: 0em 0em 0em 0em;
	border: 4px solid #FFCF31;
}
.LeftPane {
	overflow: auto;
	/* No margin or border allowed */
}
.RightPane {
	overflow: auto;
	/* No margin or border allowed */
}
.Splitter .vsplitbar {
	width: 4px;
	background: #FFCF31 url(vgrabber.gif) no-repeat center;
}
.Splitter .vsplitbar.active {
    background: #FFCF31 url(vgrabber.gif) no-repeat center;
	opacity: 0.7;
}

body {
    color: black;
    font-family: Segoe UI, Tahoma,Verdana, Helvetica, Arial, sans-serif; 
}
.kernelLogo {
    text-align: left;
	padding-left: 10px;
	font-size: 80%;
	float: left;
}
.cobaLogo {
    text-align: right;
	padding-right: 10px;
   	font-size: 80%;
	float: clear;
    
}
#title {
	background-color: #FFCF31;
    padding: 0px 0px 10px 0px;
	margin: 2px 0px 4px 0px;	
}
.title {
	text-align: left;
    padding: 0px 0px 0px 10px;
	margin: 0px 0px 0px 0px;	
    color: black;
	font-size: 133%;
	font-weight: bold;
	float: left;
}
.nav {
    padding: 0px 10px 0px 0px;
	margin: 0px 0px 0px 0px;
    text-align: right;   
    float: clear;
}
.nav a{
    text-decoration: none;
    color: black; 
    font-size: 81%;
    font-weight: normal;
}
div.Res {
	font-size: 90%;
}
a {
    text-decoration: none;
    color: black; 
    font-weight: bold;
}
ul.ZipFilesList {
    padding: 0px 0px 0px 0px;
	margin: 0px 0px 0px 0px;
	list-style-type: none;
}
li.ZipFileListItem{
    padding: 0px 0px 0px 0px;
	margin: 4px 0px 0px 0px;
}
li.ZipFileListItem div.ZipFile{
    padding: 0px 0px 0px 10px;
	margin: 0px 0px 0px 0px;
	background-color: #FFCF31;
}
ul.ImageFilesList {
    padding: 0px 0px 0px 12px;
	margin: 0px 0px 0px 0px;
	list-style-position: inside;
}
li.ImageFileListItem {
    padding: 0px 0px 0px 0px;
	margin: 3px 0px 0px 0px;
}
li.ImageFileListItem embed,
li.ImageFileListItem object,
li.ImageFileListItem img {
    min-width: 500px;
    min-height: 770px;
    width: 750px;   
    display: block;
    font-size: 90%;
}
div.RecognitionResultAndLink, 
div.KernelResponseAndLink{
    padding: 0px 0px 0px 0px;
	margin: 3px 0px 12px 4px;
	font-size: 90%;
}
div.RecognitionResultAndLink ul, 
div.KernelResponseAndLink ul{
    padding: 0px 0px 0px 0px;
	margin: 0px 0px 0px 16px;
}
div.RecognitionResultAndLink li, 
div.KernelResponseAndLink li{
    padding: 0px 0px 0px 0px;
	margin: 0px 0px 0px 0px;
}
a.ImageFileLink {
    text-decoration: none;
    color: black; 
    font-size: 90%;
    font-weight: normal;
}
a.ZipFileLink {
    text-decoration: none;
    color: black; 
    font-size: 90%;
    font-weight: normal;
}
.msg, .status {
	text-align: center;
	color: red; 
}
p.center {
	text-align: center; 
}
</style>

</head>
<body>
 
<div class="full_widthx"></div>
<div class="kernelLogo">01-15-42&nbsp;|&nbsp;Risk Calculation Kernels</div>
<div class="cobaLogo"><img src="https://portal.comproof.net/vpn/images/CoBa.png"></div> 
<div id="title">
	<div class="title"><% out.print(contextPath); %></div> 
	<div class="nav"><a href="./">documents</a>&nbsp;|&nbsp;<a href="upload.jsp">upload</a></div>
</div>

<div class="Res">
<% 
int splitterid = 0;
Object zipFilenamesObj = request.getAttribute("createdZips");
if(zipFilenamesObj == null){
	out.println( "<p class=\"msg\"> invalider Aufruf ... </p>" );
}
else{
	String doReport = "false";
	if(contextPath.startsWith("DiGest")){
		doReport = "true";
	}
	String zipFilenames = zipFilenamesObj.toString();
	if(zipFilenames.length() < 2){
		out.println( "<p class=\"msg\"> keine Daten verarbeitet ... </p>" );
	}
	StringTokenizer st = new StringTokenizer( zipFilenames, "|" );		
	out.println( "<ul class=\"ZipFilesList\">" );
	StaticUtils helper = StaticUtils.getInstance();
	while ( st.hasMoreTokens() ){
		String zipFilename = st.nextToken();	
		String caseKey = helper.getKeyOfFile(zipFilename);
		out.println( "<li class=\"ZipFileListItem\">" );
		
		out.println( "<div class=\"ZipFile\">" );	
		out.println( "<a href=\"javascript:void(0)\" onclick=\"display(this.parentNode, this);\">" + caseKey + "</a>" );//
		out.println( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		out.println( "<a href=\"/" + contextPath + "/download64?folder=zip&file=" + new String( DatatypeConverter.printBase64Binary( zipFilename.getBytes() ) ) + "\" class=\"ZipFileLink\">download" + "</a>" );//
		out.println( "</div>" );
		
		out.println( "<div id=\"Splitter" + Integer.toString(++splitterid) + "\" class=\"Splitter\">" );
		out.println( "<div class=\"LeftPane\">" );	
		
		Object imgFilenamesObj = request.getAttribute( zipFilename );
		if(imgFilenamesObj == null){
			out.println("no images" );
		}
		else{
			String imgFilenames = imgFilenamesObj.toString();
			StringTokenizer stImg = new StringTokenizer( imgFilenames, "|" );		
			out.println( "<ul class=\"ImageFilesList\">" );
			while ( stImg.hasMoreTokens() ){
				String imgFilename = stImg.nextToken();	
				String imgFilenameLowerCase = imgFilename.toLowerCase();
								
				if(imgFilenameLowerCase.endsWith(".pdf")){
					String imgFilename64 = new String( DatatypeConverter.printBase64Binary( imgFilename.getBytes() ) );

					out.println( "<li class=\"ImageFileListItem\">" );
					out.print( "<a href=\"javascript:void(0)\" onclick=\"display(this.nextSibling.nextSibling,this);\">" + imgFilename + "</a>" );//
					
						out.print(  "<a href=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" class=\"ImageFileLink\" target=\"_blank\" download>" );//
						out.println( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
						out.println( "download" );//					
						out.println( "</a>" );//					
	
						out.println( "<object data=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" type=\"application/pdf\">" );
						out.println( "kein pdf - plugin vorhanden! Fuer Firefox siehe: " );
						out.println( "<a href=\"./helpFirefoxPlugin.html\" class=\"ImageFileLink\" target=\"_blank\">\"<b>helpFirefoxPlugin.html</b>\"</a>" );//
						out.println( "</object>" );
							
					out.println( "</li>" );
				}
				else{
					File[] jpgFiles = null;
					if(  (imgFilenameLowerCase.endsWith(".tif") || imgFilenameLowerCase.endsWith(".tiff")) &&
						 ( jpgFiles = helper.getFiles(imgFilename, contextPath) ).length > 0
						){
						for(File f : jpgFiles){
							System.out.println(f.getAbsolutePath());
							
							imgFilename = f.getName();	
							String imgFilename64 = new String( DatatypeConverter.printBase64Binary( imgFilename.getBytes() ) );
							String imgFolderParam = "inCon";
							
							out.println( "<li class=\"ImageFileListItem\">" );
							out.print( "<a href=\"javascript:void(0)\" onclick=\"display(this.nextSibling.nextSibling,this);\">" + imgFilename + "</a>" );//
							
								out.println( "<a href=\"/" + contextPath + "/download64?folder=" + imgFolderParam + "&file=" + imgFilename64 + "\" class=\"ImageFileLink\" target=\"_blank\" download>" );//
								out.println( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");					
								out.println( "download" );
								out.println( "</a>" );
								out.println( "<img class=\"ImageFileImg\" src=\"/" + contextPath + "/download64?folder=" + imgFolderParam + "&file=" + imgFilename64 + "\" alt=\"no plugin for this image" + "\" >" );// style=\"width:304px;height:228px;\"
										
							out.println( "</li>" );													
						}
					}
					else{
						String imgFilename64 = new String( DatatypeConverter.printBase64Binary( imgFilename.getBytes() ) );
						String imgFolderParam = "in";
						
						out.println( "<li class=\"ImageFileListItem\">" );
						out.print( "<a href=\"javascript:void(0)\" onclick=\"display(this.nextSibling.nextSibling,this);\">" + imgFilename + "</a>" );//
						
							out.println( "<a href=\"/" + contextPath + "/download64?folder=" + imgFolderParam + "&file=" + imgFilename64 + "\" class=\"ImageFileLink\" target=\"_blank\" download>" );//
							out.println( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");					
							out.println( "download" );
							out.println( "</a>" );			
							out.println( "<img class=\"ImageFileImg\" src=\"/" + contextPath + "/download64?folder=" + imgFolderParam + "&file=" + imgFilename64 + "\" alt=\"no plugin for this image" + "\" >" );// style=\"width:304px;height:228px;\"
							
						out.println( "</li>" );
					}
				}
			}
			out.println( "</ul>" );
		}		
		out.println( "</div>" ); // LeftPane		
		
		out.println("<div class=\"RightPane\">" );
		String requestXmlFilename = "request_" + caseKey + ".xml";
		out.println(
				     "<div class=\"RecognitionResultAndLink\">" + 
					 "<a href=\"javascript:loadXMLTransformed('RecognitionLink','" + requestXmlFilename +
				     "','" + requestXmlFilename + "',false)\"" +
		             " class=\"xmlLoadButton\" id=\"RecognitionLink\">Recognition Result" + "</a>:&nbsp;<br/>" + requestXmlFilename  +
				     "<div id=\"" + requestXmlFilename + "\" class=\"RecognitionResult\"></div>" +  
				     "</div>" 
					);		
		String responseXmlFilename = "response_" + caseKey + ".xml";
		out.println(
				     "<div class=\"KernelResponseAndLink\">" + 
		             "<a href=\"javascript:loadXMLTransformed('KernelLink','" + responseXmlFilename +
				     "','" + responseXmlFilename + "'," + doReport + ")\"" +
		             " class=\"xmlLoadButton\" id=\"KernelLink\">Rechenkern Response" + "</a>:&nbsp;<br/>" + responseXmlFilename +
//			           "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\"/" + contextPath + "/report?folder=xml&file=" + "RKENTW_SCAN185400_2016-06-14_16-13-57_354_response.xml" + "\" class=\"xmlLoadButton\" target=\"_blank\" download>" +
//			           "report</a>" +	           
					 "<div id=\"" + responseXmlFilename + "\" class=\"KernelResponse\"></div>" + 
					 "</div>" 
					);	
		out.println( "</div>" ); // RightPane
		
	    out.println( "</div>" ); // Splitter
		
		//out.println("</div>");
		out.println("</li>");
	}
	out.println( "</ul>" );
}
%>
</div>

</body>
<script type="text/javascript">
$().ready(function() {
	<%
	int i = 0;
	while (++i <= splitterid){
		out.println( "$(\"#Splitter" + Integer.toString(i) + "\").splitter({" +
							"type: \"v\"," +
							"outline: false," +
							"minLeft: 100, sizeLeft: 800, minRight: 100," +
							"resizeToWidth: true" +
						"});"
				);
	}
	%>	
});
</script>
</html>