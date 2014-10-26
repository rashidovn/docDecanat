package dec.docDecanat.data.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="link_note_student_semester_status_order")
public class Link_note_student_semester_status_order implements Comparable<Link_note_student_semester_status_order>{

	private Long id;
	private StudentSemesterStatus sss; //Ссылка на student_semester_status
	private OrderRule orruStart; //переместить студента
	private OrderRule orruFinish; //Куда переместить студента
	private String note; 				//Причина перемещения
	private Employee employee; // Ссылка на человека, который перенес студента (если необходимо отслеживать чьё это действие)
	private Date dateOfNotes; // дата создания причины
	private Order order; // привязка к приказу

	@Id
	@Column(name="id_link_note_student_semester_status_order")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}


	@ManyToOne
	@JoinColumn(name="id_order_head")
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="dateofnote")
	public Date getDateOfNotes() {
		return dateOfNotes;
	}
	public void setDateOfNotes(Date dateOfNotes) {
		this.dateOfNotes = dateOfNotes;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_student_semester_status")
	public StudentSemesterStatus getSss() {
		return sss;
	}
	public void setSss(StudentSemesterStatus sss) {
		this.sss = sss;
	}

	@ManyToOne
	@JoinColumn(name="id_section_start")
	public OrderRule getOrruStart() {
		return orruStart;
	}
	public void setOrruStart(OrderRule orruStart) {
		this.orruStart = orruStart;
	}

	@ManyToOne
	@JoinColumn(name="id_section_finish")
	public OrderRule getOrruFinish() {
		return orruFinish;
	}
	public void setOrruFinish(OrderRule orruFinish) {
		this.orruFinish = orruFinish;
	}

	@Column(name="note")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_employee")
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public int compareTo(Link_note_student_semester_status_order o) {
		if(o!=null && this!=null){
//			System.out.println(">>id>> "+o.getId()+" / "+this.getId());
//			System.out.println(">>note>> "+o.getNote()+" / "+this.getNote());
			if(o.getId()!=null && getId()!=null){
				return getId().compareTo(o.getId());}
			else{
				return 0;
			}
		}else
			return 0;
	}
}
