#include "stdafx.h"
#include <iostream>
#include <fstream>
#include <stdio.h>
#include "Utils.h"
#include "TreeLanguageModel.h"
#include "Candidate.h"

using namespace std;

int main()
{
	ifstream in("f:\\word_new_all.txt");
	TreeLanguageModel model;

	char buf[256];
	if (in)
	{
		while (in.good())
		{
			in.getline(buf,256);
			string* str=new string(buf);
			if (str->size()!=0)
			{
				new vector<string>();
	/*			Word* word=parseLine(*str);
				model.insertToTree(word);
				delete word;*/
			}
			delete str;
		}
	}
	in.close();
/*
	vector<string> input;
	input.push_back("ming");
	input.push_back("tian");
	input.push_back("tian");
			
	vector<Candidate> candidates=model.getAllCandidates(input);

	for (int i=0;i<candidates.size();i++)
	{
		cout<<candidates[i].text<<endl;
	}
	*/
	system("pause");
	
	return 0;
}