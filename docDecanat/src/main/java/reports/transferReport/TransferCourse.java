package reports.transferReport;

import java.util.ArrayList;
import java.util.List;

import dec.docDecanat.data.entity.GroupSemester;

public class TransferCourse implements Comparable<TransferCourse>{
	private Integer course;
	private String foundation;
	private List<TransferGroup> listTransferGroup = new ArrayList<TransferGroup>();
	private List<GroupSemester> listGroupSemester = new ArrayList<GroupSemester>();
	
	public Integer getCourse() {
		return course;
	}
	public void setCourse(Integer course) {
		this.course = course;
	}
	public List<TransferGroup> getListTransferGroup() {
		return listTransferGroup;
	}
	public void setListTransferGroup(List<TransferGroup> listTransferGroup) {
		this.listTransferGroup = listTransferGroup;
	}
	public List<GroupSemester> getListGroupSemester() {
		return listGroupSemester;
	}
	public void setListGroupSemester(List<GroupSemester> listGroupSemester) {
		this.listGroupSemester = listGroupSemester;
	}
	public int compareTo(TransferCourse o) {
		return course.compareTo(o.course);
	}
	public String getFoundation() {
		return foundation;
	}
	public void setFoundation(String foundation) {
		this.foundation = foundation;
	}

	
}
