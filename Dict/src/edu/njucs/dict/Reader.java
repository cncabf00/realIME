package edu.njucs.dict;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Reader {
	
	public List<Word> readRawDict(String filename) throws FileNotFoundException {
		System.out.println("read file "+filename);
		List<Word> words = new ArrayList<Word>();
		BufferedReader br = new BufferedReader(new FileReader(
				new File(filename)));
		try {
			String line = br.readLine();
			while (line != null) {
				try
				{
					String[] strs = line.split(" ");
					Word word = new Word();
					word.pinyin = strs[0];
					word.name = strs[1];
					words.add(word);
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
					System.out.println(line);
				}
				line = br.readLine();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return words;
	}

	public List<Word> readStatFile(String filename)
			throws FileNotFoundException {
		List<Word> words = new ArrayList<Word>();
		BufferedReader br = new BufferedReader(new FileReader(
				new File(filename)));
		try {
			String line = br.readLine();
			while (line != null) {
				String[] strs = line.split(" ");
				Word word = new Word();
				word.name = strs[0];
				word.freqency = Integer.parseInt(strs[1]);
				words.add(word);
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return words;
	}
	
}
