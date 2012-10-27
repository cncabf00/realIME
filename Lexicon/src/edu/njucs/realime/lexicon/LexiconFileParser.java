package edu.njucs.realime.lexicon;

import java.util.List;

import edu.njucs.model.HashTree;
import edu.njucs.model.HashTreeNode;

public class LexiconFileParser {
	
	public HashTree<LexiconInfo> parse(List<String> pinyins)
	{
		HashTreeNode<LexiconInfo> root=new HashTreeNode<LexiconInfo>();
		root.setNodeInfo(new LexiconInfo('\'', ""));
		HashTreeNode<LexiconInfo> currentNode=root;
		for (String pinyin:pinyins)
		{
			currentNode=insertFromNode(currentNode, pinyin,0);
		}
		
		HashTree<LexiconInfo> tree=new HashTree<LexiconInfo>(root);
		return tree;
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
	
	
}
