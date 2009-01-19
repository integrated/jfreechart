package org.jfree.chart.editor.components;

import org.jfree.ui.VerticalAlignment;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 19-Jan-2009
 * Time: 14:49:03
 * Allows a choice between VerticalAlignment objects
 */
public class VerticalAlignmentComboBox extends AbstractListComboBox {
    private static final VerticalAlignment[] ALIGNMENTS = new VerticalAlignment[] {
            VerticalAlignment.CENTER, VerticalAlignment.TOP, VerticalAlignment.BOTTOM
    };
    private static final String[] LABELS = new String[] {
            localizationResources.getString("Centre"),
            localizationResources.getString("Top"),
            localizationResources.getString("Bottom")
    };

    protected Object[] getObjects() {
        return ALIGNMENTS;
    }

    protected String[] getLabels() {
        return LABELS;
    }
}
