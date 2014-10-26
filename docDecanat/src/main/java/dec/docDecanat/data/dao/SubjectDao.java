package dec.docDecanat.data.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import dec.docDecanat.data.entity.Subject;


public class SubjectDao extends DAO {

	public Subject createSubject(int hoursCount, int hoursAudcount, boolean exam,
			boolean pass, boolean courseProject, boolean courseWork,
			boolean practic, boolean active) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			Subject subject = new Subject(hoursCount, hoursAudcount, exam, pass, courseProject, courseWork, practic, active);
			getSession().save(subject);
			commit();
			return subject;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу создать предмет: ", e);
		}
	}
	
	public Subject updateSubject(Subject subject)throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(subject);
			commit();
			return subject;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить предмет" +  e.getMessage(), e);
		}
}
	
	//тут надо придумать, что он должен возвращать
/*	public Subject getSubject(String ) throws Exception{
		try{
			begin();
			Query q = getSession().createQuery("from Group where groupname = :groupname");
			q.setString("name", groupName);
			Group group = (Group) q.uniqueResult();
			commit();
			return group;
		}catch(HibernateException e){
			rollback();
			throw new Exception("Не удалось получать данные о группе : " + groupName, e);
		}
	}*/

	public Set<Subject> getAllSubject() throws Exception{
        try {
            begin();
            Query q = getSession().createQuery("from Subject");
            Set<Subject> subjects = new HashSet<Subject>(q.list());
            commit();
            return subjects;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Не удалось получить всю информацию о предметах", e);
        }	
    }

    public void deleteSubject(Subject subject) throws Exception {
        try {
            begin();
            getSession().delete(subject);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Нельзя удалить предмет: ", e);
        }
    }

}
