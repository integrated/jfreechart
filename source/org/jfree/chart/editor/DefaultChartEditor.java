/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * -----------------------
 * DefaultChartEditor.java
 * -----------------------
 * (C) Copyright 2000-2008, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Arnaud Lelievre;
 *                   Daniel Gredler;
 *
 * Changes
 * -------
 * 24-Nov-2005 : New class, based on ChartPropertyEditPanel.java (DG);
 * 18-Dec-2008 : Use ResourceBundleWrapper - see patch 1607918 by
 *               Jess Thrysoee (DG);
 *
 */

package org.jfree.chart.editor;

import java.awt.*;
import java.util.ResourceBundle;

import javax.swing.*;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.editor.components.InsetPanel;
import org.jfree.chart.editor.components.BorderPanel;
import org.jfree.chart.editor.components.BackgroundEditingPanel;
import org.jfree.chart.editor.themes.ExtendedChartTheme;
import org.jfree.chart.editor.themes.ChartBorder;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.ui.FontChooserPanel;
import org.jfree.ui.RectangleInsets;

/**
 * A panel for editing chart properties (includes subpanels for the title,
 * legend and plot).
 */
public class DefaultChartEditor extends BaseEditor implements ChartEditor {
    /** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
                    "org.jfree.chart.editor.LocalizationBundle");

    /** A panel for displaying/editing the properties of the title. */
    private DefaultChartTitleEditor titleEditor;

    /** A panel for displaying/editing the properties of the plot. */
    private DefaultPlotEditor plotEditor;

    private DefaultLegendEditor legendEditor;

    private JTabbedPane tabs;

    protected ExtendedChartTheme theme;

    /**
     * A checkbox indicating whether or not the chart is drawn with
     * anti-aliasing.
     */
    private JCheckBox antialias;

    /**
     * A checkbox indicating whether or not the chart's text is drawn with
     * anti-aliasing.
     */
    private JCheckBox textAntialias;

    /** Panel to edit border properties */
    private BorderPanel borderPanel;

    /** Component to edit the chart padding values */
    private InsetPanel chartPadding;

    /** The panel that edits the background's properties */
    private BackgroundEditingPanel background;

    private static FontChooserPanel fontChooser;

    static {
        fontChooser = new FontChooserPanel(new Font("Tahoma", Font.PLAIN, 12));
    }

    public static FontChooserPanel getDefaultFontChooserPanel() {
        return fontChooser;
    }

    public static void setDefaultFontChooserPanel(FontChooserPanel panel) {
        fontChooser = panel;
    }

    /**
     * Standard constructor - the property panel is made up of a number of
     * sub-panels that are displayed in the tabbed pane.
     *
     * @param theme The theme that will be edited.
     * @param chart  the chart, whichs properties should be changed.
     */
    public DefaultChartEditor(ExtendedChartTheme theme, JFreeChart chart) {
        this(theme, chart, false);
    }

    /**
     * Standard constructor - the property panel is made up of a number of
     * sub-panels that are displayed in the tabbed pane.
     *
     * @param theme The theme that will be edited.
     * @param chart  the chart, whichs properties should be changed.
     * @param immediateUpdate If true, changes are applied to the chart as they are made without waiting for the OK button.
     */
    public DefaultChartEditor(ExtendedChartTheme theme, JFreeChart chart, boolean immediateUpdate) {
        super(chart, immediateUpdate);
        setLayout(new BorderLayout());

        this.theme = theme;

        // background tab
        background = new BackgroundEditingPanel(theme);
        background.addChangeListener(updateHandler);
        // box tab
        JPanel box = buildBoxTab();

        JPanel general = new JPanel(new BorderLayout());

        JPanel interior = new JPanel(new GridBagLayout());
        GridBagConstraints c = getNewConstraints();
        interior.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        this.antialias = new JCheckBox(localizationResources.getString(
                "Draw_anti-aliased"));
        this.antialias.setSelected(theme.isAntiAliased());
        antialias.addActionListener(updateHandler);
        c.weightx = 1;
        interior.add(this.antialias,c);

        startNewRow(c);
        this.textAntialias = new JCheckBox(localizationResources.getString("Draw_anti-aliased_text"),
                theme.isAntiAliasedText());
        this.textAntialias.addActionListener(updateHandler);
        c.weightx = 1;
        interior.add(this.textAntialias, c);

        startNewRow(c);
        c.weightx = 1;
        background.setBorder(BorderFactory.createTitledBorder(localizationResources.getString("Background")));
        interior.add(background, c);

        startNewRow(c);
        c.weightx = 1;
        interior.add(box, c);

        // component that fills remaining space, pushing the rest to the top of the available space
        // rather than them all being vertically centred.
        startNewRow(c);
        c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
        interior.add(new JPanel(), c);


        general.add(interior, BorderLayout.CENTER);

        JPanel parts = new JPanel(new BorderLayout());

        Plot plot = chart.getPlot();

        tabs = new JTabbedPane();

        tabs.addTab(localizationResources.getString("Chart"), general);
        this.titleEditor = initTitleEditor();
        this.titleEditor.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        tabs.addTab(localizationResources.getString("Title"), this.titleEditor);

        this.plotEditor = initPlotEditor(plot);
        this.plotEditor.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        tabs.addTab(localizationResources.getString("Plot"), this.plotEditor);

        this.legendEditor = initLegendEditor();
        this.legendEditor.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        tabs.addTab(localizationResources.getString("Legend"), this.legendEditor);

        addCustomTabs(tabs);

        parts.add(tabs, BorderLayout.CENTER);
        add(parts);

        theme.apply(chart);
    }

    protected DefaultChartTitleEditor initTitleEditor() {
        return new DefaultChartTitleEditor(theme.getTitleTheme(), chart, this.immediateUpdate);
    }

    protected DefaultPlotEditor initPlotEditor(Plot plot) {
        return new DefaultPlotEditor(theme.getPlotTheme(), chart, plot, this.immediateUpdate);
    }

    protected DefaultLegendEditor initLegendEditor() {
        return new DefaultLegendEditor(theme.getLegendTheme(), chart, this.immediateUpdate);
    }

    public void addTab(String title, Icon icon, Component component, String tip) {
        tabs.addTab(title, icon, component, tip);
    }

    public void removeTabAt(int index) {
        tabs.removeTabAt(index);
    }

    public Component getTabComponentAt(int index) {
        return tabs.getComponentAt(index);
    }

    private JPanel buildBoxTab() {
        JPanel box = new JPanel(new BorderLayout());
        JPanel interior = new JPanel(new GridBagLayout());
        GridBagConstraints c = getNewConstraints();

        ChartBorder border = theme.getBorder();

        borderPanel = new BorderPanel(localizationResources.getString("Border"),
                border.isVisible(), border.getStroke(), border.getPaint());
        borderPanel.addChangeListener(updateHandler);
        c.gridwidth = 3; c.weightx = 1;
        interior.add(borderPanel, c);

        startNewRow(c);
        chartPadding = new InsetPanel(localizationResources.getString("Padding"), theme.getPadding());
        chartPadding.addChangeListener(updateHandler);
        c.gridwidth = 3;
        interior.add(chartPadding, c);

        box.add(interior, BorderLayout.NORTH);
        return box;
    }

    /**
     * Returns a reference to the title editor.
     *
     * @return A panel for editing the title.
     */
    public DefaultChartTitleEditor getTitleEditor() {
      return this.titleEditor;
    }

    /**
     * Returns a reference to the plot property sub-panel.
     *
     * @return A panel for editing the plot properties.
     */
    public DefaultPlotEditor getPlotEditor() {
        return this.plotEditor;
    }

    /**
     * Returns the current setting of the anti-alias flag.
     *
     * @return <code>true</code> if anti-aliasing is enabled.
     */
    public boolean getAntiAlias() {
        return this.antialias.isSelected();
    }

    /**
     * Returns the current background paint.
     *
     * @return The current background paint.
     */
    public Paint getBackgroundPaint() {
        return this.background.getBackgroundPaint();
    }

    /**
     * Updates the properties of a chart to match the properties defined on the
     * panel.
     *
     * @param chart  the chart.
     */
    public void updateChart(JFreeChart chart) {

        this.titleEditor.updateChart(chart);
        this.plotEditor.updateChart(chart);
        this.legendEditor.updateChart(chart);

        boolean antiAlias = getAntiAlias();
        chart.setAntiAlias(antiAlias);
        theme.setAntiAliased(antiAlias);

        boolean textAlias = textAntialias.isSelected();
        chart.setTextAntiAlias(textAlias);
        theme.setAntiAliasedText(textAlias);

        Paint backgroundPaint = getBackgroundPaint();
        chart.setBackgroundPaint(backgroundPaint);
        theme.setChartBackgroundPaint(backgroundPaint);

        borderPanel.apply(chart, theme);

        RectangleInsets padding = chartPadding.getSelectedInsets();
        chart.setPadding(padding);
        theme.setPadding(padding);

        applyCustomEditors(chart);
    }

    public void setLiveUpdates(boolean val) {
        super.setLiveUpdates(val);
        titleEditor.setLiveUpdates(val);
        plotEditor.setLiveUpdates(val);
        legendEditor.setLiveUpdates(val);
    }
}
