package edu.njucs.realime.languagemodel;

import java.io.IOException;
import java.io.InputStream;

public class DictFileParser {
	
	public static Word parseLine(String line)
	{
		Word word=new Word();
		int sep1=0;
		int sep2=0;
		int cur=0;
		int num=0;
		for (int i=0;i<line.length();i++)
		{
			if (line.charAt(i)==' ')
			{
				if (cur==0)
					sep1=i;
				else
				{
					sep2=i;
					break;
				}
				cur++;
			}
			else if (cur==0 && line.charAt(i)=='\'')
			{
				num++;
			}
		}
		word.characters=line.substring(sep1+1, sep2);
		word.pinyins=new String[num+1];
		cur=0;
		int count=0;
		for (int i=0;i<sep1;i++)
		{
			if (line.charAt(i)=='\'')
			{
				word.pinyins[count++]=line.substring(cur, i);
				cur=i+1;
			}
		}
		if (cur!=sep1)
			word.pinyins[count++]=line.substring(cur, sep1);
		return word;
	}
	
	public Word[] parse(InputStream input)
	{
		try
		{
			byte[] readBytes = new byte[input.available()];
			input.read(readBytes);
			String string4file = new String(readBytes,"utf-8");
			String[] lines=string4file.split("\n");
			Word[] words=new Word[lines.length];
			for (int k=0;k<lines.length;k++)
			{
				words[k]=parseLine(lines[k]);
			}

			return words;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
