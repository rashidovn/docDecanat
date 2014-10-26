package dec.docDecanat.pageControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

import dec.docDecanat.data.dao.RuleDao;
import dec.docDecanat.data.entity.OrderSubType;
import dec.docDecanat.data.entity.Rule;
import dec.docDecanat.data.entity.Section;

public class CtrRulesMenu extends SelectorComposer<Component> {
	
	public boolean ShowInfoRules = true;
	public static boolean flagNewRules = false;

	
	@Wire
	Button btnAddRules; 					//кнопка открытия формы, для создания нового правила 
	
	@Wire
	Button btnEditRules;					//кнопка для редактирования правила (отображается, только после выбора правила)
	@Wire
	Combobox cmbChooseRuleMenu;				//хранит в себе список правил
	
	@Wire
	Combobox cmbSubTypeOrderEditRule;		//хранит в себе список подтипов

	Rule ruChoose;							//локальная переменная для хранения правила
	OrderSubType OST = new OrderSubType();	//локальная переменная для хранения подтипа приказа
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		btnAddRules.setTooltiptext("Добавить правило");
		//hideElements();
		loadCmb();
    	Executions.getCurrent().getDesktop().setAttribute("CurrentCmbAllRule", this);    	
	}

	public void loadCmb(){	
		hideElements();
    	cmbSubTypeOrderEditRule.setModel(OrderSubType.loadCmb());
	}
	
	void hideElements(){
    	cmbChooseRuleMenu.setVisible(false);
    	btnAddRules.setVisible(false);
    	btnEditRules.setVisible(false);		
	}
	
	@Listen("onChange = #cmbSubTypeOrderEditRule")
	public void showCmbChooseRuleMenu(Event e){
		hideElements();
		cmbChooseRuleMenu.setVisible(true);
		cmbChooseRuleMenu.setText("");;
    	btnAddRules.setVisible(true);
    	cmbChooseRuleMenu.setModel(Rule.loadCmb((OrderSubType) cmbSubTypeOrderEditRule.getSelectedItem().getValue()));   
		OST = cmbSubTypeOrderEditRule.getSelectedItem().getValue();
	}
	

	@Listen("onChange = #cmbChooseRuleMenu")
	public void showbtn(Event e){
    	btnEditRules.setVisible(true);
	}
	@Listen("onClick = #btnAddRules")
    public void showAddRule(Event e) {
		flagNewRules = true;
		System.out.println(OST);
		loadRule(true);//новое правило
        Window window = (Window)Executions.createComponents(
                "rules.zul", null, null);
        window.doModal();
	}
	
	
    	@Listen("onClick = #btnEditRules")
        public void showModal(Event e) {
    		if(cmbChooseRuleMenu.getValue().equals("")){
    			Clients.showNotification("Необходимо выбрать правило");
    		}
    		else{
	    		flagNewRules = false;
		    	loadRule(false);//существующее правило
		    	Window window = (Window)Executions.createComponents(
		                   "rules.zul", null, null);
		        window.doModal();     			
    		} 			
	}

    void loadRule(Boolean newRule){
    	if (newRule){
    		System.out.println("1.Создание нового правила");
    		ruChoose = new Rule();
    		ruChoose.setOST(OST);
    	}
    	else{
    		ruChoose = cmbChooseRuleMenu.getSelectedItem().getValue();
    	}	
    }
}
