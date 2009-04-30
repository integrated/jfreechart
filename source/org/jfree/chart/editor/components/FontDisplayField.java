package org.jfree.chart.editor.components;

import org.jfree.chart.util.ResourceBundleWrapper;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * A field for displaying a font selection.  The display field itself is
 * read-only, to the developer must provide another mechanism to allow the
 * user to change the font.
 *
 * Code taken from JCommon (LGPL licence so this is OK). Adapted to change the method used to display the font name.
 *
 * @author David Gilbert
 */
public class FontDisplayField extends JTextField {

    /** The current font. */
    private Font displayFont;

    /** The resourceBundle for the localization. */
    protected static final ResourceBundle localizationResources =
            ResourceBundleWrapper.getBundle("org.jfree.chart.editor.LocalizationBundle");

    /**
     * Standard constructor - builds a FontDescriptionField initialised with
     * the specified font.
     *
     * @param font  the font.
     */
    public FontDisplayField(final Font font) {
        super("");
        setDisplayFont(font);
        setEnabled(false);
    }

    /**
     * Returns the current font.
     *
     * @return the font.
     */
    public Font getDisplayFont() {
        return this.displayFont;
    }

    /**
     * Sets the font.
     *
     * @param font  the font.
     */
    public void setDisplayFont(final Font font) {
        this.displayFont = font;
        setText(fontToString(this.displayFont));
    }

    /**
     * Returns a string representation of the specified font.
     *
     * @param font  the font.
     *
     * @return a string describing the font.
     */
    private String fontToString(final Font font) {
        if (font != null) {
            return font.getName() + ", " + font.getSize();
        }
        else {
            return localizationResources.getString("No_Font_Selected");
        }
    }

}
