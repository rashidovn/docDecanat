package dec.docDecanat.data.dao;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import dec.docDecanat.data.entity.Link_rule_employee;

public class Link_rule_employeeDAO extends DAO{

	public Link_rule_employee create(Link_rule_employee lre) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(lre);
			commit();
			return lre;
		} catch (HibernateException e){
			rollback();
			System.out.println(e.getMessage()+ "DAODAODAO 41 ");
			throw new Exception(e.getMessage()+"Не могу создать связь link_order_employeeDAO 25 ", e);
		}
	}

	public Link_rule_employee update(Link_rule_employee lre)throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(lre);
			commit();
			return lre;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить связь Link_order_employeeDAO 39 " + e.getMessage(), e);
		}
	}

	public Set<Link_rule_employee> getAll() throws Exception{
		try {
			begin();
			getSession().flush();
			getSession().clear();
			Query q = getSession().createQuery("from link_order_employee");
			Set<Link_rule_employee> orders = new HashSet<Link_rule_employee>(q.list());
			commit();
			return orders;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не удалось получить всю информацию о связях Link_order_employeeDAO 54", e);
		}	
	}	
	
	public Set<Link_rule_employee> getFromOrder(int idOrder) throws Exception{
		try {
			begin();
			getSession().flush();
			getSession().clear();
	//		Query q = getSession().createQuery("from link_order_employee where id_order ="+ idOrder);
	//		Set<Link_rule_employee> orders = new HashSet<Link_rule_employee>(q.list());
			commit();
			return null;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не удалось получить всю информацию о связях c людьми из приказа "+idOrder+" Link_order_employeeDAO 54", e);
		}	
	}
	
	public Set<Link_rule_employee> getFromEmployee(int idEmployee) throws Exception{
		try {
			begin();
			getSession().flush();
			getSession().clear();
			Query q = getSession().createQuery("from link_order_employee where id_employee ="+ idEmployee);
			Set<Link_rule_employee> orders = new HashSet<Link_rule_employee>(q.list());
			commit();
			return orders;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не удалось получить всю информацию о связях приказов с человеком под номером "+idEmployee+" Link_order_employeeDAO 54", e);
		}	
	}
	
	public void delete(Link_rule_employee lre ) throws Exception {
		try {
			begin();            
			getSession().flush();
			getSession().clear();
			getSession().delete(lre);
			commit();
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не удалось удалить связь Link_order_employeeDAO 68", e);
		}
	}

}
