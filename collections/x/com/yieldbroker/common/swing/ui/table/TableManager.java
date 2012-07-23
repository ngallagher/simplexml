package com.yieldbroker.common.swing.ui.table;

public class TableManager extends Thread {

	private final TableUpdater tableUpdater;
	private final long refresh;

	public TableManager(TableUpdater tableUpdater, long refresh) {
		this.tableUpdater = tableUpdater;
		this.refresh = refresh;
	}

	public void run() {
		while (true) {
			try {
				tableUpdater.refresh();
				Thread.sleep(refresh);
			} catch (Exception e) {
				e.printStackTrace();
				Thread.yield();
			}
		}
	}
}