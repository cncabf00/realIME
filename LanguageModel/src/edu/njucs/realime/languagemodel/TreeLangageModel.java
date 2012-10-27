package edu.njucs.realime.languagemodel;

import java.util.ArrayList;
import java.util.List;

import edu.njucs.model.HashTree;
import edu.njucs.model.HashTreeNode;

public class TreeLangageModel implements StaticLanguageModel {

	HashTree<LanguageNode> tree;
	
	@Override
	public void build(List<Word> dict)
	{
		HashTreeNode<LanguageNode> root=new HashTreeNode<LanguageNode>();
		root.setNodeInfo(new LanguageNode("", null));
		HashTreeNode<LanguageNode> currentNode=root;
		for (Word word:dict)
		{
			currentNode=insertFromNode(currentNode, word,0);
		}
		
		tree=new HashTree<LanguageNode>(root);
	}
	
	HashTreeNode<LanguageNode> insertFromNode(HashTreeNode<LanguageNode> node,Word word,int from)
	{
		List<String> keyPath = node.getNodeInfo().keyPath;
		if (from>=word.characters.size())
		{
			if (from==keyPath.size())
			{
				String str="";
				for (int i=0;i<word.characters.size();i++)
				{
					str+=word.characters.get(i).text;
				}
				node.getNodeInfo().candidates.add(str);
				return node;
			}
			else
			{
				return insertFromNode(node.getParent(), word, from);
			}
		}
		boolean samePrefix = true;
		for (; from < keyPath.size(); from++) {
			if (from >= word.characters.size())
				break;
			if (!word.characters.get(from).key.equals(keyPath.get(from))) {
				samePrefix = false;
				break;
			}
		}
		if (from == word.characters.size() && from == keyPath.size()) {
			String str="";
			for (int i=0;i<word.characters.size();i++)
			{
				str+=word.characters.get(i).text;
			}
			node.getNodeInfo().candidates.add(str);
			return node;
		}
		else if (from >= word.characters.size()) {
			return insertFromNode(node.getParent(), word, from);
		}
		
		if (samePrefix) {
			String str = word.characters.get(from).key;
			HashTreeNode<LanguageNode> child=node.childWithKey(str);
			if (child==null)
			{
				HashTreeNode<LanguageNode> newNode = new HashTreeNode<LanguageNode>();
				newNode.setNodeInfo(new LanguageNode(str,keyPath));
				newNode.setKey(str);
				node.addChild(newNode);
				return insertFromNode(newNode, word, from + 1);
			}
			else
			{
				return insertFromNode(child, word, from+1);
			}
		}
		else
		{
			return insertFromNode(node.getParent(), word, from);
		}
	}

	@Override
	public PinyinResult parse(List<String> input) {
		PinyinResult result=new PinyinResult();
		HashTreeNode<LanguageNode> node=tree.getRoot();
		int i=0;
		for (;i<input.size();i++)
		{
			HashTreeNode<LanguageNode> child=node.childWithKey(input.get(i));
			if (child==null)
			{
				break;
			}
			else
			{
				node=child;
			}
		}
		result.wordCandidates.addAll(node.getNodeInfo().candidates);
		if (i<=input.size())
			result.restInput=input.subList(i, input.size());
		
		return result;
	}

	@Override
	public void append(List<Word> dict) {
		HashTreeNode<LanguageNode> currentNode=tree.getRoot();
		for (Word word:dict)
		{
			currentNode=insertFromNode(currentNode, word,0);
		}
	}

	@Override
	public List<Candidate> getAllCandidates(List<String> input) {
		List<Candidate> candidates=new ArrayList<Candidate>();
		for (int i=input.size();i>=0;i--)
		{
			PinyinResult result=this.parse(input.subList(0, i));
			for (int j=0;j<result.wordCandidates.size();j++)
			{
				List<String> restInput=new ArrayList<String>();
				restInput.addAll(result.restInput);
				restInput.addAll(input.subList(i, input.size()));
				Candidate candidate=new Candidate(result.wordCandidates.get(j), restInput);
				if (!candidates.contains(candidate))
					candidates.add(candidate);
			}
		}
		return candidates;
	}

}
