package edu.njucs.realime.lexicon;

import java.util.ArrayList;
import java.util.List;

import edu.njucs.model.HashTree;
import edu.njucs.model.HashTreeNode;

public class PinyinSplitter {
	HashTree<LexiconInfo> lexiconTree;
	
	public PinyinSplitter(HashTree<LexiconInfo> tree)
	{
		this.lexiconTree=tree;
	}
	
	public List<String> split(String input)
	{
		List<String> results=new ArrayList<String>();
		HashTreeNode<LexiconInfo> node=lexiconTree.getRoot();
		for (int i=0;i<input.length();i++)
		{
			char c=input.charAt(i);
			if (c=='\'')
			{
				results.add(node.getNodeInfo().charPath);
				node=lexiconTree.getRoot();
				continue;
			}
			HashTreeNode<LexiconInfo> child=node.childWithKey(""+c);
			if (child==null)
			{
				results.add(node.getNodeInfo().charPath);
				node=lexiconTree.getRoot().childWithKey(""+c);
			}
			else
			{
				node=child;
			}
		}
		results.add(node.getNodeInfo().charPath);
		return results;
	}
}
