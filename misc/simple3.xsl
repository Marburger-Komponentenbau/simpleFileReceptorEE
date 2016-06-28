<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:SCAN="http://WSDLEntities.scan.zce.commerzbank.com/rk/Service.xsd"
	exclude-result-prefixes="SCAN"
	>

<xsl:output
	method="text"
	encoding="UTF-8"
	omit-xml-declaration="yes"
	standalone="yes"
	indent="yes"
	media-type="text/plain" />
  

<xsl:template match="/">
	<xsl:text>&lt;nav&gt;</xsl:text>
     <xsl:apply-templates select="SCAN:GetScoreResponse/SCAN:kernelStructureOutput/SCAN:entities" />
     <xsl:apply-templates select="SCAN:GetScoreRequest/SCAN:kernelStructureInput/SCAN:entities" />
    <xsl:text>&lt;/nav&gt;</xsl:text>
</xsl:template>
<!-- 
<xsl:for-each select="/SCAN:entities"></xsl:for-each>
 -->

<xsl:template match="SCAN:entities">
	<xsl:text>&lt;ul class="entities"&gt;</xsl:text>
		 <xsl:text>&lt;li class="entity"&gt;</xsl:text>
		    <xsl:text>&lt;a href="javascript:void(0)" onclick="display(this);" &gt;</xsl:text>
			   <xsl:value-of select="SCAN:name"/>
			<xsl:text>&lt;/a&gt;</xsl:text>
			<xsl:text>&lt;table class="values"&gt;</xsl:text>
				<xsl:for-each select="SCAN:values">
					<xsl:text>&lt;tr&gt;</xsl:text>
						<xsl:text>&lt;td&gt;</xsl:text>
							<xsl:value-of select="SCAN:name"/>
						<xsl:text>&lt;/td&gt;</xsl:text>
						<xsl:text>&lt;td&gt;</xsl:text>
							<xsl:value-of select="SCAN:value"/>
						<xsl:text>&lt;/td&gt;</xsl:text>
					<xsl:text>&lt;/tr&gt;</xsl:text>
				</xsl:for-each>
			<xsl:text>&lt;/table&gt;</xsl:text>
			<xsl:apply-templates select="SCAN:entities" />
		<xsl:text>&lt;/li&gt;</xsl:text>
	<xsl:text>&lt;/ul&gt;</xsl:text>
	
</xsl:template>

</xsl:stylesheet>