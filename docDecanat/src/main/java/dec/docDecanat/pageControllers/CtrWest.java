package dec.docDecanat.pageControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.zkoss.zhtml.Br;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Window;

import dec.docDecanat.data.dao.GroupDao;
import dec.docDecanat.data.dao.GroupSemesterDao;
import dec.docDecanat.data.dao.OrderTypeDao;
import dec.docDecanat.data.dao.SemesterDAO;
import dec.docDecanat.data.dao.StudentSemesterStatusDao;
import dec.docDecanat.data.entity.Group;
import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.data.entity.OrderType;
import dec.docDecanat.data.entity.ProxyStringValue;
import dec.docDecanat.data.entity.Rule;
import dec.docDecanat.data.entity.Semester;
import dec.docDecanat.data.entity.SessionRating;
import dec.docDecanat.data.entity.StudentSemesterStatus;

public class CtrWest extends SelectorComposer<Component>{	

	//	@Wire
	//	Combobox chooseGroup;
	//	@Wire
	//	Combobox chooseCourse;	

	@Wire
	Combobox cmbFilterGroup;

	@Wire
	Combobox cmbChooseSemestr;	

	@Wire
	Window winMoveStudent;	//окно для перемещения студента по приказу и за его границу

	@Wire
	Listbox lbChooseGroup;

	@Wire
	Listbox lbChooseCourse;		

	@Wire
	Listbox lbListStudents;

	@Wire
	Combobox cmbChooseAmountGS; //количество семестров, которые необходимо просмотреть

	static boolean flagChoosenGroup = false;
	static public CtrWest ctrWestGlobal = new CtrWest();//используется, для корректной работы редактирования приказа (временное решение)
	Listheader lh = new Listheader();
	//List<StudentSemesterStatus> listSSSwest = new ArrayList<StudentSemesterStatus>();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		ctrWestGlobal = this;
		Executions.getCurrent().getDesktop().setAttribute("CurrentCtrWest", this); //создаем глобальную переменную
		if(CtrHead.flagChooseRule){
			showLB();
			//showCourse();
		}
		else{
			hideLB();
		}

	}

	void showLB(){
		List<Semester> listSem;
		try {
			listSem = new ArrayList<Semester>(SemesterDAO.SemGDAO.getAll());
			Collections.sort(listSem);
			ListModel<Semester> lmSemester = new ListModelList<Semester>(listSem);
			cmbChooseSemestr.setModel(lmSemester);
		} catch (Exception e) {
			System.out.println("Не удалось получить список семестров");
		}
		cmbFilterGroup.setVisible(false);
		lbChooseGroup.setVisible(true);
		lbChooseCourse.setVisible(true);
		lbListStudents.setVisible(true);
		lbChooseGroup.setEmptyMessage("Список групп пуст");
		lbChooseCourse.setEmptyMessage("Список курсов пуст");
		lbListStudents.setEmptyMessage("Список студентов пуст");
	}

	void showFilterGroup(){
		ctrWestGlobal.cmbChooseAmountGS.setVisible(false);
		cmbChooseSemestr.setVisible(false);
		cmbFilterGroup.setVisible(true);
		lbChooseCourse.setVisible(false);
		lbChooseGroup.setVisible(false);
		lbListStudents.setVisible(true);
	}

	void hideLB(){
		cmbChooseSemestr.setVisible(false);
		cmbFilterGroup.setVisible(false);
		lbChooseGroup.setVisible(false);
		lbChooseCourse.setVisible(false);		
		lbListStudents.setVisible(false);
	}

	void showCourse(){
		ListModel<ProxyStringValue> lm = null;
		try {
			lm = new ListModelList<ProxyStringValue>(GroupSemesterDao.gsDGlobal.getAllCourse((Semester) cmbChooseSemestr.getSelectedItem().getValue()));
			((ListModelList<ProxyStringValue>)lm).setMultiple(true);
		} catch (Exception e) {
			System.out.println("PLUHERROR Cannot get list all course str103 ctrWest =( ");
			e.printStackTrace();
		}
		lbChooseCourse.setModel(lm);
		Clients.showBusy(lbChooseCourse,"Загрузка");
		Clients.clearBusy(); 
	}

	@Listen("onChange = #cmbChooseSemestr")
	public void chooseSem()
	{
		lbChooseGroup.getItems().clear();
		lbChooseCourse.getItems().clear();
		lbListStudents.getItems().clear();
		lbChooseGroup.setEmptyMessage("Список групп пуст");
		lbChooseCourse.setEmptyMessage("Список курсов пуст");
		lbListStudents.setEmptyMessage("Список студентов пуст");
		showCourse();
	}

	@Listen("onSelect = #lbChooseCourse")
	public void showGroup(Event e){//надеюсь можно по компактнее это написать
		if(lbChooseCourse.getSelectedItem()!=null){

			lbChooseGroup.getItems().clear();
			lbListStudents.getItems().clear();


			List<GroupSemester> allGroupFromCourse = new ArrayList<GroupSemester>(); // listgroupsemestr
			for (int i = 0; i < lbChooseCourse.getSelectedItems().size(); i++) {
				try {
					// getAllFromCourse ИЗМЕНИТЬ
					allGroupFromCourse.addAll((List<GroupSemester>) GroupSemesterDao.gsDGlobal.getAllFromCourse( 
							Integer.parseInt(((ProxyStringValue)((Listitem)lbChooseCourse.getSelectedItems().toArray()[i]).getValue()).getValue()), (Semester) cmbChooseSemestr.getSelectedItem().getValue()));
				} catch (NumberFormatException e1) {
					System.out.println("Не удалось получить группы из выбранных курсов");
					e1.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}			

			}		

			Collections.sort(allGroupFromCourse);

			for (int i = 0; i < allGroupFromCourse.size(); i++) {
				if(allGroupFromCourse.get(i).getStudents().size()<1)
					allGroupFromCourse.remove(allGroupFromCourse.get(i));
			}
			ListModel<GroupSemester> lm=new ListModelList<GroupSemester>(allGroupFromCourse);
			((ListModelList<GroupSemester>)lm).setMultiple(true);
			lbChooseGroup.setModel(lm);
			Clients.showBusy(lbChooseGroup,"Загрузка");
			Clients.clearBusy(); 
		}
		else {
			Clients.showNotification("Пожалуйста, выберите курс.");
			while(lbChooseGroup.getChildren().size()>1)				
				lbChooseGroup.removeChild(lbChooseGroup.getChildren().get(1));
			while(lbListStudents.getChildren().size()>1)				
				lbListStudents.removeChild(lbListStudents.getChildren().get(1));

			lbChooseGroup.setEmptyMessage("Список групп пуст");
			lbListStudents.setEmptyMessage("Список студентов пуст");


		}
	}
	//TODO:СОРТИРОВКУ ПО ГРУППАМ И СТУДЕНТАМ
	@Listen("onClick = #lbChooseGroup")
	public void showStudent()
	{
		//TODO : переделать отсев правильно
		if(lbChooseGroup.getSelectedItem()!=null){
			lbListStudents.getItems().clear();
			List<StudentSemesterStatus> listWest = new ArrayList<StudentSemesterStatus>();
			List<StudentSemesterStatus> listWestTemp = new ArrayList<StudentSemesterStatus>();
			//tempList
			for (int j = 0; j < lbChooseGroup.getSelectedItems().size(); j++){
				//tempList
				listWestTemp.addAll(((GroupSemester) ((Listitem) lbChooseGroup.getSelectedItems().toArray()[j]).getValue()).getStudents()) ;
			}	
			Collections.sort(listWestTemp);
			for (int i = 0; i < listWestTemp.size(); i++) {
				if(westStudent(listWestTemp.get(i))){
					listWest.add(listWestTemp.get(i));
				}
			}
			//TODO: переработать студентов
			addSSSListItemsWest(listWest, true);
			StudentSemesterStatus.listSSSWest = listWest;
			flagChoosenGroup = true;
		}
		else {
			Clients.showNotification("Пожалуйста, выберите группу.");
			while(lbListStudents.getChildren().size()>1)				
				lbListStudents.removeChild(lbListStudents.getChildren().get(1));
			lbListStudents.setEmptyMessage("Список студентов пуст");
			flagChoosenGroup = false;
		}
	}

	public void addSSSListItemsWest(List<StudentSemesterStatus> list, boolean flagWest){
		System.out.println("Размер листа west после сортировки по приказам "+list.size());

		Collections.sort(list);
		while(lbListStudents.getChildren().size()>0)				
			lbListStudents.removeChild(lbListStudents.getChildren().get(0));

		Listhead lh= new Listhead();

		Listheader lh1=new Listheader();
		if (flagWest){
			lh1.setLabel("Студенты ("+list.size()+")");
			lh1.setHflex("1");//Width("240px");
			lh.appendChild(lh1);
		}
		else
		{
			lh1.setLabel("Студенты вне приказа ("+list.size()+")");
			lh1.setHflex("1");//Width("190px");
			Listheader lh2=new Listheader();
			lh2.setLabel("");
			lh2.setWidth("65px");
			lh.appendChild(lh1);
			lh.appendChild(lh2);
		}
		lbListStudents.appendChild(lh);

		for(int i = 0; i < list.size(); i++)	{
			Listitem li=new Listitem();	

			Label lb = new Label();
			Listcell lcFIO=new Listcell();

			lb.setValue("("+list.get(i).getGroupSemester().getGroup().getName() +") "+ list.get(i).getStudentCard().getHumanface().getFIO()
					+ "\n");
			lcFIO.appendChild(lb);
			li.appendChild(lcFIO);
			if(!flagWest){
				//				Listcell lcRem=new Listcell();
				//				lcRem.appendChild(createButtonMoveStudent(list.get(i),null,false));
				//				Listcell lcInfo=new Listcell();
				//				lcElement.appendChild();
				Listcell lcInfo=createButtonMoveStudent(list.get(i),null,false);
				li.appendChild(lcInfo);
			}
		if(!list.get(i).getGovernmentFinanced()){
			li.setStyle("background: #ffd8da;");
		}
			lbListStudents.appendChild(li);
		}
	}

	Listcell createButtonMoveStudent(StudentSemesterStatus sss, OrderRule orru, boolean flagDelStud){

		final OrderRule or = orru;
		final StudentSemesterStatus s1 = sss;
		final boolean flagDelStudent = flagDelStud;
		Listcell lc = new Listcell();

		Image imgInfo = new Image();
		//winMoveStudent.getPage().getc

		imgInfo.setSrc("imgs/quest.png");
		imgInfo.setTooltiptext("Причины, по которым студент попал в данный пункт");	
		imgInfo.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				if(Path.getComponent("/winNotes")!=null){
					Path.getComponent("/winNotes").detach();
				}
				CtrNotes.sss = s1;
				//TODO: Избавиться от статиков, сделать через функции с передачей параметров как: CtrCmbChooseSection.trasportStudent(s1,or,false); ОБДУМАТЬ
				Window window = (Window)Executions.createComponents("notes.zul", null, null);
				window.doModal();
			}
		});	

		Image imgRem = new Image();
		imgRem.setTooltiptext("Переместить студента");
		imgRem.setSrc("imgs/arrow.png");
		imgRem.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {	
				CtrCmbChooseSection.trasportStudent(s1,or,false);
			}
		});	

		Hlayout hl = new Hlayout();
		hl.appendChild(imgInfo);
		hl.appendChild(imgRem);
		if(flagDelStudent){
			Image imgDel = new Image();
			imgDel.setTooltiptext("Убрать студента из приказа");
			imgDel.setSrc("imgs/crossCLR.png");	
			imgDel.addEventListener("onClick", new EventListener() {
				public void onEvent(Event event) {	
					CtrCmbChooseSection.trasportStudent(s1,or,true);
				}
			});
			hl.appendChild(imgDel);
		}

		lc.appendChild(hl);
		return lc;
	}

	void loadListGroupForFilter(List<GroupSemester> listGr){
		Collections.sort(listGr);
		ListModel<GroupSemester> listGrCmb = new ListModelList<GroupSemester>(listGr);
		cmbFilterGroup.setModel(listGrCmb);
	}

	public  List<StudentSemesterStatus> loadStudentsFromChooseGroup(GroupSemester group){
		List<StudentSemesterStatus> listStudent = new ArrayList<StudentSemesterStatus>(group.getStudents());
		Collections.sort(listStudent);
		return listStudent;
	}			

	@Listen("onChange = #cmbFilterGroup")
	public void filterStudent()
	{
		List<StudentSemesterStatus> tempList = new ArrayList<StudentSemesterStatus>();
		tempList.clear();
		if(cmbFilterGroup.getSelectedItem()!=null){
			for (int i = 0; i < StudentSemesterStatus.listSSSWest.size(); i++) {
				if (StudentSemesterStatus.listSSSWest.get(i).getGroupSemester().getGroup().getName().equals(((GroupSemester)cmbFilterGroup.getSelectedItem().getValue()).getGroup().getName()))
					tempList.add(StudentSemesterStatus.listSSSWest.get(i));
			}
		}else
			tempList.addAll(StudentSemesterStatus.listSSSWest);

		addSSSListItemsWest(tempList, false);
	}

	public static boolean westStudent(StudentSemesterStatus student){
		boolean flagAdd = true;

		if(student.getAcademicLeave()!=null){
			if(student.getAcademicLeave()){
				flagAdd = false;
			}
		}

		if(student.getDeducted()!=null){
			if(student.getDeducted()){
				flagAdd = false;
			}
		}

		if(student.getListener()!=null){
			if(student.getListener()){
				flagAdd = false;
			}
		}

		// if(Rule.ruChoose.getOST().getName().equals(Order.orderTransfer)){//переводной
		// if(listWestTemp.get(i).getOrderTransfer()==null){
		// listWestTemp.get(i).setOrderTransfer(false);
		// }
		// //есть ли студент в данном приказе по данному семестру
		// if(listWestTemp.get(i).getOrderTransfer()!=null){
		//
		// System.out.println("PLUHNOTE CtrWest str 231 show status flag orderTransfer:");
		// System.out.println(listWestTemp.get(i).getOrderTransfer());
		// if(listWestTemp.get(i).getOrderTransfer()){
		// flagAdd = false;
		// }
		// }
		// }
		// if(Rule.ruChoose.getOST().getName().equals(Order.orderScholarship)){//стипендиальный 
		// //
		// if(listWestTemp.get(i).getOrderScholarship()==null){
		// listWestTemp.get(i).setOrderScholarship(false);
		// }
		// if(listWestTemp.get(i).getOrderScholarship()!=null){
		// System.out.println("PLUHNOTE CtrWest str 244 show status flag orderScholarship:");
		// System.out.println(listWest.get(i).getOrderScholarship());
		// if(listWestTemp.get(i).getOrderScholarship()){
		// flagAdd = false;
		// }
		// }
		// }

		return flagAdd;
	}
}




/*
 возможно это еще пригодится, для связи между страницами
  if(getPage().getRequestPath().equals("/order.zul")){
	while(((Vlayout)getPage().getFellow("orderField")).getChildren().size()>0)				
		((Vlayout)getPage().getFellow("orderField")).removeChild(((Vlayout)getPage().getFellow("orderField")).getChildren().get(0));
((Vlayout)getPage().getFellow("orderField")).appendChild(CtrOrderScholarship.loadStudents());*/

