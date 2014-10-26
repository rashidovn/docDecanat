package dec.docDecanat.pageControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.lang.Exceptions;
import org.zkoss.zhtml.Br;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import dec.docDecanat.ItemRender.SectionCmbRender;
import dec.docDecanat.data.dao.Link_note_student_semester_status_orderDAO;
import dec.docDecanat.data.entity.Link_note_student_semester_status_order;
import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.data.entity.Rule;
import dec.docDecanat.data.entity.Section;
import dec.docDecanat.data.entity.StudentSemesterStatus;

public class CtrCmbChooseSection extends SelectorComposer<Component> {

	@Wire
	Vlayout vlMoveStudent; // хранит все содержимое формы
	@Wire
	Window winMoveStudent;	//окно для перемещения студента по приказу и за его границу

	static StudentSemesterStatus curStud = new StudentSemesterStatus(); //перемещаемый студент
	static OrderRule orRuFrom = new OrderRule();//из какой секции он пришел
	static boolean flagDelStudent;//удалять студента или переместить
	Link_note_student_semester_status_order linkNote = new Link_note_student_semester_status_order(); //объект причины 
	Link_note_student_semester_status_orderDAO noteDAO = new Link_note_student_semester_status_orderDAO();

	//Rule ruChoose = new Rule();
	static Combobox cmb = new Combobox();
	CtrOrder CtrOrd;
	final Textbox txtNote = new Textbox();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		loadForm();
		CtrOrd = (CtrOrder) Executions.getCurrent().getDesktop().getAttribute("CurrentCtrOrder");
	}

	void loadForm(){
		txtNote.setPlaceholder("Укажите причину перемещения");
		txtNote.setMultiline(true);
		txtNote.setHflex("1");
		txtNote.setHeight("100px");
		txtNote.addEventListener("onChange", new EventListener() {
			public void onEvent(Event event) {	
				linkNote.setNote(txtNote.getText());
			}
		});
		Label lb = new Label();
		if(CtrCmbChooseSection.flagDelStudent){
			lb.setValue("Удалить студента: "+ CtrCmbChooseSection.curStud.getStudentCard().getHumanface().getFIO()+" ("+  
					CtrCmbChooseSection.curStud.getGroupSemester().getGroup().getName() +")?");
			lb.setStyle("error");
		}else{
			lb.setValue("Переместить студента: "+ CtrCmbChooseSection.curStud.getStudentCard().getHumanface().getFIO()+" ("+  
					CtrCmbChooseSection.curStud.getGroupSemester().getGroup().getName() +")");
			List<OrderRule> listOrRuTemp = new ArrayList<OrderRule>(CtrOrder.ord.getOrderRules());
			if(CtrCmbChooseSection.orRuFrom!=null){
				for (int i = 0; i < listOrRuTemp.size(); i++) {
					if(listOrRuTemp.get(i).getSection().getName() == CtrCmbChooseSection.orRuFrom.getSection().getName()){
						listOrRuTemp.remove(i);
					}
				}
			}
			Collections.sort(listOrRuTemp);
			ListModel<OrderRule> lmcmb = new ListModelList<OrderRule>(listOrRuTemp);
			cmb.setHflex("1");
			cmb.setPlaceholder("Выберите пункт");
			cmb.setModel(lmcmb);
			cmb.setItemRenderer(new SectionCmbRender());
		}
		vlMoveStudent.appendChild(lb);
		if(!CtrCmbChooseSection.flagDelStudent){
			vlMoveStudent.appendChild(cmb);
		}
		vlMoveStudent.appendChild(new Br());
		vlMoveStudent.appendChild(txtNote);
		vlMoveStudent.appendChild(new Br());
	}
	
	public static void trasportStudent(StudentSemesterStatus sss, OrderRule or, boolean flagDelSt){
		CtrCmbChooseSection.curStud = sss;
		CtrCmbChooseSection.orRuFrom = or;
		CtrCmbChooseSection.flagDelStudent = flagDelSt;

		if(Path.getComponent("/winMoveStudent")!=null){
			Path.getComponent("/winMoveStudent").detach();
		}
		Window window = (Window)Executions.createComponents(
				"createComponent/cmbChooseSection.zul", null, null);
		window.doModal();
	}

	@Listen("onClick = #btnMoveStudent")
	public void save()	throws Exception {//фиксация перемещения
		if (cmb.getText()=="" && !CtrCmbChooseSection.flagDelStudent){
			Clients.showNotification("Необходимо выбрать пункт для перемещения студента");
		}else{
			//оставить, защита от дурака, требует указания причины переноса.
//			if(txtNote.getText()=="")
//			{
//				if(CtrCmbChooseSection.flagDelStudent)
//					Clients.showNotification("Необходимо указать причину удаления студента");
//				else
//					Clients.showNotification("Необходимо указать причину перемещения студента");
//			}else{
				Set<OrderRule> orru = CtrOrd.ord.getOrderRules();//???
				if(!CtrCmbChooseSection.flagDelStudent){//если студента не надо удалять, то перемещаем в пункт, иначе просто удаляем
					for (int i = 0; i < orru.size(); i++) {
						if(((OrderRule)orru.toArray()[i]).getSection().getName().equals(cmb.getText())){
							if(CtrOrder.ord.getOrderSubType().getName().equals(Order.orderTransfer)){//переводной
								CtrCmbChooseSection.curStud.setOrderScholarship(true);
							}
							if(CtrOrder.ord.getOrderSubType().getName().equals(Order.orderScholarship)){//стипендиальный	
								CtrCmbChooseSection.curStud.setOrderTransfer(true);
							}
							((OrderRule)orru.toArray()[i]).getStudents().add(CtrCmbChooseSection.curStud);
						}
					}
				}else{
					if(CtrOrder.ord.getOrderSubType().getName().equals(Order.orderTransfer)){//переводной						
						CtrCmbChooseSection.curStud.setOrderScholarship(false);
					}
					if(CtrOrder.ord.getOrderSubType().getName().equals(Order.orderScholarship)){//стипендиальный	
						CtrCmbChooseSection.curStud.setOrderTransfer(false);
					}
				}
				if(CtrCmbChooseSection.orRuFrom ==null){ //если запрос пришел не из пункта,то
					CtrOrd.listWest.remove(CtrCmbChooseSection.curStud); // удаляем этого студента из списка west
					CtrWest.ctrWestGlobal.addSSSListItemsWest(CtrOrd.listWest, false); //перерисовываем список west
				}
				else{
					for (int i = 0; i < orru.size(); i++) {//есди запрос пришел  из пункта
						if(((OrderRule)orru.toArray()[i]).getSection().getName().equals(CtrCmbChooseSection.orRuFrom.getSection().getName())) //ищем нужный нам пункт
							((OrderRule)orru.toArray()[i]).getStudents().remove(CtrCmbChooseSection.curStud);//удаляем его из пункта, откуда пришел			
					}
					CtrOrd.listWest.add(CtrCmbChooseSection.curStud);
					CtrWest.ctrWestGlobal.addSSSListItemsWest(CtrOrd.listWest, false);
				}
				savepoint();
			//}
		}
	}

	void savepoint(){
		if(CtrCmbChooseSection.flagDelStudent)
			linkNote.setOrruFinish(null);
		else
			linkNote.setOrruFinish((OrderRule) cmb.getSelectedItem().getValue());
		linkNote.setDateOfNotes(new java.util.Date());
		linkNote.setOrruStart(orRuFrom);
		linkNote.setSss(curStud);
		linkNote.setOrder(CtrOrder.ord);
		curStud.getList_notes().add(linkNote);
		try {
			//TODO Закоментировать обратно в случае глобального
			linkNote=noteDAO.create(linkNote);
			winMoveStudent.detach();
			CtrOrd.divideToSection(CtrOrder.listSSS, CtrOrder.ord, false,false,false);
			if(CtrCmbChooseSection.flagDelStudent){
				Clients.showNotification("Студент: " + CtrCmbChooseSection.curStud.getStudentCard().getHumanface().getFIO() +" удален из приказа");				
			}else{
				Clients.showNotification(CtrCmbChooseSection.curStud.getStudentCard().getHumanface().getFIO() +" переместили в пункт " + cmb.getText());

			}
			cmb.setValue("");
		} catch (Exception e) {
			e.printStackTrace();	
			Clients.showNotification("Произошла ошибка, действие не выполнено");

		}
	}


	@Listen("onClick = #btnCancelMoveStudent")
	public void cancel()	throws Exception {
		cmb.setValue("");
		winMoveStudent.detach();
	}


}
