package edu.njucs.realime.lexicon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LexiconFileParser {
	public List<String> parse(InputStream input)
	{
		BufferedReader reader=new BufferedReader(new InputStreamReader(input));
		List<String> list=new ArrayList<String>();
		try {
			String line;
			line = reader.readLine();
			while (line!=null)
			{
				list.add(line);
				line=reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return list;
	}
}
