package com.myapp.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;


/**
 * The persistent class for the student database table.
 * 
 */

@Entity
@Table(name="student")
public class Student implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long studentid;

	private String address;

	private int age;

	private String name;

	//bi-directional many-to-one association to Project
	//@OneToMany(cascade = CascadeType.ALL, mappedBy="studentBean")
	@OneToMany(mappedBy="studentBean", orphanRemoval=true)
	private List<Project> projects = new ArrayList<Project>();

	public Student() {
	}

	public long getStudentid() {
		return this.studentid;
	}

	public void setStudentid(long studentid) {
		this.studentid = studentid;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public Project addProject(Project project) {
		getProjects().add(project);
		project.setStudentBean(this);

		return project;
	}

	public Project removeProject(Project project) {
		getProjects().remove(project);
		project.setStudentBean(null);

		return project;
	}
	
	@Override
	public String toString() {
	        return this.name;
	}

}
