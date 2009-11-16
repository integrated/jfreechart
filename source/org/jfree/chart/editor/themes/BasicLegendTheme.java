package org.jfree.chart.editor.themes;

import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RectangleEdge;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.Title;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 13-Nov-2009
 * Time: 11:42:58
 * Implementation of the iPlusLegendTheme interface.
 */
public class BasicLegendTheme extends BasicAbstractTitleTheme implements LegendTheme {
    static final String CUSTOM_LEGEND_THEME = "Custom Legend Theme";
    static final boolean DEFAULT_LEGEND_VISIBILITY = true;
    static final RectangleInsets DEFAULT_ITEM_PADDING = new RectangleInsets(2,2,2,2);
    static final RectangleInsets DEFAULT_GRAPHIC_PADDING = DEFAULT_ITEM_PADDING;
    static final RectangleEdge DEFAULT_GRAPHIC_EDGE = RectangleEdge.LEFT;
    static final Font DEFAULT_ITEM_FONT = new Font("SansSerif", Font.PLAIN, 12);

    /** The legend background paint. */
    private Paint legendBackgroundPaint;

    /** The legend item paint. */
    private Paint legendItemPaint;

    private Font legendItemFont;

    private RectangleInsets itemPadding;

    private RectangleInsets graphicPadding;

    private RectangleEdge graphicEdge;

    private boolean visible;

    public BasicLegendTheme(String name) {
        super(name, CUSTOM_LEGEND_THEME);
    }

    public BasicLegendTheme(JFreeChart chart, String name) {
        super(chart, name, CUSTOM_LEGEND_THEME);
    }

    protected void initDefaults() {
        super.initDefaults();
        this.legendBackgroundPaint = ExtendedChartTheme.DEFAULT_BACKGROUND_PAINT;
        this.legendItemPaint = ExtendedChartTheme.DEFAULT_LABEL_PAINT;
        this.legendItemFont = DEFAULT_ITEM_FONT;
        this.itemPadding = DEFAULT_ITEM_PADDING;
        this.graphicPadding = DEFAULT_GRAPHIC_PADDING;
        this.graphicEdge = DEFAULT_GRAPHIC_EDGE;
        this.visible = DEFAULT_LEGEND_VISIBILITY;
    }

    public void readSettingsFromChart(JFreeChart chart) {
        super.readSettingsFromChart(chart);
        LegendTitle legend = chart.getLegend();

        if(legend != null) {
            this.legendBackgroundPaint = legend.getBackgroundPaint();
            this.legendItemPaint = legend.getItemPaint();
            this.legendItemFont = legend.getItemFont();
            this.itemPadding = legend.getItemLabelPadding();
            this.graphicPadding = legend.getLegendItemGraphicPadding();
            this.graphicEdge = legend.getLegendItemGraphicEdge();
            this.visible = true;
        } else {
            this.visible = false;
        }

    }

    protected Title getTitleObject(JFreeChart chart) {
        return chart.getLegend();
    }

    public Paint getLegendBackgroundPaint() {
        return this.legendBackgroundPaint;
    }

    public void setLegendBackgroundPaint(Paint paint) {
        this.legendBackgroundPaint = paint;
    }

    public Paint getLegendItemPaint() {
        return this.legendItemPaint;
    }

    public void setLegendItemPaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.legendItemPaint = paint;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Font getLegendItemFont() {
        return legendItemFont;
    }

    public void setLegendItemFont(Font legendItemFont) {
        this.legendItemFont = legendItemFont;
    }

    public RectangleInsets getItemPadding() {
        return itemPadding;
    }

    public void setItemPadding(RectangleInsets itemPadding) {
        this.itemPadding = itemPadding;
    }

    public RectangleInsets getGraphicPadding() {
        return graphicPadding;
    }

    public void setGraphicPadding(RectangleInsets graphicPadding) {
        this.graphicPadding = graphicPadding;
    }

    public RectangleEdge getGraphicEdge() {
        return graphicEdge;
    }

    public void setGraphicEdge(RectangleEdge graphicEdge) {
        this.graphicEdge = graphicEdge;
    }

    public void apply(JFreeChart chart) {
        LegendTitle legend = chart.getLegend();

        if(legend != null) { // legend currently visible
            if(!visible) {
                chart.removeLegend();
                return;
            }
        } else { // legend currently invisible
            if (visible) {
                legend = new LegendTitle(chart.getPlot());
                chart.addLegend(legend);
            } else {
                return; // invisible and should remain that way.
            }
        }

        legend.setBackgroundPaint(legendBackgroundPaint);
        legend.setItemPaint(legendItemPaint);
        legend.setItemFont(legendItemFont);
        legend.setItemLabelPadding(itemPadding);
        legend.setLegendItemGraphicPadding(graphicPadding);
        legend.setLegendItemGraphicEdge(graphicEdge);
        super.apply(chart);
    }
}
