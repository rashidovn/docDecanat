package dec.docDecanat.pageControllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import dec.docDecanat.data.dao.Link_note_student_semester_status_orderDAO;
import dec.docDecanat.data.dao.SemesterDAO;
import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.Link_note_student_semester_status_order;
import dec.docDecanat.data.entity.Semester;
import dec.docDecanat.data.entity.StudentSemesterStatus;

public class CtrNotes extends SelectorComposer<Component>{
	@Wire
	Listbox lbNotes;
	@Wire
	Button btnCloseNotes;
	@Wire
	Window winNotes;

	public static StudentSemesterStatus sss = new StudentSemesterStatus();
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		//Link_note_student_semester_status_orderDAO noteDAO = new Link_note_student_semester_status_orderDAO();
		List<Link_note_student_semester_status_order> listNotesTemp = new ArrayList<Link_note_student_semester_status_order>(sss.getList_notes());
		List<Link_note_student_semester_status_order> listNotes = new ArrayList<Link_note_student_semester_status_order>();
		for (int i = 0; i < listNotesTemp.size(); i++) {
			if(listNotesTemp.get(i).getOrder().getId()==CtrOrder.ord.getId()){
				listNotes.add(listNotesTemp.get(i));
			}
		}
		
		Collections.sort(listNotes);
		ListModel<Link_note_student_semester_status_order> lmNotes = new ListModelList<Link_note_student_semester_status_order>(listNotes);
		lbNotes.setModel(lmNotes);
	}

	@Listen("onClick = #btnCloseNotes")
	public void close()
	{
		winNotes.detach();
	}
	//	String sessionres = "";
	//	for (int j = 0; j < student.getSessionRating().size(); j++) {
	//		SessionRating session = (SessionRating) student.getSessionRating().toArray()[j];
	//		if(session.isExam())
	//			sessionres += session.getExamRating()+"Э ";
	//		if(session.isPass())
	//			sessionres += session.getPassRating()+"З ";
	//		if(session.isCourseProject())
	//			sessionres += session.getCourseProjectRating()+"КП ";
	//		if(session.isCourseWork())
	//			sessionres += session.getCourseWorkRating()+"КР ";
	//		if(session.isPractic())
	//			sessionres += session.getPracticRating()+"П ";			
	//	}
	//
	//	sessionres += " ("+student.getSessionResult()+","+student.getGroupSemester().getId()+")";
}
