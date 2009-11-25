package com.itao.starlite.ui.util;

import java.util.List;
import java.util.Map;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager    ;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.JRDataSource;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.views.jasperreports.ValueStackDataSource;
import org.apache.struts2.views.jasperreports.ValueStackShadowMap;


public class JasperReportUtil {

	public static final String JASPER_DIR = "/home/admin/svnwork/starlite/starlite-ui/payslips/";

	public static void toFile(HttpServletRequest request, String template, String dataSource, String filename){
		try {
			JasperPrint jasperPrint = fillReport(request,template,dataSource);
			JasperExportManager.exportReportToPdfFile( jasperPrint , JASPER_DIR + filename  );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void toPrint(HttpServletRequest request, String template, String dataSource, String printer){

		JasperPrint jasperPrint;
		try {
			jasperPrint = fillReport(request,template,dataSource);


			PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
			PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
			printServiceAttributeSet.add( new PrinterName( printer, null ) );
			JRPrintServiceExporter exporter = new JRPrintServiceExporter();
			exporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrint );
			exporter.setParameter( JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet );
			exporter.setParameter( JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet );
			exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE  );
			exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE );
			exporter.exportReport();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void toFile(String template, List dataSource, Map params, String filename){
		try {
			JasperPrint jasperPrint = fillReport(dataSource, params ,template);
			JasperExportManager.exportReportToPdfFile( jasperPrint , JASPER_DIR + filename  );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static void toPrint( String template, List dataSource, Map params, String printer){

		JasperPrint jasperPrint;
		try {
			jasperPrint = fillReport(dataSource, params ,template);

			PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
			PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
			printServiceAttributeSet.add( new PrinterName( printer, null ) );
			JRPrintServiceExporter exporter = new JRPrintServiceExporter();
			exporter.setParameter( JRExporterParameter.JASPER_PRINT, jasperPrint );
			exporter.setParameter( JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet );
			exporter.setParameter( JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet );
			exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE  );
			exporter.setParameter( JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE );
			exporter.exportReport();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@SuppressWarnings("unchecked")
	public static JasperPrint fillReport(HttpServletRequest request, String template, String dataSource) throws Exception{
		JRDataSource ds = new ValueStackDataSource(ServletActionContext.getActionContext(request).getValueStack(),dataSource);
		Map          p  = new ValueStackShadowMap(ServletActionContext.getActionContext(request).getValueStack());
		JasperPrint jasper_print = JasperFillManager.fillReport( template, p, ds );
		return jasper_print;
	}
	
	@SuppressWarnings("unchecked")
	public static JasperPrint fillReport(List dataSource, Map params, String template) throws Exception{
		//System.out.println(dataSource);
		JRDataSource ds = new JRBeanCollectionDataSource(dataSource,true);
		JasperPrint jasper_print = JasperFillManager.fillReport( template, params, ds );
		return jasper_print;
	}


}
