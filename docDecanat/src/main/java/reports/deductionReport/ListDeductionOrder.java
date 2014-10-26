package reports.deductionReport;

import java.util.ArrayList;
import java.util.List;

import dec.docDecanat.data.entity.Humanface;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.data.entity.StudentSemesterStatus;
import dec.docDecanat.pageControllers.CtrOrder;

public class ListDeductionOrder {
	public List<DeductionOrder> getAllData(){
		List<DeductionOrder> listExOr = new ArrayList<DeductionOrder>();
		List<OrderRule> listOR = new ArrayList<OrderRule>(CtrOrder.ord.getOrderRules());
		List<StudentSemesterStatus> listSSS = new ArrayList<StudentSemesterStatus>(listOR.get(0).getStudents());

		for(int i = 0; i < listSSS.size(); ++i){
			StudentSemesterStatus sss = listSSS.get(i);
			Humanface hf = listSSS.get(i).getStudentCard().getHumanface();
			DeductionOrder exOr = new DeductionOrder();
			//TODO: статик заменить на динамик 
			String foundation = "представление директора, учетная карточка";
			String reason = "Об отчислении";
			
			String formOfStudy = null;
			if(sss.getFormOfStudy() == 1){
				formOfStudy = "очная форма обучения";
			}
			else if(sss.getFormOfStudy() == 2){
				formOfStudy = "заочная форма обучения";
			}else{
				formOfStudy = "вечерняя форма обучения";
			}
			
			String financyFormOfStudy = null;
			if(sss.getGovernmentFinanced()){
				financyFormOfStudy = "за счет бюджетных ассигнований федерального бюджета";
			}else if(sss.getTrustagreement()){
				financyFormOfStudy = "на условиях договора по целевому обучению";
			}else{
				financyFormOfStudy = "на условиях договора об оказании платных образовательных услуг";
			}
			
			if(listSSS.size() != 0){
				exOr = produceOrder(hf.getName(), hf.getFamily(), hf.getPatronymic(), sss.getStudentCard().getRecordBook(),
						sss.getGroupSemester().getCourse(),sss.getGroupSemester().getGroup().getName(),
							sss.getGroupSemester().getGroup().getCurriculum().getDirectioncode(),
								"\"" + sss.getGroupSemester().getGroup().getCurriculum().getSpecialitytitle() + "\"", 
									formOfStudy, financyFormOfStudy, foundation, reason);
			}else{
				exOr = produceOrder("", "", "", "", null, "", "", "", "", "", "", "");
			}
			listExOr.add(exOr);
		}

		return listExOr;
	}
	
	public DeductionOrder produceOrder(String firstName, String lastName, String patronymicName, String recordBook, Integer course, 
				String groupName, String directioncode, String specialitytitle, String formOfStudy, String financyFormOfStudy,
					String foundation, String reason){
		DeductionOrder exOr = new DeductionOrder();
		exOr.setFirstName(firstName);
		exOr.setLastName(lastName);
		exOr.setRecordBook(recordBook);
		exOr.setPatronymicName(patronymicName);
		exOr.setGroupName(groupName);
		exOr.setCourse(course);
		exOr.setDirectioncode(directioncode);
		exOr.setSpecialitytitle(specialitytitle);
		exOr.setFormOfStudy(formOfStudy);
		exOr.setFinancyFormOfStudy(financyFormOfStudy);
		exOr.setFoundation(foundation);
		exOr.setReason(reason);
		return exOr;
	}
}
