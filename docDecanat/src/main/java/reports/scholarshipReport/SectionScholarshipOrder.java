package reports.scholarshipReport;

import java.util.ArrayList;
import java.util.List;

import dec.docDecanat.data.dao.GroupDao;
import dec.docDecanat.data.entity.Group;
import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.data.entity.Section;
import dec.docDecanat.pageControllers.CtrOrder;

public class SectionScholarshipOrder {
	private String description;
	private String titleDescription;
	private List<ScholarshipFooter> listProxyFooter = new ArrayList<ScholarshipFooter>();
	private List<SectionScholarshipCourse> listProxyCourse = new ArrayList<SectionScholarshipCourse>();
	
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ScholarshipFooter> getListProxyFooter() {
		return listProxyFooter;
	}

	public void setListProxyFooter(List<ScholarshipFooter> listProxyFooter) {
		this.listProxyFooter = listProxyFooter;
	}

	public String getTitleDescription() {
		return titleDescription;
	}

	public void setTitleDescription(String titleDescription) {
		this.titleDescription = titleDescription;
	}

	public List<SectionScholarshipCourse> getListProxyCourse() {
		return listProxyCourse;
	}

	public void setListProxyCourse(List<SectionScholarshipCourse> listProxyCourse) {
		this.listProxyCourse = listProxyCourse;
	}

}