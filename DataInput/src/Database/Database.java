package Database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import Data.Article;
import Data.Word;

public class Database {

	Connection conn;
    String myDB = "word";
    
    public Database(String username,String password)
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnection(username,password);
        }
        catch (Exception e) {
            System.out.println("Error:  " + e.toString());
        }
    }
    
    public void getConnection(String username,String password) throws SQLException
    {
        conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", username);
        connectionProps.put("password", password);

        conn = DriverManager.getConnection("jdbc:" + "mysql" + "://" + "localhost" + ":" + "3306" + "/", connectionProps);
//        System.out.println("connected to database '" + myDB + "'");
    }
    
    public void createDatabase()
    {
    	String query0="CREATE DATABASE "+myDB +" DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci";
    	String query1="create table "+myDB+".article ( date date, edition char(20), type char(50), shoulder char(100), title char(100), subTitle char(100), author char(50), primary key (title,date))";
    	String query2="create table "+myDB+".words ( name char(50) , article char(100) , frequency int ,date date, primary key (name,article,date),foreign key (article,date) references "+myDB+".article (title,date))";
        Statement stmt = null;
        try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
        try {
			stmt.execute(query0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt.execute(query1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			stmt.execute(query2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    public void insertArticle(Article article)
    {
    	String query = "INSERT INTO " + myDB + ".`article` (`date`, `edition`, `type`, `shoulder`, `title` , `subTitle`, `author`) VALUES ('"
                        + article.getDate() + "','" + article.getEdition() + "','" + article.getType() + "','" + article.getShoulder() + "','" + article.getTitle() + "','" +article.getSubTitle()+ "','" + article.getAuthor() + "')";
    	Statement stmt = null;
    	try {
			stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println(article.getTitle()+"   "+article.getDate());
		}
    }
    
    public void insertWord(Word word)
    {
    	
    	String query = "INSERT INTO " + myDB + ".`words` (`name`, `article`, `frequency`,`date`) VALUES ('"
	                + word.getName() + "','" + word.getArticle().getTitle() + "','" + word.getFrequency()  + "','"+word.getDate()+"')";
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.execute(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println(word.getName()+"   "+word.getArticle().getTitle()+"   "+word.getDate());
		}
    }
    
    public void updateSum()
    {
    	String query = "select sum(frequency) from "+myDB+".words where name<>'#total'";
    	Statement stmt=null;
    	try {
			stmt = conn.createStatement();
			ResultSet rs=stmt.executeQuery(query);
			if (rs.next())
			{
				int sum=rs.getInt(1);
				query = "select * from " + myDB + ".words where name='#total'";
				rs=stmt.executeQuery(query);
				if (rs.next())
				{
					query="update "+ myDB+".words set frequency="+sum+"where name='#total'";
					stmt.execute(query);
				}
				else
				{
					query="insert into "+myDB+".article (title) values ('#total')";
					stmt.execute(query);
					query="insert into "+myDB+".words (name,article,frequency) values ('#total','#total','"+sum+"')";
					stmt.execute(query);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
//    public static void main(String[] args)
//    {
//    	Database db=new Database();
//    	db.createTable();
//    }
    
    
}
