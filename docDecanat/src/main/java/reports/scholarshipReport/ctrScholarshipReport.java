package reports.scholarshipReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.io.FileWriter;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;

import org.eclipse.jdt.internal.compiler.batch.Main;
import org.zkoss.json.JSONObject;
import org.zkoss.util.Dates;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import dec.docDecanat.data.dao.GroupDao;
import dec.docDecanat.data.dao.HumanfaceDao;
import dec.docDecanat.data.dao.OrderDao;
import dec.docDecanat.data.dao.OrderSubTypeDao;
import dec.docDecanat.data.dao.OrderTypeDao;
import dec.docDecanat.data.dao.StudentCardDao;
import dec.docDecanat.data.dao.StudentSemesterStatusDao;
import dec.docDecanat.data.entity.Group;
import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.Humanface;
import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.data.entity.OrderStatusType;
import dec.docDecanat.data.entity.OrderSubType;
import dec.docDecanat.data.entity.OrderType;
import dec.docDecanat.data.entity.Rule;
import dec.docDecanat.data.entity.Section;
import dec.docDecanat.data.entity.StudentCard;
import dec.docDecanat.data.entity.StudentSemesterStatus;
import dec.docDecanat.pageControllers.CtrOrder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

import reports.scholarshipReport.SectionScholarshipOrder;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class ctrScholarshipReport extends Window {
	private static final long serialVersionUID = 1L;

	public void uploadFile() throws IOException {
		Clients.showNotification("Отправление приказа на согласование");
		try{
			if (CtrOrder.ord.getOrderSubType().getName()
					.equals(Order.orderScholarship)) {
				Runtime.getRuntime().exec("cmd /c start C:\\Utils\\scOrd.lnk");
			}
			if (CtrOrder.ord.getOrderSubType().getName()
					.equals(Order.orderTransfer)) {
				Runtime.getRuntime().exec("cmd /c start C:\\Utils\\trOrd.lnk");
			}
			Clients.showNotification("Приказ отправлен на согласование");
		}catch(Exception e){
			Clients.showNotification("Приказ НЕ отправлен");
		}

	}

	public void toPdf() throws IOException {
		if(!((OrderRule)CtrOrder.ord.getOrderRules().toArray()[0]).getSection().getRule().isCommonPersonal()){
			Iframe iframe = (Iframe) this.getFellow("iframe");
			SectionScholarshipList proxyList = new SectionScholarshipList();
			List<SectionScholarshipOrder> dataList = proxyList.getAllData();
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					dataList);
			try {
				JasperPrint jasperPrint = JasperFillManager
						.fillReport(
								"C:\\Alfresco\\tomcat\\webapps\\docDecanat\\WEB-INF\\classes\\reports\\scolarshipReport\\scolarshipReport\\sectionOrderReport.jasper",
								new HashMap(), beanColDataSource);

				JasperExportManager
				.exportReportToPdfFile(
						jasperPrint,
						"C:\\Alfresco\\tomcat\\webapps\\docDecanat\\WEB-INF\\classes\\reports\\scolarshipReport\\report.pdf");

				File f = new File(
						"C:\\Alfresco\\tomcat\\webapps\\docDecanat\\WEB-INF\\classes\\reports\\scolarshipReport\\report.pdf");
				byte[] buffer = new byte[(int) f.length()];
				FileInputStream fs;
				fs = new FileInputStream(f);
				fs.read(buffer);
				fs.close();
				ByteArrayInputStream is = new ByteArrayInputStream(buffer);
				AMedia amedia = new AMedia("1.pdf", "pdf", "application/pdf", is);
				iframe.setContent(amedia);
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (proxyList.resultStatus) {
			case 1:
				Clients.showNotification("Не заданы даты выплат","warning",this,"top_center",5000,true);
				break;

			default:
				break;
			}
		}else{
			Clients.showNotification("Данное отображение доступно только для индивидуальных приказов");
		}
	}

	public void toDocx() throws IOException, InterruptedException {
		if(!((OrderRule)CtrOrder.ord.getOrderRules().toArray()[0]).getSection().getRule().isCommonPersonal()){
			SectionScholarshipList proxyList = new SectionScholarshipList();
			List<SectionScholarshipOrder> dataList = proxyList.getAllData();
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					dataList);
			Iframe iframe = (Iframe) this.getFellow("iframe");
			try {
				JasperPrint jasperPrint = JasperFillManager
						.fillReport(
								"C:\\Alfresco\\tomcat\\webapps\\docDecanat\\WEB-INF\\classes\\reports\\scolarshipReport\\sectionOrderReport.jasper",
								new HashMap(), beanColDataSource);

				JRDocxExporter docxExporter = new JRDocxExporter();
				String os = "C:\\Alfresco\\tomcat\\webapps\\docDecanat\\WEB-INF\\classes\\reports\\scolarshipReport\\reportDoc.docx";
				docxExporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				docxExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, os);
				docxExporter.exportReport();

				File f = new File(
						"C:\\Alfresco\\tomcat\\webapps\\docDecanat\\WEB-INF\\classes\\reports\\scolarshipReport\\reportDoc.docx");
				byte[] buffer = new byte[(int) f.length()];
				FileInputStream fs;
				fs = new FileInputStream(f);
				fs.read(buffer);
				fs.close();
				ByteArrayInputStream is = new ByteArrayInputStream(buffer);
				AMedia amedia = new AMedia("reportDoc.docx", "docx",
						"application/docx", is);
				iframe.setContent(amedia);
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (proxyList.resultStatus) {
			case 1:
				Clients.showNotification("Не заданы даты выплат","warning",this,"top_center",5000,true);
				break;

			default:
				break;
			}
		}else{
			Clients.showNotification("Данное отображение доступно только для индивидуальных приказов");
		}
	}

	public void toPdf2() throws IOException {
		if(((OrderRule)CtrOrder.ord.getOrderRules().toArray()[0]).getSection().getRule().isCommonPersonal()){
			GroupScholarshipList proxyList2 = new GroupScholarshipList();
			List<GroupScholarshipOrder> dataList2 = proxyList2.getAllProxy();
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					dataList2);
			Iframe iframe = (Iframe) this.getFellow("iframe");
			try {
				JasperPrint jasperPrint = JasperFillManager
						.fillReport(
								"C:\\Alfresco\\tomcat\\webapps\\docDecanat\\WEB-INF\\classes\\reports\\scolarshipReport\\groupGroupReport.jasper",
								new HashMap(), beanColDataSource);

				JasperExportManager
				.exportReportToPdfFile(
						jasperPrint,
						"C:\\Alfresco\\tomcat\\webapps\\docDecanat\\WEB-INF\\classes\\reports\\scolarshipReport\\report.pdf");

				File f = new File(
						"C:\\Alfresco\\tomcat\\webapps\\docDecanat\\WEB-INF\\classes\\reports\\scolarshipReport\\report.pdf");
				byte[] buffer = new byte[(int) f.length()];
				FileInputStream fs;
				fs = new FileInputStream(f);
				fs.read(buffer);
				fs.close();
				ByteArrayInputStream is = new ByteArrayInputStream(buffer);
				AMedia amedia = new AMedia("reports.pdf", "pdf", "application/pdf",
						is);
				iframe.setContent(amedia);
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			switch (proxyList2.resultStatus) {
			case 1:
				Clients.showNotification("Не заданы даты выплат","warning",this,"top_center",5000,true);
				break;

			default:
				break;
			}
		}else{
			Clients.showNotification("Данное отображение доступно только для общих приказов");
		}
	}

	public void toDocx2() throws IOException, InterruptedException {
		if(((OrderRule)CtrOrder.ord.getOrderRules().toArray()[0]).getSection().getRule().isCommonPersonal()){
			GroupScholarshipList proxyList2 = new GroupScholarshipList();
			List<GroupScholarshipOrder> dataList2 = proxyList2.getAllProxy();
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(
					dataList2);
			Iframe iframe = (Iframe) this.getFellow("iframe");
			try {
				JasperPrint jasperPrint = JasperFillManager
						.fillReport(
								"C:\\Alfresco\\tomcat\\webapps\\docDecanat\\WEB-INF\\classes\\reports\\scolarshipReport\\groupGroupReport.jasper",
								new HashMap(), beanColDataSource);

				JRDocxExporter docxExporter = new JRDocxExporter();
				String os = "C:\\Alfresco\\tomcat\\webapps\\docDecanat\\WEB-INF\\classes\\reports\\scolarshipReport\\reportDoc.docx";
				docxExporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				docxExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, os);
				docxExporter.exportReport();

				File f = new File(
						"C:\\Alfresco\\tomcat\\webapps\\docDecanat\\WEB-INF\\classes\\reports\\scolarshipReport\\reportDoc.docx");
				byte[] buffer = new byte[(int) f.length()];
				FileInputStream fs;
				fs = new FileInputStream(f);
				fs.read(buffer);
				fs.close();
				ByteArrayInputStream is = new ByteArrayInputStream(buffer);
				AMedia amedia = new AMedia("reportDoc.docx", "docx",
						"application/docx", is);
				iframe.setContent(amedia);
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch (proxyList2.resultStatus) {
			case 1:
				Clients.showNotification("Не заданы даты выплат","warning",this,"top_center",5000,true);
				break;

			default:
				break;
			}
		}else{
			Clients.showNotification("Данное отображение доступно только для общих приказов");
		}
	}
}