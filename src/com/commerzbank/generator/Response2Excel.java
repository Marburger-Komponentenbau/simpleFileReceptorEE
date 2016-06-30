package com.commerzbank.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Response2Excel 
{
	//protected String excelFilename;	
	protected Document response;
	protected Sheet sheet;
	protected CellStyle bold;
	protected Map<Integer,Row> rows;		
	
	public Response2Excel( Document response )	
	{
		//this.excelFilename = excelFilename;		
		this.response = response;
		
		rows = new HashMap<Integer,Row>();	
	}
	
	public String getNodeValue( String ns, String name, Element element )
	{
		NodeList list = getNodeValueList( ns, name, element );
			
		if( list.getLength() > 0 )
		{
			return getNodeValueContext( ns, list.item( 0 ).getParentNode()  );				
		}
		
		return "Not found!";
	}
	
	protected NodeList getNodeValueList( String ns, String name, Element element )
	{
		try
		{
			XPath xPath = XPathFactory.newInstance().newXPath();
		
			return (NodeList)xPath.evaluate( ".//*[local-name()='name'][text()='"+ name +"']", element, XPathConstants.NODESET );			
		}
		catch( Throwable t )
		{
			t.printStackTrace();
		}
		
		return null;
	}
	
	protected String getNodeValueContext( String ns, Node parent )
	{
		for( int i = 0; i < parent.getChildNodes().getLength(); i++ )
		{
			Node child = parent.getChildNodes().item( i );
			
			if( child.getNodeName().compareTo( ns + "value" ) == 0 )
			{
				return child.getTextContent();
			}
		}
		
		return "not found!";
	}
	
	protected Element getFirstChildElement( Node node )
	{
		for( int i = 0; i < node.getChildNodes().getLength(); i++ )
		{
			if( node.getChildNodes().item( i ).getNodeType() == Node.ELEMENT_NODE )
			{
				return (Element)node.getChildNodes().item( i );
			}
		}
		
		return null;
	}
	
	public String getSheetName() throws Throwable
	{	
		String ns = loopNodes( response.getDocumentElement(), "Envelope" ) + ":";
		
		String GBKDNR = getNodeValue( ns, "GBKDNR", response.getDocumentElement() );
		String SAP = getNodeValue( ns, "SAP", response.getDocumentElement() );
		String FANR = getNodeValue( ns, "FANR", response.getDocumentElement() );
		
		String sheetName = "unset";
				
		if( !GBKDNR.isEmpty() || !SAP.isEmpty() || !FANR.isEmpty() )
		{	
			sheetName = GBKDNR +"-"+ SAP +"-"+ FANR;
		}
		return sheetName;
	}
	
	public void generate(OutputStream output) throws Throwable
	{	
		String ns = loopNodes( response.getDocumentElement(), "Envelope" ) + ":";
		
		String GBKDNR = getNodeValue( ns, "GBKDNR", response.getDocumentElement() );
		String SAP = getNodeValue( ns, "SAP", response.getDocumentElement() );
		String FANR = getNodeValue( ns, "FANR", response.getDocumentElement() );
		
		String sheetName = "unset";
		
		if( !GBKDNR.isEmpty() || !SAP.isEmpty() || !FANR.isEmpty() )
		{	
			sheetName = GBKDNR +"-"+ SAP +"-"+ FANR;
		}	
		
		//File excel = new File( excelFilename );
		
		//if( !excel.exists() )
		//{
		//	excel.createNewFile();
		//}
		
		Workbook workbook = new HSSFWorkbook();
		initWorkbook( workbook );
		//FileOutputStream output = new FileOutputStream( excel );
				
		sheet = workbook.createSheet( sheetName );
				
		setValue( "A1", "GBKDNR", bold );
		setValue( "B1", GBKDNR, bold );
		
		setValue( "A2", "SAP", bold );
		setValue( "B2", SAP, bold );
		
		setValue( "A3", "FANR", bold );
		setValue( "B3", FANR, bold );		
		
		setValue( "A5", "Barcode/Monat", bold );
		setValue( "B5", "Personalnummer", bold );
		setValue( "C5", "SVN", bold );
		setValue( "D5", "SVN Logik", bold );
		setValue( "E5", "SVN m/w", bold );
		setValue( "F5", "Brutto", bold );
		setValue( "G5", "Netto", bold );
		setValue( "H5", "Auszahlung", bold );
		setValue( "I5", "IBAN", bold );
		setValue( "J5", "IBAN Logik", bold );
		setValue( "K5", "Unternehmen", bold );
		setValue( "L5", "Gesamt Logik", bold );
		
		setValue( "A7", "Anmerkungen:" );
		setValue( "A8", "Hamburg" );
		setValue( "A9", "GRM-CC" );
		setValue( "A10", "EMC" );
		
		int rowIndex = 12;
		
		NodeList documents  = getNodeValueList( ns, "dokument", response.getDocumentElement() );
		
		for( int d = 0; d < documents.getLength(); d++ )
		{
			Element documentEntity = (Element)documents.item( d ).getParentNode();
						
			String barcode = getNodeValue( ns, "barcode", documentEntity );
			
			setValue( "A"+ rowIndex++, barcode, bold );
			
			NodeList monatList = getNodeValueList( ns, "monat", documentEntity );
			NodeList pnList = getNodeValueList( ns, "personalnummer", documentEntity );
			NodeList svnList = getNodeValueList( ns, "svn", documentEntity );
			NodeList svn_logikList = getNodeValueList( ns, "svn_logik", documentEntity );
			NodeList svn_mwList = getNodeValueList( ns, "svn_mw", documentEntity );
			NodeList bruttoList = getNodeValueList( ns, "brutto", documentEntity );
			NodeList nettoList = getNodeValueList( ns, "netto", documentEntity );
			NodeList auszahlungList = getNodeValueList( ns, "auszahlung", documentEntity );
			NodeList ibanList = getNodeValueList( ns, "iban", documentEntity );
			NodeList iban_logikList = getNodeValueList( ns, "iban_logik", documentEntity );
			NodeList unternehmenList = getNodeValueList( ns, "unternehmen", documentEntity );			
			
			for( int m = 0; m < monatList.getLength(); m++ )
			{
				String monat = getNodeValueContext( ns, monatList.item( m ).getParentNode() );
				String pn = getNodeValueContext( ns, pnList.item( m ).getParentNode() );
				String svn = getNodeValueContext( ns, svnList.item( m ).getParentNode() );
				String svn_logik = getNodeValueContext( ns, svn_logikList.item( m ).getParentNode() );
				String svn_mw = getNodeValueContext( ns, svn_mwList.item( m ).getParentNode() );
				String brutto = getNodeValueContext( ns, bruttoList.item( m ).getParentNode() );
				String netto = getNodeValueContext( ns, nettoList.item( m ).getParentNode() );
				String auszahlung = getNodeValueContext( ns, auszahlungList.item( m ).getParentNode() );
				String iban = getNodeValueContext( ns, ibanList.item( m ).getParentNode() );
				String iban_logik = getNodeValueContext( ns, iban_logikList.item( m ).getParentNode() );
				String unternehmen = getNodeValueContext( ns, unternehmenList.item( m ).getParentNode() );
				
				setValue( "A"+ rowIndex, monat );
				setValue( "B"+ rowIndex, pn );
				setValue( "C"+ rowIndex, svn );
				setValue( "D"+ rowIndex, svn_logik );
				setValue( "E"+ rowIndex, svn_mw );
				setValue( "F"+ rowIndex, brutto );
				setValue( "G"+ rowIndex, netto );
				setValue( "H"+ rowIndex, auszahlung );
				setValue( "I"+ rowIndex, iban );
				setValue( "J"+ rowIndex, iban_logik );
				setValue( "K"+ rowIndex, unternehmen );
				
				rowIndex++;
			}
			
			rowIndex += 2;
		}		
		
		for( int s = 0; s <= 12; s++ )
		{
			sheet.autoSizeColumn( s );
		}
		
		workbook.write( output );
		//output.flush();	
		
	}
	
	protected Node getNodeBySeitenID( NodeList list, String seitenID, String ns )
	{
		for( int i = 0; i< list.getLength(); i++ )
		{
			Node child = list.item( i );
			
			String textValue = getNodeValue( child, "seiten_id", ns );
			
			if( ( textValue != null ) && ( textValue.compareTo( seitenID ) == 0 ) )
			{
				return child;
			}
		}
		
		return null;
	}
		
	protected void initWorkbook( Workbook workbook )
	{
		Font fontBold = workbook.createFont();
		fontBold.setBoldweight( Font.BOLDWEIGHT_BOLD );
		bold = workbook.createCellStyle();
		bold.setFont( fontBold );	
	}
	
	protected Cell setValueFormula( String columnRow, String value )
	{
		return setValue( columnRow, value, "formula", null );
	}
	
	protected Cell setValueDouble( String columnRow, String value )
	{
		return setValue( columnRow, value, "double", null );
	}
	
	protected Cell setValue( String columnRow, String value )
	{
		return setValue( columnRow, value, "string", null );
	}
	
	protected Cell setValue( String columnRow, String value, CellStyle style )
	{
		return setValue( columnRow, value, "string", style );
	}
	
	protected Cell setValue( String columnRow, String value, String cellType, CellStyle style )
	{
		String columnIdx = columnRow.substring( 0, 1 );
		String rowIdx = columnRow.substring( 1 );
		
		int columnIndexInt = ( (int) columnIdx.charAt( 0 ) ) - 65;
		int rowIndexInt = ( new Integer( rowIdx ) ) - 1;
		
		Row row = null;
		
		if( rows.containsKey( rowIndexInt ) )
		{
			row = rows.get( rowIndexInt );
		}
		else
		{
			row = sheet.createRow( rowIndexInt );
			rows.put( rowIndexInt, row );
		}		
		
		Cell cell = row.getCell( columnIndexInt );
		
		if( cell == null )
		{
			cell = row.createCell( columnIndexInt );
		}
		
		if( cellType.compareTo( "string" ) == 0 )
		{
			cell.setCellValue( value );
		}
		if( cellType.compareTo( "double" ) == 0 )
		{
			if( value.isEmpty() )
			{
				value = "0.0";
			}
			
			cell.setCellValue( new Double( value.replace( ",", "." ) ) );			
		}
		else if( cellType.compareTo( "formula" ) == 0 )
		{
			cell.setCellType( Cell.CELL_TYPE_FORMULA );
			cell.setCellFormula( value );
		}
		
		if( style != null )
		{
			cell.setCellStyle( style );
		}
		
		return cell;
	}
	
	protected String getNodeValue( Node parent, String childName, String namespace )
	{
		String result = "not found!";
		
		for( int i = 0; i < parent.getChildNodes().getLength(); i++ )
		{
			Node child = parent.getChildNodes().item( i );
			
			if( child.getNodeType() == Node.ELEMENT_NODE )
			{
				if( child.getNodeName().compareToIgnoreCase( namespace + childName ) == 0 )
				{
					return child.getTextContent();
				}
			}
		}
		
		return result;
	}
	
	protected String loopNodes( Node parent, String searchNodeName )
	{
		String ns = "";
		
		if( parent.getNodeName().indexOf( searchNodeName ) != -1 )
		{
			NamedNodeMap map = parent.getAttributes();
			
			for( int a = 0; a < map.getLength(); a++ )
			{
				String nodeName = map.item( a ).getNodeName();
				String nodeValue = map.item( a ).getTextContent();
				
				if( ( nodeName.indexOf( "xmlns:" ) != -1 ) && ( nodeValue.indexOf( "WSDLEntities" ) != -1 ) )
				{							
					return nodeName.replace( "xmlns:", "" );
				}
			}
		}
		
		for( int i = 0; i < parent.getChildNodes().getLength(); i++ )
		{
			Node child = parent.getChildNodes().item( i );
						
			if( child.getNodeType() == Node.ELEMENT_NODE )
			{				
				if( child.hasChildNodes() && ns.isEmpty() )
				{
					ns = loopNodes( child, searchNodeName );
				}
			}
		}
				
		return ns;
	}
		
	public static void main(String[] args) 
	{		
		try
		{
			DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
			fac.setNamespaceAware( true );

			//File dir = new File( "c:\\users\\eh2oeld\\Desktop\\SCAN\\temp\\" );
			File dir = new File( "C:\\dev\\Programme\\eclipse-jee-mars-2-win32-x86_64\\eclipse\\data" );
			
			for( File file : dir.listFiles() )
			{
				if( file.getName().indexOf( "RKENTW_SCAN185400_2016-06-14_16-13-57_354_response.xml" ) != -1 )
				{
					System.out.println( "Working on file:" + file.getName() );
					
					Document response = fac.newDocumentBuilder().parse( file );
				
					//String excelFilename = "c:\\users\\eh2oeld\\Desktop\\SCAN_REPORT\\SCAN_template.xls";
					String excelFilename = "c:\\users\\cb3fias\\Desktop\\SCAN_template.xls";
				
					Response2Excel rr2Excel = new Response2Excel( response );
				
					rr2Excel.generate(null);					
				}
			}
		}
		catch( Throwable t )
		{
			System.out.println( t.getMessage() );
		}
		
		System.out.println( "Have a nice day!!!" );
	}


}
