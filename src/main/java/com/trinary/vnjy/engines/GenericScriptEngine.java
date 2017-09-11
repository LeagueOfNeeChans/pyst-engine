package com.trinary.vnjy.engines;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trinary.vnjy.injects.Actor;
import com.trinary.vnjy.injects.Narrator;
import com.trinary.vnjy.injects.Player;
import com.trinary.vnjy.injects.PystEngine;
import com.trinary.vnjy.injects.Scene;
import com.trinary.vnjy.objects.Command;
import com.trinary.vnjy.objects.GameState;

public abstract class GenericScriptEngine {
	private final List<Command> commands;
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final TypeReference<Map<String, Object>> t = new TypeReference<Map<String, Object>>() {};
	protected GameState gameState;
	
	protected abstract void injectElement(String key, Object object);
	protected abstract void loadScript(Map<String, Object> shitHeap);
	
	protected abstract String executeScene(String sceneName);
	
	protected GenericScriptEngine() {
		this.commands = new LinkedList<>();
	}
	
	@SuppressWarnings("unchecked")
	protected void init(GameState gameState) throws Exception {
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
			gameState.setPlayer(new Player(playerName, (GenericScriptEngine)this));
		}
		
		// Read in submaps (TODO open into object)
		Map<String, Object> assets = (Map<String, Object>)shitHeap.get("assets");
		Map<String, Object> actors = (Map<String, Object>)assets.get("actors");

		// Load actors if uninitialized
		if (gameState.getActors() == null || gameState.getActors().isEmpty()) {
			for (String actorName : actors.keySet()) {
				System.out.println("ACTOR " + actorName + " FOUND!");
				gameState.getActors().add(new Actor(actorName, (GenericScriptEngine)this));
			}
		}
		
		injectElement("scriptEngine", this);
		injectElement("engine", new PystEngine(this));
		
		injectElement("scene", new Scene(this));
		injectElement("narrator", new Narrator(this));
		injectElement("player", gameState.getPlayer());
		for (Actor actor : gameState.getActors()) {
			actor.setSe(this);
			injectElement(actor.getName(), actor);
		}
		
		loadScript(shitHeap);
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

	public boolean nextScene() throws Exception {
		if (gameState == null) {
			throw new Exception("ScriptEngine not correctly initialized");
		}

		return nextScene(gameState.getNextScene());
	}
	
	public boolean nextScene(String next) {
		System.out.println("NEXT: " + next);
		
		if (next.equals("_end")) {
			return false;
		}
		
		String nextScene = executeScene(next);
		if (nextScene.equals(null)) {
			return false;
		}
		
		gameState.setCurrentScene(next);
		gameState.setNextScene(nextScene);

		switch (gameState.getNextScene()) {
		case "_wait":
			addCommand("ui.choice.request", Arrays.asList());
			break;
		case "_end":
			addCommand("ui.core.shutdown", Arrays.asList());
			addCommand("sfx.core.shutdown", Arrays.asList());
			addCommand("bgm.core.shutdown", Arrays.asList());
			addCommand("se.core.shutdown", Arrays.asList());
			break;
		}

		return true;
	}
	
	public boolean currentScene() throws Exception {		
		String nextScene = executeScene(gameState.getCurrentScene());
		if (nextScene.equals(null)) {
			return false;
		}
		gameState.setNextScene(nextScene);

		switch (gameState.getNextScene()) {
		case "_wait":
			addCommand("ui.choice.request", Arrays.asList());
			break;
		}

		return true;
	}

	public void addValidTransition(String next) {
		gameState.getValidTransitions().add(next);
	}
}