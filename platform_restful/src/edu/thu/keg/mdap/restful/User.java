package edu.thu.keg.mdap.restful;

public class User{
	private String name;
	private int age;
	User(String name , int age)
	{
		this.name=name;
		this.age=age;
	}
	public void setNAme(String name)
	{
		this.name=name;
	}
	public String getname()
	{
		return name;
	}
	public void setage(int age)
	{
		this.age=age;
	}
//	public int getage()
//	{
//		return age;
//	}
	
}
