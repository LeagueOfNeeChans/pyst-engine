/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinary.vnjy.injects;

import com.trinary.vnjy.engines.GenericScriptEngine;

/**
 *
 * @author dstillz
 */
public class PystEngine {
	protected GenericScriptEngine se;

	public PystEngine(GenericScriptEngine se) {
		this.se = se;
	}

	public void choice(String text, String next) {
		se.addCommand("ui.choice.prompt", text, next);
		se.addValidTransition(next);
	}
}