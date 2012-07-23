package com.yieldbroker.common.socket;

import java.io.IOException;

/**
 * An connector is acts much like a {@link java.net.Socket} in 
 * that connects to some source of data. A {@link Connection} can be
 * used to exchange information with the other party when connected.
 * 
 * @author Niall Gallagher
 */
public interface Connector {
	Connection connect() throws IOException;
}
