package dec.docDecanat.data.comparator;

import java.util.Comparator;
import dec.docDecanat.data.entity.StudentSemesterStatus;


public class StudentSemesterStatusComp implements Comparator<StudentSemesterStatus> {

	//Флаги для сравнения
	static public enum SSSCompareMethods {
		BY_NAME,
		BY_ID,
		BY_GR,
		BY_NAME_REV,
		BY_ID_REV,
		BY_GR_REV;
	}
	
	private SSSCompareMethods compareMethod;

	public void setCompareMethod(SSSCompareMethods method) {
		compareMethod = method;
	}
	
	public int compare(StudentSemesterStatus o1, StudentSemesterStatus o2) {
		if(o1!=null && o2!=null){
			switch (compareMethod) {
			case BY_NAME:
				if((o1.getStudentCard() != null)&&(o2.getStudentCard() != null)){
					if((o1.getStudentCard().getHumanface() != null)&&(o2.getStudentCard().getHumanface() != null)){
						return o1.getStudentCard().getHumanface().getFamily().compareTo(o2.getStudentCard().getHumanface().getFamily());
					}
				}
				break;
			case BY_NAME_REV:
				if((o2.getStudentCard() != null)&&(o1.getStudentCard() != null)){
					if((o2.getStudentCard().getHumanface() != null)&&(o1.getStudentCard().getHumanface() != null)){
						return o2.getStudentCard().getHumanface().getFamily().compareTo(o1.getStudentCard().getHumanface().getFamily());
					}
				}
				break;
			case BY_ID: 
				if(o1.getId()!=null && o2.getId()!=null)
					return o1.getId().compareTo(o2.getId());
				break;
			case BY_ID_REV: 
				if(o1.getId()!=null && o2.getId()!=null)
					return o2.getId().compareTo(o1.getId());
				break;
			case BY_GR:
				if(o1.getGroupSemester()!=null && o2.getGroupSemester()!=null){
					if(o1.getGroupSemester().getGroup()!=null && o2.getGroupSemester().getGroup()!=null){
						return o1.getGroupSemester().getGroup().getName().compareTo(o2.getGroupSemester().getGroup().getName());
					}
				}
				break;
			case BY_GR_REV:
				if(o2.getGroupSemester()!=null && o1.getGroupSemester()!=null){
					if(o2.getGroupSemester().getGroup()!=null && o1.getGroupSemester().getGroup()!=null){
						return o2.getGroupSemester().getGroup().getName().compareTo(o1.getGroupSemester().getGroup().getName());
					}
				}
				break;
			}
		}else{
			return 0;
		}
		//Если приоритетное сравнение вернуло 0 - сравниваем по умолчанию
		return o1.compareTo(o2);
	}    
}
