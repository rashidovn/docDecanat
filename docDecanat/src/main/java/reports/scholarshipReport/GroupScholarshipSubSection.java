package reports.scholarshipReport;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.mapping.Array;

import dec.docDecanat.data.entity.StudentSemesterStatus;

public class GroupScholarshipSubSection implements Comparable<GroupScholarshipSubSection>{
	private String description;
	private List<StudentSemesterStatus> listStudent = new ArrayList<StudentSemesterStatus>();
	private List<ScholarshipStudent> listProxyStudents = new ArrayList<ScholarshipStudent>();
	private Boolean prol=false;


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ScholarshipStudent> getListProxyStudents() {
		return listProxyStudents;
	}

	public void setListProxyStudents(List<ScholarshipStudent> listProxyStudents) {
		this.listProxyStudents = listProxyStudents;
	}

	public List<StudentSemesterStatus> getListStudent() {
		return listStudent;
	}

	public void setListStudent(List<StudentSemesterStatus> listStudent) {
		this.listStudent = listStudent;
	}
	

	public Boolean getProl() {
		return prol;
	}

	public void setProl(Boolean prol) {
		this.prol = prol;
	}

	public int compareTo(GroupScholarshipSubSection o) {
		if(o != null && this != null ){
			if(o.getListStudent().size() != 0 && this.getListStudent().size() != 0){
				if(o.getProl()){
					Integer sesRes1=GroupScholarshipList.getPrevSessionResul(o.getListStudent().get(0));
					Integer sesRes2=GroupScholarshipList.getPrevSessionResul(this.getListStudent().get(0));
					if(sesRes1>=2 && sesRes2>=2)return sesRes1.compareTo(sesRes2);
					if(sesRes1<2)return 1;
					if(sesRes2<2)return -1;
				}
				return o.getListStudent().get(0).getSessionResult().compareTo(this.getListStudent().get(0).getSessionResult());
			}
		}
		return 0;
	}

}
