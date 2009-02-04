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
 * ----------------------
 * DefaultPlotEditor.java
 * ----------------------
 * (C) Copyright 2005-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Andrzej Porebski;
 *                   Arnaud Lelievre;
 *                   Daniel Gredler;
 *
 * Changes:
 * --------
 * 24-Nov-2005 : Version 1, based on PlotPropertyEditPanel.java (DG);
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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.ColorBar;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.ContourPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.editor.components.BorderPanel;
import org.jfree.chart.editor.components.BackgroundEditingPanel;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.BooleanUtilities;

/**
 * A panel for editing the properties of a {@link Plot}.
 */
class DefaultPlotEditor extends BaseEditor implements ActionListener {

    /** Orientation constants. */
    private final static String[] orientationNames = {"Vertical", "Horizontal"};
    private final static int ORIENTATION_VERTICAL = 0;
    private final static int ORIENTATION_HORIZONTAL = 1;

    /** The panel to adjust the properties of the plot's background */
    private BackgroundEditingPanel backgroundPanel;

    /**
     * A panel used to display/edit the properties of the domain axis (if any).
     */
    private DefaultAxisEditor domainAxisPropertyPanel;

    /**
     * A panel used to display/edit the properties of the range axis (if any).
     */
    private DefaultAxisEditor rangeAxisPropertyPanel;

    /**
     * A panel used to display/edit the properties of the colorbar axis (if
     * any).
     */
    private DefaultColorBarEditor colorBarAxisPropertyPanel;

    /** The insets for the plot. */
    private RectangleInsets plotInsets;

    /**
     * The orientation for the plot (for <tt>CategoryPlot</tt>s and
     * <tt>XYPlot</tt>s).
     */
    private PlotOrientation plotOrientation;

    /**
     * The orientation combo box (for <tt>CategoryPlot</tt>s and
     * <tt>XYPlot</tt>s).
     */
    private JComboBox orientationCombo;

    /** Whether or not to draw lines between each data point (for
     * <tt>LineAndShapeRenderer</tt>s and <tt>StandardXYItemRenderer</tt>s).
     */
    private Boolean drawLines;

    /**
     * The checkbox for whether or not to draw lines between each data point.
     */
    private JCheckBox drawLinesCheckBox;

    /** Whether or not to draw shapes at each data point (for
     * <tt>LineAndShapeRenderer</tt>s and <tt>StandardXYItemRenderer</tt>s).
     */
    private Boolean drawShapes;

    /**
     * The checkbox for whether or not to draw shapes at each data point.
     */
    private JCheckBox drawShapesCheckBox;

    private BorderPanel plotBorder;

    /** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
                    "org.jfree.chart.editor.LocalizationBundle");

    /**
     * Standard constructor - constructs a panel for editing the properties of
     * the specified plot.
     * <P>
     * In designing the panel, we need to be aware that subclasses of Plot will
     * need to implement subclasses of PlotPropertyEditPanel - so we need to
     * leave one or two 'slots' where the subclasses can extend the user
     * interface.
     *
     * @param chart the chart, which should be changed.
     * @param plot  the plot, which should be changed.
     * @param immediateUpdate Whether changes to GUI controls should immediately be applied.
     */
    public DefaultPlotEditor(JFreeChart chart, Plot plot, boolean immediateUpdate) {
        super(chart, immediateUpdate);

        this.plotInsets = plot.getInsets();
        this.backgroundPanel = new BackgroundEditingPanel(plot);
        if (plot instanceof CategoryPlot) {
            this.plotOrientation = ((CategoryPlot) plot).getOrientation();
        }
        else if (plot instanceof XYPlot) {
            this.plotOrientation = ((XYPlot) plot).getOrientation();
        }
        if (plot instanceof CategoryPlot) {
            CategoryItemRenderer renderer = ((CategoryPlot) plot).getRenderer();
            if (renderer instanceof LineAndShapeRenderer) {
                LineAndShapeRenderer r = (LineAndShapeRenderer) renderer;
                this.drawLines = BooleanUtilities.valueOf(
                        r.getBaseLinesVisible());
                this.drawShapes = BooleanUtilities.valueOf(
                        r.getBaseShapesVisible());
            }
        }
        else if (plot instanceof XYPlot) {
            XYItemRenderer renderer = ((XYPlot) plot).getRenderer();
            if (renderer instanceof StandardXYItemRenderer) {
                StandardXYItemRenderer r = (StandardXYItemRenderer) renderer;
                this.drawLines = BooleanUtilities.valueOf(r.getPlotLines());
                this.drawShapes = BooleanUtilities.valueOf(
                        r.getBaseShapesVisible());
            }
        }

        setLayout(new BorderLayout());

        // create a panel for the settings...
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                plot.getPlotType() + localizationResources.getString(":")
            )
        );

        JPanel general = new JPanel(new BorderLayout());

        JPanel interior = new JPanel(new GridBagLayout());
        GridBagConstraints c = getNewConstraints();
        interior.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        if (this.plotOrientation != null) {
            startNewRow(c);
            boolean isVertical
                = this.plotOrientation.equals(PlotOrientation.VERTICAL);
            int index
                = isVertical ? ORIENTATION_VERTICAL : ORIENTATION_HORIZONTAL;
            interior.add(
                new JLabel(localizationResources.getString("Orientation")), c
            );
            c.gridx++; c.gridwidth = 2; c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            this.orientationCombo = new JComboBox(orientationNames);
            this.orientationCombo.setSelectedIndex(index);
            this.orientationCombo.setActionCommand("Orientation");
            this.orientationCombo.addActionListener(updateHandler);
            this.orientationCombo.addActionListener(this);
            interior.add(this.orientationCombo,c);
        }

        if (this.drawLines != null) {
            startNewRow(c);
            interior.add(
                new JLabel(localizationResources.getString("Draw_lines")), c
            );
            c.gridx++; c.gridwidth = 2; c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            this.drawLinesCheckBox = new JCheckBox();
            this.drawLinesCheckBox.setSelected(this.drawLines.booleanValue());
            this.drawLinesCheckBox.setActionCommand("DrawLines");
            this.drawLinesCheckBox.addActionListener(updateHandler);
            this.drawLinesCheckBox.addActionListener(this);
            interior.add(this.drawLinesCheckBox, c);
        }

        if (this.drawShapes != null) {
            startNewRow(c);
            interior.add(
                new JLabel(localizationResources.getString("Draw_shapes")), c
            );
            c.gridx++; c.gridwidth = 2; c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.NONE;
            this.drawShapesCheckBox = new JCheckBox();
            this.drawShapesCheckBox.setSelected(this.drawShapes.booleanValue());
            this.drawShapesCheckBox.setActionCommand("DrawShapes");
            this.drawShapesCheckBox.addActionListener(updateHandler);
            this.drawShapesCheckBox.addActionListener(this);
            interior.add(this.drawShapesCheckBox, c);
        }

        startNewRow(c);
        c.gridwidth = 3; c.weightx = 1;
        plotBorder = new BorderPanel(localizationResources.getString("Border"),
                plot.isOutlineVisible(), (BasicStroke) plot.getOutlineStroke(), plot.getOutlinePaint());
        plotBorder.addChangeListener(updateHandler);
        interior.add(plotBorder, c);

        startNewRow(c);
        backgroundPanel.setBorder(BorderFactory.createTitledBorder(
                localizationResources.getString("Background")
        ));
        backgroundPanel.addChangeListener(updateHandler);
        c.gridwidth = 3; c.weightx = 1;
        interior.add(backgroundPanel,c);

        general.add(interior, BorderLayout.NORTH);

        JPanel appearance = new JPanel(new BorderLayout());
        appearance.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        appearance.add(general, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        Axis domainAxis = null;
        if (plot instanceof CategoryPlot) {
            domainAxis = ((CategoryPlot) plot).getDomainAxis();
        }
        else if (plot instanceof XYPlot) {
            domainAxis = ((XYPlot) plot).getDomainAxis();
        }
        this.domainAxisPropertyPanel
            = DefaultAxisEditor.getInstance(chart, domainAxis, immediateUpdate);
        if (this.domainAxisPropertyPanel != null) {
            this.domainAxisPropertyPanel.setBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
            );
            tabs.add(
                localizationResources.getString("Domain_Axis"),
                this.domainAxisPropertyPanel
            );
        }

        Axis rangeAxis = null;
        if (plot instanceof CategoryPlot) {
            rangeAxis = ((CategoryPlot) plot).getRangeAxis();
        }
        else if (plot instanceof XYPlot) {
            rangeAxis = ((XYPlot) plot).getRangeAxis();
        }

        this.rangeAxisPropertyPanel
            = DefaultAxisEditor.getInstance(chart, rangeAxis, immediateUpdate);
        if (this.rangeAxisPropertyPanel != null) {
            this.rangeAxisPropertyPanel.setBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
            );
            tabs.add(
                localizationResources.getString("Range_Axis"),
                this.rangeAxisPropertyPanel
            );
        }

//dmo: added this panel for colorbar control. (start dmo additions)
        ColorBar colorBar = null;
        if (plot instanceof ContourPlot) {
            colorBar = ((ContourPlot) plot).getColorBar();
        }

        this.colorBarAxisPropertyPanel
            = DefaultColorBarEditor.getInstance(chart, colorBar, immediateUpdate);
        if (this.colorBarAxisPropertyPanel != null) {
            this.colorBarAxisPropertyPanel.setBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2)
            );
            tabs.add(
                localizationResources.getString("Color_Bar"),
                this.colorBarAxisPropertyPanel
            );
        }
//dmo: (end dmo additions)

        tabs.add(localizationResources.getString("Appearance"), appearance);
        panel.add(tabs);

        add(panel);
    }

    /**
     * Returns the current plot insets.
     *
     * @return The current plot insets.
     */
    public RectangleInsets getPlotInsets() {
        if (this.plotInsets == null) {
            this.plotInsets = new RectangleInsets(0.0, 0.0, 0.0, 0.0);
        }
        return this.plotInsets;
    }

    /**
     * Returns the current outline stroke.
     *
     * @return The current outline stroke.
     */
    public Stroke getOutlineStroke() {
        return this.plotBorder.getBorderStroke();
    }

    /**
     * Returns the current outline paint.
     *
     * @return The current outline paint.
     */
    public Paint getOutlinePaint() {
        return this.plotBorder.getBorderPaint();
    }

    /**
     * Returns a reference to the panel for editing the properties of the
     * domain axis.
     *
     * @return A reference to a panel.
     */
    public DefaultAxisEditor getDomainAxisPropertyEditPanel() {
        return this.domainAxisPropertyPanel;
    }

    /**
     * Returns a reference to the panel for editing the properties of the
     * range axis.
     *
     * @return A reference to a panel.
     */
    public DefaultAxisEditor getRangeAxisPropertyEditPanel() {
        return this.rangeAxisPropertyPanel;
    }

    /**
     * Handles user actions generated within the panel.
     * @param event     the event
     */
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("Orientation")) {
            attemptOrientationSelection();
        }
        else if (command.equals("DrawLines")) {
            attemptDrawLinesSelection();
        }
        else if (command.equals("DrawShapes")) {
            attemptDrawShapesSelection();
        }
    }

    /**
     * Allow the user to modify the plot orientation if this is an editor for a
     * <tt>CategoryPlot</tt> or a <tt>XYPlot</tt>.
     */
    private void attemptOrientationSelection() {

        int index = this.orientationCombo.getSelectedIndex();

        if (index == ORIENTATION_VERTICAL) {
            this.plotOrientation = PlotOrientation.VERTICAL;
        }
        else {
            this.plotOrientation = PlotOrientation.HORIZONTAL;
        }
    }

    /**
     * Allow the user to modify whether or not lines are drawn between data
     * points by <tt>LineAndShapeRenderer</tt>s and
     * <tt>StandardXYItemRenderer</tt>s.
     */
    private void attemptDrawLinesSelection() {
        this.drawLines = BooleanUtilities.valueOf(
                this.drawLinesCheckBox.isSelected());
    }

    /**
     * Allow the user to modify whether or not shapes are drawn at data points
     * by <tt>LineAndShapeRenderer</tt>s and <tt>StandardXYItemRenderer</tt>s.
     */
    private void attemptDrawShapesSelection() {
        this.drawShapes = BooleanUtilities.valueOf(
                this.drawShapesCheckBox.isSelected());
    }

    /**
     * Updates the plot properties to match the properties defined on the panel.
     *
     * @param chart  The chart.
     */
    public void updateChart(JFreeChart chart) {
        Plot plot = chart.getPlot();
        // set the plot properties...
        plot.setOutlineVisible(plotBorder.isBorderVisible());
        plot.setOutlinePaint(getOutlinePaint());
        plot.setOutlineStroke(getOutlineStroke());
        plot.setInsets(getPlotInsets());

        plot.setBackgroundAlpha(backgroundPanel.getBackgroundAlpha());
        plot.setBackgroundPaint(backgroundPanel.getBackgroundPaint());

        // then the axis properties...
        if (this.domainAxisPropertyPanel != null) {
            Axis domainAxis = null;
            if (plot instanceof CategoryPlot) {
                CategoryPlot p = (CategoryPlot) plot;
                domainAxis = p.getDomainAxis();
            }
            else if (plot instanceof XYPlot) {
                XYPlot p = (XYPlot) plot;
                domainAxis = p.getDomainAxis();
            }
            if (domainAxis != null) {
                this.domainAxisPropertyPanel.updateChart(chart);
            }
        }

        if (this.rangeAxisPropertyPanel != null) {
            Axis rangeAxis = null;
            if (plot instanceof CategoryPlot) {
                CategoryPlot p = (CategoryPlot) plot;
                rangeAxis = p.getRangeAxis();
            }
            else if (plot instanceof XYPlot) {
                XYPlot p = (XYPlot) plot;
                rangeAxis = p.getRangeAxis();
            }
            if (rangeAxis != null) {
                this.rangeAxisPropertyPanel.updateChart(chart);
            }
        }

        if (this.plotOrientation != null) {
            if (plot instanceof CategoryPlot) {
                CategoryPlot p = (CategoryPlot) plot;
                p.setOrientation(this.plotOrientation);
            }
            else if (plot instanceof XYPlot) {
                XYPlot p = (XYPlot) plot;
                p.setOrientation(this.plotOrientation);
            }
        }

        if (this.drawLines != null) {
            if (plot instanceof CategoryPlot) {
                CategoryPlot p = (CategoryPlot) plot;
                CategoryItemRenderer r = p.getRenderer();
                if (r instanceof LineAndShapeRenderer) {
                    ((LineAndShapeRenderer) r).setLinesVisible(
                            this.drawLines.booleanValue());
                }
            }
            else if (plot instanceof XYPlot) {
                XYPlot p = (XYPlot) plot;
                XYItemRenderer r = p.getRenderer();
                if (r instanceof StandardXYItemRenderer) {
                    ((StandardXYItemRenderer) r).setPlotLines(
                        this.drawLines.booleanValue()
                    );
                }
            }
        }

        if (this.drawShapes != null) {
            if (plot instanceof CategoryPlot) {
                CategoryPlot p = (CategoryPlot) plot;
                CategoryItemRenderer r = p.getRenderer();
                if (r instanceof LineAndShapeRenderer) {
                    ((LineAndShapeRenderer) r).setShapesVisible(
                            this.drawShapes.booleanValue());
                }
            }
            else if (plot instanceof XYPlot) {
                XYPlot p = (XYPlot) plot;
                XYItemRenderer r = p.getRenderer();
                if (r instanceof StandardXYItemRenderer) {
                    ((StandardXYItemRenderer) r).setBaseShapesVisible(
                        this.drawShapes.booleanValue());
                }
            }
        }

//dmo: added this panel for colorbar control. (start dmo additions)
        if (this.colorBarAxisPropertyPanel != null) {
            ColorBar colorBar = null;
            if (plot instanceof  ContourPlot) {
                ContourPlot p = (ContourPlot) plot;
                colorBar = p.getColorBar();
            }
            if (colorBar != null) {
                this.colorBarAxisPropertyPanel.updateChart(chart);
            }
        }
//dmo: (end dmo additions)

    }

}
