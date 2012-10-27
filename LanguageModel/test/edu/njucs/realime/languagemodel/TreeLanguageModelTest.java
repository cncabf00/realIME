package edu.njucs.realime.languagemodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TreeLanguageModelTest {

	@Test
	public void test() {
		File file=new File("word.txt");
		try
		{
			BufferedReader reader=new BufferedReader(new FileReader(file));
			List<Word> list=new ArrayList<Word>();
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
			
			StaticLanguageModel model=new TreeLangageModel();
			
			model.build(list);

			List<String> input=new ArrayList<String>();
			input.add("ming");
			input.add("tian");
			
			PinyinResult result=model.parse(input);
			
			for (int i=0;i<result.wordCandidates.size();i++)
			{
				System.out.println(result.wordCandidates.get(i));
			}
			
			input=new ArrayList<String>();
			input.add("cao");
			input.add("ni");
			input.add("ma");
			
			result=model.parse(input);
			
			for (int i=0;i<result.wordCandidates.size();i++)
			{
				System.out.println(result.wordCandidates.get(i));
			}
			
		}
		catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
				e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
