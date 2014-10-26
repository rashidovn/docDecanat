package dec.docDecanat.pageControllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
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
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import dec.docDecanat.data.dao.Link_note_student_semester_status_orderDAO;
import dec.docDecanat.data.dao.OrderDao;
import dec.docDecanat.data.dao.SectionDao;
import dec.docDecanat.data.dao.StudentSemesterStatusDao;
import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.data.entity.Rule;
import dec.docDecanat.data.entity.Section;
import dec.docDecanat.data.entity.Index;

public class CtrIndex extends SelectorComposer<Component>{

	@Wire
	Groupbox gbTableAllorder;

	public static List<Order> listOrder;

	OrderDao od= new OrderDao();
	//private Grid fillList;

	public static CtrIndex ctrInd = new CtrIndex();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		ctrInd = this;
		System.out.println("ctrindex str54 point0.1");
		listOrder = new ArrayList<Order>(od.getAll());
		System.out.println("ctrindex str54 point0.2");
		Collections.sort(listOrder);
		//listOrder.clear();
		//CtrWest.ctrWestGlobal.hideLB();
		createTable(listOrder);
	} 

	void createTable(List<Order> listTable){
		while(gbTableAllorder.getChildren().size()>0) //ОЧИЩЕНИЕ
			gbTableAllorder.removeChild(gbTableAllorder.getChildren().get(0));
		Listbox table = new Listbox();
		table.appendChild(createHead());
		if(listTable.size()>0){
			try {
				for (int o = 0; o < listTable.size(); o++) {
					table.appendChild(createOrder(listTable.get(o),o));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			table.setEmptyMessage("Приказов нет");
		}
		gbTableAllorder.appendChild(table);
	}

	Listhead createHead(){
		Listhead lh= new Listhead();

		Listheader lh1=new Listheader();
		lh1.setLabel("№ п/п");
		lh1.setWidth("60px");
		Listheader lh2=new Listheader();
		lh2.setLabel("Номер");
		lh2.setWidth("150px");
		//	Listheader lh3=new Listheader();
		//	lh3.setLabel("Подтип");
		Listheader lh4=new Listheader();
		lh4.setLabel("Дата создания");
		lh4.setWidth("200px");
		Listheader lh5=new Listheader();
		lh5.setLabel("Тип документа");
		Listheader lhDescription=new Listheader();
		lhDescription.setLabel("Описание");
		Listheader lh6=new Listheader();
		lh6.setLabel("Статус");
		Listheader lh7=new Listheader();
		lh7.setLabel("");
		lh7.setWidth("50px");
		Listheader lh8=new Listheader();
		lh8.setLabel("");
		lh8.setWidth("50px");
		Listheader lh9=new Listheader();
		lh9.setLabel("");
		lh9.setWidth("65px");
		lh.appendChild(lh1);
		lh.appendChild(lh2);
		//	lh.appendChild(lh3);
		lh.appendChild(lh4);
		lh.appendChild(lh5);
		lh.appendChild(lhDescription);
		lh.appendChild(lh6);
		lh.appendChild(lh7);
		lh.appendChild(lh8);
		lh.appendChild(lh9);
		return lh;
	}

	Listitem createOrder(Order curOr, int npp){
		final Order or = curOr;
		Listitem li = new Listitem();

		//номер п/п
		Listcell lc1 = new Listcell();
		lc1.appendChild(lb1(npp+1));

		//Номер приказа
		Listcell lc2 = new Listcell();
		Label lb2 = new Label();
		if(or.getNumber()!=null && !or.getNumber().equals("") && !or.getNumber().equals("0"))
			lb2.setValue(or.getNumber());
		else{
			lb2.setValue("Номер не указан");		
			lb2.setSclass("error");
			lc2.setImage("imgs/alert.png");
		}
		lc2.appendChild(lb2);
		/*
		//Подтип
		Listcell lc3 = new Listcell();
		Label lb3 = new Label();
		lb3.setValue(or.getOrderSubType().toString());
		lc3.appendChild(lb3);
		 */
		//Дата создания
		Listcell lc4 = new Listcell();
		Label lb4 = new Label();
		if(or.getDateOfEnd()!=null){
			lb4.setValue(new SimpleDateFormat("dd.MM.yyyy").format(or.getDateOfEnd()));			
			lb4.setSclass("correct");
			lc4.setImage("imgs/okCLR.png");			
		}else{
			if(or.getDateOfBegin()!=null){
				lb4.setValue(new SimpleDateFormat("dd.MM.yyyy").format(or.getDateOfBegin()));
				lc4.setImage("imgs/waitalt2.png");
			}
			else{
				lb4.setValue("Дата не указана");			
				lb4.setSclass("error");
				lc4.setImage("imgs/alert.png");
			}
		}
		lc4.appendChild(lb4);

		//Название
		Listcell lc5 = new Listcell();
		Label lb5 = new Label();
		lb5.setValue(or.getOrderSubType().getOrderType().getName() + ", " + or.getOrderSubType().getName());
		lc5.appendChild(lb5);
		//Описание
		Listcell lcDescription = new Listcell();
		Label lbDescription = new Label();
		lbDescription.setValue(or.getDescriptionSpec());
		lcDescription.appendChild(lbDescription);
		//Статус
		Listcell lc6 = new Listcell();
		Label lb6 = new Label();
		lb6.setValue("Данные из alfresco");
		lc6.appendChild(lb6);

		//Показать приказ
		Listcell lc7 = new Listcell();
		Button btnRead = new Button();
		btnRead.setTooltiptext("Просмотр");
		btnRead.setImage("imgs/read.png");
		btnRead.addEventListener("onClick", new EventListener(){
			public void onEvent(Event event) throws Exception {
				CtrOrder.ord = or;
				Window window = null;
				if (CtrOrder.ord.getOrderSubType().getName()
						.equals(Order.orderScholarship)) {
					window = (Window)Executions.createComponents(
							"report.zul", null, null);
				}
				if (CtrOrder.ord.getOrderSubType().getName()
						.equals(Order.orderTransfer)) {
					window = (Window)Executions.createComponents(
							"deductionReport.zul", null, null);
				}
				window.doModal();
			}
		});
		lc7.appendChild(btnRead);


		//Редактировать
		Listcell lc8 = new Listcell();
		Button btnEdit = new Button();
		btnEdit.setTooltiptext("Редактировать");
		btnEdit.setImage("imgs/edit.png");
		btnEdit.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				System.out.println("Приказ который отправляем в order из index "+or+" ctrindex 161");
				CtrOrder.ord = or;
				if(or.getOrderRules().size()<=0){
					Clients.showNotification("Приказ был сохраненн некорректно, его необходимо удалить");
				}
				else{
					System.out.println("1 "+ ((OrderRule) or.getOrderRules().toArray()[0]).getSection());
					Rule.ruChoose = ((OrderRule) or.getOrderRules().toArray()[0]).getSection().getRule();
					System.out.println("Количество секций, которое мы передаем в order"+Rule.ruChoose.getSections().size()+"ctrindex 164");
					//Rule.ruChoose.setSections(((OrderRule) or.getOrderRules().toArray()[0]).getSection().getRule().getSections());
					System.out.println("Правило, которое мы отаправили "+Rule.ruChoose+" ctrIndex 164");
					System.out.println("Пункт, который мы отаправили "+((Section)Rule.ruChoose.getSections().toArray()[0])+" ctrIndex 166");
					Executions.sendRedirect("order.zul");
				}   			
			}
		});
		lc8.appendChild(btnEdit);   

		//Удалить
		Listcell lc9 = new Listcell();
		Button btnDel = new Button();
		btnDel.setVisible(false);
		btnDel.setTooltiptext("Удалить");
		btnDel.setImage("imgs/del.png");
		btnDel.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				try {
					System.out.println("ctrindex 231 order = " + or);
					Link_note_student_semester_status_orderDAO notesDAO = new Link_note_student_semester_status_orderDAO();
					notesDAO.deleteNotesFromOrder(or);
					OrderDao od2= new OrderDao();
					listOrder.remove(or);
					od2.delete(or);
					createTable(listOrder);
				} catch (Exception e) {
					e.getStackTrace();
					System.out.println("Не удалось удалить приказ: "+e.getMessage());
					Clients.showNotification("Обновите страницу, функция в доработке");
				}
			}
		});
		lc9.appendChild(btnDel);



		li.appendChild(lc1);
		li.appendChild(lc2);
		// li.appendChild(lc3);
		li.appendChild(lc4);
		li.appendChild(lc5);
		li.appendChild(lcDescription);
		li.appendChild(lc6);
		li.appendChild(lc7);
		li.appendChild(lc8);
		li.appendChild(lc9);
		return li;
	}

	Label lb1(int npp){
		Label lb1 = new Label();
		lb1.setValue(npp+"");
		return lb1;
	}

}