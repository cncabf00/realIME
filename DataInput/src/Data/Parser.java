package Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Parser {
	Set<String> banList=new HashSet<String>();
	Map<String,Integer> stat=new HashMap<>();
	Map<String,Word> characters=new HashMap<>();
	long count=0;
	
	public Parser(String username,String password)
	{
//		db=new Database(username,password);
//		db.createDatabase();
//		try {
//			readBanList("chinese-stopwords.txt");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		List<Word> words;
		try {
			words = readRawDict("characters.txt");
			for (Word word:words)
			{
				if (characters.containsKey(word.name))
				{
					characters.get(word.name).pinyins.addAll(word.pinyins);
				}
				else
				{
					characters.put(word.name, word);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	void readBanList(String filename) throws IOException
	{
		File file=new File(filename);
		BufferedReader br=new BufferedReader(new FileReader(file));
		banList=new HashSet<String>();
		String line=br.readLine();
		while (line!=null)
		{
			banList.add(line.trim());
			line=br.readLine();
		}
		br.close();
	}
	
	public void updateSum()
	{
//		db.updateSum();
		count++;
	}
	
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
					if (line.charAt(0)=='\'')
						line=line.substring(1);
					String[] strs = line.split(" ");
					Word word = new Word();
					word.pinyins.add(strs[0]);
					word.name = strs[1];
					word.frequency=1;
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
	
	public void readAll(String dir)
	{
		List<File> allFiles=new ArrayList<File>();
		File directory=new File(dir);
		File[] files=directory.listFiles();
		for (int i=0;i<files.length;i++)
		{
			allFiles.add(files[i]);
		}
		int k=0;
		while (k<allFiles.size())
		{
			File f=allFiles.get(k);
			if (f.isDirectory())
			{
				File[] tempFiles=f.listFiles();
				for (int i=0;i<tempFiles.length;i++)
				{
					allFiles.add(tempFiles[i]);
				}
			}
			k++;
		}
		boolean next=true;
		int size=allFiles.size();
		int gap=size/100;
		int nextGap=gap;
		for (int i=0;i<size;i++)
		{
			if (i>=nextGap)
			{
				System.out.println(i*100/size+"%");
				nextGap+=gap;
			}
			File newFile=allFiles.get(i);
			if (newFile.isDirectory())
				continue;
			String temp=newFile.getName();
			String[] t=temp.split("\\.");
			if (t.length<=2)
				continue;
			String suffix=t[t.length-2];
			if (!suffix.equals("new"))
				continue;
			String originFilename=t[0];
			for (int j=1;j<(t.length-2);j++)
			{
				originFilename+="."+t[j];
			}
			
//			File originFile=new File(newFile.getParentFile().getAbsolutePath()+File.separator+originFilename);
			try {
				read(newFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		printResult();
		printCharacters();
	}
	
	public void read(File newFile) throws IOException
	{
		//origin file
//		BufferedReader br=new BufferedReader(new FileReader(originFile));
//		String line=null;
//		line=br.readLine();
//		boolean start=false;
//		Article article=new Article();
//		while (line!=null)
//		{
//			if (findFirst(line)=='<')
//			{
//				if (!start)
//				{
//					start=true;
//				}
//				String[] s=line.split(">=");
//				if (s.length<2)
//				{
//					line=br.readLine();
//					continue;
//				}
//				if (s[0].contains("日期"))
//				{
//					DateFormat format = new SimpleDateFormat("yyyy.MM.dd");  
//					String dateStr=s[1];
//					Date date = null;
//					try {
//						date = format.parse(dateStr);
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					java.sql.Date date1=new java.sql.Date(date.getTime());
//					article.date=date1;
//				}
//				else if (s[0].contains("版次"))
//				{
//					article.edition=s[1];
//				}
//				else if (s[0].contains("版名"))
//				{
//					article.type=s[1];
//				}
//				else if (s[0].contains("肩标题"))
//				{
//					article.shoulder=s[1];
//				}
//				else if (s[0].contains("副标题"))
//				{
//					article.subTitle=s[1];
//				}
//				else if (s[0].contains("标题"))
//				{
//					article.title=s[1];
//				}		
//				else if (s[0].contains("作者"))
//				{
//					article.author=s[1];
//				}
//			}
//			else
//			{
//				if (start)
//					break;
//			}
//			line=br.readLine();
//		}
//		db.insertArticle(article);
//		insert(article);
		
		//new file
		BufferedReader br=new BufferedReader(new FileReader(newFile));
		Map<String,Integer> wordSet=new HashMap<String,Integer>();
		String line=br.readLine();
		int state=0;
		boolean text=false;
		while (line!=null)
		{
			if (findFirst(line)=='<')
			{
				if (state==0)
					state=1;
				line=br.readLine();
				continue;
			}
			else if (state==1)
			{
				line=br.readLine();
				state=2;
			}
			else if (state==2)
			{
				text=true;
			}
			if (text==true)
			{
				String[] words=line.split(" ");
				for (int i=0;i<words.length;i++)
				{
					String[] w=words[i].split("/");
					if (w.length<2 || isBanned(w[0]) || w[1].equals("w") || w[0].length()==1)
						continue;
					for (int j=0;j<w[0].length();j++)
					{
						String key=""+w[0].charAt(j);
						if (characters.containsKey(key))
						{
							characters.get(key).frequency++;
						}
					}
					if (!wordSet.containsKey(w[0]))
						wordSet.put(w[0],1);
					else
					{
						int t=wordSet.get(w[0]);
						t++;
						wordSet.put(w[0], t);
					}
				}
			}
			line=br.readLine();
		}
		
		for (Map.Entry<String, Integer> e:wordSet.entrySet())
		{
			String name=e.getKey();
			int frequency=e.getValue();
			Word word=new Word();
			word.name=name;
			word.frequency=frequency;
//			word.article=article;
//			word.date=article.date;
//			db.insertWord(word);
			insertWord(word);
		}
	}
	
	void insertWord(Word word)
	{
		if (stat.containsKey(word.name))
		{
			int freq=stat.get(word.name);
			stat.put(word.name, freq+word.frequency);
		}
		else
		{
			stat.put(word.name, word.frequency);
		}
	}
	
	public void printResult()
	{
		File file=new File("output.txt");
		if (file.exists())
		{
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			FileWriter writer=new FileWriter(file);
			List<Map.Entry<String, Integer>> list=new ArrayList<>();
			list.addAll(stat.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

				@Override
				public int compare(Entry<String, Integer> o1,
						Entry<String, Integer> o2) {
					return -(o1.getValue()-o2.getValue());
				}
				
			});
			for (Map.Entry<String, Integer> e:list)
			{
				if (e.getValue()>=5)
				{
					writer.append(e.getKey()+" "+(int)(Math.log(e.getValue()+1))+"\n");
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printCharacters()
	{
		File file=new File("output_characters.txt");
		if (file.exists())
		{
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			FileWriter writer=new FileWriter(file);
			List<Word> list=new ArrayList<>();
			for (Map.Entry<String, Word> e:characters.entrySet()) 
			{
				for (int i=0;i<e.getValue().pinyins.size();i++)
				{
					Word word=new Word();
					word.name=e.getKey();
					word.frequency=e.getValue().frequency;
					word.pinyins.add(e.getValue().pinyins.get(i));
					list.add(word);
				}
				
			}
			Collections.sort(list, new Comparator<Word>() {

				@Override
				public int compare(Word o1,
						Word o2) {
					return o1.pinyins.get(0).compareTo(o2.pinyins.get(0));
				}
				
			});
			for (Word e:list)
			{
				writer.append(e.pinyins.get(0)+" "+e.name+","+(int)(Math.log(e.frequency)+1)+"\n");
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public char findFirst(String str)
	{
		for (int i=0;i<str.length();i++)
		{
			if (str.charAt(i)==' ')
				continue;
			else
				return str.charAt(i);
		}
		return '\n';
	}
	
	public boolean isBanned(String name)
	{
		if (banList!=null && banList.contains(name))
		{
//			System.out.println(name+"is banned");
			return true;
		}
		else if (name.matches(".*[0-9A-Za-z*+-/?&^()!@$０-９ａ-ｚＡ-Ｚ].*"))
		{
			return true;
		}
		else
			return false;
	}
	
}
