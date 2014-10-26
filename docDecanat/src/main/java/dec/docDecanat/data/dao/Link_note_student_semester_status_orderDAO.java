package dec.docDecanat.data.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.Link_note_student_semester_status_order;
import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.data.entity.StudentSemesterStatus;


public class Link_note_student_semester_status_orderDAO extends DAO{

	public Link_note_student_semester_status_order create(Link_note_student_semester_status_order note) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			//Возможно стоит на merge если сломается
			note= (Link_note_student_semester_status_order) getSession().merge(note);
			commit();
			return note;
		} catch (HibernateException e){
			rollback();
			throw new Exception(e.getMessage()+"Не могу создать связь Link_note_student_semester_status_order 19 ", e);
		}
	}	
	public List<Link_note_student_semester_status_order> getAll() throws Exception{
		try{
			begin();
			Query q = getSession().createQuery("from Link_note_student_semester_status_order");
			List<Link_note_student_semester_status_order> notes = new ArrayList<Link_note_student_semester_status_order>(q.list());
			commit();
			return notes;
		} catch (HibernateException e){
			rollback();
			throw new Exception(e.getMessage()+"Link_note_student_semester_status_order 73, Cannot get all notes  ", e);
		}
	}	
	public List<Link_note_student_semester_status_order> getAllFromOrder(Order order) throws Exception{
		try{
			begin();
			Query q = getSession().createQuery("from Link_note_student_semester_status_order where id_order_head="+order.getId());
			List<Link_note_student_semester_status_order> notes = new ArrayList<Link_note_student_semester_status_order>(q.list());
			commit();
			return notes;
		} catch (HibernateException e){
			rollback();
			throw new Exception(e.getMessage()+"Link_note_student_semester_status_order 73, Cannot get all notes  ", e);
		}
	}
	public boolean deleteNotesFromOrder(Order order) throws Exception {
		try {
		
			List<Link_note_student_semester_status_order> list_note = getAllFromOrder(order);
			begin();            
			for (int i = 0; i < list_note.size(); i++) {
				if(list_note.get(i).getOrder().getId()==order.getId()){
					list_note.get(i).getSss().getList_notes().remove(list_note.get(i));
					//StudentSemesterStatusDao sd= new StudentSemesterStatusDao();
					//sd.update(list_note.get(i).getSss());
					getSession().delete(list_note.get(i));
				}
			}
			getSession().flush();
			getSession().clear();
			//getSession().createSQLQuery("DELETE FROM link_note_student_semester_status_order WHERE id_order_head="+order.getId()); УДАЛИТЬ
			commit();
			return true;
		} catch (HibernateException e) {
			System.out.println("Value e " + e.getMessage());
			rollback();
			throw new Exception("Не удалось удалить списки причин приказа " + order.getId(), e);
		}
	}
}
