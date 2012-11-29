// TestC.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include <iostream>
#include <stdlib.h>
#include <string>

using namespace std;

int parseDictLine(char* line) {
	long code = 0;
	int sep1 = 0;
	for (int i = 0; line[i] != '\0'; i++) {
		if (line[i] == ' ') {
			sep1 = i;
			break;
		}
	}

	bool extra = true;
	for (int i = 0; i < sep1; i++) {
		if (line[i] != '\'') {
			code = code * 27 + (line[i] - 'a' + 1);
		} else {
			code = code * 27 + 27;
		}
	}

	printf("%lu",code);

	int pos = sep1 + 1;
	int last = pos;
	while (line[pos] != '\0') {
		bool after = false;
		int priority = 0;
		while (true) {
			if (line[pos] == ',') {
				char *text = new char[pos - last + 1];
				for (int j = last; j < pos; j++) {
					text[j - last] = line[j];
				}
				text[pos - last + 1] = '\0';
				cout<<text<<endl;;
				after = true;
			} else if (after) {
				if (line[pos] == ';' || line[pos] == '\0') {
					last = pos + 1;
					pos++;
					cout<<priority<<endl;
					break;
				} else {
					if (line[pos]>='0' && line[pos]<='9')
					{
						priority = priority * 10 + (line[pos] - '0');
					}
				}
			}
			pos++;
		}
	}
	return pos;
}


int _tmain(int argc, _TCHAR* argv[])
{
	string a="aaa";
	a=a+'a';
	cout<<"ab"+a;
	system("pause");
	return 0;
}

