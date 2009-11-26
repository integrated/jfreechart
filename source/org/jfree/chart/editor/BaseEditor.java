package org.jfree.chart.editor;

import org.jfree.chart.JFreeChart;

import javax.swing.*;
import javax.swing.event.*;
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
public abstract class BaseEditor extends JPanel implements ChartEditor {

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

    public static GridBagConstraints getNewConstraints() {
        return new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,
                new Insets(2,3,2,3), 0,0);
    }

    public static void startNewRow(GridBagConstraints c) {
        c.gridx=0;
        c.gridy++;
        c.weightx = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
    }

    public void setLiveUpdates(boolean val) {
        this.immediateUpdate = val;
        setCustomEditorLiveUpdates(val);
    }

    /**
     * Provides an over-ride for sub-classes to add any application specific options here.
     * @param tabs The tabbed pane of editors for the plot.
     */
    protected void addCustomTabs(JTabbedPane tabs) {

    }


    /**
     * Over-ride point for a sub-class to apply the changes of its custom editors.
     * @param chart The chart that the editor is editing.
     */
    protected void applyCustomEditors(JFreeChart chart) {

    }


    /**
     * Over-ride so that live update settings can be applied to custom editors.
     * @param val True iff the editor should update the chart on every change.
     */
    protected void setCustomEditorLiveUpdates(boolean val) {

    }

    public void setChart(JFreeChart chart) {
        if(chart != null) {
            this.chart = chart;
        }
    }

    protected class UpdateHandler implements ActionListener, DocumentListener, ChangeListener,
            ListDataListener {
        private void chartPropertyChanged() {
            if(immediateUpdate) {
                try {
                    chart.setNotify(false);
                    updateChart(chart);
                } finally {
                    chart.setNotify(true);
                }
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

        public void contentsChanged(ListDataEvent e) {
            chartPropertyChanged();
        }

        public void intervalAdded(ListDataEvent e) {
            chartPropertyChanged();
        }

        public void intervalRemoved(ListDataEvent e) {
            chartPropertyChanged();
        }
    }
}
