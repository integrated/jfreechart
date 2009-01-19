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
    protected JCheckBox checkBox = null;

    protected boolean showCheckBox;

    private EventHandler handler = new EventHandler();

    public AbstractControl(JComponent sample) {
        this(sample, false);
    }

    public AbstractControl(JComponent sample, boolean showCheckBox) {
        super(new GridBagLayout());

        this.showCheckBox = showCheckBox;

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2,3,2,3);

        if(showCheckBox) {
            c.weightx = 0;
            checkBox = new JCheckBox();
            checkBox.addActionListener(handler);
            add(checkBox, c);
            c.gridx++;
        }

        c.weightx = 1;
        this.sample = sample;
        add(sample, c);

        c.gridx++; c.weightx = 0;
        button = new JButton(localizationResources.getString("Edit..."));
        button.addActionListener(handler);
        add(button, c);
    }

    public void setEnabled(boolean enabled) {
        setEnabled(enabled, true);
    }

    public void setEnabled(boolean enabled, boolean includeCheckbox) {
        if(includeCheckbox && showCheckBox) {
            checkBox.setEnabled(enabled);
        }
        boolean b = enabled && (!showCheckBox || checkBox.isSelected());
        button.setEnabled(b);
        sample.setEnabled(b);
        super.setEnabled(enabled);
    }

    protected abstract void doEditAction();

    private class EventHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == button) {
                doEditAction();
            } else if (e.getSource() == checkBox) {
                setEnabled(checkBox.isSelected(), false);
                fireChangeEvent();
            }
        }
    }
}
