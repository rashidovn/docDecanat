package dec.docDecanat.data.dao;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import dec.docDecanat.data.entity.SessionRating;

public class SessionRatingDao extends DAO{
	
	public SessionRating create(boolean exam, boolean pass, boolean courseProject,
			boolean courseWork, boolean practic, int examRating,
			int passRating, int courseProjectRating, int courseWorkRating,
			int practicRating) throws Exception{
		try{
			begin();
			SessionRating rating = new SessionRating(exam, pass, courseProject, courseWork, practic, 
					examRating, passRating, courseProjectRating, courseWorkRating, practicRating);
			//student.getHash();
			getSession().save(rating);
			commit();
			return rating;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу оценку сесси: ", e);
		}
	}
	
	public SessionRating update(SessionRating rating)throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(rating);
			commit();
			return rating;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить оценку сессии: " +  "\n" + e.getMessage(), e);
		}
}
	
	//Тут надо подумать что он должен возвращать. 
	/*public SessionRating get(String name) throws Exception{
		try{
			begin();
			Query q = getSession().createQuery("from Rule where name = :name");
			q.setString("name", name);
			Rule rule = (Rule) q.uniqueResult();
			commit();
			return rule;
		}catch(HibernateException e){
			rollback();
			throw new Exception("Не удалось получать данные о правиле : " + name, e);
		}
	}*/

	public Set<SessionRating> getAll() throws Exception{
        try {
            begin();
            Query q = getSession().createQuery("from SessionRating");
            Set<SessionRating> ratings = new HashSet<SessionRating>(q.list());
            commit();
            return ratings;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Не удалось получить всю информацию об оценках сессии", e);
        }	
    }

    public void deleteSessionRating(SessionRating rating ) throws Exception {
        try {
            begin();
            getSession().delete(rating);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Нельзя удалить оценку сессии: ", e);
        }
    }

}
