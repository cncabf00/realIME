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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Word other = (Word) obj;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
	
}
