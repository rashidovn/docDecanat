package dec.docDecanat.pageControllers;
import java.util.ArrayList;
import java.util.Collections;
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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import dec.docDecanat.data.dao.RuleDao;
import dec.docDecanat.data.dao.SectionDao;
import dec.docDecanat.data.entity.Employee;
import dec.docDecanat.data.entity.Link_rule_employee;
import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.Rule;
import dec.docDecanat.data.entity.Section;


public class CtrRules extends SelectorComposer<Component>{
	@Wire
	Button btnSaveRule; //кнопка сохранения правила (сохранять в бд)

	@Wire
	Button btnAddSection;	//кнопка вызова формы, для создания нового пункта
	@Wire
	Button btnAddPerson;	//кнопка вызова формы, для добавления сотрудника, для подписи праказов, созданных по данному правилу

	@Wire
	Button btnCancelSaveRule;	//кнопка отмены 

	@Wire
	Textbox txtRuleName;		//название правило
	@Wire
	Textbox txtRuleDescription;		//описание приказа

	@Wire
	Label lbPersons;

	@Wire
	Window winRul;				//Ссылка на текующее окно
	@Wire
	Vlayout vlSection;			//хранит в себе динамическое создание всех пунктов
	@Wire
	Vlayout vlPersonal;			//хранит в себе динамическое создание всех персон	

	@Wire
	Checkbox chCommonPersonal;		//маяк общий/индивидуальный приказ

	Section secChoose = new Section();//должна искоренить статик
	Link_rule_employee lreChoose = new Link_rule_employee();
	Rule ruChoose = new Rule();//должна искоренить статик

	CtrRulesMenu ctrRuleMenu = new CtrRulesMenu(); //ссылка на предыдущее окно необходимо, чтобы в RuleMenu обновить список правил, с учетом изменений, которые тут произойдут(редактриование/добавление новых пунктов(Section))

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		btnAddSection.setTooltiptext("Добавить пункт");
		ctrRuleMenu = (CtrRulesMenu) Executions.getCurrent().getDesktop().getAttribute("CurrentCmbAllRule"); 
		Executions.getCurrent().getDesktop().setAttribute("CurrentRuleWin", this);

		//не удалять, отключено, чтобы можно было настроить, пока не запустили систему
		//		btnAddSection.setVisible(CtrRulesMenu.flagNewRules);
		//		btnAddPerson.setVisible(CtrRulesMenu.flagNewRules);

		btnAddSection.setVisible(true);
		btnAddPerson.setVisible(true);
		lbPersons.setVisible(true);

		System.out.println("ctrrules str80 path:"+getPage().getRequestPath());
		if (!getPage().getRequestPath().equals("/setting.zul")){ //действие при создании приказа
			btnAddSection.setVisible(false);
			btnSaveRule.setVisible(false);
			btnCancelSaveRule.setVisible(false);
			btnAddPerson.setVisible(false);
			vlPersonal.setVisible(false);
			lbPersons.setVisible(false);
			ruChoose = Rule.ruChoose;
		}else{ //действие при настройке правил
			ruChoose = ctrRuleMenu.ruChoose; //получаем выбранное или новое пустое правило
		}
		//загружаем список секций, либо создаем пустой новый
		if(CtrRulesMenu.flagNewRules){
			//действия при создании правила
			System.out.println("PLUHNOTE 2.Create new section ctrRules str 98");
			ruChoose.setSections(new HashSet<Section>());
		}
		else {
			//действия при редактировании правила
			txtRuleName.setValue(ruChoose.getName());
			txtRuleDescription.setValue(ruChoose.getDescription());
			showAll();
		}
		if (ruChoose.isCommonPersonal()==null){
			ruChoose.setCommonPersonal(false);
		}
		chCommonPersonal.setChecked(ruChoose.isCommonPersonal()); //TODO: проверить
	}	


	public void showAll(){
		List<Section> listSec = new ArrayList<Section>(ruChoose.getSections());
		for (int i = 0; i < listSec.size(); i++){
			if(listSec.get(i).getLayout()==null){ //проверка на наличие позиции пункта, если нет, то назначают позицию
				int next=1;
				for (int j = 0; j < listSec.size(); j++) {
					if (listSec.get(j).getLayout()!=null)
						next++;
				}
				listSec.get(i).setLayout(next);
			}	
			for (int j = 0; j < listSec.size(); j++) {
				if(listSec.get(i).getLayout()==listSec.get(j).getLayout()&&listSec.get(i).getId()!=listSec.get(j).getId()){
					listSec.get(i).setLayout(listSec.get(i).getLayout()+1);
				}
			}			
		}
		Collections.sort(listSec);

		List<Link_rule_employee> listlre = new ArrayList<Link_rule_employee>(ruChoose.getListLINK());
		for (int i = 0; i < listlre.size(); i++){
			if(listlre.get(i).getPosition()==null){ //проверка на наличие позиции персоны, если нет, то назначают позицию
				int next=1;
				for (int j = 0; j < listlre.size(); j++) {
					if (listlre.get(j).getPosition()!=null)
						next++;
				}
				listlre.get(i).setPosition(next);
			}	
			for (int j = 0; j < listlre.size(); j++) {
				if(listlre.get(i).getPosition() == listlre.get(j).getPosition()
						&& listlre.get(i).getId()!= listlre.get(j).getId() ){
					listlre.get(i).setPosition(listlre.get(i).getPosition()+1);
				}
			}			
		}
		Collections.sort(listlre);

		while(vlSection.getChildren().size()>0)				
			vlSection.removeChild(vlSection.getChildren().get(0));

		//определяем это создание приказа или редактирование правила
		if (!getPage().getRequestPath().equals("/setting.zul")){//создание приказа
			if(listSec.size()<1){// проверка на наличие пунктов
				Label lb = new Label();
				lb.setValue("Cписок правил пуст");
				vlSection.appendChild(lb);
			}else{ //действие при создании приказа
				chCommonPersonal.setVisible(false); //TODO: проверить
				for (int i = 0; i < listSec.size(); i++) {
					Section sec = new Section();
					sec = listSec.get(i);
					vlSection.appendChild(showSectionCheck(sec));//to print or not to print	
				}		
				Button btn = new Button();
				btn.setLabel("Создать");
				btn.addEventListener("onClick", new EventListener() {
					public void onEvent(Event event) {
						Executions.sendRedirect("order.zul");
					}
				});
				vlSection.appendChild(btn);
			}
		}else{ //действия, если мы в настройках правил
			Listbox tableSection=new Listbox();
			loadSections(listSec, tableSection);
			Listbox tablePerson=new Listbox();
			loadPersons(ruChoose.getListLINK(), tablePerson );
		}
	}

	void loadSections(List<Section> listSec, Listbox tableSection){
		for (int i = 0; i < listSec.size(); i++) {
			Section sec = new Section();
			sec = listSec.get(i);
			//если настраиваем правило, парсим данный в зависимости от приказа
			if(ctrRuleMenu.OST.getName().equals(Order.orderScholarship)){
				sec.setSecParameters(sec.parseParametersScholarship());
			}
			if(ctrRuleMenu.OST.getName().equals(Order.orderTransfer)){
				sec.setSecParametersTransfer(sec.parseParametersTransfer());
			}
			//отображаем список пунктов
			tableSection.appendChild(showSection(sec));
		}
		vlSection.appendChild(tableSection);
	}

	void loadPersons(List<Link_rule_employee> listPers, Listbox tablePerson){ 
		while(vlPersonal.getChildren().size()>0)				
			vlPersonal.removeChild(vlPersonal.getChildren().get(0));
		Collections.sort(listPers);
		if(listPers.size()>0){
			for (int i = 0; i < listPers.size(); i++) { //количество персон				
				Link_rule_employee pers = new Link_rule_employee();
				pers = listPers.get(i);
				tablePerson.appendChild(showPersonal(pers));
			}
		}else{
			Listitem li=new Listitem();
			Listcell lc1=new Listcell();
			Label lb = new Label();
			lb.setValue("Список персон не указан");
			lc1.appendChild(lb);
			li.appendChild(lc1);
			tablePerson.appendChild(li);
		}
		vlPersonal.appendChild(tablePerson);		
	}


	@Listen("onClick = #btnSaveRule")   	//сохранить правило
	public void save()	throws Exception {
		if(txtRuleName.getValue().equals("")){
			Clients.showNotification("Необходимо указать название правилу");
		}
		else{
			saveParametr();
			winRul.detach();    		
		}
	}

	@Listen("onChange = #txtRuleDescription") //редактирование правила
	public void editDescription() throws Exception {
		ruChoose.setDescription(txtRuleDescription.getText());
	}

	void saveParametr()	{
		ruChoose.setName(txtRuleName.getValue()); //присвоение название правилу
		ruChoose.setDescription(txtRuleDescription.getValue());
		if(CtrRulesMenu.flagNewRules){
			try {
				ruChoose=RuleDao.ruDGlobal.create(ruChoose);
			}
			catch (Exception e) {
				System.out.println("PLUH ERROR Cannot create rule ctrrules str 244" + e.getMessage());
				Clients.showNotification("ОШИБКА: Правило не сохранено");
			}
			Clients.showNotification("Правило добавлено");
		}
		else{
			try {
				ruChoose=RuleDao.ruDGlobal.update(ruChoose);
				System.out.println(">>PARADOX rule "+ruChoose.getSections().size());
			} catch (Exception e) {
				System.out.println("PLUH ERROR Cannot update rule ctrrules str 253" + e.getMessage());
				Clients.showNotification("ОШИБКА: Правило не обновлено");
			}
			Clients.showNotification("Правило обновлено");
		}
		ctrRuleMenu.loadCmb();
		//ctrRuleMenu.cmbChooseRuleMenu.setModel(Rule.loadCmb(ruChoose.getOST()));   	
		ctrRuleMenu.cmbChooseRuleMenu.setText("");
		ctrRuleMenu.cmbSubTypeOrderEditRule.setText("");
	}

	@Listen("onClick = #chCommonPersonal")
	public void editChCommonPersonal(){
		ruChoose.setCommonPersonal(chCommonPersonal.isChecked());
	}

	@Listen("onClick = #btnCancelSaveRule")
	public void cancel()	throws Exception {
		winRul.detach();
	}


	// добавление пункта
	@Listen("onClick = #btnAddSection")
	public void addSection()	{
		Section.flagNewSection=true;
		Window window = (Window)Executions.createComponents("rulesSection.zul", null, null);
		window.doModal();

	}	
	@Listen("onClick = #btnAddPerson")
	public void addPerson()	{
		CtrRulesPersons.flagNewPerson=true;
		//Executions.getCurrent().getDesktop().setAttribute("CurrentRuleWin", this);
		Window window = (Window)Executions.createComponents("rulesPersons.zul", null, null);
		window.doModal();

	}

	public  Listitem  showSection(final Section sec){

		//Hlayout hlNewSection = new Hlayout();

		Listitem li=new Listitem();

		Listcell lc1=new Listcell();
		Hlayout hlName = new Hlayout();
		Vlayout vlBtn = new Vlayout();
		Button btnUp = new Button();
		btnUp.setImage("imgs/upMini.png");
		btnUp.setTooltiptext("На порядок выше");    	
		btnUp.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				upDownSection(new ArrayList<Section>(ruChoose.getSections()), sec.getLayout(), sec.getLayout() - 1);
				showAll();
			}
		});


		Button btnDown = new Button();
		btnDown.setImage("imgs/downMini.png");
		btnDown.setTooltiptext("На порядок ниже");  	
		btnDown.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				upDownSection(new ArrayList<Section>(ruChoose.getSections()), sec.getLayout(), sec.getLayout() + 1);
				showAll();
			}
		});


		Label lbNameSection = new Label();
		lbNameSection.setValue(sec.getLayout()+"."+ sec.getName());

		vlBtn.appendChild(btnUp);
		vlBtn.appendChild(btnDown);
		hlName.appendChild(vlBtn);
		hlName.appendChild(lbNameSection);
		lc1.appendChild(hlName);
		li.appendChild(lc1);
		//открыть if(CtrRulesMenu.flagNewRules){
		Listcell lc2=new Listcell();
		lc2.setStyle("text-align: right");

		Hlayout hlBtnEdit = new Hlayout();
		//кнопка редактирования
		Button btnEdit = new Button();    	
		btnEdit.setImage("imgs/edit.png");
		//btnEdit.setClass("btnEditSection");
		btnEdit.setTooltiptext("Редактирвать пункт");

		final CtrRules win = this;
		btnEdit.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {    	    	
				Section.flagNewSection = false; //СМЕНА ФЛАГА
				Executions.getCurrent().getDesktop().setAttribute("CurrentRuleWin", win);
				secChoose = sec; //ПРИСВОИТЬ ВЫБРАННЫЙ ПУНКТ    			
				Window window = (Window)Executions.createComponents(
						"rulesSection.zul", null, null);
				window.doModal();
			}
		});
		hlBtnEdit.appendChild(btnEdit);
		//удаление 
		Button btnDel = new Button();
		btnDel.setImage("imgs/crossCLR.png");
		btnDel.setTooltiptext("Убрать пункт");
		btnDel.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				ruChoose.getSections().remove(sec);
				showAll();
			}
		});

		hlBtnEdit.appendChild(btnDel);
		lc2.appendChild(hlBtnEdit);
		li.appendChild(lc2);
		//открыть}
		return li;
	}

	Listitem showPersonal(final Link_rule_employee lre){
		Listitem li=new Listitem();
		Listcell lc1=new Listcell();
		Listcell lc2=new Listcell();

		Vlayout vlBtn = new Vlayout();
		Button btnUp = new Button();
		btnUp.setImage("imgs/upMini.png");
		btnUp.setTooltiptext("На порядок выше");    	
		btnUp.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				upDownPerson(new ArrayList<Link_rule_employee>(ruChoose.getListLINK()), lre.getPosition(), lre.getPosition() - 1);
				showAll();
			}
		});


		Button btnDown = new Button();
		btnDown.setImage("imgs/downMini.png");
		btnDown.setTooltiptext("На порядок ниже");  	
		btnDown.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				upDownPerson(new ArrayList<Link_rule_employee>(ruChoose.getListLINK()), lre.getPosition(), lre.getPosition() + 1);
				showAll();
			}
		});
		Label lbPosition = new Label();
		lbPosition.setValue(Link_rule_employee.listAction.get(lre.getActionrule()));
		Label lbName = new Label(); 
		lbName.setValue(lre.getPosition()+ "." +lre.getEmployee().getHumanface().getFIO());
		Hlayout hlName = new Hlayout();
		vlBtn.appendChild(btnUp);
		vlBtn.appendChild(btnDown);
		hlName.appendChild(vlBtn);
		hlName.appendChild(lbPosition);
		lc1.appendChild(hlName);
		lc2.appendChild(lbName);
		li.appendChild(lc1);
		li.appendChild(lc2);		

		if(CtrRulesMenu.flagNewRules){
			Listcell lc3=new Listcell();
			lc3.setStyle("text-align: right");

			Hlayout hlBtnEdit = new Hlayout();
			//редактировать
			Button btnEdit = new Button();    	
			btnEdit.setImage("imgs/edit.png");
			//btnEdit.setClass("btnEditSection");
			btnEdit.setTooltiptext("Редактирвать персону");

			final CtrRules win = this;
			btnEdit.addEventListener("onClick", new EventListener() {
				public void onEvent(Event event) {    
					CtrRulesPersons.flagNewPerson=false; //СМЕНА ФЛАГА
					Executions.getCurrent().getDesktop().setAttribute("CurrentRuleWin", win);
					lreChoose = lre;   			
					Window window = (Window)Executions.createComponents(
							"rulesPersons.zul", null, null);
					window.doModal();
				}
			});
			hlBtnEdit.appendChild(btnEdit);
			//удаление 
			Button btnDel = new Button();
			btnDel.setImage("imgs/crossCLR.png");
			btnDel.setTooltiptext("Убрать персону");
			btnDel.addEventListener("onClick", new EventListener() {
				public void onEvent(Event event) {
					ruChoose.getListLINK().remove(lre);
					showAll();
				}
			});

			hlBtnEdit.appendChild(btnDel);
			lc3.appendChild(hlBtnEdit);
			li.appendChild(lc3);
		}
		return li;
	}


	Checkbox showSectionCheck(final Section sec){
		final Checkbox chSec = new Checkbox();
		chSec.setLabel(sec.getName());
		chSec.setValue(sec.getName());

		chSec.addEventListener("onClick", new EventListener() {
			public void onEvent(Event event) {
				sec.setPrint(chSec.isChecked());
			}
		});
		return chSec;
	}

	void upDownSection(List<Section> listSec, int one, int two){
		for (int i = 0; i < listSec.size(); i++) {
			boolean flagChange = false;
			//Поднять
			if(listSec.get(i).getLayout() == one && two!=0 && two != (listSec.size()+1)){
				listSec.get(i).setLayout(two);
				flagChange = true;
			}
			//Опустить
			if(listSec.get(i).getLayout() == two && !flagChange && one!=0 && one != (listSec.size()+1)){
				listSec.get(i).setLayout(one);    
			}

		}
	}

	void upDownPerson(List<Link_rule_employee> listLre, int one, int two){
		for (int i = 0; i < listLre.size(); i++) {
			boolean flagChange = false;
			//Поднять
			if(listLre.get(i).getPosition() == one && two!=0 && two != (listLre.size()+1)){
				listLre.get(i).setPosition(two);
				flagChange = true;
			}
			//Опустить
			if(listLre.get(i).getPosition() == two && !flagChange && one!=0 && one != (listLre.size()+1)){
				listLre.get(i).setPosition(one);    
			}

		}
		Collections.sort(listLre);
	}
}
