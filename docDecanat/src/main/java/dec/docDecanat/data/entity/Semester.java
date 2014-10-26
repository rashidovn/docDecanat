package dec.docDecanat.data.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="semester")
public class Semester implements Comparable<Semester>{
	private Long id;
	private SchoolYear schoolYear;
	private Boolean currentSem;
	private int formOfStudy; //- форма обучения, 1 - очная, 2 - заочная
	private Boolean closed; // флаг того, что семестр закрыт, запрещенно редактирование данных за этот семестр
	private String namesem; // для корректного отображения в комбобоксе
	private Set<GroupSemester> gs = new HashSet<GroupSemester>();
	
	public Semester(Boolean currentSem) {
		super();
		this.currentSem = currentSem;
	}
	public Semester() {
		super();
	}
	
	@Id
	@Column(name="id_semester")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne
	@JoinColumn(name="id_schoolyear")
	public SchoolYear getSchoolYear() {
		return schoolYear;
	}
	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}
	
	@Column(name="is_current_sem")//надо многое поправить...
	public Boolean getCurrentSem() {
		return currentSem;
	}
	public void setCurrentSem(Boolean currentSem) {
		this.currentSem = currentSem;
	}
	
	@Column(name="formofstudy")
	public int getFormOfStudy() {
		return formOfStudy;
	}
	public void setFormOfStudy(int formOfStudy) {
		this.formOfStudy = formOfStudy;
	}
	@Column(name="is_closed")
	public Boolean getClosed() {
		return closed;
	}
	public void setClosed(Boolean closed) {
		this.closed = closed;
	}
	
	@OneToMany(mappedBy="semester")
	public Set<GroupSemester> getGs() {
		return gs;
	}
	public void setGs(Set<GroupSemester> gs) {
		this.gs = gs;
	}
	@Transient
	public String getNamesem() {
		return namesem;
	}
	public void setNamesem(String namesem) {
		this.namesem = namesem;
	}
	
	public String getName(Semester sem){
		//Calendar begin = new GregorianCalendar();
		//begin.setTime(sem.getSchoolYear().getDateofbegin());
		//Calendar end = new GregorianCalendar();
		//begin.setTime(sem.getSchoolYear().getDateofend());
		String name= sem.getId()+". " + new SimpleDateFormat("yyyy").format(sem.getSchoolYear().getDateofbegin()) +
				"/" +  new SimpleDateFormat("yyyy").format(sem.getSchoolYear().getDateofend()) ;
		if(sem.getFormOfStudy()==1)
			name+= " очная ";
		if(sem.getFormOfStudy()==2)
			name+= " заочная ";
		return name;
	}
	
	public int compareTo(Semester o) {
		return o.getId().compareTo(getId());
	}

}
