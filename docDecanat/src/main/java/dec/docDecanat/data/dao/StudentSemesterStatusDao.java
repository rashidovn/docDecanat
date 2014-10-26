package dec.docDecanat.data.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javassist.convert.Transformer;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

import dec.docDecanat.data.entity.Group;
import dec.docDecanat.data.entity.Humanface;
import dec.docDecanat.data.entity.Link_note_student_semester_status_order;
import dec.docDecanat.data.entity.PrxSearchStudent;
import dec.docDecanat.data.entity.Semester;
import dec.docDecanat.data.entity.StudentCard;
import dec.docDecanat.data.entity.StudentSemesterStatus;

public class StudentSemesterStatusDao extends DAO{

	public static StudentSemesterStatusDao sssDGlobal = new StudentSemesterStatusDao();

	public StudentSemesterStatus create(StudentCard studentCard) throws Exception{
		try{
			begin();
			getSession().flush();
			getSession().clear();
			StudentSemesterStatus status = new StudentSemesterStatus(studentCard);
			//student.getHash();
			getSession().save(status);
			commit();
			return status;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не могу создать sss ", e);
		}
	}

	public StudentSemesterStatus update(StudentSemesterStatus status)throws Exception{
		try{
			begin();
			for(int i=0;i<status.getList_notes().size();i++){
				System.out.println(">> "+((Link_note_student_semester_status_order)status.getList_notes().toArray()[i]).getNote());
			}
			getSession().flush();
			getSession().clear();
			getSession().merge(status);
			commit();
			return status;
		} catch (HibernateException e){
			rollback();
			throw new Exception("Не удалось обновить sss " + e.getMessage(), e);
		}
	}

	public Set<StudentSemesterStatus> getAllSSS() throws Exception{
		try {
			begin();
			Query q = getSession().createQuery("from StudentSemesterStatus");
			Set<StudentSemesterStatus> statuses = new HashSet<StudentSemesterStatus>(q.list());
			commit();
			return statuses;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не удалось получить всю информацию о статусах", e);
		}	
	}

	public StudentSemesterStatus get(Long id) throws Exception{
		try {
			begin();
			Query q = getSession().createQuery("from StudentSemesterStatus WHERE id_student_semester_status = "+id);
			StudentSemesterStatus stud = (StudentSemesterStatus)q.uniqueResult();
			commit();
			return stud;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не удалось получить всю информацию о статусах", e);
		}	
	}

	public void delete(StudentSemesterStatus status ) throws Exception {
		try {
			begin();
			getSession().delete(status);
			commit();
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Нельзя удалить статус курса: " , e);
		}
	}

	public Integer prevSesRes(Group gr, StudentSemesterStatus sss, Semester curS)
	{
		try {
			begin();
			getSession().flush();
			getSession().clear();
			Integer sesRes = (Integer) getSession().createSQLQuery("SELECT sessionresult from student_semester_status where id_studentcard="+sss.getStudentCard().getId()+" and id_link_group_semester in (select id_link_group_semester from link_group_semester where id_dic_group = "+gr.getId()+" and id_semester<="+curS.getId()+" ORDER BY id_semester DESC limit 1)").uniqueResult();
			commit();
			return sesRes;
		} catch (HibernateException e) {
			rollback();
			System.out.println("Can't ger prev sessionRes: "+e);
			return null;
		}
	} 
	
	public List<PrxSearchStudent> searchStudents(String secondName, String firstName, String patronymicName, String groupName){
		try{
			begin();
			String query = "select sss.id_student_semester_status, hf.family, hf.name, hf.patronymic, gr.groupname from student_semester_status sss, studentcard sc, humanface hf, link_group_semester lgs, dic_group gr where sss.id_studentcard=sc.id_studentcard and hf.id_humanface=sc.id_humanface and sss.id_student_semester_status in (select sdr from (select distinct max(lgs.course), sc.id_studentcard, max(sss.id_student_semester_status) as sdr from humanface h, studentcard sc, student_semester_status sss, link_group_semester lgs, dic_group gr where h.id_humanface = sc.id_humanface and sc.id_studentcard = sss.id_studentcard and sss.id_link_group_semester = lgs.id_link_group_semester and lgs.id_dic_group = gr.id_dic_group and h.family like '%"+secondName+"%' and h.name like '%"+firstName+"%' and h.patronymic like '%"+patronymicName+"%' and gr.groupname like '%"+groupName+"%' group by sc.id_studentcard) as foo) and sss.id_link_group_semester = lgs.id_link_group_semester and lgs.id_dic_group = gr.id_dic_group and sss.id_deducted = '0'";
		
			Query q = getSession().createSQLQuery(query).addScalar("family").addScalar("groupname").
					addScalar("name").addScalar("patronymic").addScalar("id_student_semester_status").setResultTransformer(Transformers.aliasToBean(PrxSearchStudent.class));
			System.out.println("q:" + q);
			System.out.println("q.list" + q.list());
			System.out.println("qlist get0" + q.list().get(0).toString());
			List<PrxSearchStudent> listHSSS = new ArrayList<PrxSearchStudent>(q.list());
			
			/*String query = "select sss.* from student_semester_status sss where sss.id_student_semester_status in (select sdr from (select distinct max(lgs.course), sc.id_studentcard, max(sss.id_student_semester_status) as sdr from humanface h, studentcard sc, student_semester_status sss, link_group_semester lgs, dic_group gr where h.id_humanface = sc.id_humanface and sc.id_studentcard = sss.id_studentcard and sss.id_link_group_semester = lgs.id_link_group_semester and lgs.id_dic_group = gr.id_dic_group and h.family like '%"+secondName+"%' and h.name like '%"+firstName+"%' and h.patronymic like '%"+patronymicName+"%' and gr.groupname like '%"+groupName+"%' group by sc.id_studentcard) as foo)";
			Query q = getSession().createSQLQuery(query).addEntity(StudentSemesterStatus.class);
			System.out.println("q: " + q);
			System.out.println("qlist: " + q.list());
			List<StudentSemesterStatus> listSSS = new ArrayList<StudentSemesterStatus>(q.list());*/
			
			commit();
			return listHSSS;
		}catch(HibernateException e){
			rollback();
			System.out.println("Can't get sss: " + e);
			return null;
		}
	}

	public StudentSemesterStatus getSSS(BigInteger id) throws Exception{
		try {
			begin();
			Query q = getSession().createQuery("from StudentSemesterStatus WHERE id_student_semester_status = "+id);
			StudentSemesterStatus stud = (StudentSemesterStatus)q.uniqueResult();
			commit();
			return stud;
		} catch (HibernateException e) {
			rollback();
			throw new Exception("Не удалось получить всю информацию о статусах", e);
		}	
	}
}
