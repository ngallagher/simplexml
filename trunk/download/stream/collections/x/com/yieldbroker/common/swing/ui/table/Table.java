package com.yieldbroker.common.swing.ui.table;

import java.awt.Dimension;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

import com.yieldbroker.common.swing.ui.Context;
import com.yieldbroker.common.swing.ui.Controller;
import com.yieldbroker.common.swing.ui.Widget;

public class Table extends Widget<JTable> {

	@ElementListUnion({ 
		@ElementList(entry="date", type=DateBuilder.class, inline=true), 
		@ElementList(entry="decimal", type=DecimalBuilder.class, inline=true),
		@ElementList(entry="text", type=TextBuilder.class, inline=true)
	})
	private List<ColumnBuilder> builders;

	@Transient
	private List<Column> columns;

	@Transient
	private Vector<String> titles;
	
	@Attribute
	private long refreshFrequency;

	@Element
	private Class type;

	public Table() {
		this.columns = new LinkedList<Column>();
		this.titles = new Vector<String>();
	}

	@Commit
	public void commit() throws Exception {
		for (ColumnBuilder builder : builders) {
			Column column = builder.build(type);
			String title = column.getTitle();

			columns.add(column);
			titles.add(title);
		}
	}

	@Override
	public JTable build(Controller controller, Context context, Dimension size) {
		TableModel tableModel = new TableModel(columns, titles);
		TableManager tableManager = new TableManager(tableModel, refreshFrequency);
		TableRowSorter tableSorter = new TableRowSorter(tableModel);
		TablePanel tablePanel = new TablePanel(tableModel);

		if (id != null) {
			context.add(id, tablePanel);
		}
		tablePanel.setRowSorter(tableSorter);
		tablePanel.setPreferredScrollableViewportSize(size);
		tablePanel.setFillsViewportHeight(true);
		tableManager.start();
		return tablePanel;
	}

	@Root
	private static abstract class ColumnBuilder {

		@Attribute
		protected String property;
		
		@Attribute(required=false)
		protected boolean ignoreNull;
		
		@Attribute(required=false)
		protected String title;
		
		@Attribute(required=false)
		protected String flash;
		
		@Commit
		public void commit() {
			if(title == null) {
				title = property;
			}
		}
		
		public Column build(Class type) throws Exception {
			Gradient gradient = Gradient.NONE;
			
			if(flash != null) {
				gradient = Gradient.resolve(flash);
			}
			return build(type, gradient);			
		}
		
		public abstract Column build(Class type, Gradient gradient) throws Exception;
	}

	private static class TextBuilder extends ColumnBuilder {

		@Override
		public Column build(Class type, Gradient gradient) throws Exception {
			return new Column(type, title, property, ignoreNull, gradient);
		}
	}

	private static class DateBuilder extends ColumnBuilder {

		@Transient
		private DateFormat dateFormat;

		@Attribute
		private String format;

		public DateBuilder(@Attribute(name = "format") String format) {
			this.dateFormat = new SimpleDateFormat(format);
		}

		@Override
		public Column build(Class type, Gradient gradient) throws Exception {
			return new Column(type, title, property, ignoreNull, gradient, dateFormat);
		}
	}

	private static class DecimalBuilder extends ColumnBuilder {

		@Transient
		private DecimalFormat decimalFormat;

		@Attribute
		private String format;

		public DecimalBuilder(@Attribute(name = "format") String format) {
			this.decimalFormat = new DecimalFormat(format);
		}

		@Override
		public Column build(Class type, Gradient gradient) throws Exception {
			return new Column(type, title, property, ignoreNull, gradient, decimalFormat);
		}
	}
}
