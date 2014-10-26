package reports.scholarshipReport;

import dec.docDecanat.data.entity.StudentSemesterStatus;

public class ScholarshipStudent implements Comparable<ScholarshipStudent>{
	private String name;
	private String family;
	private String patronymic;
	private String recordBook;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getPatronymic() {
		return patronymic;
	}
	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}
	public String getRecordBook() {
		return recordBook;
	}
	public void setRecordBook(String recordBook) {
		this.recordBook = recordBook;
	}
	
	public int compareTo(ScholarshipStudent o) {
		if((getFamily() != null)&&(o.getFamily() != null)){
			return getFamily().compareTo(o.getFamily());
		}
		return ((String) getRecordBook()).compareTo((String) o.getRecordBook());
	}

}
