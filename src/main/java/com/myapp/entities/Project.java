package com.myapp.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the project database table.
 * 
 */
@Entity
@Table(name="project")
public class Project implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int projectid;

	private String title;

	//bi-directional many-to-one association to Student
	@ManyToOne
	@JoinColumn(name="student")
	private Student studentBean;

	public Project() {
	}

	public int getProjectid() {
		return this.projectid;
	}

	public void setProjectid(int projectid) {
		this.projectid = projectid;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Student getStudentBean() {
		return this.studentBean;
	}

	public void setStudentBean(Student studentBean) {
		this.studentBean = studentBean;
	}
	
	@Override
	public String toString() {
	    return title + " " + projectid + " " + studentBean;
	}

}
