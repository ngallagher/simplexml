package com.yieldbroker.common.manage;

import java.util.List;

public interface ObjectPath {
	public String getObjectName();
	public List<ObjectId> getObjectPath();
	public ObjectPath getRelativePath(ObjectId objectId);
}
