package com.bharuwa.haritkranti.models;

public class Employee {
	public String id;
	public String name;
	
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Employee(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
