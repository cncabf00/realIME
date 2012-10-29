package edu.njucs.realime.languagemodel;

import java.util.HashSet;
import java.util.Set;


public class LanguageNode implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5298815415899901356L;
	Set<String> candidates=null;
	
	public LanguageNode()
	{
	}
	
	public void addCandidate(String string)
	{
		if (candidates==null)
			candidates=new HashSet<String>();
		candidates.add(string);
	}
}
