package com.yieldbroker.common.manage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This is used to represent an object instance within the virtual machine.
 * Convenience methods are provided to extract the fields as well as other
 * information used to identify the object.
 * 
 * @author Niall Gallagher
 */
public interface ObjectInfo {

	public static enum ObjectType {
		OBJECT, MAP, COLLECTION, ARRAY, PRIMITIVE, NULL;
	}

	public List<String> getClassHierarchy();
	public ObjectPath getObjectPath();
	public ObjectInfo getObjectInfo(ObjectId objectId);
	public Map<String, ObjectFieldInfo> getFields(String type);
	public Object getObjectValue();
	public ObjectInfo[] getObjectArray();
	public Collection<ObjectInfo> getObjectCollection();
	public Map<ObjectInfo, ObjectInfo> getObjectMap();
	public ObjectType getObjectType();
	public String getClassName();
	public String getUniqueId();
}
