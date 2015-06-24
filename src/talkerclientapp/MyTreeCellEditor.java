package talkerclientapp;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;

import common.*;

public class MyTreeCellEditor extends DefaultTreeCellEditor
{

	public MyTreeCellEditor(JTree tree, DefaultTreeCellRenderer treeRenderee)
	{
		super(tree, treeRenderee);
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row)
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		if (node.getUserObject() instanceof Contact)
		{
			System.out.println("___contact - value: " + value);
			value = ((Contact)node.getUserObject()).displayedName;
		}
		
		if (node.getUserObject() instanceof ContactGroup)
		{
			System.out.println("___grupa");
			value = ((ContactGroup)node.getUserObject()).name;
		}
		
		return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
	}

}
