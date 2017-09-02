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
public class PystEngine {
	protected ScriptEngine se;

	public PystEngine(ScriptEngine se) {
		this.se = se;
	}

	public void choice(String text, String next) {
		se.addCommand("ui.choice.prompt", text, next);
		se.addValidTransition(next);
	}
}