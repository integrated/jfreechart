package org.jfree.chart.editor;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.LegendTitle;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RectangleEdge;
import org.jfree.chart.editor.components.PaintControl;
import org.jfree.chart.editor.components.FontControl;
import org.jfree.chart.editor.components.InsetPanel;
import org.jfree.chart.editor.components.PositionComboBox;
import org.jfree.chart.editor.themes.LegendTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 16-Jan-2009
 * Time: 15:29:56
 * GUI for editing chart legends. It's assumed this class will only be used if there is a legend.
 */
public class DefaultLegendEditor extends DefaultTitleEditor {

    private EventHandler handler = new EventHandler();

    private LegendTheme theme;

    private JCheckBox visible;
    private PaintControl backgroundPaint, itemPaint;

    private FontControl itemFont;

    private InsetPanel itemPadding, graphicPadding;

    private PositionComboBox graphicEdge;

    public DefaultLegendEditor(LegendTheme theme, JFreeChart chart, boolean immediateUpdate) {
        super(theme, chart, immediateUpdate);
        this.theme = theme;

        JPanel box = buildBoxTab();
        JPanel items = getItemsTab();

        setLayout(new GridBagLayout());
        GridBagConstraints c = getNewConstraints();
        c.anchor = GridBagConstraints.WEST; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        add(createVisiblePanel(), c);

        startNewRow(c);
        c.weighty =1; c.fill = GridBagConstraints.BOTH;
        JTabbedPane tabs = new JTabbedPane();
        add(tabs, c);
        tabs.addTab(localizationResources.getString("Items"), new JScrollPane(items));
        tabs.addTab(localizationResources.getString("Box"), new JScrollPane(box));
        addCustomTabs(tabs);
    }

    private JPanel createVisiblePanel() {
        JPanel retVal = createBorderedLabelPanel(localizationResources.getString("Visibility"));
        GridBagConstraints c = getNewConstraints();

        visible = new JCheckBox(localizationResources.getString("Visible"), theme.isVisible());
        visible.addActionListener(updateHandler);
        visible.addActionListener(handler);
        c.anchor = GridBagConstraints.WEST; c.fill = GridBagConstraints.HORIZONTAL; c.weightx = 1;
        retVal.add(visible, c);

        return retVal;
    }

    private JPanel getItemsTab() {
        JPanel textPanel = new JPanel(new GridBagLayout());
        textPanel.setBorder(BorderFactory.createTitledBorder(localizationResources.getString("Text")));
        GridBagConstraints c = getNewConstraints();

        itemFont = new FontControl(theme.getLegendItemFont());
        itemFont.addChangeListener(updateHandler);

        textPanel.add(new JLabel(localizationResources.getString("Font")), c);
        c.gridx++; c.weightx = 1;
        textPanel.add(itemFont, c);

        startNewRow(c);

        itemPaint = new PaintControl(theme.getLegendItemPaint(), false);
        itemPaint.addChangeListener(updateHandler);

        textPanel.add(new JLabel(localizationResources.getString("Paint")),c);
        c.gridx++; c.weightx =1;
        textPanel.add(itemPaint,c);

        itemPadding =  new InsetPanel(localizationResources.getString("Padding"), theme.getItemPadding());
        itemPadding.addChangeListener(updateHandler);

        startNewRow(c);
        c.gridwidth = 2; c.weightx =1;
        textPanel.add(itemPadding, c);

        JPanel graphicsPanel = new JPanel(new GridBagLayout());
        graphicsPanel.setBorder(BorderFactory.createTitledBorder(localizationResources.getString("Graphics")));
        c = getNewConstraints();

        graphicEdge = new PositionComboBox();
        graphicEdge.setSelectedObject(theme.getGraphicEdge());
        graphicEdge.addActionListener(updateHandler);

        graphicsPanel.add(new JLabel(localizationResources.getString("Edge")), c);
        c.gridx++; c.weightx = 1;
        graphicsPanel.add(graphicEdge, c);

        graphicPadding =  new InsetPanel(localizationResources.getString("Padding"), theme.getGraphicPadding());
        graphicPadding.addChangeListener(updateHandler);

        startNewRow(c);
        c.gridwidth = 2; c.weightx =1;
        graphicsPanel.add(graphicPadding, c);

        JPanel backgroundPanel = createBorderedLabelPanel(localizationResources.getString("Background"));
        c = getNewConstraints();

        backgroundPaint = new PaintControl(theme.getLegendBackgroundPaint(), true);
        backgroundPaint.addChangeListener(updateHandler);


        backgroundPanel.add(new JLabel(localizationResources.getString("Background_paint")),c);
        c.gridx++; c.weightx =1;
        backgroundPanel.add(backgroundPaint,c);

        JPanel retVal = new JPanel(new GridBagLayout());

        c=getNewConstraints();
        
        c.gridwidth = 2; c.weightx = 1;
        retVal.add(backgroundPanel, c);

        startNewRow(c);

        c.gridwidth = 2; c.weightx = 1;
        retVal.add(textPanel, c);

        startNewRow(c);

        c.gridwidth = 2; c.weightx = 1;
        retVal.add(graphicsPanel, c);

        startNewRow(c);
        c.weighty =1;
        retVal.add(new JPanel(),c);

        return retVal;
    }

    public void updateChart(JFreeChart chart) {

        LegendTitle legend = chart.getLegend();

        boolean visible = this.visible.isSelected();
        theme.setVisible(visible);
        if(!visible) {
            if (legend != null) {
                chart.removeLegend();
            }
            return;
        } else if (legend == null) {
            legend = new LegendTitle(chart.getPlot());
            chart.addLegend(legend);
        }

        // reaching this point means the theme wants a visible legend and the 'legend'
        // variable points to a legend on the given chart.

        Paint backgroundPaint = this.backgroundPaint.getChosenPaint();
        theme.setLegendBackgroundPaint(backgroundPaint);
        legend.setBackgroundPaint(backgroundPaint);
        
        Paint itemPaint = this.itemPaint.getChosenPaint();
        theme.setLegendItemPaint(itemPaint);
        legend.setItemPaint(itemPaint);

        Font itemFont = this.itemFont.getChosenFont();
        theme.setLegendItemFont(itemFont);
        legend.setItemFont(itemFont);

        RectangleInsets itemPadding = this.itemPadding.getSelectedInsets();
        theme.setItemPadding(itemPadding);
        legend.setItemLabelPadding(itemPadding);

        RectangleEdge graphicEdge = (RectangleEdge) this.graphicEdge.getSelectedObject();
        theme.setGraphicEdge(graphicEdge);
        legend.setLegendItemGraphicEdge(graphicEdge);

        RectangleInsets graphicPadding = this.graphicPadding.getSelectedInsets();
        theme.setGraphicPadding(graphicPadding);
        legend.setLegendItemGraphicPadding(graphicPadding);

        updateTitle(legend);
        applyCustomEditors(chart);
        //legend.setItemFont();
        //legend.setItemLabelPadding();
        //legend.setItemPaint();
        //legend.setLegendItemGraphicAnchor();
        //legend.setLegendItemGraphicEdge();
        //legend.setLegendItemGraphicLocation();
        //legend.setLegendItemGraphicPadding();
    }

    private class EventHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            enableOrDisableControls(visible.isSelected(), visible);
        }
    }
}
