<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    import="com.commerzbank.simpleFileReceptorEE.StaticUtils, java.io.File, java.util.Arrays" 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%
String contextPath = request.getContextPath();
if(contextPath == null || contextPath.length() < 1) {
	contextPath = "Receptor";
}
else{
	contextPath = contextPath.replace("/","");
}
%>
<title><% out.print(contextPath); %>  -  Test</title>
</head>
<body>

<a href="./report?folder=xml&file=SVRorg.xml" target="_blank">SVRorg.xml</a>
<br />
<a href="./report?folder=xml&file=SVR.xml" target="_blank">SVR.xml</a>
<br />
<hr />
<a href="./transform?folder=xml&file=SVRorg.xml" target="_blank">SVRorg.xml</a>
<br />
<a href="./transform?folder=xml&file=SVR.xml" target="_blank">SVR.xml</a>
<br />
<hr />
<a href="./transform?folder=xml&file=RKENTW_SCAN185400_2016-06-14_16-13-57_354_response.xml" target="_blank">RKENTW_SCAN185400_2016-06-14_16-13-57_354_response.xml</a>
<br />
<a href="./transform?folder=xml&file=response_170900_2005334297_7298683_0012606090.xml" target="_blank">response_170900_2005334297_7298683_0012606090.xml</a>
<br />
<a href="./download?folder=xml&file=response_1001363266_7313341_0012652431.xml" target="_blank">download x.xml</a>
</body>
</html>