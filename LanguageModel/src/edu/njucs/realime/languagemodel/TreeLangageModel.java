package edu.njucs.realime.languagemodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.njucs.model.HashTree;
import edu.njucs.model.HashTreeNode;
//import android.util.Log;

public class TreeLangageModel implements StaticLanguageModel, java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8860822566604451844L;
	HashTree<LanguageNode> tree;
	
	public void build(InputStream input)
	{
		HashTreeNode<LanguageNode> root=new HashTreeNode<LanguageNode>();
		root.setNodeInfo(new LanguageNode(""));
		tree=new HashTree<LanguageNode>(root);
		
		append(input);
		
		
	}
	
	public void build(Word[] dict)
	{
		HashTreeNode<LanguageNode> root=new HashTreeNode<LanguageNode>();
		root.setNodeInfo(new LanguageNode(""));
		tree=new HashTree<LanguageNode>(root);
		
		append(dict);
	}
	
	void insertToTree(Word word)
	{
		HashTreeNode<LanguageNode> node=tree.getRoot();
		int i=0;
		int length=word.pinyins.length;
		for (;i<length;i++)
		{
			HashTreeNode<LanguageNode> child=node.childWithKey(word.pinyins[i]);
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
			HashTreeNode<LanguageNode> newNode = new HashTreeNode<LanguageNode>();
			newNode.setNodeInfo(new LanguageNode(word.pinyins[i]));
			newNode.setKey(word.pinyins[i]);
			node.addChild(newNode);
			node=newNode;
		}
		node.getNodeInfo().addCandidate(word.characters);
	}
	
//	HashTreeNode<LanguageNode> insertFromNode(HashTreeNode<LanguageNode> node,Word word,int from)
//	{
//		if (word.characters.length()!=word.pinyins.length)
//			return node;
//		List<String> keyPath = getKeyPath(node);
//		if (from>=word.characters.length())
//		{
//			if (from==keyPath.size())
//			{
//				node.getNodeInfo().addCandidate(word.characters);
//				return node;
//			}
//			else
//			{
//				return insertFromNode(node.getParent(), word, from);
//			}
//		}
//		boolean samePrefix = true;
//		for (; from < keyPath.size(); from++) {
//			if (from >= word.characters.length())
//				break;
//			if (!word.pinyins[from].equals(keyPath.get(from))) {
//				samePrefix = false;
//				break;
//			}
//		}
//		if (from == word.characters.length() && from == keyPath.size()) {
//			node.getNodeInfo().addCandidate(word.characters);
//			return node;
//		}
//		else if (from >= word.characters.length()) {
//			return insertFromNode(node.getParent(), word, from);
//		}
//		
//		if (samePrefix) {
//			String str = word.pinyins[from];
//			HashTreeNode<LanguageNode> child=node.childWithKey(str);
//			if (child==null)
//			{
//				HashTreeNode<LanguageNode> newNode = new HashTreeNode<LanguageNode>();
//				newNode.setNodeInfo(new LanguageNode(str));
//				newNode.setKey(str);
//				node.addChild(newNode);
//				return insertFromNode(newNode, word, from + 1);
//			}
//			else
//			{
//				return insertFromNode(child, word, from+1);
//			}
//		}
//		else
//		{
//			return insertFromNode(node.getParent(), word, from);
//		}
//	}

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
		if (node.getNodeInfo().candidates!=null)
			result.wordCandidates.addAll(node.getNodeInfo().candidates);
		if (i<=input.size())
			result.restInput=input.subList(i, input.size());
		
		return result;
	}
	
	
	
	public void append(InputStream input) {
		
		try
		{
//			BufferedReader reader=new BufferedReader(new InputStreamReader(input,"utf-8"));
//			String line=reader.readLine();
//			while (line!=null)
//			{
//				insertToTree(DictFileParser.parseLine(line));
//				line=reader.readLine();
//			}
			byte[] readBytes = new byte[input.available()];
			input.read(readBytes);
			String string4file = new String(readBytes,"utf-8");
//			String[] lines=string4file.split("\n");
			int last=0;
			int length=string4file.length();
			for (int i=0;i<length;i++)
			{
				if (string4file.charAt(i)=='\n')
				{
					insertToTree(DictFileParser.parseLine(string4file.substring(last, i)));
					last=i+1;
				}
			}
//			if (last!=string4file.length())
//				insertToTree(DictFileParser.parseLine(string4file.substring(last, string4file.length())));
			
//			for (int k=0;k<lines.length;k++)
//			{
//				insertToTree(DictFileParser.parseLine(lines[k]));
//			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void append(Word[] dict) {
//		HashTreeNode<LanguageNode> currentNode=tree.getRoot();
		for (int i=0,n=dict.length;i<n;i++)
		{
//			currentNode=insertFromNode(currentNode, word,0);
			insertToTree(dict[i]);
		}
	}

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
	
	public List<String> getKeyPath(HashTreeNode<LanguageNode> node)
	{
		List<String> keyPath=new ArrayList<String>();
		while (node.getParent()!=null)
		{
			keyPath.add(0, node.getNodeInfo().key);
			node=node.getParent();
		}
		return keyPath;
	}

}
