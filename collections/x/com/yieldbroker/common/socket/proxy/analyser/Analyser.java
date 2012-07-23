package com.yieldbroker.common.socket.proxy.analyser;

import com.yieldbroker.common.time.Time;

/**
 * An analyser is used to collect information about a connection. When
 * data is collected performance measurements can be made, such as
 * the latency of the connection or the throughput.
 * 
 * @author Niall Gallagher
 * 
 * @see com.yieldbroker.common.socket.proxy.analyser.AnalyserProxy
 */
public interface Analyser {
	void analyse(Time startTime, Time endTime, int payloadSize);
}
