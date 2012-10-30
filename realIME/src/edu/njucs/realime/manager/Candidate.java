package edu.njucs.realime.manager;

import java.util.List;

public class Candidate{
	Word word;
	List<String> restInput;
	
	public Candidate(Word word, List<String> restInput) {
		super();
		this.word = word;
		this.restInput = restInput;
	}

	public Word getWord() {
		return word;
	}

	public List<String> getRestInput() {
		return restInput;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
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
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}

	
	
}
