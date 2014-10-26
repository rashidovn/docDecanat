package dec.docDecanat.data.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="curriculum")
public class Curriculum implements Comparable<Curriculum>{

	private Long  id;
	private Long id_direction;
	private Integer formofstudy; 								//Форма обучения: 1 - очная, 2 - заочная
	private Integer  distancetype; 								//Заочная форма обучения: 1 - заочно-очная, 2 - заочная
	private float   periodofstudy; 							//Период обучения, может быть дробным числом
	private Integer  qualification; 							// Квалификация: 1 - инженер, 2 - бакалавр, 3 - магистр
	private Date  enteryear; 								// Год набора по данному учебному плану
	private Integer  generation; 								//Поколение учебного плана
	private Long  id_chair; 								// Идентификатор кафедры, за которой закреплён учебный план
	private String specialitytitle ; 						// Наименование специальности
	private String directioncode ; 							// Код направления
	private String qualificationcode ; 						// Код квалификации
	private String programcode ; 							// Код программы
	private String planfilename ; 							// Название файла с учебным планом для привязки к планам в системе
	private Set<Group> groups = new HashSet<Group>();
	private Integer course;
	
	@Transient
	public Integer getCourse() {
		return course;
	}
	public void setCourse(Integer course) {
		this.course = course;
	}
	@OneToMany(mappedBy="curriculum", cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	public Set<Group> getGroups() {
		return groups;
	}
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}
	@Id
	@Column(name="id_curriculum")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="id_direction")
	public Long getId_direction() {
		return id_direction;
	}
	public void setId_direction(Long id_direction) {
		this.id_direction = id_direction;
	}
	

	@Column(name="formofstudy")
	public Integer getFormofstudy() {
		return formofstudy;
	}
	public void setFormofstudy(Integer formofstudy) {
		this.formofstudy = formofstudy;
	}
	

	@Column(name="distancetype")
	public Integer getDistancetype() {
		return distancetype;
	}
	public void setDistancetype(Integer distancetype) {
		this.distancetype = distancetype;
	}
	

	@Column(name="periodofstudy")
	public float getPeriodofstudy() {
		return periodofstudy;
	}
	public void setPeriodofstudy(float periodofstudy) {
		this.periodofstudy = periodofstudy;
	}
	

	@Column(name="qualification")
	public Integer getQualification() {
		return qualification;
	}
	public void setQualification(Integer qualification) {
		this.qualification = qualification;
	}
	

	@Column(name="enteryear")
	public Date getEnteryear() {
		return enteryear;
	}
	public void setEnteryear(Date enteryear) {
		this.enteryear = enteryear;
	}
	
	@Column(name="generation")
	public Integer getGeneration() {
		return generation;
	}
	public void setGeneration(Integer generation) {
		this.generation = generation;
	}

	@Column(name="id_chair")
	public Long getId_chair() {
		return id_chair;
	}
	public void setId_chair(Long id_chair) {
		this.id_chair = id_chair;
	}

	@Column(name="specialitytitle")
	public String getSpecialitytitle() {
		return specialitytitle;
	}
	public void setSpecialitytitle(String specialitytitle) {
		this.specialitytitle = specialitytitle;
	}

	@Column(name="directioncode")
	public String getDirectioncode() {
		return directioncode;
	}
	public void setDirectioncode(String directioncode) {
		this.directioncode = directioncode;
	}

	@Column(name="qualificationcode")
	public String getQualificationcode() {
		return qualificationcode;
	}
	public void setQualificationcode(String qualificationcode) {
		this.qualificationcode = qualificationcode;
	}

	@Column(name="programcode")
	public String getProgramcode() {
		return programcode;
	}
	public void setProgramcode(String programcode) {
		this.programcode = programcode;
	}

	@Column(name="planfilename")
	public String getPlanfilename() {
		return planfilename;
	}
	public void setPlanfilename(String planfilename) {
		this.planfilename = planfilename;
	}
	
	public int compareTo(Curriculum o) {		
		return getSpecialitytitle().compareTo(o.getSpecialitytitle());
	}
}
