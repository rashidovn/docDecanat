package dec.docDecanat.data.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import dec.docDecanat.data.entity.Humanface;
import dec.docDecanat.data.entity.StudentCard;


public class StudentCardDao extends DAO{
	public StudentCard createStudentCard(Humanface humanface, String recordBook) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			StudentCard studentCard = new StudentCard(humanface, recordBook);
			//student.getHash();
			getSession().save(studentCard);
			commit();
			return studentCard;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу создать студенчиский билет: " + recordBook, e);
		}
	}
	public StudentCard updateStudentCard(StudentCard studentCard)throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(studentCard);
			commit();
			return studentCard;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить студентчиский билет: " + studentCard.getRecordBook() + e.getMessage(), e);
		}
}
	public List<Integer> getStudentCard() throws Exception{
		try{
			begin();
			Query q = getSession().createQuery("from StudentCard where recordBook = :recordBook");
			List <Integer> recordBooks = q.list();
			commit();
			return recordBooks;
		}catch(HibernateException e){
			rollback();
			throw new Exception("Не удалось получать данные о приказе : ", e);
		}
	}
	
	public Set<StudentCard> getAllStudentCard() throws Exception{
        try {
            begin();
            Query q = getSession().createQuery("from StudentCard");
            Set<StudentCard> studentCards = new HashSet<StudentCard>(q.list());
            commit();
            return studentCards;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Не удалось получить всю информацию о студентичиских билетах", e);
        }	
    }
	
	public void deleteStudentCard(StudentCard studentCard) throws Exception {
        try {
            begin();
            getSession().delete(studentCard);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Нельзя удалить студентчиский билет: " + studentCard.getRecordBook(), e);
        }
    }
	
}

	

   

