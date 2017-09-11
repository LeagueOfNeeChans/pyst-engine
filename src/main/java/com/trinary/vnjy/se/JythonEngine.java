/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinary.vnjy.se;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.python.core.PyFunction;
import org.python.core.PyString;
import org.python.core.PyStringMap;
import org.python.util.PythonInterpreter;

/**
 *
 * @author mmain
 */
public class JythonEngine extends GenericScriptEngine {
	private PyStringMap namespace;
	private final PythonInterpreter pi;

	public static GenericScriptEngine create(GameState gameState) throws Exception {
		JythonEngine engine = new JythonEngine();
		engine.init(gameState);
		return engine;
	}

	protected JythonEngine() {
		super();
		this.pi = new PythonInterpreter();
	}

	@Override
	protected void loadScript(Map<String, Object> shitHeap) {
		String script = (String)shitHeap.get("pystScript");
		script = new String(Base64.decodeBase64(script));
		
		pi.exec("scriptEngine.inject(globals())");
		pi.exec(script);
	}
	
	@Override
	protected String executeScene(String scene) {
		PyString s = new PyString(scene);
		if (namespace.has_key(s)) {
			PyFunction f = (PyFunction) namespace.get(s);
			return ((PyString) f.__call__()).toString();
		} else {
			return null;
		}
	}
	
	@Override
	protected void injectElement(String key, Object object) {
		pi.set(key, object);
	}
	
	public void inject(PyStringMap functionTable) {
		this.namespace = functionTable;
	}

	public PyStringMap getNamespace() {
		return namespace;
	}
}