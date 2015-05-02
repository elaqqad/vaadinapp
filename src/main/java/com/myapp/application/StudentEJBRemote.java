package com.myapp.application;

import com.myapp.entities.Student;
import com.myapp.entities.Project;
import java.util.List;
import javax.ejb.Remote;

@Remote
public interface StudentEJBRemote {

   List<Student> findStudents();

   Student findStudentById(long id);
   
   Student createStudent(Student student);
   
   void deleteStudent(Student student);
   
   Student updateStudent(Student student);
   
   void addProjectToStudent(Project project, Student student);
   
   List<Project> findProjects();
   
   List<Project> findStudentProjects(long id);
   
   public void deleteProject(Project project);
}
