package dec.docDecanat.data.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import dec.docDecanat.data.entity.Curriculum;
import dec.docDecanat.data.entity.Group;
import dec.docDecanat.data.entity.GroupSemester;


public class CurriculumDAO extends DAO {
	public List<Curriculum> getAllFromSemestr(Long idSem, int course) throws Exception{
		try {
			begin();
			getSession().flush();
			getSession().clear();
			List<Curriculum> curriculums = new ArrayList<Curriculum>(getSession().createSQLQuery("SELECT * FROM curriculum WHERE id_curriculum in "
					+ "(SELECT id_curriculum FROM dic_group WHERE id_dic_group in (SELECT id_dic_group FROM link_group_semester WHERE id_semester ="+idSem+" AND course="+course+"))").addEntity(Curriculum.class).list());
			for (int i = 0; i < curriculums.size(); i++) {
				curriculums.get(i).setCourse(((GroupSemester)((Group)curriculums.get(i).getGroups().toArray()[0]).getGs().toArray()[0]).getCourse());
			}
			commit();
			return curriculums;
		} catch (HibernateException e) {
			rollback();
			System.out.println("Ошибка в getAll fromSemestr" + e.getMessage());
			throw new Exception("Не удалось получить всю информацию о связях Link_order_employeeDAO 54", e);
		}	
	}
	

	public Curriculum update(Curriculum curri)throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			getSession().merge(curri);
			commit();
			return curri;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить " + curri.getSpecialitytitle() +" "+ e.getMessage(), e);
		}
	}
}
