package org.jfree.chart.editor.components;

import org.jfree.ui.FontDisplayField;
import org.jfree.ui.FontChooserPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 16-Jan-2009
 * Time: 16:33:22
 * Displays font and allows editing of it.
 */
public class FontControl extends AbstractControl {
    private FontDisplayField fontField;
    public FontControl(Font font) {
        super(new FontDisplayField(font));
        fontField = (FontDisplayField) sample;
        fontField.setEditable(false);
        fontField.setFont(font.deriveFont(12f));
    }

    protected void doEditAction() {
        FontChooserPanel panel = new FontChooserPanel(fontField.getDisplayFont());
        int result =
            JOptionPane.showConfirmDialog(
                this, panel, localizationResources.getString("Font_Selection"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

        if (result == JOptionPane.OK_OPTION) {
            fontField.setDisplayFont(panel.getSelectedFont());
            fontField.setFont(panel.getSelectedFont().deriveFont(12f));
            fireChangeEvent();
        }
    }

    public Font getChosenFont() {
        return fontField.getDisplayFont();
    }
}
