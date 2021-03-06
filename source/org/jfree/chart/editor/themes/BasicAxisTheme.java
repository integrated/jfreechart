package org.jfree.chart.editor.themes;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
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
    static final String CUSTOM_AXIS_THEME = "Custom Axis Theme";

    static final Font DEFAULT_LARGE_FONT = ExtendedChartTheme.DEFAULT_LARGE_FONT;
    static final Font DEFAULT_REGULAR_FONT = PlotTheme.DEFAULT_REGULAR_FONT;
    static final Paint DEFAULT_LABEL_PAINT = ExtendedChartTheme.DEFAULT_LABEL_PAINT;
    static final String DEFAULT_DOMAIN_AXIS_LABEL = "Domain";
    static final String DEFAULT_RANGE_AXIS_LABEL = "Range";
    static final boolean DEFAULT_TICK_LABEL_VIS = true;
    static final boolean DEFAULT_TICK_MARK_VIS = true;
    static final boolean DEFAULT_AUTO_RANGE = true;
    static final double DEFAULT_LABEL_ANGLE = 0.0;
    static final double DEFAULT_CATEGORY_LABEL_ANGLE = 0.0;
    static final int DEFAULT_CATEGORY_MAX_LINES = 1;
    static final boolean DEFAULT_AXIS_LINE_VIS = true;
    static final Paint DEFAULT_AXIS_LINE_PAINT = Color.BLACK;
    static final BasicStroke DEFAULT_AXIS_LINE_STROKE = new BasicStroke(1.0f);

    public static final AxisLocation DEFAULT_DOMAIN_AXIS_LOCATION = AxisLocation.BOTTOM_OR_LEFT;
    public static final AxisLocation DEFAULT_RANGE_AXIS_LOCATION = AxisLocation.TOP_OR_LEFT;

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

    private boolean zeroLineVisible;

    private Paint zeroLinePaint;

    private BasicStroke zeroLineStroke;

    private int catLabelMaxLines;

    private double upperMargin, lowerMargin, categoryMargin, itemMargin;

    private AxisLocation axisLocation;


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

        this.zeroLineVisible = AbstractDomainRangePlot.DEFAULT_RANGE_ZERO_BASELINE_VISIBLE;
        this.zeroLinePaint = AbstractDomainRangePlot.DEFAULT_RANGE_ZERO_BASELINE_PAINT;
        this.zeroLineStroke = AbstractDomainRangePlot.DEFAULT_RANGE_ZERO_BASELINE_STROKE;

        this.tickLabelPaint = DEFAULT_LABEL_PAINT;

        this.upperMargin = CategoryAxis.DEFAULT_AXIS_MARGIN;
        this.lowerMargin = CategoryAxis.DEFAULT_AXIS_MARGIN;
        this.categoryMargin = CategoryAxis.DEFAULT_CATEGORY_MARGIN;
        this.itemMargin = BarRenderer.DEFAULT_ITEM_MARGIN;

        this.axisLocation = type == DOMAIN_AXIS ? DEFAULT_DOMAIN_AXIS_LOCATION : DEFAULT_RANGE_AXIS_LOCATION;
    }

    public void readSettingsFromChart(JFreeChart chart) {
        Plot plot = chart.getPlot();
        Axis axis = null;
        if (plot instanceof DomainRangePlot) {
            DomainRangePlot drPlot = chart.getDomainRangePlot();
            axis = drPlot.getBasicDomainAxis();
            switch (type) {
                case RANGE_AXIS:
                    axis = drPlot.getRangeAxis();
                    this.axisLocation = drPlot.getRangeAxisLocation();
                    this.zeroLineVisible = drPlot.isRangeZeroBaselineVisible();
                    this.zeroLinePaint = drPlot.getRangeZeroBaselinePaint();
                    this.zeroLineStroke = (BasicStroke) drPlot.getRangeZeroBaselineStroke();
                    break;
                case DOMAIN_AXIS:
                    axis = drPlot.getBasicDomainAxis();
                    this.axisLocation = drPlot.getDomainAxisLocation();
                    if(drPlot instanceof XYPlot) {
                        XYPlot xyPlot = (XYPlot) drPlot;
                        this.zeroLineVisible = xyPlot.isDomainZeroBaselineVisible();
                        this.zeroLinePaint = xyPlot.getDomainZeroBaselinePaint();
                        this.zeroLineStroke = (BasicStroke) xyPlot.getDomainZeroBaselineStroke();
                    }
                    break;
            }
            if(drPlot.getBasicRenderer() instanceof BarRenderer) {
                this.itemMargin = ((BarRenderer)drPlot.getBasicRenderer()).getItemMargin();
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
                this.upperMargin = cAxis.getUpperMargin();
                this.lowerMargin = cAxis.getLowerMargin();
                this.categoryMargin = cAxis.getCategoryMargin();
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

    public boolean isZeroLineVisible() {
        return zeroLineVisible;
    }

    public void setZeroLineVisible(boolean lineVisible) {
        this.zeroLineVisible = lineVisible;
    }

    public Paint getZeroLinePaint() {
        return zeroLinePaint;
    }

    public void setZeroLinePaint(Paint linePaint) {
        this.zeroLinePaint = linePaint;
    }

    public BasicStroke getZeroLineStroke() {
        return zeroLineStroke;
    }

    public void setZeroLineStroke(BasicStroke lineStroke) {
        this.zeroLineStroke = lineStroke;
    }

    public void setCategoryMargin(double d) {
        if(d>=0 && d<=1) {
            this.categoryMargin = d;
        }
    }

    public double getCategoryMargin() {
        return this.categoryMargin;
    }

    public double getLowerMargin() {
        return this.lowerMargin;
    }

    public void setLowerMargin(double d) {
        if(d>=0 && d<=1) {
            this.lowerMargin = d;
        }
    }

    public double getUpperMargin() {
        return upperMargin;
    }

    public void setUpperMargin(double d) {
        if(d>=0 && d<=1) {
            this.upperMargin = d;
        }
    }

    public double getItemMargin() {
        return itemMargin;
    }

    public void setItemMargin(double d) {
        if(d>=0 && d<=1) {
            this.itemMargin = d;
        }
    }

    public AxisLocation getAxisLocation() {
        return axisLocation;
    }

    public void setAxisLocation(AxisLocation a) {
        if(a == null) {
            throw new IllegalArgumentException("Axis location cannot be null!");
        }
        this.axisLocation = a;
    }

    public void apply(JFreeChart chart) {
        // do nothing.
    }

    public void apply(Axis axis) {

        if(axis.getPlot() instanceof DomainRangePlot) {
            DomainRangePlot plot = (DomainRangePlot) axis.getPlot();
            switch(type) {
                case DOMAIN_AXIS:
                    plot.setDomainAxisLocation(this.axisLocation);
                    if(plot instanceof XYPlot) {
                        XYPlot xyPlot = (XYPlot) plot;
                        xyPlot.setDomainZeroBaselineVisible(zeroLineVisible);
                        xyPlot.setDomainZeroBaselinePaint(zeroLinePaint);
                        xyPlot.setDomainZeroBaselineStroke(zeroLineStroke);
                    }
                    break;
                case RANGE_AXIS:
                    plot.setRangeAxisLocation(this.axisLocation);
                    plot.setRangeZeroBaselineVisible(zeroLineVisible);
                    plot.setRangeZeroBaselinePaint(zeroLinePaint);
                    plot.setRangeZeroBaselineStroke(zeroLineStroke);
                    break;
            }
        }

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
        Plot p = axis.getPlot();
        if(p instanceof CategoryPlot && type == DOMAIN_AXIS) {
            CategoryPlot cp = (CategoryPlot) p;
            if(cp.getRenderer() instanceof BarRenderer) {
                ((BarRenderer)cp.getRenderer()).setItemMargin(this.itemMargin);
            }
        }
        axis.setMaximumCategoryLabelLines(catLabelMaxLines);

        axis.setCategoryLabelPositions(ThemeUtil.getCategoryLabelPositions(catLabelAngleDegs));

        axis.setLowerMargin(getLowerMargin());
        axis.setUpperMargin(getUpperMargin());
        axis.setCategoryMargin(getCategoryMargin());

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
