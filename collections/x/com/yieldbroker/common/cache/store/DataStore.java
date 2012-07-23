package com.yieldbroker.common.cache.store;

import static java.util.Collections.singleton;

import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.yieldbroker.common.cache.Data;
import com.yieldbroker.common.cache.DataCache;
import com.yieldbroker.common.cache.DataCacheListener;
import com.yieldbroker.common.cache.Mergable;
import com.yieldbroker.common.cache.query.Query;
import com.yieldbroker.common.cache.store.filter.DataFilter;
import com.yieldbroker.common.cache.store.filter.IdentityFilter;
import com.yieldbroker.common.time.SystemClock;
import com.yieldbroker.common.time.Time;

public class DataStore implements DataCache {

	private final Set<DataCacheListener> cacheListeners;
	private final Map<Object, Data> dataTable;
	private final Deque<DataUpdate> dataStack;
	private final Set<String> dataTypes;
	private final DataFilter dataFilter;
	private final int cacheCapacity;

	public DataStore(String dataType, int cacheCapacity) {
		this(singleton(dataType), cacheCapacity);
	}

	public DataStore(Set<String> dataTypes, int cacheCapacity) {
		this(new IdentityFilter(), dataTypes, cacheCapacity);
	}

	public DataStore(DataFilter dataFilter, String dataType, int cacheCapacity) {
		this(dataFilter, singleton(dataType), cacheCapacity);
	}

	public DataStore(DataFilter dataFilter, Set<String> dataTypes, int cacheCapacity) {
		this.cacheListeners = new LinkedHashSet<DataCacheListener>();
		this.dataTable = new LinkedHashMap<Object, Data>();
		this.dataStack = new LinkedList<DataUpdate>();
		this.cacheCapacity = cacheCapacity;
		this.dataFilter = dataFilter;
		this.dataTypes = dataTypes;
	}

	@Override
	public synchronized void registerListener(DataCacheListener cacheListener) {
		cacheListeners.add(cacheListener);
	}

	@Override
	public synchronized void removeListener(DataCacheListener cacheListener) {
		cacheListeners.remove(cacheListener);
	}

	@Override
	public synchronized void updateData(Set<Data> dataBatch) {
		for (Data data : dataBatch) {
			updateData(data);
		}
	}

	@Override
	public synchronized void updateData(Data data) {
		String dataType = data.getType();

		if (dataTypes.contains(dataType)) {	
			DataUpdate dataUpdate = createUpdate(data);
			Data update = dataUpdate.getData();
			Time timeStamp = dataUpdate.getTimeStamp();
			Object key = update.getKey();
			
			dataStack.remove(dataUpdate);
			dataStack.push(dataUpdate);
			dataTable.put(key, update);

			for (DataCacheListener cacheListener : cacheListeners) {
				Time updateTime = timeStamp.clone();

				cacheListener.onUpdate(update, updateTime);
			}
			int cacheSize = dataStack.size();

			if (cacheSize > cacheCapacity) {
				dataStack.pollLast();
				dataTable.remove(key);
			}
		}
	}	

	private synchronized DataUpdate createUpdate(Data data) {
		Time timeStamp = SystemClock.currentTime();
		Data mergedData = mergeData(data);	

		return new DataUpdate(mergedData, timeStamp);
	}

	private synchronized Data mergeData(Data data) {
		Object key = data.getKey();
		Data existing = dataTable.get(key);

		if (existing instanceof Mergable) {
			Mergable mergable = (Mergable) existing;
			Data merged = mergable.merge(data);

			return merged;
		}
		return data;
	}

	@Override
	public synchronized Set<Data> query(Query query) {
		Set<Data> changeBatch = new LinkedHashSet<Data>();

		if (!dataStack.isEmpty()) {
			for (DataUpdate dataUpdate : dataStack) {
				Data data = dataUpdate.getData();
				Time time = dataUpdate.getTimeStamp();

				if (query.matches(data, time)) {
					changeBatch.add(data);
				}
			}

		}
		if (!changeBatch.isEmpty()) {
			return dataFilter.process(changeBatch);
		}
		return changeBatch;
	}

	private static class DataUpdate {

		private final Time timeStamp;
		private final Data data;
		private final Object key;

		public DataUpdate(Data data, Time timeStamp) {
			this.key = data.getKey();
			this.timeStamp = timeStamp;
			this.data = data;
		}

		public Data getData() {
			return data;
		}

		public Time getTimeStamp() {
			return timeStamp;
		}

		public boolean equals(Object other) {
			if (other instanceof DataUpdate) {
				return equals((DataUpdate) other);
			}
			return false;
		}

		public boolean equals(DataUpdate other) {
			return other.key.equals(key);
		}

		public int hashCode() {
			return key.hashCode();
		}
	}
}
