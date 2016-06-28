<?xml version="1.0" encoding="UTF-8"?>
<html xsl:version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<body style="font-family:Arial;font-size:12pt;background-color:#EEEEEE">
<div style="margin-left:20px;margin-bottom:1em;font-size:10pt">
</div>
<span style="font-style:italic">
</span>
<span style="font-weight:bold">
</span>
<p style="background-color:teal;color:white;padding:4px">
</p>
<xsl:for-each select="GetScoreResponse/kernelStructureOutput/entities">
	<div style="margin-left:20px;margin-bottom:1em;font-size:10pt">
		<xsl:value-of select="name"/>
		  - 
		<xsl:for-each select="values">
			<div style="margin-left:20px;margin-bottom:1em;font-size:10pt">
				<xsl:value-of select="name"/>
				:
				<xsl:value-of select="value"/>		
			</div>
		</xsl:for-each>
	</div>
</xsl:for-each>
</body>
</html>