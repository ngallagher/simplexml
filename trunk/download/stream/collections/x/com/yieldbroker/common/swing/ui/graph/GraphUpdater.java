package com.yieldbroker.common.swing.ui.graph;

/**
 * Each time a new point needs to be added to the graph an object
 * representing the point needs to be passed to this graph updater.
 * Using the {@linke ComponentOf} annotation will resolve this
 * as a parameter value.
 * 
 * @author Niall Gallagher  
 */
public interface GraphUpdater<T> {
	void update(T value);
}
