/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinary.vnjy;

import com.trinary.vnjy.se.Command;
import com.trinary.vnjy.se.GameState;
import com.trinary.vnjy.se.ScriptEngine;

/**
 *
 * @author mmain
 */
public class Application {
	public static void main(String[] args) throws Exception {
		// Initialize player state
		GameState state = GameState.create("lon");

		// Initialize script engine.
		ScriptEngine se = ScriptEngine.create(state);

		se.nextScene();
		se.nextScene();

		for (Command command : se.flushCommands()) {
			System.out.println("COMMAND");
			System.out.println(command);
		}
	}
}