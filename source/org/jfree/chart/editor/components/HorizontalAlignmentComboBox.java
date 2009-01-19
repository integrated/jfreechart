package org.jfree.chart.editor.components;

import org.jfree.ui.HorizontalAlignment;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 19-Jan-2009
 * Time: 14:34:51
 * Displays (and lets the user select from) the possibly horizontal alignments.
 */
public class HorizontalAlignmentComboBox extends AbstractListComboBox {
    private static final HorizontalAlignment[] ALIGNMENTS = new HorizontalAlignment[] {
            HorizontalAlignment.CENTER, HorizontalAlignment.LEFT, HorizontalAlignment.RIGHT
    };
    private static final String[] LABELS = new String[] {
            localizationResources.getString("Centre"),
            localizationResources.getString("Left"),
            localizationResources.getString("Right")
    };


    protected Object[] getObjects() {
        return ALIGNMENTS;
    }

    protected String[] getLabels() {
        return LABELS;
    }
}
