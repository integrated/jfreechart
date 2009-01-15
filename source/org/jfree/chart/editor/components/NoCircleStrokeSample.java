package org.jfree.chart.editor.components;

import org.jfree.ui.StrokeSample;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;

/**
 * Created by IntelliJ IDEA.
* User: Dan
* Date: 15-Jan-2009
* Time: 14:24:44
* Extends StrokeSample to remove the big circles at the ends of each line.
*/
public class NoCircleStrokeSample extends StrokeSample {
    public NoCircleStrokeSample(Stroke stroke) {
        super(stroke);
    }

    /**
     * Draws a line using the sample stroke.
     *
     * @param g  the graphics device.
     */
    public void paintComponent(final Graphics g) {

        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        final Dimension size = getSize();
        final Insets insets = getInsets();
        final double xx = insets.left;
        final double yy = insets.top;
        final double ww = size.getWidth() - insets.left - insets.right;
        final double hh = size.getHeight() - insets.top - insets.bottom;

        // calculate point one
        final Point2D one =  new Point2D.Double(xx + 6, yy + hh / 2);
        // calculate point two
        final Point2D two =  new Point2D.Double(xx + ww - 6, yy + hh / 2);

        // draw a line connecting the points
        final Line2D line = new Line2D.Double(one, two);
        Stroke stroke = getStroke();
        if (stroke != null) {
            g2.setStroke(stroke);
        }
        else {
            g2.setStroke(new BasicStroke(0.0f));
        }
        g2.draw(line);

    }
}
