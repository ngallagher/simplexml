package com.yieldbroker.common.swing.ui;

import java.awt.Component;

import javax.swing.JFrame;

public class Window {

	private final Context context;
	private final JFrame frame;

	public Window(JFrame frame, Context context) {
		this.context = context;
		this.frame = frame;
	}

	public <T extends Component> T get(String id) {
		return (T) context.get(id);
	}

	public void hide() {
		frame.setVisible(false);
	}

	public void show() {
		frame.setVisible(true);
	}
}
