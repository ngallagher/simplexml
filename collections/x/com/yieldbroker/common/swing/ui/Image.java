package com.yieldbroker.common.swing.ui;

import static javax.swing.SwingConstants.LEFT;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

public class Image extends Widget<JLabel> {

	@Attribute
	private File location;

	@Transient
	private BufferedImage image;

	@Commit
	public void commit() {
		try {
			image = ImageIO.read(location);
		} catch (Exception e) {
			throw new WidgetException(e);
		}
	}

	@Override
	public JLabel build(Controller controller, Context context, Dimension size) {
		ImageIcon iconImage = new ImageIcon(image);
		JLabel label = new JLabel(iconImage, LEFT);

		if (id != null) {
			context.add(id, label);
		}
		label.setPreferredSize(size);
		return label;
	}

}
