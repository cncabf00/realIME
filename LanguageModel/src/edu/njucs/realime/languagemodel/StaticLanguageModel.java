package edu.njucs.realime.languagemodel;
import java.util.List;


public interface StaticLanguageModel {
	
	public void build(List<Word> dict);
	
	public PinyinResult parse(List<String> input);
}
