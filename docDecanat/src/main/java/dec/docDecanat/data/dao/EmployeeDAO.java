package dec.docDecanat.data.dao;

import java.util.HashSet;
import java.util.Set;

import dec.docDecanat.data.entity.Employee;
import dec.docDecanat.data.entity.Order;

import org.hibernate.HibernateException;
import org.hibernate.Query;

public class EmployeeDAO extends DAO {

	public Set<Employee> getAll() throws Exception{
		try {
			begin();
			getSession().flush();
			getSession().clear();
			Query q = getSession().createQuery("from Employee");
			Set<Employee> employee = new HashSet<Employee>(q.list());
			commit();
			return employee;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не удалось получить всю информацию о приказах", e);
		}	
	}
}