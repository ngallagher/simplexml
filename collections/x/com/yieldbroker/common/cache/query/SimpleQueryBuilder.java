package com.yieldbroker.common.cache.query;

import java.util.Collection;
import java.util.LinkedList;

import com.yieldbroker.common.time.Time;

public class SimpleQueryBuilder implements QueryBuilder {

	private final Stack<Operation> operations;
	private final Stack<Query> queries;

	public SimpleQueryBuilder() {
		this.operations = new Stack<Operation>();
		this.queries = new Stack<Query>();
	}
	
	@Override
	public QueryBuilder clear() {
		operations.clear();
		queries.clear();
		return this;
	}
	
	@Override
	public QueryBuilder matches(Query query) {
		queries.push(query);
		return this;
	}

	@Override
	public QueryBuilder instanceOf(Class... types) {
		Query query = new InstanceOfQuery(types);
		queries.push(query);
		return this;
	}
	
	@Override
	public QueryBuilder instanceOf(Collection<Class> types) {
		Query query = new InstanceOfQuery(types);
		queries.push(query);
		return this;
	}

	@Override
	public QueryBuilder updateAfter(long timeInMillis) {
		Time updateTime = new Time(timeInMillis);
		return updateAfter(updateTime);
	}
	
	@Override
	public QueryBuilder updateAfter(Time time) {
		Query query = new UpdateQuery(time);
		queries.push(query);
		return this;
	}

	@Override
	public QueryBuilder updateBefore(long timeInMillis) {
		Time updateTime = new Time(timeInMillis);
		return updateBefore(updateTime);
	}	

	@Override
	public QueryBuilder updateBefore(Time time) {
		Query query = new UpdateQuery(time);
		Query not = new Not(query);
		queries.push(not);
		return this;
	}

	@Override
	public QueryBuilder typeOf(String... types) {
		Query query = new TypeQuery(types);
		queries.push(query);
		return this;
	}
	
	@Override
	public QueryBuilder typeOf(Collection<String> types) {
		Query query = new TypeQuery(types);
		queries.push(query);
		return this;
	}

	@Override
	public QueryBuilder categoryOf(String... categories) {
		Query query = new CategoryQuery(categories);
		queries.push(query);
		return this;
	}
		
	@Override
	public QueryBuilder categoryOf(Collection<String> categories) {
		Query query = new CategoryQuery(categories);
		queries.push(query);
		return this;
	}
	
	@Override
	public QueryBuilder categoryLike(String... patterns) {
		Query query = new CategoryLikeQuery(patterns);
		queries.push(query);
		return this;
	}
	
	@Override
	public QueryBuilder categoryLike(Collection<String> patterns) {
		Query query = new CategoryLikeQuery(patterns);
		queries.push(query);
		return this;
	}

	@Override
	public QueryBuilder and() {
		Operation op = new AndOperation();
		operations.push(op);
		return this;
	}

	@Override
	public QueryBuilder or() {
		Operation op = new OrOperation();
		operations.push(op);
		return this;
	}

	@Override
	public QueryBuilder not() {
		Operation op = new NotOperation();
		operations.push(op);
		return this;
	}

	@Override
	public QueryBuilder group() {
		return group(1);
	}

	@Override
	public QueryBuilder group(int count) {
		for (int i = 0; i < count; i++) {
			Operation op = operations.pop();
			Query query = op.create(operations, queries);
			queries.push(query);
		}
		return this;
	}

	@Override
	public Query build() {
		while (!operations.isEmpty()) {
			Operation op = operations.pop();
			Query query = op.create(operations, queries);
			queries.push(query);
		}
		Query query = queries.pop();
		
		if(!queries.isEmpty()) {
			throw new IllegalStateException("Query is not balanced correctly " + query);
		}
		return query;
	}

	private static interface Operation {

		public Query create(Stack<Operation> ops, Stack<Query> stack);
	}

	private static class AndOperation implements Operation {

		public Query create(Stack<Operation> ops, Stack<Query> stack) {
			Query first = stack.pop();
			Query last = stack.pop();

			return new And(first, last);
		}
	}

	private static class OrOperation implements Operation {

		public Query create(Stack<Operation> ops, Stack<Query> stack) {
			Query first = stack.pop();
			Query last = stack.pop();

			return new Or(first, last);
		}
	}

	private static class NotOperation implements Operation {

		public Query create(Stack<Operation> ops, Stack<Query> stack) {
			Query query = stack.pop();

			return new Not(query);
		}
	}

	private static class Stack<T> {

		private LinkedList<T> stack;

		public Stack() {
			this.stack = new LinkedList<T>();
		}
		
		public void clear() {
			stack.clear();
		}

		public boolean isEmpty() {
			return stack.isEmpty();
		}

		public void push(T value) {
			stack.addFirst(value);
		}

		public T pop() {
			return stack.removeFirst();
		}

		public String toString() {
			StringBuilder b = new StringBuilder();
			for (int i = 0; i < stack.size(); i++) {
				b.append(stack.get(i).getClass().getSimpleName());
				b.append("\n");
			}
			return b.toString();
		}
	}
}
