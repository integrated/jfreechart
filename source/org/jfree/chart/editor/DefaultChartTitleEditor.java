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

import javax.swing.*;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.chart.editor.components.*;
import org.jfree.chart.editor.themes.ChartTitleTheme;

/**
 * A panel for editing the properties of a chart title.
 */
public class DefaultChartTitleEditor extends DefaultTitleEditor implements ActionListener {

    private ChartTitleTheme theme;

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

    /** Select the text alignment for the title */
    private HorizontalAlignmentComboBox textAlign = new HorizontalAlignmentComboBox();

    /**
     * Standard constructor: builds a panel for displaying/editing the
     * properties of the specified title.
     *
     * @param theme The theme that will store the settings
     * @param chart The chart that is being edited.
     * @param immediateUpdate Whether changes should be applied immediately.
     */
    public DefaultChartTitleEditor(ChartTitleTheme theme, JFreeChart chart, boolean immediateUpdate) {
        super(theme, chart, immediateUpdate);

        this.theme = theme;

        this.titleField = new JTextField(theme.getChartTitle());
        this.fontPaintControl = new PaintControl(theme.getTitlePaint());

        setLayout(new BorderLayout());

        JPanel general = new JPanel(new BorderLayout());

        JPanel interior = new JPanel(new GridBagLayout());
        GridBagConstraints c = getNewConstraints();
        interior.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        c.anchor = GridBagConstraints.WEST;
        this.showTitleCheckBox = new JCheckBox(localizationResources.getString("Show_Title"));
        this.showTitleCheckBox.setSelected(theme.isTitleVisible());
        this.showTitleCheckBox.setActionCommand("ShowTitle");
        this.showTitleCheckBox.addActionListener(updateHandler);
        this.showTitleCheckBox.addActionListener(this);
        interior.add(this.showTitleCheckBox,c);

        startNewRow(c);
        c.anchor = GridBagConstraints.WEST;
        this.titleExpands = new JCheckBox(localizationResources.getString("Expand_to_fit"));
        this.titleExpands.setSelected(theme.isExpandTitle());
        this.titleExpands.addActionListener(updateHandler);
        interior.add(this.titleExpands, c);

        startNewRow(c);
        JPanel textTab = buildTextTab();
        JPanel boxTab = buildBoxTab();
        JPanel positionTab = buildPositionTab();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab(localizationResources.getString("Text"), new JScrollPane(textTab));
        tabs.addTab(localizationResources.getString("Box"), new JScrollPane(boxTab));
        tabs.addTab(localizationResources.getString("Position"), new JScrollPane(positionTab));
        c.weightx = 1; c.weighty = 1; c.fill = GridBagConstraints.BOTH;
        interior.add(tabs, c);

        addCustomTabs(tabs);

        enableOrDisableControls(showTitleCheckBox.isSelected(), showTitleCheckBox);

        general.add(interior, BorderLayout.CENTER);
        add(general, BorderLayout.CENTER);
    }

    private JPanel buildTextTab() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel interior = new JPanel(new GridBagLayout());
        interior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridBagConstraints c = getNewConstraints();

        JLabel titleLabel = new JLabel(localizationResources.getString("Text"));
        interior.add(titleLabel,c);
        c.gridx++; c.weightx = 1; c.gridwidth = 2; c.anchor = GridBagConstraints.WEST;
        interior.add(this.titleField,c);
        this.titleField.addActionListener(updateHandler);
        this.titleField.getDocument().addDocumentListener(updateHandler);

        startNewRow(c);
        interior.add(new JLabel(localizationResources.getString("Text_Align")), c);
        c.weightx = 1; c.gridx++;
        textAlign = new HorizontalAlignmentComboBox();
        textAlign.setSelectedObject(theme.getTextAlignment());
        textAlign.addActionListener(updateHandler);
        interior.add(textAlign, c);

        startNewRow(c);
        JLabel fontLabel = new JLabel(localizationResources.getString("Font"));
        this.fontControl = new FontControl(theme.getTitleFont());
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
        this.backgroundPaintControl = new PaintControl(theme.getTitleBackground(), true);
        this.backgroundPaintControl.addChangeListener(updateHandler);
        interior.add(backPaintLabel, c);
        c.gridx++; c.weightx = 1; c.gridwidth = 2;
        interior.add(this.backgroundPaintControl, c);

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
            enableOrDisableControls(showTitleCheckBox.isSelected(), showTitleCheckBox);
        }
    }

    /**
     * Sets the properties of the specified title to match the properties
     * defined on this panel.
     *
     * @param chart  the chart whose title is to be modified.
     */
    public void updateChart(JFreeChart chart) {
        boolean visible = showTitleCheckBox.isSelected();
        theme.setTitleVisible(visible);
        if (visible) {
            TextTitle title = chart.getTitle();
            if (title == null) {
                title = new TextTitle();
                chart.setTitle(title);
            }

            boolean expands = titleExpands.isSelected();
            title.setExpandToFitSpace(expands);
            theme.setExpandTitle(expands);

            String titleText = getTitleText();
            title.setText(titleText);
            theme.setChartTitle(titleText);

            HorizontalAlignment horizontalAlignment = (HorizontalAlignment) textAlign.getSelectedObject();
            title.setTextAlignment(horizontalAlignment);
            theme.setTextAlignment(horizontalAlignment);

            Font titleFont = getTitleFont();
            title.setFont(titleFont);
            theme.setTitleFont(titleFont);

            Paint titlePaint = getTitlePaint();
            title.setPaint(titlePaint);
            theme.setTitlePaint(titlePaint);

            Paint backgroundPaint = getBackgroundPaint();
            title.setBackgroundPaint(backgroundPaint);
            theme.setTitleBackground(backgroundPaint);

            updateTitle(title);

        }
        else {
            chart.setTitle((TextTitle) null);
        }

        applyCustomEditors(chart);
    }

}
