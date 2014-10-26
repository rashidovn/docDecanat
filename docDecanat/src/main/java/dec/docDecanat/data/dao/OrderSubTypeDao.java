package dec.docDecanat.data.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.OrderSubType;
import dec.docDecanat.data.entity.OrderType;

public class OrderSubTypeDao extends DAO 
{
	public static OrderSubTypeDao OSTDglobal = new OrderSubTypeDao();
	
	public OrderSubType create(OrderType type, String name) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			OrderSubType orderSubType = new OrderSubType(type, name);
			//student.getHash();
			getSession().save(orderSubType);
			commit();
			return orderSubType;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу создать подтип приказа: " + name, e);
		}
	}
	public OrderSubType create(OrderSubType orderSubType) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().save(orderSubType);
			commit();
			return orderSubType;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу создать приказ: " + orderSubType.getName(), e);
		}
	}
	
	public OrderSubType update(OrderSubType orderSubType)throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(orderSubType);
			commit();
			return orderSubType;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить подтип приказа: " + orderSubType.getName() + "\n" + e.getMessage(), e);
		}
}

	public List<OrderSubType> getAll() throws Exception{
        try {
            begin();
            Query q = getSession().createQuery("from OrderSubType");
            List<OrderSubType> orderType = new ArrayList<OrderSubType>(q.list());
            commit();
            return orderType;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Не удалось получить всю информацию о подтипах приказов", e);
        }	
    }
}
