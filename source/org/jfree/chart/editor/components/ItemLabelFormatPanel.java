package org.jfree.chart.editor.components;

import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.XYPlot;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.*;
import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 07-Dec-2009
 * Time: 13:59:05
 * Controls that affect the text used within legend/data labels.
 */
public class ItemLabelFormatPanel extends EditPanel {
    private final static int PIE = 2, CATEGORY = 1, XY = 0;

    // 3x2 array. Second index is 0 == !isForPlot, 1 == isForPlot
    // First index plot type. Example: PIE is located at 2.
    private final static String[][] TOOL_TIPS = {
            {localizationResources.getString("Format_tip_xy_legend"), localizationResources.getString("Format_tip_xy")},
            {localizationResources.getString("Format_tip_cat_legend"), localizationResources.getString("Format_tip_cat")},
            {localizationResources.getString("Format_tip_pie_legend"), localizationResources.getString("Format_tip_pie")}
    };

    private JTextField labelFormat;
    private NumberFormatDisplay numberFormatDisplay, percentFormatDisplay;
    private EventHandler updateHandler = new EventHandler();

    public ItemLabelFormatPanel(String labelFormat, Plot plot, String numFormatString, String percentFormatString, boolean isForPlot) {
        this.labelFormat = new JTextField(labelFormat);
        DocHandler handler = new DocHandler();
        this.labelFormat.getDocument().addDocumentListener(handler);
        this.labelFormat.addFocusListener(handler);
        String tip;

        int plotType = getPlotType(plot);

        tip = TOOL_TIPS[plotType][isForPlot?1:0];
        this.labelFormat.setToolTipText(tip);

        numberFormatDisplay = buildNumberFormatDisplay(numFormatString);
        numberFormatDisplay.addChangeListener(updateHandler);

        percentFormatDisplay = buildNumberFormatDisplay(percentFormatString);
        percentFormatDisplay.addChangeListener(updateHandler);

        setLayout(new GridBagLayout());

        setBorder(BorderFactory.createTitledBorder(localizationResources.getString("Label_Format")));
        GridBagConstraints c = getNewConstraints();

        add(new JLabel(localizationResources.getString("Format")), c);
        c.gridx++; c.weightx = 1;
        add(this.labelFormat, c);

        if(isForPlot || plotType == PIE) {
            startNewRow(c);
            add(new JLabel(localizationResources.getString("Number_Format")), c);
            c.gridx++; c.weightx = 1;
            add(numberFormatDisplay, c);

            startNewRow(c);
            add(new JLabel(localizationResources.getString("Percent_Format")), c);
            c.gridx++; c.weightx = 1;
            add(percentFormatDisplay, c);
        }
    }

    private static int getPlotType(Plot p) {
        if(p instanceof PiePlot) {
            return PIE;
        } else if (p instanceof XYPlot) {
            return XY;
        } else {
            return CATEGORY;
        }
    }

    protected NumberFormatDisplay buildNumberFormatDisplay(String formatStr) {
        return componentFactory.createNumberFormatDisplay(formatStr);
    }

    private boolean formatValid() {
        String format = labelFormat.getText();
        try {
            new MessageFormat(format);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void setEnabled(boolean e) {
        super.setEnabled(e);
        labelFormat.setEnabled(e);
        numberFormatDisplay.setEnabled(e);
        percentFormatDisplay.setEnabled(e);
    }

    public String getLabelFormat() {
        return labelFormat.getText();
    }

    public String getNumberFormatString() {
        return numberFormatDisplay.getFormatString();
    }

    public String getPercentFormatString() {
        return percentFormatDisplay.getFormatString();
    }

    private class DocHandler implements DocumentListener, FocusListener {
        String lastGoodFormat = labelFormat.getText();

        public void changedUpdate(DocumentEvent e) {
            if(formatValid()) {
                labelFormat.setBackground(Color.WHITE);
                labelFormat.setForeground(Color.BLACK);
                lastGoodFormat = labelFormat.getText();
                updateHandler.changedUpdate(e);
            } else {
                labelFormat.setBackground(Color.RED);
                labelFormat.setForeground(Color.WHITE);
            }
        }

        public void insertUpdate(DocumentEvent e) {
            if(formatValid()) {
                labelFormat.setBackground(Color.WHITE);
                labelFormat.setForeground(Color.BLACK);
                lastGoodFormat = labelFormat.getText();
                updateHandler.removeUpdate(e);
            } else {
                labelFormat.setBackground(Color.RED);
                labelFormat.setForeground(Color.WHITE);
            }
        }

        public void removeUpdate(DocumentEvent e) {
            if(formatValid()) {
                labelFormat.setBackground(Color.WHITE);
                labelFormat.setForeground(Color.BLACK);
                lastGoodFormat = labelFormat.getText();
                updateHandler.removeUpdate(e);
            } else {
                labelFormat.setBackground(Color.RED);
                labelFormat.setForeground(Color.WHITE);
            }
        }

        public void focusGained(FocusEvent e) {
            // do nothing.
        }

        public void focusLost(FocusEvent e) {
            if(!formatValid()) {
                labelFormat.setText(lastGoodFormat);
            }
        }
    }

    private class EventHandler implements ChangeListener, DocumentListener {

        public void insertUpdate(DocumentEvent e) {
            fireChangeEvent();
        }

        public void removeUpdate(DocumentEvent e) {
            fireChangeEvent();
        }

        public void changedUpdate(DocumentEvent e) {
            fireChangeEvent();
        }

        public void stateChanged(ChangeEvent e) {
            fireChangeEvent();
        }
    }
}
