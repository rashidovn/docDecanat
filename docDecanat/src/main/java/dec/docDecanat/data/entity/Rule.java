package dec.docDecanat.data.entity;

import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import dec.docDecanat.data.dao.RuleDao;
import dec.docDecanat.data.dao.SectionDao;

@Entity
@Table(name="order_rule")
public class Rule  implements Comparable<Rule> {
	public static Rule ruChoose;

	private Long id;
	private String name;	
	private Set<Section> sections = new HashSet<Section>();
	private OrderSubType OST = new OrderSubType();
	List<Link_rule_employee> listLINK = new ArrayList<Link_rule_employee>();
	private String description;
	private Boolean commonPersonal;

	public Rule() {
		super();
	}

	public Rule(String name) {
		super();
		this.name = name;
	}

	@Id
	@Column(name="id_order_rule")
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

	@Column(name="description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne	
	@JoinColumn(name="id_order_subtype")
	public OrderSubType getOST() {
		return OST;
	}

	public void setOST(OrderSubType oST) {
		OST = oST;
	}

	@OneToMany(mappedBy="rule", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	public Set<Section> getSections() {
		return sections;
	}

	public void setSections(Set<Section> sections) {
		this.sections = sections;
	}

	@OneToMany(mappedBy="rule", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	public List<Link_rule_employee> getListLINK() {
		return listLINK;
	}

	public void setListLINK(List<Link_rule_employee> listLINK) {
		this.listLINK = listLINK;
	}
	
	@Column(name="commonpersonal")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean isCommonPersonal() {
		return commonPersonal;
	}

	public void setCommonPersonal(Boolean commonPersonal) {
		this.commonPersonal = commonPersonal;
	}

	public int compareTo(Rule o) {
		if(o.getName()!=null && getName()!=null)
			return getName().compareTo(o.getName());
		else return 0;
	}

	public static ListModel<Rule> loadCmb(OrderSubType OST){
		List<Rule> listRule;
		try {
			listRule = new ArrayList<Rule>(OST.getRules());
			Collections.sort(listRule);
			for (int r = 0; r < listRule.size(); r++) {
				listRule.get(r).setSections(new HashSet<Section>(SectionDao.secDGlobal.getAllFromRule(listRule.get(r))));

				for (int s = 0; s < listRule.get(r).getSections().size(); s++) {
					Section sec = new Section(); 
					sec = (Section) listRule.get(r).getSections().toArray()[s];
					sec.setSecParameters(new ArrayList<SectionParameters>());
					if(OST.getName().equals("Стипендиальный")){
						sec.setSecParameters(sec.parseParametersScholarship());
					}
					if(OST.getName().equals("Переводной")){
						sec.setSecParametersTransfer(sec.parseParametersTransfer());
					}	
				}
			}
			ListModel<Rule> listRuleCmb = new ListModelList<Rule>(listRule);
			return listRuleCmb;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
