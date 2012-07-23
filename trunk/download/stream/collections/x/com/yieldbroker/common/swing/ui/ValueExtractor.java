package com.yieldbroker.common.swing.ui;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.text.JTextComponent;


public class ValueExtractor implements Extractor {

	private final ObjectConverter converter;
	private final String id;

	public ValueExtractor(String id, Class type) {
		this.converter = new ObjectConverter(type);
		this.id = id;
	}

	public Object extract(Context context) {
		Component component = context.get(id);
		Transform type = resolve(component, id);
		String value = type.transform(component);

		return converter.convert(value);
	}

	private Transform resolve(Component value, String id) {
		if (value instanceof JTextComponent) {
			return new TextTransform();
		}
		if (value instanceof JComboBox) {
			return new ComboBoxTransform();
		}
		if (value instanceof JTabbedPane) {
			return new TabPaneTransform();
		}
		if (value instanceof JLabel) {
			return new LabelTransform();
		}
		if (value instanceof JCheckBox) {
			return new CheckBoxTransform();
		}
		throw new WidgetException("Can not transform %s for %s", value, id);
	}

	private static interface Transform<T extends Component> {

		String transform(T value);
	}

	private static class TextTransform implements Transform<JTextComponent> {

		public String transform(JTextComponent value) {
			return value.getText();
		}
	}

	private static class CheckBoxTransform implements Transform<JCheckBox> {

		public String transform(JCheckBox value) {
			return String.valueOf(value.isSelected());
		}
	}

	private static class LabelTransform implements Transform<JLabel> {

		public String transform(JLabel value) {
			return value.getText();
		}
	}

	private static class ComboBoxTransform implements Transform<JComboBox> {

		public String transform(JComboBox value) {
			Object text = value.getSelectedItem();

			if (text != null) {
				return String.valueOf(text);
			}
			return null;
		}
	}

	private static class TabPaneTransform implements Transform<JTabbedPane> {

		public String transform(JTabbedPane value) {
			int index = value.getSelectedIndex();

			if (index >= 0) {
				return value.getTitleAt(index);
			}
			return null;
		}
	}
}
