package com.example.demo.utils.view;

public interface View {
	
	interface Public {}
	
	interface User extends Public {}
	
	interface Self extends Public {}
	
	interface Employee extends User, Self {}
	
	interface Internal extends Employee {}
	
	interface Admin extends Internal {}

}
