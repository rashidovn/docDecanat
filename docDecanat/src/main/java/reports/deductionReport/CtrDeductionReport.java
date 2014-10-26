package reports.deductionReport;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

import org.zkoss.util.media.AMedia;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

public class CtrDeductionReport extends Window{
	private static final long serialVersionUID = 1L;
	
	public void toPdf() throws IOException{
		
		Iframe iframe = (Iframe) this.getFellow("iframe");
		ListDeductionOrder listExOr = new ListDeductionOrder();
		List<DeductionOrder> dataList = listExOr.getAllData(); 
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
		try{
			JasperPrint jasperPrint = null;
			jasperPrint = JasperFillManager.fillReport("src/main/java/reports/deductionReport/deductionOrderReport.jasper", new HashMap(), beanColDataSource);
			JasperExportManager.exportReportToPdfFile(jasperPrint, "src/main/java/reports/deductionReport/deductionReport.pdf");
			
			File f = new File("src/main/java/reports/deductionReport/deductionReport.pdf");
			byte[] buffer = new byte[(int) f.length()];
			FileInputStream fs;
			fs = new FileInputStream(f);
			fs.read(buffer);
			fs.close();
			ByteArrayInputStream is = new ByteArrayInputStream(buffer);
			AMedia amedia = new AMedia("1.pdf", "pdf", "application/pdf",is);
			iframe.setContent(amedia);
			
		}catch(JRException e){
			e.printStackTrace();
		}
		
	}
	public void toDocx() throws IOException{

		Iframe iframe = (Iframe) this.getFellow("iframe");
		ListDeductionOrder listExOr = new ListDeductionOrder();
		List<DeductionOrder> dataList = listExOr.getAllData(); 
		JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
		try{
			JasperPrint jasperPrint = JasperFillManager.fillReport("src/main/java/reports/deductionReport/deductionOrderReport.jasper", new HashMap(), beanColDataSource);

			JRDocxExporter docxExporter = new JRDocxExporter();
			String os = "src/main/java/reports/deductionReport/deductionReportDocx.docx";
			docxExporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
			docxExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, os);
			docxExporter.exportReport();

			File f = new File("src/main/java/reports/deductionReport/deductionReportDocx.docx");
			byte[] buffer = new byte[(int) f.length()];
			FileInputStream fs;
			fs = new FileInputStream(f);
			fs.read(buffer);
			fs.close();
			ByteArrayInputStream is = new ByteArrayInputStream(buffer);
			AMedia amedia = new AMedia("deductionReportDocx.docx", "docx", "application/docx", is);
			iframe.setContent(amedia);
		}catch(JRException e){
			e.printStackTrace();
		}
	}
	
	public void uploadFile(){
		
	}
}
