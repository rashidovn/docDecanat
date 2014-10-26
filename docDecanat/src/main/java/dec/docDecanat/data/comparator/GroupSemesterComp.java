package dec.docDecanat.data.comparator;

import java.util.Comparator;
import dec.docDecanat.data.entity.GroupSemester;


//Класс для сравнения групп-семестров по всевозможным полям и сортировки
public class GroupSemesterComp implements Comparator<GroupSemester> {

	//Флаги для сравнения
	static public enum GSCompareMethods {
		BY_GR_NAME,
		BY_ID,
		BY_SEMESTER,
		BY_GR_NAME_REV,
		BY_ID_REV,
		BY_SEMESTER_REV;
	}
	
	private GSCompareMethods compareMethod;

	public void setCompareMethod(GSCompareMethods method) {
		compareMethod = method;
	}
	
	public int compare(GroupSemester o1, GroupSemester o2) {
		if(o1!=null && o2!=null){
			switch (compareMethod) {
			case BY_GR_NAME:
				if(o1.getGroup()!=null && o2.getGroup()!=null)
					return o1.getGroup().getName().compareTo(o2.getGroup().getName());
				break;
			case BY_GR_NAME_REV:
				if(o1.getGroup()!=null && o2.getGroup()!=null)
					return o2.getGroup().getName().compareTo(o1.getGroup().getName());
				break;
			case BY_ID: 
				if(o1.getId()!=null && o2.getId()!=null)
					return o1.getId().compareTo(o2.getId());
				break;
			case BY_ID_REV: 
				if(o1.getId()!=null && o2.getId()!=null)
					return o2.getId().compareTo(o1.getId());
				break;
			case BY_SEMESTER:
				if(o1.getSemester()!=null && o2.getSemester()!=null)
					return o1.getSemester().getId().compareTo(o2.getSemester().getId());
				break;
			case BY_SEMESTER_REV:
				if(o1.getSemester()!=null && o2.getSemester()!=null)
					return o2.getSemester().getId().compareTo(o1.getSemester().getId());
				break;
			}
		}else{
			return 0;
		}
		//Если приоритетное сравнение вернуло 0 - сравниваем по умолчанию
		return o1.compareTo(o2);
	}    
}