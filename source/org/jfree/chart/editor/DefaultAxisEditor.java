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
 * DefaultAxisEditor.java
 * ----------------------
 * (C) Copyright 2005-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert;
 * Contributor(s):   Andrzej Porebski;
 *                   Arnaud Lelievre;
 *
 * Changes
 * -------
 * 24-Nov-2005 : Version 1, based on AxisPropertyEditPanel.java (DG);
 * 18-Dec-2008 : Use ResourceBundleWrapper - see patch 1607918 by
 *               Jess Thrysoee (DG);
 *
 */

package org.jfree.chart.editor;

import java.awt.*;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import javax.swing.*;

import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.*;
import org.jfree.chart.editor.components.FontControl;
import org.jfree.chart.editor.components.PaintControl;
import org.jfree.chart.editor.components.LineEditorPanel;
import org.jfree.chart.editor.themes.AxisTheme;

/**
 * A panel for editing the properties of an axis.
 */
public class DefaultAxisEditor extends BaseEditor {

    protected static final AxisLocation[] DOMAIN_AXIS_POSITIONS = new AxisLocation[]{
            AxisLocation.BOTTOM_OR_LEFT, AxisLocation.TOP_OR_RIGHT
    };

    protected static final AxisLocation[] RANGE_AXIS_POSITIONS = new AxisLocation[]{
            AxisLocation.TOP_OR_LEFT, AxisLocation.BOTTOM_OR_RIGHT
    };

    protected static final Map/*<AxisLocation, Map<PlotOrientation, String>>*/ DOMAIN_AXIS_POSITION_LABELS;
    static {
        Map retVal = new HashMap();

        Map bottomLeft = new HashMap();
        bottomLeft.put(PlotOrientation.HORIZONTAL, "West");
        bottomLeft.put(PlotOrientation.VERTICAL, "South");
        bottomLeft = Collections.unmodifiableMap(bottomLeft);

        Map topRight = new HashMap();
        topRight.put(PlotOrientation.HORIZONTAL, "East");
        topRight.put(PlotOrientation.VERTICAL, "North");
        topRight = Collections.unmodifiableMap(topRight);

        retVal.put(AxisLocation.BOTTOM_OR_LEFT, bottomLeft);
        retVal.put(AxisLocation.TOP_OR_RIGHT, topRight);

        DOMAIN_AXIS_POSITION_LABELS = Collections.unmodifiableMap(retVal);
    }

    protected static final Map/*<AxisLocation, Map<PlotOrientation, String>>*/ RANGE_AXIS_POSITION_LABELS;
    static {
        Map retVal = new HashMap();

        Map topLeft = new HashMap();
        topLeft.put(PlotOrientation.HORIZONTAL, "North");
        topLeft.put(PlotOrientation.VERTICAL, "West");
        topLeft = Collections.unmodifiableMap(topLeft);

        Map bottomRight = new HashMap();
        bottomRight.put(PlotOrientation.HORIZONTAL, "South");
        bottomRight.put(PlotOrientation.VERTICAL, "East");
        bottomRight = Collections.unmodifiableMap(bottomRight);

        retVal.put(AxisLocation.TOP_OR_LEFT, topLeft);
        retVal.put(AxisLocation.BOTTOM_OR_RIGHT, bottomRight);

        RANGE_AXIS_POSITION_LABELS = Collections.unmodifiableMap(retVal);
    }

    protected AxisTheme theme;

    /** The axis label. */
    private JTextField label;

    /** Used to select the font for the title */
    private FontControl labelFontControl;

    private PaintControl labelPaintControl;


    /** Used to select the font for the title */
    private FontControl tickFontControl;

    private PaintControl tickPaintControl;

    private JSpinner labelAngle;

    private LineEditorPanel axisLine;

    private JComboBox axisLocation;

    // set from outside the class.
    private PlotOrientation plotOrientation;

    /**
     * An empty sub-panel for extending the user interface to handle more
     * complex axes.
     */
    private JPanel slot1;

    /**
     * An empty sub-panel for extending the user interface to handle more
     * complex axes.
     */
    private JPanel slot2;

    /** A flag that indicates whether or not the tick labels are visible. */
    private JCheckBox showTickLabelsCheckBox;

    /** A flag that indicates whether or not the tick marks are visible. */
    private JCheckBox showTickMarksCheckBox;

    /** A tabbed pane for... */
    private JTabbedPane otherTabs;

    /** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
                    "org.jfree.chart.editor.LocalizationBundle");
    private static final String ERROR = "Error";


    protected final boolean isRange;

    protected final boolean isXYDescendent;

    protected final boolean isDomainRangeImpl;

    /**
     * Standard constructor: builds a panel for displaying/editing the
     * properties of the specified axis.
     * @param theme The theme that will be edited.
     * @param chart The chart the axis belongs to.
     * @param immediateUpdate Whether changes to GUI controls should immediately alter the chart
     */
    public DefaultAxisEditor(AxisTheme theme, JFreeChart chart, boolean immediateUpdate) {
        super(chart, immediateUpdate);
        this.theme = theme;

        Plot p = chart.getPlot();

        isRange = theme.getType() == AxisTheme.RANGE_AXIS;
        isDomainRangeImpl = p instanceof DomainRangePlot;
        isXYDescendent = p instanceof XYPlot;


        setLayout(new GridBagLayout());
        if(isDomainRangeImpl) {
            this.plotOrientation = ((DomainRangePlot)p).getOrientation();
        }

        JPanel positionPanel = getPositionPanel();

        JPanel labelPanel = getLabelPanel();
        axisLine = new LineEditorPanel(localizationResources.getString("Axis_Line"),
                theme.isLineVisible(), theme.getLinePaint(), theme.getLineStroke());
        axisLine.addChangeListener(updateHandler);
        
        this.slot1 = new JPanel(new BorderLayout());

        JPanel other = new JPanel(new BorderLayout());
        other.setBorder(BorderFactory.createTitledBorder(
                             localizationResources.getString("Other")));

        this.otherTabs = new JTabbedPane();
        this.otherTabs.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        JPanel ticks = getTicksPanel(theme);

        this.otherTabs.add(localizationResources.getString("Ticks"), ticks);

        other.add(this.otherTabs);

        addCustomTabs(this.otherTabs);

        this.slot1.add(other);

        this.slot2 = new JPanel(new BorderLayout());
        this.slot2.add(this.slot1, BorderLayout.NORTH);

        GridBagConstraints c = getNewConstraints();
        c.weightx = 1;
        add(positionPanel, c);
        startNewRow(c);
        c.weightx = 1;
        add(labelPanel, c);
        startNewRow(c);
        c.weightx = 1;
        add(axisLine, c);
        addSubclassAxisControls(c);
        startNewRow(c);
        c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
        add(this.slot2, c);

    }

    protected void addSubclassAxisControls(GridBagConstraints c) {
        // do nothing. This should be over-ridden by subclasses to add components below the axisLine LineEditorPanel
    }

    private JPanel getPositionPanel() {
        AxisLocation themeLocation = theme.getAxisLocation();
        JPanel retVal = new JPanel(new GridBagLayout());
        retVal.setBorder(BorderFactory.createTitledBorder(localizationResources.getString("Axis_Position")));

        GridBagConstraints c = getNewConstraints();
        retVal.add(new JLabel(localizationResources.getString("Edge")), c);
        c.gridx++; c.weightx = 1;

        Object[] comboContents = theme.getType() == AxisTheme.DOMAIN_AXIS ? DOMAIN_AXIS_POSITIONS : RANGE_AXIS_POSITIONS;
        this.axisLocation = new JComboBox(comboContents);
        this.axisLocation.setSelectedItem(themeLocation);
        this.axisLocation.addActionListener(updateHandler);
        this.axisLocation.setRenderer(new AxisLocationRenderer());
        retVal.add(this.axisLocation, c);

        return retVal;
    }

    private JPanel getLabelPanel() {
        Font labelFont = theme.getAxisLabelFont();
        JPanel labelPanel = new JPanel(new GridBagLayout());
        labelPanel.setBorder(
            BorderFactory.createTitledBorder(
                localizationResources.getString("Label")
            )
        );

        GridBagConstraints c = getNewConstraints();
        labelPanel.add(new JLabel(localizationResources.getString("Text")), c);
        c.gridx++; c.gridwidth = 2;
        this.label = new JTextField(theme.getAxisLabel());
        this.label.addActionListener(updateHandler);
        this.label.getDocument().addDocumentListener(updateHandler);
        labelPanel.add(this.label, c);

        startNewRow(c);
        labelPanel.add(new JLabel(localizationResources.getString("Font")),c);
        this.labelFontControl = compFactory.getFontControl(labelFont);
        this.labelFontControl.addChangeListener(updateHandler);
        c.gridx++; c.weightx = 1; c.gridwidth = 2;
        labelPanel.add(this.labelFontControl,c);

        startNewRow(c);
        labelPanel.add(new JLabel(localizationResources.getString("Paint")),c);
        this.labelPaintControl = compFactory.getPaintControl(theme.getAxisLabelPaint());
        this.labelPaintControl.addChangeListener(updateHandler);
        c.gridx++; c.weightx = 1; c.gridwidth = 2;
        labelPanel.add(this.labelPaintControl,c);

        startNewRow(c);
        this.labelAngle = new JSpinner(new SpinnerNumberModel(theme.getLabelAngleDegs(), 0, 360, 1.0));
        labelAngle.addChangeListener(updateHandler);
        labelPanel.add(new JLabel(localizationResources.getString("Label_angle")), c);
        c.gridx++; c.gridwidth = 2; c.fill= GridBagConstraints.NONE;  c.anchor = GridBagConstraints.WEST;
        labelPanel.add(labelAngle, c);

        return labelPanel;
    }

    private JPanel getTicksPanel(AxisTheme theme) {
        GridBagConstraints c;
        JPanel ticks = new JPanel(new GridBagLayout());
        c = getNewConstraints();
        ticks.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        this.showTickLabelsCheckBox = new JCheckBox(
            localizationResources.getString("Show_tick_labels"),
            theme.isShowTickLabels()
        );
        this.showTickLabelsCheckBox.addActionListener(updateHandler);
        c.gridwidth = 3;
        ticks.add(this.showTickLabelsCheckBox, c);
        
        addAxisTypeSpecificTickControls(ticks, c);

        startNewRow(c);
        ticks.add(
            new JLabel(localizationResources.getString("Font")), c
        );
        this.tickFontControl = compFactory.getFontControl(theme.getTickLabelFont());
        this.tickFontControl.addChangeListener(updateHandler);
        c.gridx++;
        c.weightx = 1;
        c.gridwidth = 2;
        ticks.add(this.tickFontControl,c);
        
        startNewRow(c);
        ticks.add(
            new JLabel(localizationResources.getString("Paint")), c
        );
        this.tickPaintControl = compFactory.getPaintControl(theme.getTickLabelPaint());
        this.tickPaintControl.addChangeListener(updateHandler);
        c.gridx++;
        c.weightx = 1;
        c.gridwidth = 2;
        ticks.add(this.tickPaintControl,c);

        startNewRow(c);
        c.gridwidth = 3;
        this.showTickMarksCheckBox = new JCheckBox(
            localizationResources.getString("Show_tick_marks"),
            theme.isShowTickMarks()
        );
        this.showTickMarksCheckBox.addActionListener(updateHandler);
        ticks.add(this.showTickMarksCheckBox,c);

        return ticks;
    }

    protected void addAxisTypeSpecificTickControls(JPanel ticks, GridBagConstraints c) {
        // do nothing. Used in sub-classes.
    }

    private String getAxisPositionLabel(Object value) {
        Map toUse;
        if(theme.getType() == AxisTheme.DOMAIN_AXIS) {
            toUse = DOMAIN_AXIS_POSITION_LABELS;
        } else {
            toUse = RANGE_AXIS_POSITION_LABELS;
        }

        Map innerMap = (Map) toUse.get(value);
        if(innerMap == null) {
            return ERROR;
        }

        String retVal = (String) innerMap.get(plotOrientation);
        if(retVal == null) {
            retVal = ERROR;
        }
        return localizationResources.getString(retVal);
    }
    
    void plotOrientationChanged(PlotOrientation plotOrientation) {
        // this changes the labels that describe the edge of the plot along which the axis will be drawn.
        this.plotOrientation = plotOrientation;
    }

    /**
     * Returns the current axis label.
     *
     * @return The current axis label.
     */
    public String getLabel() {
        return this.label.getText();
    }

    /**
     * Returns the current label font.
     *
     * @return The current label font.
     */
    public Font getLabelFont() {
        return this.labelFontControl.getChosenFont();
    }

    /**
     * Returns the current label paint.
     *
     * @return The current label paint.
     */
    public Paint getLabelPaint() {
        return this.labelPaintControl.getChosenPaint();
    }

    /**
     * Returns a flag that indicates whether or not the tick labels are visible.
     *
     * @return <code>true</code> if ick mark labels are visible.
     */
    public boolean isTickLabelsVisible() {
        return this.showTickLabelsCheckBox.isSelected();
    }

    /**
     * Returns the font used to draw the tick labels (if they are showing).
     *
     * @return The font used to draw the tick labels.
     */
    public Font getTickLabelFont() {
        return this.tickFontControl.getChosenFont();
    }

    /**
     * Returns the paint used to draw the tick labels (if they are showing).
     *
     * @return The paint used to draw the tick labels.
     */
    public Paint getTickLabelPaint() {
        return this.tickPaintControl.getChosenPaint();
    }

    /**
     * Returns the current value of the flag that determines whether or not
     * tick marks are visible.
     *
     * @return <code>true</code> if tick marks are visible.
     */
    public boolean isTickMarksVisible() {
        return this.showTickMarksCheckBox.isSelected();
    }

    /**
     * Returns a reference to the tabbed pane.
     *
     * @return A reference to the tabbed pane.
     */
    public JTabbedPane getOtherTabs() {
        return this.otherTabs;
    }

    public AxisLocation getAxisLocation() {
        return (AxisLocation) axisLocation.getSelectedItem();
    }

    public double getLabelAngleInDegs() {
        return ((Number)labelAngle.getModel().getValue()).doubleValue();
    }

    /**
     * Sets the properties of the specified axis to match the properties
     * defined on this panel.
     *
     * @param chart The chart.
     */
    public void updateChart(JFreeChart chart) {
        Axis[] axes = getAxes(chart, theme);

        applyToAxes(axes);
        applyCustomEditors(chart);
    }

    protected void applyToAxes(Axis[] axes) {

        theme.setAxisLabel(getLabel());
        theme.setAxisLabelFont(getLabelFont());
        theme.setAxisLabelPaint(getLabelPaint());
        theme.setTickLabelFont(getTickLabelFont());
        Paint tickLabelPaint = getTickLabelPaint();
        theme.setTickLabelPaint(tickLabelPaint);
        theme.setShowTickLabels(isTickLabelsVisible());
        theme.setShowTickMarks(isTickMarksVisible());
        double angle = getLabelAngleInDegs();
        theme.setLabelAngleDegs(angle);

        boolean lineVis = axisLine.isLineVisible();
        theme.setLineVisible(lineVis);
        Paint linePaint = axisLine.getLinePaint();
        theme.setLinePaint(linePaint);
        BasicStroke lineStroke = axisLine.getLineStroke();
        theme.setLineStroke(lineStroke);

        AxisLocation axisLocation = getAxisLocation();
        if(axisLocation != null) {
            theme.setAxisLocation(axisLocation);

            if(axes.length > 0) {
                Plot p = axes[0].getPlot();
                boolean isDomain = theme.getType() == AxisTheme.DOMAIN_AXIS;
                if(p instanceof DomainRangePlot) {
                    DomainRangePlot drp = (DomainRangePlot) p;
                    if(isDomain) {
                        drp.setDomainAxisLocation(axisLocation);
                    } else {
                        drp.setRangeAxisLocation(axisLocation);
                    }
                }
            }
        }

        for(int i = 0; i < axes.length; i++) {
            axes[i].setLabel(getLabel());
            axes[i].setLabelFont(getLabelFont());
            axes[i].setLabelPaint(getLabelPaint());
            axes[i].setTickMarksVisible(isTickMarksVisible());
            axes[i].setTickLabelsVisible(isTickLabelsVisible());
            axes[i].setTickLabelFont(getTickLabelFont());
            axes[i].setTickLabelPaint(getTickLabelPaint());
            axes[i].setLabelAngle(Math.toRadians(angle));
            axes[i].setAxisLineVisible(lineVis);
            axes[i].setAxisLinePaint(linePaint);
            axes[i].setAxisLineStroke(lineStroke);
        }
    }

    /**
     * Returns all the range/domain axes of the chart as an array. Whether to return the range or domain
     * is determined by the type attribute of the axis theme.
     * @param chart The chart to extract axes from.
     * @param theme The theme that needs the axes.
     * @return All the relevant axes, or an empty array. Empty array definitely returned if the chart plot is not
     * either a CategoryPlot or an XYPlot.
     */
    public static Axis[] getAxes(JFreeChart chart, AxisTheme theme) {
        Axis[] axes = new Axis[0];
        boolean domain = theme.getType() == AxisTheme.DOMAIN_AXIS;
        Plot plot = chart.getPlot();

        if(plot instanceof CategoryPlot) {
            CategoryPlot cPlot = (CategoryPlot) plot;
            axes = new Axis[domain ? cPlot.getDomainAxisCount() : cPlot.getRangeAxisCount()];
            for(int i = 0; i < axes.length; i++) {
                axes[i] = domain ? (Axis) cPlot.getDomainAxis(i) : cPlot.getRangeAxis(i);
            }
        } else if (plot instanceof XYPlot) {
            XYPlot xPlot = (XYPlot) plot;
            axes = new Axis[domain ? xPlot.getDomainAxisCount() : xPlot.getRangeAxisCount()];
            for(int i = 0; i < axes.length; i++) {
                axes[i] = domain ? (Axis) xPlot.getDomainAxis(i) : xPlot.getRangeAxis(i);
            }
        }

        return axes;
    }

    class AxisLocationRenderer extends JLabel implements ListCellRenderer {

        AxisLocationRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            String text = getAxisPositionLabel(value);
            setText(text);
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());

            return this;
        }

    }
}
