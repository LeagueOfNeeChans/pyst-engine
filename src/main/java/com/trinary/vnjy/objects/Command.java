/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinary.vnjy.objects;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mmain
 */
public class Command {
	private String command = "";
	private List<String> args = new ArrayList<>();

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<String> getArgs() {
		return args;
	}

	public void setArgs(ArrayList<String> args) {
		this.args = args;
	}

	public String getArg(Integer index) {
		if (index >= args.size()) {
			return "";
		}

		return args.get(index);
	}

	public String getNamespace() {
		return command.split("\\.")[1];
	}

	public String getFunction() {
		return command.split("\\.")[2];
	}

	public Command() {}

	public Command(String command, List<String> args) {
		this.command = command;
		this.args = args;
	}

	@Override
	public String toString() {
		return command + ":" + args;
	}
}
