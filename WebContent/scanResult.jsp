<%@ page 
	language="java" 
	contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="java.util.StringTokenizer" %>
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

<script type="text/javascript" src="//code.jquery.com/jquery-1.9.1.js"></script>

<script type="text/javascript">

function loadXMLTransformed(folder, file, target) {
    var xmlhttp;

    if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    document.getElementById(target).innerHTML = "loading ...";
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
           //alert(xmlhttp.responseText);
           if(xmlhttp.status == 200){
              document.getElementById(target).innerHTML = xmlhttp.responseText;
           }
           else {
              //alert("mein error: ".concat(xmlhttp.status));
              document.getElementById(target).innerHTML = xmlhttp.responseText;
           }
        }
    };
    xmlhttp.open("GET", "/<% out.print(contextPath); %>/transform?folder=".concat(folder).concat("&file=").concat(file), true);
    xmlhttp.send();
}

//<![CDATA[
$(window).load(function(){
  $('#mainSplitter').jqxSplitter({
      width: 300,
      height: 300,
      theme: 'energyblue',
      panels: [{
          size: 200
      }]
  });
  $('#mainSplitter').on('resize', function (event) {
      displayEvent(event);
  });

  $('#mainSplitter').on('expanded', function (event) {
      displayEvent(event);
  });

  $('#mainSplitter').on('collapsed', function (event) {
      displayEvent(event);
  });

  function capitaliseFirstLetter(string) {
      return string.charAt(0).toUpperCase() + string.slice(1);
  }

  function displayEvent(event) {
      var eventData = "Event:" + capitaliseFirstLetter(event.type);
      eventData += ", Panel 1: " + event.args.panels[0].size;
      eventData += ", Panel 2: " + event.args.panels[1].size;
      $('#events').jqxPanel('prepend', '<div class="item" style="margin-top: 5px;">' + eventData + '</div>');
  }
  $('#events').jqxPanel({
      theme: 'energyblue',
      height: '250px',
      width: '450px'
  });
});//]]> 

</script>

    <style>


body {
    color: black;
    font-family: Segoe UI, Tahoma,Verdana, Helvetica, Arial, sans-serif; 
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
	margin-bottom: 10px;
    margin-top: 2px;
}

hr, .res {
    
    float: clear;
}
ul.ZipFilesList {
    background-color: yellow;
}
div.ImageFiles {
    background-color: red;
    
    height:400px;
    overflow-y: scroll; 
    resize:both;
    overflow:auto; /* something other than visible */
    float: left;
}
div.xml {
    background-color: blue;
    height:400px;
    overflow-y: scroll;     
}

div.RecognitionResultAndLink {
    background-color: green;
}
div.KernelResponseAndLink {
    background-color: green;
    float: clear;
}
<!--

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

 <div class="title"><% out.print(contextPath); %>  -  OCR Results</div>

<hr>
<div id='jqxWidget'>
    <div id="container" style="float: left">
        <div id="mainSplitter">
            <div class="splitter-panel" style="background-color:#F5FFF2;"><span style="margin: 5px;">Panel 1</span>

            </div>
            <div class="splitter-panel" style="background-color:#F5FFF2;"><span style="margin: 5px;">Panel 2</span>

            </div>
        </div>
        <br />
        <div style="font-family: Verdana; font-size: 13;">Events:</div>
        <div id="events" style="border-width: 0px;"></div>
    </div>
</div>

<hr>
<p>c</p>
<p>c</p>
<p>c</p>


<div class="Res">
<% 
/*
Object zipFilenamesObj = request.getAttribute("createdZips");
if(zipFilenamesObj == null){
	out.println("???" );
}
else{
	String zipFilenames = zipFilenamesObj.toString();
	//out.println( zipFiles );
	StringTokenizer st = new StringTokenizer( zipFilenames, "|" );		
	out.println( "<ul class=\"ZipFilesList\">" );
	while ( st.hasMoreTokens() ){
		String zipFilename = st.nextToken();	
		//out.println( "<a href=\"/download?folder=zip&file=" + filename + "\"> a:" + filename + "</a><br/>" );//" + contextPath + "
		out.println( "<li class=\"ZipFileListItem\">" );
		
		out.println( "<div class=\"ZipFile\">Input: " );	
		out.println( "<a href=\"/" + contextPath + "/download?folder=zip&file=" + zipFilename + "\">" + zipFilename + "</a>" );//
		out.println( "</div>" );

		out.println( "<div class=\"ImageFiles\">" );	
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
				out.println( "<li class=\"ImageFileListItem\">" );
				
				if(imgFilenameLowerCase.endsWith(".pdf")){
					out.println( "<object data=\"/" + contextPath + "/download?folder=in&file=" + imgFilename + "\" type=\"application/pdf\">" );
					out.print(  "alt : <a href=\"/" + contextPath + "/download?folder=in&file=" + imgFilename + "\" class=\"ImageFileLink\">" );//
					out.println( "</a>" );//					
					out.println( "</object>" );
				}
				else{
					out.println( "<a href=\"/" + contextPath + "/download?folder=in&file=" + imgFilename + "\" class=\"ImageFileLink\">" );//
					out.println( imgFilename );//
					out.println( "<img class=\"ImageFileImg\" src=\"/" + contextPath + "/download?folder=in&file=" + imgFilename + "\" alt=\"" + imgFilename + "\" />" );// style=\"width:304px;height:228px;\"
					out.println( "</a>" );//					
				}
				
				out.println( "</li>" );
			}	
		}		
		out.println( "</div>" );		
		
		out.println( "<div class=\"xml\">" );
		
		String requestXmlFilename = "request_170900_2005334297_7298683_0012606090.xml";//response_" + zipFilename.replace(".zip", ".xml")
		//out.println( "<a href=\"/" + contextPath + "/download?folder=res&file=response_" + zipFilename.replace(".zip", ".xml") + "\">RK-Response: response_" + zipFilename.replace(".zip", ".xml") + "</a>" );//
		out.println( "<div class=\"RecognitionResultAndLink\">" + 
				     "<div id=\"" + requestXmlFilename + "\" class=\"RecognitionResult\"></div>" +  
					 "<a href=\"javascript:loadXMLTransformed('xml','" + requestXmlFilename +
				     "','" + requestXmlFilename + "')\""
		             + " class=\"xmlLoadButton\">Recognition Result: " + requestXmlFilename + "</a></div>" );		
		
		String responseXmlFilename = "response_170900_2005334297_7298683_0012606090.xml";
		out.println( "<div class=\"KernelResponseAndLink\">" + 
					 "<div id=\"" + responseXmlFilename + "\" class=\"KernelResponse\"></div>" + 
		             "<a href=\"javascript:loadXMLTransformed('xml','" + responseXmlFilename +
				     "','" + responseXmlFilename + "')\""
		             + " class=\"xmlLoadButton\">Rechenkern Response: " + responseXmlFilename + "</a></div>" );
		
		out.println( "</div>" );	
		
		out.println( "</li>" );
	}
	out.println( "</ul>" );
}
*/
%>

</div>

<!--
<div id="myDiv">
</div>
<a href="javascript:loadXMLTransformed('xml','response_170900_2005334297_7298683_0012606090.xml','myDiv')" class="linkButton">load</a>
-->

</body>
</html>