package org.jfree.chart.editor.components;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.editor.themes.ExtendedChartTheme;
import org.jfree.chart.editor.themes.ChartBorder;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 16-Jan-2009
 * Time: 13:17:32
 * Edits standard border properties that are available in JFreeChart objects
 */
public class BorderPanel extends EditPanel {
    
    private JCheckBox visible;
    private StrokeControl strokeControl;
    private PaintControl paintControl;

    private EventHandler handler = new EventHandler();

    public BorderPanel(String title) {
        this(title, true, new BasicStroke(), Color.BLACK);
    }

    public BorderPanel(String title, boolean visible, BasicStroke stroke, Paint paint) {
        super(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2,3,2,3);
        c.fill = GridBagConstraints.HORIZONTAL;

        this.visible = new JCheckBox(localizationResources.getString("Border_Visible"), visible);
        this.strokeControl = new StrokeControl(stroke);
        this.paintControl = new PaintControl(paint);

        this.visible.addActionListener(handler);
        this.strokeControl.addChangeListener(handler);
        this.paintControl.addChangeListener(handler);

        updateControls();

        setBorder(BorderFactory.createTitledBorder(title));

        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.gridwidth = 3;
        add(this.visible,c);
        c.gridy++; c.weightx = 0; c.gridwidth = 1;
        add(new JLabel(localizationResources.getString("Border_Stroke")+":"), c);
        c.gridx++; c.weightx = 1; c.gridwidth = 2;
        add(this.strokeControl, c);

        c.gridx = 0; c.gridy++;
        add(new JLabel(localizationResources.getString("Border_Paint")+":"), c);
        c.gridx++; c.weightx = 1; c.gridwidth = 2;
        add(this.paintControl, c);
    }

    private void updateControls() {
        boolean b = visible.isSelected();
        strokeControl.setEnabled(b);
        paintControl.setEnabled(b);
    }

    public boolean isBorderVisible() {
        return visible.isSelected();
    }

    public BasicStroke getBorderStroke() {
        return strokeControl.getChosenStroke();
    }

    public Paint getBorderPaint() {
        return paintControl.getChosenPaint();
    }

    public void apply(JFreeChart chart, ExtendedChartTheme theme) {
        if(chart != null) {
            chart.setBorderVisible(isBorderVisible());
            chart.setBorderStroke(getBorderStroke());
            chart.setBorderPaint(getBorderPaint());
        }
        if(theme != null) {
            ChartBorder cBorder = theme.getBorder();
            cBorder.setVisible(isBorderVisible());
            cBorder.setStroke(getBorderStroke());
            cBorder.setPaint(getBorderPaint());
        }
    }

    private class EventHandler implements ActionListener, ChangeListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == visible) {
                updateControls();
                fireChangeEvent();
            }
        }

        public void stateChanged(ChangeEvent e) {
            if(e.getSource() == paintControl || e.getSource() == strokeControl) {
                fireChangeEvent();
            }
        }
    }
}
