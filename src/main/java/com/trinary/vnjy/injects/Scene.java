/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinary.vnjy.injects;

import java.util.Arrays;

import com.trinary.vnjy.engines.GenericScriptEngine;

/**
 *
 * @author dstillz
 */
public class Scene {
	protected GenericScriptEngine se;

	public Scene(GenericScriptEngine se) {
		this.se = se;
	}

	public void move(String location) {
		se.addCommand("ui.scene.move", location, "");
	}
	
	public void enter(String location) {
		se.addCommand("ui.scene.enter", Arrays.asList(location, "", ""));
	}
	
	public void enter(String location, String transition, Integer speed) {
		se.addCommand("ui.scene.enter", Arrays.asList(location, transition, speed.toString()));
	}
	
	public void exit() {
		se.addCommand("ui.scene.exit", Arrays.asList("", ""));
	}
	
	public void exit(String transition, Integer speed) {
		se.addCommand("ui.scene.exit", Arrays.asList(transition, speed.toString()));
	}

	public void add_actor(Actor actor, String side) {
		se.addCommand("ui.scene.add", actor.getName(), side);
	}

	public void remove_actor(Actor actor) {
		se.addCommand("ui.scene.remove", actor.getName(), "");
	}

	public void display_image(String resource) {
		se.addCommand("ui.scene.display", resource, "");
	}
}
