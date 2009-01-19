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
    private boolean allowNulls;
    private static final Paint DEFAULT_PAINT = Color.BLACK;

    public PaintControl(Paint paint) {
        this(paint, false);
    }

    public PaintControl(Paint paint, boolean allowNulls) {
        super(new PaintSample(paint), allowNulls);

        this.allowNulls = allowNulls;
        this.paintSample = (PaintSample) sample;

        if(allowNulls) {
            boolean paintNotNull = paint != null;
            checkBox.setSelected(paintNotNull);
            setEnabled(paintNotNull, false);
        }
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
        if(allowNulls && !checkBox.isSelected()) {
            return null;
        }
        Paint paint = paintSample.getPaint();
        return paint == null ? DEFAULT_PAINT : paint;
    }
}
