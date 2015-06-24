package talkerclientapp;

import common.ContactGroup;
import common.Contact;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author amster
 */
public class MainWindowFrame extends javax.swing.JFrame
{

	static DefaultTreeModel treeModel;
	MessageFrame messageFrame;
	Object popupObject;

	MouseListener contactsListMouseListener = new MouseAdapter()
	{
		private void showPopup(MouseEvent e)
		{
			// System.out.println( "w showpopup" );
			// if( e.isPopupTrigger() )
			// {
			popupObject = ((DefaultMutableTreeNode) contactsListTree.getClosestPathForLocation(e.getX(), e.getY()).getLastPathComponent()).getUserObject();
			contactsListTree.setSelectionPath(contactsListTree.getClosestPathForLocation(e.getX(), e.getY()));

			if (popupObject instanceof Contact)
			{
				contactPopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}

			if (popupObject instanceof ContactGroup)
			{
				groupPopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}

			// System.out.println( "X=" + e.getX() + " Y=" + e.getY() );
			// System.out.println( "userObject= " + popupObject + " Contact? " +
			// (popupObject instanceof Contact) );
			// System.out.println( "popupObject klasa: " +
			// popupObject.getClass().getName() );
			// }
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			super.mousePressed(e);
			// System.out.println( "wcisnieto " + e.getButton() );
			// System.out.println( "e.isPopupTrigger() " + e.isPopupTrigger() );

			if (SwingUtilities.isRightMouseButton(e))
			{
				showPopup(e);
			}

			Contact userObject = null;
			MessageFrame messageFrameForReceiver = null;
			int selRow = contactsListTree.getRowForLocation(e.getX(), e.getY());
			TreePath selPath = contactsListTree.getPathForLocation(e.getX(), e.getY());
			if (selRow != -1)
			{
				if (e.getClickCount() == 1)
				{
				} else if (e.getClickCount() == 2)
				{
					try
					{
						if (((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject() instanceof Contact)
						{
							userObject = (Contact) ((DefaultMutableTreeNode) selPath.getLastPathComponent()).getUserObject();
							if ((messageFrameForReceiver = getMessageFrameForReceiver(userObject.talkerNumber)) == null)
							{

								messageFrame = new MessageFrame();
								messageFrame.contact = userObject;
								messageFrame.setTitle(userObject.displayedName);
								messageFrame.toBack();
								messageFrame.setVisible(true);

								Main.messageFramesMap.put(messageFrame.contact.talkerNumber, messageFrame);

							} else
							{
								// messageFramesForReceiver.setExtendedState(JFrame.ICONIFIED);
								System.out.println("pokaz okno");
								// messageFramesForReceiver.toFront();
								// messageFramesForReceiver.transferFocusBackward();

								// TODO pokaz okno jesli jest zminimalizowane
							}
						}
						// messageFrame.setVisible(true);
					} catch (Exception ex)
					{
						ex.printStackTrace();
					}

				}
			}
		}

	};

	WindowAdapter mainWindowFrameWindowAdapter = new java.awt.event.WindowAdapter()
	{
		@Override
		public void windowClosing(WindowEvent winEv)
		{

			Main.client.exit();

			System.out.println("zamknieto glowne okno");

			try
			{

				File personalData = new File(Main.DIR_TO_SERIALIZED_DATA + "/personal_data.dat");
				personalData.createNewFile();

				ObjectOutput out = new ObjectOutputStream(new FileOutputStream(personalData));
				out.writeObject(Main.myPersonalData);
				out.close();

				File model = new File(Main.DIR_TO_SERIALIZED_DATA + "/model.dat");
				model.createNewFile();

				out = new ObjectOutputStream(new FileOutputStream(model));
				out.writeObject(treeModel);
				out.close();

				System.out.printf("Serialized data is saved in " + Main.DIR.getAbsolutePath());

			} catch (Exception i)
			{
				i.printStackTrace();
			}

			// TODO pozamykac wszystkie polaczenia i zserializowac co trzeba na
			// dysku
		}
	};

	/**
	 * Creates new form MainWindow
	 */
	public MainWindowFrame()
	{

		initComponents();

		// getContentPane().setBackground(Color.BLACK);]
		// Image img = new ImageIcon("images/logo_266_224.png").getImage();
		// System.out.println("sciezka do obrazow z jara: _____________ " +
		// Main.class.getResource("/resources/images/logo_266_224.png"));
		talkerLogoLabel.setIcon(new ImageIcon(Main.class.getResource("/resources/images/logo_266_224.png")));
		//
		// talkerMenu.setForeground(new Color(255, 255, 0));

		// setContentPane(talkerLogoLabel);
		// talkerLogoLabel.setIcon(new ImageIcon("images/logo_266_224.png"));
		setTitle(Main.myPersonalData.displayedName + "(" + Main.myPersonalData.talkerNumber + ")");

		addWindowListener(mainWindowFrameWindowAdapter);

		// ObjectInput objectInput = null;
		// File contacts = new File( "data/contacts.txt" );
		//
		// if( contacts.exists() )
		// {
		//
		// try
		// {
		// objectInput = new ObjectInputStream( new FileInputStream( contacts )
		// );
		// DefaultMutableTreeNode root = new DefaultMutableTreeNode( new
		// ContactGroup( "Kontakty" ) );
		// treeModel = new DefaultTreeModel( root );
		// contactsListTree.setModel( treeModel );
		// }
		// catch( IOException ex )
		// {
		// ex.printStackTrace();
		// }
		ObjectInput objectInputModel = null;

		File model = new File(Main.DIR_TO_SERIALIZED_DATA + "/model.dat");

		if (model.exists())
		{
			try
			{
				objectInputModel = new ObjectInputStream(new FileInputStream(model));
				treeModel = (DefaultTreeModel) objectInputModel.readObject();
				System.out.println("treeModel: " + treeModel);
				contactsListTree.setModel(treeModel);
			} catch (IOException ex)
			{
				ex.printStackTrace();
			} catch (ClassNotFoundException ex)
			{
				ex.printStackTrace();
			}
			// setVisible( true );

		} else
		{

			DefaultMutableTreeNode root = new DefaultMutableTreeNode(new ContactGroup("Kontakty"));
			treeModel = new DefaultTreeModel(root);
			contactsListTree.setModel(treeModel);
			// setVisible( true );
		}

		contactsListTree.addMouseListener(contactsListMouseListener);
		expandAll(contactsListTree);

		// DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer)
		// contactsListTree.getCellRenderer();

		contactsListTree.setCellRenderer(new DefaultTreeCellRenderer()
		{

			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean isLeaf, int row, boolean focused)
			{
				Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, isLeaf, row, focused);

				// System.out.println("RendererComponent: " + value);
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

				if (node.getUserObject() instanceof Contact)
				{
					// System.out.println(((Contact)value) instanceof Contact);
					setIcon(new ImageIcon(Main.class.getResource("/resources/images/contact_male.png")));

				}

				if (node.getUserObject() instanceof ContactGroup)
				{
					setIcon(new ImageIcon(Main.class.getResource("/resources/images/contacts_group.png")));
					// System.out.println("ma dzieci?: " +
					// node.getChildCount());
					//
					// if (!expanded) {
					// if (node.getChildCount() == 0) {
					// setIcon(new
					// ImageIcon(Main.class.getResource("/resources/images/contacts_group.png")));
					// } else {
					// setIcon(new
					// ImageIcon(Main.class.getResource("/resources/images/contacts_group_up.png")));
					// }
					// } else {
					// setIcon(new
					// ImageIcon(Main.class.getResource("/resources/images/contacts_group_down.png")));
					// }

				}
				return c;
			}
		});

		contactsListTree.setCellEditor(new MyTreeCellEditor(contactsListTree, (DefaultTreeCellRenderer) contactsListTree.getCellRenderer()));

		
		
		treeModel.reload(sortContacts((DefaultMutableTreeNode) treeModel.getRoot()));

		// renderer.setLeafIcon(new
		// ImageIcon(Main.class.getResource("/resources/images/contact_male.png")));
		// renderer.setOpenIcon(new
		// ImageIcon(Main.class.getResource("/resources/images/contacts_group.png")));
		// renderer.setClosedIcon(new
		// ImageIcon(Main.class.getResource("/resources/images/contacts_group.png")));
	}

	public DefaultMutableTreeNode sortContacts(DefaultMutableTreeNode node)
	{

		// sort alphabetically
		for (int i = 0; i < node.getChildCount() - 1; i++)
		{
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
			String nt = child.getUserObject().toString();

			for (int j = i + 1; j <= node.getChildCount() - 1; j++)
			{
				DefaultMutableTreeNode prevNode = (DefaultMutableTreeNode) node.getChildAt(j);
				String np = prevNode.getUserObject().toString();

				System.out.println(nt + " " + np);
				if (nt.compareToIgnoreCase(np) > 0)
				{
					node.insert(child, j);
					node.insert(prevNode, i);
				}
			}
			if (child.getChildCount() > 0)
			{
				sortContacts(child);
			}

		}

		for (int i = 0; i < node.getChildCount() - 1; i++)
		{
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
			for (int j = i + 1; j <= node.getChildCount() - 1; j++)
			{
				DefaultMutableTreeNode prevNode = (DefaultMutableTreeNode) node.getChildAt(j);

				if (!prevNode.isLeaf() && child.isLeaf())
				{
					node.insert(child, j);
					node.insert(prevNode, i);
				}
			}
		}

		return node;
	}

	public void expandAll(JTree tree)
	{
		TreeNode root = (TreeNode) tree.getModel().getRoot();
		expandAll(contactsListTree, new TreePath(root));
	}

	private void expandAll(JTree tree, TreePath parent)
	{
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0)
		{
			for (Enumeration e = node.children(); e.hasMoreElements();)
			{
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				expandAll(tree, path);
			}
		}
		tree.expandPath(parent);
		// tree.collapsePath(parent);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents()
	{

		contactPopupMenu = new javax.swing.JPopupMenu();
		editContactPopupMenuItem = new javax.swing.JMenuItem();
		deleteContactPopupMenuItem = new javax.swing.JMenuItem();
		groupPopupMenu = new javax.swing.JPopupMenu();
		addContactPopupMenuItem = new javax.swing.JMenuItem();
		addGroupPopupMenuItem = new javax.swing.JMenuItem();
		deleteGroupPopupMenuItem = new javax.swing.JMenuItem();
		jScrollPane2 = new javax.swing.JScrollPane();
		contactsListTree = new javax.swing.JTree();
		contactsListTree.setEditable(true);
		talkerLogoLabel = new javax.swing.JLabel();
		jMenuBar1 = new javax.swing.JMenuBar();
		talkerMenu = new javax.swing.JMenu();
		editPersonalDataMenuItem = new javax.swing.JMenuItem();
		contactsMenu = new javax.swing.JMenu();
		addContactMenuItem = new javax.swing.JMenuItem();
		addGroupMenuItem = new javax.swing.JMenuItem();
		editContactMenuItem = new javax.swing.JMenuItem();
		deleteContactMenuItem = new javax.swing.JMenuItem();
		settingsMenuItem = new javax.swing.JMenuItem();
		archiveMenuItem = new javax.swing.JMenuItem();
		helpMenu = new javax.swing.JMenu();
		aboutApplicationMenuItem = new javax.swing.JMenuItem();
		checkUpdatesMenuItem = new javax.swing.JMenuItem();
		exitMenuItem = new javax.swing.JMenuItem();

		editContactPopupMenuItem.setText("Edytuj kontakt");
		editContactPopupMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				editContactPopupMenuItemActionPerformed(evt);
			}
		});
		contactPopupMenu.add(editContactPopupMenuItem);

		deleteContactPopupMenuItem.setText("Usuń kontakt");
		deleteContactPopupMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				deleteContactPopupMenuItemActionPerformed(evt);
			}
		});
		contactPopupMenu.add(deleteContactPopupMenuItem);

		addContactPopupMenuItem.setText("Dodaj kontakt");
		addContactPopupMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				addContactPopupMenuItemActionPerformed(evt);
			}
		});
		groupPopupMenu.add(addContactPopupMenuItem);

		addGroupPopupMenuItem.setText("Dodaj grupę");
		addGroupPopupMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				addGroupPopupMenuItemActionPerformed(evt);
			}
		});
		groupPopupMenu.add(addGroupPopupMenuItem);

		deleteGroupPopupMenuItem.setText("Usuń grupę");
		deleteGroupPopupMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				deleteGroupPopupMenuItemActionPerformed(evt);
			}
		});
		groupPopupMenu.add(deleteGroupPopupMenuItem);

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setMinimumSize(new java.awt.Dimension(276, 320));

		jScrollPane2.setBackground(new java.awt.Color(0, 0, 0));

		javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Kontaktyyy");
		contactsListTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
		contactsListTree.setAutoscrolls(true);
		contactsListTree.setDragEnabled(true);
		contactsListTree.setRowHeight(30);
		jScrollPane2.setViewportView(contactsListTree);

		talkerMenu.setBackground(new java.awt.Color(0, 0, 0));
		talkerMenu.setText("Talker");

		editPersonalDataMenuItem.setText("Edytuj dane personalne");
		editPersonalDataMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				editPersonalDataMenuItemActionPerformed(evt);
			}
		});
		talkerMenu.add(editPersonalDataMenuItem);

		contactsMenu.setText("Kontakty");

		addContactMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
		addContactMenuItem.setText("Dodaj kontakt");
		addContactMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				addContactMenuItemActionPerformed(evt);
			}
		});
		contactsMenu.add(addContactMenuItem);

		addGroupMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
		addGroupMenuItem.setText("Dodaj grupę");
		addGroupMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				addGroupMenuItemActionPerformed(evt);
			}
		});
		contactsMenu.add(addGroupMenuItem);

		editContactMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
		editContactMenuItem.setText("Edytuj kontakt");
		editContactMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				editContactMenuItemActionPerformed(evt);
			}
		});
		contactsMenu.add(editContactMenuItem);

		deleteContactMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
		deleteContactMenuItem.setText("Usuń kontakt / grupę");
		deleteContactMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				deleteContactMenuItemActionPerformed(evt);
			}
		});
		contactsMenu.add(deleteContactMenuItem);

		talkerMenu.add(contactsMenu);

		settingsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
		settingsMenuItem.setText("Ustawienia");
		talkerMenu.add(settingsMenuItem);

		archiveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
		archiveMenuItem.setText("Archiwum");
		talkerMenu.add(archiveMenuItem);

		helpMenu.setText("Pomoc");

		aboutApplicationMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
		aboutApplicationMenuItem.setText("O programie");
		aboutApplicationMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				aboutApplicationMenuItemActionPerformed(evt);
			}
		});
		helpMenu.add(aboutApplicationMenuItem);

		checkUpdatesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
		checkUpdatesMenuItem.setText("Sprawdź aktualizacje");
		checkUpdatesMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				checkUpdatesMenuItemActionPerformed(evt);
			}
		});
		helpMenu.add(checkUpdatesMenuItem);

		talkerMenu.add(helpMenu);

		exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
		exitMenuItem.setText("Wyjście");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				exitMenuItemActionPerformed(evt);
			}
		});
		talkerMenu.add(exitMenuItem);

		jMenuBar1.add(talkerMenu);

		setJMenuBar(jMenuBar1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(talkerLogoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE).addComponent(jScrollPane2)).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addComponent(talkerLogoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
						.addContainerGap()));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	MessageFrame getMessageFrameForReceiver(int receiverNumber)
	{

		// for (MessageFrame message : messageFramesMap) {
		// if (message.receiverNumber == reveiverNumber) {
		// return message;
		// }
		// }
		// return null;
		for (MessageFrame messageFrame : Main.messageFramesMap.values())
		{
			if (messageFrame.contact.talkerNumber == receiverNumber)
			{
				return messageFrame;
			}
		}

		return null;

	}

	private void addContactMenuItemActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_addContactMenuItemActionPerformed
	{// GEN-HEADEREND:event_addContactMenuItemActionPerformed

		TreePath[] paths = contactsListTree.getSelectionModel().getSelectionPaths();

		if (contactsListTree.getSelectionModel().getSelectionCount() == 0)
		{ // add to Contacts
			JFrame addContactFrame = new AddContactFrame();
			addContactFrame.setVisible(true);
		}

		for (TreePath path : paths)
		{
			try
			{
				if (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject() instanceof ContactGroup)
				{
					JFrame addContactFrame = new AddContactFrame();
					addContactFrame.setVisible(true);
				}
			} catch (Exception e)
			{
				JOptionPane.showMessageDialog(new JFrame(), "Zaznacz grupy, do których chcesz dodać kontakt.", "Talker", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
	}// GEN-LAST:event_addContactMenuItemActionPerformed

	private void deleteContactMenuItemActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_deleteContactMenuItemActionPerformed
	{// GEN-HEADEREND:event_deleteContactMenuItemActionPerformed

		Object[] options = { "Tak", "Nie" };

		int answer = JOptionPane.showOptionDialog(new JFrame(), "Na pewno chcesz usunąć ?", "Talker", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);

		Object userObject = null;

		if (answer == 0)
		{
			TreePath[] selectedPaths = contactsListTree.getSelectionModel().getSelectionPaths();

			DefaultMutableTreeNode currentNode = null;
			DefaultMutableTreeNode parentNode = null;

			for (TreePath path : selectedPaths)
			{
				currentNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				parentNode = (DefaultMutableTreeNode) (currentNode.getParent());

				// if ((Contact) ((DefaultMutableTreeNode)
				// path.getLastPathComponent()).getUserObject() instanceof
				// Contact) {
				userObject = ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();

				if (parentNode != null)
				{
					treeModel.removeNodeFromParent(currentNode);
				}
			}

			if (userObject instanceof Contact)
			{
				Main.contactsMap.remove(((Contact) userObject).talkerNumber);
			}

			File contacts = new File(Main.DIR_TO_SERIALIZED_DATA + "/contacts.dat");

			try
			{
				contacts.createNewFile();
				ObjectOutput output = new ObjectOutputStream(new FileOutputStream(contacts));
				output.writeObject(Main.contactsMap);
				output.close();

			} catch (FileNotFoundException ex)
			{
				Logger.getLogger(AddContactFrame.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex)
			{
				Logger.getLogger(AddContactFrame.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
	}// GEN-LAST:event_deleteContactMenuItemActionPerformed

	private void editContactMenuItemActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_editContactMenuItemActionPerformed
	{// GEN-HEADEREND:event_editContactMenuItemActionPerformed
		// System.out.println("_______edytuj kontakt MENU");

		if (contactsListTree.getSelectionModel().getSelectionCount() != 1)
		{
			JOptionPane.showMessageDialog(new JFrame(), "Zaznacz jeden kontakt do edycji.", "Talker", JOptionPane.WARNING_MESSAGE);
			return;
		} else
		{

			Contact selectedContact = getSelectedContact();
			if (selectedContact != null)
			{
				EditContactFrame editContactFrame = new EditContactFrame(selectedContact);
				editContactFrame.setVisible(true);
			}
		}

	}// GEN-LAST:event_editContactMenuItemActionPerformed

	private void aboutApplicationMenuItemActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_aboutApplicationMenuItemActionPerformed
	{// GEN-HEADEREND:event_aboutApplicationMenuItemActionPerformed

	}// GEN-LAST:event_aboutApplicationMenuItemActionPerformed

	private void checkUpdatesMenuItemActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_checkUpdatesMenuItemActionPerformed
	{// GEN-HEADEREND:event_checkUpdatesMenuItemActionPerformed
	}// GEN-LAST:event_checkUpdatesMenuItemActionPerformed

	private void addGroupMenuItemActionPerformed(java.awt.event.ActionEvent evt)// GEN-FIRST:event_addGroupMenuItemActionPerformed
	{// GEN-HEADEREND:event_addGroupMenuItemActionPerformed

		if (contactsListTree.getSelectionModel().getSelectionCount() == 0)
		{
			new AddGroupFrame().setVisible(true);
		}

		TreePath[] paths = contactsListTree.getSelectionModel().getSelectionPaths();

		for (TreePath path : paths)
		{

			if (((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject() instanceof ContactGroup)
			{
				new AddGroupFrame().setVisible(true);
			} else
			{
				JOptionPane.showMessageDialog(new JFrame(), "Zaznacz grupy, do których chcesz dodać grupę kontaktów.", "Talker", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
	}// GEN-LAST:event_addGroupMenuItemActionPerformed

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_exitMenuItemActionPerformed
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}// GEN-LAST:event_exitMenuItemActionPerformed

	private void editPersonalDataMenuItemActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_editPersonalDataMenuItemActionPerformed
		new EditPersonalData(this).setVisible(true);
	}// GEN-LAST:event_editPersonalDataMenuItemActionPerformed

	private void addContactPopupMenuItemActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_addContactPopupMenuItemActionPerformed
		addContactMenuItemActionPerformed(evt);
	}// GEN-LAST:event_addContactPopupMenuItemActionPerformed

	private void addGroupPopupMenuItemActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_addGroupPopupMenuItemActionPerformed
		addGroupMenuItemActionPerformed(evt);
	}// GEN-LAST:event_addGroupPopupMenuItemActionPerformed

	private void editContactPopupMenuItemActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_editContactPopupMenuItemActionPerformed

		editContactMenuItemActionPerformed(evt);

		// Contact selectedContact = (Contact) popupObject;
		// if (selectedContact != null) {
		// EditContactFrame editContactFrame = new
		// EditContactFrame(selectedContact);
		// editContactFrame.setVisible(true);
		// }
	}// GEN-LAST:event_editContactPopupMenuItemActionPerformed

	private void deleteContactPopupMenuItemActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_deleteContactPopupMenuItemActionPerformed
		deleteContactMenuItemActionPerformed(evt);
	}// GEN-LAST:event_deleteContactPopupMenuItemActionPerformed

	private void deleteGroupPopupMenuItemActionPerformed(java.awt.event.ActionEvent evt)
	{// GEN-FIRST:event_deleteGroupPopupMenuItemActionPerformed
		deleteContactMenuItemActionPerformed(evt);
	}// GEN-LAST:event_deleteGroupPopupMenuItemActionPerformed

	static DefaultMutableTreeNode addNewGroupToSelectedNode(String groupName)
	{

		TreePath[] selectedPaths = contactsListTree.getSelectionModel().getSelectionPaths();

		DefaultMutableTreeNode newGroupNode = new DefaultMutableTreeNode(new ContactGroup(groupName));
		DefaultMutableTreeNode parentNode = null;

		if (contactsListTree.getSelectionModel().getSelectionCount() == 0)
		{// add to Contacts
			parentNode = (DefaultMutableTreeNode) treeModel.getRoot();
			treeModel.insertNodeInto(newGroupNode, parentNode, 0);
		}

		for (TreePath path : selectedPaths)
		{

			if ((ContactGroup) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject() instanceof ContactGroup)
			{

				parentNode = (DefaultMutableTreeNode) path.getLastPathComponent();
				treeModel.insertNodeInto(newGroupNode, parentNode, 0);
			} else
			{
				JOptionPane.showMessageDialog(new JFrame(), "Grupę można dodawać tylko do istniejącej grupy.", "Talker", JOptionPane.WARNING_MESSAGE);
				return null;
			}

		}

		return newGroupNode;
	}

	static Contact getSelectedContact()
	{
		if (contactsListTree.getSelectionModel().getSelectionCount() != 1)
		{
			JOptionPane.showMessageDialog(new JFrame(), "Zaznacz jeden kontakt do edycji.", "Talker", JOptionPane.WARNING_MESSAGE);
			return null;
		} else
		{

			if ((Contact) ((DefaultMutableTreeNode) contactsListTree.getSelectionModel().getSelectionPath().getLastPathComponent()).getUserObject() instanceof Contact)
			{

				Object contactObject = ((DefaultMutableTreeNode) contactsListTree.getSelectionModel().getSelectionPath().getLastPathComponent()).getUserObject();
				return (Contact) contactObject;
			} else
			{

				JOptionPane.showMessageDialog(new JFrame(), "Nie można edytować grupy. Jeśli chcesz zmienić jej nazwę - zaznacz grupę i naciśnij F2.", "Talker",
						JOptionPane.WARNING_MESSAGE);
				return null;
			}
		}

	}

	static void saveContactAfterEdit(Contact contact)
	{
		// return ( Contact )
		// contactsListTree.getSelectionModel().getSelectionPath()//.getLastPathComponent();
		treeModel.valueForPathChanged(contactsListTree.getSelectionModel().getSelectionPath(), contact);

		// if( contactsListTree.getSelectionModel().getSelectionCount() > 1 )
		// {
		// JOptionPane.showMessageDialog( new JFrame(),
		// "Nie można edytować jednocześnie kilku elementów", "",
		// JOptionPane.WARNING_MESSAGE );
		// return;
		// }
		// else
		// {
		// Contact contact = ( Contact )
		// contactsListTree.getSelectionModel().getSelectionPath().getLastPathComponent();
		//
		// }
	}

	static DefaultMutableTreeNode addContactToSelectedNodes(Contact contact)
	{
		TreePath[] selectedPaths = contactsListTree.getSelectionModel().getSelectionPaths();

		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(contact);
		DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode();

		if (contactsListTree.getSelectionModel().getSelectionCount() == 0)
		{
			parentNode = (DefaultMutableTreeNode) treeModel.getRoot();
			System.out.println("parentNode: " + parentNode);
			treeModel.insertNodeInto(childNode, parentNode, 0);
		}

		for (TreePath tp : selectedPaths)
		{

			if ((ContactGroup) ((DefaultMutableTreeNode) tp.getLastPathComponent()).getUserObject() instanceof ContactGroup)
			{
				parentNode = (DefaultMutableTreeNode) tp.getLastPathComponent();
				treeModel.insertNodeInto(childNode, parentNode, 0);
			} else
			{
				JOptionPane.showMessageDialog(new JFrame(), "Zaznacz grupę, aby dodać kontakt", "Talker", JOptionPane.WARNING_MESSAGE);
			}

		}

		return childNode;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[])
	{

		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try
		{
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
			{
				if ("Nimbus".equals(info.getName()))
				{
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;

				}

				if ("napkin".equals(info.getName()))
				{
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
		{
			java.util.logging.Logger.getLogger(MainWindowFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		/* Create and display the form */
		java.awt.EventQueue.invokeLater(() ->
		{

			new MainWindowFrame().setVisible(true);

		});

		// DefaultMutableTreeNode top = new DefaultMutableTreeNode( "Kontaktyyy"
		// );
		// DefaultMutableTreeNode group = new DefaultMutableTreeNode( "szkola"
		// );
		// top.add( group );
		// contactsListTree = new JTree( top );
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JMenuItem aboutApplicationMenuItem;
	private javax.swing.JMenuItem addContactMenuItem;
	private javax.swing.JMenuItem addContactPopupMenuItem;
	private javax.swing.JMenuItem addGroupMenuItem;
	private javax.swing.JMenuItem addGroupPopupMenuItem;
	private javax.swing.JMenuItem archiveMenuItem;
	private javax.swing.JMenuItem checkUpdatesMenuItem;
	private javax.swing.JPopupMenu contactPopupMenu;
	public static javax.swing.JTree contactsListTree;
	private javax.swing.JMenu contactsMenu;
	private javax.swing.JMenuItem deleteContactMenuItem;
	private javax.swing.JMenuItem deleteContactPopupMenuItem;
	private javax.swing.JMenuItem deleteGroupPopupMenuItem;
	private javax.swing.JMenuItem editContactMenuItem;
	private javax.swing.JMenuItem editContactPopupMenuItem;
	private javax.swing.JMenuItem editPersonalDataMenuItem;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JPopupMenu groupPopupMenu;
	private javax.swing.JMenu helpMenu;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JMenuItem settingsMenuItem;
	private javax.swing.JLabel talkerLogoLabel;
	private javax.swing.JMenu talkerMenu;
	// End of variables declaration//GEN-END:variables
}
