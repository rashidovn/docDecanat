package dec.docDecanat.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="link_rule_employee")
public class Link_rule_employee   implements Comparable<Link_rule_employee>{

	private Long id;
	private Integer position;
	private Integer actionrule; //???

	private Rule rule;
	private Employee employee;

	public static List<String> listAction;
	static{ listAction = new ArrayList<String>();
	listAction.add("Согласование");
	listAction.add("Утверждение");
	listAction.add("Подписание");
	}


	@Id
	@Column(name="id_link_rule_employee")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="pos")
	public Integer getPosition() {
		return position;
	}


	public void setPosition(Integer position) {
		this.position = position;
	}

	@Column(name="actionrule")
	public Integer getActionrule() {
		return actionrule;
	}

	public void setActionrule(Integer actionrule) {
		this.actionrule = actionrule;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_rule")
	public Rule getRule() {
		return rule;
	}


	public void setRule(Rule rule) {
		this.rule = rule;
	}


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="id_employee")
	public Employee getEmployee() {
		return employee;
	}


	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public int compareTo(Link_rule_employee o) {
		return getPosition().compareTo(o.getPosition());
	}
}
