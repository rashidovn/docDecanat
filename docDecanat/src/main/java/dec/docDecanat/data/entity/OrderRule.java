package dec.docDecanat.data.entity;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name="link_order_section")
public class OrderRule implements  Comparable<OrderRule>
{
	private Long id;
	private Order order;
	private Section section;
	private Set<StudentSemesterStatus> students = new HashSet<StudentSemesterStatus>();
	private boolean currentRule;
	private String description;
	private String foundation; //Основание пункта 
	private Boolean print;

	public OrderRule() {
		super();
	}

	public OrderRule(boolean currentRule) {
		super();
		this.currentRule = currentRule;
	}

	@Id
	@Column(name="id_link_order_section")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(optional=false,fetch = FetchType.EAGER)
	@JoinColumn(name="id_order_head")
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
	@JoinTable(name="link_order_student_status", joinColumns={@JoinColumn(name="id_link_order_section")}, inverseJoinColumns={@JoinColumn(name="id_student_semester_status")})
	public Set<StudentSemesterStatus> getStudents() {
		return students;
	}
	public void setStudents(Set<StudentSemesterStatus> students) {
		this.students = students;
	}

	public void addStudents(StudentSemesterStatus student) {
		students.add(student);
	}

	//@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@ManyToOne(fetch = FetchType.EAGER)//(cascade = {CascadeType.ALL} , orphanRemoval = false)
	@JoinColumn(name="id_order_section")
	public Section getSection() {
		return section;
	}
	public void setSection(Section section) {
		this.section = section;
	}
	
	@Column(name="description")
	public String getDescription() {
	return description;
	}

	public void setDescription(String description) {
	this.description = description;
	}

	@Column(name="foundation")
	public String getFoundation() {
	return foundation;
	}

	public void setFoundation(String foundation) {
	this.foundation = foundation;
	}

	@Column(name="print")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean isPrint() {
		return print;
	}

	public void setPrint(Boolean print) {
		this.print = print;
	}
	
	@Transient
	public boolean isCurrentRule() {
		return currentRule;
	}

	public void setCurrentRule(boolean currentRule) {
		this.currentRule = currentRule;
	}
	
	public int compareTo(OrderRule o) {
		if((o.getSection()!=null) && (getSection()!=null) && (o!=null) )
			if((getSection().getLayout()!=null) && (o.getSection().getLayout()!=null))
				return getSection().getLayout().compareTo(o.getSection().getLayout());
			else return 0;
		else return 0;
	}

}
