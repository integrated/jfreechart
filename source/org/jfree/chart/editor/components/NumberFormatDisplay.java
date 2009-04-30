package org.jfree.chart.editor.components;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 16-Mar-2009
 * Time: 15:43:54
 * To change this template use File | Settings | File Templates.
 */
public class NumberFormatDisplay extends AbstractControl {
    protected JTextField field;

    public NumberFormatDisplay(String formatString) {
        super(new JTextField(formatString, 15), false);
        this.field = (JTextField)sample;
        field.setEditable(false);
    }

    public String getFormatString() {
        return field.getText();
    }

    /**
     * override in sub-classes to alter the field.
     */
    protected void doEditAction() {

    }
}
