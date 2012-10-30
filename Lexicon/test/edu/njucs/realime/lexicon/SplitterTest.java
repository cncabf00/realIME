package edu.njucs.realime.lexicon;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;

public class SplitterTest {

	@Test
	public void test() {
		File file=new File("pinyin.txt");
		try
		{
			LexiconFileParser parser=new LexiconFileParser();
			
			
			List<String> list=parser.parse(new FileInputStream(file));
			
			LexiconTree lexiconTree=new LexiconTree();
			
			lexiconTree.build(list);
			
			List<String> results=lexiconTree.split("mingtian");
			assertEquals(2, results.size());
			assertEquals("ming", results.get(0));
			assertEquals("tian", results.get(1));
			
			results=lexiconTree.split("chanmian");
			assertEquals(2, results.size());
			assertEquals("chan", results.get(0));
			assertEquals("mian", results.get(1));
			
			results=lexiconTree.split("ajimide");
			assertEquals(4, results.size());
			assertEquals("a", results.get(0));
			assertEquals("ji", results.get(1));
			assertEquals("mi", results.get(2));
			assertEquals("de", results.get(3));
			
			results=lexiconTree.split("xi'an");
			assertEquals(2, results.size());
			assertEquals("xi", results.get(0));
			assertEquals("an", results.get(1));
			
			List<List<String>> allSplits=lexiconTree.getAllPossibleSplit("wozhe", 2);
			for (int i=0;i<allSplits.size();i++)
			{
				for (int j=0;j<allSplits.get(i).size();j++)
				{
					System.out.print(allSplits.get(i).get(j)+"'");
				}
				System.out.println();
			}
			
		}
		catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
				e1.printStackTrace();
		}
	}

}
