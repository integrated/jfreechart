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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.title.Title;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.ui.PaintSample;

/**
 * A panel for editing chart properties (includes subpanels for the title,
 * legend and plot).
 */
public class DefaultChartEditor extends BaseEditor implements ActionListener, ChartEditor {

    /** A panel for displaying/editing the properties of the title. */
    private DefaultTitleEditor titleEditor;

    /** A panel for displaying/editing the properties of the plot. */
    private DefaultPlotEditor plotEditor;

    /**
     * A checkbox indicating whether or not the chart is drawn with
     * anti-aliasing.
     */
    private JCheckBox antialias;

    /** The chart background color. */
    private PaintSample background;

    /** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
                    "org.jfree.chart.editor.LocalizationBundle");

    /**
     * Standard constructor - the property panel is made up of a number of
     * sub-panels that are displayed in the tabbed pane.
     *
     * @param chart  the chart, whichs properties should be changed.
     */
    public DefaultChartEditor(JFreeChart chart) {
        this(chart, false);
    }

    /**
     * Standard constructor - the property panel is made up of a number of
     * sub-panels that are displayed in the tabbed pane.
     *
     * @param chart  the chart, whichs properties should be changed.
     * @param immediateUpdate If true, changes are applied to the chart as they are made without waiting for the OK button.
     */
    public DefaultChartEditor(JFreeChart chart, boolean immediateUpdate) {
        super(chart, immediateUpdate);
        setLayout(new BorderLayout());

        JPanel other = new JPanel(new BorderLayout());
        other.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        JPanel general = new JPanel(new BorderLayout());
        general.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            localizationResources.getString("General")));

        JPanel interior = new JPanel(new GridBagLayout());
        GridBagConstraints c = LayoutHelper.getNewConstraints();
        interior.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        this.antialias = new JCheckBox(localizationResources.getString(
                "Draw_anti-aliased"));
        this.antialias.setSelected(chart.getAntiAlias());
        antialias.addActionListener(updateHandler);
        this.antialias.addActionListener(this);
        interior.add(this.antialias,c);
        c.gridx++;
        interior.add(new JLabel(""),c);
        c.gridx++;
        interior.add(new JLabel(""),c);
        c.gridx=0; c.gridy++;
        interior.add(new JLabel(localizationResources.getString(
                "Background_paint")),c);
        c.gridx++; c.weightx = 1.0;
        this.background = new PaintSample(chart.getBackgroundPaint());
        interior.add(this.background,c);
        c.gridx++;
        JButton button = new JButton(localizationResources.getString(
                "Select..."));
        button.setActionCommand("BackgroundPaint");
        button.addActionListener(updateHandler);
        button.addActionListener(this);
        interior.add(button,c);

        LayoutHelper.startNewRow(c);
        interior.add(new JLabel(localizationResources.getString(
                "Series_Paint")),c);
        c.gridx++; c.weightx=1;
        JTextField info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(false);
        interior.add(info,c);
        c.gridx++;
        button = new JButton(localizationResources.getString("Edit..."));
        button.setEnabled(false);
        interior.add(button,c);

        LayoutHelper.startNewRow(c);
        interior.add(new JLabel(localizationResources.getString(
                "Series_Stroke")),c);
        c.gridx++; c.weightx=1;
        info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(false);
        interior.add(info,c);
        c.gridx++;
        button = new JButton(localizationResources.getString("Edit..."));
        button.setEnabled(false);
        interior.add(button,c);

        LayoutHelper.startNewRow(c);
        interior.add(new JLabel(localizationResources.getString(
                "Series_Outline_Paint")),c);
        c.gridx++; c.weightx=1;
        info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(false);
        interior.add(info,c);
        c.gridx++;
        button = new JButton(localizationResources.getString("Edit..."));
        button.setEnabled(false);
        interior.add(button,c);

        LayoutHelper.startNewRow(c);
        interior.add(new JLabel(localizationResources.getString(
                "Series_Outline_Stroke")),c);
        c.gridx++; c.weightx=1;
        info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(false);
        interior.add(info,c);
        c.gridx++;
        button = new JButton(localizationResources.getString("Edit..."));
        button.setEnabled(false);
        interior.add(button,c);

        general.add(interior, BorderLayout.NORTH);
        other.add(general, BorderLayout.CENTER);

        JPanel parts = new JPanel(new BorderLayout());

        Title title = chart.getTitle();
        Plot plot = chart.getPlot();

        JTabbedPane tabs = new JTabbedPane();

        this.titleEditor = new DefaultTitleEditor(chart, title, this.immediateUpdate);
        this.titleEditor.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        tabs.addTab(localizationResources.getString("Title"), this.titleEditor);

        this.plotEditor = new DefaultPlotEditor(chart, plot, this.immediateUpdate);
        this.plotEditor.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        tabs.addTab(localizationResources.getString("Plot"), this.plotEditor);

        tabs.add(localizationResources.getString("Other"), other);
        parts.add(tabs, BorderLayout.CENTER);
        add(parts);
    }

    /**
     * Returns a reference to the title editor.
     *
     * @return A panel for editing the title.
     */
    public DefaultTitleEditor getTitleEditor() {
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
        return this.background.getPaint();
    }

    /**
     * Handles user interactions with the panel.
     *
     * @param event  a BackgroundPaint action.
     */
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("BackgroundPaint")) {
            attemptModifyBackgroundPaint();
        }
    }

    /**
     * Allows the user the opportunity to select a new background paint.  Uses
     * JColorChooser, so we are only allowing a subset of all Paint objects to
     * be selected (fix later).
     */
    private void attemptModifyBackgroundPaint() {
        Color c;
        c = JColorChooser.showDialog(this, localizationResources.getString(
                "Background_Color"), Color.blue);
        if (c != null) {
            this.background.setPaint(c);
        }
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
        

        chart.setAntiAlias(getAntiAlias());
        chart.setBackgroundPaint(getBackgroundPaint());
//        chart.setBackgroundImage(null);
//        chart.setBackgroundImageAlignment(null);
//        chart.setBackgroundImageAlpha(null);
//        chart.setBorderPaint(null);
//        chart.setBorderStroke(null);
//        chart.setBorderVisible(null);
//        chart.setPadding(null);
//        chart.setSubtitles(null);
//        chart.setTextAntiAlias(null);
//        chart.setTitle(null);
    }

}
