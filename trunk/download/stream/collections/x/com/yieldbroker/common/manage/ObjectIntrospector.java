package com.yieldbroker.common.manage;

/**
 * This is used to acquire object information for a object referenced by the
 * specified @{link ObjectPath}. If no object is identified this returns null.
 * 
 * @author Niall Gallagher
 */
public interface ObjectIntrospector {
	public ObjectInfo getObjectInfo(ObjectPath objectPath);
}
