package org.jfree.chart.editor.themes;

import org.jfree.ui.RectangleInsets;
import org.jfree.chart.plot.*;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.util.Rotation;
import org.jfree.util.PublicCloneable;

import java.awt.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 13-Nov-2009
 * Time: 11:45:15
 * Implementation of the iPlusPlotTheme interface.
 */
public class BasicPlotTheme extends BasicAbstractChartTheme implements PlotTheme {
    static final ExtendedDrawingSupplier DEFAULT_DRAWING_SUPPLIER = new BasicDrawingSupplier(null);
    private static final String CUSTOM_PLOT_THEME = "Custom Plot Theme";

    private static final ChartBorder DEFAULT_PLOT_BORDER =
                new BasicChartBorder(true, DEFAULT_BORDER_STROKE, DEFAULT_BORDER_PAINT);

    private static final RectangleInsets DEFAULT_AXIS_OFFSET = RectangleInsets.ZERO_INSETS;
    private static final Paint DEFAULT_GRIDLINE_PAINT = Color.DARK_GRAY;

    private static final Paint DEFAULT_BACKGROUND_PAINT = ExtendedChartTheme.DEFAULT_BACKGROUND_PAINT;

    static final String DEFAULT_NUMBER_FORMAT;
    static final String DEFAULT_PERCENT_FORMAT;
    static {
        NumberFormat nf = NumberFormat.getNumberInstance();
        NumberFormat pf = NumberFormat.getPercentInstance();
        if(nf instanceof DecimalFormat) {
            DEFAULT_NUMBER_FORMAT = ((DecimalFormat)nf).toPattern();
        } else {
            DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.UK); // guaranteed to return DecimalFormat in case of weird locale.
            DEFAULT_NUMBER_FORMAT = df.toPattern();
        }

        if(pf instanceof DecimalFormat) {
            DEFAULT_PERCENT_FORMAT = ((DecimalFormat)pf).toPattern();
        } else {
            DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.UK); // guaranteed to return DecimalFormat in case of weird locale.
            DEFAULT_PERCENT_FORMAT = df.toPattern();
        }
    }

    private AxisTheme domainAxisTheme, rangeAxisTheme;

    /**
     * The axis offsets.
     */
    private RectangleInsets axisOffset;

    /**
     * The drawing supplier.
     */
    private ExtendedDrawingSupplier drawingSupplier;

    /**
     * The background paint for the plot.
     */
    private Paint plotBackgroundPaint;

    /**
     * The plot outline
     */
    private ChartBorder plotOutline;

    private PlotOrientation orientation;

    /**
     * The domain grid line paint.
     */
    private Paint domainGridlinePaint;

    /**
     * The range grid line paint.
     */
    private Paint rangeGridlinePaint;

    private BasicStroke domainGridlineStroke;

    private BasicStroke rangeGridlineStroke;

    private boolean domainGridlineVisible, rangeGridlinesVisible;

    private boolean shadowsVisible;

    private RectangleInsets insets;

    private boolean circularPie;
    private Rotation pieDirection;
    private double startAngle;
    private boolean labelsVisible;
    private String labelFormat;
    private Font labelFont;
    private Paint labelPaint;
    private Paint labelBackgroundPaint;
    private Paint labelOutlinePaint;
    private BasicStroke labelOutlineStroke;
    private RectangleInsets labelPadding;
    private Paint labelShadowPaint;
    private boolean labelLinksVisible;
    private PieLabelLinkStyle labelLinkStyle;

    private String numberFormatString, percentFormatString;

    public BasicPlotTheme(String name) {
        super(name, CUSTOM_PLOT_THEME);
    }

    public BasicPlotTheme(JFreeChart chart, String name) {
        super(chart, name, CUSTOM_PLOT_THEME);
    }

    protected void initDefaults() {
        this.drawingSupplier = DEFAULT_DRAWING_SUPPLIER;
        this.plotBackgroundPaint = DEFAULT_BACKGROUND_PAINT;
        try {
            this.plotOutline = (ChartBorder) DEFAULT_PLOT_BORDER.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("Someone has created an extension of ChartBorder that will not clone itself!");
        }
        this.orientation = DEFAULT_PLOT_ORIENTATION;
        this.domainAxisTheme = new BasicAxisTheme(getName(), AxisTheme.DOMAIN_AXIS);
        this.rangeAxisTheme = new BasicAxisTheme(getName(), AxisTheme.RANGE_AXIS);
        this.shadowsVisible = DEFAULT_SHADOWS_VISIBLE;
        this.axisOffset = DEFAULT_AXIS_OFFSET;
        this.domainGridlinePaint = DEFAULT_GRIDLINE_PAINT;
        this.rangeGridlinePaint = DEFAULT_GRIDLINE_PAINT;
        this.domainGridlineStroke = DEFAULT_GRIDLINE_STROKE;
        this.rangeGridlineStroke = DEFAULT_GRIDLINE_STROKE;
        this.domainGridlineVisible = DEFAULT_DOMAIN_GRID_VISIBLE;
        this.rangeGridlinesVisible = DEFAULT_RANGE_GRID_VISIBLE;
        this.insets = DEFAULT_INSETS;
        this.circularPie = DEFAULT_CIRCULAR_PLOT;
        this.pieDirection = DEFAULT_PIE_DIRECTION;
        this.startAngle = DEFAULT_START_ANGLE;
        this.labelsVisible = DEFAULT_LABELS_VISIBLE;
        this.labelFormat = DEFAULT_LABEL_FORMAT;
        this.labelFont = DEFAULT_REGULAR_FONT;
        this.labelPaint = DEFAULT_LABEL_PAINT;
        this.labelBackgroundPaint = DEFAULT_LABEL_BACKGROUND_PAINT;
        this.labelOutlinePaint = DEFAULT_LABEL_OUTLINE_PAINT;
        this.labelOutlineStroke = DEFAULT_LABEL_OUTLINE_STROKE;
        this.labelPadding = DEFAULT_LABEL_PADDING;
        this.labelShadowPaint = DEFAULT_LABEL_SHADOW_PAINT;
        this.labelLinksVisible = DEFAULT_LINK_VISIBLE;
        this.labelLinkStyle = DEFAULT_LINK_STYLE;

        this.numberFormatString = DEFAULT_NUMBER_FORMAT;
        this.percentFormatString = DEFAULT_PERCENT_FORMAT;
    }

    public void readSettingsFromChart(JFreeChart chart) {
        Plot plot = chart.getPlot();
        if (plot instanceof CategoryPlot) {
            CategoryPlot cPlot = (CategoryPlot) plot;
            this.axisOffset = cPlot.getAxisOffset();
            this.orientation = cPlot.getOrientation();

            this.domainGridlinePaint = cPlot.getDomainGridlinePaint();
            this.rangeGridlinePaint = cPlot.getRangeGridlinePaint();
            this.domainGridlineStroke = (BasicStroke) cPlot.getDomainGridlineStroke();
            this.rangeGridlineStroke = (BasicStroke) cPlot.getRangeGridlineStroke();
            this.domainGridlineVisible = cPlot.isDomainGridlinesVisible();
            this.rangeGridlinesVisible = cPlot.isRangeGridlinesVisible();

            CategoryItemRenderer itemRenderer = cPlot.getRenderer();
            if (itemRenderer instanceof BarRenderer) {
                BarRenderer barRenderer = (BarRenderer) cPlot.getRenderer();
                this.shadowsVisible = barRenderer.getShadowsVisible();
            }
            Boolean Bool = itemRenderer.getBaseItemLabelsVisible();
            this.labelsVisible = Bool != null && Bool.booleanValue();
            this.labelFont = itemRenderer.getBaseItemLabelFont();
            this.labelPaint = itemRenderer.getBaseItemLabelPaint();

            StandardCategoryItemLabelGenerator sLabelGen =
                    (StandardCategoryItemLabelGenerator) itemRenderer.getBaseItemLabelGenerator();
            if(sLabelGen != null) {
                this.labelFormat = sLabelGen.getLabelFormat();
                NumberFormat nf = sLabelGen.getNumberFormat();
                NumberFormat pf = sLabelGen.getPercentFormat();
                if(nf instanceof DecimalFormat) {
                    numberFormatString = ((DecimalFormat) nf).toPattern();
                }

                if(pf instanceof DecimalFormat) {
                    percentFormatString = ((DecimalFormat) pf).toPattern();
                }
            }

        } else if (plot instanceof XYPlot) {
            XYPlot xyPlot = (XYPlot) plot;
            this.orientation = xyPlot.getOrientation();
            this.axisOffset = xyPlot.getAxisOffset();

            this.domainGridlinePaint = xyPlot.getDomainGridlinePaint();
            this.rangeGridlinePaint = xyPlot.getRangeGridlinePaint();
            this.domainGridlineStroke = (BasicStroke) xyPlot.getDomainGridlineStroke();
            this.rangeGridlineStroke = (BasicStroke) xyPlot.getRangeGridlineStroke();
            this.domainGridlineVisible = xyPlot.isDomainGridlinesVisible();
            this.rangeGridlinesVisible = xyPlot.isRangeGridlinesVisible();

            XYItemRenderer itemRenderer = xyPlot.getRenderer();
            if (itemRenderer instanceof XYBarRenderer) {
                this.shadowsVisible = ((XYBarRenderer) xyPlot.getRenderer()).getShadowsVisible();
            }

            Boolean Bool = itemRenderer.getBaseItemLabelsVisible();
            this.labelsVisible = Bool != null && Bool.booleanValue();
            this.labelFont = itemRenderer.getBaseItemLabelFont();
            this.labelPaint = itemRenderer.getBaseItemLabelPaint();

            StandardXYItemLabelGenerator sLabelGen =
                    (StandardXYItemLabelGenerator) itemRenderer.getBaseItemLabelGenerator();
            if(sLabelGen != null) {
                this.labelFormat = sLabelGen.getFormatString();
                NumberFormat nf = sLabelGen.getXFormat();
                if(nf instanceof DecimalFormat) {
                    numberFormatString = ((DecimalFormat) nf).toPattern();
                }
            }
        } else if (plot instanceof PiePlot) {
            PiePlot piePlot = (PiePlot) plot;
            this.shadowsVisible = piePlot.getShadowPaint() == null;
            this.circularPie = piePlot.isCircular();
            this.pieDirection = piePlot.getDirection();
            this.startAngle = piePlot.getStartAngle();

            PieSectionLabelGenerator labelGen = piePlot.getLabelGenerator();
            this.labelsVisible = labelGen != null;
            if(labelsVisible && labelGen instanceof StandardPieSectionLabelGenerator) {
                StandardPieSectionLabelGenerator sLabelGen = (StandardPieSectionLabelGenerator) labelGen;
                this.labelFormat = sLabelGen.getLabelFormat();
                NumberFormat nf = sLabelGen.getNumberFormat();
                NumberFormat pf = sLabelGen.getPercentFormat();
                if(nf instanceof DecimalFormat) {
                    numberFormatString = ((DecimalFormat) nf).toPattern();
                }

                if(pf instanceof DecimalFormat) {
                    percentFormatString = ((DecimalFormat) pf).toPattern();
                }
            }

            this.labelFont = piePlot.getLabelFont();
            this.labelPaint = piePlot.getLabelPaint();
            this.labelBackgroundPaint = piePlot.getLabelBackgroundPaint();
            this.labelOutlinePaint = piePlot.getLabelOutlinePaint();
            this.labelOutlineStroke = (BasicStroke)piePlot.getLabelOutlineStroke();
            this.labelPadding = piePlot.getLabelPadding();
            this.labelShadowPaint = piePlot.getLabelShadowPaint();
            this.labelLinkStyle = piePlot.getLabelLinkStyle();
            this.labelLinksVisible = piePlot.getLabelLinksVisible();
        }
        this.plotBackgroundPaint = plot.getBackgroundPaint();
        DrawingSupplier plotSupplier = plot.getDrawingSupplier();
        if (!(plotSupplier instanceof BasicDrawingSupplier)) {
            this.drawingSupplier = new BasicDrawingSupplier(getName(), plotSupplier);
        } else {
            this.drawingSupplier = (ExtendedDrawingSupplier) plotSupplier;
        }

        this.insets = plot.getInsets();

        this.plotOutline = new BasicChartBorder(plot.isOutlineVisible(), (BasicStroke) plot.getOutlineStroke(), plot.getOutlinePaint());

        this.domainAxisTheme = new BasicAxisTheme(chart, getName(), AxisTheme.DOMAIN_AXIS);
        this.rangeAxisTheme = new BasicAxisTheme(chart, getName(), AxisTheme.RANGE_AXIS);
    }

    public Paint getPlotBackgroundPaint() {
        return plotBackgroundPaint;
    }

    public void setPlotBackgroundPaint(Paint plotBackgroundPaint) {
        this.plotBackgroundPaint = plotBackgroundPaint;
    }

    public ChartBorder getBorder() {
        return plotOutline;
    }

    public void setBorder(ChartBorder plotOutline) {
        this.plotOutline = plotOutline;
    }

    public void setBorder(boolean visible, Paint paint, BasicStroke stroke) {
        this.plotOutline = new BasicChartBorder(visible, stroke, paint);
    }

    public ChartBorder getPlotOutline() {
        return plotOutline;
    }

    public void setPlotOutline(ChartBorder plotOutline) {
        this.plotOutline = plotOutline;
    }

    public PlotOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(PlotOrientation orientation) {
        this.orientation = orientation;
    }

    public boolean isShadowsVisible() {
        return shadowsVisible;
    }

    public void setShadowsVisible(boolean shadowsVisible) {
        this.shadowsVisible = shadowsVisible;
    }

    public RectangleInsets getAxisOffset() {
        return axisOffset;
    }

    public void setAxisOffset(RectangleInsets axisOffset) {
        this.axisOffset = axisOffset;
    }

    public Paint getDomainGridlinePaint() {
        return domainGridlinePaint;
    }

    public void setDomainGridlinePaint(Paint domainGridlinePaint) {
        this.domainGridlinePaint = domainGridlinePaint;
    }

    public Paint getRangeGridlinePaint() {
        return rangeGridlinePaint;
    }

    public void setRangeGridlinePaint(Paint rangeGridlinePaint) {
        this.rangeGridlinePaint = rangeGridlinePaint;
    }

    public BasicStroke getDomainGridlineStroke() {
        return domainGridlineStroke;
    }

    public void setDomainGridlineStroke(BasicStroke domainGridlineStroke) {
        this.domainGridlineStroke = domainGridlineStroke;
    }

    public BasicStroke getRangeGridlineStroke() {
        return rangeGridlineStroke;
    }

    public void setRangeGridlineStroke(BasicStroke rangeGridlineStroke) {
        this.rangeGridlineStroke = rangeGridlineStroke;
    }

    public boolean isDomainGridlineVisible() {
        return domainGridlineVisible;
    }

    public void setDomainGridlineVisible(boolean domainGridlineVisible) {
        this.domainGridlineVisible = domainGridlineVisible;
    }

    public boolean isRangeGridlinesVisible() {
        return rangeGridlinesVisible;
    }

    public void setRangeGridlinesVisible(boolean rangeGridlinesVisible) {
        this.rangeGridlinesVisible = rangeGridlinesVisible;
    }

    /**
     * Returns a clone of the drawing supplier for this theme.
     *
     * @return A clone of the drawing supplier.
     */
    public ExtendedDrawingSupplier getDrawingSupplier() {
        ExtendedDrawingSupplier result = null;
        if (this.drawingSupplier != null) {
            PublicCloneable pc = this.drawingSupplier;
            try {
                result = (ExtendedDrawingSupplier) pc.clone();
            }
            catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void setDrawingSupplier(ExtendedDrawingSupplier supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Null 'supplier' argument.");
        }
        this.drawingSupplier = supplier;
    }

    public AxisTheme getDomainAxisTheme() {
        return domainAxisTheme;
    }

    public void setDomainAxisTheme(AxisTheme domainAxisTheme) {
        this.domainAxisTheme = domainAxisTheme;
    }

    public AxisTheme getRangeAxisTheme() {
        return rangeAxisTheme;
    }

    public void setRangeAxisTheme(AxisTheme rangeAxisTheme) {
        this.rangeAxisTheme = rangeAxisTheme;
    }

    public RectangleInsets getInsets() {
        return insets;
    }

    public void setInsets(RectangleInsets insets) {
        if (insets == null) {
            throw new IllegalArgumentException("Plot insets cannot be null");
        }
        this.insets = insets;
    }

    public boolean isCircularPie() {
        return circularPie;
    }

    public void setCircularPie(boolean circularPie) {
        this.circularPie = circularPie;
    }

    public Rotation getPieDirection() {
        return pieDirection;
    }

    public void setPieDirection(Rotation pieDirection) {
        this.pieDirection = pieDirection;
    }

    public double getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(double startAngle) {
        this.startAngle = startAngle;
    }

    public boolean isLabelsVisible() {
        return labelsVisible;
    }

    public void setLabelsVisible(boolean labelsVisible) {
        this.labelsVisible = labelsVisible;
    }

    public String getLabelFormat() {
        return labelFormat;
    }

    public void setLabelFormat(String labelFormat) {
        this.labelFormat = labelFormat;
    }

    public Font getLabelFont() {
        return labelFont;
    }

    public void setLabelFont(Font labelFont) {
        this.labelFont = labelFont;
    }

    public Paint getLabelPaint() {
        return labelPaint;
    }

    public void setLabelPaint(Paint labelPaint) {
        this.labelPaint = labelPaint;
    }

    public Paint getLabelBackgroundPaint() {
        return labelBackgroundPaint;
    }

    public void setLabelBackgroundPaint(Paint labelBackgroundPaint) {
        this.labelBackgroundPaint = labelBackgroundPaint;
    }

    public Paint getLabelOutlinePaint() {
        return labelOutlinePaint;
    }

    public void setLabelOutlinePaint(Paint labelOutlinePaint) {
        this.labelOutlinePaint = labelOutlinePaint;
    }

    public BasicStroke getLabelOutlineStroke() {
        return labelOutlineStroke;
    }

    public void setLabelOutlineStroke(BasicStroke labelOutlineStroke) {
        this.labelOutlineStroke = labelOutlineStroke;
    }

    public RectangleInsets getLabelPadding() {
        return labelPadding;
    }

    public void setLabelPadding(RectangleInsets labelPadding) {
        this.labelPadding = labelPadding;
    }

    public void setLocaleForCurrentThread(Locale l) {
        super.setLocaleForCurrentThread(l);
        domainAxisTheme.setLocaleForCurrentThread(l);
        rangeAxisTheme.setLocaleForCurrentThread(l);
    }

    public Paint getLabelShadowPaint() {
        return labelShadowPaint;
    }

    public void setLabelShadowPaint(Paint labelShadowPaint) {
        this.labelShadowPaint = labelShadowPaint;
    }

    public boolean isLabelLinksVisible() {
        return labelLinksVisible;
    }

    public void setLabelLinksVisible(boolean labelLinksVisible) {
        this.labelLinksVisible = labelLinksVisible;
    }

    public PieLabelLinkStyle getLabelLinkStyle() {
        return labelLinkStyle;
    }

    public void setLabelLinkStyle(PieLabelLinkStyle labelLinkStyle) {
        this.labelLinkStyle = labelLinkStyle;
    }

    public String getPercentFormatString() {
        return percentFormatString;
    }

    public void setPercentFormatString(String percentFormatString) {
        this.percentFormatString = percentFormatString;
    }

    public String getNumberFormatString() {
        return numberFormatString;
    }

    public void setNumberFormatString(String numberFormatString) {
        this.numberFormatString = numberFormatString;
    }

    public void apply(JFreeChart chart) {
        Plot plot = chart.getPlot();
        if (plot == null) {
            throw new IllegalArgumentException("Null 'plot' argument.");
        }
        applyToPlot(plot);
    }

    protected void applyToPlot(Plot plot) {
        DrawingSupplier ds = getDrawingSupplier();
        if (ds instanceof ExtendedDrawingSupplier) {
            ((ExtendedDrawingSupplier) ds).reset();
        }
        plot.setDrawingSupplier(ds);
        plot.setBackgroundPaint(this.plotBackgroundPaint);

        plot.setOutlineVisible(plotOutline.isVisible());
        plot.setOutlineStroke(plotOutline.getStroke());
        plot.setOutlinePaint(plotOutline.getPaint());
        plot.setInsets(insets);

        // now handle specific plot types
        if (plot instanceof PiePlot) {
            applyToPiePlot((PiePlot) plot);
        } else if (plot instanceof MultiplePiePlot) {
            applyToMultiplePiePlot((MultiplePiePlot) plot);
        } else if (plot instanceof CategoryPlot) {
            applyToCategoryPlot((CategoryPlot) plot);
        } else if (plot instanceof XYPlot) {
            applyToXYPlot((XYPlot) plot);
        }
    }

    protected void applyToPiePlot(PiePlot plot) {
        plot.setCircular(circularPie);
        plot.setDirection(pieDirection);
        plot.setStartAngle(startAngle);
        if(!labelsVisible) {
            plot.setLabelGenerator(null);
        } else {
            StandardPieSectionLabelGenerator newLabelGen = new StandardPieSectionLabelGenerator(labelFormat,
                        new DecimalFormat(numberFormatString, new DecimalFormatSymbols(getLocaleForCurrentThread())),
                        new DecimalFormat(percentFormatString, new DecimalFormatSymbols(getLocaleForCurrentThread())));

            plot.setLabelGenerator(newLabelGen);
        }
        plot.setLabelFont(labelFont);
        plot.setLabelPaint(labelPaint);
        plot.setLabelBackgroundPaint(labelBackgroundPaint);
        plot.setLabelOutlinePaint(labelOutlinePaint);
        plot.setLabelOutlineStroke(labelOutlineStroke);
        plot.setLabelPadding(labelPadding);
        plot.setLabelLinksVisible(labelLinksVisible);
        plot.setLabelLinkStyle(labelLinkStyle);
        plot.setLabelShadowPaint(labelShadowPaint);

        if (!shadowsVisible) {
            plot.setShadowPaint(null);
        } else {
            plot.setShadowPaint(Color.LIGHT_GRAY);
        }

        // clear the section attributes so that the theme's DrawingSupplier
        // will be used
        if (plot.getAutoPopulateSectionPaint()) {
            plot.clearSectionPaints(false);
        }
        if (plot.getAutoPopulateSectionOutlinePaint()) {
            plot.clearSectionOutlinePaints(false);
        }
        if (plot.getAutoPopulateSectionOutlineStroke()) {
            plot.clearSectionOutlineStrokes(false);
        }
    }

    protected void applyToMultiplePiePlot(MultiplePiePlot plot) {
        apply(plot.getPieChart());
    }

    protected void applyToCategoryPlot(CategoryPlot plot) {
        plot.setDomainGridlinePaint(this.domainGridlinePaint);
        plot.setDomainGridlineStroke(this.domainGridlineStroke);
        plot.setRangeGridlinePaint(this.rangeGridlinePaint);
        plot.setRangeGridlineStroke(this.rangeGridlineStroke);
        plot.setDomainGridlinesVisible(this.domainGridlineVisible);
        plot.setRangeGridlinesVisible(this.rangeGridlinesVisible);
        plot.setOrientation(orientation);
        plot.setAxisOffset(axisOffset);

        // process all domain axes
        int domainAxisCount = plot.getDomainAxisCount();
        for (int i = 0; i < domainAxisCount; i++) {
            CategoryAxis axis = plot.getDomainAxis(i);
            if (axis != null) {
                domainAxisTheme.apply(axis);
            }
        }

        // process all range axes
        int rangeAxisCount = plot.getRangeAxisCount();
        for (int i = 0; i < rangeAxisCount; i++) {
            ValueAxis axis = plot.getRangeAxis(i);
            if (axis != null) {
                rangeAxisTheme.apply(axis);
            }
        }

        // process all renderers
        int rendererCount = plot.getRendererCount();
        for (int i = 0; i < rendererCount; i++) {
            CategoryItemRenderer r = plot.getRenderer(i);
            if (r != null) {
                applyToCategoryItemRenderer(r);
            }
        }

        if (plot instanceof CombinedDomainCategoryPlot) {
            CombinedDomainCategoryPlot cp = (CombinedDomainCategoryPlot) plot;
            Iterator iterator = cp.getSubplots().iterator();
            while (iterator.hasNext()) {
                CategoryPlot subplot = (CategoryPlot) iterator.next();
                if (subplot != null) {
                    applyToPlot(subplot);
                }
            }
        }
        if (plot instanceof CombinedRangeCategoryPlot) {
            CombinedRangeCategoryPlot cp = (CombinedRangeCategoryPlot) plot;
            Iterator iterator = cp.getSubplots().iterator();
            while (iterator.hasNext()) {
                CategoryPlot subplot = (CategoryPlot) iterator.next();
                if (subplot != null) {
                    applyToPlot(subplot);
                }
            }
        }
    }

    protected void applyToXYPlot(XYPlot plot) {
        // no way to edit in GUI
//        plot.setDomainCrosshairPaint(this.crosshairPaint);
//        plot.setRangeCrosshairPaint(this.crosshairPaint);

        plot.setDomainGridlinePaint(this.domainGridlinePaint);
        plot.setDomainGridlineStroke(this.domainGridlineStroke);
        plot.setRangeGridlinePaint(this.rangeGridlinePaint);
        plot.setRangeGridlineStroke(this.rangeGridlineStroke);
        plot.setDomainGridlinesVisible(this.domainGridlineVisible);
        plot.setRangeGridlinesVisible(this.rangeGridlinesVisible);

        plot.setOrientation(orientation);
        plot.setAxisOffset(this.axisOffset);
        // process all domain axes
        int domainAxisCount = plot.getDomainAxisCount();
        for (int i = 0; i < domainAxisCount; i++) {
            ValueAxis axis = plot.getDomainAxis(i);
            if (axis != null) {
                domainAxisTheme.apply(axis);
            }
        }

        // process all range axes
        int rangeAxisCount = plot.getRangeAxisCount();
        for (int i = 0; i < rangeAxisCount; i++) {
            ValueAxis axis = plot.getRangeAxis(i);
            if (axis != null) {
                rangeAxisTheme.apply(axis);
            }
        }

        // process all renderers
        int rendererCount = plot.getRendererCount();
        for (int i = 0; i < rendererCount; i++) {
            XYItemRenderer r = plot.getRenderer(i);
            if (r != null) {
                applyToXYItemRenderer(r);
            }
        }

        // process all annotations
        Iterator iter = plot.getAnnotations().iterator();
        while (iter.hasNext()) {
            XYAnnotation a = (XYAnnotation) iter.next();
            applyToXYAnnotation(a);
        }

        if (plot instanceof CombinedDomainXYPlot) {
            CombinedDomainXYPlot cp = (CombinedDomainXYPlot) plot;
            Iterator iterator = cp.getSubplots().iterator();
            while (iterator.hasNext()) {
                XYPlot subplot = (XYPlot) iterator.next();
                if (subplot != null) {
                    applyToPlot(subplot);
                }
            }
        }
        if (plot instanceof CombinedRangeXYPlot) {
            CombinedRangeXYPlot cp = (CombinedRangeXYPlot) plot;
            Iterator iterator = cp.getSubplots().iterator();
            while (iterator.hasNext()) {
                XYPlot subplot = (XYPlot) iterator.next();
                if (subplot != null) {
                    applyToPlot(subplot);
                }
            }
        }
    }

    protected void applyToFastScatterPlot(FastScatterPlot plot) {
        // no way to edit in GUI.
        plot.setDomainGridlinePaint(this.domainGridlinePaint);
        plot.setDomainGridlineStroke(this.domainGridlineStroke);
        plot.setRangeGridlinePaint(this.rangeGridlinePaint);
        plot.setRangeGridlineStroke(this.rangeGridlineStroke);
        plot.setDomainGridlinesVisible(this.domainGridlineVisible);
        plot.setRangeGridlinesVisible(this.rangeGridlinesVisible);
        ValueAxis xAxis = plot.getDomainAxis();
        if (xAxis != null) {
            domainAxisTheme.apply(xAxis);
        }
        ValueAxis yAxis = plot.getRangeAxis();
        if (yAxis != null) {
            rangeAxisTheme.apply(yAxis);
        }

    }

    protected void applyToPolarPlot(PolarPlot plot) {
        // no way to edit in GUI.
//        plot.setAngleLabelFont(this.itemLabelFont);
//        plot.setAngleLabelPaint(this.itemLabelPaint);
        plot.setAngleGridlinePaint(this.domainGridlinePaint);
        plot.setAngleGridlineStroke(this.domainGridlineStroke);
        plot.setRadiusGridlinePaint(this.rangeGridlinePaint);
        plot.setRadiusGridlineStroke(this.rangeGridlineStroke);
        plot.setAngleGridlinesVisible(this.domainGridlineVisible);
        plot.setRadiusGridlinesVisible(this.rangeGridlinesVisible);
        ValueAxis axis = plot.getAxis();
        if (axis != null) {
            rangeAxisTheme.apply(axis);
        }
    }

    protected void applyToAbstractRenderer(AbstractRenderer renderer) {
        if (renderer.getAutoPopulateSeriesPaint()) {
            renderer.clearSeriesPaints(false);
        }
        if (renderer.getAutoPopulateSeriesStroke()) {
            renderer.clearSeriesStrokes(false);
        }
    }

    protected void applyToCategoryItemRenderer(CategoryItemRenderer renderer) {
        if (renderer == null) {
            throw new IllegalArgumentException("Null 'renderer' argument.");
        }

        if (renderer instanceof AbstractRenderer) {
            applyToAbstractRenderer((AbstractRenderer) renderer);
        }

        renderer.setBaseItemLabelsVisible(labelsVisible);
        renderer.setBaseItemLabelFont(labelFont);
        renderer.setBaseItemLabelPaint(labelPaint);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator(labelFormat,
                        new DecimalFormat(numberFormatString, new DecimalFormatSymbols(getLocaleForCurrentThread())),
                        new DecimalFormat(percentFormatString, new DecimalFormatSymbols(getLocaleForCurrentThread()))));


        // BarRenderer
        if (renderer instanceof BarRenderer) {
            BarRenderer br = (BarRenderer) renderer;
            br.setShadowVisible(shadowsVisible);
        }
    }

    protected void applyToXYItemRenderer(XYItemRenderer renderer) {
        if (renderer == null) {
            throw new IllegalArgumentException("Null 'renderer' argument.");
        }
        if (renderer instanceof AbstractRenderer) {
            applyToAbstractRenderer((AbstractRenderer) renderer);
        }
        renderer.setBaseItemLabelsVisible(labelsVisible);
        renderer.setBaseItemLabelFont(labelFont);
        renderer.setBaseItemLabelPaint(labelPaint);
        // use the same number format on both axes - but create 2 copies for thread safety since DecimalFormat is not
        // thread safe.
        renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator(labelFormat,
                new DecimalFormat(numberFormatString, new DecimalFormatSymbols(getLocaleForCurrentThread())),
                new DecimalFormat(numberFormatString, new DecimalFormatSymbols(getLocaleForCurrentThread()))));

        if (renderer instanceof XYBarRenderer) {
            XYBarRenderer br = (XYBarRenderer) renderer;
            br.setShadowVisible(shadowsVisible);
        }
    }

    protected void applyToXYAnnotation(XYAnnotation annotation) {
        if (annotation == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        if (annotation instanceof XYTextAnnotation) {
            //XYTextAnnotation xyta = (XYTextAnnotation) annotation;
            // no way to edit in GUI
//            xyta.setPaint(this.itemLabelPaint);
        }
    }


}
