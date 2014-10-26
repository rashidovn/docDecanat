package dec.docDecanat.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="link_group_semester_subject")
public class GroupSubject {
	
	private Long id;
	
	private double esoGradeCurrent;
	private double esoGradeMax;
	private Integer esoStudyCount;
	
	private double hoursCourseProject; //(КП) часы на курсовой проект
	private double hoursCourseWork; // (КР) часы на курсовую работу
	private double hoursControlDistance; //(КРЗ) часы на контроль работы заочника
	private double hoursConsult; // часы на консультацию
	private double hoursControlSelfStudy; // часы на контроль самостоятельной работы
	
	private GroupSemester groupSemester;
	private Subject subject;
	
	public GroupSubject() {
		super();
	}
	public GroupSubject(double esoGradeCurrent, double esoGradeMax,
			Integer esoStudyCount, double hoursCourseProject,
			double hoursCourseWork, double hoursControlDistance,
			double hoursConsult, double hoursControlSelfStudy) {
		super();
		this.esoGradeCurrent = esoGradeCurrent;
		this.esoGradeMax = esoGradeMax;
		this.esoStudyCount = esoStudyCount;
		this.hoursCourseProject = hoursCourseProject;
		this.hoursCourseWork = hoursCourseWork;
		this.hoursControlDistance = hoursControlDistance;
		this.hoursConsult = hoursConsult;
		this.hoursControlSelfStudy = hoursControlSelfStudy;
	}
	
	@Id
	@Column(name="id_link_group_semester_subject")
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	@Column(name="esostudycount")
	public Integer getEsoStudyCount() {
		return esoStudyCount;
	}
	public void setEsoStudyCount(Integer esoStudyCount) {
		this.esoStudyCount = esoStudyCount;
	}
	@Column(name="hourscourseproject")
	public double getHoursCourseProject() {
		return hoursCourseProject;
	}
	public void setHoursCourseProject(double hoursCourseProject) {
		this.hoursCourseProject = hoursCourseProject;
	}
	@Column(name="hourscoursework")
	public double getHoursCourseWork() {
		return hoursCourseWork;
	}
	public void setHoursCourseWork(double hoursCourseWork) {
		this.hoursCourseWork = hoursCourseWork;
	}
	@Column(name="hourscontroldistance")
	public double getHoursControlDistance() {
		return hoursControlDistance;
	}
	public void setHoursControlDistance(double hoursControlDistance) {
		this.hoursControlDistance = hoursControlDistance;
	}
	@Column(name="hoursconsult")
	public double getHoursConsult() {
		return hoursConsult;
	}
	public void setHoursConsult(double hoursConsult) {
		this.hoursConsult = hoursConsult;
	}
	@Column(name="hourscontrolselfstudy")
	public double getHoursControlSelfStudy() {
		return hoursControlSelfStudy;
	}
	public void setHoursControlSelfStudy(double hoursControlSelfStudy) {
		this.hoursControlSelfStudy = hoursControlSelfStudy;
	}
	
	@ManyToOne
	@JoinColumn(name="id_link_group_semester")
	public GroupSemester getGroupSemester() {
		return groupSemester;
	}
	public void setGroupSemester(GroupSemester groupSemester) {
		this.groupSemester = groupSemester;
	}
	
	@ManyToOne
	@JoinColumn(name="id_subject")
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	
	
}
