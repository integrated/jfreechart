package org.jfree.chart.editor;

import org.jfree.chart.editor.themes.*;
import org.jfree.chart.editor.components.*;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.axis.Axis;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 17-Dec-2009
 * Time: 16:25:21
 * Supplies GUI components for chart editors. The static Controller class can be used to obtain different implementations
 * of this interface, should uses of this library require extended functionality.
 */
public interface ChartEditorComponentFactory {

    static final String DEFAULT_IMPLEMENTATION = "org.jfree.chart.editor.ChartEditorComponentFactoryImpl";

    DefaultChartEditor createChartEditor(ExtendedChartTheme theme, JFreeChart chart, boolean immediateUpdate);

    DefaultAxisEditor createAxisEditor(AxisTheme theme, JFreeChart chart, Axis axis, boolean immediateUpdate);

    DefaultPlotEditor createPlotEditor(PlotTheme theme, JFreeChart chart, Plot plot, boolean immediateUpdate);

    DefaultLegendEditor createLegendEditor(LegendTheme theme, JFreeChart chart, boolean immediateUpdate);

    DefaultChartTitleEditor createChartTitleEditor(ChartTitleTheme theme, JFreeChart chart, boolean immediateUpdate);

    ItemLabelFormatPanel createItemLabelFormatPanel(LegendTheme theme, Plot p);

    ItemLabelFormatPanel createItemLabelFormatPanel(PlotTheme theme, Plot p );

    BorderPanel buildBorderPanel(String title, boolean visible, BasicStroke stroke, Paint paint);

    BackgroundEditingPanel buildBackgroundEditingPanel(ExtendedChartTheme theme);

    BackgroundEditingPanel buildBackgroundEditingPanel(PlotTheme theme);

    PaintControl getPaintControl(Paint p);

    StrokeControl getStrokeControl(BasicStroke s);

    FontControl getFontControl(Font f);

    static class Controller {
        private static String implementationClass = DEFAULT_IMPLEMENTATION;

        public static void setImplementationClass(String s)
                throws ClassNotFoundException, IllegalAccessException, InstantiationException {
            // make sure this doesn't throw any nasties.
            getInstanceInternal(s);
            // reaching this point means the given class was instantiated with the right interface.
            implementationClass = s;
        }

        public static ChartEditorComponentFactory getInstance() {
            try {
                return getInstanceInternal(implementationClass);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        private static ChartEditorComponentFactory getInstanceInternal(String s)
                throws ClassNotFoundException, InstantiationException, IllegalAccessException {
            Class c = Class.forName(s);

            return (ChartEditorComponentFactory) c.newInstance();
        }
    }
}
