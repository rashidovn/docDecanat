package dec.docDecanat.data.dao;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import dec.docDecanat.data.entity.OrderSubType;
import dec.docDecanat.data.entity.Rule;

public class RuleDao extends DAO 
{
	public static RuleDao ruDGlobal = new RuleDao();
	
	public Rule create(Rule ru) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			//Rule rule = new Rule(name);
			//student.getHash();
			ru.setId((Long) getSession().save(ru));
			commit();
			return ru;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу создать правило: " + ru.getName(), e);
		}
	}
	
	public Rule update(Rule rule)throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			rule=(Rule) getSession().merge(rule);
			commit();
			return rule;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить правило: " + rule.getName() + "\n" + e.getMessage(), e);
		}
}
	
	public Rule get(String name) throws Exception{
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
	}

	public Set<Rule> getAll() throws Exception{
        try {
            begin();
            Query q = getSession().createQuery("from Rule");
            Set<Rule> rules = new HashSet<Rule>(q.list());
            commit();
            return rules;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Не удалось получить всю информацию о правилах", e);
        }	
    }	
	
	/*public Set<Rule> getRuleFromSubType(OrderSubType OST) throws Exception{
        try {
            begin();
            Query q = getSession().createQuery("from Rule where ");
            Set<Rule> rules = new HashSet<Rule>(q.list());
            commit();
            return rules;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Не удалось получить всю информацию о правилах", e);
        }	
    }*/

    public void deleteRule(Rule rule ) throws Exception {
        try {
            begin();
            getSession().delete(rule);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Нельзя удалить правило: " + rule.getName(), e);
        }
    }
}
