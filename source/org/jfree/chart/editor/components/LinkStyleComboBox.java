package org.jfree.chart.editor.components;

import org.jfree.chart.plot.PieLabelLinkStyle;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 16-Mar-2009
 * Time: 15:20:24
 * To change this template use File | Settings | File Templates.
 */
public class LinkStyleComboBox extends AbstractListComboBox {
    protected Object[] getObjects() {
        return new PieLabelLinkStyle[] {
                PieLabelLinkStyle.STANDARD, PieLabelLinkStyle.QUAD_CURVE, PieLabelLinkStyle.CUBIC_CURVE
        };
    }

    protected String[] getLabels() {
        return new String[]{
                localizationResources.getString("Standard"),
                localizationResources.getString("Quad-Curve"),
                localizationResources.getString("Cubic-Curve")
        };
    }
}
