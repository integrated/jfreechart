package org.jfree.chart.editor.components;

import org.jfree.chart.EditableBackground;

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

        add(interior, BorderLayout.NORTH);
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
