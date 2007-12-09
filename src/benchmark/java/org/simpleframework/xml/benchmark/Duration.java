package org.simpleframework.xml.benchmark;

public class Duration {
	
	private long operation;
	
	private long total;
	
	public Duration(long total, long operation) {
		this.operation = System.currentTimeMillis() - operation;
		this.total = System.currentTimeMillis() - total;
	}
	
    public long getTotal() {
    	return total;
    }
    
    public long getOperation() {
    	return operation;
    }
}
