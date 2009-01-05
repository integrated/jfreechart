package uk.co.integrate.snippets.jfreechart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 05-Jan-2009
 * Time: 14:52:19
 * To change this template use File | Settings | File Templates.
 */
public class ChartEditorHarness extends JFrame {
    private final static int DATA_POINTS = 10;
    private final static int NUM_SERIES = 3;
    private static Number[][] RANDOM_NUMBERS = new Number[NUM_SERIES][DATA_POINTS];
    private static String[] CATEGORY_IDS = new String[DATA_POINTS];
    static {
        for(int i = 0; i < NUM_SERIES; i++) {
            for(int j = 0; j < DATA_POINTS; j++) {
                RANDOM_NUMBERS[i][j] = new Double(Math.random());
                if(i == 0) {
                    CATEGORY_IDS[j] = ""+j;
                }
            }
        }
    }

    public ChartEditorHarness() {
        super("Chart Editor Harness");
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for(int i = 0; i < NUM_SERIES; i++) {
            for(int j = 0; j < DATA_POINTS; j++) {
                String colKey = ""+i;
                dataset.addValue(RANDOM_NUMBERS[i][j], colKey, CATEGORY_IDS[j]);
            }
        }

        JFreeChart chart = ChartFactory.createBarChart("Title",
                "catLabel", "valLabel", dataset, PlotOrientation.VERTICAL,
                true, true, true);

        ChartPanel panel = new ChartPanel(chart, true, true, true, true, true);
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        pack();
    }

    public static void main(String[] args) {
        ChartEditorHarness harness = new ChartEditorHarness();
        harness.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        harness.setVisible(true);
    }
}
