package org.jfree.chart.editor.themes;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 13-Nov-2009
 * Time: 11:27:51
 * Implementation of the ChartBorder interface.
 */
public class BasicChartBorder implements ChartBorder {
    static final BasicStroke DEFAULT_STROKE = ExtendedChartTheme.DEFAULT_BORDER_STROKE;
    static final Paint DEFAULT_PAINT = ExtendedChartTheme.DEFAULT_BORDER_PAINT;

    private boolean visible;
    private BasicStroke stroke;
    private Paint paint;

    public BasicChartBorder(boolean visible, BasicStroke stroke, Paint paint) {
        this.visible = visible;
        this.stroke = stroke;
        this.paint = paint;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public BasicStroke getStroke() {
        return stroke;
    }

    public void setStroke(BasicStroke stroke) {
        this.stroke = stroke;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
