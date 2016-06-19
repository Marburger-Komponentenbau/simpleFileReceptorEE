<?xml version="1.0" encoding="UTF-8"?>
<x xsl:version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:SCAN="http://WSDLEntities.scan.zce.commerzbank.com/rk/Service.xsd">

<xsl:for-each select="SCAN:GetScoreResponse/SCAN:kernelStructureOutput/SCAN:entities">
	<div style="margin-left:20px;margin-bottom:1em;font-size:10pt">
		<xsl:value-of select="SCAN:name"/>
		  - 
		<xsl:for-each select="SCAN:values">
			<div style="margin-left:20px;margin-bottom:1em;font-size:10pt">
				<xsl:value-of select="SCAN:name"/>
				:
				<xsl:value-of select="SCAN:value"/>		
			</div>
		</xsl:for-each>
	</div>
</xsl:for-each>

</x>