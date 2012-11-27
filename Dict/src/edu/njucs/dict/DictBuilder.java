package edu.njucs.dict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DictBuilder {
	Map<String,Key> keyMap=new HashMap<String, Key>();
	Map<String,Set<String>> pinyinMap=new HashMap<String, Set<String>>();
	FileWriter logWriter;
	
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
		System.out.println("DictBuilder start building");
		Reader reader=new Reader();
		try {
			List<Word> stats=reader.readStatFile("stat.txt");
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
	
	public void writeFile(String filename)
	{
		System.out.println("DictBuilder start writing file");
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
			for (Map.Entry<String, Key> e:keyMap.entrySet())
			{
				writer.append(e.getKey()+","+e.getValue().freqency+" ");
				for (Word word:e.getValue().refs)
				{
					writer.append(word.name+","+word.freqency+";");
				}
				writer.append("\n");
				if (e.getKey().contains("["))
				{
					
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
		}
		return pinyin;
	}
	
	public static void main(String[] argv)
	{
		DictBuilder builder=new DictBuilder();
		builder.build();
		builder.writeFile("output.txt");
	}
}
