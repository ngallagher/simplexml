package org.simpleframework.xml.benchmark;

public class Duration {
	
	private long operation;
	
	private long total;
	
	private int iterations;
	
	public Duration(long total, long operation, int iterations) {
		this.operation = System.currentTimeMillis() - operation;
		this.total = System.currentTimeMillis() - total;
		this.iterations = iterations;
	}
	
	public long getAverage() {
	   return operation / iterations;
	}
	
    public long getTotal() {
    	return total;
    }
    
    public long getOperation() {
    	return operation;
    }
}
