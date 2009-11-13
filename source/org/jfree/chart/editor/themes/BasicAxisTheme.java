package org.jfree.chart.editor.themes;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.Range;
import org.jfree.ui.RectangleEdge;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 13-Nov-2009
 * Time: 11:48:40
 * Implementation of the iPlusAxisTheme interface.
 */
public class BasicAxisTheme extends BasicAbstractChartTheme implements AxisTheme {
    private static final String CUSTOM_AXIS_THEME = "Custom Axis Theme";

    private static final Font DEFAULT_LARGE_FONT = ExtendedChartTheme.DEFAULT_LARGE_FONT;
    private static final Font DEFAULT_REGULAR_FONT = PlotTheme.DEFAULT_REGULAR_FONT;
    private static final Paint DEFAULT_LABEL_PAINT = ExtendedChartTheme.DEFAULT_LABEL_PAINT;
    private static final String DEFAULT_DOMAIN_AXIS_LABEL = "Domain";
    private static final String DEFAULT_RANGE_AXIS_LABEL = "Range";
    private static final boolean DEFAULT_TICK_LABEL_VIS = true;
    private static final boolean DEFAULT_TICK_MARK_VIS = true;
    private static final boolean DEFAULT_AUTO_RANGE = true;
    private static final double DEFAULT_LABEL_ANGLE = 0.0;
    private static final double DEFAULT_CATEGORY_LABEL_ANGLE = 0.0;
    private static final int DEFAULT_CATEGORY_MAX_LINES = 1;
    private static final boolean DEFAULT_AXIS_LINE_VIS = true;
    private static final Paint DEFAULT_AXIS_LINE_PAINT = Color.BLACK;
    private static final BasicStroke DEFAULT_AXIS_LINE_STROKE = new BasicStroke(1.0f);

    /**
     * A large font.  Used for the axis labels.
     */
    private Font axisLabelFont;

    /**
     * The tick label font size.  Used for axis tick labels.
     */
    private Font tickLabelFont;

    private Paint tickLabelPaint;

    /** The axis label paint. */
    private Paint axisLabelPaint;

    /** The label for the whole axis */
    private String axisLabel;

    /** Whether to show the tick labels */
    private boolean showTickLabels;

    /** Whether to show the tick marks */
    private boolean showTickMarks;

    /** Whether the range of an axis should be auto-calculated */
    private boolean autoRange;

    private double minRange, maxRange;

    private int type;

    private double labelAngleDegs;

    private double catLabelAngleDegs;

    private boolean lineVisible;

    private Paint linePaint;

    private BasicStroke lineStroke;

    private int catLabelMaxLines;


    public BasicAxisTheme(String name, int type) {
        super(name, CUSTOM_AXIS_THEME, false);
        setType(type);
        initDefaults();
    }

    public BasicAxisTheme(JFreeChart chart, String name, int type) {
        super(chart, name, CUSTOM_AXIS_THEME, false);
        // we ask the super class not to read the theme from the chart because setting the type here will affect
        // what is read - so we have to do it after setting the type of axis (domain or range)
        setType(type);
        initDefaults();
        readSettingsFromChart(chart);
    }

    private void setType(int type) {
        if (type != RANGE_AXIS && type != DOMAIN_AXIS) {
            throw new IllegalArgumentException("Axis must be of type RANGE_AXIS or DOMAIN_AXIS");
        }
        this.type = type;
    }

    public int getType() {
        return type;
    }

    protected void initDefaults() {

        this.axisLabelFont = DEFAULT_LARGE_FONT;
        this.tickLabelFont = DEFAULT_REGULAR_FONT;

        this.axisLabelPaint = DEFAULT_LABEL_PAINT;
        switch(type) {
            case RANGE_AXIS: axisLabel = DEFAULT_RANGE_AXIS_LABEL; break;
            default: axisLabel = DEFAULT_DOMAIN_AXIS_LABEL; break;
        }
        showTickLabels = DEFAULT_TICK_LABEL_VIS;
        showTickMarks = DEFAULT_TICK_MARK_VIS;
        this.autoRange = DEFAULT_AUTO_RANGE;
        this.minRange = 0;
        this.maxRange = 100;
        labelAngleDegs = DEFAULT_LABEL_ANGLE;
        catLabelAngleDegs = DEFAULT_CATEGORY_LABEL_ANGLE;
        catLabelMaxLines = DEFAULT_CATEGORY_MAX_LINES;

        this.lineVisible = DEFAULT_AXIS_LINE_VIS;
        this.linePaint = DEFAULT_AXIS_LINE_PAINT;
        this.lineStroke = DEFAULT_AXIS_LINE_STROKE;
        this.tickLabelPaint = DEFAULT_LABEL_PAINT;
    }

    public void readSettingsFromChart(JFreeChart chart) {
        Plot plot = chart.getPlot();
        Axis axis = null;
        if (plot instanceof CategoryPlot) {
            CategoryPlot cPlot = (CategoryPlot) plot;
            axis = cPlot.getDomainAxis();
            switch (type) {
                case RANGE_AXIS:
                    axis = cPlot.getRangeAxis(); break;
                case DOMAIN_AXIS:
                    axis = cPlot.getDomainAxis(); break;
            }
        } else if (plot instanceof XYPlot) {
            XYPlot xyPlot = (XYPlot) plot;
            switch (type) {
                case RANGE_AXIS:
                    axis = xyPlot.getRangeAxis(); break;
                case DOMAIN_AXIS:
                    axis = xyPlot.getDomainAxis(); break;
            }
        }
        if (axis != null) {
            this.axisLabelFont = axis.getLabelFont();
            this.tickLabelFont = axis.getTickLabelFont();
            this.axisLabelPaint = axis.getLabelPaint();
//            this.tickLabelPaint = axis.getTickLabelPaint();
            this.axisLabel = axis.getLabel();
            this.showTickLabels = axis.isTickLabelsVisible();
            this.showTickMarks = axis.isTickMarksVisible();
            this.labelAngleDegs = Math.toDegrees(axis.getLabelAngle());

            this.lineVisible = axis.isAxisLineVisible();
            this.linePaint = axis.getAxisLinePaint();
            this.lineStroke = (BasicStroke) axis.getAxisLineStroke();
            this.tickLabelPaint = axis.getTickLabelPaint();

            if(axis instanceof NumberAxis) {
                NumberAxis nAxis = (NumberAxis) axis;
                this.autoRange = nAxis.isAutoRange();
                Range range = nAxis.getRange();
                this.minRange = range.getLowerBound();
                this.maxRange = range.getUpperBound();
            } else if (axis instanceof CategoryAxis) {
                CategoryAxis cAxis = (CategoryAxis) axis;
                this.catLabelAngleDegs = Math.toDegrees(cAxis.getCategoryLabelPositions().
                        getLabelPosition(RectangleEdge.BOTTOM).getAngle());
                this.catLabelMaxLines = cAxis.getMaximumCategoryLabelLines();
            }
        }
    }

    public double getCatLabelAngleDegs() {
        return catLabelAngleDegs;
    }

    public void setCatLabelAngleDegs(double catLabelAngleDegs) {
        if(catLabelAngleDegs > 90) {
            catLabelAngleDegs = 90;
        } else if (catLabelAngleDegs < -90) {
            catLabelAngleDegs = -90;
        }
        this.catLabelAngleDegs = catLabelAngleDegs;
    }

    public int getCatLabelMaxLines() {
        return catLabelMaxLines;
    }

    public void setCatLabelMaxLines(int catLabelMaxLines) {
        this.catLabelMaxLines = catLabelMaxLines;
    }

    public double getLabelAngleDegs() {
        return labelAngleDegs;
    }

    public void setLabelAngleDegs(double labelAngleDegs) {
        this.labelAngleDegs = labelAngleDegs;
    }

    public Font getAxisLabelFont() {
        return axisLabelFont;
    }

    public void setAxisLabelFont(Font axisLabelFont) {
        this.axisLabelFont = axisLabelFont;
    }

    public Font getTickLabelFont() {
        return tickLabelFont;
    }

    public void setTickLabelFont(Font tickLabelFont) {
        this.tickLabelFont = tickLabelFont;
    }

    public Paint getAxisLabelPaint() {
        return axisLabelPaint;
    }

    public void setAxisLabelPaint(Paint axisLabelPaint) {
        this.axisLabelPaint = axisLabelPaint;
    }

    public Paint getTickLabelPaint() {
        return tickLabelPaint;
    }

    public void setTickLabelPaint(Paint tickLabelPaint) {
        this.tickLabelPaint = tickLabelPaint;
    }

    public String getAxisLabel() {
        return axisLabel;
    }

    public void setAxisLabel(String axisLabel) {
        this.axisLabel = axisLabel;
    }

    public boolean isShowTickLabels() {
        return showTickLabels;
    }

    public void setShowTickLabels(boolean showTickLabels) {
        this.showTickLabels = showTickLabels;
    }

    public boolean isShowTickMarks() {
        return showTickMarks;
    }

    public void setShowTickMarks(boolean showTickMarks) {
        this.showTickMarks = showTickMarks;
    }

    public boolean isAutoRange() {
        return autoRange;
    }

    public void setAutoRange(boolean autoRange) {
        this.autoRange = autoRange;
    }

    public double getMinRange() {
        return minRange;
    }

    public void setMinRange(double minRange) {
        this.minRange = minRange;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }

    public boolean isLineVisible() {
        return lineVisible;
    }

    public void setLineVisible(boolean lineVisible) {
        this.lineVisible = lineVisible;
    }

    public Paint getLinePaint() {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
    }

    public BasicStroke getLineStroke() {
        return lineStroke;
    }

    public void setLineStroke(BasicStroke lineStroke) {
        this.lineStroke = lineStroke;
    }

    public void apply(JFreeChart chart) {
        // do nothing.
    }

    public void apply(Axis axis) {
        axis.setLabelFont(this.axisLabelFont);
        axis.setLabelPaint(this.axisLabelPaint);
        axis.setTickLabelFont(this.tickLabelFont);

        axis.setTickLabelPaint(this.tickLabelPaint);
        axis.setLabel(this.axisLabel);
        axis.setLabelAngle(Math.toRadians(this.labelAngleDegs));
        axis.setTickLabelsVisible(this.showTickLabels);
        axis.setTickMarksVisible(this.showTickMarks);

        axis.setAxisLineVisible(this.lineVisible);
        axis.setAxisLinePaint(this.linePaint);
        axis.setAxisLineStroke(this.lineStroke);

        if(axis instanceof CategoryAxis) {
            applyToCategoryAxis((CategoryAxis)axis);
        }
        if(axis instanceof ValueAxis) {
            applyToValueAxis((ValueAxis) axis);
        }
    }

    protected void applyToCategoryAxis(CategoryAxis axis) {
        axis.setMaximumCategoryLabelLines(catLabelMaxLines);

        axis.setCategoryLabelPositions(ThemeUtil.getCategoryLabelPositions(catLabelAngleDegs));

        if (axis instanceof SubCategoryAxis) {
            SubCategoryAxis sca = (SubCategoryAxis) axis;
            sca.setSubLabelFont(this.tickLabelFont);
        }
    }

    protected void applyToValueAxis(ValueAxis axis) {
        if (axis instanceof PeriodAxis) {
            applyToPeriodAxis((PeriodAxis) axis);
        } else if (axis instanceof NumberAxis) {
            applyToNumberAxis((NumberAxis) axis);
        }
    }

    protected void applyToNumberAxis(NumberAxis axis) {
        // apply before the auto range flag as calls to this method have a side effect of setting auto range to false
        axis.setRange(new Range(minRange, maxRange));
        axis.setAutoRange(autoRange);
    }

    protected void applyToPeriodAxis(PeriodAxis axis) {
        PeriodAxisLabelInfo[] info = axis.getLabelInfo();
        for (int i = 0; i < info.length; i++) {
            PeriodAxisLabelInfo e = info[i];
            PeriodAxisLabelInfo n = new PeriodAxisLabelInfo(e.getPeriodClass(),
                    e.getDateFormat(), e.getPadding(), this.tickLabelFont,
                    /*this.tickLabelPaint*/e.getLabelPaint(), e.getDrawDividers(),
                    e.getDividerStroke(), e.getDividerPaint());
            info[i] = n;
        }
        axis.setLabelInfo(info);
    }
}
