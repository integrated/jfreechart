package org.jfree.chart.editor.components;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 11-Mar-2009
 * Time: 10:04:08
 * Re-usable control for editing properties of gridlines/axislines etc.
 */
public class LineEditorPanel extends EditPanel {

    private JCheckBox visibility;
    private PaintControl paintControl;
    private StrokeControl strokeControl;

    private EventHandler handler = new EventHandler();

    public LineEditorPanel(String title, boolean isVisible, Paint paint, BasicStroke line) {
        visibility = new JCheckBox(localizationResources.getString("Visible"), isVisible);
        paintControl = new PaintControl(paint, false);
        strokeControl = new StrokeControl(line);

        visibility.addActionListener(handler);
        paintControl.addChangeListener(handler);
        strokeControl.addChangeListener(handler);

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(title));

        GridBagConstraints c = getNewConstraints();
        c.gridwidth = 2; c.anchor = GridBagConstraints.WEST;
        add(visibility, c);

        startNewRow(c);
        add(new JLabel(localizationResources.getString("Paint")), c);
        c.gridx++; c.weightx = 1;
        add(paintControl, c);

        startNewRow(c);
        add(new JLabel(localizationResources.getString("Stroke")), c);
        c.gridx++; c.weightx = 1;
        add(strokeControl, c);

        alterItemStates();
    }

    private void alterItemStates() {
        boolean enabled = visibility.isSelected();

        paintControl.setEnabled(enabled);
        strokeControl.setEnabled(enabled);
    }

    public boolean isLineVisible() {
        return visibility.isSelected();
    }

    public Paint getLinePaint() {
        return paintControl.getChosenPaint();
    }

    public BasicStroke getLineStroke() {
        return strokeControl.getChosenStroke();
    }


    private class EventHandler implements ActionListener, ChangeListener {

        public void actionPerformed(ActionEvent e) {
            alterItemStates();
            fireChangeEvent();
        }

        public void stateChanged(ChangeEvent e) {
            fireChangeEvent();
        }
    }
}
