package net.gillessed.textadventure.editor.gui.editorpanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;

import net.gillessed.textadventure.datatype.EnemyDescription;
import net.gillessed.textadventure.datatype.Event;
import net.gillessed.textadventure.datatype.FriendlyArea;
import net.gillessed.textadventure.datatype.ItemDescription;
import net.gillessed.textadventure.datatype.PlayableCharacter;
import net.gillessed.textadventure.datatype.Skill;
import net.gillessed.textadventure.datatype.Universe;
import net.gillessed.textadventure.datatype.Variable;
import net.gillessed.textadventure.datatype.World;
import net.gillessed.textadventure.editor.gui.EditorFrame;
import net.gillessed.textadventure.utils.IconUtils;

@SuppressWarnings("serial")
public class UniverseEditorPanel extends DataEditorPanel<Universe> {

	private final JButton newWorldButton;
	private final JButton newEventButton;
	private final JButton newEnemyButton;
	private final JButton newItemButton;
	private final JButton newSkillButton;
	private final JButton newCharacterButton;
	private final JButton newVariableButton;
	private final JComboBox<Event> firstEventComboBox;
	private final JComboBox<World> firstWorldComboBox;
	private final DefaultComboBoxModel<FriendlyArea> areaComboBoxModel;
	private final JComboBox<FriendlyArea> firstAreaComboBox;
	private final JComboBox<Character> firstCharacterComboBox;

	public UniverseEditorPanel(Universe model, EditorFrame editorFrame) {
		super(model, editorFrame, 5, 7);

		newWorldButton = new JButton("New World");
		newWorldButton.setIcon(IconUtils.WORLDS_ICON_16);
		newWorldButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				World newWorld = new World(UniverseEditorPanel.this.model);
				UniverseEditorPanel.this.model.getWorlds().add(newWorld);
				UniverseEditorPanel.this.editorFrame.newDataSelected(newWorld);
			}
		});
		addNewButton(newWorldButton);

		newEventButton = new JButton("New Event");
		newEventButton.setIcon(IconUtils.EVENTS_ICON_16);
		newEventButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Event newEvent = new Event(UniverseEditorPanel.this.model);
				UniverseEditorPanel.this.model.getEvents().add(newEvent);
				UniverseEditorPanel.this.editorFrame.newDataSelected(newEvent);
			}
		});
		addNewButton(newEventButton);

		newEnemyButton = new JButton("New Enemy");
		newEnemyButton.setIcon(IconUtils.ENEMIES_ICON_16);
		newEnemyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EnemyDescription newEnemy = new EnemyDescription(UniverseEditorPanel.this.model);
				UniverseEditorPanel.this.model.getEnemies().add(newEnemy);
				UniverseEditorPanel.this.editorFrame.newDataSelected(newEnemy);
			}
		});
		addNewButton(newEnemyButton);

		newItemButton = new JButton("New Item");
		newItemButton.setIcon(IconUtils.ITEM_ICON_16);
		newItemButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final ItemDescription newItem = new ItemDescription(UniverseEditorPanel.this.model);
				UniverseEditorPanel.this.model.getItems().add(newItem);
				UniverseEditorPanel.this.editorFrame.newDataSelected(newItem);
			}
		});
		addNewButton(newItemButton);

		newSkillButton = new JButton("New Skill");
		newSkillButton.setIcon(IconUtils.SKILLS_ICON_16);
		newSkillButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final Skill newSkill = new Skill(UniverseEditorPanel.this.model);
				UniverseEditorPanel.this.model.getSkills().add(newSkill);
				UniverseEditorPanel.this.editorFrame.newDataSelected(newSkill);
			}
		});
		addNewButton(newSkillButton);

		newCharacterButton = new JButton("New Character");
		newCharacterButton.setIcon(IconUtils.CHARACTERS_ICON_16);
		newCharacterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final PlayableCharacter newCharacter = new PlayableCharacter(UniverseEditorPanel.this.model);
				UniverseEditorPanel.this.model.getCharacters().add(newCharacter);
				UniverseEditorPanel.this.editorFrame.newDataSelected(newCharacter);
			}
		});
		addNewButton(newCharacterButton);

		newVariableButton = new JButton("New Variable");
		newVariableButton.setIcon(IconUtils.VARIABLE_ICON_16);
		newVariableButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final Variable newVariable = new Variable(UniverseEditorPanel.this.model);
				UniverseEditorPanel.this.model.getVariables().add(newVariable);
				UniverseEditorPanel.this.editorFrame.newDataSelected(newVariable);
			}
		});
		addNewButton(newVariableButton);

		firstEventComboBox = new JComboBox<Event>(model.getEvents().toArray(new Event[0]));
		if(model.getFirstEvent() != null) {
			firstEventComboBox.setSelectedItem(model.getFirstEvent());
		}
		getPropertyPanel().addProperty("First Event: ", firstEventComboBox);

		firstWorldComboBox = new JComboBox<World>(model.getWorlds().toArray(new World[0]));
		if(model.getFirstWorld() != null) {
			firstWorldComboBox.setSelectedItem(model.getFirstWorld());
		}
		firstWorldComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				areaComboBoxModel.removeAllElements();
				for(FriendlyArea fa : ((World)firstWorldComboBox.getSelectedItem()).getFriendlyAreas()) {
					areaComboBoxModel.addElement(fa);
				}
			}
		});
		getPropertyPanel().addProperty("First World: ", firstWorldComboBox);

		if(model.getFirstWorld() == null) {
			if(firstWorldComboBox.getSelectedItem() == null) {
				areaComboBoxModel = new DefaultComboBoxModel<FriendlyArea>();
				firstAreaComboBox = new JComboBox<FriendlyArea>(areaComboBoxModel);
			} else {
				areaComboBoxModel = new DefaultComboBoxModel<FriendlyArea>(((World)firstWorldComboBox.getSelectedItem())
						.getFriendlyAreas().toArray(new FriendlyArea[0]));
				firstAreaComboBox = new JComboBox<FriendlyArea>(areaComboBoxModel);
			}
		} else {
			areaComboBoxModel = new DefaultComboBoxModel<FriendlyArea>(model.getFirstWorld().getFriendlyAreas().toArray(new FriendlyArea[0]));
			firstAreaComboBox = new JComboBox<FriendlyArea>(areaComboBoxModel);
			if(model.getFirstEvent() != null) {
				firstEventComboBox.setSelectedItem(model.getFirstArea());
			}
		}
		getPropertyPanel().addProperty("First Area: ", firstAreaComboBox);
		
		firstCharacterComboBox = new JComboBox<Character>();
		if(model.getFirstWorld() != null) {
			firstCharacterComboBox.setSelectedItem(model.getStartingCharacter());
		}
		getPropertyPanel().addProperty("First Character: ", firstCharacterComboBox);

		generateSaveDeletePanel(false);
	}

	@Override
	public void save() {
		model.setFirstEvent((Event) firstEventComboBox.getSelectedItem());
		model.setFirstWorld((World) firstWorldComboBox.getSelectedItem());
		model.setFirstArea((FriendlyArea) firstAreaComboBox.getSelectedItem());
		model.setStartingCharacter((PlayableCharacter) firstCharacterComboBox.getSelectedItem());
	}

	@Override
	public void delete() {
		// nothing. cannot delete the universe.
	}

}
