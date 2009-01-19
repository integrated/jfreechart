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
 * DefaultTitleEditor.java
 * -----------------------
 * (C) Copyright 2005-2008, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Arnaud Lelievre;
 *                   Daniel Gredler;
 *
 * Changes
 * -------
 * 24-Nov-2005 : Version 1, based on TitlePropertyEditPanel.java (DG);
 * 18-Dec-2008 : Use ResourceBundleWrapper - see patch 1607918 by
 *               Jess Thrysoee (DG);
 *
 */

package org.jfree.chart.editor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.*;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.editor.components.*;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;

/**
 * A panel for editing the properties of a chart title.
 */
class DefaultTitleEditor extends BaseEditor implements ActionListener {

    /** Whether or not to display the title on the chart. */
    private boolean showTitle;

    /** The checkbox to indicate whether or not to display the title. */
    private JCheckBox showTitleCheckBox;

    /** Checkbox to indicate whether the title's frame will expand to fit the available space on
     * its edge of the chart */
    private JCheckBox titleExpands;

    /** A field for displaying/editing the title text. */
    private JTextField titleField;

    /** Used to select the paint for the title text */
    private PaintControl fontPaintControl;

    /** Used to select the font for the title */
    private FontControl fontControl;

    /** Edit the background color of the title */
    private PaintControl backgroundPaintControl;

    /** The tabs that group the title's properties */
    private JTabbedPane tabs;

    /** Controls the border of the title, which can be colored */
    private InsetPanel borderPanel;

    /** Controls the margin of the title */
    private InsetPanel marginPanel;

    /** Controls the padding of the title */
    private InsetPanel paddingPanel;

    /** Select the edge of the chart that holds the title */
    private PositionComboBox posCombo;

    /** Select the horizontal alignment for the title */
    private HorizontalAlignmentComboBox horizontalAlign = new HorizontalAlignmentComboBox();

    /** Select the vertical alignment for the title */
    private VerticalAlignmentComboBox verticalAlign = new VerticalAlignmentComboBox();

    /** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
                    "org.jfree.chart.editor.LocalizationBundle");

    /**
     * Standard constructor: builds a panel for displaying/editing the
     * properties of the specified title.
     *
     * @param chart The chart that is being edited.
     * @param title  the title, which should be changed.
     * @param immediateUpdate Whether changes should be applied immediately.
     */
    public DefaultTitleEditor(JFreeChart chart, Title title, boolean immediateUpdate) {
        super(chart, immediateUpdate);

        TextTitle t = (title != null ? (TextTitle) title
                : new TextTitle(localizationResources.getString("Title")));
        this.showTitle = (title != null);
        this.titleField = new JTextField(t.getText());
        this.fontPaintControl = new PaintControl(t.getPaint());

        setLayout(new BorderLayout());

        JPanel general = new JPanel(new BorderLayout());

        JPanel interior = new JPanel(new GridBagLayout());
        GridBagConstraints c = getNewConstraints();
        interior.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        interior.add(new JLabel(localizationResources.getString("Show_Title")),c);
        c.gridx++; c.gridwidth = 2; c.anchor = GridBagConstraints.WEST;
        this.showTitleCheckBox = new JCheckBox();
        this.showTitleCheckBox.setSelected(this.showTitle);
        this.showTitleCheckBox.setActionCommand("ShowTitle");
        this.showTitleCheckBox.addActionListener(updateHandler);
        this.showTitleCheckBox.addActionListener(this);
        interior.add(this.showTitleCheckBox,c);

        startNewRow(c);
        interior.add(new JLabel(localizationResources.getString("Expand_to_fit")+":"),c);
        c.gridx++; c.gridwidth = 2; c.anchor = GridBagConstraints.WEST;
        this.titleExpands = new JCheckBox();
        this.titleExpands.setSelected(t.getExpandToFitSpace());
        this.titleExpands.addActionListener(updateHandler);
        interior.add(this.titleExpands, c);

        startNewRow(c);
        JPanel textTab = buildTextTab(t);
        JPanel boxTab = buildBoxTab(t);
        JPanel positionTab = buildPositionTab(t);

        tabs = new JTabbedPane();
        tabs.addTab(localizationResources.getString("Text"), textTab);
        tabs.addTab(localizationResources.getString("Box"), boxTab);
        tabs.addTab(localizationResources.getString("Position"), positionTab);
        c.gridwidth = 3; c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
        interior.add(tabs, c);

        this.enableOrDisableControls();

        general.add(interior, BorderLayout.CENTER);
        add(general, BorderLayout.CENTER);
    }

    private JPanel buildTextTab(TextTitle t) {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel interior = new JPanel(new GridBagLayout());
        interior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridBagConstraints c = getNewConstraints();

        JLabel titleLabel = new JLabel(localizationResources.getString("Text")+":");
        interior.add(titleLabel,c);
        c.gridx++; c.weightx = 1; c.gridwidth = 2; c.anchor = GridBagConstraints.WEST;
        interior.add(this.titleField,c);
        this.titleField.addActionListener(updateHandler);
        this.titleField.getDocument().addDocumentListener(updateHandler);

        startNewRow(c);
        JLabel fontLabel = new JLabel(localizationResources.getString("Font"));
        this.fontControl = new FontControl(t.getFont());
        this.fontControl.addChangeListener(updateHandler);
        interior.add(fontLabel,c);
        c.gridx++; c.weightx = 1; c.gridwidth = 2;
        interior.add(this.fontControl,c);

        startNewRow(c);
        JLabel colorLabel = new JLabel(
            localizationResources.getString("Color")
        );
        this.fontPaintControl.addChangeListener(updateHandler);
        interior.add(colorLabel,c);
        c.gridx++; c.weightx = 1; c.gridwidth = 2;
        interior.add(this.fontPaintControl,c);

        startNewRow(c);
        JLabel backPaintLabel = new JLabel(
                localizationResources.getString("Background_paint")
        );
        this.backgroundPaintControl = new PaintControl(t.getBackgroundPaint(), true);
        this.backgroundPaintControl.addChangeListener(updateHandler);
        interior.add(backPaintLabel, c);
        c.gridx++; c.weightx = 1; c.gridwidth = 2;
        interior.add(this.backgroundPaintControl, c);

        wrapper.add(interior, BorderLayout.NORTH);

        return wrapper;
    }

    private JPanel buildBoxTab(TextTitle t) {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel interior = new JPanel(new GridBagLayout());
        interior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridBagConstraints c = getNewConstraints();

        c.gridwidth = 3; c.weightx = 1;
        this.borderPanel = new InsetPanel(localizationResources.getString("Border"), (BlockBorder)t.getFrame());
        this.borderPanel.addChangeListener(updateHandler);
        interior.add(this.borderPanel, c);

        startNewRow(c);
        c.gridwidth = 3; c.weightx = 1;
        this.marginPanel = new InsetPanel(localizationResources.getString("Margin"), t.getMargin());
        this.marginPanel.addChangeListener(updateHandler);
        interior.add(this.marginPanel, c);

        startNewRow(c);
        c.gridwidth = 3; c.weightx = 1;
        this.paddingPanel = new InsetPanel(localizationResources.getString("Padding"), t.getPadding());
        this.paddingPanel.addChangeListener(updateHandler);
        interior.add(this.paddingPanel, c);

        wrapper.add(interior, BorderLayout.NORTH);
        return wrapper;
    }

    private JPanel buildPositionTab(TextTitle t) {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel interior = new JPanel(new GridBagLayout());
        interior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridBagConstraints c = getNewConstraints();

        interior.add(new JLabel(localizationResources.getString("Edge")+":"), c);
        c.weightx = 1; c.gridx++;
        posCombo = new PositionComboBox();
        posCombo.addActionListener(updateHandler);
        posCombo.setSelectedObject(t.getPosition());
        interior.add(posCombo, c);

        startNewRow(c);
        interior.add(new JLabel(localizationResources.getString("Horizontal_Align")+":"), c);
        c.weightx = 1; c.gridx++;
        horizontalAlign = new HorizontalAlignmentComboBox();
        horizontalAlign.addActionListener(updateHandler);
        horizontalAlign.setSelectedObject(t.getHorizontalAlignment());
        interior.add(horizontalAlign, c);

        startNewRow(c);
        interior.add(new JLabel(localizationResources.getString("Vertical_Align")+":"), c);
        c.weightx = 1; c.gridx++;
        verticalAlign = new VerticalAlignmentComboBox();
        verticalAlign.addActionListener(updateHandler);
        verticalAlign.setSelectedObject(t.getVerticalAlignment());
        interior.add(verticalAlign, c);

        wrapper.add(interior, BorderLayout.NORTH);
        return wrapper;
    }

    /**
     * Returns the title text entered in the panel.
     *
     * @return The title text entered in the panel.
     */
    public String getTitleText() {
        return this.titleField.getText();
    }

    /**
     * Returns the font selected in the panel.
     *
     * @return The font selected in the panel.
     */
    public Font getTitleFont() {
        return this.fontControl.getChosenFont();
    }

    /**
     * Returns the paint selected in the panel.
     *
     * @return The paint selected in the panel.
     */
    public Paint getTitlePaint() {
        return this.fontPaintControl.getChosenPaint();
    }

    /**
     * Returns the background paint selected in the panel.
     *
     * @return The paint selected in the panel.
     */
    public Paint getBackgroundPaint() {
        return this.backgroundPaintControl.getChosenPaint();
    }

    /**
     * Handles button clicks by passing control to an appropriate handler
     * method.
     *
     * @param event  the event
     */
    public void actionPerformed(ActionEvent event) {

        String command = event.getActionCommand();

        if (command.equals("ShowTitle")) {
            attemptModifyShowTitle();
        }
    }

    /**
     * Allow the user the opportunity to change whether the title is
     * displayed on the chart or not.
     */
    private void attemptModifyShowTitle() {
        this.showTitle = this.showTitleCheckBox.isSelected();
        this.enableOrDisableControls();
    }

    /**
     * If we are supposed to show the title, the controls are enabled.
     * If we are not supposed to show the title, the controls are disabled.
     */
    private void enableOrDisableControls() {
        boolean enabled = (this.showTitle);
        this.titleField.setEnabled(enabled);
        this.titleExpands.setEnabled(enabled);
        this.fontControl.setEnabled(enabled);
        this.fontPaintControl.setEnabled(enabled);
        this.backgroundPaintControl.setEnabled(enabled);
        this.borderPanel.setEnabled(enabled);
        this.marginPanel.setEnabled(enabled);
        this.paddingPanel.setEnabled(enabled);
        this.posCombo.setEnabled(enabled);
        this.horizontalAlign.setEnabled(enabled);
        this.verticalAlign.setEnabled(enabled);
        this.tabs.setEnabled(enabled);
    }

    /**
     * Sets the properties of the specified title to match the properties
     * defined on this panel.
     *
     * @param chart  the chart whose title is to be modified.
     */
    public void updateChart(JFreeChart chart) {
        if (this.showTitle) {
            TextTitle title = chart.getTitle();
            if (title == null) {
                title = new TextTitle();
                chart.setTitle(title);
            }
            title.setExpandToFitSpace(titleExpands.isSelected());
            title.setText(getTitleText());
            title.setFont(getTitleFont());
            title.setPaint(getTitlePaint());
            title.setBackgroundPaint(getBackgroundPaint());
            title.setFrame(borderPanel.getSelectedBlockBorder());
            title.setMargin(marginPanel.getSelectedInsets());
            title.setPadding(paddingPanel.getSelectedInsets());
            title.setPosition((RectangleEdge) posCombo.getSelectedObject());
            title.setHorizontalAlignment((HorizontalAlignment) horizontalAlign.getSelectedObject());
            title.setVerticalAlignment((VerticalAlignment) verticalAlign.getSelectedObject());
        }
        else {
            chart.setTitle((TextTitle) null);
        }
    }

}
