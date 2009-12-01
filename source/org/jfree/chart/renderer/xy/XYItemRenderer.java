/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * -------------------
 * XYItemRenderer.java
 * -------------------
 * (C) Copyright 2001-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Mark Watson (www.markwatson.com);
 *                   Sylvain Vieujot;
 *                   Focus Computer Services Limited;
 *                   Richard Atkinson;
 *
 * Changes
 * -------
 * 19-Oct-2001 : Version 1, based on code by Mark Watson (DG);
 * 22-Oct-2001 : Renamed DataSource.java --> Dataset.java etc. (DG);
 * 13-Dec-2001 : Changed return type of drawItem from void --> Shape.  The area
 *               returned can be used as the tooltip region.
 * 23-Jan-2002 : Added DrawInfo parameter to drawItem() method (DG);
 * 28-Mar-2002 : Added a property change listener mechanism.  Now renderers do
 *               not have to be immutable (DG);
 * 04-Apr-2002 : Added the initialise() method (DG);
 * 09-Apr-2002 : Removed the translated zero from the drawItem method, it can
 *               be calculated inside the initialise method if it is required.
 *               Added a new getToolTipGenerator() method.  Changed the return
 *               type for drawItem() to void (DG);
 * 24-May-2002 : Added ChartRenderingInfo the initialise method API (DG);
 * 25-Jun-2002 : Removed redundant import (DG);
 * 20-Aug-2002 : Added get/setURLGenerator methods to interface (DG);
 * 02-Oct-2002 : Fixed errors reported by Checkstyle (DG);
 * 18-Nov-2002 : Added methods for drawing grid lines (DG);
 * 17-Jan-2003 : Moved plot classes into a separate package (DG);
 * 27-Jan-2003 : Added shape lookup table (DG);
 * 05-Jun-2003 : Added domain and range grid bands (sponsored by Focus Computer
 *               Services Ltd) (DG);
 * 27-Jul-2003 : Added getRangeType() to support stacked XY area charts (RA);
 * 16-Sep-2003 : Changed ChartRenderingInfo --> PlotRenderingInfo (DG);
 * 25-Feb-2004 : Replaced CrosshairInfo with CrosshairState.  Renamed
 *               XYToolTipGenerator --> XYItemLabelGenerator (DG);
 * 26-Feb-2004 : Added lots of new methods (DG);
 * 30-Apr-2004 : Added getRangeExtent() method (DG);
 * 06-May-2004 : Added methods for controlling item label visibility (DG);
 * 13-May-2004 : Removed property change listener mechanism (DG);
 * 18-May-2004 : Added item label font and paint methods (DG);
 * 10-Sep-2004 : Removed redundant getRangeType() method (DG);
 * 06-Oct-2004 : Replaced getRangeExtent() with findRangeBounds() and added
 *               findDomainBounds (DG);
 * 23-Nov-2004 : Changed drawRangeGridLine() --> drawRangeLine() (DG);
 * 07-Jan-2005 : Removed deprecated method (DG);
 * 24-Feb-2005 : Now extends LegendItemSource (DG);
 * 20-Apr-2005 : Renamed XYLabelGenerator --> XYItemLabelGenerator (DG);
 * ------------- JFREECHART 1.0.x ---------------------------------------------
 * 19-Apr-2007 : Deprecated seriesVisible and seriesVisibleInLegend flags (DG);
 * 20-Apr-2007 : Deprecated paint, fillPaint, outlinePaint, stroke,
 *               outlineStroke, shape, itemLabelsVisible, itemLabelFont,
 *               itemLabelPaint, positiveItemLabelPosition,
 *               negativeItemLabelPosition and createEntities override
 *               fields (DG);
 *
 */

package org.jfree.chart.renderer.xy;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.renderer.ItemRenderer;
import org.jfree.chart.annotations.XYAnnotation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.labels.XYSeriesLabelGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.*;
import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.Layer;

/**
 * Interface for rendering the visual representation of a single (x, y) item on
 * an {@link XYPlot}.
 * <p>
 * To support cloning charts, it is recommended that renderers implement both
 * the {@link Cloneable} and <code>PublicCloneable</code> interfaces.
 */
public interface XYItemRenderer extends ItemRenderer {

    /**
     * Convenience method that wraps getPlot() and automatically casts the plot object to XYPlot
     * since it must be a XYPlot object in this case.
     * @return The plot object that this renderer is used with.
     */
    XYPlot getXYPlot();

    /**
     * Returns the lower and upper bounds (range) of the x-values in the
     * specified dataset.
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @return The range.
     */
    public Range findDomainBounds(XYDataset dataset);

    /**
     * Returns the lower and upper bounds (range) of the y-values in the
     * specified dataset.  The implementation of this method will take
     * into account the presentation used by the renderers (for example,
     * a renderer that "stacks" values will return a bigger range than
     * a renderer that doesn't).
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @return The range (or <code>null</code> if the dataset is
     *         <code>null</code> or empty).
     */
    public Range findRangeBounds(XYDataset dataset);

    //// LEGEND ITEM LABEL GENERATOR //////////////////////////////////////////

    /**
     * Returns the legend item label generator.
     *
     * @return The legend item label generator (never <code>null</code>).
     *
     * @see #setLegendItemLabelGenerator(XYSeriesLabelGenerator)
     */
    public XYSeriesLabelGenerator getLegendItemLabelGenerator();

    /**
     * Sets the legend item label generator and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> not permitted).
     */
    public void setLegendItemLabelGenerator(XYSeriesLabelGenerator generator);


    //// TOOL TIP GENERATOR ///////////////////////////////////////////////////

    /**
     * Returns the tool tip generator for a data item.
     *
     * @param row  the row index (zero based).
     * @param column  the column index (zero based).
     *
     * @return The generator (possibly <code>null</code>).
     */
    public XYToolTipGenerator getToolTipGenerator(int row, int column);

    /**
     * Returns the tool tip generator for a series.
     *
     * @param series  the series index (zero based).
     *
     * @return The generator (possibly <code>null</code>).
     *
     * @see #setSeriesToolTipGenerator(int, XYToolTipGenerator)
     */
    public XYToolTipGenerator getSeriesToolTipGenerator(int series);

    /**
     * Sets the tool tip generator for a series and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero based).
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #getSeriesToolTipGenerator(int)
     */
    public void setSeriesToolTipGenerator(int series,
                                          XYToolTipGenerator generator);

    /**
     * Returns the base tool tip generator.
     *
     * @return The generator (possibly <code>null</code>).
     *
     * @see #setBaseToolTipGenerator(XYToolTipGenerator)
     */
    public XYToolTipGenerator getBaseToolTipGenerator();

    /**
     * Sets the base tool tip generator and sends a {@link RendererChangeEvent}
     * to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #getBaseToolTipGenerator()
     */
    public void setBaseToolTipGenerator(XYToolTipGenerator generator);

    //// URL GENERATOR ////////////////////////////////////////////////////////


    /**
     * Returns the URL generator for HTML image maps.
     *
     * @return The URL generator (possibly null).
     */
    public XYURLGenerator getURLGenerator();

    /**
     * Sets the URL generator for HTML image maps.
     *
     * @param urlGenerator the URL generator (null permitted).
     */
    public void setURLGenerator(XYURLGenerator urlGenerator);

    //// ITEM LABEL GENERATOR /////////////////////////////////////////////////

    /**
     * Returns the item label generator for a data item.
     *
     * @param row  the row index (zero based).
     * @param column  the column index (zero based).
     *
     * @return The generator (possibly <code>null</code>).
     */
    public XYItemLabelGenerator getItemLabelGenerator(int row, int column);

    /**
     * Returns the item label generator for a series.
     *
     * @param series  the series index (zero based).
     *
     * @return The generator (possibly <code>null</code>).
     *
     * @see #setSeriesItemLabelGenerator(int, XYItemLabelGenerator)
     */
    public XYItemLabelGenerator getSeriesItemLabelGenerator(int series);

    /**
     * Sets the item label generator for a series and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param series  the series index (zero based).
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #getSeriesItemLabelGenerator(int)
     */
    public void setSeriesItemLabelGenerator(int series,
                                            XYItemLabelGenerator generator);

    // FIXME:

    /**
     * Returns the base item label generator.
     *
     * @return The generator (possibly <code>null</code>).
     *
     * @see #setBaseItemLabelGenerator(XYItemLabelGenerator)
     */
    public XYItemLabelGenerator getBaseItemLabelGenerator();

    /**
     * Sets the base item label generator and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @see #getBaseItemLabelGenerator()
     */
    public void setBaseItemLabelGenerator(XYItemLabelGenerator generator);

    // CREATE ENTITIES
    // FIXME:  these methods should be defined

//    public boolean getItemCreateEntity(int series, int item);
//
//    public Boolean getSeriesCreateEntities(int series);
//
//    public void setSeriesCreateEntities(int series, Boolean create);
//
//    public void setSeriesCreateEntities(int series, Boolean create,
//            boolean notify);
//
//    public boolean getBaseCreateEntities();
//
//    public void setBaseCreateEntities(boolean create);
//
//    public void setBaseCreateEntities(boolean create, boolean notify);

    //// ANNOTATIONS //////////////////////////////////////////////////////////

    /**
     * Adds an annotation and sends a {@link RendererChangeEvent} to all
     * registered listeners.  The annotation is added to the foreground
     * layer.
     *
     * @param annotation  the annotation (<code>null</code> not permitted).
     */
    public void addAnnotation(XYAnnotation annotation);

    /**
     * Adds an annotation to the specified layer.
     *
     * @param annotation  the annotation (<code>null</code> not permitted).
     * @param layer  the layer (<code>null</code> not permitted).
     */
    public void addAnnotation(XYAnnotation annotation, Layer layer);

    /**
     * Removes the specified annotation and sends a {@link RendererChangeEvent}
     * to all registered listeners.
     *
     * @param annotation  the annotation to remove (<code>null</code> not
     *                    permitted).
     *
     * @return A boolean to indicate whether or not the annotation was
     *         successfully removed.
     */
    public boolean removeAnnotation(XYAnnotation annotation);

    /**
     * Removes all annotations and sends a {@link RendererChangeEvent}
     * to all registered listeners.
     */
    public void removeAnnotations();

    /**
     * Draws all the annotations for the specified layer.
     *
     * @param g2  the graphics device.
     * @param dataArea  the data area.
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param layer  the layer.
     * @param info  the plot rendering info.
     */
    public void drawAnnotations(Graphics2D g2,
                                Rectangle2D dataArea,
                                ValueAxis domainAxis,
                                ValueAxis rangeAxis,
                                Layer layer,
                                PlotRenderingInfo info);

    //// DRAWING //////////////////////////////////////////////////////////////

    /**
     * Initialises the renderer then returns the number of 'passes' through the
     * data that the renderer will require (usually just one).  This method
     * will be called before the first item is rendered, giving the renderer
     * an opportunity to initialise any state information it wants to maintain.
     * The renderer can do nothing if it chooses.
     *
     * @param g2  the graphics device.
     * @param dataArea  the area inside the axes.
     * @param plot  the plot.
     * @param dataset  the dataset.
     * @param info  an optional info collection object to return data back to
     *              the caller.
     *
     * @return The number of passes the renderer requires.
     */
    public XYItemRendererState initialise(Graphics2D g2,
                                          Rectangle2D dataArea,
                                          XYPlot plot,
                                          XYDataset dataset,
                                          PlotRenderingInfo info);

    /**
     * Called for each item to be plotted.
     * <p>
     * The {@link XYPlot} can make multiple passes through the dataset,
     * depending on the value returned by the renderer's initialise() method.
     *
     * @param g2  the graphics device.
     * @param state  the renderer state.
     * @param dataArea  the area within which the data is being rendered.
     * @param info  collects drawing info.
     * @param plot  the plot (can be used to obtain standard color
     *              information etc).
     * @param domainAxis  the domain axis.
     * @param rangeAxis  the range axis.
     * @param dataset  the dataset.
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     * @param crosshairState  crosshair information for the plot
     *                        (<code>null</code> permitted).
     * @param pass  the pass index.
     */
    public void drawItem(Graphics2D g2,
                         XYItemRendererState state,
                         Rectangle2D dataArea,
                         PlotRenderingInfo info,
                         XYPlot plot,
                         ValueAxis domainAxis,
                         ValueAxis rangeAxis,
                         XYDataset dataset,
                         int series,
                         int item,
                         CrosshairState crosshairState,
                         int pass);

    /**
     * Fills a band between two values on the axis.  This can be used to color
     * bands between the grid lines.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param axis  the domain axis.
     * @param dataArea  the data area.
     * @param start  the start value.
     * @param end  the end value.
     */
    public void fillDomainGridBand(Graphics2D g2,
                                   XYPlot plot,
                                   ValueAxis axis,
                                   Rectangle2D dataArea,
                                   double start, double end);

    /**
     * Fills a band between two values on the range axis.  This can be used to
     * color bands between the grid lines.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param axis  the range axis.
     * @param dataArea  the data area.
     * @param start  the start value.
     * @param end  the end value.
     */
    public void fillRangeGridBand(Graphics2D g2,
                                  XYPlot plot,
                                  ValueAxis axis,
                                  Rectangle2D dataArea,
                                  double start, double end);

    /**
     * Draws a grid line against the domain axis.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param axis  the value axis.
     * @param dataArea  the area for plotting data (not yet adjusted for any
     *                  3D effect).
     * @param value  the value.
     */
    public void drawDomainGridLine(Graphics2D g2,
                                   XYPlot plot,
                                   ValueAxis axis,
                                   Rectangle2D dataArea,
                                   double value);

    /**
     * Draws a line perpendicular to the range axis.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param axis  the value axis.
     * @param dataArea  the area for plotting data.
     * @param value  the data value.
     * @param paint  the paint (<code>null</code> not permitted).
     * @param stroke  the stroke (<code>null</code> not permitted).
     */
    public void drawRangeLine(Graphics2D g2, XYPlot plot, ValueAxis axis,
            Rectangle2D dataArea, double value, Paint paint, Stroke stroke);

    /**
     * Draws the specified <code>marker</code> against the domain axis.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param axis  the value axis.
     * @param marker  the marker.
     * @param dataArea  the axis data area.
     */
    public void drawDomainMarker(Graphics2D g2, XYPlot plot, ValueAxis axis,
            Marker marker, Rectangle2D dataArea);

    /**
     * Draws a horizontal line across the chart to represent a 'range marker'.
     *
     * @param g2  the graphics device.
     * @param plot  the plot.
     * @param axis  the value axis.
     * @param marker  the marker line.
     * @param dataArea  the axis data area.
     */
    public void drawRangeMarker(Graphics2D g2, XYPlot plot, ValueAxis axis,
            Marker marker, Rectangle2D dataArea);

    /**
     * Sets the item label generator for ALL series and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @deprecated As of version 1.0.6, this override setting should not be
     *     used.  You can use the base setting instead
     *     ({@link #setBaseItemLabelGenerator(XYItemLabelGenerator)}).
     */
    public void setItemLabelGenerator(XYItemLabelGenerator generator);

    /**
     * Sets the tool tip generator for ALL series and sends a
     * {@link RendererChangeEvent} to all registered listeners.
     *
     * @param generator  the generator (<code>null</code> permitted).
     *
     * @deprecated As of version 1.0.6, this override setting should not be
     *     used.  You can use the base setting instead
     *     ({@link #setBaseToolTipGenerator(XYToolTipGenerator)}).
     */
    public void setToolTipGenerator(XYToolTipGenerator generator);
}
