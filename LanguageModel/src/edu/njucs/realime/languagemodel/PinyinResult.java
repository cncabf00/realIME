package edu.njucs.realime.languagemodel;

import java.util.ArrayList;
import java.util.List;

public class PinyinResult {
	List<String> wordCandidates=new ArrayList<String>();
	List<String> restInput=new ArrayList<String>();
	
	public List<String> getWordCandidates() {
		return wordCandidates;
	}
	public List<String> getRestInput() {
		return restInput;
	}
	
	
}
