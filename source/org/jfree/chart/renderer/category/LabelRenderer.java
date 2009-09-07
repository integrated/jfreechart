package org.jfree.chart.renderer.category;

import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.category.CategoryDataset;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 07-Sep-2009
 * Time: 09:35:05
 * Renderer that has a drawItem method that is able to just render the data labels. Used as a 2 pass approach
 * for when gridlines need to be drawn over the top of data - labels get redrawn afterwards so that gridlines
 * do not obscur them.
 */
public interface LabelRenderer {
    public void drawItem(Graphics2D g2,
                     CategoryItemRendererState state,
                     Rectangle2D dataArea,
                     CategoryPlot plot,
                     CategoryAxis domainAxis,
                     ValueAxis rangeAxis,
                     CategoryDataset dataset,
                     int row,
                     int column,
                     int pass,
                     boolean justLabel);
}
