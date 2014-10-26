package dec.docDecanat.data.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="studentcard")
public class StudentCard {
	
	private Long id;
	private String recordBook; //Номер зачётной книжки
	private String contractNumber; // Номер договора о платном обучении
	private String eMail; // Адрес электронной почты
	private String login; // Видимо логин студента
	private Integer studentCategory;
	private Date contractDate;
	private Set<StudentSemesterStatus> students = new HashSet<StudentSemesterStatus>(0);
	private Humanface humanface;
	
	public StudentCard(Humanface humanface, String recordBook) {
		super();
		this.humanface = humanface;
		this.recordBook = recordBook;
	}

	public StudentCard() {
		super();
	}

	@Id
	@Column(name="id_studentcard")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public String getRecordBook() {
		return recordBook;
	}


	public void setRecordBook(String recordBook) {
		this.recordBook = recordBook;
	}

	//Ассоциация с s_s_S 1 к n
	@OneToMany(mappedBy ="studentCard")
	public Set<StudentSemesterStatus> getStudents() {
		return students;
	}
	public void setStudents(Set<StudentSemesterStatus> students) {
		this.students = students;
	}

	/*Ассоциация с humanface 1 к 1 */
	@OneToOne
	//@PrimaryKeyJoinColumn
	@JoinColumn(name="id_humanface")
	public Humanface getHumanface() {
		return humanface;
	}
	public void setHumanface(Humanface humanface) {
		this.humanface = humanface;
	}

	@Column(name="contractnumber")
	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	@Column(name="other_email")
	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	@Column(name="other_login")
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Column(name="studentcategory")
	public Integer getStudentCategory() {
		return studentCategory;
	}

	public void setStudentCategory(Integer studentCategory) {
		this.studentCategory = studentCategory;
	}

	@Column(name="contractdate")
	public Date getContractDate() {
		return contractDate;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}
}
