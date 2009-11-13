package org.jfree.chart.editor.themes;

import org.jfree.ui.RectangleInsets;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.Block;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.chart.title.CompositeTitle;

import java.awt.*;
import java.util.Properties;
import java.util.Locale;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 13-Nov-2009
 * Time: 11:26:17
 * Implementation of the iPlusChartTheme interface.
 */
public class BasicChartTheme extends BasicAbstractChartTheme implements EditableBorder, Cloneable, ExtendedChartTheme {
    public static final ChartBorder DEFAULT_CHART_BORDER =
        new BasicChartBorder(false, DEFAULT_BORDER_STROKE, DEFAULT_BORDER_PAINT);


    private ChartTitleTheme titleTheme;
    private PlotTheme plotTheme;
    private LegendTheme legendTheme;

    private Font subtitleFont;

    private Paint subtitlePaint;

    private Paint chartBackgroundPaint;

    private boolean antiAliased;

    private boolean antiAliasedText;

    private ChartBorder border;

    private RectangleInsets padding;

    private String chartTemplate;

    private boolean seriesInColumns;
    private Properties properties;

    public BasicChartTheme(String name) {
        super(name, THEME_NAME);
    }

    protected void initDefaults() {
        this.subtitleFont = DEFAULT_LARGE_FONT;
        this.subtitlePaint = DEFAULT_TEXT_PAINT;
        this.chartBackgroundPaint = DEFAULT_BACKGROUND_PAINT;
        this.antiAliased = true;
        this.antiAliasedText = true;
        try {
            this.border = (ChartBorder) DEFAULT_CHART_BORDER.clone();
        } catch (CloneNotSupportedException e) {
            System.err.println("Someone has created an implementation of ChartBorder that will not clone itself!");
        }
        this.titleTheme = new BasicChartTitleTheme(getName());
        this.plotTheme = new BasicPlotTheme(getName());
        this.legendTheme = new BasicLegendTheme(getName());
        this.padding = DEFAULT_PADDING;
        this.chartTemplate = "BarChart";
        this.seriesInColumns = DEFAULT_DATA_ORIENTATION_IN_COLS;

        this.properties = new Properties();
    }


    public void setPreferredChartTemplate(String s) {
        this.chartTemplate = s;
    }

    public void setProperty(String property, String value) {
        properties.setProperty(property, value);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Returns a clone of the theme properties. Changes to the returned object will not be
     * reflected in this theme.
     * @return A clone of the theme's properties.
     */
    public Properties getProperties() {
        return (Properties) properties.clone();
    }

    public String getPreferredChartTemplate() {
        return chartTemplate;
    }

    public ChartTitleTheme getTitleTheme() {
        return titleTheme;
    }

    public void setTitleTheme(ChartTitleTheme titleTheme) {
        this.titleTheme = titleTheme;
    }

    public PlotTheme getPlotTheme() {
        return plotTheme;
    }

    public void setPlotTheme(PlotTheme plotTheme) {
        this.plotTheme = plotTheme;
    }

    public LegendTheme getLegendTheme() {
        return legendTheme;
    }

    public void setLegendTheme(LegendTheme legendTheme) {
        this.legendTheme = legendTheme;
    }

    /**
     * Changes any parameters that are set differently in the given chart.
     * @param chart The chart to read from.
     */
    public void readSettingsFromChart(JFreeChart chart) {
        if(chart.getSubtitleCount() > 0 && chart.getSubtitle(0) instanceof TextTitle) {
            TextTitle subtitle = (TextTitle) chart.getSubtitle(0);
            this.subtitlePaint = subtitle.getPaint();
            this.subtitleFont = subtitle.getFont();
        }

        this.antiAliased = chart.getAntiAlias();
        Object anti = chart.getTextAntiAlias();
        this.antiAliasedText = anti == null || RenderingHints.VALUE_TEXT_ANTIALIAS_ON.equals(anti);

        this.chartBackgroundPaint = chart.getBackgroundPaint();
        this.padding = chart.getPadding();

        border = new BasicChartBorder(chart.isBorderVisible(), (BasicStroke) chart.getBorderStroke(), chart.getBorderPaint());

        this.titleTheme = new BasicChartTitleTheme(chart, getName());
        this.plotTheme = new BasicPlotTheme(chart, getName());
        this.legendTheme = new BasicLegendTheme(chart, getName());
    }

    public void setLocaleForCurrentThread(Locale l) {
        super.setLocaleForCurrentThread(l);
        plotTheme.setLocaleForCurrentThread(l);
        legendTheme.setLocaleForCurrentThread(l);
        titleTheme.setLocaleForCurrentThread(l);
    }

    public RectangleInsets getPadding() {
        return padding;
    }

    public void setPadding(RectangleInsets padding) {
        this.padding = padding;
    }

    public Font getSubtitleFont() {
        return subtitleFont;
    }

    public void setSubtitleFont(Font subtitleFont) {
        this.subtitleFont = subtitleFont;
    }

    public Paint getSubtitlePaint() {
        return this.subtitlePaint;
    }
    public void setSubtitlePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null paint argument.");
        }
        this.subtitlePaint = paint;
    }

    public Paint getChartBackgroundPaint() {
        return this.chartBackgroundPaint;
    }

    public void setChartBackgroundPaint(Paint paint) {
        this.chartBackgroundPaint = paint;
    }

    public boolean isAntiAliased() {
        return antiAliased;
    }

    public void setAntiAliased(boolean antiAliased) {
        this.antiAliased = antiAliased;
    }

    public boolean isAntiAliasedText() {
        return antiAliasedText;
    }

    public void setAntiAliasedText(boolean antiAliasedText) {
        this.antiAliasedText = antiAliasedText;
    }

    public boolean isSeriesInColumns() {
        return seriesInColumns;
    }

    public void setSeriesInColumns(boolean seriesInColumns) {
        this.seriesInColumns = seriesInColumns;
    }

    public void apply(JFreeChart chart) {
        if (chart == null) {
            throw new IllegalArgumentException("Null 'chart' argument.");
        }


        int subtitleCount = chart.getSubtitleCount();
        for (int i = 0; i < subtitleCount; i++) {
            applyToTitle(chart.getSubtitle(i));
        }

        chart.setBackgroundPaint(this.chartBackgroundPaint);

        chart.setAntiAlias(antiAliased);
        chart.setTextAntiAlias(antiAliasedText);

        chart.setBorderVisible(border.isVisible());
        chart.setBorderStroke(border.getStroke());
        chart.setBorderPaint(border.getPaint());

        chart.setPadding(padding);

        // now process the plot if there is one
        this.plotTheme.apply(chart);

        if(this.titleTheme != null) {
            titleTheme.apply(chart);
        }

        if(this.legendTheme != null) {
            legendTheme.apply(chart);
        }

        applyCustomChartProperties(chart);
    }

    public void applyCustomChartProperties(JFreeChart chart) {

    }

    /**
     * Applies the attributes of this theme to the specified title.
     *
     * @param title  the title.
     */
    protected void applyToTitle(Title title) {
        if (title instanceof TextTitle) {
            TextTitle tt = (TextTitle) title;
            tt.setFont(this.subtitleFont);
            tt.setPaint(this.subtitlePaint);
        }
        else if (title instanceof CompositeTitle) {
            CompositeTitle ct = (CompositeTitle) title;
            BlockContainer bc = ct.getContainer();
            java.util.List blocks = bc.getBlocks();
            Iterator iterator = blocks.iterator();
            while (iterator.hasNext()) {
                Block b = (Block) iterator.next();
                if (b instanceof Title) {
                    applyToTitle((Title) b);
                }
            }
        }
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setBorder(ChartBorder border) {
        this.border = border;
    }

    public void setBorder(boolean visible, Paint paint, BasicStroke stroke) {
        this.border = new BasicChartBorder(visible, stroke, paint);
    }

    public ChartBorder getBorder() {
        return border;
    }
}
