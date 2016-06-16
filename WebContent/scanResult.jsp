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


</body>
</html>