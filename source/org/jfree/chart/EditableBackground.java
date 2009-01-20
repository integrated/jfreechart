package org.jfree.chart;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 19-Jan-2009
 * Time: 15:55:51
 * Denotes a class with the standard editable background methods. JFreeChart and Plot both have the same method
 * signatures for these methods - but (until now) no common interface to make it easier to deal with.
 */
public interface EditableBackground {

    public Image getBackgroundImage();

    public void setBackgroundImage(Image image);

    public int getBackgroundImageAlignment();

    public void setBackgroundImageAlignment(int alignment);

    public float getBackgroundImageAlpha();

    public void setBackgroundImageAlpha(float alpha);

    public Paint getBackgroundPaint();

    public void setBackgroundPaint(Paint paint);
}
