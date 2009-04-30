package org.jfree.chart.editor.components;

import java.awt.*;
import java.awt.color.ColorSpace;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 27-Feb-2009
 * Time: 15:35:49
 * To change this template use File | Settings | File Templates.
 */
public class NamedColor extends Color {
    private String name;
    public NamedColor(String name, ColorSpace cspace, float[] components, float alpha) {
        super(cspace, components, alpha);
        setName(name);
    }

    public NamedColor(String name, float r, float g, float b) {
        super(r,g,b);
        setName(name);
    }

    public NamedColor(String name, float r, float g, float b, float a) {
        super(r,g,b,a);
        setName(name);
    }

    public NamedColor(String name, int rgb) {
        super(rgb);
        setName(name);
    }

    public NamedColor(String name, int rgba, boolean hasAlpha) {
        super(rgba, hasAlpha);
        setName(name);
    }

    public NamedColor(String name, int r, int g , int b) {
        super(r,g,b);
        setName(name);
    }

    public NamedColor(String name, int r, int g , int b, int a) {
        super(r,g,b,a);
        setName(name);
    }

    public NamedColor(String name, Color c) {
        super(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        setName(name);
    }


    public String getName() {
        return name;
    }

    private void setName(String name) {
        if(name == null) {
            throw new IllegalArgumentException("Name of a NamedColor cannot be null");
        }
        this.name = name;
    }

    public boolean equals(Object o) {
        return o instanceof NamedColor && super.equals(o) && ((NamedColor)o).name.equals(name);
    }

    public int hashcode() {
        return name.hashCode() + super.hashCode();
    }

    public String toString() {
        return name + " ("+getRed()+","+getGreen()+","+getBlue()+","+getAlpha()+")";
    }
}
