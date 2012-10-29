package edu.njucs.realime.languagemodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.njucs.realime.lexicon.LexiconFileParser;
import edu.njucs.realime.lexicon.LexiconTree;
import edu.njucs.realime.manager.Candidate;


public class TreeLanguageModelTest {

	@Test
	public void test() {
		System.out.println("TEST0");
		
		File file=new File("word_new_all.txt");
		try
		{
			DictFileParser parser=new DictFileParser();
			TreeLangageModel model=new TreeLangageModel();
			model.build(new FileInputStream(file));
			model.append(new FileInputStream(new File("characters.txt")));
			
			System.out.println("node count= "+model.nodeCount);

			List<String> input=new ArrayList<String>();
			input.add("ming");
			input.add("tian");
			input.add("tian");
			
			List<Candidate> candidates=model.getAllCandidates(input);
			
			for (int i=0;i<candidates.size();i++)
			{
				System.out.print(candidates.get(i).text);
				for (int j=0;j<candidates.get(i).restInput.size();j++)
				{
					System.out.print(candidates.get(i).restInput.get(j));
				}
				System.out.println();
			}
			
		}
		catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
				e1.printStackTrace();
		}
	}
	
//	@Test
//	public void test1()
//	{
//		System.out.println("TEST1");
//		
//		File file=new File("word.txt");
//		try
//		{
//			
//			DictFileParser parser=new DictFileParser();
//			Word[] list=parser.parse(new FileInputStream(file));
//			StaticLanguageModel model=new TreeLangageModel();
//			model.build(list);
//			list=parser.parse(new FileInputStream(new File("characters.txt")));
//			model.append(list);
//
//			LexiconFileParser parser1=new LexiconFileParser();
//			LexiconTree tree=new LexiconTree();
//			tree.build(parser1.parse(new FileInputStream(new File("pinyin.txt"))));
//			
//			String inputString="mingtiantian";
//			List<String> input=tree.split(inputString);
//			
//			List<Candidate> candidates=model.getAllCandidates(input);
//			
//			for (int i=0;i<candidates.size();i++)
//			{
//				System.out.print(candidates.get(i).text);
//				for (int j=0;j<candidates.get(i).restInput.size();j++)
//				{
//					System.out.print(candidates.get(i).restInput.get(j));
//				}
//				System.out.println();
//			}
//			
//		}
//		catch (FileNotFoundException e1) {
//			// TODO Auto-generated catch block
//				e1.printStackTrace();
//		}
//	}
	
//	@Test
//	public void test2()
//	{
//		try {
//			TreeLangageModel model=TreeLanguageModelReader.readObject(new FileInputStream(new File("dict1.ser")));
//			
//			LexiconFileParser parser1=new LexiconFileParser();
//			LexiconTree tree=new LexiconTree();
//			tree.build(parser1.parse(new FileInputStream(new File("pinyin.txt"))));
//			
//			String inputString="mingtiantian";
//			List<String> input=tree.split(inputString);
//			
//			List<Candidate> candidates=model.getAllCandidates(input);
//			
//			for (int i=0;i<candidates.size();i++)
//			{
//				System.out.print(candidates.get(i).text);
//				for (int j=0;j<candidates.get(i).restInput.size();j++)
//				{
//					System.out.print(candidates.get(i).restInput.get(j));
//				}
//				System.out.println();
//			}
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
