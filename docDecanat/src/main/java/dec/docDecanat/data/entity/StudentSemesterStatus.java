package dec.docDecanat.data.entity;

import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import dec.docDecanat.data.comparator.GroupSemesterComp;
import dec.docDecanat.data.dao.DAO;
import dec.docDecanat.data.dao.GroupSemesterDao;
import dec.docDecanat.data.dao.OrderDao;

@Entity
@Table(name="student_semester_status")
public class StudentSemesterStatus implements Comparable<StudentSemesterStatus>{
	
	private Long id;
	
	private Integer debtCount; // Количество долгов
	private Integer sessionResult; // Закрыл ли студент сессию:...
	private Integer formOfStudy; // Форма обучения: 1 - очная, 2 - заочная, 3 - вечерняя
	
	private Boolean governmentFinanced; //Флаг бюджетной формы обучения
	private Boolean putAppForSocialGrant; // Флаг подачи заявления на социальную стипендию
	private Boolean getSocialGrant; //Флаг начисления (получения) социальной стипендии
	private Boolean deducted; // Флаг того, что студент отчислен в текущем семестре
	private Boolean scientificWork; //Флаг научной работы
	private Boolean publicWork; // Флаг общественной работы
	private Boolean chernobolec ; //Флаг того, что студент является ликвидатором аварии на ЧАЭС
	private Boolean sirota; // Флаг того, что студент сирота
	private Boolean invalid; // Флаг того, что студент имеет инвалидность
	private Boolean sessionProlongation; // Флаг того, что студент имеет продление сессии
	private Boolean combatants; //Флаг того, что студент является участником боевых действий
	private Boolean academicLeave; //  Флаг того, что студент в академическом отпуске
	private Boolean listener; // Слушатель: посещает занятия, но неофициально. Не учитывается в приказах и отчётах.
	private Boolean trustagreement; // Флаг целевого договора
	private Boolean secondDegree; //Флаг второго высшего образования (для заочников)
	private Boolean groupLeader; // Флаг старосты группы
	private Boolean educationComplete; // Флаг того, что студент завершил обучение
	private Boolean orderScholarship; // Студент прошел в стипендиальном приказе
	private Boolean orderTransfer; // Студент прошел в  переводном приказе
	
	private StudentCard studentCard;
	private Set<OrderRule> orderRules = new HashSet<OrderRule>();
	private Set<SessionRating> sessionRating = new HashSet<SessionRating>();
	private Set<Link_note_student_semester_status_order> list_notes = new HashSet<Link_note_student_semester_status_order>(); 

	private GroupSemester groupSemester;
	//привязать даты продления сессии
//	private Date dateOfBegin; //для стипендиального
//	private Date dateOfEnd;
	private Date prolongationbegindate; //дата окончания продления
	private Date prolongationenddate; //дата отчисления, в случае наличии долгов
	private Date dateOfScholarshipBegin; //начало выплат стипендии
	private Date dateOfScholarshipEnd;//окончание выплат стипендии
	
	//Список для хранения студентов содержащихся на данный момент в West (Обновляется регулярно с 10:00 до 14:00)
	public static List<StudentSemesterStatus> listSSSWest = new ArrayList<StudentSemesterStatus>();
	//public static List<StudentSemesterStatus> listSSSWest = new ArrayList<StudentSemesterStatus>();
	//public static List<StudentSemesterStatus> listSSSOrder = new ArrayList<StudentSemesterStatus>();
	
	//@Transient - свойства
	public Boolean inSection=false;
	public Integer localSesRes=null;
	
	
	public StudentSemesterStatus(StudentCard studentCard) {
		super();
		this.studentCard = studentCard;
	}
	
	public StudentSemesterStatus() {
		super();
	}

	@Id
	@Column(name="id_student_semester_status")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;	
	}

	@Column(name="is_ord_scholarship")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getOrderScholarship() {
		return orderScholarship;
	}

	public void setOrderScholarship(Boolean orderScholarship) {
		this.orderScholarship = orderScholarship;
	}
	
	@Column(name="is_ord_transfer")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getOrderTransfer() {
		return orderTransfer;
	}

	public void setOrderTransfer(Boolean orderTransfer) {
		this.orderTransfer = orderTransfer;
	}
	
	@Column(name="dateofscholarshipbegin")
	public Date getDateOfScholarshipBegin() {
		return dateOfScholarshipBegin;
	}

	public void setDateOfScholarshipBegin(Date dateOfScholarshipBegin) {
		this.dateOfScholarshipBegin = dateOfScholarshipBegin;
	}

	@Column(name="dateofscholarshipend")
	public Date getDateOfScholarshipEnd() {
		return dateOfScholarshipEnd;
	}

	public void setDateOfScholarshipEnd(Date dateOfScholarshipEnd) {
		this.dateOfScholarshipEnd = dateOfScholarshipEnd;
	}

	/*Связь с студентчиским билетом n к 1*/
	@ManyToOne
	@JoinColumn(name="id_studentcard")
	public StudentCard getStudentCard() {
		return studentCard;
	}
	public void setStudentCard(StudentCard studentCard) {
		this.studentCard = studentCard;
	}
	

	@OneToMany(mappedBy="sss", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	public Set<Link_note_student_semester_status_order> getList_notes() {
		return list_notes;
	}

	public void setList_notes(Set<Link_note_student_semester_status_order> list_notes) {
		this.list_notes = list_notes;
	}

	@OneToMany(mappedBy="student", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	public Set<SessionRating> getSessionRating() {
		return sessionRating;
	}
	public void setSessionRating(Set<SessionRating> sessionRating) {
		this.sessionRating = sessionRating;
	}
	
	@ManyToMany
	@JoinTable(name="link_order_student_status", joinColumns={@JoinColumn(name="id_student_semester_status")}, inverseJoinColumns={@JoinColumn(name="id_link_order_section")})
	public Set<OrderRule> getOrderRules() {
		return orderRules;
	}
	
	public void setOrderRules(Set<OrderRule> orderRules) {
		this.orderRules = orderRules;
	}
	
	public void addOrderRules(OrderRule orderRule){
		orderRules.add(orderRule);
	}

	@Column(name="debtcount")
	public Integer getDebtCount() {
	return debtCount;
	}

	public void setDebtCount(Integer debtCount) {
		this.debtCount = debtCount;
	}
	
	@Column(name="sessionresult")
	public Integer getSessionResult() {
		return sessionResult;
	}

	public void setSessionResult(Integer sessionResult) {
		this.sessionResult = sessionResult;
	}

	@Column(name="formofstudy")
	public Integer getFormOfStudy() {
		return formOfStudy;
	}

	public void setFormOfStudy(Integer formOfStudy) {
		this.formOfStudy = formOfStudy;
	}

	@Column(name="is_government_financed")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getGovernmentFinanced() {
		return governmentFinanced;
	}

	public void setGovernmentFinanced(Boolean governmentFinanced) {
		this.governmentFinanced = governmentFinanced;
	}

	@Column(name="is_put_app_for_social_grant")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getPutAppForSocialGrant() {
		return putAppForSocialGrant;
	}

	public void setPutAppForSocialGrant(Boolean putAppForSocialGrant) {
		this.putAppForSocialGrant = putAppForSocialGrant;
	}

	@Column(name="is_get_social_grant")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getGetSocialGrant() {
		return getSocialGrant;
	}

	public void setGetSocialGrant(Boolean getSocialGrant) {
		this.getSocialGrant = getSocialGrant;
	}

	@Column(name="is_deducted")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getDeducted() {
		return deducted;
	}

	public void setDeducted(Boolean deducted) {
		this.deducted = deducted;
	}

	@Column(name="is_scientificwork")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getScientificWork() {
		return scientificWork;
	}

	public void setScientificWork(Boolean scientificWork) {
		this.scientificWork = scientificWork;
	}

	@Column(name="is_publicwork")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getPublicWork() {
		return publicWork;
	}

	public void setPublicWork(Boolean publicWork) {
		this.publicWork = publicWork;
	}

	@Column(name="is_chernobolec")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getChernobolec() {
		return chernobolec;
	}

	public void setChernobolec(Boolean chernobolec) {
		this.chernobolec = chernobolec;
	}

	@Column(name="is_sirota")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getSirota() {
		return sirota;
	}

	public void setSirota(Boolean sirota) {
		this.sirota = sirota;
	}

	@Column(name="is_invalid")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getInvalid() {
		return invalid;
	}

	public void setInvalid(Boolean invalid) {
		this.invalid = invalid;
	}

	@Column(name="is_sessionprolongation")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getSessionProlongation() {
		return sessionProlongation;
	}

	public void setSessionProlongation(Boolean sessionProlongation) {
		this.sessionProlongation = sessionProlongation;
	}

	@Column(name="is_combatants")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getCombatants() {
		return combatants;
	}

	public void setCombatants(Boolean combatants) {
		this.combatants = combatants;
	}

	@Column(name="is_academicleave")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getAcademicLeave() {
		return academicLeave;
	}

	public void setAcademicLeave(Boolean academicLeave) {
		this.academicLeave = academicLeave;
	}

	@Column(name="is_listener")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getListener() {
		return listener;
	}

	public void setListener(Boolean listener) {
		this.listener = listener;
	}

	@Column(name="is_trustagreement")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getTrustagreement() {
		return trustagreement;
	}

	public void setTrustagreement(Boolean trustagreement) {
		this.trustagreement = trustagreement;
	}

	@Column(name="is_seconddegree")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getSecondDegree() {
		return secondDegree;
	}

	public void setSecondDegree(Boolean secondDegree) {
		this.secondDegree = secondDegree;
	}

	@Column(name="is_group_leader")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getGroupLeader() {
		return groupLeader;
	}

	public void setGroupLeader(Boolean groupLeader) {
		this.groupLeader = groupLeader;
	}

	@Column(name="is_educationcomplete")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	public Boolean getEducationComplete() {
		return educationComplete;
	}

	public void setEducationComplete(Boolean educationComplete) {
		this.educationComplete = educationComplete;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_link_group_semester")
	public GroupSemester getGroupSemester() {
		return groupSemester;
	}

	public void setGroupSemester(GroupSemester groupSemester) {
		this.groupSemester = groupSemester;
	}

	@Column(name="prolongationbegindate")
	public Date getProlongationbegindate() {
		return prolongationbegindate;
	}

	public void setProlongationbegindate(Date prolongationbegindate) {
		this.prolongationbegindate = prolongationbegindate;
	}

	@Column(name="prolongationenddate")
	public Date getProlongationenddate() {
		return prolongationenddate;
	}

	public void setProlongationenddate(Date prolongationenddate) {
		this.prolongationenddate = prolongationenddate;
	}
	
	public int compareTo(StudentSemesterStatus o) {
		if((getStudentCard() != null)&&(o.getStudentCard() != null)){
			if((getStudentCard().getHumanface() != null)&&(o.getStudentCard().getHumanface() != null)){
				return getStudentCard().getHumanface().getFamily().compareTo(o.getStudentCard().getHumanface().getFamily());
			}
		}
		return ((Long) getId()).compareTo((Long) o.getId());
	}
	
	//Метод получения результатов предыдущей сессии
	public Integer prevSessionResult() {
		OrderDao or=new OrderDao(); 
		List<GroupSemester> listGrS=or.getListGroup(this);
		//List<GroupSemester> listGrS=new ArrayList<GroupSemester>(this.getGroupSemester().getGroup().getGs());
		GroupSemesterComp comparetoGr = new GroupSemesterComp();
		comparetoGr.setCompareMethod(GroupSemesterComp.GSCompareMethods.BY_SEMESTER_REV);
		Collections.sort(listGrS, comparetoGr);
		if (listGrS.size() > 1) {
			for (int i = 0; i < listGrS.get(1).getStudents().size(); ++i) {
				GroupSemester gsLoc=listGrS.get(1);
				List<StudentSemesterStatus> listSSSloc=new ArrayList<StudentSemesterStatus>(gsLoc.getStudents());
				StudentSemesterStatus dopSSS = listSSSloc.get(i);
				if (this.getStudentCard().getId().equals(dopSSS.getStudentCard().getId())) {
					return dopSSS.getSessionResult();
				}
			}
		}
		return null;
	}
	
	public Integer prevSessionResult(Integer num) {
		GroupSemesterDao gsD = new GroupSemesterDao();
		List<GroupSemester> listGrS=null;
		try {
			listGrS = gsD.getSomeFromGroup(this.getGroupSemester().getGroup(), num, this.getGroupSemester().getSemester());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		GroupSemesterComp comparetoGr = new GroupSemesterComp();
		comparetoGr.setCompareMethod(GroupSemesterComp.GSCompareMethods.BY_SEMESTER_REV);
		Collections.sort(listGrS, comparetoGr);
		if (listGrS.size() > 1) {
			for (int i = 0; i < listGrS.get(1).getStudents().size(); ++i) {
				GroupSemester gsLoc=listGrS.get(1);
				List<StudentSemesterStatus> listSSSloc=new ArrayList<StudentSemesterStatus>(gsLoc.getStudents());
				StudentSemesterStatus dopSSS = listSSSloc.get(i);
				if (this.getStudentCard().getId().equals(dopSSS.getStudentCard().getId())) {
					return dopSSS.getSessionResult();
				}
			}
		}
		return null;
	}
	
	public Integer prevSessionResult(List<GroupSemester> listGrS) {
		System.out.println(">>sr size="+listGrS.size());
		GroupSemesterComp comparetoGr = new GroupSemesterComp();
		comparetoGr.setCompareMethod(GroupSemesterComp.GSCompareMethods.BY_SEMESTER_REV);
		Collections.sort(listGrS, comparetoGr);
		if (listGrS.size() > 1) {
			for (int i = 0; i < listGrS.get(1).getStudents().size(); ++i) {
				StudentSemesterStatus dopSSS = (StudentSemesterStatus) listGrS.get(1).getStudents().toArray()[i];
				if (this.getStudentCard().getId().equals(dopSSS.getStudentCard().getId())) {
					return dopSSS.getSessionResult();
				}
			}
		}else{
			System.out.println("sss ListSize < 1 : "+listGrS.size());
		}
		return null;
	}

	
	
	
	@Transient
	public Boolean getInSection() {
		return inSection;
	}

	public void setInSection(Boolean inSection) {
		this.inSection = inSection;
	}
	
	@Transient
	public Integer getLocalSesRes() {
		return localSesRes;
	}

	public void setLocalSesRes(Integer localSesRes) {
		this.localSesRes = localSesRes;
	}


}
