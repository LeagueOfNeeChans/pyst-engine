/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinary.vnjy.se;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.python.core.PyFunction;
import org.python.core.PyString;
import org.python.core.PyStringMap;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trinary.vnjy.pystengine.Actor;
import com.trinary.vnjy.pystengine.Narrator;
import com.trinary.vnjy.pystengine.Player;
import com.trinary.vnjy.pystengine.PystEngine;
import com.trinary.vnjy.pystengine.Scene;

/**
 *
 * @author mmain
 */
public class ScriptEngine {
	private PyStringMap namespace;
	private final List<Command> commands;
	private final PythonInterpreter pi;
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final TypeReference<Map<String, Object>> t = new TypeReference<Map<String, Object>>() {};

	private GameState gameState;

	public static ScriptEngine create(GameState gameState) throws Exception {
		ScriptEngine engine = new ScriptEngine();
		engine.init(gameState);
		return engine;
	}

	private ScriptEngine() {
		this.pi = new PythonInterpreter();
		this.commands = new LinkedList<>();
	}

	@SuppressWarnings("unchecked")
	private void init(GameState gameState) throws Exception {
		if (gameState == null) {
			throw new Exception("GameState cannot be null.");
		}

		this.gameState = gameState;
		
		// Find game's shit
		String shitFile = "/var/games/" + gameState.getGame() + ".shit";
		File shitFileObject = new File(shitFile);
		if (!shitFileObject.exists()) {
			throw new Exception("Cannot find shit for game: "+ gameState.getGame());
		}
		
		// Load in the shit heap
		Map<String, Object> shitHeap = mapper.readValue(shitFileObject, t);
		
		// Create player if uninitialized
		if (gameState.getPlayer() == null) {
			String playerName = "Guy";
			gameState.setPlayer(new Player(playerName, this));
		}
		
		// Read in submaps (TODO open into object)
		Map<String, Object> assets = (Map<String, Object>)shitHeap.get("assets");
		Map<String, Object> actors = (Map<String, Object>)assets.get("actors");
		String script = (String)shitHeap.get("script");

		// Load actors if uninitialized
		if (gameState.getActors() == null || gameState.getActors().isEmpty()) {
			for (String actorName : actors.keySet()) {
				System.out.println("ACTOR " + actorName + " FOUND!");
				gameState.getActors().add(new Actor(actorName, this));
			}
		}
		
		// Inject script engine
		pi.set("scriptEngine", this);
		pi.exec("scriptEngine.inject(globals())");
		pi.set("engine", new PystEngine(this));

		// Inject game elements
		pi.set("scene", new Scene(this));
		pi.set("narrator", new Narrator(this));
		pi.set("player", gameState.getPlayer());
		for (Actor actor : gameState.getActors()) {
			actor.setSe(this);
			pi.set(actor.getName(), actor);
		}
		
		script = new String(Base64.decodeBase64(script));

		// Load game script
		pi.exec(script);
	}

	protected Set<String> scanForActors(String gameFolder) {
		Set<String> actors = new LinkedHashSet<String>();

		File f = new File(gameFolder + "/vn/actors");

		for (File node : f.listFiles()) {
			String file = node.getName();
			if (node.isFile()) {
				String filename = file.substring(0, file.lastIndexOf('.'));
				if (filename != null && !filename.isEmpty()) {
					String actorName = filename.split("_")[0];
					actors.add(actorName);
				}
			}
		}

		return actors;
	}

	public void inject(PyStringMap functionTable) {
		this.namespace = functionTable;
	}

	public void addCommand(String command, PyTuple args) {
		addCommand(new Command(command, args));
	}

	public void addCommand(String command, List<String> args) {
		addCommand(new Command(command, args));
	}

	public void addCommand(String command, String name, String args) {
		ArrayList<String> actual_args = new ArrayList<String>();
		actual_args.add(name);
		actual_args.add(args);
		addCommand(new Command(command, actual_args));
	}

	public void addCommand(Command c) {
		commands.add(c);
	}

	public List<Command> flushCommands() {
		List<Command> cache = new LinkedList<>(commands);
		commands.clear();
		return cache;
	}

	public void choose(String next) throws Exception {
		if (gameState == null) {
			throw new Exception("ScriptEngine not correctly initialized");
		}

		// Check if scene change is valid
		if (!gameState.getValidTransitions().contains(next)) {
			return;
		}
		gameState.setNextScene(next);
		gameState.getValidTransitions().clear();
	}

	public boolean currentScene() throws Exception {
		PyString s = new PyString(gameState.getCurrentScene());
		if (namespace.has_key(s)) {
			PyFunction f = (PyFunction) namespace.get(s);
			gameState.setNextScene(((PyString) f.__call__()).toString());
			((PyString) f.__call__()).toString();
		} else {
			return false;
		}

		switch (gameState.getNextScene()) {
		case "_wait":
			addCommand("ui.choice.request", new PyTuple());
			break;
		}

		return true;
	}

	public boolean nextScene() throws Exception {
		if (gameState == null) {
			throw new Exception("ScriptEngine not correctly initialized");
		}

		return nextScene(gameState.getNextScene());
	}

	public boolean nextScene(String next) {
		System.out.println("NEXT: " + next);

		gameState.setCurrentScene(next);
		PyString s = new PyString(next);
		if (namespace.has_key(s)) {
			PyFunction f = (PyFunction) namespace.get(s);
			gameState.setNextScene(((PyString) f.__call__()).toString());
		} else {
			return false;
		}

		switch (gameState.getNextScene()) {
		case "_wait":
			addCommand("ui.choice.request", new PyTuple());
			break;
		case "_end":
			addCommand("ui.core.shutdown", new PyTuple());
			addCommand("sfx.core.shutdown", new PyTuple());
			addCommand("bgm.core.shutdown", new PyTuple());
			addCommand("se.core.shutdown", new PyTuple());
			break;
		}

		return true;
	}

	public void addValidTransition(String next) {
		gameState.getValidTransitions().add(next);
	}

	public PyStringMap getNamespace() {
		return namespace;
	}
}