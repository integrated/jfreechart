package org.jfree.chart.editor.themes;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 29-Apr-2009
 * Time: 15:23:46
 * To change this template use File | Settings | File Templates.
 */
public interface ChartBorder {
    boolean isVisible();

    void setVisible(boolean visible);

    BasicStroke getStroke();

    void setStroke(BasicStroke stroke);

    Paint getPaint();

    void setPaint(Paint paint);

    Object clone() throws CloneNotSupportedException;
}
