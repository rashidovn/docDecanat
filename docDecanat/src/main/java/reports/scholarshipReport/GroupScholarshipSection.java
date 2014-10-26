package reports.scholarshipReport;

import java.util.ArrayList;
import java.util.List;

import dec.docDecanat.data.entity.Humanface;

public class GroupScholarshipSection {
	private String description;
	private String foundation;
	private List<GroupScholarshipSubSection> listGroupProxySubSection = new ArrayList<GroupScholarshipSubSection>();
	private List<ScholarshipFooter> listProxyFooter = new ArrayList<ScholarshipFooter>();
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
	public String getFoundation() {
		return foundation;
	}
	public void setFoundation(String foundation) {
		this.foundation = foundation;
	}
	public List<GroupScholarshipSubSection> getListGroupProxySubSection() {
		return listGroupProxySubSection;
	}
	public void setListGroupProxySubSection(List<GroupScholarshipSubSection> listGroupProxySubSection) {
		this.listGroupProxySubSection = listGroupProxySubSection;
	}
	
	
}
