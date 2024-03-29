package com.myapp.application;

import com.myapp.entities.Student;
import com.myapp.entities.Project;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.EntityType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Join;
import java.util.List;

@Stateless
public class StudentEJB implements StudentEJBRemote {

    @PersistenceContext(unitName = "student-pu")
    private EntityManager entityManager;

    public Student createStudent(Student student) {
        entityManager.persist(student);
        return student;
    }
    
    public Student findStudentById(long id) {
        return entityManager.find(Student.class, id);
    }
    
    public List<Student> findStudents() {
        CriteriaQuery<Student> cq = entityManager.getCriteriaBuilder().createQuery(Student.class);
        cq.select(cq.from(Student.class));
        return entityManager.createQuery(cq).getResultList();
    }
    
    public void deleteStudent(Student student) {
        if(student.getStudentid() > 0) {
            // reattach to remove
            student = entityManager.merge(student);
            entityManager.remove(student);
        }
    }
    
    public Student updateStudent(Student student) {
        return entityManager.merge(student);
    }
    
    public void addProjectToStudent(Project project, Student student) {
        Student s = entityManager.find(Student.class, student.getStudentid());
        entityManager.persist(project);
        s.addProject(project);
    }
    
    public List<Project> findProjects() {
        CriteriaQuery<Project> cq = entityManager.getCriteriaBuilder().createQuery(Project.class);
        cq.select(cq.from(Project.class));
        return entityManager.createQuery(cq).getResultList();
    }
    
    /* list projects of selected student */
    public List<Project> findStudentProjects(long id) {
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	CriteriaQuery<Project> cq = cb.createQuery(Project.class);
	Root<Student> root = cq.from(Student.class);
	Join<Student, Project> related = root.join("projects");
	cq.select(related);
	cq.where(cb.equal(root.get("studentid"),id));
	return entityManager.createQuery(cq).getResultList();
    }
    
    public void deleteProject(Project project) {
            /*Student s = project.getStudentBean();
            s = entityManager.merge(s);
            s.removeProject(project);*/
            project = entityManager.merge(project);
            entityManager.remove(project);
    }
}
