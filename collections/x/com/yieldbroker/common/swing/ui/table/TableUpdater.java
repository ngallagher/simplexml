package com.yieldbroker.common.swing.ui.table;

/**
 * Each time a new row needs to be added to the table an object
 * representing the row needs to be passed to this table updater.
 * Using the {@linke ComponentOf} annotation will resolve this
 * as a parameter value.
 * 
 * @author Niall Gallagher  
 */
public interface TableUpdater<T> {
	void refresh();
	void update(T value);
}
