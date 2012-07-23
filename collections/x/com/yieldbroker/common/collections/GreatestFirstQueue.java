package com.yieldbroker.common.collections;

import java.util.Comparator;

public class GreatestFirstQueue<T extends Weight> extends WeightQueue<T> {
	
	public GreatestFirstQueue() {
		super(new GreatestFirstComparator<T>());
	}	
	
	private static final class GreatestFirstComparator<T extends Weight> implements Comparator<T> {

		@Override
		public int compare(T left, T right) {
			Integer leftWeight = left.getWeight();
			Integer rightWeight = right.getWeight();
			
			return -leftWeight.compareTo(rightWeight);
		}
	}
}
