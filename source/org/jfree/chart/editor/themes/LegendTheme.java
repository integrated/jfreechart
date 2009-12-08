package org.jfree.chart.editor.themes;

import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RectangleEdge;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 29-Apr-2009
 * Time: 15:03:56
 * Information that a ChartEditor GUI can change about the legend within a chart.
 */
public interface LegendTheme extends TitleTheme {
    Paint getLegendBackgroundPaint();

    void setLegendBackgroundPaint(Paint paint);

    Paint getLegendItemPaint();

    void setLegendItemPaint(Paint paint);

    boolean isVisible();

    void setVisible(boolean visible);

    Font getLegendItemFont();

    void setLegendItemFont(Font legendItemFont);

    RectangleInsets getItemPadding();

    void setItemPadding(RectangleInsets itemPadding);

    RectangleInsets getGraphicPadding();

    void setGraphicPadding(RectangleInsets graphicPadding);

    RectangleEdge getGraphicEdge();

    void setGraphicEdge(RectangleEdge graphicEdge);

    /**
     * Can be used for all chart types, denotes the format string to use when printing the item
     * labels in a legend. For PiePlot this will have more possible parameters, but most of the time
     * it will just be "{0}" to denote the series/category name.
     * @return A MessageFormat string for legend items.
     */
    String getItemFormatString();

    void setItemFormatString(String s);

    /**
     * Number format pattern used for numbers that occur in legend labels. Will mostly only be
     * used for PiePlots.
     * @return A number format string for legend item labels.
     */
    String getItemNumberFormatString();

    void setItemNumberFormatString(String s);

    /**
     * Number format pattern used for percentages that occur in legend labels. Will mostly only be
     * used for PiePlots.
     * @return A number format string for legend item labels, when percentages are required.
     */
    String getItemPercentFormatString();

    void setItemPercentFormatString(String s);
}
