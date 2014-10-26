package dec.docDecanat.data.dao;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;


public class GroupSubjectDAO extends DAO{

	public GroupSubjectDAO create(GroupSubjectDAO gs) throws Exception{
		try{
			begin();
			getSession().save(gs);
			commit();
			return gs;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу создать приказ: ", e);//заменить, как надо 
		}
	}


	
	public GroupSubjectDAO update(GroupSubjectDAO gs)throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(gs);
			commit();
			return gs;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить приказ: " + e.getMessage(), e);//заменить, как надо 
		}
}


	public Set<GroupSubjectDAO> getAll() throws Exception{
        try {
            begin();
            Query q = getSession().createQuery("from GroupSubject");
            Set<GroupSubjectDAO> gs = new HashSet<GroupSubjectDAO>(q.list());
            commit();
            return gs;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Не удалось получить всю информацию о приказах", e);//заменить, как надо 
        }	
    }

    public void delete(GroupSubjectDAO gs) throws Exception {
        try {
            begin();
            getSession().delete(gs);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Нельзя удалить приказ: ", e);//заменить, как надо 
        }
    }
}
