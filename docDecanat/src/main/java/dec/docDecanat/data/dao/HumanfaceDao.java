package dec.docDecanat.data.dao;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import dec.docDecanat.data.entity.Humanface;
import dec.docDecanat.data.entity.Order;

public class HumanfaceDao extends DAO{
	
	public Humanface getHumanface(String name) throws Exception{
		try{
			begin();
			Query q = getSession().createQuery("from Humanface where name = :name");
			q.setString("name", name);
			Humanface humanface = (Humanface) q.uniqueResult();
			commit();
			return humanface;
		}catch(HibernateException e){
			rollback();
			throw new Exception("Не удалось получать данные о приказе : " + name, e);
		}
	}
	public Set<Humanface> getAllHumanface() throws Exception{
        try {
            begin();
            Query q = getSession().createQuery("from Humanface");
            Set<Humanface> humanfaces = new HashSet<Humanface>(q.list());
            commit();
            return humanfaces;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Не удалось получить всю информацию о студентах", e);
        }	
    }
}