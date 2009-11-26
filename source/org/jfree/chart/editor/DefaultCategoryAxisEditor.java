package org.jfree.chart.editor;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.editor.themes.AxisTheme;
import org.jfree.chart.editor.themes.ThemeUtil;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 10-Mar-2009
 * Time: 11:57:57
 * GUI controls that are specific to the properties of a category axis.
 */
public class DefaultCategoryAxisEditor extends DefaultAxisEditor {

    private JSpinner catLabelAngle;

    private JSpinner catLabelLines;

    private JSpinner upperMargin, lowerMargin, categoryMargin;

    public DefaultCategoryAxisEditor(AxisTheme theme, JFreeChart chart, boolean immediateUpdate) {
        super(theme, chart, immediateUpdate);
    }

    protected void addAxisTypeSpecificTickControls(JPanel ticks, GridBagConstraints c) {
        startNewRow(c);

        catLabelAngle = new JSpinner(new SpinnerNumberModel(theme.getCatLabelAngleDegs(), -90, 90, 1));
        catLabelAngle.addChangeListener(updateHandler);
        ticks.add(new JLabel(localizationResources.getString("Label_angle")), c);
        c.gridx++; c.gridwidth = 2; c.fill= GridBagConstraints.NONE; c.anchor = GridBagConstraints.WEST;
        ticks.add(catLabelAngle, c);

        startNewRow(c);
        catLabelLines = new JSpinner(new SpinnerNumberModel(theme.getCatLabelMaxLines(), 1, 100, 1));
        catLabelLines.addChangeListener(updateHandler);
        ticks.add(new JLabel(localizationResources.getString("Label_lines")), c);
        c.gridx++; c.gridwidth = 2; c.fill= GridBagConstraints.NONE; c.anchor = GridBagConstraints.WEST;
        ticks.add(catLabelLines, c);


    }

    protected void addCustomTabs(JTabbedPane tabs) {
        JPanel margins = new JPanel(new GridBagLayout());
        GridBagConstraints c = getNewConstraints();

        upperMargin = configureMarginSpinner(theme.getUpperMargin());
        margins.add(new JLabel(localizationResources.getString("Upper_Margin")), c);
        c.gridx++; c.gridwidth = 2; c.fill= GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        margins.add(upperMargin, c);

        startNewRow(c);
        
        lowerMargin = configureMarginSpinner(theme.getLowerMargin());
        margins.add(new JLabel(localizationResources.getString("Lower_Margin")), c);
        c.gridx++; c.gridwidth = 2; c.fill= GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        margins.add(lowerMargin, c);

        startNewRow(c);

        categoryMargin = configureMarginSpinner(theme.getCategoryMargin());
        margins.add(new JLabel(localizationResources.getString("Category_Margin")), c);
        c.gridx++; c.gridwidth = 2; c.fill= GridBagConstraints.HORIZONTAL; c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        margins.add(categoryMargin, c);

        tabs.addTab(localizationResources.getString("Margin"), margins);
    }

    private JSpinner configureMarginSpinner(double val) {
        JSpinner retVal = new JSpinner(new SpinnerNumberModel(val, 0.0, 1.0, 0.01));
        retVal.setEditor(new JSpinner.NumberEditor(retVal, "#.#%"));
        retVal.addChangeListener(updateHandler);
        return retVal;
    }

    public double getCategoryLabelAngle() {
        return ((Number)catLabelAngle.getValue()).doubleValue();
    }

    public int getCategoryLabelMaxLines() {
        return ((Number)catLabelLines.getValue()).intValue();
    }

    public double getLowerMargin() {
        return ((Number)lowerMargin.getValue()).doubleValue();
    }

    public double getUpperMargin() {
        return ((Number)upperMargin.getValue()).doubleValue();
    }

    public double getCategoryMargin() {
        return ((Number)categoryMargin.getValue()).doubleValue();
    }

    protected void applyToAxes(Axis[] axes) {
        super.applyToAxes(axes);

        double angle = getCategoryLabelAngle();
        int lines = getCategoryLabelMaxLines();
        theme.setCatLabelAngleDegs(angle);
        theme.setCatLabelMaxLines(lines);

        double lowerMargin = getLowerMargin();
        theme.setLowerMargin(lowerMargin);

        double upperMargin = getUpperMargin();
        theme.setUpperMargin(upperMargin);

        double categoryMargin = getCategoryMargin();
        theme.setCategoryMargin(categoryMargin);

        CategoryLabelPositions labelPositions = null;
        for(int i = 0; i < axes.length; i++) {
            if(axes[i] instanceof CategoryAxis) {
                CategoryAxis c = (CategoryAxis) axes[i];

                if(labelPositions == null) {
                    labelPositions = ThemeUtil.getCategoryLabelPositions(angle);
                }
                c.setCategoryLabelPositions(labelPositions);
                c.setMaximumCategoryLabelLines(lines);
                c.setUpperMargin(upperMargin);
                c.setLowerMargin(lowerMargin);
                c.setCategoryMargin(categoryMargin);
            }
        }
    }
}
