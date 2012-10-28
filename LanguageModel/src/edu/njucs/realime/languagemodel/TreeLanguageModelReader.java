package edu.njucs.realime.languagemodel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class TreeLanguageModelReader {
	public static TreeLangageModel readObject(InputStream input)
	{
		TreeLangageModel model = null;
        try
        {
           ObjectInputStream in = new ObjectInputStream(input);
           model = (TreeLangageModel) in.readObject();
           in.close();
       }catch(IOException i)
       {
           i.printStackTrace();
       }catch(ClassNotFoundException c)
       {
           c.printStackTrace();
       }
        return model;
	}
}
