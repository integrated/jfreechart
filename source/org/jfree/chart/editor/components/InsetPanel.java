package org.jfree.chart.editor.components;

import org.jfree.ui.RectangleInsets;
import org.jfree.chart.block.BlockBorder;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 16-Jan-2009
 * Time: 12:38:08
 * Panel that lets a user edit an insets object, or a frame border.
 */
public class InsetPanel extends EditPanel {
    private RectangleInsets insets;
    private JSpinner left, right, top, bottom;
    private PaintControl paintControl;

    private EventHandler handler = new EventHandler();

    public InsetPanel(String title) {
        this(title, new RectangleInsets());
    }

    public InsetPanel(String title, BlockBorder frame) {
        this(title, frame.getInsets(), frame.getPaint());
    }

    public InsetPanel(String title, RectangleInsets insets) {
        this(title, insets, null);
    }

    public InsetPanel(String title, RectangleInsets insets, Paint paint) {
        super(new GridBagLayout());
        this.insets = insets;

        left = new JSpinner(new SpinnerNumberModel(insets.getLeft(), 0 , 100, 0.5));
        right = new JSpinner(new SpinnerNumberModel(insets.getRight(), 0 , 100, 0.5));
        top = new JSpinner(new SpinnerNumberModel(insets.getTop(), 0 , 100, 0.5));
        bottom = new JSpinner(new SpinnerNumberModel(insets.getBottom(), 0 , 100, 0.5));

        left.addChangeListener(handler);
        right.addChangeListener(handler);
        top.addChangeListener(handler);
        bottom.addChangeListener(handler);

        setBorder(BorderFactory.createTitledBorder(title));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(2,3,2,3);
        c.gridx = 0; c.gridy = 0; c.weightx = 0;
        add(new JLabel(localizationResources.getString("Top")+":"),c);
        c.gridx++; c.weightx = 1;
        add(top, c);
        c.gridx++; c.weightx = 0;
        add(new JLabel(localizationResources.getString("Bottom")+":"),c);
        c.gridx++; c.weightx = 1;
        add(bottom, c);
        c.gridx=0; c.gridy++; c.weightx = 0;
        add(new JLabel(localizationResources.getString("Left")+":"),c);
        c.gridx++; c.weightx = 1;
        add(left, c);
        c.gridx++; c.weightx = 0;
        add(new JLabel(localizationResources.getString("Right")+":"),c);
        c.gridx++; c.weightx = 1;
        add(right, c);
        if(paint != null) {
            paintControl = componentFactory.getPaintControl(paint);
            paintControl.addChangeListener(handler);
            c.gridx = 0; c.gridy++; c.weightx = 0;
            add(new JLabel(localizationResources.getString("Color")), c);
            c.gridx++; c.weightx = 1; c.gridwidth = 3;
            add(paintControl, c);
        } else {
            paintControl = null;
        }
    }

    public void setEnabled(boolean val) {
        left.setEnabled(val);
        right.setEnabled(val);
        top.setEnabled(val);
        bottom.setEnabled(val);
        if(paintControl != null) {
            paintControl.setEnabled(val);
        }
        super.setEnabled(val);
    }

    public RectangleInsets getSelectedInsets() {
        return insets;
    }

    public void setSelectedInsets(RectangleInsets insets) {
        this.insets = insets;
    }

    public BlockBorder getSelectedBlockBorder() {
        return new BlockBorder(insets, paintControl.getChosenPaint());
    }

    private class EventHandler implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            if(e.getSource() != paintControl) {
                insets =  new RectangleInsets(
                        insets.getUnitType(), getValueAsDouble(top), getValueAsDouble(left),
                        getValueAsDouble(bottom), getValueAsDouble(right));
            }
            fireChangeEvent();
        }

        private double getValueAsDouble(JSpinner s) {
            return ((Number)s.getValue()).doubleValue();
        }
    }
}
