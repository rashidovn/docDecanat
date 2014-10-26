package dec.docDecanat.data.entity;

import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="link_group_semester")
public class GroupSemester  implements Comparable<GroupSemester>{
	private Long id;

	private Date dateOfBeginSemester; // Дата начала семестра
	private Date dateOfEndSemester; // Дата окончания семестра
	private Date dateOfBeginSession; // Дата начала сессии
	private Date dateOfEndSession; // Дата окончания сессии
	private Date dateOfBeginPassWeek; //Дата начала зачётной недели
	private Date dateOfEndPassWeek; // Дата окончания зачётной недели
	private Date dateOfBeginVacation;// Дата начала каникул
	private Date dateOfEndVacation; // Дата окончания каникул
	private Date dateOfBeginGrant; // Дата начала выплаты стипендии
	private Date dateOfEndGrant; //  Дата окончания выплаты стипендии

	private Integer semesterNumber; // Номер семестра для группы
	private Integer course; 
	//private Integer id_semester;



	private Group group;
	private Set<StudentSemesterStatus> students = new HashSet<StudentSemesterStatus>();
	private Set<GroupSubject> groupSubject = new HashSet<GroupSubject>();
	private Semester semester;

	public GroupSemester() {
		super();
	}

	public GroupSemester(Date dateOfBeginSemester, Date dateOfEndSemester,
			Date dateOfBeginSession, Date dateOfEndSession,
			Date dateOfBeginPassWeek, Date dateOfEndPassWeek,
			Date dateOfBeginVacation, Date dateOfEndVacation,
			Date dateOfBeginGrant, Date dateOfEndGrant, Integer semesterNumber,
			Integer course) {
		super();
		this.dateOfBeginSemester = dateOfBeginSemester;
		this.dateOfEndSemester = dateOfEndSemester;
		this.dateOfBeginSession = dateOfBeginSession;
		this.dateOfEndSession = dateOfEndSession;
		this.dateOfBeginPassWeek = dateOfBeginPassWeek;
		this.dateOfEndPassWeek = dateOfEndPassWeek;
		this.dateOfBeginVacation = dateOfBeginVacation;
		this.dateOfEndVacation = dateOfEndVacation;
		this.dateOfBeginGrant = dateOfBeginGrant;
		this.dateOfEndGrant = dateOfEndGrant;
		this.semesterNumber = semesterNumber;
		this.course = course;
	}


	@Id
	@Column(name="id_link_group_semester")
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	/*
	@Column(name="id_semester")
	public Integer getId_semester() {
		return id_semester;
	}

	public void setId_semester(Integer id_semester) {
		this.id_semester = id_semester;
	}
	 */
	@Column(name="dateofbeginsemester")
	public Date getDateOfBeginSemester() {
		return dateOfBeginSemester;
	}

	public void setDateOfBeginSemester(Date dateOfBeginSemester) {
		this.dateOfBeginSemester = dateOfBeginSemester;
	}

	@Column(name="dateofendsemester")
	public Date getDateOfEndSemester() {
		return dateOfEndSemester;
	}

	public void setDateOfEndSemester(Date dateOfEndSemester) {
		this.dateOfEndSemester = dateOfEndSemester;
	}
	@Column(name="dateofbeginsession")
	public Date getDateOfBeginSession() {
		return dateOfBeginSession;
	}

	public void setDateOfBeginSession(Date dateOfBeginSession) {
		this.dateOfBeginSession = dateOfBeginSession;
	}
	@Column(name="dateofendsession")
	public Date getDateOfEndSession() {
		return dateOfEndSession;
	}

	public void setDateOfEndSession(Date dateOfEndSession) {
		this.dateOfEndSession = dateOfEndSession;
	}

	@Column(name="dateofbeginpassweek")
	public Date getDateOfBeginPassWeek() {
		return dateOfBeginPassWeek;
	}

	public void setDateOfBeginPassWeek(Date dateOfBeginPassWeek) {
		this.dateOfBeginPassWeek = dateOfBeginPassWeek;
	}

	@Column(name="dateofendpassweek")
	public Date getDateOfEndPassWeek() {
		return dateOfEndPassWeek;
	}

	public void setDateOfEndPassWeek(Date dateOfEndPassWeek) {
		this.dateOfEndPassWeek = dateOfEndPassWeek;
	}
	@Column(name="dateofbeginvacation")
	public Date getDateOfBeginVacation() {
		return dateOfBeginVacation;
	}

	public void setDateOfBeginVacation(Date dateOfBeginVacation) {
		this.dateOfBeginVacation = dateOfBeginVacation;
	}
	@Column(name="dateofendvacation")
	public Date getDateOfEndVacation() {
		return dateOfEndVacation;
	}

	public void setDateOfEndVacation(Date dateOfEndVacation) {
		this.dateOfEndVacation = dateOfEndVacation;
	}

	@Column(name="dateofbegingrant")
	public Date getDateOfBeginGrant() {
		return dateOfBeginGrant;
	}

	public void setDateOfBeginGrant(Date dateOfBeginGrant) {
		this.dateOfBeginGrant = dateOfBeginGrant;
	}

	@Column(name="dateofendgrant")
	public Date getDateOfEndGrant() {
		return dateOfEndGrant;
	}

	public void setDateOfEndGrant(Date dateOfEndGrant) {
		this.dateOfEndGrant = dateOfEndGrant;
	}

	@Column(name="semesternumber")
	public Integer getSemesterNumber() {
		return semesterNumber;
	}

	public void setSemesterNumber(Integer semesterNumber) {
		this.semesterNumber = semesterNumber;
	}

	@Column(name="course")
	public Integer getCourse() {
		return course;
	}

	public void setCourse(Integer course) {
		this.course = course;
	}

	@OneToMany(mappedBy="groupSemester", cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
	public Set<StudentSemesterStatus> getStudents() {
		return students;
	}
	public void setStudents(Set<StudentSemesterStatus> students) {
		this.students = students;
	}

	@ManyToOne(cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
	@JoinColumn(name="id_dic_group")
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}

	@ManyToOne(cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
	@JoinColumn(name="id_semester")
	public Semester getSemester() {
		return semester;
	}
	public void setSemester(Semester semester) {
		this.semester = semester;
	}

	@OneToMany(cascade = {CascadeType.ALL}, mappedBy="groupSemester")
	public Set<GroupSubject> getGroupSubject() {
		return groupSubject;
	}
	public void setGroupSubject(Set<GroupSubject> groupSubject) {
		this.groupSubject = groupSubject;
	}


	public int compareTo(GroupSemester o) {		
		if((o.getGroup()!=null)&&(getGroup()!=null)){
			if(getGroup().getName().compareTo(o.getGroup().getName())==0){
				return getId().compareTo(o.getId());
			}else
				return getGroup().getName().compareTo(o.getGroup().getName());
		}
		else return 0;

	}

	//	static public List<GroupSemester> sortLGroup(List<GroupSemester> cur, Boolean rev){
	//		
	//		
	//		return cur;
	//	}
}


