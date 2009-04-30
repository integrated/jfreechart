package org.jfree.chart.editor.themes;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartTheme;

import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 29-Apr-2009
 * Time: 12:02:58
 * To change this template use File | Settings | File Templates.
 */
public interface AbstractiPlusChartTheme extends ChartTheme, Cloneable {
    String getName();

    void setName(String n);

    Locale getLocaleForCurrentThread();

    void setLocaleForCurrentThread(Locale locale);

    void readSettingsFromChart(JFreeChart chart);

    public Object clone() throws CloneNotSupportedException;
}
