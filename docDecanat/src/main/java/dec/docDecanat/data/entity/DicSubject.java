package dec.docDecanat.data.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="dic_subject")
public class DicSubject {
	
	private Long id;
	private String subjectName;
	private String subjectShortName;
	private Set<Subject> subjects = new HashSet<Subject>(0);
	
	public DicSubject() {
		super();
	}
	
	public DicSubject(String subjectName, String subjectShortName) {
		super();
		this.subjectName = subjectName;
		this.subjectShortName = subjectShortName;
	}
	
	@Id
	@Column(name="id_dic_subject")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="subjectname")
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	@Column(name="subjectshortname")
	public String getSubjectShortName() {
		return subjectShortName;
	}
	public void setSubjectShortName(String subjectShortName) {
		this.subjectShortName = subjectShortName;
	}
	
	@OneToMany(mappedBy = "dicSub", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public Set<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(Set<Subject> subjects) {
		this.subjects = subjects;
	}	
}
