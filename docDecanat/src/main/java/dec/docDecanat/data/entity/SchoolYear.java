package dec.docDecanat.data.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="schoolyear")
public class SchoolYear{

	Long id;
	Date dateofbegin; 	//Дата начала учебного года
	Date dateofend; 	//Дата окончания учебного года

	@Id
	@Column(name="id_schoolyear")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="dateofbegin")
	public Date getDateofbegin() {
		return dateofbegin;
	}
	public void setDateofbegin(Date dateofbegin) {
		this.dateofbegin = dateofbegin;
	}
	
	@Column(name="dateofend")
	public Date getDateofend() {
		return dateofend;
	}
	public void setDateofend(Date dateofend) {
		this.dateofend = dateofend;
	}
}
