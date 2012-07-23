package com.yieldbroker.common.chart;

import java.util.Date;

public class ObjectConverter {

	public Double convert(Object value) {
		Class type = value.getClass();
		Transform transform = transform(type);
		
		return transform.transform(value);		
	}
	
	private Transform transform(Class type) {
		if(type == Double.class) {
			return new DoubleTransform();
		}
		if(type == String.class) {
			return new StringTransform();
		}
		if(type == Integer.class) {
			return new IntegerTransform();
		}
		if(type == Float.class) {
			return new FloatTransform();
		}
		if(type == Long.class) {
			return new LongTransform();
		}
		if(type == Date.class) {
			return new DateTransform();
		}
		if(type == int.class) {
			return new IntegerTransform();
		}
		if(type == float.class) {
			return new FloatTransform();
		}
		if(type == long.class) {
			return new LongTransform();
		}
		if(type == double.class) {
			return new DoubleTransform();
		}
		throw new IllegalArgumentException("Can not convert " + type);

	}
		

	public static interface Transform<T> {
		Double transform(T value);
	}
	
	private static class StringTransform implements Transform<String> {

		@Override
		public Double transform(String value) {
			return Double.valueOf(value);
		}
	}
	
	private static class FloatTransform implements Transform<Float> {

		@Override
		public Double transform(Float value) {
			return Double.valueOf(value);
		}
	}
	
	private static class LongTransform implements Transform<Long> {

		@Override
		public Double transform(Long value) {
			return Double.valueOf(value);
		}
	}
	
	private static class IntegerTransform implements Transform<Integer> {

		@Override
		public Double transform(Integer value) {
			return Double.valueOf(value);
		}
	}
	
	private static class DateTransform implements Transform<Date> {
		
		@Override
		public Double transform(Date value) {
			return Double.valueOf(value.getTime());
		}
	}
	
	private static class DoubleTransform implements Transform<Double> {
		
		@Override
		public Double transform(Double value) {
			return value;
		}
	}
}
