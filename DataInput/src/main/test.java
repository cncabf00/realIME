package main;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Data.Parser;

public class test {

	public static void main(String[] args)
	{
		Parser parser=new Parser("root","5kjm1kxgz2");
		parser.readAll("E:/peoples_daliy/2011/output");
//		parser.updateSum();
//		DateFormat format = new SimpleDateFormat("yyyy.MM.dd");  
//		String str="2012.2.26";
//		Date date = null;
//		try {
//			date = format.parse(str);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.print(date);

	}
}
