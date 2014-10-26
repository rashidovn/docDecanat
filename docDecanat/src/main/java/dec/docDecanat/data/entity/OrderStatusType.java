package dec.docDecanat.data.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="order_status_type")
public class OrderStatusType {
	private Long id;
	private String name;
	private Set<Order> orders = new HashSet<Order>();
	
	public OrderStatusType() {
		super();
	}
	public OrderStatusType(String name) {
		super();
		this.name = name;
	}
	
	@Id
	@Column(name="id_order_status_type")
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
	
	
	@OneToMany(mappedBy="orderStatusType")
	public Set<Order> getOrders() {
		return orders;
	}
	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}
	
	

}
