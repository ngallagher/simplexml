package com.yieldbroker.common.collections;

import java.util.Comparator;

public class LeastFirstQueue<T extends Weight> extends WeightQueue<T> {

	public LeastFirstQueue() {
		super(new LeastFirstComparator<T>());
	}	
	
	private static final class LeastFirstComparator<T extends Weight> implements Comparator<T> {

		@Override
		public int compare(T left, T right) {
			Integer leftWeight = left.getWeight();
			Integer rightWeight = right.getWeight();
			
			return leftWeight.compareTo(rightWeight);
		}
	}
}
