package reports.scholarshipReport;

import java.util.ArrayList;
import java.util.List;

import dec.docDecanat.data.entity.Humanface;

public class SectionScholarshipGroup {
	private String name;
	private List<ScholarshipStudent> listHumanface = new ArrayList<ScholarshipStudent>();
	

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ScholarshipStudent> getListHumanface() {
		return listHumanface;
	}
	public void setListHumanface(List<ScholarshipStudent> listHumanface) {
		this.listHumanface = listHumanface;
	}
	
}
