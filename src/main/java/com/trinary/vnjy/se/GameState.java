package com.trinary.vnjy.se;

import java.util.HashSet;
import java.util.Set;

import com.trinary.vnjy.pystengine.Actor;
import com.trinary.vnjy.pystengine.Player;

public class GameState {
	private String currentScene;
	private String nextScene;
	private Set<String> validTransitions;
	private String game;
	private Player player;
	private Set<Actor> actors;
	private boolean isInitialized;

	public static GameState create(String game) {
		GameState state = new GameState();
		state.setGame(game);

		return state;
	}

	private GameState() {
		currentScene = "start";
		validTransitions = new HashSet<>();
		player = null;
		actors = new HashSet<>();
	}

	public String getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(String currentScene) {
		this.currentScene = currentScene;
	}

	public String getNextScene() {
		return nextScene;
	}

	public void setNextScene(String nextScene) {
		this.nextScene = nextScene;
	}

	public Set<String> getValidTransitions() {
		return validTransitions;
	}

	public void setValidTransitions(Set<String> validTransitions) {
		this.validTransitions = validTransitions;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Set<Actor> getActors() {
		return actors;
	}

	public void setActors(Set<Actor> actors) {
		this.actors = actors;
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}
}