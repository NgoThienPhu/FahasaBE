package com.example.demo.util.view;

public interface View {
	
	interface Public {}
	
    interface Internal extends Public, Self {}
    
    interface Admin extends Internal {}
    
    
    interface Self extends Public {}

}
