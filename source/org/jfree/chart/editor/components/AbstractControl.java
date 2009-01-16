package org.jfree.chart.editor.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 16-Jan-2009
 * Time: 15:56:02
 * Base class for the controls of the form (sample, editButton)
 */
public abstract class AbstractControl extends EditPanel {
    protected JComponent sample;
    protected JButton button;

    private EventHandler handler = new EventHandler();

    public AbstractControl(JComponent sample) {
        super(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.weightx = 0; c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1; c.insets = new Insets(2,3,2,3);
        this.sample = sample;
        add(sample, c);

        c.gridx++; c.weightx = 0;
        button = new JButton(localizationResources.getString("Edit..."));
        button.addActionListener(handler);
        add(button, c);
    }

    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
        sample.setEnabled(enabled);
        super.setEnabled(enabled);
    }

    protected abstract void doEditAction();

    private class EventHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == button) {
                doEditAction();
            }
        }
    }
}
