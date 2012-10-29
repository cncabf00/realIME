#include "stdafx.h"
#include "Utils.h"

Word* parseLine(string& line)
{
	Word* word=new Word();
	word->pinyins=new vector<string*>;
		int sep1=0;
		int sep2=0;
		int cur=0;
		int num=0;
		for (int i=0,n=line.size();i<n;i++)
		{
			if (line[i]==' ')
			{
				if (cur==0)
					sep1=i;
				else
				{
					sep2=i;
					break;
				}
				cur++;
			}
			else if (cur==0 && line[i]=='\'')
			{
				num++;
			}
		}
		word->characters=new string(line,sep1+1, sep2-sep1-1);
		cur=0;
		int count=0;
		for (int i=0;i<sep1;i++)
		{
			if (line[i]=='\'')
			{
				word->pinyins->push_back(new string(line,cur, i-cur));
				cur=i+1;
			}
		}
		if (cur!=sep1)
			word->pinyins->push_back(new string(line,cur, sep1-cur));
		return word;
}