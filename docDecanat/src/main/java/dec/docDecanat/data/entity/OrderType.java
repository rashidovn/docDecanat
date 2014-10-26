package dec.docDecanat.data.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="order_type")
public class OrderType implements Comparable<OrderType>{
	private Long id;
	private String name;
	private Set<OrderSubType> orderSubTypes = new HashSet<OrderSubType>(); 
	
	public OrderType() {
		super();
	}

	public OrderType(String name) {
		super();
		this.name = name;
	}

	@Id
	@Column(name="id_order_type")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="name")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy="orderType",fetch = FetchType.EAGER)
	public Set<OrderSubType> getOrderSubTypes() {
		return orderSubTypes;
	}
	
	public void setOrderSubTypes(Set<OrderSubType> orderSubTypes) {
		this.orderSubTypes = orderSubTypes;
	}

	public int compareTo(OrderType o) {
		if(o.getName()!=null && getName()!=null)
		return getName().compareTo(o.getName());
		else return 0;
	}
}
