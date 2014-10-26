package dec.docDecanat.data.dao;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import dec.docDecanat.data.entity.Order;
import dec.docDecanat.data.entity.Semester;

public class SemesterDAO  extends DAO {
	public static SemesterDAO SemGDAO = new SemesterDAO();
	public Set<Semester> getAll() throws Exception{
		try {
			begin();
			getSession().flush();
			getSession().clear();
			Query q = getSession().createQuery("from Semester");
			Set<Semester> semester = new HashSet<Semester>(q.list());
			commit();
			for (int i = 0; i < semester.size(); i++) {
				Semester sem = (Semester)semester.toArray()[i];
				sem.setNamesem(sem.getName(sem));
			}
			return semester;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не удалось получить всю информацию о semester", e);
		}	
	}
	
	public Semester get(Long id) throws Exception{
		try {
			begin();
			getSession().flush();
			getSession().clear();
			Query q = getSession().createQuery("from Semester where id_semester = "+id);
			Semester semester = (Semester) q.uniqueResult();
			commit();
			return semester;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не удалось получить всю информацию о semester", e);
		}	
	}

}
