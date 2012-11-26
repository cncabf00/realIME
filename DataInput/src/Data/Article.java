package Data;

public class Article {
	java.sql.Date date;
	String edition="";
	String type="";
	String shoulder="";
	String title="";
	String subTitle="";
	String author="";
	
	public java.sql.Date getDate() {
		return date;
	}
	public String getEdition() {
		return edition;
	}
	public String getType() {
		return type;
	}
	public String getShoulder() {
		return shoulder;
	}
	public String getTitle() {
		return title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public String getAuthor(){
		return author;
	}
	
	
}
