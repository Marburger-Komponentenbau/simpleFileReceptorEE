<%@ page 
	language="java" 
	contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="java.util.StringTokenizer, java.util.Base64" 
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
    if(node.childNodes != null && node.childNodes.length > 0 && node.firstChild.nodeType != 3){//
		if(node.style.display !== 'none'){
			node.style.display = 'none';
			btnNode.style.opacity = 0.5;
		}
		else{
			node.style.display = 'block';
			btnNode.style.opacity = 1;
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
    node.innerHTML = "<p>loading ...</p>";
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
              node.innerHTML = "bin noch nicht so weit! versuchen Sie es gleich nochmal...";
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
    min-height: 770px;
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
.title {
	text-align: left;
	padding-left: 10px;
	padding-right: 10px;
	font-size: 133%;
	font-weight: bold;
    background-color: #FFCF31;
	margin-bottom: 2px;
    margin-top: 2px;
}

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
	margin: 0px 0px 0px 12px;
	
	/*list-style-position: inside;*/
	border: 1px solid red; 
}

li.ZipFileListItem{
    padding: 0px 0px 0px 0px;
	margin: 3px 0px 0px 0px;
	/*border: 1px solid black;*/ 
}

li.ZipFileListItem div.ZipFile{
    padding: 0px 0px 0px 0px;
	margin: 0px 0px 0px 0px;
	border: 1px solid green; 
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
	border: 1px solid black; 
}

li.ImageFileListItem object,
li.ImageFileListItem a.ImageFileLink img {
    min-width: 500px;
    min-height: 750px;
}

		div.RecognitionResultAndLink, 
		div.KernelResponseAndLink{
    padding: 0px 0px 0px 0px;
	margin: 3px 0px 12px 12px;
	border: 1px solid black; 
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
 <div class="kernelLogo">01-15-42 &nbsp;|&nbsp; Risk Calculation Kernels</div>
 <div class="cobaLogo"><img src="https://portal.comproof.net/vpn/images/CoBa.png"></div> 

 <div class="title"><% out.print(contextPath); %></div> <!---->



<div class="Res">
<% 
int splitterid = 0;
Object zipFilenamesObj = request.getAttribute("createdZips");
if(zipFilenamesObj == null){
	out.println( "???" );
}
else{
	String zipFilenames = zipFilenamesObj.toString();
	if(zipFilenames.length() < 2){
		out.println( 
		"<p style=\"text-align: center;\">" +
		"keine Daten verarbeitet ..."+
		"</p>" +
		
		"<p style=\"text-align: center;\">" +
		"<a href=\"./\" class=\"linkButton\">back</a>" +
		"</p>" 
		);
	}
	//out.println( zipFiles );
	StringTokenizer st = new StringTokenizer( zipFilenames, "|" );		
	out.println( "<ul class=\"ZipFilesList\">" );
	while ( st.hasMoreTokens() ){
		String zipFilename = st.nextToken();	
		//out.println( "<a href=\"/download?folder=zip&file=" + filename + "\"> a:" + filename + "</a><br/>" );//" + contextPath + "
		out.println( "<li class=\"ZipFileListItem\">" );
		
		out.println( "<div class=\"ZipFile\">Input: " );	
		out.println( "<a href=\"javascript:void(0)\" onclick=\"display(this.parentNode, this);\">" + zipFilename + "</a>" );//
		out.println( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		out.println( "<a href=\"/" + contextPath + "/download64?folder=zip&file=" + new String( Base64.getEncoder().encode( zipFilename.getBytes() ) ) + "\">download" + "</a>" );//
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
				String imgFilename64 = new String( Base64.getEncoder().encode( imgFilename.getBytes() ) );
								
				//out.println( "<a href=\"/download?folder=zip&file=" + filename + "\"> a:" + filename + "</a><br/>" );//" + contextPath + "
				out.println( "<li class=\"ImageFileListItem\">" );
				
				out.print( "<a href=\"javascript:void(0)\" onclick=\"display(this,this);\">" + imgFilename + "</a>" );//
				if(imgFilenameLowerCase.endsWith(".pdf")){
					//out.println( "<object data=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" type=\"application/pdf\">" );
					out.print(  "<a href=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" class=\"ImageFileLink\">" );//
					out.println( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					out.println( "download</a>" );//					
					out.println( "<embed src=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" alt=\"pdf\" pluginspage=\"http://www.adobe.com/products/acrobat/readstep2.html\" />" );
					//out.println( "</object>" );
					
					/*
					
<iframe src="http://docs.google.com/gview?url=http://domain.com/pdf.pdf&embedded=true" 
style="width:600px; height:500px;" frameborder="0"></iframe>					
					
					*/
					
				}
				else{
					out.println( "<a href=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" class=\"ImageFileLink\">" );//
					out.println( imgFilename );//
					out.println( "<img class=\"ImageFileImg\" src=\"/" + contextPath + "/download64?folder=in&file=" + imgFilename64 + "\" alt=\"" + imgFilename + "\" />" );// style=\"width:304px;height:228px;\"
					out.println( "</a>" );//					
				}
				
				out.println( "</li>" );
			}
			out.println( "</ul>" );
		}		
		out.println( "</div>" );		
		
		out.println( "<div class=\"RightPane\">" );
		
		String requestXmlFilename = "request_170900_2005334297_7298683_0012606090.xml";//response_" + zipFilename.replace(".zip", ".xml")
		//out.println( "<a href=\"/" + contextPath + "/download?folder=res&file=response_" + zipFilename.replace(".zip", ".xml") + "\">RK-Response: response_" + zipFilename.replace(".zip", ".xml") + "</a>" );//
		out.println(
				     "<div class=\"RecognitionResultAndLink\">" + 
					 "<a href=\"javascript:loadXMLTransformed('RecognitionLink','" + requestXmlFilename +
				     "','" + requestXmlFilename + "')\"" +
		             " class=\"xmlLoadButton\" id=\"RecognitionLink\">Recognition Result" + "</a>:&nbsp;" + requestXmlFilename  +
				     "<div id=\"" + requestXmlFilename + "\" class=\"RecognitionResult\"></div>" +  
				     "</div>" 
		             
				   );		

		String responseXmlFilename = "response_170900_2005334297_7298683_0012606090.xml";
		out.println(
				    "<div class=\"KernelResponseAndLink\">" + 
		             "<a href=\"javascript:loadXMLTransformed('KernelLink','" + responseXmlFilename +
				     "','" + responseXmlFilename + "')\"" +
		             " class=\"xmlLoadButton\" id=\"KernelLink\">Rechenkern Response" + "</a>:&nbsp;" + responseXmlFilename +
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
							"minLeft: 100, sizeLeft: 540, minRight: 100," +
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