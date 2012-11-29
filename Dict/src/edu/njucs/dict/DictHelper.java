package edu.njucs.dict;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DictHelper {
	public static void main(String[] argv)
	{
		Reader reader=new Reader();
		try {
			List<Word> words=reader.readRawDict("characters.txt");
			FileWriter writer=new FileWriter(new File("newCharatcers.txt"));
			for (int i=0;i<words.size();i++)
			{
				writer.append(words.get(i).pinyin+" "+words.get(i).name+","+words.get(i).freqency);
				if (i!=words.size()-1)
					writer.append('\n');
			}
			writer.flush();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
