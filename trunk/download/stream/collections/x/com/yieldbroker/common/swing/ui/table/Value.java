package com.yieldbroker.common.swing.ui.table;

import java.awt.Color;

public class Value {

	private boolean ignoreValue;
	private String text;
	private Flash flash;

	public Value(String text, Gradient gradient) {
		this(text, gradient, false);
	}
	
	public Value(String text, Gradient gradient, boolean ignoreValue) {
		this.flash = new Flash(gradient);
		this.ignoreValue = ignoreValue;
		this.text = text;
	}

	public Color color() {
		return flash.color();
	}

	public boolean isFlash() {
		return flash.isFlash();
	}	

	public boolean isIgnore() {
		return ignoreValue;
	}

	public void refresh() {
		flash.fade();
	}

	public void flash() {
		flash.flash();
	}

	public void set(String text) {
		this.text = text;
	}

	public String get() {
		return text;
	}
}