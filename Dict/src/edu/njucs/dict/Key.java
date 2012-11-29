package edu.njucs.dict;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Key {
	String value;
	Set<Word> refs=new HashSet<Word>();
	int freqency=0;
	
	public Key(String pinyin)
	{
		value=pinyin;
	}
	
	public void add(Word word)
	{
		refs.add(word);
		freqency+=word.freqency;
	}
	
}
