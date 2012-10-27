package edu.njucs.realime.languagemodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DictFileParser {
	public List<Word> parse(InputStream input)
	{
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		List<Word> list=new ArrayList<Word>();
		try
		{
			String line=reader.readLine();
			while (line!=null)
			{
				String[] strs=line.split(" ");
				String[] pinyins=strs[0].split("'");
				Word word=new Word();
				for (int i=0;i<strs[1].length();i++)
				{
					ChineseCharacter cc=new ChineseCharacter();
					cc.key=pinyins[i];
					cc.text=""+strs[1].charAt(i);
					word.characters.add(cc);
				}
				list.add(word);
				line=reader.readLine();
			}
			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
}
