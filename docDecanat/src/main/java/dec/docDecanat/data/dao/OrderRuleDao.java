package dec.docDecanat.data.dao;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.OrderRule;
import dec.docDecanat.data.entity.Section;

public class OrderRuleDao extends DAO {
	public static OrderRuleDao ORDglobal = new OrderRuleDao();

	public OrderRule createRule(boolean currentRule) throws Exception {
		try {
			begin();
			getSession().flush();
			getSession().clear();
			OrderRule orderRule = new OrderRule(currentRule);
			// student.getHash();
			getSession().save(orderRule);
			commit();
			return orderRule;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не могу создать набор правил", e);
		}
	}

	public OrderRule updateRule(OrderRule orderRule) throws Exception {
		try {
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(orderRule);
			commit();
			return orderRule;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не удалось обновить набор правил" + "\n"
					+ e.getMessage(), e);
		}
	}

	public Set<OrderRule> getAllRules() throws Exception {
		try {
			begin();
			Query q = getSession().createQuery("from OrderRule");
			Set<OrderRule> orderRule = new HashSet<OrderRule>(q.list());
			commit();
			return orderRule;
		} catch (HibernateException e) {
			rollback();
			throw new Exception(
					"Не удалось получить всю информацию о наборах правил", e);
		}
	}
}
