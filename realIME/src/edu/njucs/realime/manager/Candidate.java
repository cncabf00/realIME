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

	
	
}
