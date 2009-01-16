package org.jfree.chart.editor.components;

import org.jfree.ui.PaintSample;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 16-Jan-2009
 * Time: 15:52:51
 * Controls for editing a paint.
 */
public class PaintControl extends AbstractControl {
    private PaintSample paintSample;
    public PaintControl(Paint paint) {
        super(new PaintSample(paint));

        this.paintSample = (PaintSample) sample;
    }

    protected void doEditAction() {
        Color c;
        c = JColorChooser.showDialog(
            this, localizationResources.getString("Color"), Color.black
        );
        if (c != null) {
            paintSample.setPaint(c);
            fireChangeEvent();
        }
    }

    public Paint getChosenPaint() {
        return paintSample.getPaint();
    }
}
