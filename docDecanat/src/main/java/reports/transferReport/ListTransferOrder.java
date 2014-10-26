package reports.transferReport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.data.entity.StudentSemesterStatus;
import dec.docDecanat.pageControllers.CtrOrder;

public class ListTransferOrder {

	public int resultStatus=0;
	
	public List<TransferOrder> getAllData() {
		List<TransferOrder> listTransferOrder = new ArrayList<TransferOrder>();
		List<OrderRule> listOrderRule = new ArrayList<OrderRule>(
				CtrOrder.ord.getOrderRules());
		for(int z = 0; z < listOrderRule.size(); ++ z){
			listOrderRule.get(z).getSection().setSecParametersTransfer(listOrderRule.get(z).getSection().parseParametersTransfer());
			if(listOrderRule.get(z).getDescription() == null){
				listOrderRule.get(z).setDescription(listOrderRule.get(z).getSection().getDescription());
			}else{
				if(listOrderRule.get(z).getDescription().equals("")){
					listOrderRule.get(z).setDescription(listOrderRule.get(z).getSection().getDescription());
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
			List<TransferCourse> listTransferCourse = new ArrayList<TransferCourse>();
			if (stlistInSection.size() != 0) {
				List<GroupSemester> listGrS = CtrOrder
						.sectionStudentForGroup(stlistInSection);
				listTransferCourse = getListTransferCourse(listGrS);
				for (int j = 0; j < listTransferCourse.size(); ++j) {
					List<TransferGroup> listTransferGroup = new ArrayList<TransferGroup>();
					for (int k = 0; k < listTransferCourse.get(j)
							.getListGroupSemester().size(); ++k) {
						List<TransferStudent> listTransferStudent = new ArrayList<TransferStudent>();
						List<StudentSemesterStatus> listSSS = new ArrayList<StudentSemesterStatus>(
								listTransferCourse.get(j)
								.getListGroupSemester().get(k)
								.getStudents());
						for (int l = 0; l < listSSS.size(); ++l) {
							StudentSemesterStatus sss = listSSS.get(l);
							listTransferStudent.add(produceStudent(sss
									.getStudentCard().getHumanface()
									.getFamily(), sss.getStudentCard()
									.getHumanface().getName(), sss
									.getStudentCard().getHumanface()
									.getPatronymic(), sss.getStudentCard()
									.getRecordBook()));
						}
						Collections.sort(listTransferStudent);
						listTransferGroup.add(produceGroup(listTransferCourse
								.get(j).getListGroupSemester().get(k)
								.getGroup().getName(), listTransferStudent));
					}
					listTransferCourse.get(j).setListTransferGroup(
							listTransferGroup);
					if (listOrderRule.get(i).getFoundation() != null) {
						if(!listOrderRule.get(i).getFoundation().equals("")){
							listTransferCourse.get(j).setFoundation(
									"Основания: " + listOrderRule.get(i).getFoundation());
						}
						else{
							listTransferCourse.get(j).setFoundation(" ");
						}	
					} else {
						listTransferCourse.get(j).setFoundation(" ");
					}
				}

			}
			//			if (listOrderRule.get(i).getSection().isPrint()) {
			if (listOrderRule.get(i).isPrint()==null)
				listOrderRule.get(i).setPrint(listOrderRule.get(i).getSection().isPrint());
			if (listOrderRule.get(i).isPrint()) {
				if (CtrOrder.ord.getDescription() == null) {
					if (listOrderRule.get(i).getStudents().size() != 0) {
						listTransferOrder.add(produceOrder(" ", listOrderRule
								.get(i).getDescription(),
								listTransferCourse));
						System.out.println(">>   "
								+ CtrOrder.ord.getDescription());
					}
				} else {
					if (listOrderRule.get(i).getStudents().size() != 0)
						listTransferOrder.add(produceOrder(CtrOrder.ord
								.getDescription(), listOrderRule.get(i)
								.getDescription(), listTransferCourse));
				}
			}
		}

		return listTransferOrder;
	}

	public List<OrderRule> divOrderRule(List<OrderRule> listOrderRule) {
		List<OrderRule> listOR = new ArrayList<OrderRule>();
		for (int i = 0; i < listOrderRule.size(); ++i) {
			System.out.println("getSecParam" + listOrderRule.get(i).getSection().getSecParametersTransfer());
			if (listOrderRule.get(i).getSection().getSecParametersTransfer() != null) {
				if (listOrderRule.get(i).getSection()
						.getSecParametersTransfer().isProl()) {
					System.out.println("convert date1 "+  listOrderRule.get(i).getDescription().contains("$date$"));
					if (listOrderRule.get(i).getDescription().contains("$date$")) {
						List<OrderRule> dopOR = divideOrderRuleByDate(listOrderRule.get(i));
						listOR.addAll(dopOR);
					} else {
						listOR.add(listOrderRule.get(i));
					}
				} else {
					listOR.add(listOrderRule.get(i));
				}
			} else {
				listOR.add(listOrderRule.get(i));
			}
		}
		return listOR;
	}

	public List<OrderRule> divideOrderRuleByDate(OrderRule orderRule) {
		List<OrderRule> listOR = new ArrayList<OrderRule>();
		List<StudentSemesterStatus> listSSS = new ArrayList<StudentSemesterStatus>(
				orderRule.getStudents());
		for (int i = 0; i < listSSS.size(); ++i) {
			boolean addInSection = false;
			for (int j = 0; j < listOR.size(); ++j) {
				if(listSSS.get(i).getProlongationbegindate()!=null){
					if (listSSS.get(i).getProlongationbegindate().equals(((StudentSemesterStatus) listOR.get(j).getStudents().toArray()[0]).getProlongationbegindate())) {
						listOR.get(j).getStudents().add(listSSS.get(i));
						addInSection = true;
					}
				}else{
					System.out.println("Не указана дата продления");
					listOR.get(j).getStudents().add(listSSS.get(i));
					addInSection = true;
					resultStatus=1;
				}
			}
			if (!addInSection) {
				System.out.println();
				OrderRule or = new OrderRule();
				if(listSSS.get(i).getProlongationbegindate()!=null){
					or.setDescription(orderRule.getDescription().replace("$date$", new SimpleDateFormat("dd.MM.yyyy").format(listSSS.get(i).getProlongationbegindate())));
				}else{
					System.out.println("Не указана дата продления");
					or.setDescription(orderRule.getDescription());
				}
				or.setId(orderRule.getId());
				or.setSection(orderRule.getSection());
				or.getStudents().add(listSSS.get(i));
				listOR.add(or);
			}
		}
		if(listOR.size()>0)
			listOR.get(listOR.size()-1).setFoundation(orderRule.getFoundation());
		return listOR;
	}

	public List<TransferCourse> getListTransferCourse(
			List<GroupSemester> listGroupSemester) {
		List<TransferCourse> listTransferCourse = new ArrayList<TransferCourse>();

		for (int i = 0; i < listGroupSemester.size(); i++) {
			boolean addInCourse = false;
			for (int j = 0; j < listTransferCourse.size(); j++) {
				if (listGroupSemester.get(i).getCourse() == listTransferCourse
						.get(j).getCourse()) {
					listTransferCourse.get(j).getListGroupSemester()
					.add(listGroupSemester.get(i));
					addInCourse = true;
				}
			}

			if (!addInCourse) {
				TransferCourse transferCourse = new TransferCourse();
				System.out.println("course"
						+ listGroupSemester.get(i).getCourse());
				transferCourse
				.setCourse(listGroupSemester.get(i).getCourse() + 1);
				transferCourse.getListGroupSemester().add(
						listGroupSemester.get(i));
				listTransferCourse.add(transferCourse);
			}
		}
		Collections.sort(listTransferCourse);

		return listTransferCourse;
	}

	public TransferOrder produceOrder(String descriptionTitle,
			String description, List<TransferCourse> listTransferCourse) {
		TransferOrder transferOrder = new TransferOrder();
		transferOrder.setDescription(description);
		transferOrder.setDescriptionTitle(descriptionTitle);
		transferOrder.setListTransferCourse(listTransferCourse);
		return transferOrder;
	}

	public TransferCourse produceCourse(String foundation, int course,
			List<TransferGroup> listTransferGroup) {
		TransferCourse transferCourse = new TransferCourse();
		transferCourse.setCourse(course);
		transferCourse.setListTransferGroup(listTransferGroup);
		transferCourse.setFoundation(foundation);
		return transferCourse;
	}

	public TransferGroup produceGroup(String groupName,
			List<TransferStudent> listTransferStudent) {
		TransferGroup transferGroup = new TransferGroup();
		transferGroup.setGroupName(groupName);
		transferGroup.setListTransferStudent(listTransferStudent);
		return transferGroup;
	}

	public TransferStudent produceStudent(String family, String name,
			String patronymic, String recordBook) {
		TransferStudent transferStudent = new TransferStudent();
		transferStudent.setFamily(family);
		transferStudent.setName(name);
		transferStudent.setPatronymic(patronymic);
		transferStudent.setRecordBook(recordBook);
		return transferStudent;
	}
}
