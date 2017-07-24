package com.hb.ex01.a0621_chat;

import java.io.Serializable;

public class Protocol implements Serializable {
	public static final long serialVersionUID = 123L;
	String playerName, message;
	int cmd;
	public Protocol() {}
	public Protocol(String playerName, String message, int cmd) {
		super();
		this.playerName = playerName;
		this.message = message;
		this.cmd = cmd;
	}
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
}
