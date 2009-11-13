package org.jfree.chart.editor;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.title.Title;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;
import org.jfree.chart.editor.components.InsetPanel;
import org.jfree.chart.editor.components.PositionComboBox;
import org.jfree.chart.editor.components.HorizontalAlignmentComboBox;
import org.jfree.chart.editor.components.VerticalAlignmentComboBox;
import org.jfree.chart.editor.themes.TitleTheme;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 13-Mar-2009
 * Time: 11:37:28
 * Base class for the legend and chart title editors.
 */
public abstract class DefaultTitleEditor extends BaseEditor {
    private TitleTheme theme;

    /** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
                    "org.jfree.chart.editor.LocalizationBundle");

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

    public DefaultTitleEditor(TitleTheme theme, JFreeChart chart, boolean immediateUpdate) {
        super(chart, immediateUpdate);
        this.theme = theme;
    }

    protected void enableOrDisableControls(boolean enabled, Component toSkip) {
        recursivelySetEnabled(getComponents(), enabled, 0, toSkip);
    }

    private final static int MAX_DEPTH = 5;
    private void recursivelySetEnabled(Component[] comps, boolean enabled, int depth, Component toSkip) {
        if(comps == null || depth >= MAX_DEPTH) {
            return;
        }

        for(int i = 0; i < comps.length; i++) {
            if(comps[i] == toSkip) {
                continue;
            }

            comps[i].setEnabled(enabled);
            if(comps[i] instanceof Container) {
                recursivelySetEnabled(((Container)comps[i]).getComponents(), enabled, depth + 1, toSkip);
            }
        }
    }

    protected JPanel buildBoxTab() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel interior = new JPanel(new GridBagLayout());
        interior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridBagConstraints c = getNewConstraints();

        c.gridwidth = 3; c.weightx = 1;
        this.borderPanel = new InsetPanel(localizationResources.getString("Border"), theme.getTitleFrame());
        this.borderPanel.addChangeListener(updateHandler);
        interior.add(this.borderPanel, c);

        startNewRow(c);
        c.gridwidth = 3; c.weightx = 1;
        this.marginPanel = new InsetPanel(localizationResources.getString("Margin"), theme.getTitleMargin());
        this.marginPanel.addChangeListener(updateHandler);
        interior.add(this.marginPanel, c);

        startNewRow(c);
        c.gridwidth = 3; c.weightx = 1;
        this.paddingPanel = new InsetPanel(localizationResources.getString("Padding"), theme.getTitlePadding());
        this.paddingPanel.addChangeListener(updateHandler);
        interior.add(this.paddingPanel, c);

        wrapper.add(interior, BorderLayout.NORTH);
        return wrapper;
    }

    protected JPanel buildPositionTab() {
        JPanel wrapper = new JPanel(new BorderLayout());
        JPanel interior = new JPanel(new GridBagLayout());
        interior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GridBagConstraints c = getNewConstraints();

        interior.add(new JLabel(localizationResources.getString("Edge")), c);
        c.weightx = 1; c.gridx++;
        posCombo = new PositionComboBox();
        posCombo.setSelectedObject(theme.getPosition());
        posCombo.addActionListener(updateHandler);
        interior.add(posCombo, c);

        startNewRow(c);
        interior.add(new JLabel(localizationResources.getString("Horizontal_Align")), c);
        c.weightx = 1; c.gridx++;
        horizontalAlign = new HorizontalAlignmentComboBox();
        horizontalAlign.setSelectedObject(theme.getHorizontalAlignment());
        horizontalAlign.addActionListener(updateHandler);
        interior.add(horizontalAlign, c);

        startNewRow(c);
        interior.add(new JLabel(localizationResources.getString("Vertical_Align")), c);
        c.weightx = 1; c.gridx++;
        verticalAlign = new VerticalAlignmentComboBox();
        verticalAlign.setSelectedObject(theme.getVerticalAlignment());
        verticalAlign.addActionListener(updateHandler);
        interior.add(verticalAlign, c);

        wrapper.add(interior, BorderLayout.NORTH);
        return wrapper;
    }

    public abstract void updateChart(JFreeChart chart);

    protected void updateTitle(Title title) {
        BlockBorder frame = borderPanel.getSelectedBlockBorder();
            title.setFrame(frame);
            theme.setTitleFrame(frame);

            RectangleInsets margin = marginPanel.getSelectedInsets();
            title.setMargin(margin);
            theme.setTitleMargin(margin);

            RectangleInsets padding = paddingPanel.getSelectedInsets();
            title.setPadding(padding);
            theme.setTitlePadding(padding);

            RectangleEdge position = (RectangleEdge) posCombo.getSelectedObject();
            title.setPosition(position);
            theme.setPosition(position);

            HorizontalAlignment horizAlign = (HorizontalAlignment) horizontalAlign.getSelectedObject();
            title.setHorizontalAlignment(horizAlign);
            theme.setHorizontalAlignment(horizAlign);

            VerticalAlignment vertAlign = (VerticalAlignment) verticalAlign.getSelectedObject();
            title.setVerticalAlignment(vertAlign);
            theme.setVerticalAlignment(vertAlign);
    }
}
