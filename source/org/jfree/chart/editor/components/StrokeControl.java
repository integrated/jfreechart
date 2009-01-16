package org.jfree.chart.editor.components;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 16-Jan-2009
 * Time: 16:14:56
 * Shows a stroke sample and a button to edit it with.
 */
public class StrokeControl extends AbstractControl {

    private NoCircleStrokeSample strokeSample;

    public StrokeControl(BasicStroke stroke) {
        super(new NoCircleStrokeSample(stroke));
        this.strokeSample = (NoCircleStrokeSample) sample;
    }

    protected void doEditAction() {
        StrokeEditorPanel dialog = new StrokeEditorPanel((BasicStroke)strokeSample.getStroke());
        int result = JOptionPane.showConfirmDialog(this, dialog,
            localizationResources.getString("Border_Stroke"),
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            strokeSample.setStroke(dialog.getSelectedStroke());
            fireChangeEvent();
        }
    }

    public BasicStroke getChosenStroke() {
        return (BasicStroke) strokeSample.getStroke();
    }
}
