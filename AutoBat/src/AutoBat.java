import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AutoBat {
	public static void main(String[] args)
	{
		if (args!=null)
		{
			List<File> allFiles=new ArrayList<File>();
//			String dir=args[0];
			String dir="E:/peoples_daliy/2010";
//			String cmd=args[1];
			String cmd="cmd /c seg/settag.exe";
			File output=new File(dir+File.separator+"output");
			if (!output.exists())
				output.mkdirs();
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
			int count=0;
			for (int i=0;i<allFiles.size();i++)
			{
				if (allFiles.get(i).isDirectory())
					continue;
				String filename=allFiles.get(i).getAbsolutePath();
				String newFilename=dir+"/output/"+ count++ +".new.txt";
				String str=cmd+" "+filename+" >"+newFilename;
				try {
					Process process = Runtime.getRuntime().exec(str);
					process.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
