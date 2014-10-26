package dec.docDecanat.pageControllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.ItemRenderer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;
import org.zkoss.zul.ext.GroupsSortableModel;

import reports.scholarshipReport.GroupScholarshipList;
import antlr.collections.impl.LList;
import dec.docDecanat.ItemRender.CourseCmbRender;
import dec.docDecanat.ItemRender.GroupCmbRender;
import dec.docDecanat.ItemRender.SectionCmbRender;
import dec.docDecanat.data.comparator.GroupSemesterComp;
import dec.docDecanat.data.comparator.StudentSemesterStatusComp;
import dec.docDecanat.data.dao.GroupDao;
import dec.docDecanat.data.dao.GroupSemesterDao;
import dec.docDecanat.data.dao.Link_note_student_semester_status_orderDAO;
import dec.docDecanat.data.dao.OrderDao;
import dec.docDecanat.data.dao.OrderRuleDao;
import dec.docDecanat.data.dao.OrderTypeDao;
import dec.docDecanat.data.dao.RuleDao;
import dec.docDecanat.data.dao.StudentSemesterStatusDao;
import dec.docDecanat.data.entity.Group;
import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.Link_note_student_semester_status_order;
import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.data.entity.OrderSubType;
import dec.docDecanat.data.entity.OrderType;
import dec.docDecanat.data.entity.Rule;
import dec.docDecanat.data.entity.Section;
import dec.docDecanat.data.entity.SessionRating;
import dec.docDecanat.data.entity.StudentSemesterStatus;

public class CtrOrder extends SelectorComposer<Component>{
	static public boolean printAll = true;
	//Время для оптимизации алгоритма
	static public boolean gettime = true;
	Long startTime=(long) 0;
	Long endTime=(long) 0;
	Long startTimeQuery=(long) 0;
	Long endTimeQuery=(long) 0;
	Long startTimeStudent=(long) 0;
	Long endTimeStudent=(long) 0;
	Long startTimeVisual=(long) 0;
	Long endTimeVisual=(long) 0;
	//=======================

	@Wire
	Groupbox gbBodyOrder;

	@Wire
	Groupbox gbDateOrder;

	@Wire
	Hlayout hlHeader;

	@Wire
	Vlayout orderField;

	@Wire
	Button btnCreateOrder; //формирование приказа

	@Wire
	Button btnReadOrder; //предпросмотр приказа

	@Wire
	Button btnSaveOrder; //сохранение текущего состояния приказа

	@Wire
	Button btnOrderWriteToday;

	@Wire
	Textbox txtNumberOrder;

	@Wire
	Textbox txtRuleDescriptionOrder;

	@Wire
	Datebox dateOfBeginOrder;

	@Wire
	Datebox dateOfEndOrder;

	@Wire
	Textbox txtRuleNameOrder;

	@Wire
	Label lbRuleNameOrder;

	@Wire
	Button btnReCreateOrder;

	@Wire
	Groupbox orderAll;


	//List<StudentSemesterStatus> listSSSorder = new ArrayList<StudentSemesterStatus>();
	Rule ruChoose = new Rule();
	public static Order ord = null;
	//??
	public static String GSsize = "0";

	List<StudentSemesterStatus> listWest = new ArrayList<StudentSemesterStatus>();
	public static List<StudentSemesterStatus> listSSS;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		Executions.getCurrent().getDesktop().setAttribute("CurrentCtrOrder", this);
		if(Rule.ruChoose == null){
			orderAll.setVisible(false);
			Clients.showNotification("Пожалуйста, подождите");
			Executions.sendRedirect("index.zul");
		}
		else{
			orderAll.setVisible(true);
			//			if(printAll)System.out.println("Choosen Rule " + Rule.ruChoose.getName());
			//			if(printAll)System.out.println("Choosen Order " + CtrOrder.ord.getId());
			ruChoose = Rule.ruChoose;
			txtNumberOrder.setVisible(false);

			if (CtrOrder.ord==null){//действия при работе с новым приказом			
				hideBodyOrder();			
				btnCreateOrder.setVisible(true);
			}
			else{//действия при работе с выбранным приказом				
				showBodyOrder();
			}
		}
	}

	void hideBodyOrder(){
		gbBodyOrder.setVisible(false);
		btnSaveOrder.setVisible(false);		
		btnReadOrder.setVisible(false);	
		hlHeader.setVisible(false);	
		txtRuleNameOrder.setVisible(false);
		lbRuleNameOrder.setVisible(false);
		CtrWest.ctrWestGlobal.lbListStudents.getItems().clear();
		CtrWest.ctrWestGlobal.lbChooseGroup.getItems().clear();		
		while(orderField.getChildren().size()>0) //ОЧИЩЕНИЕ				
			orderField.removeChild(orderField.getChildren().get(0));

	}

	void showBodyOrder(){
		// даты приказа
		if(CtrOrder.ord.getDateOfBegin()!=null){
			//			gbDateOrder.setVisible(true);
			dateOfBeginOrder.setValue(CtrOrder.ord.getDateOfBegin());
			if(CtrOrder.ord.getDateOfEnd()!=null)
				dateOfEndOrder.setValue(CtrOrder.ord.getDateOfEnd());
		}
		//номер приказа
		if((CtrOrder.ord.getDateOfEnd()!=null)&&(!CtrOrder.ord.getDateOfEnd().equals(""))&&(!CtrOrder.ord.getDateOfEnd().equals("0"))){
			txtNumberOrder.setVisible(true);
			if(CtrOrder.ord.getNumber()!=null && !CtrOrder.ord.getNumber().equals("0"))
				txtNumberOrder.setValue(CtrOrder.ord.getNumber());
			else showTxtNumberOrder();
		}
		//описание приказа
		if(CtrOrder.ord.getDescription()!=null){
			txtRuleDescriptionOrder.setValue(CtrOrder.ord.getDescription());
		}

		//наименование приказа
		if(CtrOrder.ord.getDescriptionSpec()!=null){
			txtRuleNameOrder.setValue(CtrOrder.ord.getDescriptionSpec());
		}

		//TODO: вероятно стоит заменить на listSSS
		List<StudentSemesterStatus> stlistInSection = new ArrayList<StudentSemesterStatus>();
		stlistInSection.clear();

		for (int i = 0; i < CtrOrder.ord.getOrderRules().size(); i++) {				
			stlistInSection.addAll(new ArrayList<StudentSemesterStatus>(((OrderRule)CtrOrder.ord.getOrderRules().toArray()[i]).getStudents()));			
		}

		List<GroupSemester> stgroup = sectionStudentForGroup(new ArrayList<StudentSemesterStatus>(stlistInSection));
		stlistInSection.clear();
		for (int j = 0; j < stgroup.size(); j++) {
			stlistInSection.addAll(((StudentSemesterStatus)stgroup.get(j).getStudents().toArray()[0]).getGroupSemester().getStudents());
		}
		listSSS=stlistInSection;
		divideToSection(listSSS,CtrOrder.ord,false,false,false);
		btnCreateOrder.setVisible(false);		
		CtrWest.ctrWestGlobal.showFilterGroup();
	}

	public List<StudentSemesterStatus> getListSSS(){
		//TODO: вероятно стоит заменить на listSSS
		List<StudentSemesterStatus> stlistInSection = new ArrayList<StudentSemesterStatus>();
		stlistInSection.clear();

		for (int i = 0; i < CtrOrder.ord.getOrderRules().size(); i++) {				
			stlistInSection.addAll(new ArrayList<StudentSemesterStatus>(((OrderRule)CtrOrder.ord.getOrderRules().toArray()[i]).getStudents()));			
		}

		List<GroupSemester> stgroup = sectionStudentForGroup(new ArrayList<StudentSemesterStatus>(stlistInSection));
		stlistInSection.clear();
		for (int j = 0; j < stgroup.size(); j++) {
			stlistInSection.addAll(((StudentSemesterStatus)stgroup.get(j).getStudents().toArray()[0]).getGroupSemester().getStudents());
		}
		listSSS=stlistInSection;
		return listSSS;
	}

	@Listen("onClick = #btnCreateOrder")//формирует приказ, при выборе правила, при условии, что в west выбрана хотя бы одна группа.
	public void show(){
		if (CtrWest.ctrWestGlobal.cmbChooseAmountGS.getSelectedItem()!=null){
			if (CtrWest.flagChoosenGroup){
				if(StudentSemesterStatus.listSSSWest.size()>0){
					hlHeader.setVisible(true);
					btnCreateOrder.setVisible(false);
					dateOfBeginOrder.setValue(new java.util.Date());
					//gbDateOrder.setVisible(true);
					gbBodyOrder.setVisible(true);
					txtRuleNameOrder.setVisible(true);
					lbRuleNameOrder.setVisible(true);
					listSSS = new ArrayList<StudentSemesterStatus>(StudentSemesterStatus.listSSSWest);
					if(printAll)System.out.println("PLUHNOTE 1.Amount students in order: " + listSSS.size() + " ctrOrder str 191");
					GSsize = CtrWest.ctrWestGlobal.cmbChooseAmountGS.getSelectedItem().getValue().toString();
					divideListAllStudent(listSSS,0);
					btnSaveOrder.setVisible(true);		
					btnReadOrder.setVisible(true);
					CtrWest.ctrWestGlobal.cmbChooseAmountGS.setVisible(false);
					CtrWest.ctrWestGlobal.lbChooseCourse.setVisible(false);
					CtrWest.ctrWestGlobal.cmbChooseSemestr.setVisible(false);
					CtrWest.ctrWestGlobal.cmbFilterGroup.setVisible(true);
					CtrWest.ctrWestGlobal.lbChooseGroup.setVisible(false);
				}else Clients.showNotification("Ошибка загрузки списка студентов");
			}else Clients.showNotification("Выберите хотя бы одну группу");	
		}else Clients.showNotification("Необходимо указать количество семестров для проверки"); 
	}

	@Listen("onClick = #btnReadOrder")//осуществляет предпросмотр приказа.
	public void read(){
		Window window = null;
		if (CtrOrder.ord.getOrderSubType().getName()
				.equals(Order.orderScholarship)) {
			window = (Window)Executions.createComponents(
					"report.zul", null, null);
		}
		if (CtrOrder.ord.getOrderSubType().getName()
				.equals(Order.orderTransfer)) {
			window = (Window)Executions.createComponents(
					"transferReport.zul", null, null);
		}
		window.doModal();
		//Window window = (Window)Executions.createComponents("report.zul", null, null);
		//window.doModal();
	}		

	public void divideListAllStudent(List<StudentSemesterStatus> listAllStudent, int typeFlag){	
		if(typeFlag == 0){
			ord = new Order();
			if(printAll)System.out.println("PLUHNOTE 2.Create new order, run divideToSection ctrOrder str 211");
			ord.setDescription(ruChoose.getDescription());
			txtRuleDescriptionOrder.setValue(ord.getDescription());
			ord.setOrderSubType(ruChoose.getOST());
			dateOfBeginOrder(); 
			ord = divideToSection(listAllStudent, ord, true,false,false);
		}else{
			ord = divideToSection(listAllStudent, ord, true,false,true);
		}

		//		ord = new Order();
		//		if(printAll)System.out.println("PLUHNOTE 2.Create new order, run divideToSection ctrOrder str 211");
		//		ord.setDescription(ruChoose.getDescription());
		//		txtRuleDescriptionOrder.setValue(ord.getDescription());
		//		ord.setOrderSubType(ruChoose.getOST());
		//		dateOfBeginOrder();		
		//		ord = divideToSection(listAllStudent, ord, true,false,false);
	}

	final public Order divideToSection(List<StudentSemesterStatus> listAllStudent, Order order, boolean newOrder, boolean flagFilter,boolean reCreate){//разделение всех людей по секциям
		List<StudentSemesterStatus> listAllStudentsInSection = new ArrayList<StudentSemesterStatus>();
		listAllStudentsInSection.clear();
		listWest.clear();

		if(gettime)startTime = System.currentTimeMillis();

		if(newOrder){
			List<List<GroupSemester>> listListGs = new ArrayList<List<GroupSemester>>();
			GroupSemester lastGroup=new GroupSemester();
			List<GroupSemester> listGS=new ArrayList<GroupSemester>();
			//ТЕСТОВАЯ ОБЛАСТЬ
			if(!reCreate){
				//Формирование разделов приказа из секций
				for (int i = 0; i < ruChoose.getSections().size(); ++i) {
					OrderRule orRu = new OrderRule();
					orRu.setSection((Section) ruChoose.getSections().toArray()[i]);
					orRu.setDescription(((Section) ruChoose.getSections().toArray()[i]).getDescription());
					orRu.setFoundation(((Section) ruChoose.getSections().toArray()[i]).getFoundation());
					order.addOrderRule(orRu);
					orRu.setOrder(order);
					orRu.setPrint(orRu.getSection().isPrint());
				}
			}else{
				for(int j = 0; j < listAllStudent.size(); ++j){
					listAllStudent.get(j).setInSection(false);
					listAllStudent.get(j).getList_notes().clear();
				}
				for(int i = 0; i < order.getOrderRules().size(); ++i){
					OrderRule orRu=((OrderRule)order.getOrderRules().toArray()[i]);
					//TODO: ПРОВЕРИТЬ
					if(order.getOrderSubType().getName().equals(Order.orderTransfer)){
						orRu.getSection().setSecParametersTransfer(orRu.getSection().parseParametersTransfer());
					}
					if(order.getOrderSubType().getName().equals(Order.orderScholarship)){
						System.out.println(">>here1 "+orRu.getSection().getSecParameters());
						orRu.getSection().setSecParameters(orRu.getSection().parseParametersScholarship());
					}
					//На всякий пожарный
					for(int ii=0;ii<((OrderRule)order.getOrderRules().toArray()[i]).getStudents().size();ii++){
						StudentSemesterStatus stSS=(StudentSemesterStatus) ((OrderRule)order.getOrderRules().toArray()[i]).getStudents().toArray()[ii];
						stSS.getList_notes().clear();
					}
					//-----
					orRu.getStudents().clear();
				}
				//Очистить примечания
				Link_note_student_semester_status_orderDAO sda=new Link_note_student_semester_status_orderDAO();
				try {
					sda.deleteNotesFromOrder(order);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//Сортировка списка студентов по группам
			StudentSemesterStatusComp comparator = new StudentSemesterStatusComp();
			comparator.setCompareMethod(StudentSemesterStatusComp.SSSCompareMethods.BY_GR);
			Collections.sort(listAllStudent, comparator);

			//Заполнение секций студентами
			for(int i = 0; i < order.getOrderRules().size(); ++i){
				OrderRule orRu=((OrderRule)order.getOrderRules().toArray()[i]);
				boolean fl=false;
				//проверка переводного на Продление
				if(orRu.getSection().getSecParametersTransfer()!=null){
					if (orRu.getSection().getSecParametersTransfer().isProl()){
						fl=false;
					}else{
						fl=true;
					}
				}else{
					fl=true;
				}
				//проверка стипендиального на Продление
				if(orRu.getSection().getSecParameters()!=null){
					if (orRu.getSection().getSecParameters().size()>0){
						if (orRu.getSection().getSecParameters().get(0).isProl()){
							fl=false;
						}else{
							fl=true;
						}
					}
				}else{
					fl=true;
				}
				//если не Продление - заполняем пункт студентами
				if (fl){
					List<StudentSemesterStatus> listSectionSSS = new ArrayList<StudentSemesterStatus>();
					if(printAll)System.out.println("PLUHNOTE 4.1. Write in section: " + orRu.getSection().getName()+" ctrOrder str371 ");	
					for(int j = 0; j < listAllStudent.size(); ++j){
						//Если студент еще не попал в данный приказ
						System.out.println(">>in section "+listAllStudent.get(j).getInSection());
						if(!listAllStudent.get(j).getInSection()){
							if(!listAllStudent.get(j).getGroupSemester().getId().equals(lastGroup.getId())){
								if(gettime)startTimeQuery = System.currentTimeMillis();
								boolean findGS=false;
								for(int ll=0;ll<listListGs.size();ll++){
									//System.out.println("size DOP "+listListGs.size()+" id1="+listListGs.get(ll).get(0).getGroup().getId()+" || "+listAllStudent.get(j).getGroupSemester().getGroup().getId());
									if(listListGs.get(ll).get(0).getGroup().getId().equals(listAllStudent.get(j).getGroupSemester().getGroup().getId())){
										findGS=true;
										listGS=listListGs.get(ll);	
										lastGroup=listAllStudent.get(j).getGroupSemester();
									}
									if(findGS)break;
								}
								if(findGS==false){
									GroupSemesterDao gsD = new GroupSemesterDao();
									try {
										//System.out.println(">>go to DB "+lastGroup+" new group "+listAllStudent.get(j).getGroupSemester().getGroup().getName()+" | "+listAllStudent.get(j).getGroupSemester().getGroup().getId()+" (ctrOrd 315)");
										Integer amountGS=0;
										if(GSsize.equals("0")){//TODO: Временное решение, запросить кол-во семестров отдельно.
											amountGS=1;
										}
										else{
											if(GSsize.equals("Все")){
												amountGS = -1;
											}
											else{
												amountGS = Integer.parseInt(GSsize);
											}
										}
										listGS=gsD.getSomeFromGroup(listAllStudent.get(j).getGroupSemester().getGroup(), order.getOrderSubType().getName().equals(Order.orderTransfer)?amountGS:amountGS+1, listAllStudent.get(j).getGroupSemester().getSemester());
										System.out.println("<>< LISTGS SIZE "+listGS.size());
										listListGs.add(listGS);
										lastGroup=listAllStudent.get(j).getGroupSemester();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								GroupSemesterComp comparatorGr = new GroupSemesterComp();
								comparatorGr.setCompareMethod(GroupSemesterComp.GSCompareMethods.BY_SEMESTER_REV);
								Collections.sort(listGS, comparatorGr);
								//Для отладки:
								if(gettime)endTimeQuery = System.currentTimeMillis();
								if(gettime)System.out.println(">>> Time Query:"+ (endTimeQuery-startTimeQuery)+" ms.");
							}

							if(order.getOrderSubType().getName().equals(Order.orderTransfer)){//переводной
								if(gettime)startTimeStudent = System.currentTimeMillis();
								if(listAllStudent.get(j).getGovernmentFinanced()==orRu.getSection().getSecParametersTransfer().isPayOrFree()){//TODO: проверяет бюджетник или платник
									if(checkStudentSectionTransfer(listGS,orRu.getSection(), listAllStudent.get(j))){
										listSectionSSS.add(listAllStudent.get(j));
										listAllStudent.get(j).setOrderTransfer(true);
										addNotes(listAllStudent.get(j),orRu);
									}
								}
								if(gettime)endTimeStudent = System.currentTimeMillis();
								if(gettime)System.out.println(">>> Time Student:"+ (endTimeStudent-startTimeStudent)+" ms.");
							}		

							if(order.getOrderSubType().getName().equals(Order.orderScholarship)){//стипендиальный	
								if(gettime)startTimeStudent = System.currentTimeMillis();
								if(checkStudentSectionScholar(listGS,orRu.getSection(), listAllStudent.get(j))){
									listSectionSSS.add(listAllStudent.get(j));		
									listAllStudent.get(j).setOrderScholarship(true);
									addNotes(listAllStudent.get(j),orRu);	
								}
								if(gettime)endTimeStudent = System.currentTimeMillis();
								if(gettime)System.out.println(">>> Time Student:"+ (endTimeStudent-startTimeStudent)+" ms.");
							}
						}
					}

					if(listSectionSSS.size() > 0){
						if(printAll)System.out.println("PLUHNOTE 5.2. Complete, size list = " + listSectionSSS.size() + ", for section "+orRu.getSection().getName()+" ctrOrder str442");
						orRu.getStudents().addAll(listSectionSSS);
						listAllStudentsInSection.addAll(listSectionSSS);
					}
					if(!reCreate){
						order.addOrderRule(orRu);
						orRu.setOrder(order);
					}
				}
			}

			for (int i = 0; i < ord.getOrderRules().size(); i++) {
				OrderRule orru = (OrderRule) ord.getOrderRules().toArray()[i];
				if(orru.getSection().getSecParametersTransfer()!=null)
					if (orru.getSection().getSecParametersTransfer().isProl()){
						listAllStudentsInSection.addAll(orru.getStudents());
					}
				if(orru.getSection().getSecParameters()!=null)
					if (orru.getSection().getSecParameters().size()>0)
						if (orru.getSection().getSecParameters().get(0).isProl()){
							listAllStudentsInSection.addAll(orru.getStudents());
						}
			}

		}else{
			//РЕДАКТИРОВАНИЕ ПРИКАЗА
			for (int i = 0; i < CtrOrder.ord.getOrderRules().size(); i++) {				
				listAllStudentsInSection.addAll(new ArrayList<StudentSemesterStatus>(((OrderRule)CtrOrder.ord.getOrderRules().toArray()[i]).getStudents()));			
			}
		}
		//================
		for (int i = 0; i < listAllStudent.size(); i++) {
			if(!listAllStudentsInSection.contains(listAllStudent.get(i))){
				if(CtrWest.westStudent(listAllStudent.get(i))){
					listWest.add(listAllStudent.get(i));
				}
			}
		}

		//загружаем список групп в фильтр west
		////////////////////////////////////////////////////////////!!!!!!! ОПТИМИЗИРОВАТЬ, список групп получать другим способом
		List<GroupSemester> listGS = sectionStudentForGroup(new ArrayList<StudentSemesterStatus>(listAllStudentsInSection)); //спиcок групп в приказе
		Collections.sort(listGS);
		CtrWest.ctrWestGlobal.loadListGroupForFilter(listGS);//посылаем список групп в комбобокс фильтр
		//-------Посылаем список студентов, которых нет в приказе
		if(!flagFilter){
			if(newOrder){
				for (int i = 0; i < listWest.size(); i++) {
					addNotes(listWest.get(i),null); 
				}
			}

			StudentSemesterStatus.listSSSWest.clear();
			StudentSemesterStatus.listSSSWest.addAll(listWest);

			//			CtrWest.ctrWestGlobal.addSSSListItemsWest(StudentSemesterStatus.listSSSWest, false); //вызываем не из west
			CtrWest.ctrWestGlobal.addSSSListItemsWest(listWest, false); //вызываем не из west
		}


		List<OrderRule> listOrRu = new ArrayList<OrderRule>(order.getOrderRules());
		List<OrderRule> listorruTemp = new ArrayList<OrderRule>();
		if(flagFilter){
			for (int i = 0; i < listOrRu.size(); i++) {
				List<StudentSemesterStatus> listTemp = new ArrayList<StudentSemesterStatus>();
				List<StudentSemesterStatus> listS = new ArrayList<StudentSemesterStatus>(listOrRu.get(i).getStudents());
				for (int k = 0; k < listS.size(); k++) {
					for (int z = 0; z < listAllStudent.size(); z++) {
						if(listS.get(k).getId()==listAllStudent.get(z).getId()){
							listTemp.add(listS.get(k));
						}
					}
				}
				OrderRule orruTemp = new OrderRule();
				orruTemp.setSection(listOrRu.get(i).getSection());
				orruTemp.setId(listOrRu.get(i).getId());
				orruTemp.setOrder(listOrRu.get(i).getOrder());
				orruTemp.setCurrentRule(listOrRu.get(i).isCurrentRule());
				orruTemp.setStudents(new HashSet<StudentSemesterStatus>(listTemp));
				listorruTemp.add(orruTemp);

			}
		}else{
			listorruTemp=listOrRu;
		}
		Collections.sort(listorruTemp);
		if(printAll)System.out.println("PLUHNOTE 3.Dividing finished, go to visualOrder ctrOrder str309");
		if(gettime)startTimeVisual = System.currentTimeMillis();
		visualOrder(listorruTemp, newOrder, listGS);
		if(gettime)endTimeVisual = System.currentTimeMillis();
		if(gettime)System.out.println(">>> Time Visual:"+ (endTimeVisual-startTimeVisual)+" ms.");

		if(newOrder && !reCreate){
			try {
				CtrOrder.ord = OrderDao.odDGlobal.create(CtrOrder.ord);
				CtrOrder.ord = OrderDao.odDGlobal.update(CtrOrder.ord);
				order=CtrOrder.ord;
				listSSS=getListSSS();
			} catch (Exception e) {
				Clients.showNotification("ОШИБКА: Не удалось сохранить приказ");
				if(printAll)System.out.println("PLUHERROR Cannot save order in datebase ctrOrder str570 "+e.getMessage());
			}
		}
		if(reCreate){
			try {
				CtrOrder.ord = OrderDao.odDGlobal.update(CtrOrder.ord);
				order=CtrOrder.ord;
				listSSS=getListSSS();
			} catch (Exception e) {
				Clients.showNotification("ОШИБКА: Не удалось обновить приказ");
				if(printAll)System.out.println("PLUHERROR Cannot update order in datebase ctrOrder str578 "+e.getMessage());
			}
		}
		if(gettime)endTime = System.currentTimeMillis();
		if(gettime)System.out.println(">>> Time Total:"+ (endTime-startTime)+" ms.");
		//System.out.println(">>Test Size: "+listAllStudent);
		return order;
	}

	boolean checkStudentSectionTransfer(List<GroupSemester> listGS,Section sec, StudentSemesterStatus sssCur){
		//		if(printAll)System.out.println("-----");
		//		if(printAll)System.out.println("str275 student:"+sssCur.getStudentCard().getHumanface().getFIO());
		//		if(printAll)System.out.println("str275 gs:"+sssCur.getGroupSemester().getId());
		boolean flagInOrder = sec.getSecParametersTransfer().isAllsemOrOnlysem();
		// TODO: Попытка сделать корректную сортировку
		//		OrderDao or=new OrderDao(); 
		//		List<GroupSemester> listGS=or.getListGroup(sssCur);
		//			    List<GroupSemester> listGS = new ArrayList<GroupSemester>(sssCur.getGroupSemester().getGroup().getGs());
		//				GroupSemesterDao gsD = new GroupSemesterDao();
		//				List<GroupSemester> listGS=new ArrayList<GroupSemester>();
		//				try {
		//					listGS = new ArrayList<GroupSemester>(gsD.getAllFromGroup(sssCur.getGroupSemester().getGroup()));
		//				} catch (Exception e) {
		//					// TODO Auto-generated catch block
		//					e.printStackTrace();
		//				}
		//===========
		GroupSemesterComp comparator = new GroupSemesterComp();
		comparator.setCompareMethod(GroupSemesterComp.GSCompareMethods.BY_SEMESTER_REV);
		Collections.sort(listGS, comparator);

		Integer amountGS = 0;

		if(GSsize.equals("Все")){
			amountGS = listGS.size();
		}
		else{
			amountGS = Integer.parseInt(GSsize);
		}

		//		System.out.println(">>count: "+amountGS+", size"+GSsize);
		//		for (int i = 0; i < amountGS ; i++) {
		//			if(i<listGS.size()){
		//				GroupSemester gs = (GroupSemester) listGS.get(i);
		//				System.out.println(">> "+gs.getSemester().getId());
		//			}
		//		}

		//System.out.println(">>student come tr "+sssCur.getStudentCard().getHumanface().getFIO()+", "+amountGS+" | "+listGS.size());
		for (int i = 0; i < amountGS ; i++) {
			if(i<listGS.size()){ //TODO: Проверить, возможно лишнее условие!
				GroupSemester gs = (GroupSemester) listGS.get(i);
				int amountNotFinishSubject = 0; //количество не закрытых
				//				System.out.println(">>maybe here "+sssCur.getGroupSemester().getId()+", "+gs.getId());
				if(sssCur.getGroupSemester().getId()>=gs.getId()){ // Проверяем только предыдущие семестры (исправить, id - не самая надежная вещь)
					//					System.out.println(">>or here "+gs.getStudents().size());
					for (int j = 0; j < gs.getStudents().size(); j++) { // идем по списку сессий студента и проверяем наличие долгов
						StudentSemesterStatus sssGs = (StudentSemesterStatus) gs.getStudents().toArray()[j];
						///
						boolean flagHaveProblems = false;
						//System.out.println(">>student come tr2 "+sssCur.getStudentCard().getHumanface().getFIO()+" | "+sssGs.getStudentCard().getId()+" | "+sssCur.getStudentCard().getId());
						if(sssGs.getStudentCard().getId().equals(sssCur.getStudentCard().getId())){
							//System.out.println(">>student come tr3 "+sssCur.getStudentCard().getHumanface().getFIO()+" SR: "+sssGs.getSessionResult()+" | "+gs.getId());
							if(sssGs.getSessionResult()<1){	//если <1 значит студент не закрыл сессию
								flagHaveProblems = true;
								Calendar myCal = new GregorianCalendar();
								//							if(printAll)System.out.println("!!!ctrorder str 303 КОНЕЦ СЕССИИ " + sssGs.getGroupSemester().getDateOfEndSession() + ", "+sssGs.getGroupSemester().getId());
								myCal.setTime(sssGs.getGroupSemester().getDateOfEndSession());
								myCal.add(Calendar.MONTH,sec.getSecParametersTransfer().getAmountSummerSemester());
								sssGs.setProlongationenddate(myCal.getTime()); //присваиваем дату окончания проверямому студенту
								int amountSubject = 0; 			//количество предметов
								for (int z = 0; z < sssGs.getSessionRating().size(); z++) {
									SessionRating sesRat = (SessionRating) sssGs.getSessionRating().toArray()[z];
									if(sesRat.isExam()){
										amountSubject++;
									}
									if(sesRat.isCourseProject()){
										amountSubject++;
									}
									if(sesRat.isCourseWork()){
										amountSubject++;
									}
									if(sesRat.isPractic()){
										amountSubject++;
									}
									if(sesRat.isPass()){
										amountSubject++;
									}
								}

								for (int k = 0; k < sssGs.getSessionRating().size(); k++) {
									//int amountNotFinishSubjectLocal=0;


									SessionRating sesRat = (SessionRating) sssGs.getSessionRating().toArray()[k];
									if(sesRat.getExamRating()<3 && sesRat.isExam()){
										//if(printAll)System.out.println(">> - exam " + sssCur.getStudentCard().getHumanface().getFIO());
										amountNotFinishSubject++;
									}
									if(sesRat.getCourseProjectRating()<3 && sesRat.isCourseProject()){
										//if(printAll)System.out.println(">> - cp " + sssCur.getStudentCard().getHumanface().getFIO());
										amountNotFinishSubject++;
									}
									if(sesRat.getCourseWorkRating()<3 && sesRat.isCourseWork()){
										//if(printAll)System.out.println(">> - cw " + sssCur.getStudentCard().getHumanface().getFIO());
										amountNotFinishSubject++;
									}
									if(!sssCur.getGroupSemester().getSemester().getId().equals(gs.getSemester().getId())){
										if(sesRat.getPracticRating()<3 && sesRat.isPractic()){
											//if(printAll)System.out.println(">> - pr " + sssCur.getStudentCard().getHumanface().getFIO());
											amountNotFinishSubject++;
										}
									}
									if(sesRat.getPassRating()==0 && sesRat.isPass()){
										//if(printAll)System.out.println(">> - pa " + sssCur.getStudentCard().getHumanface().getFIO());
										amountNotFinishSubject++;
									}
								}
								if(amountNotFinishSubject==amountSubject){// опасная строчка, прощаем долги
									amountNotFinishSubject =0;
								}
							}else{
								amountNotFinishSubject=0;
							}
							if(printAll)System.out.println("ctrorder str 475 " + sec.getSecParametersTransfer().getAmountMinSubject() +"<" + amountNotFinishSubject + " < "+sec.getSecParametersTransfer().getAmountMaxSubject() + " " + sssCur.getStudentCard().getHumanface().getFIO() + "-"+gs.getSemester().getId());								

							if(!flagHaveProblems){
								Calendar myCal = new GregorianCalendar();
								myCal.setTime(ord.getDateOfBegin());
								myCal.add(Calendar.MONTH,12);
								sssGs.setProlongationenddate(myCal.getTime()); //присваиваем дату окончания проверямому студенту
							}

							if(amountNotFinishSubject > sec.getSecParametersTransfer().getAmountMinSubject() 
									&& amountNotFinishSubject < sec.getSecParametersTransfer().getAmountMaxSubject()){
								//если есть долги, то смотр, до какого числа он может иметь долги
								if(printAll)System.out.println("Date prologation "+sssGs.getProlongationenddate()+" ctrorder str 480");
								if(sssGs.getProlongationenddate()!=null)	
									if (ord.getDateOfBegin().compareTo(sssGs.getProlongationenddate())<0) {
										if(printAll)System.out.println("Date prologation"+ord.getDateOfBegin()+" < "+sssGs.getProlongationenddate()+" ctrorder str 483");
										if(!sec.getSecParametersTransfer().isAllsemOrOnlysem()){
											if(printAll)System.out.println("ctrorder str 485");
											sssCur.setInSection(true);
											flagInOrder = true;
										}
									}else{
										if(printAll)System.out.println("ctrord 490 "+ord.getDateOfBegin()+">"+sssGs.getProlongationenddate());
										return false;
									}
								if(amountNotFinishSubject==0){//если нет долгов
									//???
								}
								if (amountNotFinishSubject<0){
									Clients.showNotification("Ошибка в алгоритме, которые определяет, попадает ли студент в переводной приказ. :\\");
								}
							}else{
								if(sec.getSecParametersTransfer().isAllsemOrOnlysem()){
									if(printAll)System.out.println("ctrorder str 499");
									flagInOrder = false;
								}
							}
							break;
						}
					}
				}
			}
		}
		//TODO КОСТЫЛЬ В СВЯЗИ С ОТСУТСТВИЕМ ДАТ ПРОДЛЕНИЯ
		if(flagInOrder){
			if(!sec.getName().equals("Перевод") && !sec.getName().equals("Перевод договорники")){
				if(sssCur.getSessionProlongation()!=null){
					if(sssCur.getSessionProlongation()==true){
						for (int i = 0; i < ord.getOrderRules().size(); i++) {
							OrderRule orru = (OrderRule) ord.getOrderRules().toArray()[i];
							if (orru.getSection().getSecParametersTransfer()!=null)
								if (orru.getSection().getSecParametersTransfer().isProl() && ((orru.getSection().getSecParametersTransfer().isPayOrFree() && sssCur.getGovernmentFinanced()) || (!orru.getSection().getSecParametersTransfer().isPayOrFree() && !sssCur.getGovernmentFinanced()))){
									//TODO Здесь можно ставить дату
									System.out.println("=============Отладка");
									orru.getStudents().add(sssCur);
									sssCur.setInSection(true);
									addNotes(sssCur,orru);
									return false;
								}
						}

					}
				}
			}
		}
		//TODO НОРМАЛЬНЫЙ КОД
		//				if(flagInOrder){
		//					if(!sec.getName().equals("Перевод")){
		//						if(sssCur.getProlongationbegindate()!=null){
		//							if(sssCur.getProlongationbegindate().compareTo(ord.getDateOfBegin())>0){
		//								for (int i = 0; i < ord.getOrderRules().size(); i++) {
		//									OrderRule orru = (OrderRule) ord.getOrderRules().toArray()[i];
		//									if (orru.getSection().getSecParametersTransfer()!=null)
		//										if (orru.getSection().getSecParametersTransfer().isProl()){
		//											orru.getStudents().add(sssCur);
		//											return false;
		//										}
		//								}
		//							}
		//						}
		//					}
		//				}

		return flagInOrder;

	}

	public static List<OrderRule> groupStudentForSection(List<StudentSemesterStatus> listStudent){
		List<OrderRule> sectionInGroup = new ArrayList<OrderRule>();
		for(int i = 0; i < listStudent.size(); ++i){
			for(int j = 0; j < CtrOrder.ord.getOrderRules().size(); ++j){
				for(int k = 0; k < ((OrderRule)CtrOrder.ord.getOrderRules().toArray()[j]).getStudents().size();++k){
					StudentSemesterStatus student = (((StudentSemesterStatus)((OrderRule)CtrOrder.ord.getOrderRules().toArray()[j]).getStudents().toArray()[k]));
					if(listStudent.get(i).getId()==student.getId()){
						boolean addInSection = false;
						for (int m = 0; m < sectionInGroup.size(); m++) {
							if(sectionInGroup.get(m).getId()==((OrderRule)CtrOrder.ord.getOrderRules().toArray()[j]).getId()){
								sectionInGroup.get(m).getStudents().add(listStudent.get(i));
								addInSection = true;
							}
						}				
						if(!addInSection){
							OrderRule orderRule = new OrderRule();
							orderRule.setId(((OrderRule)CtrOrder.ord.getOrderRules().toArray()[j]).getId());
							orderRule.setSection(((OrderRule)CtrOrder.ord.getOrderRules().toArray()[j]).getSection());
							orderRule.getStudents().add(listStudent.get(i));
							orderRule.setDescription(((OrderRule)CtrOrder.ord.getOrderRules().toArray()[j]).getDescription());
							orderRule.setFoundation(((OrderRule)CtrOrder.ord.getOrderRules().toArray()[j]).getFoundation());
							orderRule.setPrint(((OrderRule)CtrOrder.ord.getOrderRules().toArray()[j]).isPrint());
							sectionInGroup.add(orderRule);
						}
					}
				}
			}
		}

		return sectionInGroup;
	}

	boolean checkStudentSectionScholar(List<GroupSemester> listGS,Section sec, StudentSemesterStatus sss){
		//Если студент - платник (разобраться куда кидать)
		if(sss.getGovernmentFinanced()==false){
			return false;
		}
		//OrderDao or=new OrderDao(); 
		//List<GroupSemester> listGS=or.getListGroup(sss);
		//List<GroupSemester> listGS = new ArrayList<GroupSemester>(sss.getGroupSemester().getGroup().getGs());
		System.out.println(">>check for section: "+sec.getName()+" / "+sss.getStudentCard().getHumanface().getFIO());
		for (int i = 0; i <listGS.size() ; i++) {
			GroupSemester gs = listGS.get(i);
			if(!gs.getId().equals(sss.getGroupSemester().getId())){
				if(sss.getGroupSemester().getDateOfBeginSession().compareTo(gs.getDateOfBeginSession())==-1){
					for (int j = 0; j < gs.getStudents().size(); j++) {
						StudentSemesterStatus sssGs = (StudentSemesterStatus) gs.getStudents().toArray()[j];
						if(sssGs.getStudentCard().getId().equals(sss.getStudentCard().getId())){
							if(sssGs.getSessionResult()<1 && !sec.getName().equals("Отказать")){	
								//System.out.println(">>here1 "+sssGs.getStudentCard().getHumanface().getFIO());
								return false;
							}
						}
					}
				}
			}
		}

		int sch5=0,sch4=0,sch3=0, schComplete=0, schPlan=0;
		for (int k = 0; k < sss.getSessionRating().size(); k++) {
			SessionRating sesRat = (SessionRating) sss.getSessionRating().toArray()[k];
			//5--------------------------------------------------
			if(sesRat.getExamRating()==5)  {
				sch5++;
				schComplete++;
			}
			if(sesRat.getCourseProjectRating()==5){
				sch5++;
				schComplete++;
			}
			if(sesRat.getCourseWorkRating()==5){
				sch5++;
				schComplete++;
			}

			if(sesRat.getPracticRating()==5){
				sch5++;
				schComplete++;
			}
			//4--------------------------------------------------
			if(sesRat.getExamRating()==4) {
				sch4++;
				schComplete++;
			}
			if(sesRat.getCourseProjectRating()==4){
				sch4++;
				schComplete++;
			} 
			if(sesRat.getCourseWorkRating()==4){
				sch4++;
				schComplete++;
			}
			if(sesRat.getPracticRating()==4){
				sch4++;
				schComplete++;
			}
			//3--------------------------------------------------		
			if(sesRat.getExamRating()==3) {
				sch3++;
				schComplete++;
			}
			if(sesRat.getCourseProjectRating()==3){
				sch3++;
				schComplete++;
			}
			if(sesRat.getCourseWorkRating()==3){
				sch3++;
				schComplete++;
			}
			if(sesRat.getPracticRating()==3){
				sch3++;
				schComplete++;
			}

			if(sesRat.getPassRating()==1){
				schComplete++;
			}


			if(((SessionRating) sss.getSessionRating().toArray()[k]).isExam()) {
				schPlan++;
				if(printAll)System.out.println("PLUHNOTE ctrorder495 " +sss.getStudentCard().getHumanface().getFamily()+" - " 
						+ ((SessionRating) sss.getSessionRating().toArray()[k]).getExamRating() +
						((SessionRating) sss.getSessionRating().toArray()[k]).getCourseProjectRating() +
						((SessionRating) sss.getSessionRating().toArray()[k]).getCourseWorkRating() +
						((SessionRating) sss.getSessionRating().toArray()[k]).getPracticRating() + 
						((SessionRating) sss.getSessionRating().toArray()[k]).getPassRating() + " " + schComplete +" from " + schPlan);
			}
			if(((SessionRating) sss.getSessionRating().toArray()[k]).isPass()){
				schPlan++;
				if(printAll)System.out.println("PLUHNOTE ctrorder504 " +sss.getStudentCard().getHumanface().getFamily()+" - " 
						+ ((SessionRating) sss.getSessionRating().toArray()[k]).getExamRating() +
						((SessionRating) sss.getSessionRating().toArray()[k]).getCourseProjectRating() +
						((SessionRating) sss.getSessionRating().toArray()[k]).getCourseWorkRating() +
						((SessionRating) sss.getSessionRating().toArray()[k]).getPracticRating() + 
						((SessionRating) sss.getSessionRating().toArray()[k]).getPassRating() + " " + schComplete +" from " + schPlan);
			}
			if(((SessionRating) sss.getSessionRating().toArray()[k]).isCourseProject()){
				schPlan++;
				if(printAll)System.out.println("PLUHNOTE ctrorder513 " +sss.getStudentCard().getHumanface().getFamily()+" - " 
						+ ((SessionRating) sss.getSessionRating().toArray()[k]).getExamRating() +
						((SessionRating) sss.getSessionRating().toArray()[k]).getCourseProjectRating() +
						((SessionRating) sss.getSessionRating().toArray()[k]).getCourseWorkRating() +
						((SessionRating) sss.getSessionRating().toArray()[k]).getPracticRating() + 
						((SessionRating) sss.getSessionRating().toArray()[k]).getPassRating() + " " + schComplete +" from " + schPlan);
			}
			if(((SessionRating) sss.getSessionRating().toArray()[k]).isCourseWork()){
				schPlan++;
				if(printAll)System.out.println("PLUHNOTE ctrorder522 " +sss.getStudentCard().getHumanface().getFamily()+" - " 
						+ ((SessionRating) sss.getSessionRating().toArray()[k]).getExamRating() +
						((SessionRating) sss.getSessionRating().toArray()[k]).getCourseProjectRating() +
						((SessionRating) sss.getSessionRating().toArray()[k]).getCourseWorkRating() +
						((SessionRating) sss.getSessionRating().toArray()[k]).getPracticRating() + 
						((SessionRating) sss.getSessionRating().toArray()[k]).getPassRating() + " " + schComplete +" from " + schPlan);
			}
			// TODO: Поставить условие проверки не в текущем семестре для практики
			//			if(((SessionRating) sss.getSessionRating().toArray()[k]).isPractic()){
			//				schPlan++;
			//				if(printAll)System.out.println("PLUHNOTE ctrorder637 " +sss.getStudentCard().getHumanface().getFamily()+" - " 
			//						+ ((SessionRating) sss.getSessionRating().toArray()[k]).getExamRating() +
			//						((SessionRating) sss.getSessionRating().toArray()[k]).getCourseProjectRating() +
			//						((SessionRating) sss.getSessionRating().toArray()[k]).getCourseWorkRating() +
			//						((SessionRating) sss.getSessionRating().toArray()[k]).getPracticRating() + 
			//						((SessionRating) sss.getSessionRating().toArray()[k]).getPassRating() + " " + schComplete +" from " + schPlan);
			//			}


		}

		/*S	if(printAll)System.out.println("!!!!!!!!!!!!!!!!!!!!!!!"+sec.getName()+"!!!!!!!!!!!!!!!!!!!!!!!");
		if(printAll)System.out.println("Студент "+sss.getStudentCard().getHumanface().getFamily());
		if(printAll)System.out.println("Количество предметов "+schAllSub + " Количество сданных предметов " + schAll);
		if(printAll)System.out.println("sch5 "+sch5);
		if(printAll)System.out.println("sch4 "+sch4);
		if(printAll)System.out.println("sch3 "+sch3);*/
		//if(printAll)System.out.println("ctrorder548 " +sss.getStudentCard().getHumanface().getFamily()+" - " +sch3);

		// Old Vers: Должники попадают в отказать
		//		if(schPlan>schComplete && !sec.getName().equals("Отказать")){
		// NEW Vers: Должники попадают в WEST


		//		if(schPlan>schComplete && sec.getName().equals("Отказать")){
		//		if(schPlan>schComplete || sec.getName().equals("Отказать")){
		if(schPlan>schComplete){
			if(sss.getSessionProlongation()==true){
				for (int i = 0; i < ord.getOrderRules().size(); i++) {
					OrderRule orru = (OrderRule) ord.getOrderRules().toArray()[i];
					if (orru.getSection().getSecParameters()!=null){
						if(orru.getSection().getSecParameters().size()>0)
							if (orru.getSection().getSecParameters().get(0).isProl()){
								if(!orru.getStudents().contains(sss)){							
									if(sss.getLocalSesRes()==null){
										//Тестируется данный метод
										sss.setLocalSesRes(sss.prevSessionResult(listGS));
									}
									if(sss.getLocalSesRes()!=null)
										if(sss.getLocalSesRes()>1){
											orru.getStudents().add(sss);
											sss.setInSection(true);
											addNotes(sss,orru);
											System.out.println(">>go to prologation");
										}
								}
								return false;
							}
					}
				}
			}
			//System.out.println(">>go: "+schPlan+" | "+schComplete+" to "+sec.getName()+" / "+sss.getStudentCard().getHumanface().getFIO());
			//return true;
			return false;
		}
		//		}

		///////////5/////////////////
		if(sec.getSecParameters().get(0).getSimbol().equals(">")){
			if(sch5 <= sec.getSecParameters().get(0).getAmount()){
				return false;
			}
		}

		if(sec.getSecParameters().get(0).getSimbol().equals("<")){
			if(sch5 >= sec.getSecParameters().get(0).getAmount()){
				return false;
			}
		}

		if(sec.getSecParameters().get(0).getSimbol().equals(">=")){
			if(sch5 < sec.getSecParameters().get(0).getAmount()){
				return false;
			}
		}

		if(sec.getSecParameters().get(0).getSimbol().equals("<=")){
			if(sch5 > sec.getSecParameters().get(0).getAmount()){
				return false;
			}
		}

		if(sec.getSecParameters().get(0).getSimbol().equals("=")){
			if(sch5 != sec.getSecParameters().get(0).getAmount()){
				return false;
			}
		}

		///////////4/////////////////
		if(sec.getSecParameters().get(1).getSimbol().equals(">")){
			if(sch4 <= sec.getSecParameters().get(1).getAmount()){
				return false;
			}
		}

		if(sec.getSecParameters().get(1).getSimbol().equals("<")){
			if(sch4 >= sec.getSecParameters().get(1).getAmount()){
				return false;
			}
		}

		if(sec.getSecParameters().get(1).getSimbol().equals(">=")){
			if(sch4 < sec.getSecParameters().get(1).getAmount()){
				return false;
			}
		}

		if(sec.getSecParameters().get(1).getSimbol().equals("<=")){
			if(sch4 > sec.getSecParameters().get(1).getAmount()){
				return false;
			}
		}

		if(sec.getSecParameters().get(1).getSimbol().equals("=")){
			if(sch4 != sec.getSecParameters().get(1).getAmount()){
				return false;
			}
		}

		///////////3/////////////////
		if(sec.getSecParameters().get(2).getSimbol().equals(">")){
			if(sch3 <= sec.getSecParameters().get(2).getAmount()){
				return false;
			}
		}

		if(sec.getSecParameters().get(2).getSimbol().equals("<")){
			if(sch3 >= sec.getSecParameters().get(2).getAmount()){
				return false;
			}
		}

		if(sec.getSecParameters().get(2).getSimbol().equals(">=")){
			if(sch3 < sec.getSecParameters().get(2).getAmount()){
				return false;
			}
		}

		if(sec.getSecParameters().get(2).getSimbol().equals("<=")){
			if(sch3 > sec.getSecParameters().get(2).getAmount()){
				return false;
			}
		}

		if(sec.getSecParameters().get(2).getSimbol().equals("=")){
			if(sch3 != sec.getSecParameters().get(2).getAmount()){
				return false;
			}
		}

		System.out.println(">>go to all param: "+schPlan+" | "+schComplete+" to "+sec.getName()+" / "+sss.getStudentCard().getHumanface().getFIO());
		sss.setInSection(true);
		return true;

	}

	void addNotes(StudentSemesterStatus student, OrderRule orRu){
		//TODO: Запись причины
		String sessionres = "";
		for (int j = 0; j < student.getSessionRating().size(); j++) {
			SessionRating session = (SessionRating) student.getSessionRating().toArray()[j];
			if(session.isExam())
				sessionres += session.getSubject().getDicSub().getSubjectName()+" - "+session.getExamRating()+ " (Экз) <br/> " ;
			if(session.isPass()){
				String rez="";
				if(session.getPassRating()==1)
					rez = " \"+\" ";
				else 
					rez =  " \"-\" ";
				sessionres +=  session.getSubject().getDicSub().getSubjectName() +" - "+ rez+" (Зач) <br/> ";
			}
			if(session.isCourseProject())
				sessionres += session.getSubject().getDicSub().getSubjectName() +" - "+  session.getCourseProjectRating()+" (КП) <br/> " ;
			if(session.isCourseWork())
				sessionres += session.getSubject().getDicSub().getSubjectName()  +" - "+ session.getCourseWorkRating()+" (КР) <br/>"  ;
			if(session.isPractic())
				sessionres +=session.getSubject().getDicSub().getSubjectName() +" - "+  session.getPracticRating()+" (П) <br/>" ;			
		}
		if(!student.getGovernmentFinanced())sessionres +="------<br/>Договорник.<br/>";
		//sessionres += " ("+student.getSessionResult()+","+student.getGroupSemester().getId()+")";

		Link_note_student_semester_status_order note = new Link_note_student_semester_status_order();
		note.setDateOfNotes(CtrOrder.ord.getDateOfBegin());
		note.setSss(student);
		note.setOrder(CtrOrder.ord);
		note.setOrruStart(null);
		note.setOrruFinish(orRu);
		note.setNote(sessionres);
		student.getList_notes().add(note);
	}

	void visualOrder(List<OrderRule> listOrRu, boolean newOrder, List<GroupSemester> listGS){
		while(orderField.getChildren().size()>0) //ОЧИЩЕНИЕ				
			orderField.removeChild(orderField.getChildren().get(0));
		//Vlayout resLayout = new Vlayout(); удалить
		orderField.appendChild(filterOrder(listGS));
		for(int i = 0; i < listOrRu.size(); ++i){
			OrderRule curOrRu = listOrRu.get(i);
			//if(printAll)System.out.println("ctrorder str 656 ID ORRU: " + curOrRu.getId());
			//if(printAll)System.out.println("ctrorder str 656 ID ORRU: " + curOrRu.get);
			List<GroupSemester> listGroup = sectionStudentForGroup(new ArrayList<StudentSemesterStatus>(curOrRu.getStudents()));
			//НЕ ЧИСТИТСЯ СПИСОК
			orderField.appendChild(visualizationSection(listGroup, curOrRu));		
		}
	}

	Hbox filterOrder(List<GroupSemester> listGS){ // заполняет комбобоксы для фильтра по приказу
		Hbox hlFilter = new Hbox();
		hlFilter.setAlign("center");
		Label lbname = new Label();
		lbname.setValue("Фильтр: ");
		hlFilter.appendChild(lbname);
		List<Integer> listCourse = groupForCourse(listGS);
		Group g = new Group();
		g.setName("Все");
		GroupSemester gs = new GroupSemester();
		gs.setGroup(g);

		Collections.sort(listGS);
		listGS.add(0, gs);
		Collections.sort(listCourse);
		ListModel<Integer> lmCourse = new ListModelList<Integer>(listCourse);
		ListModel<GroupSemester> lmGS = new ListModelList<GroupSemester>(listGS);

		if(listCourse.size()>1){
			Combobox cmbCours = new Combobox();
			cmbCours.setPlaceholder("Выберите курс");
			cmbCours.setModel(lmCourse);
			cmbCours.setItemRenderer(new CourseCmbRender());
			hlFilter.appendChild(cmbCours);
		}
		final Combobox cmbGroup = new Combobox();
		cmbGroup.setPlaceholder("Выберите группу");
		cmbGroup.setModel(lmGS);
		cmbGroup.setItemRenderer(new GroupCmbRender());
		cmbGroup.addEventListener("onChange", new EventListener() {
			public void onEvent(Event event) {
				//listSSS.get
				List<StudentSemesterStatus> listFiltrSSS = new ArrayList<StudentSemesterStatus>();
				if(cmbGroup.getSelectedIndex()!=0){
					for (int i = 0; i < listSSS.size(); i++) {
						if(listSSS.get(i).getGroupSemester().getId().equals(((GroupSemester)cmbGroup.getSelectedItem().getValue()).getId()))
							listFiltrSSS.add(listSSS.get(i));		
						divideToSection(listFiltrSSS, ord, false,true,false);			
					}
				}else{
					divideToSection(listSSS, ord, false,false,false);	
				}
			}
		});
		hlFilter.appendChild(cmbGroup);
		if(listCourse.size()==1){
			Label lb = new Label();
			lb.setValue("В данном приказе только один курс"); //listGS.get(0).getCourse()
			hlFilter.appendChild(lb);
		}
		return hlFilter;
	}
	public static List<GroupSemester> sectionStudentForGroup(List<StudentSemesterStatus> listStudent){
		//делим по группам
		if(printAll)System.out.println("PLUHNOTE Divided group for section ctrOrder str722 ");
		List<GroupSemester> groupsInSection = new ArrayList<GroupSemester>();
		groupsInSection.clear();
		for (int i = 0; i < listStudent.size(); i++) {			
			boolean addInGroup = false;
			for (int j = 0; j < groupsInSection.size(); j++) {
				if(listStudent.get(i).getGroupSemester().getId()==groupsInSection.get(j).getId()){
					groupsInSection.get(j).getStudents().add(listStudent.get(i));
					addInGroup=true;
				}				
			}

			if(!addInGroup){
				GroupSemester newGroup = new GroupSemester();
				newGroup.setId(listStudent.get(i).getGroupSemester().getId());
				newGroup.setGroup(listStudent.get(i).getGroupSemester().getGroup());
				newGroup.setCourse(listStudent.get(i).getGroupSemester().getCourse());
				newGroup.getStudents().add(listStudent.get(i));
				groupsInSection.add(newGroup);
			}
		}
		Collections.sort(groupsInSection);
		return groupsInSection;
	}

	List<Integer> groupForCourse(List<GroupSemester> listGS){
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < listGS.size(); i++) {
			if(!list.contains(listGS.get(i).getCourse())){
				list.add(listGS.get(i).getCourse());
			}
		}
		return list;
	}
	public Groupbox visualizationSection(List<GroupSemester> listGroup, OrderRule or){
		final OrderRule orRu = or;
		if(printAll)System.out.println("PLUHNOTE Show list groups ctrOrder str757");
		Long id = orRu.getSection().getId();
		//описываем пункт
		Groupbox gb=new Groupbox();
		final Caption cp=new Caption();
		gb.setSclass("section");
		gb.setMold("3d");

		final Checkbox ch = new Checkbox();//чекбокс куотороый определяет, будет ли данный пункт в печатном варианте
		ch.setStyle("font-weight:bold;");
		ch.setLabel(orRu.getSection().getName() + " (" + orRu.getStudents().size() + ")");
		//ch.setChecked(orRu.getSection().isPrint());
		if(orRu.isPrint()==null)
			orRu.setPrint(false);
		ch.setChecked(orRu.isPrint());
		ch.setTooltiptext("Печатать или не печатать в бумажном варианте");
		ch.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				if(ch.isChecked()){
					cp.setSclass("NotPrintSection");
				}
				else {
					cp.setSclass("PrintSection");
				}
				//				orRu.getSection().setPrint(ch.isChecked());
				//				if(printAll)System.out.println("PLUHNOTE Status printOrNotToPrint: "+orRu.getSection().isPrint()+" ctrorder str779");
				orRu.setPrint(ch.isChecked());
				if(printAll)System.out.println("PLUHNOTE Status printOrNotToPrint: "+orRu.isPrint()+" ctrorder str1165");
			}
		});
		cp.appendChild(ch);
		if(ch.isChecked()){
			cp.setSclass("NotPrintSection");
		}
		else {
			cp.setSclass("PrintSection");
		}
		gb.appendChild(cp);

		Vlayout finish = new Vlayout();

		if (listGroup.size()>0){	
			if (listGroup.size()>1){
				Hbox hlh = new Hbox();
				hlh.setAlign("center");
				hlh.appendChild(new Label("Всем группам "));
				hlh.appendChild(showDatebox(orRu, null));
				finish.appendChild(hlh);	
			}
			finish.appendChild(createDescript(orRu));	
			//загружаем группы пункта
			for (int g = 0; g < listGroup.size(); g++) {	
				Listbox studentsBox = new Listbox();


				Listhead lh= new Listhead();
				String widthCalendar = "150px";						//ширина столбцов для календарей

				// заголовок ФИО студента
				Listheader lhFIO=new Listheader();
				lhFIO.setHflex("1");
				lhFIO.setHeight("0px");
				// заголовок даты начала выплат стипендии
				Listheader lhBeginDateScholar=new Listheader();
				lhBeginDateScholar.setWidth(widthCalendar);
				lhBeginDateScholar.setHeight("0px");
				// заголовок даты окончания выплат стипендии
				Listheader lhEndDateScholar=new Listheader();
				lhEndDateScholar.setWidth(widthCalendar);
				lhEndDateScholar.setHeight("0px");
				// заголовок даты отчисления студента
				Listheader lhFinishStudyDate=new Listheader();
				lhFinishStudyDate.setWidth(widthCalendar);
				lhFinishStudyDate.setHeight("0px");
				// заголовок даты продления студента
				Listheader lhProlongationDate=new Listheader();
				lhProlongationDate.setWidth(widthCalendar);
				lhProlongationDate.setHeight("0px");
				// заголовок кнопки управления студентом
				Listheader lhInfo=new Listheader();
				lhInfo.setWidth("80px");
				lhInfo.setHeight("0px");
				//				Listheader lhRem=new Listheader();
				//				lhRem.setWidth("35px");
				//				lhRem.setHeight("0px");
				// заголовок кнопки удаления студента
				//				Listheader lhDel=new Listheader();
				//				lhDel.setWidth("35px");
				//				lhDel.setHeight("0px");
				lh.appendChild(lhFIO);
				if(ord.getOrderSubType().getName().equals(Order.orderScholarship)){
					lh.appendChild(lhBeginDateScholar);
					lh.appendChild(lhEndDateScholar);
				}
				lh.appendChild(lhFinishStudyDate);
				lh.appendChild(lhProlongationDate);
				lh.appendChild(lhInfo);
				//	lh.appendChild(lhRem);
				//				lh.appendChild(lhDel);
				studentsBox.appendChild(lh);


				//загружаем студентов группы
				final List<StudentSemesterStatus> listStudent = new ArrayList<StudentSemesterStatus>(listGroup.get(g).getStudents());
				Collections.sort(listStudent);
				//if(printAll)System.out.println("Количество студентов "  + listStudent.size() +" ctrOrder str514");
				//собираем имя игруппы и календарь

				finish.appendChild(createHLHead(listGroup.get(g), orRu));

				//обрабатываем список студентов одной группы
				for ( int s = 0; s < listStudent.size(); s++) {

					studentsBox.appendChild(createBodyOrderWithGoodStudents(listStudent.get(s), listStudent, orRu));

				}
				finish.appendChild(studentsBox);

				//<<Добавлено 28.06

				if(orRu.getSection().getFoundation()!=null){
					if(orRu.getFoundation()!=null){
						if(orRu.getFoundation().equals("")){
							orRu.setFoundation(orRu.getSection().getFoundation());	
						}
					}else{
						orRu.setFoundation(orRu.getSection().getFoundation());
					}
				}
			}
			final Textbox txtFoundation = new Textbox();
			txtFoundation.setTooltiptext("Основания");
			txtFoundation.setPlaceholder("Основания для данного пункта");
			txtFoundation.setHflex("1");
			txtFoundation.setText(orRu.getFoundation());
			txtFoundation.addEventListener("onChange", new EventListener() {
				public void onEvent(Event event) {
					orRu.setFoundation(txtFoundation.getText());
				}
			}); 
			finish.appendChild(txtFoundation);
		}
		else{//заполнение пункта без студентов
			Listbox studentsBox = new Listbox();
			Listitem li=new Listitem();
			Listcell lc1=new Listcell();
			Label lb = new Label();
			lb = new Label();
			lb.setValue("В данном пункте студентов нет");
			lc1.appendChild(lb);
			li.appendChild(lc1);
			studentsBox.appendChild(li);
			finish.appendChild(studentsBox);
		}
		gb.appendChild(finish);
		return gb;
	}

	Textbox createDescript (OrderRule orrul){
		final OrderRule orru = orrul;
		//собираем описание пункта
		final Textbox txtDescript = new Textbox();

		if(orru.getSection().getDescription()!=null){
			if(orru.getDescription()!=null){
				if(orru.getDescription().equals("")){
					orru.setDescription(orru.getSection().getDescription());	
				}
			}else{
				orru.setDescription(orru.getSection().getDescription());
			}
		}


		txtDescript.setValue(orru.getDescription());
		txtDescript.setPlaceholder("Описание пункта " + orru.getSection().getName());
		txtDescript.setHflex("1");
		txtDescript.setHeight("50px");
		txtDescript.setMultiline(true);
		txtDescript.setValue(orru.getDescription());
		txtDescript.addEventListener("onChange", new EventListener() {
			public void onEvent(Event event) {
				orru.setDescription(txtDescript.getText());
				for(int i=0;i<orru.getOrder().getOrderRules().size();i++){
					if(printAll)System.out.println(">> DEsc("+i+")"+((OrderRule)orru.getOrder().getOrderRules().toArray()[i]).getDescription());
				}
			}
		}); 
		return txtDescript;
	}

	Hlayout createHLHead(GroupSemester gs, OrderRule orru){
		Hlayout hlGroupName = new Hlayout();
		Label chGroupName = new Label();
		chGroupName.setStyle("font-weight:bold;");
		chGroupName.setValue(gs.getGroup().getName() + " (" + gs.getStudents().size() + ")");
		Hbox hlh = new Hbox();
		hlh.setAlign("center");
		hlh.appendChild(chGroupName);	
		hlh.appendChild(showDatebox(orru, gs));
		hlGroupName.appendChild(hlh);

		return hlGroupName;
	}

	Listitem createBodyOrderWithGoodStudents(StudentSemesterStatus sss, final List<StudentSemesterStatus> list, OrderRule or){
		final StudentSemesterStatus student = sss;
		final OrderRule orRu = or;
		// Создание строки для стипендиального и переводного
		Listitem li=new Listitem(); 						//строка таблицы
		Listcell LCFIO=new Listcell(); 						//первый столбец (фио студента)
		final Listcell LCScholarBegin=new Listcell(); 		//второй столбец дата начала выплат стипендии (только стипендиальный)
		final Listcell LCScholarEnd=new Listcell(); 		//третий столбец дата начала окончания стипендии (только стипендиальный)
		final Listcell LCFinishStudy=new Listcell();	 	//четвертый столбец дата отчисления студента (стипендиальный, переводной)
		final Listcell LCProlongationStudy=new Listcell(); 	//пятый столбец дата окончания продления (стипендиальный, переводной)
		if(!sss.getGovernmentFinanced()){
			li.setStyle("background: #ffd8da;");
		}
		//label для ФИО
		String result = "";
		if(student.getSessionResult()<=0){
			result = " (долги)";
		}
		if(student.getSessionResult()==1){
			result = " (удовл)";
		}
		if(student.getSessionResult()==2){
			result = " (хор)";
		}
		if(student.getSessionResult()==3){
			result = " (хор и отл)";
		}
		if(student.getSessionResult()==4){
			result = " (отл)";
		}


		Label lbFIO = new Label();
		lbFIO.setValue(student.getStudentCard().getHumanface().getFIO()+result);
		String valueDdate = "Не указано";


		//начало выплат стипендии
		final Label lbScholarBegin = new Label();
		lbScholarBegin.setTooltiptext("Начало выплат стипендий");
		if(student.getDateOfScholarshipBegin()==null)
			lbScholarBegin.setValue(valueDdate);
		else{
			lbScholarBegin.setValue(new SimpleDateFormat("dd.MM.yyyy").format(student.getDateOfScholarshipBegin()));
			lbScholarBegin.setSclass("correct");
		}

		// окончание выплат
		final Label lbScholarEnd = new Label();
		lbScholarEnd.setTooltiptext("Оканчание выплат стипендий");
		if(student.getDateOfScholarshipEnd()==null)
			lbScholarEnd.setValue(valueDdate);
		else{
			lbScholarEnd.setValue(new SimpleDateFormat("dd.MM.yyyy").format(student.getDateOfScholarshipEnd()));
			lbScholarEnd.setSclass("correct");
		}
		//отчисление
		final Label lbProlongationEndDate = new Label();
		lbProlongationEndDate.setTooltiptext("Дата отчисления");
		if(student.getProlongationenddate()==null)
			lbProlongationEndDate.setValue(valueDdate);
		else{
			lbProlongationEndDate.setValue(new SimpleDateFormat("dd.MM.yyyy").format(student.getProlongationenddate()));
			lbProlongationEndDate.setSclass("correct");
		}

		//продление
		final Label lbProlongationBeginDate = new Label();
		lbProlongationBeginDate.setTooltiptext("Дата продления");
		if(student.getProlongationbegindate()==null)
			lbProlongationBeginDate.setValue(valueDdate);
		else{
			lbProlongationBeginDate.setValue(new SimpleDateFormat("dd.MM.yyyy").format(student.getProlongationbegindate()));
			lbProlongationBeginDate.setSclass("correct");
		}


		// при клике на label начало выплат
		lbScholarBegin.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				final Datebox dateScholarBegin = new Datebox();
				dateScholarBegin.setPlaceholder("Начало стипендии");
				if(student.getDateOfScholarshipBegin()!=null)
					dateScholarBegin.setValue(student.getDateOfScholarshipBegin());
				//изменение даты продления
				dateScholarBegin.addEventListener("onChange", new EventListener() {
					public void onEvent(Event event) {
						student.setDateOfScholarshipBegin(dateScholarBegin.getValue());
						while(LCScholarBegin.getChildren().size()>0) //ОЧИЩЕНИЕ				
							LCScholarBegin.removeChild(LCScholarBegin.getChildren().get(0));
						if(student.getDateOfScholarshipBegin()==null)
							lbScholarBegin.setValue("Не указано");
						else
							lbScholarBegin.setValue(new SimpleDateFormat("dd.MM.yyyy").format(student.getDateOfScholarshipBegin()));
						LCScholarBegin.appendChild(lbScholarBegin);
					}
				});	
				while(LCScholarBegin.getChildren().size()>0) //ОЧИЩЕНИЕ				
					LCScholarBegin.removeChild(LCScholarBegin.getChildren().get(0));
				LCScholarBegin.appendChild(dateScholarBegin);
			}});

		// при клике на label окончание выплат
		lbScholarEnd.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				final Datebox dateScholarEnd = new Datebox();
				dateScholarEnd.setPlaceholder("Окончание стипендии");
				if(student.getDateOfScholarshipEnd()!=null)
					dateScholarEnd.setValue(student.getDateOfScholarshipEnd());
				//изменение даты продления
				dateScholarEnd.addEventListener("onChange", new EventListener() {
					public void onEvent(Event event) {
						student.setDateOfScholarshipEnd(dateScholarEnd.getValue());
						while(LCScholarEnd.getChildren().size()>0) //ОЧИЩЕНИЕ				
							LCScholarEnd.removeChild(LCScholarEnd.getChildren().get(0));
						if(student.getDateOfScholarshipEnd()==null)
							lbScholarEnd.setValue("Не указано");
						else
							lbScholarEnd.setValue(new SimpleDateFormat("dd.MM.yyyy").format(student.getDateOfScholarshipEnd()));
						LCScholarEnd.appendChild(lbScholarEnd);
					}
				});	
				while(LCScholarEnd.getChildren().size()>0) //ОЧИЩЕНИЕ				
					LCScholarEnd.removeChild(LCScholarEnd.getChildren().get(0));
				LCScholarEnd.appendChild(dateScholarEnd);
			}});



		// при клике на label отчисление
		lbProlongationEndDate.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				final Datebox dateProlongationEndDate = new Datebox();
				dateProlongationEndDate.setPlaceholder("Отчисление: ");
				if(student.getProlongationenddate()!=null)
					dateProlongationEndDate.setValue(student.getProlongationenddate());
				//изменение даты отчисления
				dateProlongationEndDate.addEventListener("onChange", new EventListener() {
					public void onEvent(Event event) {
						student.setProlongationenddate(dateProlongationEndDate.getValue());
						while(LCFinishStudy.getChildren().size()>0) //ОЧИЩЕНИЕ				
							LCFinishStudy.removeChild(LCFinishStudy.getChildren().get(0));
						if(student.getProlongationenddate()==null)
							lbProlongationEndDate.setValue("Не указано");
						else
							lbProlongationEndDate.setValue(new SimpleDateFormat("dd.MM.yyyy").format(student.getProlongationenddate()));
						LCFinishStudy.appendChild(lbProlongationEndDate);
					}
				});	
				while(LCFinishStudy.getChildren().size()>0) //ОЧИЩЕНИЕ				
					LCFinishStudy.removeChild(LCFinishStudy.getChildren().get(0));
				LCFinishStudy.appendChild(dateProlongationEndDate);
			}});

		// при клике на label продление
		lbProlongationBeginDate.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				final Datebox dateProlongationBeginDate = new Datebox();
				dateProlongationBeginDate.setPlaceholder("Продление: ");
				if(student.getProlongationbegindate()!=null)
					dateProlongationBeginDate.setValue(student.getProlongationbegindate());
				//изменение даты продления
				dateProlongationBeginDate.addEventListener("onChange", new EventListener() {
					public void onEvent(Event event) {
						student.setProlongationbegindate(dateProlongationBeginDate.getValue());
						while(LCProlongationStudy.getChildren().size()>0) //ОЧИЩЕНИЕ 
							LCProlongationStudy.removeChild(LCProlongationStudy.getChildren().get(0));
						if(student.getProlongationbegindate()==null){
							student.setSessionProlongation(false);
							lbProlongationBeginDate.setValue("Не указано");
						}
						else{
							student.setSessionProlongation(true);
							lbProlongationBeginDate.setValue(new SimpleDateFormat("dd.MM.yyyy").format(student.getProlongationbegindate()));
						}
						LCProlongationStudy.appendChild(lbProlongationBeginDate);
					}
				});
				while(LCProlongationStudy.getChildren().size()>0) //ОЧИЩЕНИЕ				
					LCProlongationStudy.removeChild(LCProlongationStudy.getChildren().get(0));
				LCProlongationStudy.appendChild(dateProlongationBeginDate);
			}
		});

		LCFIO.appendChild(lbFIO);	
		LCScholarBegin.appendChild(lbScholarBegin);
		LCScholarEnd.appendChild(lbScholarEnd);
		LCFinishStudy.appendChild(lbProlongationEndDate);
		LCProlongationStudy.appendChild(lbProlongationBeginDate);


		li.appendChild(LCFIO);
		if(ord.getOrderSubType().getName().equals(Order.orderScholarship)){
			li.appendChild(LCScholarBegin);
			li.appendChild(LCScholarEnd);
		}
		if(ord.getOrderSubType().getName().equals(Order.orderTransfer)){
			li.appendChild(LCFinishStudy);
		}
		li.appendChild(LCProlongationStudy);

		//li.appendChild(CtrWest.ctrWestGlobal.createButtonMoveStudent(student, orRu, false)); удалить
		li.appendChild(CtrWest.ctrWestGlobal.createButtonMoveStudent(student, orRu, true));
		return li;
	}

	@Listen("onChange = #txtRuleNameOrder")
	public void nameOrder(){
		ord.setDescriptionSpec(txtRuleNameOrder.getText());
	}

	@Listen("onChange = #dateOfBeginOrder")
	public void dateOfBeginOrder(){
		ord.setDateOfBegin(dateOfBeginOrder.getValue());
	}

	@Listen("onChange = #dateOfEndOrder")
	public void dateOfEndOrder(){
		ord.setDateOfEnd(dateOfEndOrder.getValue());
		showTxtNumberOrder();
	}

	@Listen("onClick = #btnOrderWriteToday")
	public void OrderWriteToday(){
		CtrOrder.ord.setDateOfEnd(new java.util.Date());
		dateOfEndOrder.setValue(CtrOrder.ord.getDateOfEnd());
		showTxtNumberOrder();
	}

	void showTxtNumberOrder(){
		if(txtNumberOrder.getValue().equals("") || txtNumberOrder.getValue().equals("0")){
			txtNumberOrder.setVisible(true);
			txtNumberOrder.setValue("");
			Clients.showNotification("Укажите номер приказа");
		}
	}
	public static List<StudentSemesterStatus> getStudentsOfSection(Section section,Order or){
		List<StudentSemesterStatus> students = new ArrayList<StudentSemesterStatus>();
		for(int i=0;i<or.getOrderRules().size();i++){
			OrderRule orR=(OrderRule) or.getOrderRules().toArray()[i];
			if(orR.getSection().getId()==section.getId()){
				students.addAll(orR.getStudents());
			}
		}
		return students;
	}

	@Listen("onClick = #btnReCreateOrder")
	public void reCreateOrder(){
		//TODO: Сюда вставлять
		System.out.println("=Recreate");
		divideListAllStudent(listSSS,1);
	}

	@Listen("onClick = #btnSaveOrder")
	public void createOrder(){
		System.out.println("=Save");
		if(dateOfBeginOrder.getValue()!=null && !dateOfBeginOrder.getValue().equals("")){
			try {
				if(!txtNumberOrder.getValue().equals("")&&!txtNumberOrder.getValue().equals("0"))				
					CtrOrder.ord.setNumber(txtNumberOrder.getValue());
				if(!txtRuleDescriptionOrder.getValue().equals("")&&!txtRuleDescriptionOrder.getValue().equals("0"))
					CtrOrder.ord.setDescription(txtRuleDescriptionOrder.getValue());
				//TEST
				//				for(int i=0;i<CtrOrder.ord.getOrderRules().size();i++){
				//					if(printAll)System.out.println(">> DEsc("+i+")"+((OrderRule)CtrOrder.ord.getOrderRules().toArray()[i]).isPrint());
				//					//if(printAll)System.out.println(">> DEsc("+i+")"+((OrderRule)CtrOrder.ord.getOrderRules().toArray()[i]).getDescription());
				//					for (int j = 0; j < ((OrderRule) ord.getOrderRules().toArray()[i]).getStudents().size(); j++) {
				//						if(printAll)System.out.println("ctrorder1292 FFF "+((StudentSemesterStatus)((OrderRule) ord.getOrderRules().toArray()[i]).getStudents().toArray()[j]).getProlongationbegindate());
				//					}
				//				}
				//
				CtrOrder.ord = OrderDao.odDGlobal.update(CtrOrder.ord);
				listSSS=getListSSS();
				Clients.showNotification("Приказ обновлен");
			} catch (Exception e) {
				if(printAll)System.out.println("PLUHERROR Cannot update order ctrOrder str1117");
				e.printStackTrace();
				Clients.showNotification("ОШИБКА, приказ не обновлен. " + e.toString());
			}
		}else{
			Clients.showNotification("Необходимо указать дату создания приказа");
		}
	}

	public Hbox showDatebox(OrderRule orruIn, GroupSemester gsIn){
		final OrderRule orru=orruIn;
		final GroupSemester gs=gsIn;
		Hbox hl = new Hbox();
		hl.setAlign("center");
		Label lbWith = new Label();
		lbWith.setValue("c ");

		final Datebox dateStart = new Datebox();
		dateStart.setPlaceholder("Начало выплат");

		Label lbTill = new Label();
		lbTill.setValue("до ");

		final Datebox dateFinish = new Datebox();
		dateFinish.setPlaceholder("Окончание выплат");

		final Datebox prolongationbegindate = new Datebox(); //дата отчисления, в случае если долги не будут закрыты в указанный срок
		prolongationbegindate.setPlaceholder("Дата отчисления");

		Button btnOkT = new Button();
		btnOkT.setLabel("Применить");
		btnOkT.setVisible(false);
		btnOkT.setTooltiptext("Присвоить дату всем студентам группы/пункта");
		final Button btnOk = btnOkT;
		btnOk.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				if(dateStart.getValue()!=null && !dateStart.getText().equals(""))
					if(gs!=null){
					    //System.out.println(">1_2> "+dateStart.getValue());
						for (int j = 0; j < orru.getStudents().size(); j++) {
							for (int i = 0; i < gs.getStudents().size(); i++) {
								if(((StudentSemesterStatus)gs.getStudents().toArray()[i]).getId().equals(((StudentSemesterStatus)orru.getStudents().toArray()[j]).getId())){
									for (int k = 0; k < listSSS.size(); k++) {
										if(listSSS.get(k).getId().equals(((StudentSemesterStatus)orru.getStudents().toArray()[j]).getId())){
											//if(listSSS.get(k).getDateOfScholarshipBegin()==null){
											listSSS.get(k).setDateOfScholarshipBegin(dateStart.getValue());
											break;
											//}
										}
									}
								}
							}
						}
					}else{
					   //System.out.println(">1_1> "+listSSS.size());
						for (int i = 0; i < orru.getStudents().size(); i++) { 
							for (int k = 0; k < listSSS.size(); k++) {
								if(listSSS.get(k).getId().equals(((StudentSemesterStatus)orru.getStudents().toArray()[i]).getId())){
									//if(listSSS.get(k).getDateOfScholarshipBegin()==null){
									listSSS.get(k).setDateOfScholarshipBegin(dateStart.getValue());
									System.out.println("set "+listSSS.get(k).getDateOfScholarshipBegin());
									break;
									//}
								}
							}
						}
					}

				if(dateFinish.getValue()!=null && !dateFinish.getText().equals(""))
					if(gs!=null){
						//System.out.println(">2_1> "+dateFinish.getValue());
						for (int j = 0; j < orru.getStudents().size(); j++) {
							for (int i = 0; i < gs.getStudents().size(); i++) {
								if(((StudentSemesterStatus)gs.getStudents().toArray()[i]).getId().equals(((StudentSemesterStatus)orru.getStudents().toArray()[j]).getId())){
									for (int k = 0; k < listSSS.size(); k++) {
										if(listSSS.get(k).getId().equals(((StudentSemesterStatus)orru.getStudents().toArray()[j]).getId())){
											//if(listSSS.get(k).getDateOfScholarshipEnd()==null){
											listSSS.get(k).setDateOfScholarshipEnd(dateFinish.getValue());
											break;
											//}
										}
									}
								}
							}
						}
					}else{
						//System.out.println(">2_2> "+dateFinish.getValue());
						for (int i = 0; i < orru.getStudents().size(); i++) { 
							for (int k = 0; k < listSSS.size(); k++) {
								if(listSSS.get(k).getId()==((StudentSemesterStatus)orru.getStudents().toArray()[i]).getId()){
									//if(listSSS.get(k).getDateOfScholarshipEnd()==null){
									listSSS.get(k).setDateOfScholarshipEnd(dateFinish.getValue());
									break;
									//}
								}
							}
						}
					}

				if(prolongationbegindate.getValue()!=null && !prolongationbegindate.getText().equals(""))
					if(gs!=null){
						for (int j = 0; j < orru.getStudents().size(); j++) {
							for (int i = 0; i < gs.getStudents().size(); i++) {
								if(((StudentSemesterStatus)gs.getStudents().toArray()[i]).getId().equals(((StudentSemesterStatus)orru.getStudents().toArray()[j]).getId())){
									for (int k = 0; k < listSSS.size(); k++)	
									{
										if(listSSS.get(k).getId().equals(((StudentSemesterStatus)orru.getStudents().toArray()[j]).getId())){
											//if(listSSS.get(k).getProlongationenddate()==null){
											listSSS.get(k).setProlongationenddate(prolongationbegindate.getValue());
											break;
											//}
										}
									}
								}
							}
						}
					}else{
						for (int i = 0; i < orru.getStudents().size(); i++) { 
							for (int k = 0; k < listSSS.size(); k++) {
								if(listSSS.get(k).getId().equals(((StudentSemesterStatus)orru.getStudents().toArray()[i]).getId())){
									//if(listSSS.get(k).getProlongationenddate()==null){
									listSSS.get(k).setProlongationenddate(prolongationbegindate.getValue());
									break;
									//}
								}
							}
						}
					}
				divideToSection(listSSS,CtrOrder.ord,false,false,false);
				btnOk.setVisible(false);
			}
		}); 

		dateStart.addEventListener("onChange", new EventListener() {
			public void onEvent(Event event) {
				btnOk.setVisible(true);
			}
		});

		dateFinish.addEventListener("onChange", new EventListener() {
			public void onEvent(Event event) {
				btnOk.setVisible(true);
			}
		});

		prolongationbegindate.addEventListener("onChange", new EventListener() {
			public void onEvent(Event event) {
				btnOk.setVisible(true);
			}
		});

		if(ord.getOrderSubType().getName().equals(Order.orderScholarship)){
			hl.appendChild(lbWith);
			hl.appendChild(dateStart);
			hl.appendChild(lbTill);
			hl.appendChild(dateFinish);
		}
		if(ord.getOrderSubType().getName().equals(Order.orderTransfer)){
			hl.appendChild(prolongationbegindate); 
		}
		hl.appendChild(btnOk);
		return hl;
	}
}

/*	public Hlayout createHLstudentLine(String infoStudent, String id){
		if(printAll)System.out.println("student " + id);
		Hlayout studentLine = new Hlayout();
		Label lbStudent = new Label();
		lbStudent.setValue(infoStudent);
		Button btnStudentDelete = new Button();
		btnStudentDelete.setId("btnDelStudent"+id);
		btnStudentDelete.setLabel("Удалить хлеб");
		studentLine.appendChild(lbStudent);
		studentLine.appendChild(btnStudentDelete);
		return studentLine;
	}
 */