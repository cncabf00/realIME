package edu.njucs.realime.lexicon;

import java.util.ArrayList;
import java.util.List;

import edu.njucs.model.Tree;
import edu.njucs.model.TreeNode;

public class PinyinSplitter {
	Tree<LexiconInfo> lexiconTree;
	
	public PinyinSplitter(Tree<LexiconInfo> tree)
	{
		this.lexiconTree=tree;
	}
	
	public List<String> split(String input)
	{
		List<String> results=new ArrayList<String>();
		TreeNode<LexiconInfo> node=lexiconTree.getRoot();
		for (int i=0;i<input.length();i++)
		{
			char c=input.charAt(i);
			TreeNode<LexiconInfo> child=node.findChildWithKey(new LexiconInfo(c, ""));
			if (child==null)
			{
				results.add(node.getNodeInfo().charPath);
				node=lexiconTree.getRoot().findChildWithKey(new LexiconInfo(c, ""));
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
