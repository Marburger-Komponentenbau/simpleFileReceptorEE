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
<title>Insert title here</title>

<script type="text/javascript">
function loadXMLDocX() {
	loadXMLDoc('eine datei.zip');
}
function loadXMLDoc(folder, file, target) {
    var xmlhttp;

    if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp = new XMLHttpRequest();
    } else {
        // code for IE6, IE5
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }

    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
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
</script>

</head>
<body>

<p>
<% 
Object s = request.getAttribute("createdZips");
if(s == null){
	out.println("???" );
}
else{
	String zipFiles = s.toString();
	//out.println( zipFiles );
	StringTokenizer st = new StringTokenizer( zipFiles, "|" );		
	out.println( "<ul style=\"list-style-type:square\">" );
	while ( st.hasMoreTokens() ){
		String filename = st.nextToken();	
		//out.println( "<a href=\"/download?folder=zip&file=" + filename + "\"> a:" + filename + "</a><br/>" );//" + contextPath + "
		out.println( "<li>" );
		out.println( "<a href=\"/" + contextPath + "/download?folder=zip&file=" + filename + "\">images: " + filename + "</a>" );//
		out.println( "<br/>" );
		out.println( "<a href=\"/" + contextPath + "/download?folder=res&file=request_" + filename.replace(".zip", ".xml") + "\">RK-Request: request_" + filename.replace(".zip", ".xml") + "</a>" );//
		out.println( "<br/>" );
		out.println( "<a href=\"/" + contextPath + "/download?folder=res&file=response_" + filename.replace(".zip", ".xml") + "\">RK-Response: response_" + filename.replace(".zip", ".xml") + "</a>" );//
		out.println( "</li>" );
	}
	out.println( "</ul>" );
}

%>
</p> 

<p style="text-align: center;">
<a href="javascript:loadXMLDoc('xml','response_170900_2005334297_7298683_0012606090.xml','myDiv')" class="linkButton">load</a>
</p>
<hr>
<div id="myDiv">
</div>

</body>
</html>