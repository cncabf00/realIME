package Data;

import java.util.ArrayList;
import java.util.List;

public class Word {
		String name="";
		Article article;
		List<String> pinyins=new ArrayList<>();
		int frequency=0;
		java.sql.Date date;
		public String getName() {
			return name;
		}
		public Article getArticle() {
			return article;
		}
		public int getFrequency() {
			return frequency;
		}
		public java.sql.Date getDate()
		{
			return date;
		}
		
		
		
}
