package edu.njucs.realime.lexicon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.njucs.model.HashTree;
import edu.njucs.model.HashTreeNode;

public class LexiconTree {
	HashTree<LexiconInfo> lexiconTree;
	
	
	public void build(List<String> pinyins)
	{
		HashTreeNode<LexiconInfo> root=new HashTreeNode<LexiconInfo>();
		root.setNodeInfo(new LexiconInfo('\'', ""));
		lexiconTree=new HashTree<LexiconInfo>(root);
		for (char c='a';c<='z';c++)
		{
			insertToTree(""+c);
		}
//		HashTreeNode<LexiconInfo> currentNode=root;
		for (String pinyin:pinyins)
		{
			insertToTree(pinyin);
		}
		
		
	}
	
	void insertToTree(String keyPath)
	{
		HashTreeNode<LexiconInfo> node=lexiconTree.getRoot();
		int i=0;
		int length=keyPath.length();
		for (;i<length;i++)
		{
			HashTreeNode<LexiconInfo> child=node.childWithKey(keyPath.charAt(i));
			if (child==null)
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
			HashTreeNode<LexiconInfo> child=node.childWithKey(keyPath.charAt(i));
			if (child==null)
			{
				HashTreeNode<LexiconInfo> newNode = new HashTreeNode<LexiconInfo>();
				newNode.setKey(keyPath.charAt(i));
				newNode.setNodeInfo(new LexiconInfo(keyPath.charAt(i), node.getNodeInfo().charPath+keyPath.charAt(i)));
				node=node.addChild(newNode);
			}
			else
				node=child;
		}
		node.getNodeInfo().isFinal=true;
	}
	
//	HashTreeNode<LexiconInfo> insertFromNode(HashTreeNode<LexiconInfo> node,String keyPath,int from)
//	{
//		if (keyPath==null || keyPath.equals("") || from>=keyPath.length())
//			return node;
//
//		String charPath = node.getNodeInfo().charPath;
//		boolean samePrefix = true;
//		for (; from < charPath.length(); from++) {
//			if (from >= keyPath.length())
//				break;
//			if (charPath.charAt(from) != keyPath.charAt(from)) {
//				samePrefix = false;
//				break;
//			}
//		}
//		if (from >= keyPath.length()) {
//			return node;
//		}
//		if (samePrefix) {
//			char c = keyPath.charAt(from);
//			HashTreeNode<LexiconInfo> child=node.childWithKey(""+c);
//			if (child==null)
//			{
//				HashTreeNode<LexiconInfo> newNode = new HashTreeNode<LexiconInfo>();
//				newNode.setNodeInfo(new LexiconInfo(c, node.getNodeInfo().charPath
//						+ c));
//				newNode.setKey(""+c);
//				node.addChild(newNode);
//				return insertFromNode(newNode, keyPath, from + 1);
//			}
//			else
//			{
//				return insertFromNode(child, keyPath, from+1);
//			}
//		}
//		else
//		{
//			return insertFromNode(node.getParent(), keyPath, from);
//		}
//	}
	
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
			HashTreeNode<LexiconInfo> child=node.childWithKey(c);
			if (child==null)
			{
				results.add(node.getNodeInfo().charPath);
				node=lexiconTree.getRoot().childWithKey(c);
			}
			else
			{
				node=child;
			}
		}
		results.add(node.getNodeInfo().charPath);
		return results;
	}
	
	public List<String> splitReverse(String input)
	{
		List<String> results=new ArrayList<String>();
		int end=input.length();
		for (int i=input.length()-1;i>=0;i--)
		{
			String sub=input.substring(i,end);
			if (i==0)
			{
				results.add(0, input.substring(i,end));
			}
			else if (!isSingleSplit(sub))
			{
				results.add(0, input.substring(i+1,end));
				if (input.charAt(i)!='\'')
				{
					end=i+1;
				}
				else
				{
					end=i;
				}
			}
			
		}
		return results;
	}
	
	public boolean isSingleSplit(String input)
	{
		HashTreeNode<LexiconInfo> node=lexiconTree.getRoot();
		for (int i=0;i<input.length();i++)
		{
			char c=input.charAt(i);
			if (c=='\'')
			{
				return false;
			}
			HashTreeNode<LexiconInfo> child=node.childWithKey(c);
			if (child==null)
			{
				return false;
			}
			else
			{
				node=child;
			}
		}
		return true;
	}
	
	
	public Collection<List<String>> getAllPossibleSplit(String input)
	{
		Set<List<String>> allSplits=new HashSet<List<String>>();
		allSplits.add(split(input));
		allSplits.add(splitReverse(input));
		Set<List<String>> result=new HashSet<List<String>>();
		for (List<String> split:allSplits)
		{
			List<Integer> ext=new ArrayList<Integer>();
			for (int i=0;i<split.size();i++)
			{
				if (split.get(i).equals("zh") || split.get(i).equals("ch") || split.get(i).equals("sh"))
				{
					ext.add(i);
				}
			}
			int max=1<<ext.size();
			for (int flag=0;flag<max;flag++)
			{
				List<String> list=new ArrayList<String>(split);
				int p=1;
				for (int j=ext.size()-1;j>=0;j--)
				{
					if ((flag&p)!=0)
					{
						int pos=ext.get(j);
						String str=list.remove(pos);
						list.add(pos,""+str.charAt(1));
						list.add(pos,""+str.charAt(0));
					}
					p=p<<1;
				}
				result.add(list);
			}
		}
//		for (int i=1;i<originSplit.size() && fill<=maxFill;i++)
//		{
//			String str=originSplit.get(i);
//			HashTreeNode<LexiconInfo> n=lexiconTree.getRoot();
//			boolean isFinal=false;
//			for (int j=0;j<str.length();j++)
//			{
//				n=n.childWithKey(str.charAt(j));
//				if (n.getNodeInfo().isFinal)
//				{
//					isFinal=true;
//					break;
//				}
//			}
//			if (isFinal)
//				continue;
////			if (str.length()==1 || (str.length()==2 && str.charAt(1)=='h'))
////			{
////				//fill
////				fill++;
////				HashTreeNode<LexiconInfo> node=lexiconTree.getRoot();
////				for (int j=0;j<str.length();j++)
////				{
////					node=node.childWithKey(str.charAt(j));
////				}
////				List<HashTreeNode<LexiconInfo>> leafs=getAllFinal(node);
////				for (int j=0;j<leafs.size();j++)
////				{
////					List<String> list=new ArrayList<String>();
////					list.addAll(originSplit);
////					list.set(i, leafs.get(j).getNodeInfo().charPath);
////					allSplits.add(list);
////				}
////			}
//		}
		
		return result;
	}
	
	public List<HashTreeNode<LexiconInfo>> getAllFinal(HashTreeNode<LexiconInfo> n)
	{
		List<HashTreeNode<LexiconInfo>> leafs=new ArrayList<HashTreeNode<LexiconInfo>>();
		Collection<HashTreeNode<LexiconInfo>> allChildren=n.getAllChildren();
		List<HashTreeNode<LexiconInfo>> array=new ArrayList<HashTreeNode<LexiconInfo>>(allChildren);
		while (array.size()>0)
		{
			HashTreeNode<LexiconInfo> node=array.get(0);
			if (node.getNodeInfo().isFinal)
			{
				leafs.add(node);
			}
			if (node.hasChild())
			{
				array.addAll(node.getAllChildren());
			}
			array.remove(0);
		}
		
		return leafs;
	}
	
}
