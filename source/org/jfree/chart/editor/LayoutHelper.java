package org.jfree.chart.editor;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 12-Jan-2009
 * Time: 09:49:19
 * Standard methods for laying out chart editor GUI's.
 */
class LayoutHelper {

    static GridBagConstraints getNewConstraints() {
        return new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,
                new Insets(0,0,0,0), 0,0);
    }

    static void startNewRow(GridBagConstraints c) {
        c.gridx=0;
        c.gridy++;
        c.weightx = 0;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
    }
}
