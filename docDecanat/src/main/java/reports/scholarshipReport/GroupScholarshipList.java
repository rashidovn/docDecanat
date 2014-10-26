package reports.scholarshipReport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dec.docDecanat.data.comparator.GroupSemesterComp;
import dec.docDecanat.data.entity.Group;
import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.Humanface;
import dec.docDecanat.data.entity.Link_rule_employee;
import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.StudentSemesterStatus;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.pageControllers.CtrOrder;

/*//TODO: подписывающие лица, если они нужны будут.
 List<Link_rule_employee> listLRE = ((OrderRule) CtrOrder.ord
 .getOrderRules().toArray()[0]).getSection().getRule()
 .getListLINK();

 * for(int zz = 0; zz < listLRE.size(); ++zz){
 * //List<Link_employer_department> listLED =
 * listLRE.get(zz).getEmployee().getLED(); for(int zzz = 0; zzz
 * < listLED.size(); ++zzz){
 * if(listLED.get(zzz).getRole().getName().equals("Ректор")){
 * //Записать ректора + его имя } }
 * if(listLRE.get(zz).getEmployee().get) }

 // вывод надписи согласующие
 for (int z = 0; z < listLRE.size(); ++z) {
 if (listLRE.get(z).getActionrule() == 1) {
 ProxyFooter proxyFooter = produceFooter(listLRE.get(z)
 .getEmployee().getHumanface().getFamily());
 listProxyFooter.add(proxyFooter);
 }
 }*/

public class GroupScholarshipList {
	
	public int resultStatus=0;
	
	public List<GroupScholarshipOrder> getAllProxy() {
		List<GroupScholarshipTitle> listProxyTitle = new ArrayList<GroupScholarshipTitle>();
		GroupScholarshipTitle proxyTitle;
		if (CtrOrder.ord.getDescription() == null) {
			proxyTitle = produceTitle(" ");
		} else {
			proxyTitle = produceTitle(CtrOrder.ord.getDescription());
		}
		listProxyTitle.add(proxyTitle);

		List<StudentSemesterStatus> listAllStudent = new ArrayList<StudentSemesterStatus>();
		for (int k = 0; k < CtrOrder.ord.getOrderRules().size(); ++k) {
			listAllStudent.addAll(new ArrayList<StudentSemesterStatus>(
					((OrderRule) CtrOrder.ord.getOrderRules().toArray()[k])
					.getStudents()));
		}
		List<GroupScholarshipOrder> listProxy = new ArrayList<GroupScholarshipOrder>();
		List<GroupSemester> listGrS = CtrOrder
				.sectionStudentForGroup(listAllStudent);

		for (int i = 0; i < listGrS.size(); ++i) {
			List<ScholarshipFooter> listProxyFooter = new ArrayList<ScholarshipFooter>();
			List<OrderRule> listOrderRule = CtrOrder
					.groupStudentForSection(new ArrayList<StudentSemesterStatus>(
							listGrS.get(i).getStudents()));
			for (int z = 0; z < listOrderRule.size(); ++z) {
				listOrderRule.get(z).getSection().setSecParameters(listOrderRule.get(z).getSection().parseParametersScholarship());

				if (listOrderRule.get(z).getDescription() == null) {
					listOrderRule.get(z).setDescription(
							listOrderRule.get(z).getSection().getDescription());
				} else {
					if (listOrderRule.get(z).getDescription().equals("")) {
						listOrderRule.get(z).setDescription(
								listOrderRule.get(z).getSection()
								.getDescription());
					}
				}
				// TODO: Добавить, если нужно будет, основание.
			}
			listOrderRule = divOrderRule(listOrderRule);
			List<GroupScholarshipSection> listProxySection = new ArrayList<GroupScholarshipSection>();
			Collections.sort(listOrderRule);
			for (int j = 0; j < listOrderRule.size(); ++j) {
				List<GroupScholarshipSubSection> listGroupProxySubSection = getProxySubSection(listOrderRule
						.get(j));
				Collections.sort(listGroupProxySubSection);

				for (int l = 0; l < listGroupProxySubSection.size(); ++l) {
					List<ScholarshipStudent> listProxyStudent = new ArrayList<ScholarshipStudent>();
					for (int f = 0; f < listGroupProxySubSection.get(l)
							.getListStudent().size(); ++f) {
						StudentSemesterStatus sss = listGroupProxySubSection
								.get(l).getListStudent().get(f);
						ScholarshipStudent proxyStudent = produceStudent(sss
								.getStudentCard().getHumanface().getName(), sss
								.getStudentCard().getHumanface().getFamily(),
								sss.getStudentCard().getHumanface()
								.getPatronymic(), sss.getStudentCard()
								.getRecordBook());
						listProxyStudent.add(proxyStudent);
					}
					Collections.sort(listProxyStudent);
					listGroupProxySubSection.get(l).getListProxyStudents()
					.addAll(listProxyStudent);
				}
				GroupScholarshipSection proxySection = null;
				if (listOrderRule.get(j).getDescription() != null) {
					proxySection = produceSection(listOrderRule.get(j)
							.getDescription(), listGroupProxySubSection,
							listProxyFooter);
				} else {
					proxySection = produceSection(listOrderRule.get(j)
							.getDescription(), listGroupProxySubSection,
							listProxyFooter);
				}
				//				if (listOrderRule.get(j).getSection().isPrint()) {
				if(listOrderRule.get(j).isPrint()==null)
					listOrderRule.get(j).setPrint(listOrderRule.get(i).getSection().isPrint());
				if (listOrderRule.get(j).isPrint()) {
					listProxySection.add(proxySection);
				}
			}

			if (listProxySection.size() != 0) {
				listProxy.add(produce(listGrS.get(i).getGroup().getName(),
						listProxySection, listProxyTitle));
			}
		}

		return listProxy;
	}


	public List<GroupScholarshipSubSection> getProxySubSection(OrderRule orderRule) {
		System.out.println("Имя секции: " + orderRule.getSection().getName());
		List<GroupScholarshipSubSection> listSubSection = new ArrayList<GroupScholarshipSubSection>();

		if (!orderRule.getSection().getName().equals("Отказать")) {
			boolean flagProl = false;
			System.out.println("getSecParameters " + orderRule.getSection().getSecParameters() );
			if (orderRule.getSection().getSecParameters() != null) {
				if (orderRule.getSection().getSecParameters().size() > 0) {
					if (orderRule.getSection().getSecParameters().get(0).isProl()) {
						flagProl = true;
					} else {
						flagProl = false;
					}
				}
				else{
					flagProl = false;
				}
			} else{
				flagProl = false;
			}
			if(flagProl){
				List<StudentSemesterStatus> listSSSProl = new ArrayList<StudentSemesterStatus>(
						orderRule.getStudents());
				List<GroupScholarshipSubSection> dopListSubSectionProl = new ArrayList<GroupScholarshipSubSection>();
				for (int i = 0; i < listSSSProl.size(); ++i) {
					boolean addInSubSectionProl = false;
					for (int j = 0; j < dopListSubSectionProl.size(); ++j) {
						if (dopListSubSectionProl.get(j)
								.getListStudent().size() > 0) {
							Integer sessionRes = listSSSProl.get(i).prevSessionResult();
							Integer subSectionRes = dopListSubSectionProl.get(j).getListStudent().get(0).getSessionResult();
							System.out.println("SessionRes: " + sessionRes);
							if (sessionRes != null) {
								if (sessionRes.equals(subSectionRes) || (sessionRes<2 && subSectionRes<2)) {
									dopListSubSectionProl.get(j).getListStudent().add(listSSSProl.get(i));
									addInSubSectionProl = true;
									System.out.println("Описание подпункта 2: " + dopListSubSectionProl.get(j).getDescription());
								}
							}
						}
					}
					if (!addInSubSectionProl) {
						GroupScholarshipSubSection groupPSSProl = new GroupScholarshipSubSection();
						Integer sesRes=listSSSProl.get(i).prevSessionResult();
						if (sesRes.equals(2)) {
							groupPSSProl
							.setDescription("обучающимся на 'хорошо':");
						}
						if (sesRes.equals(3)) {
							groupPSSProl
							.setDescription("обучающимся на 'хорошо' и 'отлично':");
						}
						if (sesRes.equals(4)) {
							groupPSSProl
							.setDescription("обучающимся на 'отлично':");
						}
						if (sesRes < 2) {
							groupPSSProl.setDescription("");
						}
						if (sesRes == null) {
							groupPSSProl.setDescription("");
						}
						groupPSSProl.setProl(true);
						groupPSSProl.getListStudent().add(
								listSSSProl.get(i));
						dopListSubSectionProl.add(groupPSSProl);
						System.out.println("Описание подпункта 3: " + groupPSSProl.getDescription());
					}
				}
				return dopListSubSectionProl;
			}else{
				List<StudentSemesterStatus> listSSS = new ArrayList<StudentSemesterStatus>(
						orderRule.getStudents());
				List<GroupScholarshipSubSection> dopListSubSection = new ArrayList<GroupScholarshipSubSection>();
				for (int i = 0; i < listSSS.size(); ++i) {
					boolean addInSubSection = false;
					for (int j = 0; j < dopListSubSection.size(); ++j) {
						if (dopListSubSection.get(j).getListStudent().size() > 0) {
							if (listSSS.get(i).getSessionResult().equals(dopListSubSection.get(j).getListStudent().get(0).getSessionResult()))
							{
								dopListSubSection.get(j).getListStudent().add(listSSS.get(i));
								addInSubSection = true;
								System.out.println("Описание подпункта 1: " + dopListSubSection.get(j).getDescription());
							}
						}
					}
					if (!addInSubSection) {
						System.out.println(">>SS debug "+listSSS.get(i).getSessionResult()+" | "+listSSS.get(i).getStudentCard().getHumanface().getFIO());
						GroupScholarshipSubSection groupPSS = new GroupScholarshipSubSection();
						if (listSSS.get(i).getSessionResult().equals(2)) {
							groupPSS.setDescription("обучающимся на 'хорошо':");
						}
						if (listSSS.get(i).getSessionResult().equals(3)) {
							groupPSS.setDescription("обучающимся на 'хорошо' и 'отлично':");
						}
						if (listSSS.get(i).getSessionResult().equals(4)) {
							groupPSS.setDescription("обучающимся на 'отлично':");
						}
						if (listSSS.get(i).getSessionResult() < 2) {
							groupPSS.setDescription("");
						}
						if (listSSS.get(i).getSessionResult() == null) {
							groupPSS.setDescription("");
						}
						groupPSS.getListStudent().add(listSSS.get(i));
						dopListSubSection.add(groupPSS);
					}
				}

				return dopListSubSection;
			}
		}
		else {
			GroupScholarshipSubSection groupProxySubSection = new GroupScholarshipSubSection();
			groupProxySubSection.setDescription("");
			groupProxySubSection.getListStudent().addAll(
					orderRule.getStudents());
			listSubSection.add(groupProxySubSection);
			System.out.println("Описание подпункта 4: " + groupProxySubSection.getDescription());
		}
		return listSubSection;
	}

	//TODO: Убрать отсюда.
	static public Integer getPrevSessionResul(StudentSemesterStatus sss) {
		Group gr = sss.getGroupSemester().getGroup();
		List<GroupSemester> listGrS = new ArrayList<GroupSemester>(gr.getGs());
		GroupSemesterComp comparetoGr = new GroupSemesterComp();
		comparetoGr
		.setCompareMethod(GroupSemesterComp.GSCompareMethods.BY_SEMESTER_REV);
		Collections.sort(listGrS, comparetoGr);
		if (listGrS.size() > 1) {
			for (int i = 0; i < listGrS.get(1).getStudents().size(); ++i) {
				StudentSemesterStatus dopSSS = (StudentSemesterStatus) listGrS
						.get(1).getStudents().toArray()[i];
				if (sss.getStudentCard().getId()
						.equals(dopSSS.getStudentCard().getId())) {
					return dopSSS.getSessionResult();
				}
			}
		}
		return null;
	}

	/*
	 * public List<GroupProxy> getAllProxy() { List<GroupProxyTitle>
	 * listProxyTitle = new ArrayList<GroupProxyTitle>(); for (int m = 0; m < 1;
	 * ++m) { GroupProxyTitle proxyTitle; if (CtrOrder.ord.getDescription() ==
	 * null) { proxyTitle = produceTitle(" "); } else { proxyTitle =
	 * produceTitle(CtrOrder.ord.getDescription()); }
	 * 
	 * listProxyTitle.add(proxyTitle); }
	 * 
	 * List<StudentSemesterStatus> listAllStudent = new
	 * ArrayList<StudentSemesterStatus>(); for (int k = 0; k <
	 * CtrOrder.ord.getOrderRules().size(); ++k) { listAllStudent.addAll(new
	 * ArrayList<StudentSemesterStatus>( ((OrderRule)
	 * CtrOrder.ord.getOrderRules().toArray()[k]) .getStudents())); }
	 * List<GroupProxy> listProxy = new ArrayList<GroupProxy>();
	 * List<GroupSemester> listGrS = CtrOrder
	 * .sectionStudentForGroup(listAllStudent); for (int i = 0; i <
	 * listGrS.size(); ++i) { List<ProxyFooter> listProxyFooter = new
	 * ArrayList<ProxyFooter>(); List<OrderRule> listOrderRule = CtrOrder
	 * .groupStudentForSection(new ArrayList<StudentSemesterStatus>(
	 * listGrS.get(i).getStudents())); for (int z = 0; z < listOrderRule.size();
	 * ++z) { //
	 * listOrderRule.get(z).getSection().setSecParametersTransfer(listOrderRule
	 * .get(z).getSection().parseParametersTransfer());
	 * 
	 * if (listOrderRule.get(z).getDescription() == null) {
	 * listOrderRule.get(z).setDescription(
	 * listOrderRule.get(z).getSection().getDescription()); } else { if
	 * (listOrderRule.get(z).getDescription().equals("")) {
	 * listOrderRule.get(z).setDescription( listOrderRule.get(z).getSection()
	 * .getDescription()); } } System.out.println("из секции: " +
	 * listOrderRule.get(z).getSection().getDescription());
	 * 
	 * } listOrderRule = divOrderRule(listOrderRule); List<GroupProxySection>
	 * listProxySection = new ArrayList<GroupProxySection>();
	 * Collections.sort(listOrderRule); for (int j = 0; j <
	 * listOrderRule.size(); ++j) { List<ProxyStudent> listProxyStudent = new
	 * ArrayList<ProxyStudent>(); for (int f = 0; f <
	 * listOrderRule.get(j).getStudents().size(); ++f) { StudentSemesterStatus
	 * sss = (StudentSemesterStatus) listOrderRule
	 * .get(j).getStudents().toArray()[f]; ProxyStudent proxyStudent =
	 * produceStudent(sss .getStudentCard().getHumanface().getName(), sss
	 * .getStudentCard().getHumanface().getFamily(), sss
	 * .getStudentCard().getHumanface().getPatronymic(),
	 * sss.getStudentCard().getRecordBook());
	 * listProxyStudent.add(proxyStudent); } Collections.sort(listProxyStudent);
	 * List<Link_rule_employee> listLRE = ((OrderRule) CtrOrder.ord
	 * .getOrderRules().toArray()[0]).getSection().getRule() .getListLINK();
	 * 
	 * for(int zz = 0; zz < listLRE.size(); ++zz){
	 * //List<Link_employer_department> listLED =
	 * listLRE.get(zz).getEmployee().getLED(); for(int zzz = 0; zzz <
	 * listLED.size(); ++zzz){
	 * if(listLED.get(zzz).getRole().getName().equals("Ректор")){ //Записать
	 * ректора + его имя } } if(listLRE.get(zz).getEmployee().get) }
	 * 
	 * // вывод надписи согласующие for (int z = 0; z < listLRE.size(); ++z) {
	 * if (listLRE.get(z).getActionrule() == 1) { ProxyFooter proxyFooter =
	 * produceFooter(listLRE.get(z) .getEmployee().getHumanface().getFamily());
	 * listProxyFooter.add(proxyFooter); } } GroupProxySection proxySection =
	 * null; if (listProxyStudent.size() != 0) { if
	 * (listOrderRule.get(j).getDescription() != null) { proxySection =
	 * produceSection(listOrderRule.get(j) .getDescription(), listProxyStudent,
	 * listProxyFooter); } else { proxySection =
	 * produceSection(listOrderRule.get(j) .getDescription(), listProxyStudent,
	 * listProxyFooter); }
	 * 
	 * if (((OrderRule) CtrOrder.ord.getOrderRules().toArray()[j])
	 * .getSection().isPrint()) { listProxySection.add(proxySection); } } }
	 * 
	 * if (listProxySection.size() != 0) {
	 * listProxy.add(produce(listGrS.get(i).getGroup().getName(),
	 * listProxySection, listProxyTitle)); } }
	 * 
	 * return listProxy; }
	 */

	public List<OrderRule> divOrderRule(List<OrderRule> listOrderRule) {
		List<OrderRule> listOR = new ArrayList<OrderRule>();
		for (int i = 0; i < listOrderRule.size(); ++i) {
			// if(!(listOrderRule.get(i).getSection().getName().equals("Отказать"))){
			if (listOrderRule.get(i).getDescription().contains("$date1$")|| listOrderRule.get(i).getDescription().contains("$date2$")) {
				List<OrderRule> dopOR = divideOrderRuleByDate(listOrderRule
						.get(i));
				listOR.addAll(dopOR);
			} else {
				listOR.add(listOrderRule.get(i));
			}
			// }
			// else{
			// listOR.add(listOrderRule.get(i));
			// }
		}
		return listOR;
	}

	public List<OrderRule> divideOrderRuleByDate(OrderRule orderRule) {
		List<OrderRule> listOR = new ArrayList<OrderRule>();
		List<StudentSemesterStatus> listSSS = new ArrayList<StudentSemesterStatus>(
				orderRule.getStudents());
		// пристствуют дата окончания, нужна дата окончания
		if ((orderRule.getDescription().contains("$date1$"))&& (!(orderRule.getDescription().contains("$date2$")))) {
			for (int i = 0; i < listSSS.size(); ++i) {
				boolean addInSection = false;
				for (int j = 0; j < listOR.size(); ++j) {
					if(listSSS.get(i).getDateOfScholarshipBegin()!=null){
						if (listSSS.get(i).getDateOfScholarshipBegin().equals(((StudentSemesterStatus) listOR.get(j).getStudents().toArray()[0]).getDateOfScholarshipBegin())) {
							listOR.get(j).getStudents().add(listSSS.get(i));
							addInSection = true;
						}
					}else{
						System.out.println("Не указана дата начала выплат");
						listOR.get(j).getStudents().add(listSSS.get(i));
						addInSection = true;
						resultStatus=1;
					}
				}
				if (!addInSection) {
					System.out.println();
					OrderRule or = new OrderRule();
					if(listSSS.get(i).getDateOfScholarshipBegin()!=null){
						or.setDescription(orderRule.getDescription().replace("$date1$",new SimpleDateFormat("dd.MM.yyyy").format(listSSS.get(i).getDateOfScholarshipBegin())));
					}else{
						or.setDescription(orderRule.getDescription());
						System.out.println("Не указана дата начала выплат");
						resultStatus=1;
					}
					or.setId(orderRule.getId());
					or.setSection(orderRule.getSection());
					or.getStudents().add(listSSS.get(i));
					or.setPrint(orderRule.isPrint());
					listOR.add(or);
				}
			}
		}
		// присутствует только дата начала, нужна дата окончания
		if (!orderRule.getDescription().contains("$date1$") && orderRule.getDescription().contains("$date2$")) {
			for (int i = 0; i < listSSS.size(); ++i) {
				boolean addInSection = false;
				for (int j = 0; j < listOR.size(); ++j) {
					if(listSSS.get(i).getDateOfScholarshipEnd()!=null){
						if (listSSS.get(i).getDateOfScholarshipEnd().equals(((StudentSemesterStatus) listOR.get(j).getStudents().toArray()[0]).getDateOfScholarshipEnd())) {
							listOR.get(j).getStudents().add(listSSS.get(i));
							addInSection = true;
						}
					}else{
						System.out.println("Не указана дата окончания выплат");
						listOR.get(j).getStudents().add(listSSS.get(i));
						addInSection = true;
						resultStatus=1;
					}
				}
				if (!addInSection) {
					System.out.println();
					OrderRule or = new OrderRule();
					if(listSSS.get(i).getDateOfScholarshipEnd()!=null){
						or.setDescription(orderRule.getDescription().replace("$date2$",new SimpleDateFormat("dd.MM.yyyy").format(listSSS.get(i).getDateOfScholarshipEnd())));
					}else{
						or.setDescription(orderRule.getDescription());
						System.out.println("Не указана дата окончания выплат");
						resultStatus=1;
					}
					or.setId(orderRule.getId());
					or.setSection(orderRule.getSection());
					or.getStudents().add(listSSS.get(i));
					or.setPrint(orderRule.isPrint());
					listOR.add(or);
				}
			}
		}
		// Присутствуют обе даты
		if (orderRule.getDescription().contains("$date1$")&& orderRule.getDescription().contains("$date2$")) {
			for (int i = 0; i < listSSS.size(); ++i) {
				boolean addInSection = false;
				for (int j = 0; j < listOR.size(); ++j) {
					System.out.println("End: "+ listSSS.get(i).getDateOfScholarshipEnd()+ "//begin: "+ listSSS.get(i).getDateOfScholarshipBegin());
					if(listSSS.get(i).getDateOfScholarshipEnd()!=null && listSSS.get(i).getDateOfScholarshipBegin()!=null){
						if (listSSS.get(i).getDateOfScholarshipEnd().equals(((StudentSemesterStatus) listOR.get(j).getStudents().toArray()[0]).getDateOfScholarshipEnd()) 
								&& listSSS.get(i).getDateOfScholarshipBegin().equals(((StudentSemesterStatus) listOR.get(j).getStudents().toArray()[0]).getDateOfScholarshipBegin())){
							listOR.get(j).getStudents().add(listSSS.get(i));
							addInSection = true;
						}
					}else{
						System.out.println("Не указаны даты выплат");
						listOR.get(j).getStudents().add(listSSS.get(i));
						addInSection = true;
						resultStatus=1;
					}
				}
				if (!addInSection) {
					System.out.println();
					OrderRule or = new OrderRule();
					System.out.println("__== "+ listSSS.get(i).getDateOfScholarshipBegin()+ "///get(i)" + listSSS.get(i));

					if(listSSS.get(i).getDateOfScholarshipBegin()!=null){
						or.setDescription(orderRule.getDescription().replace("$date1$",new SimpleDateFormat("dd.MM.yyyy").format(listSSS.get(i).getDateOfScholarshipBegin())));
					}else{
						System.out.println("Не указана дата начала выплат");
						or.setDescription(orderRule.getDescription());
						resultStatus=1;
					}

					if(listSSS.get(i).getDateOfScholarshipEnd()!=null){
						or.setDescription(or.getDescription().replace("$date2$",new SimpleDateFormat("dd.MM.yyyy").format(listSSS.get(i).getDateOfScholarshipEnd())));
					}else{
						System.out.println("Не указана дата окончание выплат");
						or.setDescription(orderRule.getDescription());
						resultStatus=1;
					}

					or.setId(orderRule.getId());
					or.setSection(orderRule.getSection());
					or.getStudents().add(listSSS.get(i));
					or.setPrint(orderRule.isPrint());
					listOR.add(or);
				}
			}
		}
		return listOR;
	}

	public GroupScholarshipOrder produce(String name, List<GroupScholarshipSection> listSection,
			List<GroupScholarshipTitle> listProxyTitle) {
		GroupScholarshipOrder proxy = new GroupScholarshipOrder();
		proxy.setName(name);
		proxy.setListSection(listSection);
		proxy.setListProxyTitle(listProxyTitle);
		return proxy;
	}

	public GroupScholarshipTitle produceTitle(String title) {
		GroupScholarshipTitle proxyTitle = new GroupScholarshipTitle();
		proxyTitle.setTitle(title);

		return proxyTitle;
	}

	public ScholarshipFooter produceFooter(String name) {
		ScholarshipFooter proxyFooter = new ScholarshipFooter();
		proxyFooter.setName(name);

		return proxyFooter;
	}

	public ScholarshipStudent produceStudent(String name, String family,
			String patronymic, String recordBook) {
		ScholarshipStudent proxyStudent = new ScholarshipStudent();

		proxyStudent.setFamily(family);
		proxyStudent.setName(name);
		proxyStudent.setPatronymic(patronymic);
		proxyStudent.setRecordBook(recordBook);
		return proxyStudent;
	}

	public GroupScholarshipSection produceSection(String description,
			List<GroupScholarshipSubSection> listGroupProxySubSection,
			List<ScholarshipFooter> listProxyFooter) {
		GroupScholarshipSection proxySection = new GroupScholarshipSection();

		proxySection.setDescription(description);
		proxySection.setListGroupProxySubSection(listGroupProxySubSection);
		proxySection.setListProxyFooter(listProxyFooter);
		return proxySection;
	}
}
