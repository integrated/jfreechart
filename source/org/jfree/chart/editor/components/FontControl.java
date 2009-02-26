package org.jfree.chart.editor.components;

import org.jfree.ui.FontDisplayField;
import org.jfree.ui.FontChooserPanel;
import org.jfree.chart.editor.DefaultChartEditor;

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
        fontField.setFont(new Font(font.getName(), font.getStyle(), 12));
    }

    protected void doEditAction() {
        FontChooserPanel panel = DefaultChartEditor.getDefaultFontChooserPanel();
        panel.setSelectedFont(getChosenFont());
        int result =
            JOptionPane.showConfirmDialog(
                this, panel, localizationResources.getString("Font_Selection"),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

        if (result == JOptionPane.OK_OPTION) {
            Font chosenFont = panel.getSelectedFont();
            fontField.setDisplayFont(chosenFont);
            fontField.setFont(new Font(chosenFont.getName(), chosenFont.getStyle(), 12));
            fireChangeEvent();
        }
    }

    public Font getChosenFont() {
        return fontField.getDisplayFont();
    }
}
