package com.yieldbroker.common.cache;

import java.util.Arrays;

public class Key {

	private Object[] list;

	public Key(Object... list) {
		this.list = list;
	}

	public boolean equals(Object value) {
		if (value instanceof Key) {
			return equals((Key) value);
		}
		return false;
	}

	public boolean equals(Key key) {
		if (key.list.length == list.length) {
			for (int i = 0; i < list.length; i++) {
				if (list[i] != null) {
					if (key.list[i] == null) {
						return false;
					}
					if (!list[i].equals(key.list[i])) {
						return false;
					}
				} else if (key.list[i] != null) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public int hashCode() {
		int hashCode = 1;

		for (Object value : list) {
			if (value != null) {
				int valueHash = value.hashCode();
				hashCode = 31 * hashCode + valueHash;
			}
		}
		return hashCode;
	}

	public String toString() {
		return Arrays.asList(list).toString();
	}
}
