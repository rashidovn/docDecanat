package dec.docDecanat.data.entity;

import java.awt.Checkbox;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hlayout;

import dec.docDecanat.data.dao.SectionDao;
import dec.docDecanat.data.entity.Rule;

import org.zkoss.json.JSONObject;
import org.zkoss.json.JSONArray;
import org.zkoss.json.parser.*;

@Entity
@Table(name="order_section")
public class Section implements  Comparable<Section>
{
	private Long id;
	private boolean print;
	private Rule rule;
	private String name; 
	private String description; 
	private Integer layout;
	private String parameters;
	private String foundation;
	private List<SectionParameters> secParametersScholarship;
	private SectionParameters secParametersTransfer;
	public static boolean flagNewSection;

	public Section() {
		super();
	}

	public Section(Rule rule, String description) {
		super();
		this.rule = rule;
		this.description = description;
	}

	@Id
	@Column(name="id_order_section")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}	

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="print")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isPrint() {
		return print;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}

	@ManyToOne
	@JoinColumn(name="id_order_rule")
	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	@Column(name="name")
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

	@Column(name="layout")
	public Integer getLayout() {
		return layout;
	}

	public void setLayout(Integer layout) {
		this.layout = layout;
	}

	public int compareTo(Section o) {
		return getLayout().compareTo(o.getLayout());
	}

	@Transient	
	public SectionParameters getSecParametersTransfer() {
		return secParametersTransfer;
	}

	public void setSecParametersTransfer(SectionParameters secParametersTransfer) {
		this.secParametersTransfer = secParametersTransfer;
	}

	@Transient	
	public List<SectionParameters> getSecParameters() {
		return secParametersScholarship;
	}

	public void setSecParameters(List<SectionParameters> secParameters) {
		this.secParametersScholarship = secParameters;
	}

	@Column(name="parameters")
	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	@Column(name="foundation")
	public String getFoundation() {
		return foundation;
	}

	public void setFoundation(String foundation) {
		this.foundation = foundation;
	}

	public List<SectionParameters> parseParametersScholarship(){		
		List<SectionParameters> listParam = new ArrayList<SectionParameters>();
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = new JSONObject();
		jsonObject = (JSONObject) jsonParser.parse(parameters);
		JSONArray lang= (JSONArray) jsonObject.get("parameters");
		for (int i = 0; i < lang.size(); i++) {
			JSONObject paramSec = (JSONObject) lang.get(i);
			SectionParameters secPar = new SectionParameters();

			secPar.setName( Integer.parseInt(paramSec.get("name").toString()));
			secPar.setSimbol((String) paramSec.get("simbol")); 
			secPar.setAmount(Integer.parseInt(paramSec.get("amount").toString())); 
			secPar.setMeasure((String) paramSec.get("measure")); 
			try{

				if(paramSec.get("prol")!=null){
					secPar.setProl(Boolean.valueOf(paramSec.get("prol").toString()));
					//System.out.println(paramSec.get("prol").toString()+"dscswcedc");
				}else{
					secPar.setProl(false);
				}
			}catch(Exception ex){
				secPar.setProl(false);
			}
			listParam.add(secPar);
		}
		return listParam;
	}

	public SectionParameters parseParametersTransfer(){		
		SectionParameters param = new SectionParameters();
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = new JSONObject();
		jsonObject = (JSONObject) jsonParser.parse(parameters);
		param.setSimbolMin((String) jsonObject.get("simbolMin")); 
		param.setSimbolMax((String) jsonObject.get("simbolMax")); 
		System.out.println(">>here "+jsonObject.get("amountMaxSubject"));
		param.setAmountMaxSubject(Integer.parseInt(jsonObject.get("amountMaxSubject").toString()));
		param.setAmountMinSubject(Integer.parseInt(jsonObject.get("amountMinSubject").toString())); 
		if(!jsonObject.get("amountSummerSemester").equals("")){
			param.setAmountSummerSemester(Integer.parseInt(jsonObject.get("amountSummerSemester").toString())); 
		}else{
			param.setAmountSummerSemester(1);
		}
		param.setAllsemOrOnlysem(Boolean.valueOf(jsonObject.get("allORonly").toString())); 
		param.setPayOrFree(Boolean.valueOf(jsonObject.get("payOrFree").toString())); 
		try{
			if(jsonObject.get("prol")!=null){
				param.setProl(Boolean.valueOf(jsonObject.get("prol").toString()));
			}else{
				param.setProl(false);
			}
		}catch(Exception ex){
			param.setProl(false);
		}
		return param;
	}
}
