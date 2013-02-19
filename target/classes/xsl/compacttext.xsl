<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:strip-space elements="*"/>
	
	<xsl:output method="text" omit-xml-declaration="yes" indent="no"
		version="1.0" />

	<xsl:template match="Lems">
		<Lems>
			<xsl:apply-templates/>
		</Lems>
	</xsl:template>

  

	<xsl:template match="*">
		<xsl:param name="ind" select="''" />
		<xsl:text>
</xsl:text>
		<xsl:if test="$ind = ''"><xsl:text>
</xsl:text>
		</xsl:if>
		<xsl:value-of select="$ind" />
		<xsl:value-of select="local-name()" />
		<xsl:if test="@name">
			<xsl:text> </xsl:text>
			<xsl:value-of select="@name" />
		</xsl:if>
		<xsl:for-each select="@*">
			<xsl:choose>
				<xsl:when test="name() = 'name'"></xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="concat(', ', name(), '=', .)" />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
		 
		<xsl:apply-templates>
			<xsl:with-param name="ind" select="concat($ind, '    ')" />
		</xsl:apply-templates>
	</xsl:template>
</xsl:stylesheet>