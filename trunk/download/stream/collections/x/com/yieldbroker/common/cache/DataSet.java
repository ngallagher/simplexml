package com.yieldbroker.common.cache;

import java.util.Set;

import com.yieldbroker.common.time.Time;

public class DataSet {

	private final Set<Data> dataSet;
	private final Time timeStamp;

	public DataSet(Set<Data> dataSet, Time timeStamp) {
		this.dataSet = dataSet;
		this.timeStamp = timeStamp;
	}

	public Time getTimeStamp() {
		return timeStamp;
	}

	public Set<Data> getData() {
		return dataSet;
	}
}
