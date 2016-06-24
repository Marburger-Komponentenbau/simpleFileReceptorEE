<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="com.conetex.simpleFileReceptorEE.StaticUtils, java.io.File, java.util.Arrays" 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
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

<script type="text/javascript">
function submitZipSendScanForm()
{
	var current = document.getElementById("uploadedFiles");
	current.value = getChecked( document.getElementById('fileList') );
	//alert(current.value);
	document.ZipSendScanForm.submit();
}
function checkAll(node, trueOrFalse)
{
	if(node.tagName === "INPUT" && node.type === "checkbox"){
		node.checked = trueOrFalse;
	}
	else{
		if(node.childNodes){
			var i = 0;
			while(i < node.childNodes.length)
			{		
				checkAll(node.childNodes[i], trueOrFalse);
				i++;
			}
		}
	}
}
function getChecked(node)
{
	if(node.tagName === "INPUT" && node.type === "checkbox"){
		if(node.checked === true){
			return node.id.concat("|");
		}
	}
	else{
		if(node.childNodes){
			var re = "";
			var i = 0;
			while(i < node.childNodes.length)
			{		
				re = re.concat( getChecked(node.childNodes[i]) );
				i++;
			}
			return re;
		}
	}
	return "";
}
</script>


<style>

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

.linkButtonSmall:link, .linkButtonSmall:visited {
    background-color: #FFCF31;
    color: black;
    padding: 2px 8px 2px 8px;
    text-align: center; 
    text-decoration: none;
    display: inline-block;
    border: 2px solid black; 
    font-weight: normal;
    font-size: 90%;
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

.linkButton:hover, .linkButton:active 
.linkButtonSmall:hover, .linkButtonSmall:active {
    background-color: black;
    color: #FFCF31;
}



div.Res {
	font-size: 75%;	
  /*  overflow-x: scroll!important;
  overflow-y: hidden;*/	
}

#fileList{
	color: black;
    -webkit-column-count: 3; /* Chrome, Safari, Opera */
    -moz-column-count: 3; /* Firefox */
     column-count: 3;
	/*
	overflow-x: scroll!important;
	*/
}

#fileList span{
	overflow-x: hidden;
	background-color: white;
	white-space: nowrap;

	/*overflow-x: scroll!important;
	width: 100%;*/
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

<div style="text-align: left;">
<a href="javascript:void(0)" onclick="javascript:checkAll(document.getElementById('fileList'), true);" class="linkButtonSmall"> check All </a>
&nbsp;
<a href="javascript:void(0)" onclick="javascript:checkAll(document.getElementById('fileList'), false);" class="linkButtonSmall"> uncheck All </a>
</div>

<div class="Res">
<p id="fileList">

<%
	StaticUtils helper = StaticUtils.getInstance();
	File folder = helper.getDataFolder(contextPath);
	File[] files = folder.listFiles();
	Arrays.sort(files);
	if (files != null) {
		for (File file : files) {
			String fname = file.getName();
			String fnameLc = fname.toLowerCase();
			if(fnameLc.endsWith(".tif") || fnameLc.endsWith(".pdf") || fnameLc.endsWith(".tiff")){
				//out.println("<li class=\"fileListItem\">");
				out.println("<span><input id=\"" + fname + "\" type=\"checkbox\" />");//class=\"noO\"
				out.println(fname);
				out.println("</span><br />");//
				//out.println("</li>");
			}
		}
	}	
%>
</p>
</div> 

<p style="text-align: center;">
<%
	out.println("<a href=\"javascript: submitZipSendScanForm()\" class=\"linkButton\">recognize!</a>");
%>
</p>
<form name="ZipSendScanForm" class="inline" method="post" action="/<% out.print(contextPath); %>/ZipSendScan">
  <input type="hidden" name="uploadedFiles" id="uploadedFiles" value="|">
</form>

</body>
</html>