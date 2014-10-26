import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.io.FileWriter;

import org.zkoss.json.JSONObject;
import org.zkoss.util.Dates;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Vlayout;

import dec.docDecanat.data.comparator.GroupSemesterComp;
import dec.docDecanat.data.dao.EmployeeDAO;
import dec.docDecanat.data.dao.GroupDao;
import dec.docDecanat.data.dao.GroupSemesterDao;
import dec.docDecanat.data.dao.HumanfaceDao;
import dec.docDecanat.data.dao.OrderDao;
import dec.docDecanat.data.dao.OrderSubTypeDao;
import dec.docDecanat.data.dao.OrderTypeDao;
import dec.docDecanat.data.dao.SemesterDAO;
import dec.docDecanat.data.dao.StudentCardDao;
import dec.docDecanat.data.dao.StudentSemesterStatusDao;
import dec.docDecanat.data.entity.Employee;
import dec.docDecanat.data.entity.Group;
import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.Humanface;
import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.data.entity.OrderStatusType;
import dec.docDecanat.data.entity.OrderSubType;
import dec.docDecanat.data.entity.OrderType;
import dec.docDecanat.data.entity.Rule;
import dec.docDecanat.data.entity.Section;
import dec.docDecanat.data.entity.Semester;
import dec.docDecanat.data.entity.SessionRating;
import dec.docDecanat.data.entity.StudentCard;
import dec.docDecanat.data.entity.StudentSemesterStatus;
import dec.docDecanat.data.entity.Subject;


public class Main {

	static StudentCardDao scd = new StudentCardDao();
	static List<StudentCard> listSCD = new ArrayList<StudentCard>();

	//1-Стефан, 2-Роланд, 3 - Ричард
	
	public static void main(String[] args) {
		Random rand = new Random();
		for(int i = 1; i < 3; ++i){
			System.out.println("rand = " + rand.nextInt());
		}
		SemesterDAO sd = new SemesterDAO();
		Semester s=null;
		try {
			s = sd.get((long) 35);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GroupSemesterDao gsd=new GroupSemesterDao();
		try {
		List<GroupSemester> gs=	gsd.getAllFromCourse(4,s);
		System.out.println(gs.get(0).getGroup().getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void showEmployee(){

		EmployeeDAO f = new EmployeeDAO();
		Set<Employee> list = new HashSet<Employee>();
		try {
			list = f.getAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < list.size(); i++) {
			System.out.println(((Employee)list.toArray()[i]).getHumanface().getFamily());			
		}
	}

	void createTypeSubType(){	
		try {
			OrderType ot = new OrderType();
			ot.setName("Приказ");
			OrderSubType ost1 = new OrderSubType();
			ost1.setName("Стипендиальный");
			ost1.setOrderType(ot);
			OrderSubType ost2 = new OrderSubType();
			ost2.setName("Переводной");
			ost2.setOrderType(ot);
			OrderTypeDao otd = new OrderTypeDao();
			otd.createOrderType(ot);

			OrderSubTypeDao ostd1 = new OrderSubTypeDao();
			ostd1.create(ost1);
			ostd1.create(ost2);
			System.out.println("Успех");
		} catch (Exception e) {
			System.out.println("Ошибка в методе Main");
			e.printStackTrace();
		}
	}

	//===Функция Глобального создания
	//		dg = Создания фиктивной D_G
	//		sc1=Создаем человека HF_SC
	//		sc2=Создаем человека HF_SC

	//	...
	//    +-Запрос или создание предметов (3-4 на каждый семестр)
	//		lgs1 = Создания фиктивного L_G_S(...,dg)
	//		dg.add(lgs1) //Возможно потребуется сохранение dg еще и тут
	public static void start(){
		Group gr1 = new Group();
		gr1 = createGroup((long) 5643, "АКИ 01 б", new GregorianCalendar(2010, 9, 1), new GregorianCalendar(2015, 9, 1));
		gr1.setGs(createLGS(gr1,2014, 2014, 7,21,1,1,12,18,5,5 ));
		Group gr2 = new Group();
		gr2 = createGroup((long) 5644, "АКИ 01 п", new GregorianCalendar(2010, 9, 1), new GregorianCalendar(2015, 9, 1));
		gr2.setGs(createLGS(gr2,2013, 2014, 24,14,12,1,12,18,5,5));
		try {
			listSCD = new ArrayList<StudentCard>(scd.getAllStudentCard());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("main str152 error: not studentscard");
		}
		List<StudentCard> listStudentGroup1 = new ArrayList<StudentCard>();
		for (int i = 300; i < 305; i++) {
			listStudentGroup1.add(listSCD.get(i));
		}
		List<StudentCard> listStudentGroup2 = new ArrayList<StudentCard>();
		for (int i = 320; i < 325; i++) {
			listStudentGroup2.add(listSCD.get(i));			
		}
		List<StudentSemesterStatus> list_sss1 = new ArrayList<StudentSemesterStatus>();
		List<StudentSemesterStatus> list_sss2 = new ArrayList<StudentSemesterStatus>();

		list_sss1.add(createSSS(listStudentGroup1.get(0), false, (GroupSemester) gr1.getGs().toArray()[0], 1));
		list_sss1.add(createSSS(listStudentGroup1.get(1), false, (GroupSemester) gr1.getGs().toArray()[0], 1));
		list_sss1.add(createSSS(listStudentGroup1.get(2), false, (GroupSemester) gr1.getGs().toArray()[0], -1));
		list_sss1.add(createSSS(listStudentGroup1.get(3), false, (GroupSemester) gr1.getGs().toArray()[0], 1));
		list_sss1.add(createSSS(listStudentGroup1.get(4), false, (GroupSemester) gr1.getGs().toArray()[0], -1));
		
		//отличник
		createSR(list_sss1.get(0), true, 5, false, 0, false, 0, false, 0, false, 0, 9301);
		createSR(list_sss1.get(0), true, 5, false, 0, true, 5, false, 0, false, 0, 9319);
		createSR(list_sss1.get(0), true, 5, false, 0, false, 0, false, 0, false, 0, 9307);
		createSR(list_sss1.get(0), true, 5, false, 0, false, 0, false, 0, false, 0, 9317);
		createSR(list_sss1.get(0), false, 0, true, 1, false, 0, false, 0, false, 0, 9311);
		createSR(list_sss1.get(0), false, 0, true, 1, false, 0, false, 0, false, 0, 9315);
		
		createSR(list_sss1.get(1), true, 4, false, 0, false, 0, false, 0, false, 0, 9301);
		createSR(list_sss1.get(1), true, 4, false, 0, true, 4, false, 0, false, 0, 9319);
		createSR(list_sss1.get(1), true, 4, false, 0, false, 0, false, 0, false, 0, 9307);
		createSR(list_sss1.get(1), true, 4, false, 0, false, 0, false, 0, false, 0, 9317);
		createSR(list_sss1.get(1), false, 0, true, 1, false, 0, false, 0, false, 0, 9311);
		createSR(list_sss1.get(1), false, 0, true, 1, false, 0, false, 0, false, 0, 9315);
		
		createSR(list_sss1.get(2), true, 5, false, 0, false, 0, false, 0, false, 0, 9301);
		createSR(list_sss1.get(2), true, 4, false, 0, true, 4, false, 0, false, 0, 9319);
		createSR(list_sss1.get(2), true, 5, false, 0, false, 0, false, 0, false, 0, 9307);
		createSR(list_sss1.get(2), true, 4, false, 0, false, 0, false, 0, false, 0, 9317);
		createSR(list_sss1.get(2), false, 0, true, 0, false, 0, false, 0, false, 0, 9311);
		createSR(list_sss1.get(2), false, 0, true, 1, false, 0, false, 0, false, 0, 9315);
		
		createSR(list_sss1.get(3), true, 4, false, 0, false, 0, false, 0, false, 0, 9301);
		createSR(list_sss1.get(3), true, 5, false, 0, true, 5, false, 0, false, 0, 9319);
		createSR(list_sss1.get(3), true, 4, false, 0, false, 0, false, 0, false, 0, 9307);
		createSR(list_sss1.get(3), true, 3, false, 0, false, 0, false, 0, false, 0, 9317);
		createSR(list_sss1.get(3), false, 0, true, 1, false, 0, false, 0, false, 0, 9311);
		createSR(list_sss1.get(3), false, 0, true, 1, false, 0, false, 0, false, 0, 9315);
		
		createSR(list_sss1.get(4), true, 5, false, 0, false, 0, false, 0, false, 0, 9301);
		createSR(list_sss1.get(4), true, 4, false, 0, true, 4, false, 0, false, 0, 9319);
		createSR(list_sss1.get(4), true, 0, false, 0, false, 0, false, 0, false, 0, 9307);
		createSR(list_sss1.get(4), true, 4, false, 0, false, 0, false, 0, false, 0, 9317);
		createSR(list_sss1.get(4), false, 0, true, 1, false, 0, false, 0, false, 0, 9311);
		createSR(list_sss1.get(4), false, 0, true, 1, false, 0, false, 0, false, 0, 9315);


		list_sss1.add(createSSS(listStudentGroup1.get(0), false, (GroupSemester) gr1.getGs().toArray()[1], 1));
		list_sss1.add(createSSS(listStudentGroup1.get(1), false, (GroupSemester) gr1.getGs().toArray()[1], 1));
		list_sss1.add(createSSS(listStudentGroup1.get(2), false, (GroupSemester) gr1.getGs().toArray()[1], 1));
		list_sss1.add(createSSS(listStudentGroup1.get(3), false, (GroupSemester) gr1.getGs().toArray()[1], -1));
		list_sss1.add(createSSS(listStudentGroup1.get(4), false, (GroupSemester) gr1.getGs().toArray()[1], -1));
		
		createSR(list_sss1.get(5), true, 4, false, 0, false, 0, false, 0, false, 0, 5956);
		createSR(list_sss1.get(5), true, 4, false, 0, false, 0, false, 0, false, 0, 5957);
		createSR(list_sss1.get(5), true, 4, false, 0, false, 0, true, 4, false, 0, 5953);
		createSR(list_sss1.get(5), true, 4, false, 0, false, 0, false, 0, false, 0, 5955);
		createSR(list_sss1.get(5), false, 0, false, 0, false, 0, false, 0, true, 4, 5954);
		createSR(list_sss1.get(5), false, 0, true, 1, false, 0, false, 0, false, 0, 5958);
		
		createSR(list_sss1.get(6), true, 5, false, 0, false, 0, false, 0, false, 0, 5956);
		createSR(list_sss1.get(6), true, 4, false, 0, false, 0, false, 0, false, 0, 5957);
		createSR(list_sss1.get(6), true, 5, false, 0, false, 0, true, 5, false, 0, 5953);
		createSR(list_sss1.get(6), true, 4, false, 0, false, 0, false, 0, false, 0, 5955);
		createSR(list_sss1.get(6), false, 0, false, 0, false, 0, false, 0, true, 5, 5954);
		createSR(list_sss1.get(6), false, 0, true, 1, false, 0, false, 0, false, 0, 5958);
		
		createSR(list_sss1.get(7), true, 5, false, 0, false, 0, false, 0, false, 0, 5956);
		createSR(list_sss1.get(7), true, 5, false, 0, false, 0, false, 0, false, 0, 5957);
		createSR(list_sss1.get(7), true, 5, false, 0, false, 0, true, 5, false, 0, 5953);
		createSR(list_sss1.get(7), true, 5, false, 0, false, 0, false, 0, false, 0, 5955);
		createSR(list_sss1.get(7), false, 0, false, 0, false, 0, false, 0, true, 5, 5954);
		createSR(list_sss1.get(7), false, 0, true, 1, false, 0, false, 0, false, 0, 5958);
		
		createSR(list_sss1.get(8), true, 4, false, 0, false, 0, false, 0, false, 0, 5956);
		createSR(list_sss1.get(8), true, 4, false, 0, false, 0, false, 0, false, 0, 5957);
		createSR(list_sss1.get(8), true, 3, false, 0, false, 0, true, 4, false, 0, 5953);
		createSR(list_sss1.get(8), true, 5, false, 0, false, 0, false, 0, false, 0, 5955);
		createSR(list_sss1.get(8), false, 0, false, 0, false, 0, false, 0, true, 5, 5954);
		createSR(list_sss1.get(8), false, 0, true, 0, false, 0, false, 0, false, 0, 5958);
		
		createSR(list_sss1.get(9), true, 5, false, 0, false, 0, false, 0, false, 0, 5956);
		createSR(list_sss1.get(9), true, 5, false, 0, false, 0, false, 0, false, 0, 5957);
		createSR(list_sss1.get(9), true, 4, false, 0, false, 0, true, 5, false, 0, 5953);
		createSR(list_sss1.get(9), true, 0, false, 0, false, 0, false, 0, false, 0, 5955);
		createSR(list_sss1.get(9), false, 0, false, 0, false, 0, false, 0, true, 4, 5954);
		createSR(list_sss1.get(9), false, 0, true, 1, false, 0, false, 0, false, 0, 5958);

		for (int i = 0; i < list_sss1.size()-listStudentGroup1.size(); i++) {
			((GroupSemester)gr1.getGs().toArray()[0]).getStudents().add(list_sss1.get(i));			
		}
		for (int i = listStudentGroup1.size(); i < list_sss1.size(); i++) {
			((GroupSemester)gr1.getGs().toArray()[1]).getStudents().add(list_sss1.get(i));			
		}
		
		list_sss2.add(createSSS(listStudentGroup2.get(0), false, (GroupSemester) gr2.getGs().toArray()[0], 1));
		list_sss2.add(createSSS(listStudentGroup2.get(1), false, (GroupSemester) gr2.getGs().toArray()[0], 1));
		list_sss2.add(createSSS(listStudentGroup2.get(2), false, (GroupSemester) gr2.getGs().toArray()[0], -1));
		list_sss2.add(createSSS(listStudentGroup2.get(3), false, (GroupSemester) gr2.getGs().toArray()[0], 1));
		list_sss2.add(createSSS(listStudentGroup2.get(4), false, (GroupSemester) gr2.getGs().toArray()[0], -1));
		
		
		createSR(list_sss2.get(0), true, 5, false, 0, false, 0, false, 0, false, 0, 16333);
		createSR(list_sss2.get(0), true, 5, false, 0, false, 0, false, 0, false, 0, 16335);
		createSR(list_sss2.get(0), true, 5, false, 0, false, 0, false, 0, false, 0, 16331);
		createSR(list_sss2.get(0), true, 5, false, 0, false, 0, false, 0, false, 0, 16336);
		createSR(list_sss2.get(0), false, 0, true, 1, false, 0, false, 0, false, 0, 16316);
		createSR(list_sss2.get(0), false, 0, true, 1, false, 0, false, 0, false, 0, 16340);
		
		createSR(list_sss2.get(1), true, 4, false, 0, false, 0, false, 0, false, 0, 16333);
		createSR(list_sss2.get(1), true, 4, false, 0, false, 0, false, 0, false, 0, 16335);
		createSR(list_sss2.get(1), true, 4, false, 0, false, 0, false, 0, false, 0, 16331);
		createSR(list_sss2.get(1), true, 4, false, 0, false, 0, false, 0, false, 0, 16336);
		createSR(list_sss2.get(1), false, 0, true, 1, false, 0, false, 0, false, 0, 16316);
		createSR(list_sss2.get(1), false, 0, true, 1, false, 0, false, 0, false, 0, 16340);
		
		createSR(list_sss2.get(2), true, 5, false, 0, false, 0, false, 0, false, 0, 16333);
		createSR(list_sss2.get(2), true, 4, false, 0, false, 0, false, 0, false, 0, 16335);
		createSR(list_sss2.get(2), true, 5, false, 0, false, 0, false, 0, false, 0, 16331);
		createSR(list_sss2.get(2), true, 4, false, 0, false, 0, false, 0, false, 0, 16336);
		createSR(list_sss2.get(2), false, 0, true, 0, false, 0, false, 0, false, 0, 16316);
		createSR(list_sss2.get(2), false, 0, true, 1, false, 0, false, 0, false, 0, 16340);
		
		createSR(list_sss2.get(3), true, 5, false, 0, false, 0, false, 0, false, 0, 16333);
		createSR(list_sss2.get(3), true, 4, false, 0, false, 0, false, 0, false, 0, 16335);
		createSR(list_sss2.get(3), true, 3, false, 0, false, 0, false, 0, false, 0, 16331);
		createSR(list_sss2.get(3), true, 5, false, 0, false, 0, false, 0, false, 0, 16336);
		createSR(list_sss2.get(3), false, 0, true, 1, false, 0, false, 0, false, 0, 16316);
		createSR(list_sss2.get(3), false, 0, true, 1, false, 0, false, 0, false, 0, 16340);
		
		createSR(list_sss2.get(4), true, 5, false, 0, false, 0, false, 0, false, 0, 16333);
		createSR(list_sss2.get(4), true, 0, false, 0, false, 0, false, 0, false, 0, 16335);
		createSR(list_sss2.get(4), true, 5, false, 0, false, 0, false, 0, false, 0, 16331);
		createSR(list_sss2.get(4), true, 4, false, 0, false, 0, false, 0, false, 0, 16336);
		createSR(list_sss2.get(4), false, 0, true, 1, false, 0, false, 0, false, 0, 16316);
		createSR(list_sss2.get(4), false, 0, true, 1, false, 0, false, 0, false, 0, 16340);


		list_sss2.add(createSSS(listStudentGroup2.get(0), false, (GroupSemester) gr2.getGs().toArray()[1], 1));
		list_sss2.add(createSSS(listStudentGroup2.get(1), false, (GroupSemester) gr2.getGs().toArray()[1], 1));
		list_sss2.add(createSSS(listStudentGroup2.get(2), false, (GroupSemester) gr2.getGs().toArray()[1], 1));
		list_sss2.add(createSSS(listStudentGroup2.get(3), false, (GroupSemester) gr2.getGs().toArray()[1], 1));
		list_sss2.add(createSSS(listStudentGroup2.get(4), false, (GroupSemester) gr2.getGs().toArray()[1], -1));
		

		createSR(list_sss2.get(5), true, 4, false, 0, false, 0, false, 0, false, 0, 16777);
		createSR(list_sss2.get(5), true, 4, false, 0, false, 0, false, 0, false, 0, 16765);
		createSR(list_sss2.get(5), true, 4, false, 0, false, 0, false, 0, false, 0, 16752);
		createSR(list_sss2.get(5), true, 4, false, 0, false, 0, false, 0, false, 0, 16758);
		createSR(list_sss2.get(5), true, 4, false, 0, false, 0, false, 0, false, 0, 16760);
		createSR(list_sss2.get(5), false, 0, true, 1, false, 0, false, 0, false, 0, 16756);
		
		createSR(list_sss2.get(6), true, 5, false, 0, false, 0, false, 0, false, 0, 16777);
		createSR(list_sss2.get(6), true, 4, false, 0, false, 0, false, 0, false, 0, 16765);
		createSR(list_sss2.get(6), true, 5, false, 0, false, 0, false, 0, false, 0, 16752);
		createSR(list_sss2.get(6), true, 4, false, 0, false, 0, false, 0, false, 0, 16758);
		createSR(list_sss2.get(6), true, 5, false, 0, false, 0, false, 0, false, 0, 16760);
		createSR(list_sss2.get(6), false, 0, true, 1, false, 0, false, 0, false, 0, 16756);
		
		createSR(list_sss2.get(7), true, 5, false, 0, false, 0, false, 0, false, 0, 16777);
		createSR(list_sss2.get(7), true, 5, false, 0, false, 0, false, 0, false, 0, 16765);
		createSR(list_sss2.get(7), true, 5, false, 0, false, 0, false, 0, false, 0, 16752);
		createSR(list_sss2.get(7), true, 5, false, 0, false, 0, false, 0, false, 0, 16758);
		createSR(list_sss2.get(7), true, 5, false, 0, false, 0, false, 0, false, 0, 16760);
		createSR(list_sss2.get(7), false, 0, true, 1, false, 0, false, 0, false, 0, 16756);
		
		createSR(list_sss2.get(8), true, 5, false, 0, false, 0, false, 0, false, 0, 16777);
		createSR(list_sss2.get(8), true, 4, false, 0, false, 0, false, 0, false, 0, 16765);
		createSR(list_sss2.get(8), true, 3, false, 0, false, 0, false, 0, false, 0, 16752);
		createSR(list_sss2.get(8), true, 4, false, 0, false, 0, false, 0, false, 0, 16758);
		createSR(list_sss2.get(8), true, 5, false, 0, false, 0, false, 0, false, 0, 16760);
		createSR(list_sss2.get(8), false, 0, true, 1, false, 0, false, 0, false, 0, 16756);
		
		createSR(list_sss2.get(9), true, 0, false, 0, false, 0, false, 0, false, 0, 16777);
		createSR(list_sss2.get(9), true, 4, false, 0, false, 0, false, 0, false, 0, 16765);
		createSR(list_sss2.get(9), true, 4, false, 0, false, 0, false, 0, false, 0, 16752);
		createSR(list_sss2.get(9), true, 5, false, 0, false, 0, false, 0, false, 0, 16758);
		createSR(list_sss2.get(9), true, 5, false, 0, false, 0, false, 0, false, 0, 16760);
		createSR(list_sss2.get(9), false, 0, true, 1, false, 0, false, 0, false, 0, 16756);
		System.out.println("Момент волнения");
		for (int i = 0; i < list_sss2.size()-listStudentGroup2.size(); i++) {
			((GroupSemester)gr2.getGs().toArray()[0]).getStudents().add(list_sss2.get(i));			
		}
		for (int i = listStudentGroup2.size(); i < list_sss2.size(); i++) {
			((GroupSemester)gr2.getGs().toArray()[1]).getStudents().add(list_sss2.get(i));			
		}
		///Повторить столько сколько человек создали (не через цикл, т.к. оценки разные)
		//	sss1_1 = Создание фиктивного SSS(sc1,lgs1)
		//		lgs1.add(sss1); //Возможно потребуется сохранение lgs1
		//			sr1_1 = Cоздания рейтинга SSS (sss1_1,Предмет1,Оценка)
		//			sr2_1 = Cоздания рейтинга SSS (sss1_1,Предмет2,Оценка)
		//		...
		///Конец первого повторения

		//    lgs2 = Создания фиктивного L_G_S(...,dg)
		//	dg.add(lgs2) //Возможно потребуется сохранение dg еще и тут

		///Повторить столько сколько человек создали (не через цикл, т.к. оценки разные)
		//	sss1_2 = Создание фиктивного SSS(sc1,lgs2)
		//	lgs2.add(sss1_2); //Возможно потребуется сохранение lgs1
		//		sr1_2 = Cоздания рейтинга SSS (sss1_2,ДругойПредмет1,Оценка)
		//		sr2_2 = Cоздания рейтинга SSS (sss1_2,ДругойПредмет2,Оценка)
		//		...
		///Конец второго повторения
		GroupDao g1 = new GroupDao();
		try {
			g1.create(gr1);
			System.out.println("Успех бюджетников");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("У бюджетников проблемы");
		}
		try {
			g1.create(gr2);
			System.out.println("Успех договорников");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("У договорников проблемы");
		}

	}
	//===Функция Создания фиктивной D_G
	//	Входные параметры: ???/определить по бд
	//	Выходные параметры: Dic_Group
	//		dg = Создание новой Dic_Group (Заполняем, Сохраняем в БД, если есть какие то поля которые != NULL обязательно, то задаем / возможны траблы с institute и curriculum)
	static Group createGroup(Long id, String name, Calendar begin, Calendar end){
		Group gr = new Group();
		gr.setId(id);
		gr.setName(name);
		gr.setDateOfBegin(begin.getTime());
		gr.setDateOfEnd(end.getTime());
		return gr;
	}
















	//===Функция Создания фиктивного L_G_S
	//	Входные параметры: ???/определить по бд/даты, номер семестра, курс, Группа/Dic_Group
	//	Выходные параметры: L_G_S
	//		lgs = Создаем новый текущий Семестр (Заполняем, Сохраняем в БД)/Если нужно то привязываем к какому-либо существующему семестру. 
	//		например если номер семестра 1 - то к прошлому, если 2 - то к текущему
	static Set<GroupSemester> createLGS(Group gr,int SemYearStart,int SemYearStart2, int SemDayStart,int SemDayEnd,int SemMonthStart,int SemMonthEnd,
			int SemDayStart2,int SemDayEnd2,int SemMonthStart2,int SemMonthEnd2){
		Set<GroupSemester> lgs = new HashSet<GroupSemester>();
		lgs.add(createGS(new GregorianCalendar(2010, 9, 1), new GregorianCalendar(2015, 9, 1),
				new GregorianCalendar(SemYearStart, SemMonthStart, SemDayStart), new GregorianCalendar(2014, SemMonthEnd, SemDayEnd),
				new GregorianCalendar(2010, 9, 1), new GregorianCalendar(2015, 9, 1),
				new GregorianCalendar(2010, 9, 1), new GregorianCalendar(2015, 9, 1),
				new GregorianCalendar(2010, 9, 1), new GregorianCalendar(2015, 9, 1),
				1 , 1 , gr
				));
		lgs.add(createGS(new GregorianCalendar(2010, 9, 1), new GregorianCalendar(2015, 9, 1),
				new GregorianCalendar(SemYearStart2, SemMonthStart2, SemDayStart2), new GregorianCalendar(2014, SemMonthEnd2, SemDayEnd2),
				new GregorianCalendar(2010, 9, 1), new GregorianCalendar(2015, 9, 1),
				new GregorianCalendar(2010, 9, 1), new GregorianCalendar(2015, 9, 1),
				new GregorianCalendar(2010, 9, 1), new GregorianCalendar(2015, 9, 1),
				2 , 1 , gr
				));
		return lgs;
	}

	static GroupSemester createGS(Calendar beginSem, Calendar endSem, 
			Calendar beginSes, Calendar endSes, 
			Calendar beginZach, Calendar endZach,
			Calendar beginVacation, Calendar endVacation,
			Calendar beginGrant, Calendar endGrant,
			int numSem, int course, Group gr){
		GroupSemester gs = new GroupSemester();

		gs.setDateOfBeginSemester(beginSem.getTime());
		gs.setDateOfEndSemester(endSem.getTime());

		gs.setDateOfBeginSession(beginSes.getTime());
		gs.setDateOfEndSession(endSes.getTime());

		gs.setDateOfBeginPassWeek(beginZach.getTime());
		gs.setDateOfEndPassWeek(endZach.getTime());

		gs.setDateOfBeginVacation(beginVacation.getTime());
		gs.setDateOfEndVacation(endVacation.getTime());

		gs.setDateOfBeginGrant(beginGrant.getTime());
		gs.setDateOfEndGrant(endGrant.getTime());

		gs.setSemesterNumber(numSem);
		gs.setCourse(course);
		
		gs.setCourse(4);
		//gs.setId_semester(33);
		
		gs.setGroup(gr);
		return gs;
	}	

	//===Функция Создание фиктивного SSS
	//	Входные параметры: Студенческая карта, LGS
	//	Выходные параметры: Новый SSS
	//		Создаем, Сохраняем
	static StudentSemesterStatus createSSS(StudentCard studCard, boolean governmentFinanced, 
			GroupSemester groupSemester, int sesres ){
		StudentSemesterStatus student = new StudentSemesterStatus();		
		student.setStudentCard(studCard);
		student.setGroupSemester(groupSemester);
		student.setGovernmentFinanced(governmentFinanced);
		student.setListener(false);
		//student.setSessionResult(-1);
		student.setTrustagreement(false); //все будут НЕ целевиками
		//student.setFormOfStudy(1);//все очники
		student.setSecondDegree(false); // у всех первое высшее образование
		student.setGroupLeader(false); //нет старост
		student.setEducationComplete(false);
		student.setSessionResult(sesres);
		return student;
	}
	 //===Функция Создания предмета (Не факт что нужно, возмжно лучше взять какие нибудь предметы из бд)

	//===Функция Cоздания рейтинга SSS
	//	Входные параметры: Студент/sss, Предмет/subject, Оценка/int
	//	Выходные параметры: Новый SesRait
	//		sr = Создаем новый рейтинг(Заполняем, Сохраняем в БД)
	static SessionRating createSR(StudentSemesterStatus student, boolean Exam, int ExamRat,
			boolean Pass, int PassRat,boolean CourseProject, int CourseProjectRat,
			boolean CourseWork, int CourseWorkRat,boolean Practic, int PracticRat, int idSub){
		SessionRating sr = new SessionRating();
		sr.setStudent(student);
		sr.setExam(Exam);
		sr.setExamRating(ExamRat);
		sr.setPass(Pass);
		sr.setPassRating(PassRat);
		sr.setCourseProject(CourseProject);
		sr.setCourseProjectRating(CourseProjectRat);
		sr.setCourseWork(CourseWork);
		sr.setCourseWorkRating(CourseWorkRat);
		sr.setPractic(Practic);
		sr.setPracticRating(PracticRat);
		
		
		student.getSessionRating().add(sr);
		return sr;
	}
}