#pragma once
#include <map>

using namespace std;

template <class T>
class HashTreeNode
{
public:

	/* data */
	map<string,HashTreeNode<T>*>* children;
	T* nodeInfo;
	string* key;


	HashTreeNode()
	{
		children=NULL;
		nodeInfo=NULL;
	}

	~HashTreeNode()
	{
		delete children;
		delete nodeInfo;
	}

	void addChild(HashTreeNode<T>* node)
	{
		if (children==NULL)
		{
			children=new map<string,HashTreeNode<T>*>();
		}
		(*children)[*node->key]=node;
	}

	int childCount()
	{
		if (children==NULL)
		return 0;
	else
		return children->size(0;)
	}

	HashTreeNode<T>* childWithKey(string* key)
	{
		if (children==NULL)
			return NULL;
		else
			return (*children)[*key];
	}

	T* getNodeInfo()
	{
		return nodeInfo;
	}

	void setNodeInfo(T* info)
	{
		nodeInfo=info;
	}

	bool hasChild()
	{
		return children!=NULL;
	}

	void setKey(string* key)
	{
		this->key=key;
	}
	
};

