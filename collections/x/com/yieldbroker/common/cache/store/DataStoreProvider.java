package com.yieldbroker.common.cache.store;

import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableSet;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.yieldbroker.common.cache.Data;
import com.yieldbroker.common.cache.DataCacheListener;
import com.yieldbroker.common.cache.DataSet;
import com.yieldbroker.common.cache.query.Query;
import com.yieldbroker.common.time.SystemClock;
import com.yieldbroker.common.time.Time;

public class DataStoreProvider implements DataProvider {

	private final Set<DataStore> dataStores;

	public DataStoreProvider(DataStore dataStore) {
		this(singleton(dataStore));
	}
	
	public DataStoreProvider(Set<DataStore> dataStores) {
		this.dataStores = unmodifiableSet(dataStores);
	}

	@Override
	public DataSet query(Query query) {
		Time currentTime = SystemClock.currentTime();
		Set<Data> dataBatch = queryBatch(query);

		return new DataSet(dataBatch, currentTime);
	}

	@Override
	public DataSet query(Query query, long timeout) {
		Time currentTime = SystemClock.currentTime();
		Set<Data> dataBatch = queryBatch(query);
		
		if(!dataBatch.isEmpty()) {
			return new DataSet(dataBatch, currentTime);
		}
		return queryBlocking(query, timeout);
	}
	
	private Set<Data> queryBatch(Query query) {
		Set<Data> dataBatch = new HashSet<Data>();

		for (DataStore dataStore : dataStores) {
			Set<Data> dataSet = dataStore.query(query);
			
			dataBatch.addAll(dataSet);
		}
		return dataBatch;
	}
	
	private DataSet queryBlocking(Query query, long timeout) {
		BlockingQuery blockingQuery = new BlockingQuery(query);

		try {
			for (DataStore dataStore : dataStores) {
				dataStore.registerListener(blockingQuery);
			}
			return blockingQuery.execute(timeout);
		} finally {
			for (DataStore dataStore : dataStores) {
				dataStore.removeListener(blockingQuery);
			}
		}
	}

	private class BlockingQuery implements DataCacheListener {

		private final BlockingQueue<Data> dataQueue;
		private final Query query;

		public BlockingQuery(Query query) {
			this.dataQueue = new LinkedBlockingQueue<Data>();
			this.query = query;
		}

		public void onUpdate(Data data, Time updateTime) {			
			if (query.matches(data,  updateTime)) {
				dataQueue.offer(data);
			}
		}

		public DataSet execute(long timeout) {
			try {
				dataQueue.poll(timeout, TimeUnit.MILLISECONDS);
			} catch (Exception e) {
				return query(query);
			}
			return query(query);
		}
	}

}
