package dec.docDecanat.pageControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;

import dec.docDecanat.data.dao.StudentSemesterStatusDao;
import dec.docDecanat.data.entity.PrxSearchStudent;
import dec.docDecanat.data.entity.StudentSemesterStatus;

public class CtrSearchStudent extends SelectorComposer<Component>{
	private static final long serialVersionUID = 5183321186606483396L;
	
	@Wire
	Textbox tbSecondName;
	
	@Wire
	Textbox tbFirstName;
	
	@Wire
	Textbox tbPatronymicName;
	
	@Wire
	Textbox tbGroupName;
	
	@Wire
	Listbox lbSearchStudent;
	
	@Wire
	Listbox lbChosenStudents;
	
	@Wire
	Button btnSearch;
	
	public static CtrSearchStudent ctrSS = new CtrSearchStudent();
	public static List<PrxSearchStudent> listSSS = new ArrayList<PrxSearchStudent>();
	public static List<PrxSearchStudent> listChosenStudents = new ArrayList<PrxSearchStudent>();
 	public static StudentSemesterStatus globalSSS = new StudentSemesterStatus();
 	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		ctrSS = this;
		configurateElements();
		configurateLbChoosenStudents();
	}
	
	public void configurateElements(){
		btnSearch.setVisible(true);
		btnSearch.setLabel("Поиск");
		btnSearch.setWidth("100px");
		
		lbSearchStudent.setVisible(true);
		lbSearchStudent.setRows(10);
		lbSearchStudent.setWidth("350px");
		lbSearchStudent.setHeight("400px");
		lbSearchStudent.setMultiple(true);
	}
	
	public void configurateLbChoosenStudents(){
		lbChosenStudents.setVisible(true);
		lbChosenStudents.setRows(10);
		lbChosenStudents.setWidth("350px");
		lbChosenStudents.setHeight("400px");
		lbChosenStudents.setMultiple(true);
		lbChosenStudents.setEmptyMessage("Список студентов пуст");
	}
	
	@Listen("onClick = #btnSearch")
	public void createTable(){
		String firstName = null;
		String secondName = null;
		String patronymicName = null;
		String groupName = null;
		if(!tbFirstName.getValue().equals("")){
			firstName = tbFirstName.getValue();
		}else{
			firstName = "";
		}
		if(!tbSecondName.getValue().equals("")){
			secondName = tbSecondName.getValue();
		}else{
			secondName = "";
		}
		if(!tbPatronymicName.getValue().equals("")){
			patronymicName = tbPatronymicName.getValue();
		}else{
			patronymicName = "";
		}
		if(!tbGroupName.getValue().equals("")){
			groupName = tbGroupName.getValue();
		}else{
			groupName = "";
		}
		listSSS.clear();
		
		listSSS = StudentSemesterStatusDao.sssDGlobal.searchStudents(secondName, firstName, patronymicName, groupName);
		Collections.sort(listSSS);

		
		while(lbChosenStudents.getChildren().size()>0)
			lbChosenStudents.removeChild(lbChosenStudents.getChildren().get(0));
		
		lbChosenStudents.appendChild(createHead());
		if(listChosenStudents.size() > 0){
			try{
				for(int i = 0; i < listChosenStudents.size(); ++i){
					lbChosenStudents.appendChild(createStudent(listChosenStudents.get(i)));
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			lbChosenStudents.setEmptyMessage("Студентов нет");
		}
		
		while(lbSearchStudent.getChildren().size()>0)
			lbSearchStudent.removeChild(lbSearchStudent.getChildren().get(0));
		
		lbSearchStudent.appendChild(createHead());
		if(listSSS.size() > 0){
			try{
				for(int i = 0; i < listSSS.size(); ++i){
					lbSearchStudent.appendChild(createStudent(listSSS.get(i)));
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			lbSearchStudent.setEmptyMessage("Студентов нет");
		}
	}
	
	Listhead createHead(){
		Listhead lh = new Listhead();
		
		Listheader lh1 = new Listheader();
		lh1.setLabel("Фамилия и имя");
		lh1.setWidth("190px");
		
		Listheader lh2 = new Listheader();
		lh2.setLabel("Текущая группа");
		lh2.setWidth("100px");
		
		Listheader lh3 = new Listheader();
		lh3.setLabel("Инфо");
		lh3.setWidth("60px");
		
		lh.appendChild(lh1);
		lh.appendChild(lh2);
		lh.appendChild(lh3);
		return lh;
	}
	
	Listitem createStudent(PrxSearchStudent hsss){
		final PrxSearchStudent prxHSSS = hsss;
		Listitem li = new Listitem();
		li.setCheckable(true);
		
		Listcell lc1 = new Listcell();
		Label lb1 = new Label();
		if(!(prxHSSS.getFamily().equals("")&&prxHSSS.getName().equals(""))){
			lb1.setValue(prxHSSS.getFamily() + " "+ prxHSSS.getName());
		}else{
			lb1.setValue("Нет фамилии");	
			lb1.setSclass("error");
			lc1.setImage("imgs/alert.png");
		}
		lc1.appendChild(lb1);
		
		
		Listcell lc2 = new Listcell();
		Label lb2 = new Label();
		if(!prxHSSS.getGroupname().equals("")){
			lb2.setValue(prxHSSS.getGroupname());
		}else{
			lb2.setValue("Нету имени");
			lb2.setSclass("error");
			lc2.setImage("imgs/alert.png");
		}
		lc2.appendChild(lb2);
		
		Listcell lc3 = new Listcell();
		Button btnInfo = new Button();
		btnInfo.setTooltiptext("Информация");
		btnInfo.setImage("imgs/info.png");
		btnInfo.addEventListener("onClick", new EventListener(){
			public void onEvent(Event event) throws Exception{
				CtrSearchStudent.globalSSS = StudentSemesterStatusDao.sssDGlobal.getSSS(prxHSSS.getId_student_semester_status());
				Window window = (Window)Executions.createComponents(
						"infoStudent.zul", null, null);
				window.doModal();
			}
		});
		lc3.appendChild(btnInfo);
		
		li.appendChild(lc1);
		li.appendChild(lc2);
		li.appendChild(lc3);
		return li;
	}
	
	public void ss2(){
		System.out.println("git ");
	}
	
	public class ChooseEvent extends Event {
        public ChooseEvent(Component target, List<PrxSearchStudent> data) {
            super("onChoose", target, data);
        }
    }
}
