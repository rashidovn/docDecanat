package reports.scholarshipReport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import reports.transferReport.TransferCourse;
import dec.docDecanat.data.entity.Group;
import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.Humanface;
import dec.docDecanat.data.entity.Link_rule_employee;
import dec.docDecanat.data.entity.Section;
import dec.docDecanat.data.entity.StudentSemesterStatus;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.pageControllers.CtrOrder;

public class SectionScholarshipList {
	public int resultStatus=0;
	
	public List<SectionScholarshipOrder> getAllData() {
		List<SectionScholarshipOrder> listProxy = new ArrayList<SectionScholarshipOrder>();
		List<OrderRule> listOrderRule = new ArrayList<OrderRule>(
				CtrOrder.ord.getOrderRules());

		for (int z = 0; z < listOrderRule.size(); ++z) {
			if (listOrderRule.get(z).getDescription() == null) {
				listOrderRule.get(z).setDescription(
						listOrderRule.get(z).getSection().getDescription());
			} else {
				if (listOrderRule.get(z).getDescription().equals("")) {
					listOrderRule.get(z).setDescription(
							listOrderRule.get(z).getSection().getDescription());
				}
			}

			if (listOrderRule.get(z).getFoundation() == null) {
				listOrderRule.get(z).setFoundation(
						listOrderRule.get(z).getSection().getFoundation());
			} else {
				if (listOrderRule.get(z).getFoundation().equals("")) {
					listOrderRule.get(z).setFoundation(
							listOrderRule.get(z).getSection().getFoundation());
				}
			}
		}
		listOrderRule = divOrderRule(listOrderRule);

		Collections.sort(listOrderRule);
		for (int i = 0; i < listOrderRule.size(); ++i) {
			List<StudentSemesterStatus> stlistInSection = new ArrayList<StudentSemesterStatus>();
			stlistInSection.addAll(new ArrayList<StudentSemesterStatus>(
					listOrderRule.get(i).getStudents()));
			List<SectionScholarshipCourse> listProxyCourse = new ArrayList<SectionScholarshipCourse>();
			if (stlistInSection.size() != 0) {
				List<GroupSemester> listGrS = CtrOrder
						.sectionStudentForGroup(stlistInSection);
				listProxyCourse = getListSectionProxyCourse(listGrS);
				for (int j = 0; j < listProxyCourse.size(); ++j) {
					List<SectionScholarshipGroup> listProxyGroup = new ArrayList<SectionScholarshipGroup>();
					for (int k = 0; k < listProxyCourse.get(j)
							.getListGroupSemester().size(); ++k) {
						List<ScholarshipStudent> listProxyStudent = new ArrayList<ScholarshipStudent>();
						List<StudentSemesterStatus> listSSS = new ArrayList<StudentSemesterStatus>(
								listProxyCourse.get(j).getListGroupSemester()
								.get(k).getStudents());
						for (int l = 0; l < listSSS.size(); ++l) {
							StudentSemesterStatus sss = listSSS.get(l);
							listProxyStudent.add(produceStudent(sss
									.getStudentCard().getHumanface().getName(),
									sss.getStudentCard().getHumanface()
									.getFamily(), sss.getStudentCard()
									.getHumanface().getPatronymic(),
									sss.getStudentCard().getRecordBook()));
						}
						Collections.sort(listProxyStudent);
						listProxyGroup.add(produceGroup(listProxyCourse.get(j)
								.getListGroupSemester().get(k).getGroup()
								.getName(), listProxyStudent));
					}
					listProxyCourse.get(j).setListProxyGroup(listProxyGroup);
					if (listOrderRule.get(i).getFoundation() != null) {
						if (!(listOrderRule.get(i).getFoundation()
								.equals(""))) {
							listProxyCourse.get(j).setFoundation(
									"Основания: "
											+ listOrderRule.get(i).getFoundation());
						} else {
							listProxyCourse.get(j).setFoundation(" ");
						}
					} else {
						listProxyCourse.get(j).setFoundation(" ");
					}
					//					System.out.println("foundation из секции "+ i + listOrderRule.get(i).getSection().getFoundation());
					//					System.out.println("foundation из OR "+ i + listOrderRule.get(i).getFoundation());

				}
			}
			//			if (listOrderRule.get(i).getSection().isPrint()) {
			if(listOrderRule.get(i).isPrint()==null)
				listOrderRule.get(i).setPrint(listOrderRule.get(i).getSection().isPrint());
			if (listOrderRule.get(i).isPrint()) {
				if (listOrderRule.get(i).getSection().getFoundation() != null) {
					if (listOrderRule.get(i).getStudents().size() != 0) {
						if (listOrderRule.get(i).getDescription() != null) {
							listProxy.add(produce(listOrderRule.get(i)
									.getDescription(), CtrOrder.ord
									.getDescription(), listProxyCourse, null));
						} else {
							listProxy.add(produce(listOrderRule.get(i)
									.getSection().getDescription(),
									CtrOrder.ord.getDescription(),
									listProxyCourse, null));
						}
					}
				}
			}
		}
		return listProxy;
	}

	public List<OrderRule> divOrderRule(List<OrderRule> listOrderRule) {
		List<OrderRule> listOR = new ArrayList<OrderRule>();
		for (int i = 0; i < listOrderRule.size(); ++i) {
			//			if (!(listOrderRule.get(i).getSection().getName().equals("Отказать"))) {
			if (listOrderRule.get(i).getDescription().contains("$date1$")
					|| listOrderRule.get(i).getDescription()
					.contains("$date2$")) {
				List<OrderRule> dopOR = divideOrderRuleByDate(listOrderRule
						.get(i));
				listOR.addAll(dopOR);
			} else {
				listOR.add(listOrderRule.get(i));
			}
			//			} else {
			//				listOR.add(listOrderRule.get(i));
			//			}
		}
		return listOR;
	}

	public List<OrderRule> divideOrderRuleByDate(OrderRule orderRule) {
		List<OrderRule> listOR = new ArrayList<OrderRule>();
		List<StudentSemesterStatus> listSSS = new ArrayList<StudentSemesterStatus>(
				orderRule.getStudents());
		// пристствуют дата окончания, нужна дата окончания
		if ((orderRule.getDescription().contains("$date1$"))
				&& (!(orderRule.getDescription().contains("$date2$")))) {
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
					OrderRule or = new OrderRule();
					if(listSSS.get(i).getDateOfScholarshipBegin()!=null){
						or.setDescription(orderRule.getDescription().replace("$date1$",	new SimpleDateFormat("dd.MM.yyyy").format(listSSS.get(i).getDateOfScholarshipBegin())));
					}else{
						System.out.println("Не указана дата начала выплат");
						or.setDescription(orderRule.getDescription());
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
		if (!orderRule.getDescription().contains("$date1$")
				&& orderRule.getDescription().contains("$date2$")) {
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
						System.out.println("Не указана дата окончания выплат");
						or.setDescription(orderRule.getDescription());
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
		if (orderRule.getDescription().contains("$date1$") && orderRule.getDescription().contains("$date2$")) {
			for (int i = 0; i < listSSS.size(); ++i) {
				boolean addInSection = false;
				for (int j = 0; j < listOR.size(); ++j) {
					if(listSSS.get(i).getDateOfScholarshipEnd()!=null && listSSS.get(i).getDateOfScholarshipBegin()!=null){
						if (listSSS.get(i).getDateOfScholarshipEnd().equals(((StudentSemesterStatus) listOR.get(j).getStudents().toArray()[0]).getDateOfScholarshipEnd())
								&& listSSS.get(i).getDateOfScholarshipBegin().equals(((StudentSemesterStatus) listOR.get(j).getStudents().toArray()[0]).getDateOfScholarshipBegin())) {
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
					if(listSSS.get(i).getDateOfScholarshipBegin()!=null){
						or.setDescription(orderRule.getDescription().replace("$date1$",new SimpleDateFormat("dd.MM.yyyy").format(listSSS.get(i).getDateOfScholarshipBegin())));
					}else{
						System.out.println("Не указана дата начала выплат");
						or.setDescription(orderRule.getDescription());
					}

					if(listSSS.get(i).getDateOfScholarshipEnd()!=null){
						or.setDescription(or.getDescription().replace("$date2$",new SimpleDateFormat("dd.MM.yyyy").format(listSSS.get(i).getDateOfScholarshipEnd())));
					}else{
						System.out.println("Не указана дата окончания выплат");
						or.setDescription(orderRule.getDescription());
					}
					or.setId(orderRule.getId());
					or.setSection(orderRule.getSection());
					or.getStudents().add(listSSS.get(i));
					or.setPrint(orderRule.isPrint());
					listOR.add(or);
				}
			}
		}
		if(listOR.size()>0)
			listOR.get(listOR.size()-1).setFoundation(orderRule.getFoundation());
		return listOR;
	}

	public List<SectionScholarshipCourse> getListSectionProxyCourse(
			List<GroupSemester> listGroupSemester) {
		List<SectionScholarshipCourse> listProxyCourse = new ArrayList<SectionScholarshipCourse>();

		for (int i = 0; i < listGroupSemester.size(); i++) {
			boolean addInCourse = false;
			for (int j = 0; j < listProxyCourse.size(); j++) {
				if (listGroupSemester.get(i).getCourse() == listProxyCourse
						.get(j).getCourse()) {
					listProxyCourse.get(j).getListGroupSemester()
					.add(listGroupSemester.get(i));
					addInCourse = true;
				}
			}

			if (!addInCourse) {
				SectionScholarshipCourse sectionProxyCourse = new SectionScholarshipCourse();
				System.out.println("course"
						+ listGroupSemester.get(i).getCourse());
				sectionProxyCourse.setCourse(listGroupSemester.get(i)
						.getCourse());
				sectionProxyCourse.getListGroupSemester().add(
						listGroupSemester.get(i));
				listProxyCourse.add(sectionProxyCourse);
			}
		}
		Collections.sort(listProxyCourse);

		return listProxyCourse;
	}

	public SectionScholarshipOrder produce(String description, String titleDescription,
			List<SectionScholarshipCourse> listProxyCourse,
			List<ScholarshipFooter> listProxyFooter) {
		SectionScholarshipOrder proxy = new SectionScholarshipOrder();
		proxy.setDescription(description);
		proxy.setListProxyCourse(listProxyCourse);
		proxy.setListProxyFooter(listProxyFooter);
		proxy.setTitleDescription(titleDescription);
		return proxy;
	}

	public SectionScholarshipCourse produceCourse(String foundation, Integer course,
			List<SectionScholarshipGroup> listProxyGroup) {
		SectionScholarshipCourse proxyCourse = new SectionScholarshipCourse();
		proxyCourse.setCourse(course);
		proxyCourse.setListProxyGroup(listProxyGroup);
		proxyCourse.setFoundation(foundation);
		return proxyCourse;
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

	public SectionScholarshipGroup produceGroup(String name,
			List<ScholarshipStudent> listHumanface) {
		SectionScholarshipGroup proxyGroup = new SectionScholarshipGroup();

		proxyGroup.setName(name);
		proxyGroup.setListHumanface(listHumanface);
		return proxyGroup;

	}
}