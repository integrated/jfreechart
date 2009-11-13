package org.jfree.chart.editor.themes;

import org.jfree.chart.axis.CategoryLabelPositions;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 29-Apr-2009
 * Time: 12:25:55
 * To change this template use File | Settings | File Templates.
 */
public class ThemeUtil {
    public static CategoryLabelPositions getCategoryLabelPositions(double angleInDegs) {
        if (angleInDegs == 0) {
            return CategoryLabelPositions.STANDARD;
        } else if (angleInDegs >= 90) {
            return CategoryLabelPositions.UP_90;
        } else if (angleInDegs <= -90) {
            return CategoryLabelPositions.DOWN_90;
        } else if (angleInDegs > 0) {
            return CategoryLabelPositions.createUpRotationLabelPositions(
                    Math.toRadians(angleInDegs)
            );
        } else {
            return CategoryLabelPositions.createDownRotationLabelPositions(
                    Math.toRadians(Math.abs(angleInDegs))
            );
        }
    }

    /**
     * Creates an array of standard shapes to display for the items in series
     * on charts.
     *
     * @return The array of shapes.
     */
    public static Shape[] createStandardSeriesShapes() {

        Shape[] result = new Shape[10];

        double size = 6.0;
        double delta = size / 2.0;
        int[] xpoints;
        int[] ypoints;

        // square
        result[0] = new Rectangle2D.Double(-delta, -delta, size, size);
        // circle
        result[1] = new Ellipse2D.Double(-delta, -delta, size, size);

        // up-pointing triangle
        xpoints = intArray(0.0, delta, -delta);
        ypoints = intArray(-delta, delta, delta);
        result[2] = new Polygon(xpoints, ypoints, 3);

        // diamond
        xpoints = intArray(0.0, delta, 0.0, -delta);
        ypoints = intArray(-delta, 0.0, delta, 0.0);
        result[3] = new Polygon(xpoints, ypoints, 4);

        // horizontal rectangle
        result[4] = new Rectangle2D.Double(-delta, -delta / 2, size, size / 2);

        // down-pointing triangle
        xpoints = intArray(-delta, +delta, 0.0);
        ypoints = intArray(-delta, -delta, delta);
        result[5] = new Polygon(xpoints, ypoints, 3);

        // horizontal ellipse
        result[6] = new Ellipse2D.Double(-delta, -delta / 2, size, size / 2);

        // right-pointing triangle
        xpoints = intArray(-delta, delta, -delta);
        ypoints = intArray(-delta, 0.0, delta);
        result[7] = new Polygon(xpoints, ypoints, 3);

        // vertical rectangle
        result[8] = new Rectangle2D.Double(-delta / 2, -delta, size / 2, size);

        // left-pointing triangle
        xpoints = intArray(-delta, delta, delta);
        ypoints = intArray(0.0, -delta, +delta);
        result[9] = new Polygon(xpoints, ypoints, 3);

        return result;

    }

    /**
     * Helper method to avoid lots of explicit casts in getShape().  Returns
     * an array containing the provided doubles cast to ints.
     *
     * @param a  x
     * @param b  y
     * @param c  z
     *
     * @return int[3] with converted params.
     */
    static int[] intArray(double a, double b, double c) {
        return new int[] {(int) a, (int) b, (int) c};
    }

    /**
     * Helper method to avoid lots of explicit casts in getShape().  Returns
     * an array containing the provided doubles cast to ints.
     *
     * @param a  x
     * @param b  y
     * @param c  z
     * @param d  t
     *
     * @return int[4] with converted params.
     */
    static int[] intArray(double a, double b, double c, double d) {
        return new int[] {(int) a, (int) b, (int) c, (int) d};
    }
}
