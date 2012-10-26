package edu.njucs.realime.lexicon;

import java.util.List;

import edu.njucs.model.Tree;
import edu.njucs.model.TreeNode;

public class LexiconFileParser {
	
	public Tree<LexiconInfo> parse(List<String> pinyins)
	{
		TreeNode<LexiconInfo> root=new TreeNode<LexiconInfo>();
		root.setNodeInfo(new LexiconInfo('\'', ""));
		TreeNode<LexiconInfo> currentNode=root;
		for (String pinyin:pinyins)
		{
			currentNode=insertFromNode(currentNode, pinyin,0);
		}
		
		Tree<LexiconInfo> tree=new Tree<LexiconInfo>(root);
		return tree;
	}
	
	TreeNode<LexiconInfo> insertFromNode(TreeNode<LexiconInfo> node,String keyPath,int from)
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
			TreeNode<LexiconInfo> child=node.findChildWithKey(new LexiconInfo(c, ""));
			if (child==null)
			{
				TreeNode<LexiconInfo> newNode = new TreeNode<LexiconInfo>();
				newNode.setNodeInfo(new LexiconInfo(c, node.getNodeInfo().charPath
						+ c));
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
