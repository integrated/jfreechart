package org.jfree.chart.editor.themes;

import org.jfree.chart.axis.Axis;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 29-Apr-2009
 * Time: 12:24:02
 * Information that a ChartEditor GUI can change about an axis within a chart.
 */
public interface AxisTheme extends ChartThemeBasis {
    int RANGE_AXIS = 11;
    int DOMAIN_AXIS = 22;

    int getType();

    double getCatLabelAngleDegs();

    void setCatLabelAngleDegs(double catLabelAngleDegs);

    int getCatLabelMaxLines();

    void setCatLabelMaxLines(int catLabelMaxLines);

    double getLabelAngleDegs();

    void setLabelAngleDegs(double labelAngleDegs);

    Font getAxisLabelFont();

    void setAxisLabelFont(Font axisLabelFont);

    Font getTickLabelFont();

    void setTickLabelFont(Font tickLabelFont);

    Paint getAxisLabelPaint();

    void setAxisLabelPaint(Paint axisLabelPaint);

    Paint getTickLabelPaint();

    void setTickLabelPaint(Paint tickLabelPaint);

    String getAxisLabel();

    void setAxisLabel(String axisLabel);

    boolean isShowTickLabels();

    void setShowTickLabels(boolean showTickLabels);

    boolean isShowTickMarks();

    void setShowTickMarks(boolean showTickMarks);

    boolean isAutoRange();

    void setAutoRange(boolean autoRange);

    double getMinRange();

    void setMinRange(double minRange);

    double getMaxRange();

    void setMaxRange(double maxRange);

    boolean isLineVisible();

    void setLineVisible(boolean lineVisible);

    Paint getLinePaint();

    void setLinePaint(Paint linePaint);

    BasicStroke getLineStroke();

    void setLineStroke(BasicStroke lineStroke);

    double getLowerMargin();

    void setLowerMargin(double d);

    double getUpperMargin();

    void setUpperMargin(double d);

    double getCategoryMargin();

    void setCategoryMargin(double d);

    double getItemMargin();

    void setItemMargin(double d);

    void apply(Axis axis);
}
