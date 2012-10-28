package edu.njucs.realime.languagemodel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import edu.njucs.realime.lexicon.LexiconFileParser;
import edu.njucs.realime.lexicon.LexiconTree;

public class TreeLanguageModelWriter {
	public static void main(String[] args) {
		File file = new File("word_new.txt");
		try {

			DictFileParser parser = new DictFileParser();
			Word[] list = parser.parse(new FileInputStream(file));
			StaticLanguageModel model = new TreeLangageModel();
			model.build(list);
			list = parser.parse(new FileInputStream(new File("characters.txt")));
			model.append(list);

			FileOutputStream fileOut = new FileOutputStream("dict.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(model);
			out.close();
			fileOut.close();

			LexiconFileParser parser1 = new LexiconFileParser();
			LexiconTree tree = new LexiconTree();
			tree.build(parser1
					.parse(new FileInputStream(new File("pinyin.txt"))));

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
