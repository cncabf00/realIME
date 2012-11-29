import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AutoBat {
	static Integer count=0;
	public static void main(String[] args)
	{
		if (args!=null)
		{
			List<File> allFiles=new ArrayList<File>();
//			String dir=args[0];
			final String dir="E:/peoples_daliy/target";
//			String cmd=args[1];
			final String cmd="cmd /c seg\\segtag.exe";
			File output=new File(dir.substring(0,dir.lastIndexOf('/'))+File.separator+"output");
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
		
			
			class Task implements Runnable
			{
				List<File> files;
				public Task(List<File> files)
				{
					this.files=files;
				}
				
				public void run()
				{
					for (int i=0;i<files.size();i++)
					{
						if (files.get(i).isDirectory())
							continue;
						String filename=files.get(i).getAbsolutePath();
						String newFilename;
						synchronized (count) {
							newFilename=dir.substring(0,dir.lastIndexOf('/'))+"/output/"+ count++ +".new.txt";
						}
						String str=cmd+" "+filename+" >"+newFilename;
						try {
							Process process = Runtime.getRuntime().exec(str);
							process.waitFor();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			int threadCount=20;
			int size=allFiles.size();
			int step=size/threadCount+1;
			int cur=0;
			int next=step;
			for (int i=0;i<threadCount;i++)
			{
				if (next>size)
					next=size;
				new Thread(new Task(allFiles.subList(cur, next))).start();
				cur=next;
				next+=step;
			}
		}
	}
	
	
}
