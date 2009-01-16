package org.jfree.chart.editor;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.LegendTitle;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 16-Jan-2009
 * Time: 15:29:56
 * GUI for editing chart legends. It's assumed this class will only be used if there is a legend.
 */
public class DefaultLegendEditor extends BaseEditor {
    private LegendTitle legend;

    public DefaultLegendEditor(JFreeChart chart, boolean immediateUpdate) {
        super(chart, immediateUpdate);

        legend = chart.getLegend();
    }
    public void updateChart(JFreeChart chart) {
        //legend.setBackgroundPaint();
        //legend.setBorder();
        //legend.setHorizontalAlignment();
        //legend.setItemFont();
        //legend.setItemLabelPadding();
        //legend.setItemPaint();
        //legend.setLegendItemGraphicAnchor();
        //legend.setLegendItemGraphicEdge();
        //legend.setLegendItemGraphicLocation();
        //legend.setLegendItemGraphicPadding();
        //legend.setMargin();
        //legend.setPadding();
        //legend.setPosition();
        //legend.setVerticalAlignment();
        //legend.setVisible();
    }
}
