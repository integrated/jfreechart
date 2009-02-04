package org.jfree.chart.editor.components;

import org.jfree.chart.EditableBackground;
import org.jfree.chart.plot.Plot;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 19-Jan-2009
 * Time: 15:53:48
 * Allows editing of the common interace for background properties in JFreeChart and Plot objects.
 */
public class BackgroundEditingPanel extends EditPanel {

    /** The chart background color. */
    private PaintControl backgroundPaint;

    /** Control to change the alpha-value for the background color (only applicable to Plot objects at the moment) */
    private JSlider backgroundAlpha;

    private final EventHandler updateHandler = new EventHandler();


    public BackgroundEditingPanel(EditableBackground background) {
        setLayout(new BorderLayout());
        JPanel interior = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        interior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        c.gridx = 0; c.gridy = 0; c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(2,3,2,3);
        c.anchor = GridBagConstraints.CENTER;
        interior.add(new JLabel(localizationResources.getString(
                "Background_paint")),c);
        c.gridx++; c.weightx = 1.0; c.gridwidth = 3;
        this.backgroundPaint = new PaintControl(background.getBackgroundPaint(), true);
        this.backgroundPaint.addChangeListener(updateHandler);
        interior.add(this.backgroundPaint,c);

        if(background instanceof Plot) {
            backgroundAlpha = new JSlider(0,100);
            backgroundAlpha.addChangeListener(updateHandler);
            backgroundAlpha.setValue((int)(((Plot)background).getBackgroundAlpha()*100));

            c.gridy++; c.gridx=0; c.weightx = 0; c.gridwidth = 1;
            interior.add(new JLabel(localizationResources.getString("Background_Alpha")+":"),c);
            c.gridx++; c.weightx = 1; c.gridwidth = 3;
            interior.add(backgroundAlpha, c);
        }

        add(interior, BorderLayout.NORTH);
    }

    public float getBackgroundAlpha() {
        return (float)backgroundAlpha.getValue()/100;
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint.getChosenPaint();
    }

    private class EventHandler implements ChangeListener {

        public void stateChanged(ChangeEvent e) {
            fireChangeEvent();
        }
    }
}
