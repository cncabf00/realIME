package edu.njucs.realime.lexicon;

import java.util.ArrayList;
import java.util.List;

import edu.njucs.model.HashTree;
import edu.njucs.model.HashTreeNode;

public class LexiconTree {
	HashTree<LexiconInfo> lexiconTree;
	
	public void build(List<String> pinyins)
	{
		HashTreeNode<LexiconInfo> root=new HashTreeNode<LexiconInfo>();
		root.setNodeInfo(new LexiconInfo('\'', ""));
		HashTreeNode<LexiconInfo> currentNode=root;
		for (String pinyin:pinyins)
		{
			currentNode=insertFromNode(currentNode, pinyin,0);
		}
		
		lexiconTree=new HashTree<LexiconInfo>(root);
	}
	
	HashTreeNode<LexiconInfo> insertFromNode(HashTreeNode<LexiconInfo> node,String keyPath,int from)
	{
		if (keyPath==null || keyPath.equals("") || from>=keyPath.length())
			return node;

		String charPath = node.getNodeInfo().charPath;
		boolean samePrefix = true;
		for (; from < charPath.length(); from++) {
			if (from >= keyPath.length())
				break;
			if (charPath.charAt(from) != keyPath.charAt(from)) {
				samePrefix = false;
				break;
			}
		}
		if (from >= keyPath.length()) {
			return node;
		}
		if (samePrefix) {
			char c = keyPath.charAt(from);
			HashTreeNode<LexiconInfo> child=node.childWithKey(""+c);
			if (child==null)
			{
				HashTreeNode<LexiconInfo> newNode = new HashTreeNode<LexiconInfo>();
				newNode.setNodeInfo(new LexiconInfo(c, node.getNodeInfo().charPath
						+ c));
				newNode.setKey(""+c);
				node.addChild(newNode);
				return insertFromNode(newNode, keyPath, from + 1);
			}
			else
			{
				return insertFromNode(child, keyPath, from+1);
			}
		}
		else
		{
			return insertFromNode(node.getParent(), keyPath, from);
		}
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
