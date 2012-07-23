package com.yieldbroker.common.swing.ui;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.RowSorter;
import javax.swing.table.TableModel;

import com.yieldbroker.common.swing.ui.graph.GraphPanel;
import com.yieldbroker.common.swing.ui.graph.GraphUpdater;
import com.yieldbroker.common.swing.ui.table.TablePanel;
import com.yieldbroker.common.swing.ui.table.TableUpdater;

public class ComponentExtractor implements Extractor {

	private final Class type;
	private final String id;

	public ComponentExtractor(String id, Class type) {
		this.type = type;
		this.id = id;
	}

	public Object extract(Context context) {
		Component component = context.get(id);
		Transform type = resolve(component, id);

		return type.transform(component);
	}

	private Transform resolve(Component value, String id) {
		if (value instanceof TablePanel) {
			return new TableTransform(type);
		}
		if(value instanceof GraphPanel) {
			return new GraphTransform(type);
		}
		return new ComponentTransform();
	}

	private static interface Transform<T> {

		Object transform(T value);
	}

	private static class ComponentTransform implements Transform<JComponent> {

		public Object transform(JComponent value) {
			return value;
		}
	}

	private static class TableTransform implements Transform<TablePanel> {

		private final Class type;

		public TableTransform(Class type) {
			this.type = type;
		}

		public Object transform(TablePanel value) {
			if (TableModel.class.isAssignableFrom(type)) {
				return value.getModel();
			}
			if (RowSorter.class.isAssignableFrom(type)) {
				return value.getRowSorter();
			}
			if (TableUpdater.class.isAssignableFrom(type)) {
				return value.getUpdater();
			}
			return value;
		}
	}
	

	private static class GraphTransform implements Transform<GraphPanel> {

		private final Class type;

		public GraphTransform(Class type) {
			this.type = type;
		}

		public Object transform(GraphPanel value) {
			if (GraphUpdater.class.isAssignableFrom(type)) {
				return value.getUpdater();
			}
			return value;
		}
	}
}