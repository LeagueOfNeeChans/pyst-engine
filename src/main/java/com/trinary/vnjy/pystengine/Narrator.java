/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinary.vnjy.pystengine;

import com.trinary.vnjy.se.ScriptEngine;

/**
 *
 * @author dstillz
 */
public class Narrator extends Actor {
	public Narrator(ScriptEngine se) {
		super("narrator", se);
	}

	public void say(String dialogue) {
		se.addCommand("ui.narrator.say", name, dialogue);
	}
}