package edu.njucs.realime.languagemodel;

import java.util.ArrayList;
import java.util.List;


public class LanguageNode {
	String key;
	List<String> keyPath=new ArrayList<String>();
	List<String> candidates=new ArrayList<String>();
	
	public LanguageNode(String key,List<String> oldKeyPath)
	{
		this.key=key;
		if (oldKeyPath!=null)
		{
			this.keyPath.addAll(oldKeyPath);
			this.keyPath.add(key);
		}
	}
}
