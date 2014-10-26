package dec.docDecanat.data.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import javax.print.attribute.standard.DateTimeAtProcessing;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.type.SortedSetType;
import org.zkoss.util.Dates;

import dec.docDecanat.data.entity.Group;

public class GroupDao extends DAO {
	public Group createGroup(String groupName, Date dateOfBegin, Date dateOfEnd) throws Exception{
		try{
			begin();
			Group group = new Group(groupName, dateOfBegin, dateOfEnd);
			getSession().save(group);
			commit();
			return group;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу создать студента: " + groupName, e);
		}
	}
	public Group create(Group group) throws Exception{
		try{
			begin();
			getSession().save(group);
			System.out.println("Группа сохранена");
			commit();
			return group;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу создать группу: " + group.getName(), e);
		}
	}

	public Group update(Group group)throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(group);
			commit();
			return group;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить" + group.getName() + e.getMessage(), e);
		}
	}
	
	public Group getGroup(String groupName) throws Exception{
		try{
			begin();
			Query q = getSession().createQuery("from Group where groupname = :groupname");
			q.setString("groupname", groupName);
			Group group = (Group) q.uniqueResult();
			commit();
			return group;
		}catch(HibernateException e){
			rollback();
			throw new Exception("Не удалось получать данные о группе : " + groupName, e);
		}
	}

	public Set<Group> getAll() throws Exception{
		try {
			begin();
			Query q = getSession().createQuery("from Group");
			Set<Group> groups = new HashSet<Group>(q.list());
			commit();
			return groups;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не удалось получить всю информацию о студентах", e);
		}	
	}

	public void delete( Group group ) throws Exception {
		try {
			begin();
			getSession().delete(group);
			commit();
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Нельзя удалить группу: " + group.getName(), e);
		}
	}



	/*  public List<Integer> getIdGroupFromGroupName(String groupName) throws Exception{
    	try{
    		begin();
    		Query q = getSession().createSQLQuery("select id_dic_group from dic_group where groupname='" +groupName +"'");
    		List<Integer> idSQL = q.list();
    		commit();
    		return idSQL;
    	}catch(HibernateException e){
    		rollback();
    		throw new Exception("Не удалось получить ID группы " + groupName+ e);
    	}
    }*/

	/*  public List<Group> getGroupFromCourse(int course) throws Exception{
    	try {
            begin();
	        java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
	        calendar.setTime(new java.util.Date());
	        int yearNow = calendar.get(java.util.Calendar.YEAR);
			System.out.println(course);

            List<Group> groupsOfCourse = new ArrayList<Group>( getSession().createSQLQuery("select * from dic_group where id_dic_group in "
            		+ "(select distinct id_dic_group from student_semester_status where course="+course+") "
            		+ "and dateofbegin = '"+(yearNow - course)+"-01-01' order by groupname").addEntity(Group.class).list());
           // Set<Group> groupsOfCourse = new HashSet<Group>(q.list());
            commit();
            return groupsOfCourse;
        } catch (HibernateException e) {
            rollback();
            throw new Exception("Не удалось получить список групп " + course+ " курса", e);
        }	
	 */
}


