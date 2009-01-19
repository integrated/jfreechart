package org.jfree.chart.editor.components;

import org.jfree.ui.RectangleEdge;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 19-Jan-2009
 * Time: 14:06:36
 * JComboBox to let users select the RectangleEdge object for a position.
 */
public class PositionComboBox extends AbstractListComboBox {
    private static RectangleEdge[] EDGES = new RectangleEdge[] {
            RectangleEdge.TOP, RectangleEdge.RIGHT, RectangleEdge.BOTTOM,  RectangleEdge.LEFT
    };

    private static String[] LABELS = new String[] {
            localizationResources.getString("North"),
            localizationResources.getString("East"),
            localizationResources.getString("South"),
            localizationResources.getString("West")
    };

    protected Object[] getObjects() {
        return EDGES;
    }

    protected String[] getLabels() {
        return LABELS;
    }
}
