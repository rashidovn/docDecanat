package dec.docDecanat.pageControllers;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import dec.docDecanat.ItemRender.CurriculumListboxRender;
import dec.docDecanat.data.dao.CurriculumDAO;
import dec.docDecanat.data.dao.GroupSemesterDao;
import dec.docDecanat.data.dao.SemesterDAO;
import dec.docDecanat.data.entity.Curriculum;
import dec.docDecanat.data.entity.Group;
import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.ProxyStringValue;
import dec.docDecanat.data.entity.Semester;	

public class CtrSetDate extends SelectorComposer<Component>{

	@Wire
	Window winSetDate;
	@Wire
	Combobox cmbSetDateChooseSem;
	@Wire
	Combobox cmbSetDateChooseCourse;
	@Wire
	Listbox lbChoose;
	@Wire
	Listitem liSpecialAndGroup;
	@Wire
	Button saveSetDate;
	@Wire
	Datebox dtbSetDateBeginSem;
	@Wire
	Datebox dtbSetDateEndSem;
	@Wire
	Datebox dtbSetDateBeginPass;
	@Wire
	Datebox dtbSetDateEndPass;
	@Wire
	Datebox dtbSetDateBeginSes;
	@Wire
	Datebox dtbSetDateEndSes;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		List<Semester> listSem;
		try {
			listSem = new ArrayList<Semester>(SemesterDAO.SemGDAO.getAll());
			Collections.sort(listSem);
			ListModel<Semester> lmSemester = new ListModelList<Semester>(listSem);
			cmbSetDateChooseSem.setModel(lmSemester);
		} catch (Exception e) {
			System.out.println("Не удалось получить список семестров");
			Clients.showNotification("Не удалось получить список семестров");
		}

		
	}	
	@Listen("onChange = #cmbSetDateChooseSem")
	public void showCourse() {		
		try {
			ListModel<ProxyStringValue> lm = new ListModelList<ProxyStringValue>(GroupSemesterDao.gsDGlobal.getAllCourse((Semester) cmbSetDateChooseSem.getSelectedItem().getValue()));
			cmbSetDateChooseCourse.setModel(lm);
		} catch (Exception e) {
			System.out.println("Не удалось получить список всех курсов str56 ctrSetDate");
			e.printStackTrace();
		}

	}
	@Listen("onChange = #cmbSetDateChooseCourse")
	public void showCurri() {		
		CurriculumDAO curriDAO = new CurriculumDAO();		
		List<Curriculum> listCurri;		
		try {			
			listCurri = curriDAO.getAllFromSemestr(((Semester)cmbSetDateChooseSem.getSelectedItem().getValue()).getId(),
					Integer.parseInt(((ProxyStringValue)(cmbSetDateChooseCourse.getSelectedItem().getValue())).getValue()));
					//(Integer) cmbSetDateChooseCourse.getSelectedItem().getValue());
			Collections.sort(listCurri);
			for (int i = 0; i < listCurri.size(); i++) {
				lbChoose.appendChild(createSpecialLine(listCurri.get(i)));				
			}
//			ListModel<Curriculum> lmCurri = new ListModelList<Curriculum>(listCurri);
//			//lbChoose.setItemRenderer(new CurriculumListboxRender());
//			lbChoose.setModel(lmCurri);
		} catch (Exception e) {
			System.out.println("Не удалось получить список специальностей" + e.getMessage());
			Clients.showNotification("Не удалось получить список специальностей");
		}

	}
	
	Listitem createSpecialLine(Curriculum curri){
		Listitem li = new Listitem();
		li.setValue(curri);
		Listcell lcSpecial = new Listcell();
		Listcell lcGroup = new Listcell();

		Label lbSpecial = new Label();
		lbSpecial.setValue(curri.getSpecialitytitle());
		lbSpecial.setWidth("400px");
		lcSpecial.appendChild(lbSpecial);
		String allGroup = "";
		for (int i = 0; i < curri.getGroups().size(); i++) {
			allGroup+= ((Group)curri.getGroups().toArray()[i]).getName();
		}
		Label lbGroup = new Label();
		lbGroup.setWidth("100px");
		lbGroup.setValue(allGroup);
		lcGroup.appendChild(lbGroup);
		li.appendChild(lcSpecial);
		li.appendChild(lcGroup);
		return li;
		
	}
	
	@Listen("onClick = #saveSetDate")
	public void saveDate()
	{
		List<Curriculum> listCurri = new ArrayList<Curriculum>();
		for (int i = 0; i < lbChoose.getSelectedItems().size(); i++) {
			System.out.println("ctrset set date136 " + (Curriculum) ((Listitem) lbChoose.getSelectedItems().toArray()[i]).getValue());
			listCurri.add((Curriculum) ((Listitem) lbChoose.getSelectedItems().toArray()[i]).getValue());
			
		}
		
		for (int c = 0; c <listCurri.size() ; c++) {
			Curriculum curri = listCurri.get(c);
			for (int g = 0; g < curri.getGroups().size(); g++) {
				Group group = (Group) curri.getGroups().toArray()[g];
				for (int s = 0; s < group.getGs().size(); s++) {
					GroupSemester gs = (GroupSemester) group.getGs().toArray()[s];
					if(gs.getCourse()==Integer.parseInt(((ProxyStringValue)(cmbSetDateChooseCourse.getSelectedItem().getValue())).getValue())
							&& gs.getSemester().getId()==((Semester)cmbSetDateChooseSem.getSelectedItem().getValue()).getId()){
						if(dtbSetDateBeginSem.getValue()!=null){
							gs.setDateOfBeginSemester(dtbSetDateBeginSem.getValue());
						}
						if(dtbSetDateEndSem.getValue()!=null){
							gs.setDateOfEndSemester(dtbSetDateEndSem.getValue());
						}
						if(dtbSetDateBeginPass.getValue()!=null){
							gs.setDateOfBeginPassWeek(dtbSetDateBeginPass.getValue());
						}
						if(dtbSetDateEndPass.getValue()!=null){
							gs.setDateOfEndPassWeek(dtbSetDateEndPass.getValue());
						}
						if(dtbSetDateBeginSes.getValue()!=null){
							gs.setDateOfBeginSession(dtbSetDateBeginSes.getValue());
						}
						if(dtbSetDateEndSes.getValue()!=null){
							gs.setDateOfEndSession(dtbSetDateEndSes.getValue());
						}
					}					
				}
			}			
		CurriculumDAO curriDAO = new CurriculumDAO();
		try {
			curriDAO.update(curri);
			winSetDate.detach();
		} catch (Exception e) {
			Clients.showNotification("Не удалось сохранить");
			System.out.println("Не удалось сохранить");
			e.printStackTrace();
		}
		}
	}
}
