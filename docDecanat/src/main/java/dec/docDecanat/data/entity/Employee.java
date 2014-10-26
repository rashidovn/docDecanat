package dec.docDecanat.data.entity;

import java.beans.Transient;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name="employee")
public class Employee 
{

	private Long id;
	private long dbuid; //Идентификатор из внешней БД
	private String ad;	//Внешний логин (LDAP)
	private String email;	//Адрес электронной почты
	private Humanface humanface;
	private boolean active;



	Set<Link_rule_employee> list_LOE = new HashSet<Link_rule_employee>();

	@OneToMany(mappedBy="employee", cascade = {CascadeType.ALL})
	public Set<Link_rule_employee> getList_LOE() {
		return list_LOE;
	}

	public void setList_LOE(Set<Link_rule_employee> list_LOE) {
		this.list_LOE = list_LOE;
	}

	@Id
	@Column(name = "id_employee")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "other_dbuid")
	public long getDbuid() {
		return dbuid;
	}

	public void setDbuid(long dbuid) {
		this.dbuid = dbuid;
	}

	@Column(name = "other_ad")
	public String getAd() {
		return ad;
	}

	public void setAd(String ad) {
		this.ad = ad;
	}

	@Column(name = "other_email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToOne
	@JoinColumn(name = "id_humanface")
	public Humanface getHumanface() {
		return humanface;
	}

	public void setHumanface(Humanface humanface) {
		this.humanface = humanface;
	}

	@Column(name = "is_active")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	public Employee() {
		super();
	}
}
