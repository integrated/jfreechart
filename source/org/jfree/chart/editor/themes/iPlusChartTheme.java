package org.jfree.chart.editor.themes;

import java.util.Properties;
import java.awt.*;

import org.jfree.ui.RectangleInsets;
import org.jfree.chart.JFreeChart;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 29-Apr-2009
 * Time: 12:05:44
 * To change this template use File | Settings | File Templates.
 */
public interface iPlusChartTheme extends AbstractiPlusChartTheme, EditableBorder {
    String THEME_NAME = "Custom Chart Theme";
    Font DEFAULT_LARGE_FONT = new Font("Tahoma", Font.BOLD, 14);
    Color DEFAULT_TEXT_PAINT = Color.BLACK;
    Color DEFAULT_LABEL_PAINT = Color.DARK_GRAY;
    Color DEFAULT_SHADOW_PAINT = Color.GRAY;
    boolean DEFAULT_SHADOW_VISIBILITY = false;
    Color DEFAULT_BACKGROUND_PAINT = Color.WHITE;
    boolean ANTI_ALIASED_DEFAULT = true;
    BasicStroke DEFAULT_BORDER_STROKE = new BasicStroke(0.5f);
    Paint DEFAULT_BORDER_PAINT = Color.BLACK;
    RectangleInsets DEFAULT_PADDING = RectangleInsets.ZERO_INSETS;
    boolean DEFAULT_DATA_ORIENTATION_IN_COLS = true;

    void setPreferredChartTemplate(String s);

    void setProperty(String property, String value);

    String getProperty(String key);

    Properties getProperties();

    String getPreferredChartTemplate();

    iPlusChartTitleTheme getTitleTheme();

    void setTitleTheme(iPlusChartTitleTheme titleTheme);

    iPlusPlotTheme getPlotTheme();

    void setPlotTheme(iPlusPlotTheme plotTheme);

    iPlusLegendTheme getLegendTheme();

    void setLegendTheme(iPlusLegendTheme legendTheme);

    RectangleInsets getPadding();

    void setPadding(RectangleInsets padding);

    Font getSubtitleFont();

    void setSubtitleFont(Font subtitleFont);

    Paint getSubtitlePaint();

    void setSubtitlePaint(Paint paint);

    Paint getChartBackgroundPaint();

    void setChartBackgroundPaint(Paint paint);

    boolean isAntiAliased();

    void setAntiAliased(boolean antiAliased);

    boolean isAntiAliasedText();

    void setAntiAliasedText(boolean antiAliasedText);

    boolean isSeriesInColumns();

    void setSeriesInColumns(boolean seriesInColumns);

    void applyCustomChartProperties(JFreeChart chart);
}
