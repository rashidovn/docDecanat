package dec.docDecanat.data.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.util.SystemOutLogger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import dec.docDecanat.data.entity.Group;
import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.OrderSubType;
import dec.docDecanat.data.entity.StudentSemesterStatus;

public class OrderDao extends DAO {
	public static OrderDao odDGlobal = new OrderDao();

	public Order create(Order order) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().save(order);
			commit();
			return order;
		} catch (HibernateException e){
			System.out.println(e.getMessage()+ "DAODAODAO 27 ");
			rollback();
			throw new Exception(e.getMessage()+"Не могу создать приказ: " + order+ "ordDAO 37 ", e);
		}
	}

	//TODO: Решить проблему с WEST - сделать дополнительный section.
	public Order update(Order order)throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			order=(Order) getSession().merge(order);
			commit();
			return order;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить приказ: " + order.getNumber() + e.getMessage(), e);
		}
	}

	public Set<Order> getAll() throws Exception{
		try {
			begin();
			getSession().flush();
			getSession().clear();
			Query q = getSession().createQuery("from Order");
			Set<Order> orders = new HashSet<Order>(q.list());
			commit();
			return orders;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не удалось получить всю информацию о приказах", e);
		}	
	}	
	public Order getOrder(Long id) throws Exception{
		try {
			begin();
			getSession().flush();
			getSession().clear();
			Query q = getSession().createQuery("from Order where id_order_head="+id);
			Order order =(Order) (q.uniqueResult());
			commit();
			return order;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Connot get Order from Id Order", e);
		}	
	}

	public void delete(Order order ) {
		try {
			begin();            
//			getSession().flush();
//			getSession().clear();
//			getSession().delete(order);
		    Session session = getSession();
		    Object ordObj = session.load(Order.class, order.getId());
		    session.delete(ordObj);
			commit();
		} catch (HibernateException e) {
			System.out.println("Value e " + e.getMessage());
			rollback();
			System.out.println("Не удалось удалить приказ: " + order);
		}
	}

	//Очень странный кусок кода, без сессий не работает :)
	public List<GroupSemester> getListGroup(StudentSemesterStatus sss){
		try {
			begin();   
			getSession().flush();
			getSession().clear();
			Group gr = sss.getGroupSemester().getGroup();
			getSession().refresh(gr);
			List<GroupSemester> listGrS = new ArrayList<GroupSemester>(gr.getGs());
			commit();		
			return listGrS;
		}		
		catch (HibernateException e) {
			rollback();
			System.out.println("<> "+e.getMessage());
			return null;
		}	
	}

}
