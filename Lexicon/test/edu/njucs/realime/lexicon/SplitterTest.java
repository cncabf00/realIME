package edu.njucs.realime.lexicon;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SplitterTest {

	@Test
	public void test() {
		File file=new File("pinyin.txt");
		try
		{
			BufferedReader reader=new BufferedReader(new FileReader(file));
			List<String> list=new ArrayList<String>();
			String line=reader.readLine();
			while (line!=null)
			{
				list.add(line);
				line=reader.readLine();
			}
			reader.close();
			
			LexiconFileParser parser=new LexiconFileParser();
			
			PinyinSplitter splitter=new PinyinSplitter(parser.parse(list));
			
			List<String> results=splitter.split("mingtian");
			assertEquals(2, results.size());
			assertEquals("ming", results.get(0));
			assertEquals("tian", results.get(1));
			
			results=splitter.split("chanmian");
			assertEquals(2, results.size());
			assertEquals("chan", results.get(0));
			assertEquals("mian", results.get(1));
			
			results=splitter.split("ajimide");
			assertEquals(4, results.size());
			assertEquals("a", results.get(0));
			assertEquals("ji", results.get(1));
			assertEquals("mi", results.get(2));
			assertEquals("de", results.get(3));
			
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
