package dec.docDecanat.pageControllers;

import java.awt.Checkbox;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zhtml.Br;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

import dec.docDecanat.data.dao.SectionDao;
import dec.docDecanat.data.entity.Link_rule_employee;
import dec.docDecanat.data.entity.Section;
import dec.docDecanat.data.entity.SectionParameters;


public class CtrRulesSection  extends SelectorComposer<Component> {
	@Wire
	Window winRulSection; //форма для отображения всех параметров пункта

	@Wire
	Textbox tbName;			//хранит название пункта

	@Wire
	Textbox tbDescription;	//хранит описание пункта, в ближайшем будущем будет удалена

	@Wire
	Textbox txtSectionFoundation; //Основание пункта 

	@Wire
	Vlayout vlSecParam;		

	CtrRules winRule ;  	//окно правила, чтобы иметь обратную связь

	int maxRating = 5; //максимальная оценка
	int minRating = 3; //минимальная оценка

	List<Combobox> listSimbol;	//список символов стипеньдиального пункта

	List<Label> listName;		//список названий оценок(5,4,3) стипеньдиального пункта

	List<Textbox> listAmount;	//список "количество оценок" стипеньдиального пункта

	List<Combobox> listMeasure;	//список единиц измерения (единиц, %) стипеньдиального пункта

	Combobox cmbSimbolMinTransfer;	
	Combobox cmbSimbolMaxTransfer;
	Textbox txtAmountMinSubject; 	//минимальное количество долго перевеодного пункта
	Textbox txtAmountMaxSubject;	//максимальное количество долго перевеодного пункта
	Textbox txtAmountSummerSemester;//срок, на который студент может продлить свою задолжность (!)не продление сессии(!) -для перевеодного пункта
	org.zkoss.zul.Checkbox chAllOrOnly; //галочка, по все или не по всем сессия студент может иметь долг - перевеодного пункта
	org.zkoss.zul.Checkbox chPayOrFree; //Флаг бюджетной формы обучения true - бюджет
	org.zkoss.zul.Checkbox chPrologation; //Флаг для пункта, продление или нет
	@Override
	public void doAfterCompose(Component comp) throws Exception {    	
		super.doAfterCompose(comp);

		tbDescription.setTooltiptext("Данное поле содержит в себе отображаемую формулировку в приказе по данному пункту");
		winRule =(CtrRules)Executions.getCurrent().getDesktop().getAttribute("CurrentRuleWin");
		listName= new ArrayList<Label>();
		listSimbol= new ArrayList<Combobox>();
		listAmount= new ArrayList<Textbox>();
		listMeasure= new ArrayList<Combobox>();
		cmbSimbolMinTransfer = new Combobox();
		cmbSimbolMaxTransfer = new Combobox();
		txtAmountMinSubject= new Textbox();
		txtAmountMaxSubject= new Textbox();
		txtAmountSummerSemester = new Textbox();
		chAllOrOnly = new org.zkoss.zul.Checkbox();
		if(Section.flagNewSection){//новый пункт, просто отобразить форму
			showParameters();
		}
		else{//редактирование пункта
			//в зависимости от подтипа приказа, парсить данные
			if(winRule.ctrRuleMenu.OST.getName().equals("Стипендиальный")){
				winRule.secChoose.setSecParameters(winRule.secChoose.parseParametersScholarship());
			}
			if(winRule.ctrRuleMenu.OST.getName().equals("Переводной")){
				winRule.secChoose.setSecParametersTransfer(winRule.secChoose.parseParametersTransfer());
			}

			tbDescription.setValue(winRule.secChoose.getDescription());
			tbName.setValue(winRule.secChoose.getName());
			if(winRule.secChoose.getFoundation()!=null)
				txtSectionFoundation.setValue(winRule.secChoose.getFoundation());
			showParameters();
		}
	}

	void showParameters(){
		while(vlSecParam.getChildren().size()>0) //ОЧИЩЕНИЕ				
			vlSecParam.removeChild(vlSecParam.getChildren().get(0));
		if(winRule.ctrRuleMenu.OST.getName().equals("Стипендиальный"))
		{ 
			org.zkoss.zul.Checkbox chProl = new org.zkoss.zul.Checkbox();
			chProl.setLabel("Продление");
			chPrologation = chProl; 
			if(!Section.flagNewSection)
				chPrologation.setChecked(winRule.secChoose.getSecParameters().get(0).isProl());
			vlSecParam.appendChild(chProl);
			vlSecParam.appendChild(showParametersScholarship());
		}
		if(winRule.ctrRuleMenu.OST.getName().equals("Переводной"))
		{
			vlSecParam.appendChild(showParametersTransfer());
			//заполнить по закрытой/не закрытой сессии
		}
	}

	Listbox showParametersScholarship(){    	
		Listbox table = new Listbox();
		table.appendChild(createHeadScolar());

		int sch=0;
		for (int rating = maxRating; minRating-1  < rating; rating--) {
			table.appendChild(createParamScolar(rating,sch));//передается отдельный счетчик (sch), так как maxRating - это оценка
			sch++;
		}    	
		return table;
	}

	Listhead createHeadScolar(){

		Listhead lh= new Listhead();

		Listheader lh1=new Listheader();
		lh1.setLabel("Оц.");
		lh1.setWidth("40px");
		Listheader lh2=new Listheader();
		lh2.setLabel("Равенство");
		Listheader lh3=new Listheader();
		lh3.setLabel("Кол-во оц.");
		Listheader lh4=new Listheader();
		lh4.setLabel("Ед. изм.");
		lh.appendChild(lh1);
		lh.appendChild(lh2);
		lh.appendChild(lh3);
		lh.appendChild(lh4);
		return lh;
	}

	Listitem createParamScolar(int ratingCur, int sch){
		Listitem li = new Listitem();
		//Оценка
		Listcell lc1 = new Listcell();
		Label lb = new Label();
		lb.setValue(ratingCur+"");
		lb.setHflex("1");
		listName.add(lb);
		lc1.appendChild(lb);
		//Знак равенства
		Listcell lc2 = new Listcell();
		Combobox cmbSimbol = new Combobox();
		cmbSimbol.setHflex("1");
		cmbSimbol.appendItem("<");
		cmbSimbol.appendItem(">");
		cmbSimbol.appendItem("<=");
		cmbSimbol.appendItem(">=");
		cmbSimbol.appendItem("=");
		cmbSimbol.setValue("=");
		listSimbol.add(cmbSimbol);
		lc2.appendChild(cmbSimbol);
		//Количество
		Listcell lc3 = new Listcell();
		Textbox txt = new Textbox();
		listAmount.add(txt);
		txt.setHflex("1");
		txt.setValue("0");
		lc3.appendChild(txt);
		//Ед. изм
		Listcell lc4 = new Listcell();
		Combobox cmbMeasure = new Combobox();
		listMeasure.add(cmbMeasure);
		cmbMeasure.setHflex("1");
		cmbMeasure.appendItem("Единиц");	
		cmbMeasure.setValue("Единиц");
		lc4.appendChild(cmbMeasure);

		if (!Section.flagNewSection)//если редактирование, то загрузить распарсенные данные
		{
			cmbSimbol.setValue( winRule.secChoose.getSecParameters().get(sch).getSimbol());
			cmbMeasure.setValue(winRule.secChoose.getSecParameters().get(sch).getMeasure());
			txt.setValue(String.valueOf(winRule.secChoose.getSecParameters().get(sch).getAmount()));			
		}
		li.appendChild(lc1);
		li.appendChild(lc2);
		li.appendChild(lc3);
		li.appendChild(lc4);


		return li;
	}

	Listbox showParametersTransfer(){
		Listbox table = new Listbox();

		Listitem li0 = new Listitem();
		Listitem li = new Listitem();
		Listitem li2 = new Listitem();
		Listitem li3 = new Listitem();
		//Больше/равно
		Listcell lcCmbMinSimb = new Listcell();
		Combobox cmbMinSimbol = new Combobox();
		cmbMinSimbol.setPlaceholder("Больше");
		cmbMinSimbol.setHflex("1");
		cmbMinSimbol.appendItem(">");
		cmbMinSimbol.appendItem(">=");
		cmbMinSimbol.appendItem("=");
		cmbSimbolMinTransfer = cmbMinSimbol;
		lcCmbMinSimb.appendChild(cmbMinSimbol);
		//Значение минимального порога
		Listcell lcTxtMinSubject = new Listcell();
		Textbox txtMin = new Textbox();
		txtMin.setHflex("1");
		txtMin.setPlaceholder("количество");
		txtAmountMinSubject = txtMin;		
		lcTxtMinSubject.appendChild(txtMin);

		Listcell lclb = new Listcell();
		Label lb = new Label();
		lb.setHflex("1");
		lb.setValue("Долгов"); 
		lclb.appendChild(lb);

		Listcell lclb2 = new Listcell();
		Label lb2 = new Label();
		lb2.setHflex("1");
		lb2.setValue("Долгов"); 
		lclb2.appendChild(lb2);



		// Меньше/равно
		Listcell lcCmbMaxSimb = new Listcell();
		Combobox cmbMaxSimbol = new Combobox();
		cmbMaxSimbol.setPlaceholder("Меньше");
		cmbMaxSimbol.setHflex("1");
		cmbMaxSimbol.appendItem("<");
		cmbMaxSimbol.appendItem("<=");
		cmbMaxSimbol.appendItem("=");
		cmbSimbolMaxTransfer = cmbMaxSimbol;
		lcCmbMaxSimb.appendChild(cmbMaxSimbol);
		// Значение максимального порога
		Listcell lcTxtMaxSubject = new Listcell();
		Textbox txtMax = new Textbox();
		txtMax.setHflex("1");
		txtMax.setPlaceholder("количество");
		txtAmountMaxSubject = txtMax;		
		lcTxtMaxSubject.appendChild(txtMax);
		//Знак равенства
		Listcell lcAmount = new Listcell();
		Textbox txtAmount = new Textbox();
		txtAmount.setHflex("1");
		txtAmount.setPlaceholder("сколько?");
		txtAmountSummerSemester = txtAmount;
		lcAmount.appendChild(txtAmount);
		//Ед. изм
		Listcell lcMeasure = new Listcell();
		Combobox cmbMeasure = new Combobox();
		cmbMeasure.setPlaceholder("Ед.изм");
		cmbMeasure.setHflex("1");
		cmbMeasure.appendItem("Месяцев");
		cmbMeasure.setValue("Месяцев");
		lcMeasure.appendChild(cmbMeasure);
		//В каждом или хотя бы в одном семестре
		Listcell lcAllOrOnly = new Listcell();
		org.zkoss.zul.Checkbox chAllOnly = new org.zkoss.zul.Checkbox();
		chAllOnly.setLabel("В каждом семестре");
		chAllOrOnly = chAllOnly;
		lcAllOrOnly.appendChild(chAllOnly);
		//Бюджетник или платник
		Listcell lcPayFree = new Listcell();
		org.zkoss.zul.Checkbox chPayFree = new org.zkoss.zul.Checkbox();
		chPayFree.setLabel("Бюджетники");
		chPayOrFree = chPayFree;
		lcPayFree.appendChild(chPayFree);

		Listcell lcProl = new Listcell();
		org.zkoss.zul.Checkbox chProl = new org.zkoss.zul.Checkbox();
		chProl.setLabel("Продление");
		chPrologation = chProl;
		lcProl.appendChild(chProl);

		if (!Section.flagNewSection)
		{
			// заполнение элементов правила
			cmbSimbolMinTransfer.setValue(winRule.secChoose.getSecParametersTransfer().getSimbolMin()); 
			cmbSimbolMaxTransfer.setValue(winRule.secChoose.getSecParametersTransfer().getSimbolMax()); 
			txtAmountMinSubject.setValue(String.valueOf(winRule.secChoose.getSecParametersTransfer().getAmountMinSubject()));
			txtAmountMaxSubject.setValue(String.valueOf(winRule.secChoose.getSecParametersTransfer().getAmountMaxSubject()));
			txtAmountSummerSemester.setValue(String.valueOf(winRule.secChoose.getSecParametersTransfer().getAmountSummerSemester()));
			chAllOrOnly.setChecked(winRule.secChoose.getSecParametersTransfer().isAllsemOrOnlysem());
			chPayOrFree.setChecked(winRule.secChoose.getSecParametersTransfer().isPayOrFree());
			chPrologation.setChecked(winRule.secChoose.getSecParametersTransfer().isProl());
		}
		li0.appendChild(lcAllOrOnly);
		li0.appendChild(lcPayFree);
		li0.appendChild(lcProl);
		table.appendChild(li0);
		li.appendChild(lcCmbMinSimb);
		li.appendChild(lcTxtMinSubject);
		li.appendChild(lclb);
		table.appendChild(li);
		li2.appendChild(lcCmbMaxSimb);
		li2.appendChild(lcTxtMaxSubject);
		li2.appendChild(lclb2);
		table.appendChild(li2);
		li3.appendChild(lcAmount);
		li3.appendChild(lcMeasure);
		table.appendChild(li3);
		return table;
	}

	@Listen("onClick = #btnSaveRuleSection")
	public void save(Event e) {
		//сохранение правила
		if(!tbName.getValue().equals(""))
			if(!tbDescription.getValue().equals("")){   
				boolean flagCorrect  =true;
				if(winRule.ctrRuleMenu.OST.getName().equals("Стипендиальный")){
					for (int i = 0; i < listName.size(); i++) {
						if(listSimbol.get(i).getValue().equals("")){
							flagCorrect  = false;
						}
						if(listAmount.get(i).getValue().equals("")){
							flagCorrect  = false;
						}
						if(listMeasure.get(i).getValue().equals("")){
							flagCorrect  = false;
						}
					}
				}

				if(winRule.ctrRuleMenu.OST.getName().equals("Переводной")){
					//TODO:проверить параметры для переводного
				}

				if(flagCorrect){
					if(Section.flagNewSection){
						winRule.secChoose = new Section();
						setAllParamForSecction();
						winRule.ruChoose.getSections().add(winRule.secChoose); 
						winRule.secChoose.setRule(winRule.ruChoose);
						Clients.showNotification("Пункт: "+winRule.secChoose.getName()+" добавлен");

					}
					else
					{
						try {
							setAllParamForSecction();
							Clients.showNotification("Пункт: "+winRule.secChoose.getName()+" обновлен");
						} catch (Exception e1) {
							System.out.println("Не удалось обновить пункт");
						}
					}

					winRule.showAll();   
					winRulSection.detach();
				}    		
				else Clients.showNotification("Параметры пункта заполнены неполностью");
			} else Clients.showNotification("Необходимо указать описание пункта");
		else Clients.showNotification("Необходимо указать название пункта");

	}

	@Listen("onClick = #btnCancelSaveRuleSection")
	public void cancel(Event e) {
		winRule.showAll();   
		winRulSection.detach();

	}

	void setAllParamForSecction(){
		winRule.secChoose.setDescription(tbDescription.getValue());
		winRule.secChoose.setName(tbName.getValue());
		winRule.secChoose.setFoundation(txtSectionFoundation.getValue());
		if(winRule.ctrRuleMenu.OST.getName().equals("Стипендиальный")){
			winRule.secChoose.setParameters(saveParametersScholarship());
		}
		if(winRule.ctrRuleMenu.OST.getName().equals("Переводной")){
			winRule.secChoose.setParameters(saveParametersTransfer());
			System.out.println("ctrrulessection str 349 "+winRule.secChoose.getParameters() );
		}

	}

	String saveParametersScholarship(){
		String allParam = "{"
				+"\"parameters\":"
				+ "[";
		int sch=0;
		for (int i = minRating-1; i < maxRating; i++) {    		
			allParam += "{"
					+"\"name\":\""+  listName.get(sch).getValue()  +"\","
					+"\"simbol\":\"" + listSimbol.get(sch).getValue()  + "\","
					+"\"amount\":\"" + listAmount.get(sch).getValue()  + "\","
					+"\"prol\":\"" + chPrologation.isChecked() + "\","
					+"\"measure\":\"" + listMeasure.get(sch).getValue()  + "\""
					+ "},";
			sch++;
		}
		allParam +=	 "]"
				+ "}";
		return allParam;
	}    

	String saveParametersTransfer(){    	
		String allParam = "{"
				+"\"simbolMin\":\"" + cmbSimbolMinTransfer.getSelectedItem().getLabel()  + "\","
				+"\"simbolMax\":\"" + cmbSimbolMaxTransfer.getSelectedItem().getLabel()  + "\","
				+"\"amountMaxSubject\":\"" + txtAmountMaxSubject.getValue().toString()  + "\","
				+"\"amountMinSubject\":\"" + txtAmountMinSubject.getValue().toString()  + "\","
				+"\"amountSummerSemester\":\"" + txtAmountSummerSemester.getValue().toString()  + "\","
				+"\"payOrFree\":\"" + chPayOrFree.isChecked()  + "\","
				+"\"prol\":\"" + chPrologation.isChecked() + "\","
				+"\"allORonly\":\"" + chAllOrOnly.isChecked()  + "\"";
		return allParam += "}";
	}	
}

