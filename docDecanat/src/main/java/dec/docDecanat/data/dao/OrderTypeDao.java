package dec.docDecanat.data.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import dec.docDecanat.data.entity.OrderType;

public class OrderTypeDao extends DAO 
{
	public static OrderTypeDao OTDglobal = new OrderTypeDao();
	public OrderType createOrderType(String name) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			OrderType orderType = new OrderType(name);
			//student.getHash();
			getSession().save(orderType);
			commit();
			return orderType;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу создать тип приказа: " + name, e);
		}
	}
	public OrderType createOrderType(OrderType orderType) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().save(orderType);
			commit();
			return orderType;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу создать приказ: " + orderType.getName(), e);
		}
	}
	
	public OrderType updateOrderType(OrderType orderType)throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(orderType);
			commit();
			return orderType;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить тип приказа: " + orderType.getName() + "\n" + e.getMessage(), e);
		}
	}
	
	/*public Set<OrderType> getOrderType() throws Exception{
		try{
			begin();
			Query q = getSession().createQuery("from OrderType where name = :name");
			Set<OrderType> orderType = new HashSet<OrderType>(q.list());
			commit();
			return orderType;
		}catch(HibernateException e){
			rollback();
			throw new Exception("Не удалось получать данные о группе : ", e);
		}
	}*/

	public List<OrderType> getAll() throws Exception{
        try {
            begin();
            Query q = getSession().createQuery("from OrderType");
            List<OrderType> orderType = new ArrayList<OrderType>(q.list());
            commit();
            return orderType;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Не удалось получить всю информацию о типах приказов", e);
        }	
    }
}
