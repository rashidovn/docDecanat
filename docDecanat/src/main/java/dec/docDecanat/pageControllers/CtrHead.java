package dec.docDecanat.pageControllers;

import java.lang.ProcessBuilder.Redirect;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import dec.docDecanat.data.dao.GroupDao;
import dec.docDecanat.data.dao.OrderRuleDao;
import dec.docDecanat.data.dao.OrderSubTypeDao;
import dec.docDecanat.data.dao.OrderTypeDao;
import dec.docDecanat.data.dao.RuleDao;
import dec.docDecanat.data.entity.Group;
import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.data.entity.OrderSubType;
import dec.docDecanat.data.entity.OrderType;
import dec.docDecanat.data.entity.Rule;

public class CtrHead extends SelectorComposer<Component> {

	public boolean ShowCreateOrder = true;
	public boolean ShowFilter = true;

	@Wire
	Hlayout hlCreadOrder;

	@Wire
	Button btnGoToOrder;

	//Это добавил Макс
	@Wire
	Button btnSearchStudent;
	
	@Wire
	Button btnFilter;

	@Wire
	Button btnListOrder;

	@Wire
	Button btnAllSetting;

	@Wire
	Button btnAdd;

	@Wire
	Label lbErrorWest;

	@Wire
	Hlayout hlFilter;
	@Wire
	Combobox cmbNewTypeDoc;

	/* не удалять
	@Wire
	Combobox cmbFilterTypeDoc;
	 */
	@Wire
	Combobox cmbNewSubTypeDoc;


	@Wire
	Combobox cmbFilterSubTypeDoc;

	@Wire
	Combobox cmbChooseRule;

	@Wire
	Datebox dateFilterBegin;

	@Wire
	Textbox txtFilterNumberOrder;
	OrderSubType orSubType;
	static boolean flagChooseRule = false;




	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		hideElements();
		//Подсказки
		btnAdd.setTooltiptext("Добавить документ");
		btnFilter.setTooltiptext("Поиск документа");
		btnAllSetting.setTooltiptext("Настройки");
		btnListOrder.setTooltiptext("Список документов");
		btnGoToOrder.setTooltiptext("Создать");
		//Добавил Макс
		btnSearchStudent.setVisible(true);
		//cmbNewSubTypeDoc.setModel(addTypeDoc());
		cmbNewTypeDoc.setModel(addTypeDoc());
		cmbNewTypeDoc.setVisible(false);
		cmbNewSubTypeDoc.setModel(OrderSubType.loadCmb());
		cmbFilterSubTypeDoc.setModel(OrderSubType.loadCmb());
		/*не удалять	
		cmbFilterTypeDoc.setModel(addTypeDoc());
		cmbFilterSubTypeDoc.setVisible(false);
		 */
		Executions.getCurrent().getDesktop().setAttribute("CurrentCmbAllRuleOfHead", this);		
		if(getPage().getRequestPath().equals("/index.zul"))
			btnFilter.setVisible(true);
		else
			btnFilter.setVisible(false);		
		if(getPage().getRequestPath().equals("/setting.zul"))
			btnAdd.setVisible(false);
		else
			btnAdd.setVisible(true);
		showNameRule();
	}

	void showNameRule(){
		if(Rule.ruChoose!=null){
			lbErrorWest.setValue(Rule.ruChoose.getOST().getOrderType().getName()+", "+
					Rule.ruChoose.getOST().getName()+", "+
					Rule.ruChoose.getName()); 

			lbErrorWest.setSclass("lblHeadRule");
		}
		else{
			lbErrorWest.setValue("");
		}
	}
	/*//НЕ ТРОГАТЬ!!!
	function void Pater(noster)
	{
	    for (Qui es in caelis){
	        sanctificetur(nomen);
	        tuum++;
	    }
	}
	 */


	ListModel<OrderType> addTypeDoc(){
		List<OrderType> listOrderType;
		try {
			listOrderType = new ArrayList<OrderType>(OrderTypeDao.OTDglobal.getAll());
			Collections.sort(listOrderType);
			ListModel<OrderType> listOrderTypeCmb = new ListModelList<OrderType>(listOrderType);
			return listOrderTypeCmb;
		} catch (Exception e) {
			Clients.showNotification("Не удалось загрузить список");
		}
		return null;
	}

	void hideElements(){
		btnGoToOrder.setVisible(false);

		hlFilter.setVisible(false);
		cmbChooseRule.setVisible(false);
		hlCreadOrder.setVisible(false) ;
		//cmbNewSubTypeDoc.setVisible(false);	// не удалять	
	}

	//Добавил Макс
	@Listen("onClick = #btnSearchStudent")
	public void openWindow(){
		Window window = (Window)Executions.createComponents("searchStudent.zul", null, null);
		window.doModal();
	}

	@Listen("onClick = #btnAdd")
	public void showBtnAdd() {
		if(!ShowFilter)
			showBtnFilter();
		hlCreadOrder.setVisible(ShowCreateOrder) ;
		ShowCreateOrder=!ShowCreateOrder;  
	}

	@Listen("onClick = #btnListOrder")
	public void goToListOrder() {
		lbErrorWest.setValue("");
		Rule.ruChoose=null;
		Executions.sendRedirect("index.zul"); 
	}


	@Listen("onChange = #cmbNewTypeDoc")
	public void showStudent()
	{
		if(cmbNewTypeDoc.getSelectedItem() != null){
			try {				
				cmbNewSubTypeDoc.setModel(OrderSubType.loadCmb());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cmbNewSubTypeDoc.setVisible(true);
	}/* не удалять*/

	@Listen("onChange = #cmbNewSubTypeDoc")
	public void showCmbChooseRule()
	{
		cmbChooseRule.setVisible(true);
		cmbChooseRule.setModel(Rule.loadCmb((OrderSubType) cmbNewSubTypeDoc.getSelectedItem().getValue())); 
	}

	@Listen("onChange = #cmbChooseRule")
	public void showBtnCreate()
	{
		flagChooseRule=true;
		btnGoToOrder.setVisible(true);
	}

	@Listen("onClick = #btnGoToOrder")
	public void chooseOrder(){

		if(cmbNewSubTypeDoc.getValue()!=null)
		{	
			//создание нового приказа
			Rule.ruChoose = cmbChooseRule.getSelectedItem().getValue();
			System.out.println(">>PARADOX head "+Rule.ruChoose.getSections().size());
			CtrOrder.ord = null;
			CtrRulesMenu.flagNewRules = false;
			Window window = (Window)Executions.createComponents(
					"rules.zul", null, null);
			window.doModal();
		}
		else
			Clients.showNotification("Выберите подтип документа");
		/* не удалять
		 	if (cmbNewTypeDoc.getValue().equals("Приказ")){	}
		else Clients.showNotification("Выберите тип документа");
		 */

	}


	@Listen("onClick = #btnFilter")
	public void showBtnFilter(){
		if(!ShowCreateOrder)
			showBtnAdd();
		hlFilter.setVisible(ShowFilter);
		ShowFilter=!ShowFilter; 
	}



	void filter(){
		//проверить все элементы
		//отсортировать по всем элементам
		List<Order> orList = new ArrayList<Order>(CtrIndex.listOrder);
		List<Order> tableList = new ArrayList<Order>();	

		//String orDate="";
		String dateF = "";
		if(dateFilterBegin.getValue()!=null)
			dateF = new SimpleDateFormat("dd.MM.yyyy").format(dateFilterBegin.getValue());
		String numberF = txtFilterNumberOrder.getValue();
		/* не удалять
		OrderType typeF = null;		 
		if(cmbFilterTypeDoc.getSelectedItem()!=null)
			typeF = cmbFilterTypeDoc.getSelectedItem().getValue();
		 */
		OrderSubType subTypeF = null;
		if(cmbFilterSubTypeDoc.getSelectedItem()!=null)
			subTypeF = cmbFilterSubTypeDoc.getSelectedItem().getValue();

		for (int i = 0; i < orList.size(); i++) {
			boolean flagCheck = true;
			if(numberF!=null){
				if(!numberF.equals("")){
					if(orList.get(i).getNumber()!=null){
						if(!orList.get(i).getNumber().equals(numberF)){
							flagCheck = false;
						}
					}else{
						flagCheck = false;
					}
				}
			}
			/* не удалять
			if(typeF!=null){
				if(!typeF.equals("")){
					if(!orList.get(i).getOrderSubType().getOrderType().getName().equals(typeF.getName())){
						flagCheck = false;
					}
				}
			}
			 */
			if(subTypeF!=null){
				if(!subTypeF.equals("")){
					if(!orList.get(i).getOrderSubType().getName().equals(subTypeF.getName())){
						flagCheck = false;
					}
				}
			}
			String orDate= "";
			if(orList.get(i).getDateOfBegin()!=null)
				orDate = new SimpleDateFormat("dd.MM.yyyy").format(orList.get(i).getDateOfBegin());
			if(dateF!=null){
				if(!dateF.equals("")){
					if(!orDate.equals(dateF)){
						flagCheck = false;
					}
				}
			}

			if(flagCheck){
				tableList.add(orList.get(i));
			}
		}

		CtrIndex.ctrInd.createTable(tableList);
	}

	@Listen("onChange = #txtFilterNumberOrder")
	public void filterNumberBegin(){
		filter();
	}	

	/* не удалять
	@Listen("onChange = #cmbFilterTypeDoc")
	public void showcmbFilterSubTypeDoc()
	{
		cmbFilterSubTypeDoc.setVisible(true);
		cmbFilterSubTypeDoc.setModel(OrderSubType.loadCmb());		
		filter();
	}
	 */

	@Listen("onChange = #cmbFilterSubTypeDoc")
	public void filterSubTypeDocBegin(){		
		filter();
	}	

	@Listen("onChange = #dateFilterBegin")
	public void filterDateBegin(){		
		filter();
	}


	@Listen("onClick = #btnAllSetting")
	public void showAllSetting() {
		Rule.ruChoose=null;
		Executions.sendRedirect("setting.zul"); 
	}




}
