package org.jfree.chart.editor.themes;

import org.jfree.ui.HorizontalAlignment;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 13-Nov-2009
 * Time: 11:39:54
 * Implementation of the iPlusChartTitleTheme interfacce.
 */
public class BasicChartTitleTheme extends BasicAbstractTitleTheme implements ChartTitleTheme {

    private static final String DEFAULT_THEME_NAME = "Custom Title Theme";

    private static final Font DEFAULT_TITLE_FONT = new Font("Tahoma", Font.BOLD, 20);
    private static final Color DEFAULT_TEXT_PAINT = Color.BLACK;
    private static final Paint DEFAULT_BACKGROUND_PAINT = null;
    private static final boolean DEFAULT_TITLE_VISIBILITY = true;
    private static final boolean DEFAULT_TITLE_EXPAND = false;
    private static final String DEFAULT_CHART_TITLE = "Title";


    private Font titleFont;
    /** The paint used to display the main chart title. */
    private Paint titlePaint;

    /** The chart's title */
    private String chartTitle;

    /** The paint for the title's background */
    private Paint titleBackground;

    /** Whether to show the title at all */
    private boolean titleVisible;

    /** Whether the box containining the title should expand to fit the edge - this box determines where borders and
     * backgrounds are drawn */
    private boolean expandTitle;



    /* How the title's text will be aligned when it wraps. */
    private HorizontalAlignment textAlignment;

    public BasicChartTitleTheme(String name) {
        super(name, DEFAULT_THEME_NAME);
    }

    public BasicChartTitleTheme(JFreeChart chart, String name) {
        super(chart, name, DEFAULT_THEME_NAME);
    }

    protected void initDefaults() {
        super.initDefaults();
        this.titleFont = DEFAULT_TITLE_FONT;
        this.titlePaint = DEFAULT_TEXT_PAINT;
        this.titleVisible = DEFAULT_TITLE_VISIBILITY;
        this.expandTitle = DEFAULT_TITLE_EXPAND;
        this.chartTitle = DEFAULT_CHART_TITLE;
        this.titleBackground = DEFAULT_BACKGROUND_PAINT;

        this.textAlignment = DEFAULT_HORIZONTAL_ALIGNMENT;
    }

    public void readSettingsFromChart(JFreeChart chart) {
        super.readSettingsFromChart(chart);
        TextTitle title = chart.getTitle();
        this.titleVisible = title != null;
        if(titleVisible) {
            this.titleFont = title.getFont();
            this.titlePaint = title.getPaint();
            this.expandTitle = title.getExpandToFitSpace();
            this.titleBackground = title.getBackgroundPaint();
            this.chartTitle = title.getText();
            this.textAlignment = title.getTextAlignment();
        }
    }

    protected Title getTitleObject(JFreeChart chart) {
        return chart.getTitle();
    }


    public Font getTitleFont() {
        return this.titleFont;
    }

    public void setTitleFont(Font font) {
        if (font == null) {
            throw new IllegalArgumentException("Null font argument.");
        }
        this.titleFont = font;
    }

    public Paint getTitlePaint() {
        return this.titlePaint;
    }

    public void setTitlePaint(Paint paint) {
        if (paint == null) {
            throw new IllegalArgumentException("Null 'paint' argument.");
        }
        this.titlePaint = paint;
    }

    public String getChartTitle() {
        return chartTitle;
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    public Paint getTitleBackground() {
        return titleBackground;
    }

    public void setTitleBackground(Paint titleBackground) {
        this.titleBackground = titleBackground;
    }

    public boolean isTitleVisible() {
        return titleVisible;
    }

    public void setTitleVisible(boolean titleVisible) {
        this.titleVisible = titleVisible;
    }

    public boolean isExpandTitle() {
        return expandTitle;
    }

    public void setExpandTitle(boolean expandTitle) {
        this.expandTitle = expandTitle;
    }

    public HorizontalAlignment getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(HorizontalAlignment textAlignment) {
        this.textAlignment = textAlignment;
    }

    public void apply(JFreeChart chart) {
        TextTitle title = chart.getTitle();
        if (titleVisible) {
            if(title == null) {
                title = new TextTitle();
                chart.setTitle(title);
            }
            title.setFont(this.titleFont);
            title.setPaint(this.titlePaint);
            title.setExpandToFitSpace(expandTitle);
            title.setText(chartTitle);
            title.setBackgroundPaint(titleBackground);
            title.setTextAlignment(textAlignment);
        } else {
            chart.setTitle((TextTitle) null);
        }
        super.apply(chart);
    }

}
