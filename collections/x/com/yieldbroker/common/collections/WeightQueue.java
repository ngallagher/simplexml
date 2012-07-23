package com.yieldbroker.common.collections;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public abstract class WeightQueue<T extends Weight> implements Iterable<T> {

	private final PriorityQueue<T> queue;
	
	protected WeightQueue(Comparator<T> comparator) {
		this.queue = new PriorityQueue<T>(100, comparator);
	}	
	
	public Iterator<T> iterator() {
		return queue.iterator();
	}
	
	public void offer(T item) {
		queue.offer(item);
	}
	
	public T peek() {
		return queue.peek();
	}
	
	public T poll() {
		return queue.poll();
	}
	
	public int size() {
		return queue.size();
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
