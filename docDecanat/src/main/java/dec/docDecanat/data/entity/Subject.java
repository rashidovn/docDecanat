package dec.docDecanat.data.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="subject")
public class Subject {
	private Long id;
	private Integer hoursCount;//количество часов "Всего"
	private Integer hoursAudcount;//количество часов "Всего аудиторных"
	
	private boolean exam;//флаг экзамена
	private boolean pass;//флаг зачета
	private boolean courseProject;//флаг курсового проекта
	private boolean courseWork;//флаг курсовой работы
	private boolean practic;//флаг практики
	private boolean active;//флаг, обозначающий признак того, что запись активна(не удалена)
	
	private Set<GroupSubject> gs = new HashSet<GroupSubject>();
	private DicSubject dicSub; // объект класса DicSubject для связи с Subject
	private Set<SessionRating> ratings=new HashSet<SessionRating>();
	
	public Subject() {
		super();
	}
	
	public Subject(Integer hoursCount, Integer hoursAudcount, boolean exam,
			boolean pass, boolean courseProject, boolean courseWork,
			boolean practic, boolean active) {
		super();
		this.hoursCount = hoursCount;
		this.hoursAudcount = hoursAudcount;
		this.exam = exam;
		this.pass = pass;
		this.courseProject = courseProject;
		this.courseWork = courseWork;
		this.practic = practic;
		this.active = active;
	}
	
	@Id
	@Column(name="id_subject")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="hourscount")
	public Integer getHoursCount() {
		return hoursCount;
	}
	public void setHoursCount(Integer hoursCount) {
		this.hoursCount = hoursCount;
	}
	
	@Column(name="hoursaudcount")
	public Integer getHoursAudcount() {
		return hoursAudcount;
	}
	public void setHoursAudcount(Integer hoursAudcount) {
		this.hoursAudcount = hoursAudcount;
	}
	
	@Column(name="is_exam")
	public boolean isExam() {
		return exam;
	}
	public void setExam(boolean exam) {
		this.exam = exam;
	}
	
	@Column(name="is_pass")
	public boolean isPass() {
		return pass;
	}
	public void setPass(boolean pass) {
		this.pass = pass;
	}
	
	@Column(name="is_courseproject")
	public boolean isCourseProject() {
		return courseProject;
	}
	public void setCourseProject(boolean courseProject) {
		this.courseProject = courseProject;
	}
	
	@Column(name="is_coursework")
	public boolean isCourseWork() {
		return courseWork;
	}
	public void setCourseWork(boolean courseWork) {
		this.courseWork = courseWork;
	}
	
	@Column(name="is_practic")
	public boolean isPractic() {
		return practic;
	}
	public void setPractic(boolean practic) {
		this.practic = practic;
	}
	
	@Column(name="is_active")
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

	@OneToMany(mappedBy="subject")
	public Set<GroupSubject> getGs() {
		return gs;
	}
	public void setGs(Set<GroupSubject> gs) {
		this.gs = gs;
	}

	@ManyToOne
	@JoinColumn(name = "id_dic_subject")
	public DicSubject getDicSub() {
		return dicSub;
	}

	public void setDicSub(DicSubject dicSub) {
		this.dicSub = dicSub;
	}

	
	@OneToMany(mappedBy="subject")
	public Set<SessionRating> getRatings() {
		return ratings;
	}

	public void setRatings(Set<SessionRating> ratings) {
		this.ratings = ratings;
	}
	
	
}
