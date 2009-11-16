package org.jfree.chart.editor.themes;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartTheme;

import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 29-Apr-2009
 * Time: 12:02:58
 * Basic methods that all theme implementations should support.
 */
public interface ChartThemeBasis extends ChartTheme, Cloneable {
    String getName();

    void setName(String n);

    Locale getLocaleForCurrentThread();

    void setLocaleForCurrentThread(Locale locale);

    void readSettingsFromChart(JFreeChart chart);

    public Object clone() throws CloneNotSupportedException;
}
