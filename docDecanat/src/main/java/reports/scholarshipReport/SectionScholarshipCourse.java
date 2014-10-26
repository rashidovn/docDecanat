package reports.scholarshipReport;

import java.util.ArrayList;
import java.util.List;

import reports.transferReport.TransferCourse;
import dec.docDecanat.data.entity.GroupSemester;

public class SectionScholarshipCourse implements Comparable<SectionScholarshipCourse> {
	private Integer course;
	private String foundation;
	private List<GroupSemester> listGroupSemester = new ArrayList<GroupSemester>();
	private List<SectionScholarshipGroup> listProxyGroup = new ArrayList<SectionScholarshipGroup>();
	
	public List<SectionScholarshipGroup> getListProxyGroup() {
		return listProxyGroup;
	}
	public void setListProxyGroup(List<SectionScholarshipGroup> listProxyGroup) {
		this.listProxyGroup = listProxyGroup;
	}
	public Integer getCourse() {
		return course;
	}
	public void setCourse(Integer course) {
		this.course = course;
	}
	public List<GroupSemester> getListGroupSemester() {
		return listGroupSemester;
	}
	public void setListGroupSemester(List<GroupSemester> listGroupSemester) {
		this.listGroupSemester = listGroupSemester;
	}
	
	public int compareTo(SectionScholarshipCourse o) {
		return course.compareTo(o.course);
	}
	public String getFoundation() {
		return foundation;
	}
	public void setFoundation(String foundation) {
		this.foundation = foundation;
	}
	
}
