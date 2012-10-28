package edu.njucs.realime.languagemodel;

import java.io.IOException;
import java.io.InputStream;

//import android.util.Log;

public class DictFileParser {
	public Word[] parse(InputStream input)
	{
//		List<Word> list=new ArrayList<Word>();
		
		try
		{
//			Log.d("realime","start read file");
			byte[] readBytes = new byte[input.available()];
			input.read(readBytes);
			String string4file = new String(readBytes,"utf-8");
//			Log.d("realime","end read file");
//			Log.d("realime","split");
			String[] lines=string4file.split("\n");
//			Log.d("realime","start parse file");
			Word[] words=new Word[lines.length];
			for (int k=0;k<lines.length;k++)
			{
				String[] strs=lines[k].split("\\s");
				String[] pinyins=strs[0].split("'");
				Word word=new Word();
				word.pinyins=pinyins;
				word.characters=strs[1];
				words[k]=word;
			}
			
//			Log.d("realime","end parse file");
			return words;
			
//			InputStreamReader ir=new InputStreamReader(input,"utf-8");
//			BufferedReader reader=new BufferedReader(ir);
//			String line=reader.readLine();
//			while (line!=null)
//			{
//				String[] strs=line.split("\\s");
//				String[] pinyins=strs[0].split("'");
//				Word word=new Word();
//				word.characters=new ChineseCharacter[strs[1].length()];
//				for (int i=0;i<strs[1].length();i++)
//				{
//					ChineseCharacter cc=new ChineseCharacter();
//					cc.key=pinyins[i];
//					cc.text=""+strs[1].charAt(i);
//					word.characters[i]=cc;
//				}
//				list.add(word);
//				line=reader.readLine();
//			}
//			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
