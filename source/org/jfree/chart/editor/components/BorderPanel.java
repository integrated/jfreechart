package org.jfree.chart.editor.components;

import org.jfree.ui.PaintSample;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.chart.editor.StrokeEditorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 16-Jan-2009
 * Time: 13:17:32
 * Edits standard border properties that are available in JFreeChart objects
 */
public class BorderPanel extends EditPanel {

    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
                    "org.jfree.chart.editor.LocalizationBundle");
    
    private JCheckBox visible;
    private NoCircleStrokeSample stroke;
    private PaintSample paint;
    private JButton strokeButton, paintButton;

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
        this.stroke = new NoCircleStrokeSample(stroke);
        this.paint = new PaintSample(paint);

        this.strokeButton = new JButton(localizationResources.getString("Edit..."));
        this.paintButton = new JButton(localizationResources.getString("Edit..."));

        this.visible.addActionListener(handler);
        this.strokeButton.addActionListener(handler);
        this.paintButton.addActionListener(handler);

        updateControls();

        setBorder(BorderFactory.createTitledBorder(title));

        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.gridwidth = 3;
        add(this.visible,c);
        c.gridy++; c.weightx = 0; c.gridwidth = 1;
        add(new JLabel(localizationResources.getString("Border_Stroke")+":"), c);
        c.gridx++; c.weightx = 1;
        add(this.stroke, c);
        c.gridx++; c.weightx = 0;
        add(this.strokeButton, c);

        c.gridx = 0; c.gridy++;
        add(new JLabel(localizationResources.getString("Border_Paint")+":"), c);
        c.gridx++; c.weightx = 1;
        add(this.paint, c);
        c.gridx++; c.weightx = 0;
        add(this.paintButton, c);
    }

    private void updateControls() {
        boolean b = visible.isSelected();
        strokeButton.setEnabled(b);
        paintButton.setEnabled(b);
    }

    public boolean isBorderVisible() {
        return visible.isSelected();
    }

    public BasicStroke getBorderStroke() {
        return (BasicStroke) stroke.getStroke();
    }

    public Paint getBorderPaint() {
        return paint.getPaint();
    }

    /**
     * Allows the user the opportunity to choose a new color for the chart border
     */
    private void attemptModifyBorderPaint() {
        Color c;
        c = JColorChooser.showDialog(
            this, localizationResources.getString("Border_Paint"), Color.black
        );
        if (c != null) {
            paint.setPaint(c);
            fireChangeEvent();
        }
    }

    /**
     * Allows the user the opportunity to choose a new color for the chart border
     */
    private void attemptModifyBorderStroke() {
        StrokeEditorPanel dialog = new StrokeEditorPanel((BasicStroke)stroke.getStroke());
        int result = JOptionPane.showConfirmDialog(this, dialog,
            localizationResources.getString("Border_Stroke"),
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            stroke.setStroke(dialog.getSelectedStroke());
            stroke.invalidate();
            fireChangeEvent();
        }
    }

    private class EventHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == visible) {
                updateControls();
                fireChangeEvent();
            } else if (e.getSource() == strokeButton) {
                attemptModifyBorderStroke();
            } else if (e.getSource() == paintButton) {
                attemptModifyBorderPaint();
            }
        }
    }
}
