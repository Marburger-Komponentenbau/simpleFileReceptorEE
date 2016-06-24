<%@ page 
	language="java" 
	contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="java.util.StringTokenizer, java.io.File, javax.xml.bind.DatatypeConverter, com.conetex.simpleFileReceptorEE.StaticUtils" 
    %>
<!-- <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">   -->
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
String contextPath = request.getContextPath();
//String contextPath = request.getRequestURI();
if(contextPath == null || contextPath.length() < 1) {
	contextPath = "Receptor";
}
else{
	contextPath = contextPath.replace("/","");
}
%>
<title><% out.print(contextPath); %>  -  OCR Results</title>

<script type="text/javascript" src="./jquery.js"></script>
<!-- <script type="text/javascript" src="./jquery.cookie.js"></script>  -->
<script type="text/javascript" src="./splitter.js"></script>

<script type="text/javascript">

function loadXMLTransformed(btn, file, target) {
    var xmlhttp;

    var node = document.getElementById(target);
    var btnNode = document.getElementById(btn);
    if(node.childNodes != null && node.childNodes.length > 0 && node.firstChild.className !== "msg" ){//
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
        //alert(node.firstChild.nodeType);
		return;
    }
    if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    node.innerHTML = "<p class=\"status\">loading ...</p>";
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
           //alert(xmlhttp.responseText);
    	   if(node.style.display !== 'block'){
  			  node.style.display = 'block';
  			  btnNode.style.opacity = 1;
  		   }
           if(xmlhttp.status == 200){
        	   node.innerHTML = xmlhttp.responseText;
           }
           else {
              //alert("mein error: ".concat(xmlhttp.status));
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
	//var disp = 'block';
	//var deco = 'none';
	if(btnNode.style.opacity != 0.5){
	  //node.style.fontWeight = 'lighter'; // "none|underline|overline|line-through|blink|initial|inherit" 
	  btnNode.style.opacity = 0.5;
	  //disp = 'block';//none
	  //deco = 'line-through';
	}
	else{
	  //node.style.fontWeight = 'bold';
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
		//node.style.display = disp;
		//node.style.textDecoration = deco;
		node = node.nextSibling;		
    }
/*
	if(node.style.textDecoration !== 'line-through'){
	  node.style.textDecoration = 'line-through'; // "none|underline|overline|line-through|blink|initial|inherit" 
	}
	else{
	  node.style.textDecoration = 'none';
	}
*/
}

</script>

<style>

.Splitter {
    min-width: 770px;
    min-height: 810px;
	margin: 0em 0em 0em 0em;
	border: 4px solid #FFCF31;
	/*border: 4px solid #bdb;*/
	/* No padding allowed */
}
.LeftPane {
	/*background: #efe;
	background: green;*/
	overflow: auto;
	/* No margin or border allowed */
}
.RightPane {
	/*background: #f8fff8;
	background: red;*/
	overflow: auto;
	/* No margin or border allowed */
}
.Splitter .vsplitbar {
	width: 4px;
	background: #FFCF31 url(vgrabber.gif) no-repeat center;
	/* background: #aca url(vgrabber.gif) no-repeat center; */
}
.Splitter .vsplitbar.active {
    background: #FFCF31 url(vgrabber.gif) no-repeat center;
	/* background: #da8 url(vgrabber.gif) no-repeat center; */
	opacity: 0.7;
}

body {
/* padding: 10px; */
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

/*
.linkButton:link, .linkButton:visited {
    background-color: #FFCF31;
    color: black;
    padding: 6px 12px;
    text-align: center; 
    text-decoration: none;
    display: inline-block;
    border: 2px solid black; 
    font-weight: bold;
}

.linkButton:hover, .linkButton:active {
    background-color: black;
    color: #FFCF31;
}
*/

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
	/*list-style-position: inside;*/
	
}

li.ZipFileListItem{
    padding: 0px 0px 0px 0px;
	margin: 4px 0px 0px 0px;
	
	/*border: 1px solid black;*/ 
}

li.ZipFileListItem div.ZipFile{
    padding: 0px 0px 0px 10px;
	margin: 0px 0px 0px 0px;
	background-color: #FFCF31;
}

ul.ImageFilesList {
    padding: 0px 0px 0px 12px;
	margin: 0px 0px 0px 0px;
	/*text-align: right;*/
	list-style-position: inside;
	/*border: 1px solid red;*/ 
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
    /*font-weight: lighter;*/
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

  <!-- 
	div.ZipFile
	 a
	
		

margin-left: 35px;
ul.ZipFilesList
    li.ZipFileListItem
	  div.ZipFile
	    a
	  div.ImageFiles
	    ul.ImageFilesList
		  li.ImageFileListItem
		    object a    width=\"300\" height=\"200\"
			a img
	  div.xml
	   div.RecognitionResultAndLink
	    div
		a
	   div.KernelResponseAndLink
	    div
		a
-->


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
	out.println( 
	"<p class=\"msg\">" +
	"invalider Aufruf ..."+
	"</p>"
//	+
//	"<p style=\"text-align: center;\">" +
//	"<a href=\"./\" class=\"linkButton\">back</a>" +
//	"</p>" 
	);
}
else{
	String zipFilenames = zipFilenamesObj.toString();
	if(zipFilenames.length() < 2){
		out.println( 
		"<p class=\"msg\">" +
		"keine Daten verarbeitet ..."+
		"</p>" 
		);
	}
	//out.println( zipFiles );
	StringTokenizer st = new StringTokenizer( zipFilenames, "|" );		
	out.println( "<ul class=\"ZipFilesList\">" );
	StaticUtils helper = StaticUtils.getInstance();
	while ( st.hasMoreTokens() ){
		String zipFilename = st.nextToken();	
		String caseKey = helper.getKeyOfFile(zipFilename);
		//out.println( "<a href=\"/download?folder=zip&file=" + filename + "\"> a:" + filename + "</a><br/>" );//" + contextPath + "
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
			out.println( "<ul class=\"ImageFilesList\">" );// style=\"list-style-type:square\"
			while ( stImg.hasMoreTokens() ){
				String imgFilename = stImg.nextToken();	
				String imgFilenameLowerCase = imgFilename.toLowerCase();
								
				//out.println( "<a href=\"/download?folder=zip&file=" + filename + "\"> a:" + filename + "</a><br/>" );//" + contextPath + "
				if(imgFilenameLowerCase.endsWith(".pdf")){
					String imgFilename64 = new String( DatatypeConverter.printBase64Binary( imgFilename.getBytes() ) );

					out.println( "<li class=\"ImageFileListItem\">" );
					out.print( "<a href=\"javascript:void(0)\" onclick=\"display(this.nextSibling.nextSibling,this);\">" + imgFilename + "</a>" );//
					
						out.print(  "<a href=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" class=\"ImageFileLink\" target=\"_blank\" download>" );//
						out.println( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
						out.println( "download" );//					
						out.println( "</a>" );//					
	
						//out.println( "<object data=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" type=\"application/x-msoffice14\">" );
						//out.println( "</object>" );
	
						out.println( "<object data=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" type=\"application/pdf\">" );
						out.println( "</object>" );
						
						//out.println( "<embed src=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" alt=\"pdf\" pluginspage=\"http://www.adobe.com/products/acrobat/readstep2.html\" />" );
						//out.println( "<embed src=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" type=\"application/pdf\" >" );
						 
						//out.println( "<div><iframe style=\"float:none;display:inline;height:300px;\" src=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" frameborder=\"0\"></iframe><br></div>" );
					
					out.println( "</li>" );
					
					/*
					
<iframe src="http://docs.google.com/gview?url=http://domain.com/pdf.pdf&embedded=true" 
style="width:600px; height:500px;" frameborder="0"></iframe>					
					
					*/
					
				}
				else{
					
					File[] jpgFiles = null;
					
					if( (imgFilenameLowerCase.endsWith(".tif") || imgFilenameLowerCase.endsWith(".tiff")) &&
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
								//out.println( imgFilename );//
								out.println( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");					
								out.println( "download" );//
								out.println( "</a>" );//					
								out.println( "<img class=\"ImageFileImg\" src=\"/" + contextPath + "/download64?folder=" + imgFolderParam + "&file=" + imgFilename64 + "\" alt=\"no plugin for this image" + "\" >" );// style=\"width:304px;height:228px;\"
								
								//out.println( "<img src=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\">" );// style=\"width:304px;height:228px;\"
								//out.println( "<img src=\"/" + contextPath + "/B.TIF\" />" );// style=\"width:304px;height:228px;\"
								//out.println( "<img src=\"/" + contextPath + "/A.jpg\" />" );// style=\"width:304px;height:228px;\"
								
								//out.println( "<object data=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" type=\"application/tiff\">" );
								//out.println( "</object>" );						
							
							out.println( "</li>" );							
							
						}
					}
					else{
						String imgFilename64 = new String( DatatypeConverter.printBase64Binary( imgFilename.getBytes() ) );
						String imgFolderParam = "in";
						
						out.println( "<li class=\"ImageFileListItem\">" );
						out.print( "<a href=\"javascript:void(0)\" onclick=\"display(this.nextSibling.nextSibling,this);\">" + imgFilename + "</a>" );//
						
							out.println( "<a href=\"/" + contextPath + "/download64?folder=" + imgFolderParam + "&file=" + imgFilename64 + "\" class=\"ImageFileLink\" target=\"_blank\" download>" );//
							//out.println( imgFilename );//
							out.println( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");					
							out.println( "download" );//
							out.println( "</a>" );//					
							out.println( "<img class=\"ImageFileImg\" src=\"/" + contextPath + "/download64?folder=" + imgFolderParam + "&file=" + imgFilename64 + "\" alt=\"no plugin for this image" + "\" >" );// style=\"width:304px;height:228px;\"
							
							//out.println( "<img src=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\">" );// style=\"width:304px;height:228px;\"
							//out.println( "<img src=\"/" + contextPath + "/B.TIF\" />" );// style=\"width:304px;height:228px;\"
							//out.println( "<img src=\"/" + contextPath + "/A.jpg\" />" );// style=\"width:304px;height:228px;\"
							
							//out.println( "<object data=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" type=\"application/tiff\">" );
							//out.println( "</object>" );						
						
						out.println( "</li>" );
						
					}


					
					
					
				}
				
				
			}
			out.println( "</ul>" );
		}		
		out.println( "</div>" );		
		
		out.println( "<div class=\"RightPane\">" );
		
		//String requestXmlFilename = "request_170900_2005334297_7298683_0012606090.xml";//String requestXmlFilename = "request_" + zipFilename.replace(".zip", ".xml");
		String requestXmlFilename = "request_" + caseKey + ".xml";
		
		//out.println( "<a href=\"/" + contextPath + "/download?folder=res&file=response_" + zipFilename.replace(".zip", ".xml") + "\">RK-Response: response_" + zipFilename.replace(".zip", ".xml") + "</a>" );//
		out.println(
				     "<div class=\"RecognitionResultAndLink\">" + 
					 "<a href=\"javascript:loadXMLTransformed('RecognitionLink','" + requestXmlFilename +
				     "','" + requestXmlFilename + "')\"" +
		             " class=\"xmlLoadButton\" id=\"RecognitionLink\">Recognition Result" + "</a>:&nbsp;<br/>" + requestXmlFilename  +
				     "<div id=\"" + requestXmlFilename + "\" class=\"RecognitionResult\"></div>" +  
				     "</div>" 
		             
				   );		

		//String responseXmlFilename = "response_170900_2005334297_7298683_0012606090.xml";
		String responseXmlFilename = "response_" + caseKey + ".xml";
		out.println(
				    "<div class=\"KernelResponseAndLink\">" + 
		             "<a href=\"javascript:loadXMLTransformed('KernelLink','" + responseXmlFilename +
				     "','" + responseXmlFilename + "')\"" +
		             " class=\"xmlLoadButton\" id=\"KernelLink\">Rechenkern Response" + "</a>:&nbsp;<br/>" + responseXmlFilename +
					 "<div id=\"" + responseXmlFilename + "\" class=\"KernelResponse\"></div>" + 
				     "</div>" 
		             
				);
		
		out.println( "</div>" );	
		out.println( "</div>" );
		
		out.println( "</li>" );
	}
	out.println( "</ul>" );
}

%>

</div>

<!--
<div id="myDiv">
</div>
<a href="javascript:loadXMLTransformed('xml','response_170900_2005334297_7298683_0012606090.xml','myDiv')" class="linkButton">load</a>
-->

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
		/*
							"cookie: \"vsplitter" + Integer.toString(i) + "\"," +
							"accessKey: 'I'" +		
		*/
	}
	%>	
});

</script>
</html>