package edu.njucs.realime.manager;

import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.util.Log;
import edu.njucs.realime.lexicon.LexiconTree;

public class InputManager {
	private static InputManager instance=new InputManager();
	private LexiconTree lexicon;
	boolean initialized=false;
    
    static
    {
    	System.loadLibrary("language-model");
    }
	
	private InputManager()
	{
		
	}
	
	public boolean isInitialzed()
	{
		return initialized;
	}
	
	public static InputManager getInstance()
	{
		return instance;
	}

	public void setLexicon(LexiconTree lexicon) {
		this.lexicon = lexicon;
	}
	
	public List<String> split(String input)
	{
		return this.lexicon.split(input);
	}
	
	 /** Native methods, implemented in jni folder */
    public static native int cReadDict(FileDescriptor fd, long off, long len); 
    public static native int cReadTransfer(FileDescriptor fd, long off, long len);
    public static native Word[] cGetWord(String id);
    
    public int readDict(FileDescriptor fd,long off,long len)
    {
    	initialized=true;
    	return cReadDict(fd, off, len);
    }
    
    public int readTransfer(FileDescriptor fd,long off,long len)
    {
    	initialized=true;
    	return cReadTransfer(fd, off, len);
    }
    
    public Word[] getWord(String id)
    {
    	return cGetWord(id);
    }
    
    public PinyinResult parse(List<String> input) {
		PinyinResult result=new PinyinResult();
		String str="";
		for (int i=0;i<input.size();i++)
		{
			str+=input.get(i);
			if (i!=input.size()-1)
			{
				str+='\'';
			}
		}
		Word[] strs=InputManager.getInstance().getWord(str);
//		if (strs.length==0)
//		{
//			strs=InputManager.getInstance().getWord(getSpecialAbbreviation(str));
//		}
		for (int i=0;i<strs.length;i++)
			result.wordCandidates.add(strs[i]);

		return result;
	}
    
    public String getSpecialAbbreviation(String pinyin)
    {
    	return "'"+getAbbreviation(pinyin);
    }
    
    public String getAbbreviation(String pinyin)
	{
		String abbr="";
		String[] parts=pinyin.split("'");
		for (int i=0;i<parts.length;i++)
		{
			if (parts[i].length()==0)
			{
				System.out.println(pinyin);
			}
			abbr+=parts[i].charAt(0);
			if (parts[i].length()>1 && parts[i].charAt(1)=='h')
			{
				abbr+=parts[i].charAt(1);
			}
			if (i!=parts.length-1)
			{
				abbr+="'";
			}
		}
		return abbr;
	}
    
//    public PinyinResult parse(long code)
//    {
//    	PinyinResult result=new PinyinResult();
//    	Word[] strs=InputManager.getInstance().getWord(code);
//		for (int i=0;i<strs.length;i++)
//			result.wordCandidates.add(strs[i]);
//
//		return result;
//    }
    
    public List<Candidate> getAllCandidatesForMultipleInput(Collection<List<String>> inputs)
    {
    	Set<Candidate> set=new HashSet<Candidate>();
    	Map<String,Boolean> parseMap=new HashMap<String, Boolean>();
//    	List<String> originInput=inputs.get(0);
    	Log.d("realime", "start parsing");
    	for (List<String> input:inputs)
    	{
    		for (int i=input.size();i>0;i--)
    		{
    			String str="";
    			for (int j=0;j<i;j++)
    			{
    				str+=input.get(j);
    				if (j!=i-1)
    				{
    					str+='\'';
    				}
    			}
    			if (parseMap.containsKey(str))
    				continue;
    			else
    			{
    				parseMap.put(str, true);
    			}
    			

    			PinyinResult result=this.parse(input.subList(0, i));
    			for (int j=0;j<result.wordCandidates.size();j++)
    			{
    				List<String> restInput=new ArrayList<String>();
    				restInput=input.subList(i, input.size());
//    				restInput.addAll(result.restInput);
//    				restInput.addAll(input.subList(i, input.size()));
    				Candidate candidate=new Candidate(result.wordCandidates.get(j), restInput);
    				set.add(candidate);
    			}
    		}
    	}
    	Log.d("realime", "sort");
    	List<Candidate> list=new ArrayList<Candidate>(set);
    	if (list.size()>1)
    		Collections.sort(list,new CandidateComparatorByPriority());
    	Log.d("realime", "complete");
    	return list;
    }
	
	public List<Candidate> getAllCandidates(List<String> input) {
		List<Candidate> candidates=new ArrayList<Candidate>();
		for (int i=input.size();i>0;i--)
		{
			PinyinResult result=this.parse(input.subList(0, i));
			for (int j=0;j<result.wordCandidates.size();j++)
			{
				List<String> restInput=new ArrayList<String>();
				restInput.addAll(result.restInput);
				restInput.addAll(input.subList(i, input.size()));
				Candidate candidate=new Candidate(result.wordCandidates.get(j), restInput);
				if (!candidates.contains(candidate))
					candidates.add(candidate);
			}
		}
//		Collections.sort(candidates,new CandidateComparatorByPriority());
		return candidates;
	}	
	
	public long trans(String str)
	{
		long code=0;
		for (int j=0;j<str.length();j++)
		{
			if (str.charAt(j)!='\'')
			{
				code=code*27l+(str.charAt(j)-'a' + 1);
			}
			else
			{
				code=code*27l+27l;
			}
			
		}
		return code;
	}
	
	public long trans(List<String> list,int start,int end)
	{
		long code=0;
		for (int i=start;i<end;i++)
		{
			String str=list.get(i);
			for (int j=0;j<str.length();j++)
			{
				code=code*27l+(str.charAt(j)-'a' + 1);
			}
			if (i!=end-1)
			{
				code=code*27l+27l;
			}
		}
		return code;
	}
	
	public Collection<List<String>> getAllPossibleSplit(String input)
	{
		return lexicon.getAllPossibleSplit(input);
	}
}

class CandidateComparatorByPriority implements Comparator<Candidate>
{

	public int compare(Candidate lhs, Candidate rhs) {
		if (lhs.restInput.size()==rhs.restInput.size())
			return -(lhs.word.priority-rhs.word.priority);
		else
			return lhs.restInput.size()-rhs.restInput.size();
	}

	
}
