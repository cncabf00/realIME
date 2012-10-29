package edu.njucs.realime.manager;


public class Word implements java.io.Serializable{
	int priority;
	String text;
	
	public Word()
	{
		
	}
	
	public Word(String text,int priority)
	{
		this.priority=priority;
		this.text=text;
	}

	public int getPriority() {
		return priority;
	}

	public String getText() {
		return text;
	}
	
}
