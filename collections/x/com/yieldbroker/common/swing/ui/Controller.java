package com.yieldbroker.common.swing.ui;

/**
 * A controller is used to resolve an object that will be used to
 * handle various events. Events such as {@link PanelCreate} and
 * {@link ButtonClick} can then be dispatched to the resolved object.
 * 
 * @author Niall Gallagher
 */
public interface Controller {
	Object resolve(Context context);
}
