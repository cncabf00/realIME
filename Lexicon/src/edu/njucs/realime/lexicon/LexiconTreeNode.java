package edu.njucs.realime.lexicon;

import edu.njucs.model.TreeNode;

public class LexiconTreeNode extends TreeNode<LexiconInfo>{

	
	TreeNode<LexiconInfo> findChildWithKey(TreeNode<LexiconInfo> node,char key)
	{
		for (int i=0;i<this.childCount();i++)
		{
			if (node.childAt(i).getNodeInfo().character==key)
				return node.childAt(i);
		}
		return null;
	}

}
