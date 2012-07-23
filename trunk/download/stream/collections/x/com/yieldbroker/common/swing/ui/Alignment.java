package com.yieldbroker.common.swing.ui;

import static java.awt.Component.BOTTOM_ALIGNMENT;
import static java.awt.Component.CENTER_ALIGNMENT;
import static java.awt.Component.LEFT_ALIGNMENT;
import static java.awt.Component.RIGHT_ALIGNMENT;
import static java.awt.Component.TOP_ALIGNMENT;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

public enum Alignment {
	
	TOP(CENTER_ALIGNMENT, TOP_ALIGNMENT, "north", "top"), 
	TOPLEFT(LEFT_ALIGNMENT, TOP_ALIGNMENT, "northwest", "topleft"), 
	TOPRIGHT(RIGHT_ALIGNMENT, TOP_ALIGNMENT, "northeast", "topright"), 
	MIDDLE(CENTER_ALIGNMENT, CENTER_ALIGNMENT, "center", "middle"), 
	RIGHT(RIGHT_ALIGNMENT, CENTER_ALIGNMENT,	"east", "right"), 
	LEFT(LEFT_ALIGNMENT, CENTER_ALIGNMENT, "west", "left"), 
	BOTTOM(CENTER_ALIGNMENT, BOTTOM_ALIGNMENT, "south", "bottom"), 
	BOTTOMLEFT(LEFT_ALIGNMENT, BOTTOM_ALIGNMENT, "southwest", "bottomleft"), 
	BOTTOMRIGHT(RIGHT_ALIGNMENT, BOTTOM_ALIGNMENT,"southeast", "bottomright");

	public final List<String> terms;
	public final float x;
	public final float y;

	private Alignment(float x, float y, String... terms) {
		this(x, y, asList(terms));
	}
	
	private Alignment(float x, float y, List<String> terms) {
		this.terms = unmodifiableList(terms);
		this.x = x;
		this.y = y;
	}

	public static Alignment resolve(String name) {
		if (name != null) {
			String term = name.toLowerCase();

			for (Alignment align : values()) {
				if (align.terms.contains(term)) {
					return align;
				}
			}
		}
		return MIDDLE;
	}
}