package org.jfree.chart.editor.themes;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 04-Feb-2009
 * Time: 14:53:52
 * Denotes a theme object that uses a ChartBorder object for its outline.
 */
public interface EditableBorder {

    public void setBorder(ChartBorder border);

    public void setBorder(boolean visible, Paint paint, BasicStroke stroke);

    public ChartBorder getBorder();
}
