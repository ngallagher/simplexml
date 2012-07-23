package com.yieldbroker.common.chart;

public interface Series {
	int getLength();
	Object getValue(int index);
}
