package edu.njucs.realime.languagemodel;

import java.util.List;

public class Candidate{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1472340313806828656L;
	String text;
	List<String> restInput;
	
	public Candidate(String text, List<String> restInput) {
		super();
		this.text = text;
		this.restInput = restInput;
	}

	public String getText() {
		return text;
	}



	public List<String> getRestInput() {
		return restInput;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((restInput == null) ? 0 : restInput.hashCode());
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
		Candidate other = (Candidate) obj;
		if (restInput == null) {
			if (other.restInput != null)
				return false;
		} else if (!restInput.equals(other.restInput))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}
	
	
}
