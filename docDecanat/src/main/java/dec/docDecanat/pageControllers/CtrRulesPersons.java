package dec.docDecanat.pageControllers;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import dec.docDecanat.data.dao.EmployeeDAO;
import dec.docDecanat.data.dao.RuleDao;
import dec.docDecanat.data.entity.Employee;
import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.Link_rule_employee;
import dec.docDecanat.data.entity.Rule;

public class CtrRulesPersons extends SelectorComposer<Component>{

	@Wire
	Window winRulPersons;
	@Wire
	Combobox cmbChooseActionPerson;
	@Wire
	Listbox lbChoosePersons;
	CtrRules winRule ; 
	public static boolean flagNewPerson = true;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		EmployeeDAO emp = new EmployeeDAO();
		ListModel<Employee> lm=new ListModelList<Employee>(emp.getAll());
		lbChoosePersons.setModel(lm);
		ListModel<String> lmcmb=new ListModelList<String>(Link_rule_employee.listAction);
		cmbChooseActionPerson.setModel(lmcmb);
		winRule =(CtrRules)Executions.getCurrent().getDesktop().getAttribute("CurrentRuleWin");

		if (CtrRulesPersons.flagNewPerson){

		}else{
			cmbChooseActionPerson.setText(Link_rule_employee.listAction.get(winRule.lreChoose.getActionrule()));
			//TODO: загрузить человека в lb

			//lbChoosePersons.setModel(lm);
			//for (int i = 0; i < lm.getSize(); i++) {
			//	if()
			//}
			//lbChoosePersons.getItemAtIndex(index) winRule.lreChoose.getEmployee();
		}
	}

	@Listen("onClick = #btnCancelSaveRulePersons")
	public void cancelSaveRule() {
		System.out.println("test1");
		winRulPersons.detach();
	}
	
	@Listen("onClick = #btnSaveRulePersons")   	//сохранить правило
	public void saveRule() {
		System.out.println("test2");
		if(cmbChooseActionPerson.getValue().equals("")){
			Clients.showNotification("Необходимо указать действие для персоны");
		}
		else{
			saveParametr();
			winRule.showAll(); 
			winRulPersons.detach();    		
		}
	}

	void saveParametr()	{
		Link_rule_employee lre = new Link_rule_employee();
		lre.setEmployee((Employee) lbChoosePersons.getSelectedItem().getValue());//получить из листбокса
		lre.setActionrule(cmbChooseActionPerson.getSelectedIndex());
		lre.setRule(winRule.ruChoose);
		winRule.ruChoose.getListLINK().add(lre);
	}

}
