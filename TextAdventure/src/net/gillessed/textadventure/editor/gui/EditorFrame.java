package net.gillessed.textadventure.editor.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import net.gillessed.textadventure.datatype.DataType;
import net.gillessed.textadventure.datatype.EnemyArea;
import net.gillessed.textadventure.datatype.EnemyDescription;
import net.gillessed.textadventure.datatype.Event;
import net.gillessed.textadventure.datatype.FriendlyArea;
import net.gillessed.textadventure.datatype.Interaction;
import net.gillessed.textadventure.datatype.ItemDescription;
import net.gillessed.textadventure.datatype.PlayableCharacter;
import net.gillessed.textadventure.datatype.Shop;
import net.gillessed.textadventure.datatype.Skill;
import net.gillessed.textadventure.datatype.Universe;
import net.gillessed.textadventure.datatype.UniverseDAO;
import net.gillessed.textadventure.datatype.Variable;
import net.gillessed.textadventure.datatype.World;
import net.gillessed.textadventure.editor.gui.custom.UniverseTreeCellRenderer;
import net.gillessed.textadventure.editor.gui.editorpanel.CharacterEditorPanel;
import net.gillessed.textadventure.editor.gui.editorpanel.DataEditorPanel;
import net.gillessed.textadventure.editor.gui.editorpanel.EnemyAreaEditorPanel;
import net.gillessed.textadventure.editor.gui.editorpanel.EnemyEditorPanel;
import net.gillessed.textadventure.editor.gui.editorpanel.EventEditorPanel;
import net.gillessed.textadventure.editor.gui.editorpanel.FriendlyAreaEditorPanel;
import net.gillessed.textadventure.editor.gui.editorpanel.InteractionEditorPanel;
import net.gillessed.textadventure.editor.gui.editorpanel.ItemEditorPanel;
import net.gillessed.textadventure.editor.gui.editorpanel.ShopEditorPanel;
import net.gillessed.textadventure.editor.gui.editorpanel.SkillEditorPanel;
import net.gillessed.textadventure.editor.gui.editorpanel.UniverseEditorPanel;
import net.gillessed.textadventure.editor.gui.editorpanel.VariableEditorPanel;
import net.gillessed.textadventure.editor.gui.editorpanel.WorldEditorPanel;
import net.gillessed.textadventure.utils.IconUtils;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

@SuppressWarnings("serial")
public class EditorFrame extends JFrame {
	
	private static final int SAVED = 0;
	private static final int CANCELLED = 1;
	
	public static final String TITLE_BAR_STRING = "Text Adventure Editor";
	public static final String WORLDS_NODE_NAME = "Worlds";
	public static final String EVENTS_NODE_NAME = "Events";
	public static final String ENEMIES_NODE_NAME = "Enemies";
	public static final String ITEMS_NODE_NAME = "Items";
	public static final String SKILLS_NODE_NAME = "Skills";
	public static final String CHARACTERS_NODE_NAME = "Characters";
	public static final String VARIABLES_NODE_NAME = "Variables";
	public static final String FRIENDLY_AREAS_NODE_NAME = "Friendly Areas";
	public static final String ENEMY_AREAS_NODE_NAME = "Enemy Areas";
	public static final String SHOPS_NODE_NAME = "Shops";
	public static final String INTERACTIONS_NODE_NAME = "Interactions";
	
	private File targetFile;
	private boolean unsavedChanges;
	private String gameName;
	private boolean edited;
	private Universe model;
	private final UniverseDAO universeDAO;
	
	private final JMenuBar menuBar;
	private final JMenu fileMenu;
	private final JMenuItem newMenuItem;
	private final JMenuItem saveMenuItem;
	private final JMenuItem saveAsMenuItem;
	private final JMenuItem openMenuItem;
	private final JMenuItem quitMenuItem;
	
	private JTree universeTree;
	private DataEditorPanel<? extends DataType> activeEditor;
	private DefaultMutableTreeNode rootNode;
	private final DefaultTreeModel universeTreeModel;
	private final UniverseTreeCellRenderer universeTreeRenderer;
	private final TreeSelectionListener universeTreeSelectionListener = new TreeSelectionListener() {
		
		@Override
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)
					(getUniverseTree().getLastSelectedPathComponent());
			if (node == null) {
				return;
			}
	
		    Object nodeObject = node.getUserObject();
		    if(nodeObject instanceof DataType) {
		    	newDataSelected((DataType)nodeObject);
		    }
		}
	};

	private final CellConstraints cc;
	
	public EditorFrame() {
		edited = false;
		universeDAO = new UniverseDAO();
		
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		newMenuItem = new JMenuItem("New");
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		newMenuItem.setIcon(IconUtils.NEW_ICON_16);
		newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(confirmSave() == SAVED) {
					new NewGameDialog(EditorFrame.this);
				}
			}
		});
		fileMenu.add(newMenuItem);
		saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
		saveMenuItem.setIcon(IconUtils.SAVE_ICON_16);
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		fileMenu.add(saveMenuItem);
		saveAsMenuItem = new JMenuItem("Save As");
		saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
		saveAsMenuItem.setIcon(IconUtils.SAVEAS_ICON_16);
		saveAsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAs();
			}
		});
		fileMenu.add(saveAsMenuItem);
		openMenuItem = new JMenuItem("Open");
		openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
		openMenuItem.setIcon(IconUtils.OPEN_ICON_16);
		openMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(confirmSave() == SAVED) {
					open();
				}
			}
		});
		fileMenu.add(openMenuItem);
		quitMenuItem = new JMenuItem("Quit");
		quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
		quitMenuItem.setIcon(IconUtils.QUIT_ICON_16);
		quitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		fileMenu.add(quitMenuItem);
		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
		
		FormLayout formLayout = new FormLayout("200dlu, fill:pref:grow", "fill:pref:grow");
		getContentPane().setLayout(formLayout);
		cc = new CellConstraints();

		universeTreeModel = new DefaultTreeModel(rootNode);
		rootNode = new DefaultMutableTreeNode("Universe");
		universeTreeModel.setRoot(rootNode);
		universeTreeRenderer = new UniverseTreeCellRenderer();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
	}

	public Universe getModel() {
		return model;
	}

	public void setModel(Universe model) {
		if(this.model == null) {
			createEditorUI();
		} else {
			DataType.clearData();
		}
		setTitle(TITLE_BAR_STRING + " - " + getGameName());
		this.model = model;
		updateUI(true);
	}

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public File getTargetFile() {
		return targetFile;
	}

	public void setTargetFile(File targetFile) {
		this.targetFile = targetFile;
	}
	
	public void createNewModel(String name) {
		Universe universe = new Universe();
		setGameName(name);
		setModel(universe);
		setUnsavedChanges(true);
	}
	
	public void setActiveEditor(DataEditorPanel<? extends DataType> newActiveEditor) {
		if(activeEditor != null) {
			activeEditor.save();
			getContentPane().remove(activeEditor);
		}
		activeEditor = newActiveEditor;
		getContentPane().add(activeEditor, cc.xy(2, 1));
		universeTree.validate();
		setUnsavedChanges(true);
		validate();
		repaint();
	}
	
	private void createEditorUI() {
		universeTree = new JTree(rootNode);
		universeTree.setModel(universeTreeModel);
		universeTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		universeTree.setCellRenderer(universeTreeRenderer);
		universeTree.addTreeSelectionListener(universeTreeSelectionListener);
				
		JScrollPane pane = new JScrollPane(universeTree,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		getContentPane().add(pane, cc.xy(1, 1));
	}
	
	private JTree getUniverseTree() {
		return universeTree;
	}
	
	public void updateUI(boolean resetActiveEditor) {
		rootNode = new DefaultMutableTreeNode(model);
		DefaultMutableTreeNode worldsNode = new DefaultMutableTreeNode(
				WORLDS_NODE_NAME);
		rootNode.add(worldsNode);
		for(World world : model.getWorlds()) {
			DefaultMutableTreeNode worldNode = new DefaultMutableTreeNode(world);
			DefaultMutableTreeNode friendlyAreasNode = new DefaultMutableTreeNode(FRIENDLY_AREAS_NODE_NAME);
			for(FriendlyArea friendlyArea : world.getFriendlyAreas()) {
				DefaultMutableTreeNode friendlyAreaNode = new DefaultMutableTreeNode(friendlyArea);
				DefaultMutableTreeNode shopsNode = new DefaultMutableTreeNode(SHOPS_NODE_NAME);
				for(Shop shop : friendlyArea.getShops()) {
					shopsNode.add(new DefaultMutableTreeNode(shop));
				}
				friendlyAreaNode.add(shopsNode);
				DefaultMutableTreeNode interactionsNode = new DefaultMutableTreeNode(INTERACTIONS_NODE_NAME);
				for(Interaction interaction : friendlyArea.getInteractions()) {
					interactionsNode.add(new DefaultMutableTreeNode(interaction));
				}
				friendlyAreaNode.add(interactionsNode);
				friendlyAreasNode.add(friendlyAreaNode);
			}
			worldNode.add(friendlyAreasNode);
			DefaultMutableTreeNode enemyAreasNode = new DefaultMutableTreeNode(ENEMY_AREAS_NODE_NAME);
			for(EnemyArea enemyArea : world.getEnemyAreas()) {
				DefaultMutableTreeNode enemyAreaNode = new DefaultMutableTreeNode(enemyArea);
				//TODO load enemy area node
				enemyAreasNode.add(enemyAreaNode);
			}
			worldNode.add(enemyAreasNode);
			worldsNode.add(worldNode);
		}
		DefaultMutableTreeNode eventsNode = new DefaultMutableTreeNode(
				EVENTS_NODE_NAME);
		for(Event event : model.getEvents()) {
			eventsNode.add(new DefaultMutableTreeNode(event));
		}
		rootNode.add(eventsNode);
		DefaultMutableTreeNode enemiesNode = new DefaultMutableTreeNode(
				ENEMIES_NODE_NAME);
		for(EnemyDescription enemy : model.getEnemies()) {
			DefaultMutableTreeNode enemyNode = new DefaultMutableTreeNode(enemy);
			enemiesNode.add(enemyNode);
		}
		rootNode.add(enemiesNode);
		DefaultMutableTreeNode itemsNode = new DefaultMutableTreeNode(
				ITEMS_NODE_NAME);
		for(ItemDescription itemDescription : model.getItems()) {
			DefaultMutableTreeNode itemNode = new DefaultMutableTreeNode(itemDescription);
			itemsNode.add(itemNode);
		}
		rootNode.add(itemsNode);
		DefaultMutableTreeNode skillsNode = new DefaultMutableTreeNode(
				SKILLS_NODE_NAME);
		for(Skill skill : model.getSkills()) {
			DefaultMutableTreeNode skillNode = new DefaultMutableTreeNode(skill);
			skillsNode.add(skillNode);
		}
		rootNode.add(skillsNode);
		DefaultMutableTreeNode charactersNode = new DefaultMutableTreeNode(
				CHARACTERS_NODE_NAME);
		for(PlayableCharacter character : model.getCharacters()) {
			DefaultMutableTreeNode characterNode = new DefaultMutableTreeNode(character);
			charactersNode.add(characterNode);
		}
		rootNode.add(charactersNode);
		DefaultMutableTreeNode variablesNode = new DefaultMutableTreeNode(
				VARIABLES_NODE_NAME);
		for(Variable variable: model.getVariables()) {
			DefaultMutableTreeNode variableNode = new DefaultMutableTreeNode(variable);
			variablesNode.add(variableNode);
		}
		rootNode.add(variablesNode);
		universeTreeModel.setRoot(rootNode);
		
		//TODO: add the rest of the nodes
		
		DefaultMutableTreeNode currentNode = rootNode;
		do {
			universeTree.expandPath(new TreePath(currentNode.getPath()));
			if(currentNode != null && activeEditor != null) {
				if(currentNode.getUserObject() != null &&
						currentNode.getUserObject().equals(activeEditor.getModel())) {
					universeTree.setSelectionPath(new TreePath(currentNode.getPath()));
				}
			}
			currentNode = currentNode.getNextNode();
		} while (currentNode != null);
		universeTree.repaint();
		
		if(resetActiveEditor) {
			setActiveEditor(new UniverseEditorPanel(model, this));
		}
		validate();
		repaint();
	}
	
	public void newDataSelected(DataType type) {
		if(type != activeEditor.getModel()) {
			DataEditorPanel<? extends DataType> newActiveEditor = null;
			if(type instanceof Universe) {
				newActiveEditor = new UniverseEditorPanel((Universe)type, this);
			} else if(type instanceof World) {
				newActiveEditor = new WorldEditorPanel((World)type, this);
			} else if(type instanceof ItemDescription) {
				newActiveEditor = new ItemEditorPanel((ItemDescription)type, this);
			} else if(type instanceof FriendlyArea) {
				newActiveEditor = new FriendlyAreaEditorPanel((FriendlyArea)type, this);
			} else if(type instanceof Event) {
				newActiveEditor = new EventEditorPanel((Event)type, this);
			} else if(type instanceof Skill) {
				newActiveEditor = new SkillEditorPanel((Skill)type, this);
			} else if(type instanceof Shop) {
				newActiveEditor = new ShopEditorPanel((Shop)type, this);
			} else if(type instanceof Interaction) {
				newActiveEditor = new InteractionEditorPanel((Interaction)type, this);
			} else if(type instanceof PlayableCharacter) {
				newActiveEditor = new CharacterEditorPanel((PlayableCharacter)type, this);
			} else if(type instanceof Variable) {
				newActiveEditor = new VariableEditorPanel((Variable)type, this);
			} else if(type instanceof EnemyDescription) {
				newActiveEditor = new EnemyEditorPanel((EnemyDescription)type, this);
			} else if(type instanceof EnemyArea) {
				newActiveEditor = new EnemyAreaEditorPanel((EnemyArea)type, this);
			}
			//TODO: add other data type editors
			if(newActiveEditor == null) {
				throw new RuntimeException("No editor panel for " + type.getClass().getName() + " implemented yet!");
			} else {
				setActiveEditor(newActiveEditor);
				updateUI(false);
				universeTree.repaint();
			}
		}
	}

	public boolean isUnsavedChanges() {
		return unsavedChanges;
	}

	public void setUnsavedChanges(boolean unsavedChanges) {
		this.unsavedChanges = unsavedChanges;
	}
	
	private int confirmSave() {
		if(isUnsavedChanges()) {
			int result = JOptionPane.showConfirmDialog(EditorFrame.this, "\"" + getGameName() + "\" has unsaved changes." +
					" Do you wish to save them?", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				result = save();
				if(result == CANCELLED) {
					return CANCELLED;
				}
			} else if(result == JOptionPane.CANCEL_OPTION) {
				return CANCELLED;
			}
			return SAVED;
		} else {
			return SAVED;
		}
	}
	
	private int confirm(File f) {
		if(f.exists()){
            int result = JOptionPane.showConfirmDialog(this,"The file \"" + f.getName()
            		+ "\" exists. Do you want to overwrite it?" ,"Existing File", JOptionPane.YES_NO_CANCEL_OPTION);
            switch(result){
                case JOptionPane.YES_OPTION:
                    return SAVED;
                case JOptionPane.NO_OPTION:
                    return CANCELLED;
                case JOptionPane.CLOSED_OPTION:
                    return CANCELLED;
                case JOptionPane.CANCEL_OPTION:
                    return CANCELLED;
            }
        }
		return SAVED;
	}
	
	private int save() {
		if(targetFile == null) {
			JFileChooser fileChooser = getFileChooser();
			int result = fileChooser.showSaveDialog(this);
			if(result == JFileChooser.APPROVE_OPTION) {
				String filename = fileChooser.getSelectedFile().getAbsolutePath();
				if(!filename.endsWith(".tag")) {
					filename += ".tag";
				}
				File newFile = new File(filename);
				result = confirm(newFile);
				if(result != SAVED) {
					return CANCELLED;
				}
				targetFile = newFile;
			} else {
				return CANCELLED;
			}
		}
		saveData(targetFile);
		setUnsavedChanges(false);
		return SAVED;
	}
	
	private void saveAs() {
		JFileChooser fileChooser = getFileChooser();
		int result = fileChooser.showSaveDialog(this);
		if(result == JFileChooser.APPROVE_OPTION) {
			String filename = fileChooser.getSelectedFile().getAbsolutePath();
			if(!filename.endsWith(".tag")) {
				filename += ".tag";
			}
			File newFile = new File(filename);
			result = confirm(newFile);
			if(result == SAVED) {
				saveData(newFile);
			}
		}
	}
	
	private void open() {
		if(targetFile == null) {
			JFileChooser fileChooser = getFileChooser();
			int result = fileChooser.showOpenDialog(this);
			if(result == JFileChooser.APPROVE_OPTION) {
				targetFile = fileChooser.getSelectedFile();
			} else {
				return;
			}
			readData(targetFile);
		}
	}
	
	private void saveData(File targetFile) {
		activeEditor.save();
		universeDAO.saveUniverse(targetFile, model, getGameName());
	}
	
	private void readData(File f) {
		universeDAO.readUniverse(f);
		setGameName(universeDAO.getGameName());
		setModel(universeDAO.getUniverse());
	}
	
	private void close() {
		if(confirmSave() == SAVED) {
			System.exit(0);
		}
	}
	
	private JFileChooser getFileChooser() {
		JFileChooser fileChooser = new JFileChooser();
		String dir = System.getProperty("user.dir");
		dir += File.separator;
		dir += "universes";
		fileChooser.setCurrentDirectory(new File(dir));
		return fileChooser;
	}
}