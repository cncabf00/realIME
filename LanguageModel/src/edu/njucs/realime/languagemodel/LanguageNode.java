package edu.njucs.realime.languagemodel;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


public class LanguageNode implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5298815415899901356L;
	String key;
//	List<String> keyPath=new ArrayList<String>();
	Set<String> candidates=null;
	
	public LanguageNode(String key)
	{
		this.key=key;
	}
	
	public void addCandidate(String string)
	{
		if (candidates==null)
			candidates=new HashSet<String>();
		candidates.add(string);
	}
}
