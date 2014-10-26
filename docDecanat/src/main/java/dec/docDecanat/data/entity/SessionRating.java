package dec.docDecanat.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;


@Entity
@Table(name="sessionrating")
public class SessionRating {
	private Long id;
	
	private boolean exam;//флаг экзамена
	private boolean pass;//флаг зачета
	private boolean courseProject;//флаг курсового проекта
	private boolean courseWork;//флаг курсовой работы
	private boolean practic;//Флаг практики (летней, учебной, производственной и т.д.)
	
	private boolean esoStudy; // Флаг обучения по электронным обучающим курсам (если для дисциплины указан обучающий курс)
	
	private int examRating;//оценка за экзамен (учитывать совместно с флагом exam)
	private int passRating;// Оценка за зачёт (учитывать совместно с флагом pass): 0 - не зачтено, 1 - зачтено
	private int courseProjectRating;// Оценка за курсовой проект (учитывать совместно с флагом courseproject)
	private int courseWorkRating; //Оценка за курсовую работу (учитывать совместно с флагом coursework)
	private int practicRating;//Оценка за практику (учитывать совместно с флагом practic)
	
	private int skipCount; //Количество пропусков занятий на текущий момент (из электронного журнала)
	private int visitCount; //Количество посещений занятий на текущий момент (из электронного журнала)
	
	private double esoGradeCurrent; //Текущее количество баллов по электронным обучающим курсам, которое набрано студентом к текущему моменту
	private double esoGradeMax; // Максимальное количество баллов по электронным обучающим курсам, которое можно набрать к текущему моменту
	
	private StudentSemesterStatus student;
	
	//ТЕСТОВОЕ ПОЛЕ
	private Subject subject;
	
	public SessionRating() {
		super();
	}

	public SessionRating(boolean exam, boolean pass, boolean courseProject,
			boolean courseWork, boolean practic, int examRating,
			int passRating, int courseProjectRating, int courseWorkRating,
			int practicRating) {
		super();
		this.exam = exam;
		this.pass = pass;
		this.courseProject = courseProject;
		this.courseWork = courseWork;
		this.practic = practic;
		this.examRating = examRating;
		this.passRating = passRating;
		this.courseProjectRating = courseProjectRating;
		this.courseWorkRating = courseWorkRating;
		this.practicRating = practicRating;
	}
	
	@Id
	@Column(name="id_sessionrating")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="is_exam")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isExam() {
		return exam;
	}
	public void setExam(boolean exam) {
		this.exam = exam;
	}
	
	@Column(name="is_pass")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isPass() {
		return pass;
	}
	public void setPass(boolean pass) {
		this.pass = pass;
	}
	
	@Column(name="is_courseproject")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isCourseProject() {
		return courseProject;
	}
	public void setCourseProject(boolean courseProject) {
		this.courseProject = courseProject;
	}
	
	@Column(name="is_coursework")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isCourseWork() {
		return courseWork;
	}
	public void setCourseWork(boolean courseWork) {
		this.courseWork = courseWork;
	}
	
	@Column(name="is_practic")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isPractic() {
		return practic;
	}
	public void setPractic(boolean practic) {
		this.practic = practic;
	}
	
	@Column(name="examrating")
	public int getExamRating() {
		return examRating;
	}
	public void setExamRating(int examRating) {
		this.examRating = examRating;
	}
	
	@Column(name="passrating")
	public int getPassRating() {
		return passRating;
	}
	public void setPassRating(int passRating) {
		this.passRating = passRating;
	}
	
	@Column(name="courseprojectrating")
	public int getCourseProjectRating() {
		return courseProjectRating;
	}
	public void setCourseProjectRating(int courseProjectRating) {
		this.courseProjectRating = courseProjectRating;
	}
	
	@Column(name="courseworkrating")
	public int getCourseWorkRating() {
		return courseWorkRating;
	}
	public void setCourseWorkRating(int courseWorkRating) {
		this.courseWorkRating = courseWorkRating;
	}
	
	@Column(name="practicrating")
	public int getPracticRating() {
		return practicRating;
	}
	public void setPracticRating(int practicRating) {
		this.practicRating = practicRating;
	}

	@Column(name="is_esostudy")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isEsoStudy() {
		return esoStudy;
	}

	public void setEsoStudy(boolean esoStudy) {
		this.esoStudy = esoStudy;
	}
	
	@Column(name="skipcount")
	public int getSkipCount() {
		return skipCount;
	}

	public void setSkipCount(int skipCount) {
		this.skipCount = skipCount;
	}

	@Column(name="visitcount")
	public int getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

	@Column(name="esogradecurrent")
	public double getEsoGradeCurrent() {
		return esoGradeCurrent;
	}

	public void setEsoGradeCurrent(double esoGradeCurrent) {
		this.esoGradeCurrent = esoGradeCurrent;
	}

	@Column(name="esogrademax")
	public double getEsoGradeMax() {
		return esoGradeMax;
	}

	public void setEsoGradeMax(double esoGradeMax) {
		this.esoGradeMax = esoGradeMax;
	}

	@ManyToOne
	@JoinColumn(name="id_student_semester_status")
	public StudentSemesterStatus getStudent() {
		return student;
	}
	
	public void setStudent(StudentSemesterStatus student) {
		this.student = student;
	}

	//Связка
	@ManyToOne
	@JoinColumn(name="id_subject")
	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	
	
}
