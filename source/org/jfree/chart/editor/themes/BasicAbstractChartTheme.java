package org.jfree.chart.editor.themes;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.util.StringUtil;

import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 13-Nov-2009
 * Time: 11:29:47
 * Functionality that is common to all of the implementations of *Theme interfaces in this package.
 */
public abstract class BasicAbstractChartTheme implements ChartThemeBasis {
    private String name;

    /**
     * The locale currently in use. Not a persistent setting (ie not stored in the XML), but used during publishing.
     */
    private ThreadLocal locale = new ThreadLocal();

    public BasicAbstractChartTheme(String name, String defaultName) {
        this(name, defaultName, true);
    }

    public BasicAbstractChartTheme(String name, String defaultName, boolean doInitDefaults) {
        if(!StringUtil.isEmpty(name)) {
            this.name = name;
        } else {
            this.name = defaultName;
        }
        if(doInitDefaults) {
            initDefaults();
        }
    }

    public BasicAbstractChartTheme(JFreeChart chart, String name, String defaultName) {
        this(chart, name, defaultName, true);
    }

    protected BasicAbstractChartTheme(JFreeChart chart, String name, String defaultName, boolean doReadFromChart) {
        this(name, defaultName, doReadFromChart);
        if (chart == null) {
            throw new IllegalArgumentException("Null 'chart' argument.");
        }
        if(doReadFromChart) {
            readSettingsFromChart(chart);
        }
    }


    /**
     * Returns the name of this theme.
     *
     * @return The name of this theme.
     */
    public String getName() {
        return this.name;
    }

    public void setName(String n) {
        if(!StringUtil.isEmpty(n)) {
            this.name = n;
        }
    }


    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Locale getLocaleForCurrentThread() {
        Locale local = (Locale) locale.get();
        if(local == null) {
            return Locale.getDefault();
        }
        return local;
    }

    public void setLocaleForCurrentThread(Locale locale) {
        this.locale.set(locale);
    }

    protected abstract void initDefaults();
}
