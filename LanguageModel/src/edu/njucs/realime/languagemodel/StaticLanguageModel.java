package edu.njucs.realime.languagemodel;
import java.io.InputStream;
import java.util.List;


public interface StaticLanguageModel {
	
	
	public void build(InputStream input);
	
	public void build(Word[] dict);
	
	public void append(InputStream input);
	
	public void append(Word[] dict);
	
	public PinyinResult parse(List<String> input);
	
	public List<Candidate> getAllCandidates(List<String> input);
}
