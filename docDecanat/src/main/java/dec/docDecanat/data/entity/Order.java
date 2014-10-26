package dec.docDecanat.data.entity;

import java.beans.Transient;
import java.util.Date;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Type;




@Entity
@Table(name="order_head")
public class Order implements Comparable<Order> {

	private Long id;
	private String number;
	private Date dateOfBegin;
	private Date dateOfEnd;
	private OrderSubType orderSubType;
	private Set<OrderRule> orderRules = new HashSet<OrderRule>();
	private OrderStatusType orderStatusType;
	private String description;
	private String descriptionSpec;
	private Boolean commonPersonal;

	//	private Set<Link_note_student_semester_status_order> list_notes = new HashSet<Link_note_student_semester_status_order>();
	//	
	//
	//	@OneToMany(mappedBy="order", cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval = true)
	//	public Set<Link_note_student_semester_status_order> getList_notes() {
	//		return list_notes;
	//	}
	//
	//	public void setList_notes(
	//			Set<Link_note_student_semester_status_order> list_notes) {
	//		this.list_notes = list_notes;
	//	}

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Order(OrderSubType orderSubType, String number, Date dateOfBegin,
			Date dateOfEnd) {
		super();
		this.orderSubType = orderSubType;
		this.number = number;
		this.dateOfBegin = dateOfBegin;
		this.dateOfEnd = dateOfEnd;
	}

	public static String orderScholarship = "Стипендиальный";
	public static String orderTransfer = "Переводной";

	public Order(){

	}

	@Id
	@Column(name="id_order_head")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="ordernumber")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	@OneToMany(mappedBy="order", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	public Set<OrderRule> getOrderRules(){
		return orderRules;
	}
	public void setOrderRules(Set<OrderRule> orderRule){
		this.orderRules = orderRule;
	}
	public void addOrderRule(OrderRule newOrderRule){
		orderRules.add(newOrderRule);
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_order_subtype")
	public OrderSubType getOrderSubType() {
		return orderSubType;
	}
	public void setOrderSubType(OrderSubType orderSubType) {
		this.orderSubType = orderSubType;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_order_status_type")
	public OrderStatusType getOrderStatusType() {
		return orderStatusType;
	}
	public void setOrderStatusType(OrderStatusType orderStatusType) {
		this.orderStatusType = orderStatusType;
	}

	@Column(name="descriptionspec")
	public String getDescriptionSpec() {
	return descriptionSpec;
	}

	public void setDescriptionSpec(String descriptionSpec) {
	this.descriptionSpec = descriptionSpec;
	}

	@Column(name="commonpersonal")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean isCommonPersonal() {
		return commonPersonal;
	}

	public void setCommonPersonal(Boolean commonPersonal) {
		this.commonPersonal = commonPersonal;
	}

	
	public int compareTo(Order o) {
		if(getDateOfEnd()==null && o.getDateOfEnd()!=null){
			return -1;
		}

		if(o.getDateOfEnd()==null && getDateOfEnd()!=null){
			return 1;
		}

		if(o.getDateOfEnd()!=null && getDateOfEnd()!=null){
			if(o.getDateOfEnd().compareTo(getDateOfEnd())!=0){
				return o.getDateOfEnd().compareTo(getDateOfEnd());
			}else{
				return o.getId().compareTo(getId());
			}
		}

		if(o.getDateOfBegin()!=null && getDateOfBegin()!=null){
			if(o.getDateOfBegin().compareTo(getDateOfBegin())!=0){
				return o.getDateOfBegin().compareTo(getDateOfBegin());
			}else{
				return o.getId().compareTo(getId());
			}
		}
		return 0;
	}


}
