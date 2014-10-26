package dec.docDecanat.data.dao;

import org.hibernate.HibernateException;

import dec.docDecanat.data.entity.Rule;
import dec.docDecanat.data.entity.Section;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SectionDao extends DAO{
	
	public static SectionDao secDGlobal = new SectionDao();
	
	public Section create(Section section) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().save(section);
			commit();
			return section;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу создать пункт ", e);			
		}
	}
	
	public Section update(Section section) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(section);
			commit();
			return section;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить пункт: " + section.getDescription(), e);			
		}
	}
	
	
	public void delete(Section section) throws Exception{
		try{
			begin();
			getSession().delete(section);
			commit();
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу удалить пункт ", e);			
		}
	}
	
	
	public Set<Section> getAll() throws Exception{
        try {
            begin();
            Query q = getSession().createQuery("from Section");
            Set<Section> section = new HashSet<Section>(q.list());
            commit();
            return section;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Не удалось получить всю информацию во всех пунктах ", e);
        }	
	}
	public Set<Section> getAllFromRule(Rule ru) throws Exception{
        try {
        	Set<Section> section = null;
            begin();
            Query q = getSession().createQuery("from Section where rule.id ="+ru.getId());
            if(q.list() != null )
            	section = new  HashSet<Section>(q.list());
            commit();
            return section;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Не удалось получить всю информацию о пунктах выбранных по правилу", e);
        }	
	}
}
