package org.jfree.chart.editor;

import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 12-Jan-2009
 * Time: 12:28:01
 * Common functionality for editor classes.
 */
abstract class BaseEditor extends JPanel implements ChartEditor {

    /** Whether to update the chart as and when changes are made to its properties by this editor */
    protected boolean immediateUpdate;

    /** The chart used to initialise the editor - so that updates can be applied immediately */
    protected JFreeChart chart;

    /** This is used to handle live updating of the chart */
    protected final UpdateHandler updateHandler = new UpdateHandler();

    public BaseEditor(JFreeChart chart, boolean immediateUpdate) {
        super();
        this.immediateUpdate = immediateUpdate;
        this.chart = chart;
    }

    protected static GridBagConstraints getNewConstraints() {
        return new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,
                new Insets(2,3,2,3), 0,0);
    }

    protected static void startNewRow(GridBagConstraints c) {
        c.gridx=0;
        c.gridy++;
        c.weightx = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
    }

    private class UpdateHandler implements ActionListener, DocumentListener, ChangeListener {
        private void chartPropertyChanged() {
            if(immediateUpdate) {
                updateChart(chart);
            }
        }

        public void actionPerformed(ActionEvent e) {
            chartPropertyChanged();
        }

        public void insertUpdate(DocumentEvent e) {
            chartPropertyChanged();
        }

        public void removeUpdate(DocumentEvent e) {
            chartPropertyChanged();
        }

        public void changedUpdate(DocumentEvent e) {
            chartPropertyChanged();
        }

        public void stateChanged(ChangeEvent e) {
            chartPropertyChanged();
        }
    }
}
