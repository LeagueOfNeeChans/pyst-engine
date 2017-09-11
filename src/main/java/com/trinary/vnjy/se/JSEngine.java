/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinary.vnjy.se;

import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author mmain
 */
public class JSEngine extends GenericScriptEngine {
	private final ScriptEngine jsEngine;

	public static GenericScriptEngine create(GameState gameState) throws Exception {
		JSEngine engine = new JSEngine();
		engine.init(gameState);
		return engine;
	}

	private JSEngine() {
		super();
		ScriptEngineManager manager = new ScriptEngineManager();
		this.jsEngine = manager.getEngineByName("Nashorn");
	}
	
	@Override
	protected void injectElement(String key, Object object) {
		jsEngine.put(key, object);
	}

	@Override
	protected void loadScript(Map<String, Object> shitHeap) {
		String script = (String)shitHeap.get("jystScript");
		script = new String(Base64.decodeBase64(script));
		try {
			jsEngine.eval(script);
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String executeScene(String sceneName) {
		Invocable inv = (Invocable)jsEngine;
		try {
			return (String)inv.invokeFunction(sceneName);
		} catch (NoSuchMethodException | ScriptException e) {
			e.printStackTrace();
			return null;
		}
	}
}