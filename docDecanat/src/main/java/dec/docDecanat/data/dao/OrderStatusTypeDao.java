package dec.docDecanat.data.dao;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import dec.docDecanat.data.entity.OrderStatusType;



public class OrderStatusTypeDao extends DAO{
	public OrderStatusType createOrderStatusType(String name) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			OrderStatusType orderStatusType = new OrderStatusType(name);
			//student.getHash();
			getSession().save(orderStatusType);
			commit();
			return orderStatusType;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу создать статус приказа", e);
		}
	}
	
	public OrderStatusType updateOrderStatusType(OrderStatusType orderStatusType)throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(orderStatusType);
			commit();
			return orderStatusType;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить статус приказа" + "\n" + e.getMessage(), e);
		}
}

	public Set<OrderStatusType> getAllOrderStatusTypes() throws Exception{
        try {
            begin();
            Query q = getSession().createQuery("from OrderStatusType");
            Set<OrderStatusType> orderStatusType = new HashSet<OrderStatusType>(q.list());
            commit();
            return orderStatusType;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Не удалось получить всю информацию о статусе приказов", e);
        }	
    }

}
