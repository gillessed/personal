package com.gillessed.spacepenguin.gui.renderproperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gillessed.spacepenguin.gui.Pair;
import com.gillessed.spacepenguin.gui.RenderTarget;
import com.gillessed.spacepenguin.gui.RenderTarget.RenderState;

public abstract class RenderSettings {
	
	private final List<RenderRule> rules;
	
	public RenderSettings() {
		rules = new ArrayList<RenderRule>();
	}
	
	protected void addRenderRule(RenderRule rule) {
		rules.add(rule);
	}
	
	public List<RenderRule> getRenderRules() {
		return rules;
	}
	
	public void agglomerateRules(RenderSettings settings) {
		for(RenderRule rr : settings.getRenderRules()) {
			addRenderRule(rr);
		}
	}
	
	public void childAdded(RenderTarget child) {
		child.setRenderSettings(this);
		updateSettings(child);
		child.getScreen().setUpdate(true);
	}
	
	public void updateSettings(RenderTarget child) {
		if(child.getId().equals("text1")) {
			System.out.println("hi");
		}
		Set<Pair<RenderProperties.Props, RenderState>> props = new HashSet<Pair<RenderProperties.Props, RenderState>>();
		Pair<RenderProperties.Props, RenderTarget.RenderState> temp = new Pair<RenderProperties.Props, RenderTarget.RenderState>();
		for(RenderRule rule : rules) {
			temp.setFirst(rule.property);
			temp.setSecond(rule.state);
			if(rule.idNames.contains(child.getId()) && !props.contains(temp)) {
				 child.getRenderProperties().set(rule.property, rule.value, rule.state);
				 props.add(new Pair<RenderProperties.Props, RenderTarget.RenderState>(rule.property, rule.state));
			}
		}
		for(RenderRule rule : rules) {
			temp.setFirst(rule.property);
			temp.setSecond(rule.state);
			for(String className : child.getClasses()) {
				if(rule.classNames.contains(className) && rule.idNames.isEmpty()
						&& !props.contains(temp)) {
					 child.getRenderProperties().set(rule.property, rule.value, rule.state);
					 props.add(new Pair<RenderProperties.Props, RenderTarget.RenderState>(rule.property, rule.state));
				}
			}
		}
		for(RenderRule rule : rules) {
			temp.setFirst(rule.property);
			temp.setSecond(rule.state);
			if(rule.typeNames.contains(child.getClass().getSimpleName().toLowerCase()) && rule.idNames.isEmpty()
					 && rule.classNames.isEmpty() && !props.contains(temp)) {
				 child.getRenderProperties().set(rule.property, rule.value, rule.state);
				 props.add(new Pair<RenderProperties.Props, RenderTarget.RenderState>(rule.property, rule.state));
			}
		}
		for(RenderProperties.Props p : RenderProperties.Props.values()) {
			for(RenderState state : RenderState.values()) {
				temp.setFirst(p);
				temp.setSecond(state);
				if(!props.contains(temp)) {
					child.getRenderProperties().remove(p, state);
				}
			}
		}
	}
}
