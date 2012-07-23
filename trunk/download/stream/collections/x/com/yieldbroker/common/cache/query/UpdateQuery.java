package com.yieldbroker.common.cache.query;

import com.yieldbroker.common.cache.Data;
import com.yieldbroker.common.time.Time;


public class UpdateQuery implements Query {

	private final Time referenceTime;

	public UpdateQuery(Time referenceTime) {
		this.referenceTime = referenceTime;
	}	
	
	@Override
	public boolean matches(Data data, Time updateTime) {	
		if(referenceTime != null) {
			return updateTime.after(referenceTime) || updateTime.sameTime(referenceTime);
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UPDATE( ");
		builder.append(referenceTime);
		builder.append(" )");
		return builder.toString();
	}
}
