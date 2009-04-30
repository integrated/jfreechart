package org.jfree.chart.editor;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.editor.themes.iPlusAxisTheme;
import org.jfree.chart.editor.themes.iPlusThemeUtil;
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
 * To change this template use File | Settings | File Templates.
 */
public class DefaultCategoryAxisEditor extends DefaultAxisEditor {

    private JSpinner catLabelAngle;

    private JSpinner catLabelLines;

    public DefaultCategoryAxisEditor(iPlusAxisTheme theme, JFreeChart chart, boolean immediateUpdate) {
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

    public double getCategoryLabelAngle() {
        return ((Number)catLabelAngle.getValue()).doubleValue();
    }

    public int getCategoryLabelMaxLines() {
        return ((Number)catLabelLines.getValue()).intValue();
    }

    protected void applyToAxes(Axis[] axes) {
        super.applyToAxes(axes);

        double angle = getCategoryLabelAngle();
        int lines = getCategoryLabelMaxLines();
        theme.setCatLabelAngleDegs(angle);
        theme.setCatLabelMaxLines(lines);

        CategoryLabelPositions labelPositions = null;
        for(int i = 0; i < axes.length; i++) {
            if(axes[i] instanceof CategoryAxis) {
                CategoryAxis c = (CategoryAxis) axes[i];

                if(labelPositions == null) {
                    labelPositions = iPlusThemeUtil.getCategoryLabelPositions(angle);
                }
                c.setCategoryLabelPositions(labelPositions);
                c.setMaximumCategoryLabelLines(lines);
            }
        }
    }
}
