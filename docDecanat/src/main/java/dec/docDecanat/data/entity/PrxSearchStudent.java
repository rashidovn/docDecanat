package dec.docDecanat.data.entity;

import java.math.BigInteger;

public class PrxSearchStudent implements Comparable<PrxSearchStudent> {
	private BigInteger id_student_semester_status;
	private String family;
	private String name;
	private String patronymic;
	private String groupname;

	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPatronymic() {
		return patronymic;
	}
	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}
	public BigInteger getId_student_semester_status() {
		return id_student_semester_status;
	}
	public void setId_student_semester_status(BigInteger id_student_semester_status) {
		this.id_student_semester_status = id_student_semester_status;
	}
	
	public int compareTo(PrxSearchStudent o) {
		if((o.getFamily() != null)&&(getFamily() != null)){
			return getFamily().compareTo(o.getFamily());
		}
		return ((BigInteger) getId_student_semester_status()).compareTo((BigInteger) o.getId_student_semester_status());
	}
	
	
}
