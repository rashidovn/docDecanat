package dec.docDecanat.data.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.mapping.Collection;
import org.hsqldb.types.Collation;

import dec.docDecanat.data.entity.Group;
import dec.docDecanat.data.entity.GroupSemester;
import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.ProxyStringValue;
import dec.docDecanat.data.entity.Semester;

public class GroupSemesterDao extends DAO{

	public static GroupSemesterDao gsDGlobal = new GroupSemesterDao();

	public GroupSemester create(GroupSemester gs) throws Exception{
		try{
			begin();
			getSession().save(gs);
			commit();
			return gs;
		} catch (HibernateException e){
			rollback();
			throw new Exception("PLUHERROR Cannot create groupsemester: ", e);//заменить, как надо 
		}
	}



	public GroupSemester update(GroupSemester gs)throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(gs);
			commit();
			return gs;
		} catch (HibernateException e){
			rollback();
			throw new Exception("PLUHERROR Cannot update groupsemester " + e.getMessage(), e);//заменить, как надо 
		}
	}


	public Set<GroupSemester> getAll() throws Exception{
		try {
			begin();
			Query q = getSession().createQuery("from GroupSemester");
			Set<GroupSemester> gs = new HashSet<GroupSemester>(q.list());
			commit();
			return gs;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("PLUHERROR Cannot get list groupsemester", e);
		}	
	}


	public Set<GroupSemester> getAllFromGroup(Group gr) throws Exception{
		try {
			begin();
			getSession().flush();
			getSession().clear();
			Query q = getSession().createQuery("from GroupSemester where id_dic_group = " + gr.getId());
			Set<GroupSemester> gs = new HashSet<GroupSemester>(q.list());
			commit();
			return gs;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("PLUHERROR Cannot get list groupsemester", e);
		}	
	}

	public List<GroupSemester> getSomeFromGroup(Group gr, Integer num,Semester curS) throws Exception{
		try {
			begin();
			getSession().flush();
			getSession().clear();
			
			List<GroupSemester> gs=new ArrayList<GroupSemester>();
			if(num==-1){
				gs = new ArrayList<GroupSemester>(getSession().createSQLQuery("SELECT * from link_group_semester where id_dic_group = "+gr.getId()+" and id_semester<="+curS.getId()+" ORDER BY id_semester DESC").addEntity(GroupSemester.class).list());
			}else{
				gs = new ArrayList<GroupSemester>(getSession().createSQLQuery("SELECT * from link_group_semester where id_dic_group = "+gr.getId()+" and id_semester<="+curS.getId()+" ORDER BY id_semester DESC limit "+num).addEntity(GroupSemester.class).list());
			}
			commit();
			return gs;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("PLUHERROR Cannot get list groupsemester", e);
		}	
	}

	public List<GroupSemester> getAllFromCourse(int course, Semester Sem) throws Exception{
		try {
			java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getDefault(), java.util.Locale.getDefault());
			calendar.setTime(new java.util.Date());
			int yearNow = calendar.get(java.util.Calendar.YEAR);
			begin();
			//ВАРИАНТ ДЛЯ ТЕСТИРОВАНИЯ
			getSession().flush();
			getSession().clear();
			List<GroupSemester> gs = new ArrayList<GroupSemester>( getSession().createSQLQuery("SELECT * from link_group_semester where course="+course + " AND id_semester="+ Sem.getId()).addEntity(GroupSemester.class).list());
			// ЭТО ПРАВИЛЬНЫЙ ВАРИАНТ  List<GroupSemester> gs = new ArrayList<GroupSemester>( getSession().createSQLQuery("SELECT * from link_group_semester where id_dic_group in(SELECT id_dic_group FROM dic_group WHERE dateofbegin = '"+(yearNow - course)+"-01-01' order by groupname) AND course="+course+" AND id_semester in (SELECT id_semester FROM semester where is_current_sem=1)").addEntity(GroupSemester.class).list());
			//Set<GroupSemester> gs = new HashSet<GroupSemester>(q.list());
			getSession().flush();
			commit();
			return gs;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("PLUHERROR Cannot get groupsemester from course", e);
		}	
	}

	public List<ProxyStringValue> getAllCourse(Semester Sem) throws Exception{
		try {
			int realCourse = 5;
			begin();
			Query q = getSession().createSQLQuery("SELECT DISTINCT link_group_semester.course "
					+ "FROM link_group_semester WHERE EXISTS "
					+ "(SELECT *FROM student_semester_status where id_link_group_semester = link_group_semester.id_link_group_semester)"
					+ " AND id_semester ="+Sem.getId() + "order by course");//
			List <Integer> coursestemp = (List <Integer>) q.list();
			List <ProxyStringValue> courses =  new ArrayList<ProxyStringValue>();
			Collections.sort(coursestemp);
			for (int i = 0; i < coursestemp.size(); i++) {
				//					System.out.println(coursestemp.get(i).toString()+" э хлеб");
				if(coursestemp.get(i)!=null)
					courses.add(new ProxyStringValue( coursestemp.get(i).toString(), coursestemp.get(i).toString()));
			}
			commit();
			return courses;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("PLUHERROR Cannot get list all course", e);
		}	
	}


	public void delete(GroupSemester gs) throws Exception {
		try {
			begin();
			getSession().delete(gs);
			commit();
		} catch (HibernateException e) {
			rollback();
			throw new Exception("PLUHERROR cannot drop groupsemester ", e);//заменить, как надо 
		}
	}
}
