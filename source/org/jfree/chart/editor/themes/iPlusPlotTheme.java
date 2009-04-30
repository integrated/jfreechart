package org.jfree.chart.editor.themes;

import java.awt.*;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.Rotation;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 29-Apr-2009
 * Time: 14:22:17
 * To change this template use File | Settings | File Templates.
 */
public interface iPlusPlotTheme extends AbstractiPlusChartTheme, EditableBorder {

    static final BasicStroke DEFAULT_GRIDLINE_STROKE = new BasicStroke(0.5f,
        BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0.0f, new float[]
        {2.0f, 2.0f}, 0.0f);
    static final Font DEFAULT_REGULAR_FONT = new Font("Tahoma", Font.BOLD, 12);
    static final BasicStroke DEFAULT_BORDER_STROKE = new BasicStroke(0.5f);
    static final Paint DEFAULT_BORDER_PAINT = Color.BLACK;
    static final PlotOrientation DEFAULT_PLOT_ORIENTATION = PlotOrientation.VERTICAL;
    static final boolean DEFAULT_SHADOWS_VISIBLE = false;
    static final boolean DEFAULT_DOMAIN_GRID_VISIBLE = false;
    static final boolean DEFAULT_RANGE_GRID_VISIBLE = true;
    static final RectangleInsets DEFAULT_INSETS
                        = new RectangleInsets(4.0, 8.0, 4.0, 8.0);
    static final boolean DEFAULT_CIRCULAR_PLOT = true;
    static final Rotation DEFAULT_PIE_DIRECTION = Rotation.CLOCKWISE;
    static final double DEFAULT_START_ANGLE = 90;
    static final boolean DEFAULT_LABELS_VISIBLE = false;
    static final String DEFAULT_LABEL_FORMAT = "{0}";
    /** The default section label paint. */
    static final Paint DEFAULT_LABEL_PAINT = Color.black;
    static final Paint DEFAULT_LABEL_SHADOW_PAINT = Color.LIGHT_GRAY;
    static final PieLabelLinkStyle DEFAULT_LINK_STYLE = PieLabelLinkStyle.STANDARD;
    static final boolean DEFAULT_LINK_VISIBLE = true;
    /** The default section label background paint. */
    static final Paint DEFAULT_LABEL_BACKGROUND_PAINT = new Color(255,
            255, 192);
    /** The default section label outline paint. */
    static final Paint DEFAULT_LABEL_OUTLINE_PAINT = Color.black;
    /** The default section label outline stroke. */
    static final BasicStroke DEFAULT_LABEL_OUTLINE_STROKE = new BasicStroke(
            0.5f);
    static final boolean DEFAULT_LABEL_OUTLINE_VISIBLE = true;
    static final RectangleInsets DEFAULT_LABEL_PADDING = new RectangleInsets(2, 2, 2, 2);


    Paint getPlotBackgroundPaint();

    void setPlotBackgroundPaint(Paint plotBackgroundPaint);

    ChartBorder getPlotOutline();

    void setPlotOutline(ChartBorder plotOutline);

    PlotOrientation getOrientation();

    void setOrientation(PlotOrientation orientation);

    boolean isShadowsVisible();

    void setShadowsVisible(boolean shadowsVisible);

    RectangleInsets getAxisOffset();

    void setAxisOffset(RectangleInsets axisOffset);

    Paint getDomainGridlinePaint();

    void setDomainGridlinePaint(Paint domainGridlinePaint);

    Paint getRangeGridlinePaint();

    void setRangeGridlinePaint(Paint rangeGridlinePaint);

    BasicStroke getDomainGridlineStroke();

    void setDomainGridlineStroke(BasicStroke domainGridlineStroke);

    BasicStroke getRangeGridlineStroke();

    void setRangeGridlineStroke(BasicStroke rangeGridlineStroke);

    boolean isDomainGridlineVisible();

    void setDomainGridlineVisible(boolean domainGridlineVisible);

    boolean isRangeGridlinesVisible();

    void setRangeGridlinesVisible(boolean rangeGridlinesVisible);

    iPlusDrawingSupplier getDrawingSupplier();

    void setDrawingSupplier(iPlusDrawingSupplier supplier);

    iPlusAxisTheme getDomainAxisTheme();

    void setDomainAxisTheme(iPlusAxisTheme domainAxisTheme);

    iPlusAxisTheme getRangeAxisTheme();

    void setRangeAxisTheme(iPlusAxisTheme rangeAxisTheme);

    RectangleInsets getInsets();

    void setInsets(RectangleInsets insets);

    boolean isCircularPie();

    void setCircularPie(boolean circularPie);

    Rotation getPieDirection();

    void setPieDirection(Rotation pieDirection);

    double getStartAngle();

    void setStartAngle(double startAngle);

    boolean isLabelsVisible();

    void setLabelsVisible(boolean labelsVisible);

    String getLabelFormat();

    void setLabelFormat(String labelFormat);

    Font getLabelFont();

    void setLabelFont(Font labelFont);

    Paint getLabelPaint();

    void setLabelPaint(Paint labelPaint);

    Paint getLabelBackgroundPaint();

    void setLabelBackgroundPaint(Paint labelBackgroundPaint);

    Paint getLabelOutlinePaint();

    void setLabelOutlinePaint(Paint labelOutlinePaint);

    BasicStroke getLabelOutlineStroke();

    void setLabelOutlineStroke(BasicStroke labelOutlineStroke);

    RectangleInsets getLabelPadding();

    void setLabelPadding(RectangleInsets labelPadding);

    Paint getLabelShadowPaint();

    void setLabelShadowPaint(Paint labelShadowPaint);

    boolean isLabelLinksVisible();

    void setLabelLinksVisible(boolean labelLinksVisible);

    PieLabelLinkStyle getLabelLinkStyle();

    void setLabelLinkStyle(PieLabelLinkStyle labelLinkStyle);

    String getPercentFormatString();

    void setPercentFormatString(String percentFormatString);

    String getNumberFormatString();

    void setNumberFormatString(String numberFormatString);
}
