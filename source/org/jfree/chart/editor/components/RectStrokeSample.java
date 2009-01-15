package org.jfree.chart.editor.components;

import org.jfree.ui.StrokeSample;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
* User: Dan
* Date: 15-Jan-2009
* Time: 14:23:55
* Extends StrokeSample to draw the stroke in a rectangle.
*/
public class RectStrokeSample extends StrokeSample {
    private Dimension preferredSize;
    public RectStrokeSample(Stroke stroke) {
        super(stroke);
        this.preferredSize = new Dimension(100,80);
    }

    /**
     * Returns the preferred size of the component.
     *
     * @return the preferred size of the component.
     */
    public Dimension getPreferredSize() {
        return this.preferredSize;
    }

    /**
     * Draws a rectangle using the sample stroke.
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
        final Point2D one =  new Point2D.Double(xx + 6, yy + 6);

        // draw a rectangle
        final Rectangle2D rect = new Rectangle2D.Double(one.getX(), one.getY(), ww-12, hh-12);
        Stroke stroke = getStroke();
        if (stroke != null) {
            g2.setStroke(stroke);
        }
        else {
            g2.setStroke(new BasicStroke(0.0f));
        }
        g2.draw(rect);

    }
}
