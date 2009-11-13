package org.jfree.chart.editor.themes;

import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.util.StringUtil;

import java.awt.*;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 13-Nov-2009
 * Time: 11:51:51
 * Implementation of the iPlusDrawingSupplier interface.
 */
public class BasicDrawingSupplier implements ExtendedDrawingSupplier {

    /** The current paint index. */
    private int paintIndex;

    /** The current outline paint index. */
    private int outlinePaintIndex;

    /** The current fill paint index. */
    private int fillPaintIndex;

    /** The stroke sequence. */
    private transient BasicStroke[] strokeSequence;

    /** The current stroke index. */
    private int strokeIndex;

    /** The outline stroke sequence. */
    private transient BasicStroke[] outlineStrokeSequence;

    /** The current outline stroke index. */
    private int outlineStrokeIndex;

    /** The shape sequence. */
    private transient Shape[] shapeSequence;

    /** The current shape index. */
    private int shapeIndex;

    private static final String IPLUS_DEFAULTS = "Default Drawing Supplier";
    private static final int RED = 0, GREEN = 1, BLUE = 2, ALPHA = 3;

    private boolean calculatePaintGradient = false;
    private ColorGradientSupplier colorGradSupplier = null;
    private Paint[] paintSequence = null;

    private boolean calculateFillPaintGradient = false;
    private ColorGradientSupplier fillColorGradSupplier = null;
    private Paint[] fillPaintSequence = null;

    private boolean calculateOutlinePaintGradient = false;
    private ColorGradientSupplier outlineGradSupplier = null;
    private Paint[] outlinePaintSequence = null;

    private String name;

    public BasicDrawingSupplier(String name) {
        this(name, DEFAULT_PAINT_SEQUENCE, DEFAULT_FILL_PAINT_SEQUENCE,
             DEFAULT_OUTLINE_PAINT_SEQUENCE,
             DEFAULT_STROKE_SEQUENCE,
             DEFAULT_OUTLINE_STROKE_SEQUENCE,
             DEFAULT_SHAPE_SEQUENCE);
    }

    public BasicDrawingSupplier(String name, Paint[] paintSequence, Paint[] fillPaintSequence, Paint[] outlinePaintSequence,
                                BasicStroke[] strokeSequence, BasicStroke[] outlineStrokeSequence, Shape[] shapeSequence) {
        this.paintSequence = paintSequence;
        this.fillPaintSequence = fillPaintSequence;
        this.outlinePaintSequence = outlinePaintSequence;
        this.strokeSequence = strokeSequence;
        this.outlineStrokeSequence = outlineStrokeSequence;
        this.shapeSequence = shapeSequence;
        setName(name);
    }

    public BasicDrawingSupplier(String name, Paint[] paintSequence, Paint[] outlinePaintSequence,
                                BasicStroke[] strokeSequence, BasicStroke[] outlineStrokeSequence, Shape[] shapeSequence) {
        this.paintSequence = paintSequence;
        this.fillPaintSequence = DEFAULT_FILL_PAINT_SEQUENCE;
        this.outlinePaintSequence = outlinePaintSequence;
        this.strokeSequence = strokeSequence;
        this.outlineStrokeSequence = outlineStrokeSequence;
        this.shapeSequence = shapeSequence;
        setName(name);
    }

    public BasicDrawingSupplier(String name, final DrawingSupplier source) {

        {
            java.util.List paintObjList = iterateIntoList(new SupplierIterator(){
                public Object next() {
                    return source.getNextPaint();
                }
            });
            this.paintSequence = (Paint[])paintObjList.toArray(new Paint[paintObjList.size()]);

            paintObjList = iterateIntoList(new SupplierIterator(){
                public Object next() {
                    return source.getNextFillPaint();
                }
            });
            this.fillPaintSequence = (Paint[])paintObjList.toArray(new Paint[paintObjList.size()]);

            paintObjList = iterateIntoList(new SupplierIterator(){
                public Object next() {
                    return source.getNextOutlinePaint();
                }
            });
            this.outlinePaintSequence = (Paint[])paintObjList.toArray(new Paint[paintObjList.size()]);
        }
        {
            java.util.List basicStrokeObjList = iterateIntoList(new SupplierIterator(){
                public Object next() {
                    return source.getNextStroke();
                }
            });

            this.strokeSequence = (BasicStroke[]) basicStrokeObjList.toArray(new BasicStroke[basicStrokeObjList.size()]);

            basicStrokeObjList = iterateIntoList(new SupplierIterator(){
                public Object next() {
                    return source.getNextOutlineStroke();
                }
            });
            this.outlineStrokeSequence = (BasicStroke[]) basicStrokeObjList.toArray(new BasicStroke[basicStrokeObjList.size()]);
        }
        {
            java.util.List shapeObjList = iterateIntoList(new SupplierIterator(){
                public Object next() {
                    return source.getNextShape();
                }
            });
            this.shapeSequence = (Shape[])shapeObjList.toArray(new Shape[shapeObjList.size()]);
        }
        setName(name);
    }

    private java.util.List iterateIntoList(Iterator i) {
        java.util.List list = new ArrayList();
        Object o;
        while(!list.contains(o = i.next())) {
            list.add(o);
        }
        return list;
    }

    private void setName(String name) {
        if(!StringUtil.isEmpty(name)) {
            this.name = name;
        } else {
            this.name = IPLUS_DEFAULTS;
        }
    }

    public String getName() {
        return name;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean isCalculatePaintGradient() {
        return calculatePaintGradient;
    }

    public void setCalculatePaintGradient(boolean b, int steps) {
        this.calculatePaintGradient = b;
        if(!b) { return; }
        colorGradSupplier = new ColorGradientSupplier(paintSequence[0], paintSequence[paintSequence.length-1], steps);
    }

    public boolean isCalculateFillPaintGradient() {
        return calculateFillPaintGradient;
    }

    public void setCalculateFillPaintGradient(boolean b, int steps) {
        this.calculateFillPaintGradient = b;
        if(!b) { return; }
        fillColorGradSupplier = new ColorGradientSupplier(fillPaintSequence[0],
                fillPaintSequence[fillPaintSequence.length-1], steps);
    }

    public boolean isCalculateOutlinePaintGradient() {
        return calculateOutlinePaintGradient;
    }

    public void setCalculateOutlinePaintGradient(boolean b, int steps) {
        this.calculateOutlinePaintGradient = b;
        if(!b) { return; }
        outlineGradSupplier = new ColorGradientSupplier(outlinePaintSequence[0],
                outlinePaintSequence[outlinePaintSequence.length-1], steps);
    }


    /**
     * Resets the drawing supplier to its initial state, so colours/shapes/strokes etc will be supplied starting
     * from the beginning of the sequences.
     */
    public void reset() {
        this.paintIndex = 0;
        if(this.colorGradSupplier != null) {
            this.colorGradSupplier.reset();
        }
        this.outlinePaintIndex = 0;
        if(this.outlineGradSupplier != null) {
            this.outlineGradSupplier.reset();
        }
        this.fillPaintIndex = 0;
        if(this.fillColorGradSupplier != null) {
            this.fillColorGradSupplier.reset();
        }
        this.strokeIndex = 0;
        this.outlineStrokeIndex = 0;
        this.shapeIndex = 0;
    }

    public Paint getNextPaint() {
        if(calculatePaintGradient) {
            return colorGradSupplier.getNextPaint();
        } else {
            Paint result
                = this.paintSequence[this.paintIndex % this.paintSequence.length];
            this.paintIndex++;
            return result;
        }
    }

    public Paint getNextOutlinePaint() {
        if(calculateOutlinePaintGradient) {
            return outlineGradSupplier.getNextPaint();
        } else {
            Paint result = this.outlinePaintSequence[
                this.outlinePaintIndex % this.outlinePaintSequence.length];
            this.outlinePaintIndex++;
            return result;
        }
    }

    public Paint getNextFillPaint() {
        if(calculateFillPaintGradient) {
            return fillColorGradSupplier.getNextPaint();
        } else {
            Paint result = this.fillPaintSequence[this.fillPaintIndex
                % this.fillPaintSequence.length];
            this.fillPaintIndex++;
            return result;
        }
    }

    /**
     * Returns the next stroke in the sequence.
     *
     * @return The stroke.
     */
    public Stroke getNextStroke() {
        Stroke result = this.strokeSequence[
                this.strokeIndex % this.strokeSequence.length];
        this.strokeIndex++;
        return result;
    }

    /**
     * Returns the next outline stroke in the sequence.
     *
     * @return The stroke.
     */
    public Stroke getNextOutlineStroke() {
        Stroke result = this.outlineStrokeSequence[
                this.outlineStrokeIndex % this.outlineStrokeSequence.length];
        this.outlineStrokeIndex++;
        return result;
    }

    /**
     * Returns the next shape in the sequence.
     *
     * @return The shape.
     */
    public Shape getNextShape() {
        Shape result = this.shapeSequence[
                this.shapeIndex % this.shapeSequence.length];
        this.shapeIndex++;
        return result;
    }

    public BasicStroke[] getStrokeSequence() {
        BasicStroke[] retVal = new BasicStroke[strokeSequence.length];
        System.arraycopy(strokeSequence, 0, retVal, 0, strokeSequence.length);
        return retVal;
    }

    public void setStrokeSequence(BasicStroke[] seq) {
        BasicStroke[] inVal = new BasicStroke[seq.length];
        System.arraycopy(seq, 0, inVal, 0, seq.length);
        this.strokeSequence = inVal;
        reset();
    }

    public BasicStroke[] getOutlineStrokeSequence() {
        BasicStroke[] retVal = new BasicStroke[outlineStrokeSequence.length];
        System.arraycopy(outlineStrokeSequence, 0, retVal, 0, outlineStrokeSequence.length);
        return retVal;
    }

    public void setOutlineStrokeSequence(BasicStroke[] seq) {
        BasicStroke[] inVal = new BasicStroke[seq.length];
        System.arraycopy(seq, 0, inVal, 0, seq.length);
        this.outlineStrokeSequence = inVal;
        reset();
    }

    public Shape[] getShapeSequence() {
        Shape[] retVal = new Shape[shapeSequence.length];
        System.arraycopy(shapeSequence, 0, retVal, 0, shapeSequence.length);
        return retVal;
    }

    public void setShapeSequence(Shape[] seq) {
        Shape[] inVal = new Shape[seq.length];
        System.arraycopy(seq, 0, inVal, 0, seq.length);
        this.shapeSequence = inVal;
        reset();
    }

    public Paint[] getPaintSequence() {
        Paint[] retVal = new Paint[paintSequence.length];
        System.arraycopy(paintSequence, 0, retVal, 0, paintSequence.length);
        return retVal;
    }

    public void setPaintSequence(Paint[] seq) {
        Paint[] inVal = new Paint[seq.length];
        System.arraycopy(seq, 0, inVal, 0, seq.length);
        this.paintSequence = inVal;
        reset();
    }

    public Paint[] getFillPaintSequence() {
        Paint[] retVal = new Paint[fillPaintSequence.length];
        System.arraycopy(fillPaintSequence, 0, retVal, 0, fillPaintSequence.length);
        return retVal;
    }

    public void setFillPaintSequence(Paint[] seq) {
        Paint[] inVal = new Paint[seq.length];
        System.arraycopy(seq, 0, inVal, 0, seq.length);
        this.fillPaintSequence = inVal;
        reset();
    }

    public Paint[] getOutlinePaintSequence() {
        Paint[] retVal = new Paint[outlinePaintSequence.length];
        System.arraycopy(outlinePaintSequence, 0, retVal, 0, outlinePaintSequence.length);
        return retVal;
    }

    public void setOutlinePaintSequence(Paint[] seq) {
        Paint[] inVal = new Paint[seq.length];
        System.arraycopy(seq, 0, inVal, 0, seq.length);
        this.outlinePaintSequence = inVal;
        reset();
    }

    private class ColorGradientSupplier {
        private int pointer;
        private Color[] paints;

        public ColorGradientSupplier(Paint start, Paint end, int steps) {
            this.pointer = 0;
            paints = new Color[steps];

            Color cStart = extractColorOrDefault(start, Color.WHITE);
            Color cEnd = extractColorOrDefault(end, Color.BLACK);


            int[] startVals = getComponents(cStart);
            int[] endVals = getComponents(cEnd);
            int[] diffVals = new int[4];
            for(int i = 0; i < diffVals.length; i++) {
                diffVals[i] = endVals[i] - startVals[i] ;
            }
            int[] currentVals = new int[diffVals.length];
            System.arraycopy(startVals, 0, currentVals, 0, diffVals.length);

            paints[0] = cStart;
            for(int i = 1; i < steps-1; i++) {
                for(int j = 0; j < diffVals.length; j++) {
                    currentVals[j] = startVals[j] + (int)((i/((double)(steps-1)))*diffVals[j]);
                }
                paints[i] = new Color(currentVals[RED], currentVals[GREEN], currentVals[BLUE], currentVals[ALPHA]);
            }
            paints[steps-1] = cEnd;
        }

        public void reset() {
            this.pointer = 0;
        }

        private Color extractColorOrDefault(Paint p, Color defaultColor) {
            if(p instanceof Color) {
                return (Color) p;
            } else if (p instanceof GradientPaint) {
                return ((GradientPaint)p).getColor1();
            } else {
                return defaultColor;
            }
        }

        public Paint getNextPaint() {
            Color retVal = paints[pointer];
            pointer = (pointer+1)%paints.length;
            return retVal;
        }

        private int[] getComponents(Color c) {
            int[] cVals = new int[4];
            cVals[RED] = c.getRed();
            cVals[GREEN] = c.getGreen();
            cVals[BLUE] = c.getBlue();
            cVals[ALPHA] = c.getAlpha();
            return cVals;
        }
    }

    private class SupplierIterator implements Iterator {

        public void remove() {
            throw new UnsupportedOperationException("Not supported by this class");
        }

        public boolean hasNext() {
            return true;
        }

        public Object next() {
            return null;
        }
    }

}
