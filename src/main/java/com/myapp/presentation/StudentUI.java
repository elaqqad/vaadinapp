package com.myapp.presentation;

import com.vaadin.ui.Alignment;
import com.vaadin.event.FieldEvents;
import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;
import java.util.List;
import javax.inject.Inject;
import javax.ejb.EJB;
import javax.enterprise.event.Observes;
import com.myapp.presentation.EntityEvent.Type;
import com.myapp.application.StudentEJBRemote;
import com.myapp.entities.Student;
import com.myapp.entities.Project;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.Header;
import com.vaadin.ui.Label;
import com.vaadin.shared.ui.label.ContentMode;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;


@CDIUI
@Theme("dawn")
public class StudentUI extends UI {

    @EJB
    private StudentEJBRemote service;
    
    @Inject
    StudentEditor studentEditor;
    
    @Inject
    ProjectEditor projectEditor;
    
    
    Label studentlabel = new Label("<b>Select student to create projects</b>", ContentMode.HTML);
    Label projectlabel = new Label("<b>Projects</b>", ContentMode.HTML);
    
    /* students table */
    MTable<Student> studentTable = new MTable()
    	.withProperties("studentid", "name", "age", "address")
    	.withColumnHeaders("ID", "Name", "Age", "Address");
    /* projects table */
    MTable<Project> projectTable = new MTable()
    	.withProperties("title", "studentBean")
    	.withColumnHeaders("Project Title", "Owner");
    
    /* Students Buttons */
    final Button deleteButton = new Button("Delete");
    final Button editButton = new Button("Edit");
    
    /*Projects Buttons*/
    final Button createButton = new Button("create project");
    final Button removeButton = new Button("remove project");

    @Override
    public void init(VaadinRequest request) {
            
            /* Find student by id */
            final MTextField searchField = new MTextField();
	    searchField.setInputPrompt("Find student by id");
	    searchField.addTextChangeListener(new FieldEvents.TextChangeListener() {
		    @Override
		    public void textChange(FieldEvents.TextChangeEvent event) {
		        
		        if(isNumeric(event.getText()) && service.findStudentById(Long.parseLong(event.getText())) != null) {
		            getStudentbyId(Long.parseLong(event.getText()));
		        } else {
		            listStudents();
		    	}
		    }	
	    });
	    
	    /* empty search field on focus */
	    searchField.addListener(new FieldEvents.FocusListener() {
	    
		    @Override
		    public void focus(FieldEvents.FocusEvent event) {
		       searchField.setValue("");
		    }
            });
	    
	    /* config students table */
	    studentTable.setSelectable(true);
	    studentTable.setImmediate(true);
	    studentTable.setSizeFull();
	    
	    /* config projects table */
	    projectTable.setSelectable(true);
	    projectTable.setImmediate(true);
	    projectTable.setSizeFull();
	    
	    /* Add student */
	    Button addButton = new Button("Add student", new Button.ClickListener() {

		    @Override
		    public void buttonClick(Button.ClickEvent event) {
		        studentEditor.setStudent(new Student());
        		addWindow(studentEditor);
		    }
	    });
	    
	    /* Edit student */
	    editButton.addClickListener(new Button.ClickListener() {
	    	    
	    	    @Override
		    public void buttonClick(ClickEvent event) {
		        studentEditor.setStudent(studentTable.getValue());
            		addWindow(studentEditor);
		    }
	    });
	    
	    /* Delete student */
	    deleteButton.addClickListener(new Button.ClickListener() {
	    	    
	    	    @Override
		    public void buttonClick(ClickEvent event) {
		        service.deleteStudent(studentTable.getValue());
		        listStudents();
		        listProjects();
		    }
	    });
	    
	    /* create project */
	    createButton.addClickListener(new Button.ClickListener() {
	    	    
	    	    @Override
		    public void buttonClick(ClickEvent event) {
		    	projectEditor.setProject(new Project());
        		addWindow(projectEditor);
		    }
	    });
            
            /* remove project */
	    removeButton.addClickListener(new Button.ClickListener() {
	    	    
	    	    @Override
		    public void buttonClick(ClickEvent event) {
		        service.deleteProject(projectTable.getValue());
		        /* refresh projects table */
		        if (studentTable.getValue() != null) {
		        	long id = studentTable.getValue().getStudentid();
				projectTable.setBeans(service.findStudentProjects(id));
				removeButton.setEnabled(false);
			} else {
				listProjects();
			}
		    }
	    });
	    
	    /* when a student is selected show his projects and enable Add/edit buttons & create project button */
	    studentTable.addMValueChangeListener(new MValueChangeListener<Student>() {

		    @Override
		    public void valueChange(MValueChangeEvent<Student> event) {
		        if (event.getValue() != null) {
				setModificationsEnabled(event.getValue() != null);
				long id = event.getValue().getStudentid();
				projectTable.setBeans(service.findStudentProjects(id));
				removeButton.setEnabled(false);
				//Notification.show(event.getValue().getProjects().size()+"");
			} else {
				listStudents();
				listProjects();
			}
		    }
		    
		    private void setModificationsEnabled(boolean b) {
		        createButton.setEnabled(b);
		        deleteButton.setEnabled(b);
		        editButton.setEnabled(b);
		    }
	    });
	    
	    /* when a project is selected enable remove project button */
	    projectTable.addMValueChangeListener(new MValueChangeListener<Project>() {

		    @Override
		    public void valueChange(MValueChangeEvent<Project> event) {
		    	if (event.getValue() != null) {
		    		removeButton.setEnabled(true);
		    	} else {
		    		removeButton.setEnabled(false);
		    	}
		    }
	    });
	    
	    
	    listStudents();
	    listProjects();
	    HorizontalSplitPanel hsplit = new HorizontalSplitPanel();
	    
	    /* set students layout */
	    hsplit.setFirstComponent(new MVerticalLayout().withFullWidth().with(studentlabel, new MHorizontalLayout().withFullWidth().with(addButton, deleteButton,editButton, searchField).withAlign(searchField, Alignment.TOP_RIGHT).expand(searchField), studentTable).expand(studentTable));
	    
	    /* set proejcts layout */
	    hsplit.setSecondComponent(new MVerticalLayout().with(projectlabel, new MHorizontalLayout().withFullWidth().with(createButton, removeButton).expand(createButton).expand(removeButton), projectTable).expand(projectTable));

	    setContent(hsplit);
    
    }
    
    /* function used to validate search field */
    public boolean isNumeric(String s) {  
        return s.matches("[-+]?\\d*\\.?\\d+");  
    }
    
    /* list all students and disable edit/delete buttons and project create button*/
    private void listStudents() {
        deleteButton.setEnabled(false);
        editButton.setEnabled(false);
        createButton.setEnabled(false);
        studentTable.setBeans(service.findStudents());
    }
    
    /* called when user clicks on save (student) */
    void saveStudent(@Observes @EntityEvent(Type.SAVE) Student student) {
        if(student.getStudentid() > 0) {
            service.updateStudent(student);
        } else {
            service.createStudent(student);
        }
        listStudents();
        removeWindow(studentEditor);
        Notification.show("Student successfully saved!");
    }
    
    /* called when user clicks on cancel (student) */
    void resetStudent(@Observes @EntityEvent(Type.REFRESH) Student student) {
        listStudents();
        removeWindow(studentEditor);
    }
    
    /* called when user clicks on save (project) */
    void saveProject(@Observes @EntityEvent(Type.SAVE) Project project) {
        service.addProjectToStudent(project, studentTable.getValue());
        long id = studentTable.getValue().getStudentid();
	projectTable.setBeans(service.findStudentProjects(id));
        removeWindow(projectEditor);
        Notification.show("Project successfully added!");
    }
    
    /* called when user clicks on cancel (student) */
    void resetProject(@Observes @EntityEvent(Type.REFRESH) Project project) {
        long id = studentTable.getValue().getStudentid();
	projectTable.setBeans(service.findStudentProjects(id));
        removeWindow(projectEditor);
    }
    
    /* list all projects and disable create/remove buttons */
    private void listProjects() {
    	createButton.setEnabled(false);
        removeButton.setEnabled(false);
        projectTable.setBeans(service.findProjects());
    }
    
    /* used in updating student table based on search field */
    private void getStudentbyId(long id) {
        studentTable.setBeans(service.findStudentById(id));
    }

}
