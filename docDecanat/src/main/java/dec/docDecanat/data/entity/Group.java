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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="dic_group")
public class Group implements Comparable<Group> {

	private Long id;
	private String name;
	private Date dateOfBegin;
	private Date dateOfEnd;
	private Curriculum curriculum;
	private Set<GroupSemester> gs = new HashSet<GroupSemester>();
	
	public Group(String name, Date dateOfBegin, Date dateOfEnd) {
		super();
		this.name = name;
		this.dateOfBegin = dateOfBegin;
		this.dateOfEnd = dateOfEnd;
	}
	
	public Group(){
		super();
	}

	@Id
	@Column(name="id_dic_group")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="groupname")
	public String getName() {
		return name;
	}


	public void setName(String groupName) {
		this.name = groupName;
	}

	@Column(name="dateofbegin")
	public Date getDateOfBegin() {
		return dateOfBegin;
	}


	public void setDateOfBegin(Date dateOfBegin) {
		this.dateOfBegin = dateOfBegin;
	}

	@Column(name="dateofend")
	public Date getDateOfEnd() {
		return dateOfEnd;
	}


	public void setDateOfEnd(Date dateOfEnd) {
		this.dateOfEnd = dateOfEnd;
	}

	public int compareTo(Group o) {
		// TODO Auto-generated method stub
		return getName().compareTo(o.getName());
		
	}
	
	@OneToMany(mappedBy = "group", cascade = {CascadeType.ALL})
	public Set<GroupSemester> getGs() {
		return gs;
	}
	public void setGs(Set<GroupSemester> gs) {
		this.gs = gs;
	}

	@ManyToOne
	@JoinColumn(name="id_curriculum")
	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}
	
	
}
