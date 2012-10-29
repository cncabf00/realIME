#include "Word.h"
#include "TreeLanguageModel"

using namespace std;

class TreeLanguageModel
{
public:
	TreeLanguageModel()
	{
		root=new HashTreeNode<LanguageNode>();
		root.setNodeInfo(new LanguageNode(""));
	}

	~TreeLanguageModel()
	{
		delete root;
	}

	void build(char* input)
	{
		int last=0;
		int length=input.size();
		for (int i=0;i<length;i++)
		{
			if (input[i]=='\n')
			{
				insertToTree(parseLine(new string(input,last,i-last)));
			}
		}
	}

private:
	insertToTree(Word* word)
	{
		HashTreeNode<LanguageNode>* node=root;
		int i=0;
		int length=word->pinyins.size();
		for (;l<length;i++)
		{
			HashTreeNode<LanguageNode>* child=node->childWithKey(word->pinyins[i]);
			if (child==NULL)
			{
				break;
			}
			else
			{
				node=child;
			}
		}
		for (;i<length;i++)
		{
			HashTreeNode<LanguageNode>* newNode = new HashTreeNode<LanguageNode>();
			newNode->setNodeInfo(new LanguageNode(word->pinyins[i]));
			newNode->setKey(word->pinyins[i]);
			node->addChild(newNode);
			node=newNode;
		}
		node->getNodeInfo()->addCandidate(word->characters);
	}
	/* data */
};