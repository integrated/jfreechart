package org.jfree.chart.editor.themes;

import org.jfree.chart.ChartColor;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.util.PublicCloneable;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 29-Apr-2009
 * Time: 15:10:34
 * Additions to the DrawingSupplier interface that make it more versatile and also allow
 * support for the implementation to generate a gradient of paints between a start and end point.
 */
public interface ExtendedDrawingSupplier extends Cloneable, PublicCloneable, DrawingSupplier {
    /** The default fill paint sequence. */
    Paint[] DEFAULT_PAINT_SEQUENCE
            = ChartColor.createDefaultPaintArray();
    /** The default outline paint sequence. */
    Paint[] DEFAULT_OUTLINE_PAINT_SEQUENCE = new Paint[] {
            Color.lightGray};
    /** The default fill paint sequence. */
    Paint[] DEFAULT_FILL_PAINT_SEQUENCE = new Paint[] {
            Color.white};
    /** The default stroke sequence. */
    BasicStroke[] DEFAULT_STROKE_SEQUENCE = new BasicStroke[] {
            new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
                    BasicStroke.JOIN_BEVEL)};
    /** The default outline stroke sequence. */
    BasicStroke[] DEFAULT_OUTLINE_STROKE_SEQUENCE
            = new BasicStroke[] {new BasicStroke(1.0f, BasicStroke.CAP_SQUARE,
                    BasicStroke.JOIN_BEVEL)};
    /** The default shape sequence. */
    Shape[] DEFAULT_SHAPE_SEQUENCE
            = ThemeUtil.createStandardSeriesShapes();

    String getName();

    boolean isCalculatePaintGradient();

    void setCalculatePaintGradient(boolean b, int steps);

    boolean isCalculateFillPaintGradient();

    void setCalculateFillPaintGradient(boolean b, int steps);

    boolean isCalculateOutlinePaintGradient();

    void setCalculateOutlinePaintGradient(boolean b, int steps);

    void reset();

    BasicStroke[] getStrokeSequence();

    void setStrokeSequence(BasicStroke[] seq);

    BasicStroke[] getOutlineStrokeSequence();

    void setOutlineStrokeSequence(BasicStroke[] seq);

    Shape[] getShapeSequence();

    void setShapeSequence(Shape[] seq);

    Paint[] getPaintSequence();

    void setPaintSequence(Paint[] seq);

    Paint[] getFillPaintSequence();

    void setFillPaintSequence(Paint[] seq);

    Paint[] getOutlinePaintSequence();

    void setOutlinePaintSequence(Paint[] seq);
}
