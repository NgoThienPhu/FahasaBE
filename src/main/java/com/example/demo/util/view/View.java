package com.example.demo.util.view;

public interface View {
	
	interface Public {}
	
    interface Internal extends Public {}
    
    interface Admin extends Internal, Self {}
    
    
    interface Self extends Public {}

}
