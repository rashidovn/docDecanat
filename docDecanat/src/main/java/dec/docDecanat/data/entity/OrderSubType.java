package dec.docDecanat.data.entity;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import dec.docDecanat.data.dao.OrderSubTypeDao;
import dec.docDecanat.data.dao.OrderTypeDao;
import dec.docDecanat.data.dao.RuleDao;

@Entity
@Table(name="order_subtype")
public class OrderSubType implements Comparable<OrderSubType>{
	private Long id;
	private String name;
	private OrderType orderType;
	//TODO: Убрать.
	//private Set<Order> orders = new HashSet<Order>();
	private Set<Rule> rules = new HashSet<Rule>();

	public OrderSubType() {
		super();
	}

	public OrderSubType(OrderType type, String name) {
		super();
		this.orderType = type;
		this.name = name;
	}
	
	@Id
	@Column(name="id_order_subtype")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_order_type")
	public OrderType getOrderType() {
		return orderType;
	}
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

//	@OneToMany(mappedBy="orderSubType",fetch = FetchType.EAGER)
//	public Set<Order> getOrders() {
//		return orders;
//	}
//	public void setOrders(Set<Order> orders) {
//		this.orders = orders;
//	}

	@OneToMany(mappedBy="OST",fetch = FetchType.EAGER)
	public Set<Rule> getRules() {
		return rules;
	}
	public void setRules(Set<Rule> rules) {
		this.rules = rules;
	}

	public int compareTo(OrderSubType o) {
		if(o.getName()!=null && getName()!=null)
			return getName().compareTo(o.getName());
		else return 0;
	}

	public static ListModel<OrderSubType> loadCmb(){
		try {
			List<OrderSubType> listOrderSubType = new ArrayList<OrderSubType>(OrderSubTypeDao.OSTDglobal.getAll());
			Collections.sort(listOrderSubType);
			ListModel<OrderSubType> listOrderSubTypeCmb = new ListModelList<OrderSubType>(listOrderSubType);
			return listOrderSubTypeCmb;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
