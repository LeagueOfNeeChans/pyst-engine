/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinary.vnjy.pystengine;

import java.util.ArrayList;
import java.util.Arrays;

import com.trinary.vnjy.se.GenericScriptEngine;

/**
 *
 * @author dstillz
 */
public class Actor {
	protected GenericScriptEngine se;
	protected String name = "";
	protected ArrayList<String> switches = new ArrayList<>();
	protected ArrayList<String> inventory = new ArrayList<>();

	public Actor(String name, GenericScriptEngine se) {
		this.name = name;
		this.se = se;
	}

	public GenericScriptEngine getSe() {
		return se;
	}

	public void setSe(GenericScriptEngine se) {
		this.se = se;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void set(String name) {
		if (!this.switches.contains(name)) {
			this.switches.add(name);
		}
	}

	public void give(Actor target, String item) {
		if (this.has(item)) {
			target.receive(item);
		}
	}

	public void receive(String name) {
		this.inventory.add(name);
	}

	public boolean is_set(String switch_name) {
		return (this.switches.contains(switch_name));
	}

	public boolean has(String item) {
		return (this.inventory.contains(item));
	}
	
	public void enterScene(String mood, String actorPosition) {
		se.addCommand("ui.actor.enter", Arrays.asList(name, mood, actorPosition, "", ""));
	}
	
	public void exitScene() {
		se.addCommand("ui.actor.exit", Arrays.asList(name, "", ""));
	}
	
	public void enterScene(String mood, String actorPosition, String transition, Integer speed) {
		se.addCommand("ui.actor.enter", Arrays.asList(name, mood, actorPosition, transition, speed.toString()));
	}
	
	public void exitScene(String transition, Integer speed) {
		se.addCommand("ui.actor.exit", Arrays.asList(name, transition, speed.toString()));
	}

	public void say(String dialogue) {
		se.addCommand("ui.actor.say", name, dialogue);
	}

	public void change(String mood) {
		se.addCommand("ui.actor.change", Arrays.asList(name, mood, "", ""));
	}
	
	public void change(String mood, String transition1, String transition2, Integer speed) {
		se.addCommand("ui.actor.change", Arrays.asList(name, mood, transition1, transition2, speed.toString()));
	}
}
