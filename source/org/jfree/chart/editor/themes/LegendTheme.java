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
}
