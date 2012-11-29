package edu.njucs.dict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DictBuilder {
	Map<String,Key> keyMap=new HashMap<String, Key>();
	Map<String,Set<String>> pinyinMap=new HashMap<String, Set<String>>();
	FileWriter logWriter;
	Map<String,HiddenKey> hiddenKeyMap=new HashMap<String, HiddenKey>();
	Map<String,Key> preserveMap=new HashMap<String, Key>();
	
	public DictBuilder()
	{
		System.out.println("DictBuilder start initializing");
		Reader reader=new Reader();
		try {
			List<Word> words=reader.readRawDict("word.txt");
			words.addAll(reader.readRawDict("characters.txt"));
			words.addAll(reader.readRawDict("name.txt"));
			words.addAll(reader.readRawDict("name1.txt"));
			words.addAll(reader.readRawDict("name2.txt"));
			words.addAll(reader.readRawDict("name3.txt"));
			words.addAll(reader.readRawDict("name4.txt"));
			words.addAll(reader.readRawDict("name5.txt"));
			words.addAll(reader.readRawDict("name6.txt"));
			words.addAll(reader.readRawDict("name7.txt"));
			words.addAll(reader.readRawDict("name8.txt"));
			words.addAll(reader.readRawDict("football.txt"));
			words.addAll(reader.readRawDict("word1.txt"));
			words.addAll(reader.readRawDict("word2.txt"));
			words.addAll(reader.readRawDict("word3.txt"));
			words.addAll(reader.readRawDict("word4.txt"));
			words.addAll(reader.readRawDict("word5.txt"));
			words.addAll(reader.readRawDict("westname.txt"));
			words.addAll(reader.readRawDict("geography.txt"));
			words.addAll(reader.readRawDict("placename.txt"));
			words.addAll(reader.readRawDict("placename1.txt"));
			words.addAll(reader.readRawDict("2.txt"));
			words.addAll(reader.readRawDict("eco.txt"));
			words.addAll(reader.readRawDict("log_fix.txt"));
			for (Word word:words)
			{
				if (pinyinMap.containsKey(word.name))
				{
					pinyinMap.get(word.name).add(word.pinyin);
				}
				else
				{
					Set<String> set=new HashSet<String>();
					set.add(word.pinyin);
					pinyinMap.put(word.name, set);
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		File log=new File("log.txt");
		if (log.exists())
		{
			log.delete();
		}
		try {
			log.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			logWriter=new FileWriter(log);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void build()
	{
		buildDict();
		buildTransfer();
	}
	
	void buildDict()
	{
		System.out.println("DictBuilder start building dict");
		Reader reader=new Reader();
		try {
			List<Word> stats=reader.readStatFile("stat.txt");
			stats.addAll(reader.readRawDict("characters.txt"));
			for (Word word:stats)
			{
				try {
					String pinyin=inferPinyin(word.name);
					word.pinyin=pinyin;
					if (keyMap.containsKey(pinyin))
					{
						keyMap.get(pinyin).add(word);
					}
					else
					{
						Key key=new Key(pinyin);
						key.add(word);
						keyMap.put(pinyin, key);
					}
				} catch (Exception e) {
					System.out.println(e.toString());
					continue;
				}
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void buildTransfer()
	{
		preserveAll();
		for (Key key:keyMap.values())
		{
			List<HiddenKey> hiddenKeys=inferAllHiddenKeys(key);
			for (HiddenKey hk:hiddenKeys)
			{
				if (hiddenKeyMap.containsKey(hk.value))
				{
					HiddenKey oldHiddenKey=hiddenKeyMap.get(hk.value);
					oldHiddenKey.addAll(hk.refs);
				}
				else
				{
					hiddenKeyMap.put(hk.value, hk);
				}
			}
		}
	}
	
	public List<HiddenKey> inferAllHiddenKeys(Key key)
	{
		List<HiddenKey> hiddenKeys=new ArrayList<HiddenKey>();
		String specialAbbr=getSpecialAbbreviation(key.value);
		if (preserveMap.containsKey(specialAbbr))
		{
			HiddenKey hk=new HiddenKey(specialAbbr);
			hk.add(key);
			hiddenKeys.add(hk);
		}
		else
		{
			List<String> abbrs=getPartialAbbreviations(key.value);
			for (String a:abbrs)
			{
				HiddenKey hk=new HiddenKey(a);
				hk.add(key);
				hiddenKeys.add(hk);
			}
		}
		return hiddenKeys;
	}
	
	public void preserveAll()
	{
		List<String> toRemove=new ArrayList<String>();
		for (Key key:keyMap.values())
		{
			String abbr=getSpecialAbbreviation(key.value);
			if (!preserveMap.containsKey(abbr))
			{
				preserveMap.put(abbr,key);
			}
			else
			{
				toRemove.add(abbr);
			}
		}
		for (String str:toRemove)
		{
			preserveMap.remove(str);
		}
//		for (Map.Entry<String, Key> e:preserveMap.entrySet())
//		{
//			HiddenKey hk=new HiddenKey(e.getKey());
//			hk.add(e.getValue());
//			hiddenKeyMap.put(e.getKey(),hk);
//		}
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
	
	public List<String> getPartialAbbreviations(String pinyin)
	{
		List<String> abbrs=new ArrayList<String>();
		String[] parts=pinyin.split("'");
		int max=1;
		int length=parts.length;
		for (int i=0;i<length;i++)
		{
			max*=2;
		}
		for (int flag=1;flag<max;flag++)
		{
			int p=1;
			String abbr="";
			for (int i=0;i<length;i++)
			{
				if ((flag&p)!=0)
				{
					abbr+=parts[i].charAt(0);
					if (parts[i].length()>1 && parts[i].charAt(1)=='h')
					{
						abbr+=parts[i].charAt(1);
					}
				}
				else
				{
					abbr+=parts[i];
				}
				if (i!=length-1)
				{
					abbr+="'";
				}
				p=p<<1;
			}
			abbrs.add(abbr);
		}
		return abbrs;
	}
	
	public void writeDictFile(String filename)
	{
		System.out.println("DictBuilder start writing dict file "+filename);
		File file=new File(filename);
		if (file.exists())
		{
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			FileWriter writer=new FileWriter(file);
			int lineSize=keyMap.size();
			int line=0;
			for (Map.Entry<String, Key> e:keyMap.entrySet())
			{
				if (!e.getKey().contains("'"))
					continue;
				line++;
				writer.append(e.getKey()+" ");
				int size=e.getValue().refs.size();
				int count=0;
				for (Word word:e.getValue().refs)
				{
					count++;
					writer.append(word.name+","+word.freqency);
					if (count!=size)
					{
						writer.append(";");
					}
				}
				if (line!=lineSize)
				{
					writer.append("\n");
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeTransferFile(String filename)
	{
		System.out.println("DictBuilder start writing transfer file "+filename);
		File file=new File(filename);
		if (file.exists())
		{
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			FileWriter writer=new FileWriter(file);
			int lineSize=hiddenKeyMap.size();
			int line=0;
			for (Map.Entry<String, HiddenKey> e:hiddenKeyMap.entrySet())
			{
				line++;
				writer.append(e.getValue().value+" ");
				int size=e.getValue().refs.size();
				int count=0;
				for (Key key:e.getValue().refs)
				{
					count++;
					writer.append(key.value+","+key.freqency);
					if (count!=size)
					{
						writer.append(";");
					}
				}
				if (line!=lineSize)
				{
					writer.append("\n");
				}
			}
			writer.flush();
			writer.close();
			logWriter.flush();
			logWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	String inferPinyin(String text) throws Exception
	{
		String pinyin="";
		int start=0;
		int end=text.length();
		int cur=end;
		boolean wild=false;
		while (start!=end)
		{
			if (start==cur)
			{
				throw new Exception("word "+ text+"'s pinyin cannot be infered");
			}
			String t=text.substring(start, cur);
			if (pinyinMap.containsKey(t))
			{
				Set<String> set=pinyinMap.get(t);
				if (start==cur-1 && set.size()>1)
					wild=true;
				if (wild)
				{
					pinyin+=set;
				}
				else
				{
					pinyin+=set.toArray(new String[0])[0];
				}
				start=cur;
				cur=end;
				if (start!=end)
				{
					pinyin+="'";
				}
			}
			else
			{
				cur--;
			}
		}
		if (wild)
		{
			logWriter.append(pinyin+" ");
			logWriter.append(text);
			logWriter.append("\n");
			throw new Exception("word "+ text+"'s pinyin has multiple inferences");
		}
		return pinyin;
	}
	
	public static void main(String[] argv)
	{
		DictBuilder builder=new DictBuilder();
		builder.build();
		builder.writeDictFile("output_dict.txt");
		builder.writeTransferFile("output_transfer.txt");
	}
}
