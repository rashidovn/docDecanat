package reports.scholarshipReport;

import java.util.ArrayList;
import java.util.List;

public class GroupScholarshipOrder {
	private String name;
	private List<GroupScholarshipSection> listSection = new ArrayList<GroupScholarshipSection>();
	private List<GroupScholarshipTitle> listProxyTitle = new ArrayList<GroupScholarshipTitle>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<GroupScholarshipSection> getListSection() {
		return listSection;
	}
	public void setListSection(List<GroupScholarshipSection> listSection) {
		this.listSection = listSection;
	}
	public List<GroupScholarshipTitle> getListProxyTitle() {
		return listProxyTitle;
	}
	public void setListProxyTitle(List<GroupScholarshipTitle> listProxyTitle) {
		this.listProxyTitle = listProxyTitle;
	}
}
