package com.yieldbroker.common.swing.ui;

import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLineBorder;
import static javax.swing.BorderFactory.createTitledBorder;

import javax.swing.border.Border;

public enum Boundary {
	NONE("none") {
		public Border create(String title) {
			return createEmptyBorder(0, 0, 0, 0);
		}
	},
	THINLINE("thinline") {
		public Border create(String title) {
			return createLineBorder(BLACK);
		}
	},
	LINE("line") {
		public Border create(String title) {
			return createLineBorder(BLACK, 2);
		}
	},
	FATLINE("fatline") {
		public Border create(String title) {
			return createLineBorder(BLACK, 4);
		}
	},
	REDLINE("redline") {
		public Border create(String title) {
			return createLineBorder(RED);
		}
	},
	GREENLINE("greenline") {
		public Border create(String title) {
			return createLineBorder(GREEN);
		}
	},
	BLUELINE("blueline") {
		public Border create(String title) {
			return createLineBorder(BLUE);
		}
	},
	THINPAD("thinpad") {
		public Border create(String title) {
			return createEmptyBorder(2, 2, 2, 2);
		}
	},
	PAD("pad") {
		public Border create(String title) {
			return createEmptyBorder(4, 4, 4, 4);
		}
	},
	FATPAD("fatpad") {
		public Border create(String title) {
			return createEmptyBorder(8, 8, 8, 8);
		}
	},
	HUGEPAD("hugepad") {
		public Border create(String title) {
			return createEmptyBorder(20, 20, 20, 20);
		}
	},
	TITLE("title") {
		public Border create(String title) {
			Border border = createLineBorder(BLACK);

			if (title != null) {
				return createTitledBorder(border, title);
			}
			return border;
		}
	};

	public final String term;

	private Boundary(String term) {
		this.term = term;
	}

	public abstract Border create(String title);

	public static Boundary resolve(String name) {
		if (name != null) {
			String term = name.toLowerCase();

			for (Boundary border : values()) {
				if (border.term.equals(term)) {
					return border;
				}
			}
		}
		return TITLE;

	}
}
