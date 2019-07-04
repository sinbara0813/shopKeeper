package com.d2cmall.shopkeeper.common;

public class Person {

	public Person(int age){
		this.age=age;
	}
	
	public int age;
	public String name;
	
	
	@Override
	public String toString() {
		return "age=="+age;
	}
}
