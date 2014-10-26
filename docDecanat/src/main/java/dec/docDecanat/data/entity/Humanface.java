package dec.docDecanat.data.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="humanface")
public class Humanface {
	private Long id;
	private String name;
	private String family;
	private String patronymic;
	private StudentCard studentCard;
	
	public Humanface(String name, String family, String partonymic) {
		super();
		this.name = name;
		this.family = family;
		this.patronymic = partonymic;
	}

	public Humanface() {
		super();
	}

	@Id
    @Column(name="id_humanface")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Transient
	public String getFIO(){
		return family + " " + name + " " + patronymic;
	}
	
	
	@Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column
	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	@Column
	public String getPatronymic() {
		return patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
	}

	/*ассоциация с studentCard 1 к 1*/
	@OneToOne
	@PrimaryKeyJoinColumn
	public StudentCard getStudentCard() {
		return studentCard;
	}
	public void setStudentCard(StudentCard studentCard) {
		this.studentCard = studentCard;
	}


}
