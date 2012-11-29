package edu.njucs.realime.manager;

import java.util.ArrayList;
import java.util.List;

public class PinyinResult {
	List<Word> wordCandidates=new ArrayList<Word>();
	List<String> restInput=new ArrayList<String>();
	String input;
	
	public List<Word> getWordCandidates() {
		return wordCandidates;
	}
	public List<String> getRestInput() {
		return restInput;
	}
	
	
}
