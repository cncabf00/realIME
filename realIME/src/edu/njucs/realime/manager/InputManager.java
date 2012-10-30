package edu.njucs.realime.manager;

import java.io.FileDescriptor;
import java.util.ArrayList;
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
    
    static
    {
    	System.loadLibrary("language-model");
    }
	
	private InputManager()
	{
		
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
    public static native int cReadDict(FileDescriptor fd, long off, long 
    		len); 
    public static native Word[] cGetWord(long id);
    
    public int readDict(FileDescriptor fd,long off,long len)
    {
    	return cReadDict(fd, off, len);
    }
    
    public Word[] getWord(long id)
    {
    	return cGetWord(id);
    }
    
    public PinyinResult parse(List<String> input) {
		PinyinResult result=new PinyinResult();
		String str="";
		for (int i=0;i<input.size();i++)
			str+=input.get(i);
		Word[] strs=InputManager.getInstance().getWord(trans(str));
		for (int i=0;i<strs.length;i++)
			result.wordCandidates.add(strs[i]);

		return result;
	}
    
    public PinyinResult parse(int code)
    {
    	PinyinResult result=new PinyinResult();
    	Word[] strs=InputManager.getInstance().getWord(code);
		for (int i=0;i<strs.length;i++)
			result.wordCandidates.add(strs[i]);

		return result;
    }
    
    public List<Candidate> getAllCandidatesForMultipleInput(List<List<String>> inputs)
    {
    	Set<Candidate> set=new HashSet<Candidate>();
    	Map<String,Boolean> parseMap=new HashMap<String, Boolean>();
    	List<String> originInput=inputs.get(0);
    	Log.d("realime", "start parsing");
    	for (int k=0;k<inputs.size();k++)
    	{
    		Log.d("realime", "input "+k);
    		List<String> input=inputs.get(k);
    		for (int i=input.size();i>0;i--)
    		{
    			String str="";
    			for (int j=0;j<i;j++)
    			{
    				str+=input.get(j);
    			}
    			if (parseMap.containsKey(str))
    				continue;
    			else
    			{
    				parseMap.put(str, true);
    			}

    			PinyinResult result=this.parse(trans(input,0, i));
    			for (int j=0;j<result.wordCandidates.size();j++)
    			{
    				List<String> restInput=new ArrayList<String>();
    				restInput=originInput.subList(originInput.size()-result.restInput.size()-(input.size()-i), originInput.size());
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
		Collections.sort(candidates,new CandidateComparatorByPriority());
		return candidates;
	}	
	
	public int trans(String str)
	{
		int code=0;
		for (int j=0;j<str.length();j++)
		{
			code=code*26+(str.charAt(j)-'a' + 1);
		}
		return code;
	}
	
	public int trans(List<String> list,int start,int end)
	{
		int code=0;
		for (int i=start;i<end;i++)
		{
			String str=list.get(i);
			for (int j=0;j<str.length();j++)
			{
				code=code*26+(str.charAt(j)-'a' + 1);
			}
		}
		return code;
	}
	public List<List<String>> getAllPossibleSplit(String input,int maxFill)
	{
		return lexicon.getAllPossibleSplit(input, maxFill);
	}
	
	public List<List<String>> getAllPossibleSplit(String input)
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
