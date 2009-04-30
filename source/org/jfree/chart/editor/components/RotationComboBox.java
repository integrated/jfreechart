package org.jfree.chart.editor.components;

import org.jfree.util.Rotation;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 16-Mar-2009
 * Time: 11:45:02
 * Combo box used for selecting the direction of rotation in a pie chart.
 */
public class RotationComboBox extends AbstractListComboBox {


    protected Object[] getObjects() {
        return new Rotation[] {
                Rotation.CLOCKWISE, Rotation.ANTICLOCKWISE
        };
    }

    protected String[] getLabels() {
        return new String[] {
                localizationResources.getString("Clockwise"),
                localizationResources.getString("Anti-Clockwise")
        };
    }
}
