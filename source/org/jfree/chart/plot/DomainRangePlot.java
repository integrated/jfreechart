package org.jfree.chart.plot;

import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.Dataset;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RectangleEdge;
import org.jfree.chart.annotations.Annotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.renderer.ItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.*;
import java.util.Map;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 30-Nov-2009
 * Time: 11:00:29
 * Added this interface for common methods between CategoryPlot and XYPlot as its unwieldy to keep writing code
 * for handling both classes when the method calls are often the same.
 */
public interface DomainRangePlot extends Cloneable, RendererChangeListener {
    /**
     * Returns the drawing supplier for the plot.
     *
     * @return The drawing supplier (possibly <code>null</code>).
     *
     * @see #setDrawingSupplier(DrawingSupplier)
     */
    DrawingSupplier getDrawingSupplier();

    /**
     * Sets the drawing supplier for the plot and sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.  The drawing
     * supplier is responsible for supplying a limitless (possibly repeating)
     * sequence of <code>Paint</code>, <code>Stroke</code> and
     * <code>Shape</code> objects that the plot's renderer(s) can use to
     * populate its (their) tables.
     *
     * @param supplier  the new supplier.
     *
     * @see #getDrawingSupplier()
     */
    void setDrawingSupplier(DrawingSupplier supplier);

    /**
     * Returns the number of renderer slots for this plot.
     *
     * @return The number of renderer slots.
     *
     * @since 1.0.11
     */
    int getRendererCount();

    /**
     * Returns the renderer for the primary dataset.
     *
     * @return The item renderer (possibly <code>null</code>).
     *
     * @see #setRenderer(ItemRenderer)
     */
    ItemRenderer getBasicRenderer();

    /**
     * Returns the renderer for a dataset, or <code>null</code>.
     *
     * @param index  the renderer index.
     *
     * @return The renderer (possibly <code>null</code>).
     *
     * @see #setRenderer(int, ItemRenderer)
     */
    ItemRenderer getBasicRenderer(int index);

    /**
     * Sets the renderer for the primary dataset and sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.  If the renderer
     * is set to <code>null</code>, no data will be displayed.
     *
     * @param renderer  the renderer (<code>null</code> permitted).
     *
     * @see #getBasicRenderer()
     */
    void setRenderer(ItemRenderer renderer);

    /**
     * Sets a renderer and sends a {@link org.jfree.chart.event.PlotChangeEvent} to all
     * registered listeners.
     *
     * @param index  the index.
     * @param renderer  the renderer.
     *
     * @see #getBasicRenderer(int)
     */
    public void setRenderer(int index, ItemRenderer renderer);

    /**
     * Sets a renderer and sends a {@link org.jfree.chart.event.PlotChangeEvent} to all
     * registered listeners.
     *
     * @param index  the index.
     * @param renderer  the renderer.
     * @param notify  notify listeners?
     *
     * @see #getBasicRenderer(int)
     */
    public void setRenderer(int index, ItemRenderer renderer,
                            boolean notify);

    /**
     * Sets the renderers for this plot and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     *
     * @param renderers  the renderers (<code>null</code> not permitted).
     * @throws ClassCastException If items cannot be cast as appropriate renderers for this plot.
     */
    public void setRenderers(ItemRenderer[] renderers) throws ClassCastException;

    /**
     * Returns the index of the specified renderer, or <code>-1</code> if the
     * renderer is not assigned to this plot.
     *
     * @param renderer  the renderer (<code>null</code> permitted).
     *
     * @return The renderer index.
     */
    public int getIndexOf(ItemRenderer renderer);

    /**
     * Returns the renderer for the specified dataset.  The code first
     * determines the index of the dataset, then checks if there is a
     * renderer with the same index (if not, the method returns renderer(0).
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @return The renderer (possibly <code>null</code>).
     */
    public ItemRenderer getBasicRendererForDataset(Dataset dataset);

    /**
     * Adds an annotation to the plot and sends a {@link org.jfree.chart.event.PlotChangeEvent} to all
     * registered listeners.
     *
     * @param annotation  the annotation (<code>null</code> not permitted). Must be of appropriate type for the plot,
     * CategoryAnnotation for CategoryPlot, XYAnnotation for XYPlot.
     * @throws ClassCastException If the annotation cannot be used with this plot.
     *
     * @see #removeAnnotation(org.jfree.chart.annotations.Annotation)
     */
    public void addAnnotation(Annotation annotation) throws ClassCastException;
    
    /**
     * Adds an annotation to the plot and, if requested, sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param annotation  the annotation (<code>null</code> not permitted).
     * @param notify  notify listeners?
     * @throws ClassCastException If the annotation cannot be used with this plot.
     */
    public void addAnnotation(Annotation annotation, boolean notify) throws ClassCastException;

    /**
     * Removes an annotation from the plot and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     *
     * @param annotation  the annotation (<code>null</code> not permitted).
     *
     * @return A boolean (indicates whether or not the annotation was removed).
     *
     * @see #addAnnotation(Annotation)
     */
    public boolean removeAnnotation(Annotation annotation);

    /**
     * Removes an annotation from the plot and, if requested, sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param annotation  the annotation (<code>null</code> not permitted).
     * @param notify  notify listeners?
     *
     * @return A boolean (indicates whether or not the annotation was removed).
     */
    public boolean removeAnnotation(Annotation annotation, boolean notify);

    /**
     * Clears all the annotations and sends a {@link org.jfree.chart.event.PlotChangeEvent} to all
     * registered listeners.
     */
    void clearAnnotations();

    /**
     * Clears the domain axes from the plot and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     */
    void clearDomainAxes();
    /**
     * Clears all the domain markers for the plot and sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @see #clearRangeMarkers()
     */
    void clearDomainMarkers();
    /**
     * Clears all the domain markers for the specified renderer.
     *
     * @param index  the renderer index.
     *
     * @see #clearRangeMarkers(int)
     */
    void clearDomainMarkers(int index);
    /**
     * Clears the range axes from the plot and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     */
    void clearRangeAxes();
    /**
     * Clears all the range markers for the plot and sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @see #clearDomainMarkers()
     */
    void clearRangeMarkers();
    /**
     * Clears all the range markers for the specified renderer.
     *
     * @param index  the renderer index.
     *
     * @see #clearDomainMarkers(int)
     */
    void clearRangeMarkers(int index);

    /**
     * Configures the domain axes.
     */
    void configureDomainAxes();
    /**
     * Configures the range axes.
     */
    void configureRangeAxes();
    /**
     * Receives notification of a change to the plot's dataset.
     * <P>
     * The range axis bounds will be recalculated if necessary.
     *
     * @param e  information about the event (not used here).
     */
    void datasetChanged(DatasetChangeEvent e);
    /**
     * Draws the plot on a Java 2D graphics device (such as the screen or a
     * printer).
     * <P>
     * At your option, you may supply an instance of {@link PlotRenderingInfo}.
     * If you do, it will be populated with information about the drawing,
     * including various plot dimensions and tooltip info.
     *
     * @param g2  the graphics device.
     * @param area  the area within which the plot (including axes) should
     *              be drawn.
     * @param anchor  the anchor point (<code>null</code> permitted).
     * @param parentState  the state from the parent plot, if there is one.
     * @param state  collects info as the chart is drawn (possibly
     *               <code>null</code>).
     */
    void draw(Graphics2D g2, Rectangle2D area,
                     Point2D anchor,
                     PlotState parentState,
                     PlotRenderingInfo state);

    /**
     * Draws the annotations for the plot.
     *
     * @param g2  the graphics device.
     * @param dataArea  the data area.
     * @param info  the chart rendering info.
     */
    void drawAnnotations(Graphics2D g2,
                                Rectangle2D dataArea,
                                PlotRenderingInfo info);

    /**
     * A utility method for drawing the plot's axes.
     *
     * @param g2  the graphics device.
     * @param plotArea  the plot area.
     * @param dataArea  the data area.
     * @param plotState  collects information about the plot (<code>null</code>
     *                   permitted).
     *
     * @return A map containing the axis states.
     */
    Map drawAxes(Graphics2D g2,
                           Rectangle2D plotArea,
                           Rectangle2D dataArea,
                           PlotRenderingInfo plotState);

    /**
     * Draws the plot background (the background color and/or image).
     * <P>
     * This method will be called during the chart drawing process and is
     * declared public so that it can be accessed by the renderers used by
     * certain subclasses.  You shouldn't need to call this method directly.
     *
     * @param g2  the graphics device.
     * @param area  the area within which the plot should be drawn.
     */
    void drawBackground(Graphics2D g2, Rectangle2D area);

    /**
     * Draws the domain markers (if any) for an axis and layer.  This method is
     * typically called from within the draw() method.
     *
     * @param g2  the graphics device.
     * @param dataArea  the data area.
     * @param index  the renderer index.
     * @param layer  the layer (foreground or background).
     */
    void drawDomainMarkers(Graphics2D g2, Rectangle2D dataArea,
                                     int index, Layer layer);

    /**
     * Draws the range markers (if any) for an axis and layer.  This method is
     * typically called from within the draw() method.
     *
     * @param g2  the graphics device.
     * @param dataArea  the data area.
     * @param index  the renderer index.
     * @param layer  the layer (foreground or background).
     *
     * @see #drawDomainMarkers(Graphics2D, Rectangle2D, int, Layer)
     */
    void drawRangeMarkers(Graphics2D g2, Rectangle2D dataArea,
                                    int index, Layer layer);
    /**
     * Returns the list of annotations.
     *
     * @return The list of annotations (never <code>null</code>).
     *
     * @see #addAnnotation(org.jfree.chart.annotations.Annotation)
     * @see #clearAnnotations()
     */
    List getAnnotations();

    /**
     * Returns the axis offset.
     *
     * @return The axis offset (never <code>null</code>).
     *
     * @see #setAxisOffset(org.jfree.ui.RectangleInsets)
     */
    RectangleInsets getAxisOffset();

    /**
     * Sets the axis offsets (gap between the data area and the axes) and
     * sends a {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param offset  the offset (<code>null</code> not permitted).
     *
     * @see #getAxisOffset()
     */
    void setAxisOffset(RectangleInsets offset);

    /**
     * Returns the dataset for the plot. A more convenient getDataset() method is implemented in CategoryPlot and
     * XYPlot, hence the odd name for this method.
     *
     * @return The primary dataset (possibly <code>null</code>).
     *
     * @see #setDataset(Dataset)
     */
    Dataset getBasicDataset();

    /**
     * Returns the dataset at the given index.A more convenient getDataset(int) method is implemented in CategoryPlot and
     * XYPlot, hence the odd name for this method.
     *
     * @param index  the dataset index.
     *
     * @return The dataset (possibly <code>null</code>).
     *
     * @see #setDataset(int, Dataset)
     */
    Dataset getBasicDataset(int index);

    /**
     * Sets the dataset for the plot, replacing the existing dataset, if there
     * is one. The dataset must be an appropriate type for the plot - CategoryDataset for CategoryPlot and
     * XYDataset for XYPlot. 
     *
     * This method also calls the
     * {@link #datasetChanged(DatasetChangeEvent)} method, which adjusts the
     * axis ranges if necessary and sends a {@link org.jfree.chart.event.PlotChangeEvent} to all
     * registered listeners.
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     * @throws ClassCastException If the dataset cannot be used with this plot.
     *
     * @see #getBasicDataset()
     */
    void setDataset(Dataset dataset) throws ClassCastException;

    /**
     * Sets a dataset for the plot. The dataset must be an appropriate type for the plot - CategoryDataset for
     * CategoryPlot and XYDataset for XYPlot. 
     *
     * @param index  the dataset index.
     * @param dataset  the dataset (<code>null</code> permitted).
     * @throws ClassCastException If the dataset cannot be used with this plot.
     *
     * @see #getBasicDataset(int)
     */
    void setDataset(int index, Dataset dataset) throws ClassCastException;

    /**
     * Returns the number of datasets.
     *
     * @return The number of datasets.
     *
     * @since 1.0.2
     */
    int getDatasetCount();

    /**
     * Returns the index of the specified dataset, or <code>-1</code> if the
     * dataset does not belong to the plot.
     *
     * @param dataset  the dataset (<code>null</code> not permitted).
     *
     * @return The index.
     */
    public int indexOf(Dataset dataset);

    /**
     * Returns the dataset rendering order.
     *
     * @return The order (never <code>null</code>).
     *
     * @see #setDatasetRenderingOrder(DatasetRenderingOrder)
     */
    DatasetRenderingOrder getDatasetRenderingOrder();

    /**
     * Sets the rendering order and sends a {@link org.jfree.chart.event.PlotChangeEvent} to all
     * registered listeners.  By default, the plot renders the primary dataset
     * last (so that the primary dataset overlays the secondary datasets).  You
     * can reverse this if you want to.
     *
     * @param order  the rendering order (<code>null</code> not permitted).
     *
     * @see #getDatasetRenderingOrder()
     */
    void setDatasetRenderingOrder(DatasetRenderingOrder order);

    /**
     * Returns the number of domain axes.
     *
     * @return The axis count.
     */
    int getDomainAxisCount();

    /**
     * Returns the number of range axes.
     *
     * @return The axis count.
     */
    int getRangeAxisCount();

    /**
     * Returns the domain axis edge.  This is derived from the axis location
     * and the plot orientation.
     *
     * @return The edge (never <code>null</code>).
     */
    RectangleEdge getDomainAxisEdge();

    /**
     * Returns the index of the given domain axis.
     *
     * @param axis  the axis.
     *
     * @return The axis index.
     *
     * @see #getRangeAxisIndex(ValueAxis)
     */
    int getDomainAxisIndex(Axis axis);

    /**
     * Returns the index of the given range axis.
     *
     * @param axis  the axis.
     *
     * @return The axis index.
     *
     * @see #getDomainAxisIndex(Axis)
     */
    int getRangeAxisIndex(ValueAxis axis);

    /**
     * Returns the range axis edge.  This is derived from the axis location
     * and the plot orientation.
     *
     * @return The edge (never <code>null</code>).
     */
    RectangleEdge getRangeAxisEdge();

    /**
     * Returns the edge for a domain axis.
     *
     * @param index  the axis index.
     *
     * @return The edge (never <code>null</code>).
     */
    RectangleEdge getDomainAxisEdge(int index);

    /**
     * Returns the edge for a range axis.
     *
     * @param index  the axis index.
     *
     * @return The edge (never <code>null</code>).
     */
    RectangleEdge getRangeAxisEdge(int index);

    /**
     * Sets the location of the primary domain axis and sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param location  the location (<code>null</code> not permitted).
     *
     * @see #getDomainAxisLocation()
     */
    void setDomainAxisLocation(AxisLocation location);

    /**
     * Sets the location of the primary range axis and sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param location  the location (<code>null</code> not permitted).
     *
     * @see #getRangeAxisLocation()
     */
    void setRangeAxisLocation(AxisLocation location);

    /**
     * Sets the location of the domain axis and, if requested, sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param location  the location (<code>null</code> not permitted).
     * @param notify  notify listeners?
     *
     * @see #getDomainAxisLocation()
     */
    void setDomainAxisLocation(AxisLocation location, boolean notify);

    /**
     * Sets the location of the range axis and, if requested, sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param location  the location (<code>null</code> not permitted).
     * @param notify  notify listeners?
     *
     * @see #getDomainAxisLocation()
     */
    void setRangeAxisLocation(AxisLocation location, boolean notify);

    /**
     * Returns the location of the primary domain axis.
     *
     * @return The location (never <code>null</code>).
     *
     * @see #setDomainAxisLocation(AxisLocation)
     */
    AxisLocation getDomainAxisLocation();

    /**
     * Returns the location of the primary range axis.
     *
     * @return The location (never <code>null</code>).
     *
     * @see #setDomainAxisLocation(AxisLocation)
     */
    AxisLocation getRangeAxisLocation();

    /**
     * Sets the location for a domain axis and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     *
     * @param index  the axis index.
     * @param location  the location (<code>null</code> not permitted for index
     *     0).
     *
     * @see #getDomainAxisLocation(int)
     */
    void setDomainAxisLocation(int index, AxisLocation location);

    /**
     * Sets the location for a range axis and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     *
     * @param index  the axis index.
     * @param location  the location (<code>null</code> not permitted for index
     *     0).
     *
     * @see #getRangeAxisLocation(int)
     */
    void setRangeAxisLocation(int index, AxisLocation location);

    /**
     * Returns the location for a domain axis.  If this hasn't been set
     * explicitly, the method returns the location that is opposite to the
     * primary domain axis location.
     *
     * @param index  the axis index.
     *
     * @return The location (never <code>null</code>).
     *
     * @see #setDomainAxisLocation(int, AxisLocation)
     */
    AxisLocation getDomainAxisLocation(int index);

    /**
     * Returns the location for a range axis.  If this hasn't been set
     * explicitly, the method returns the location that is opposite to the
     * primary range axis location.
     *
     * @param index  the axis index.
     *
     * @return The location (never <code>null</code>).
     *
     * @see #setRangeAxisLocation(int, AxisLocation)
     */
    AxisLocation getRangeAxisLocation(int index);

    /**
     * Sets the axis location for a domain axis and, if requested, sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param index  the axis index.
     * @param location  the location (<code>null</code> not permitted for
     *     index 0).
     * @param notify  notify listeners?
     *
     * @since 1.0.5
     *
     * @see #getDomainAxisLocation(int)
     * @see #setRangeAxisLocation(int, org.jfree.chart.axis.AxisLocation , boolean)
     */
    void setDomainAxisLocation(int index, AxisLocation location,
            boolean notify);

    /**
     * Sets the axis location for a range axis and, if requested, sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param index  the axis index.
     * @param location  the location (<code>null</code> not permitted for
     *     index 0).
     * @param notify  notify listeners?
     *
     * @since 1.0.5
     *
     * @see #getRangeAxisLocation(int)
     * @see #setDomainAxisLocation(int, org.jfree.chart.axis.AxisLocation , boolean)
     */
    void setRangeAxisLocation(int index, AxisLocation location,
            boolean notify);

    /**
     * Maps the specified dataset to the axes in the list.  Note that the
     * conversion of data values into Java2D space is always performed using
     * the first axis in the list.
     *
     * @param index  the dataset index (zero-based).
     * @param axisIndices  the axis indices (<code>null</code> permitted).
     *
     * @since 1.0.12
     */
    void mapDatasetToDomainAxes(int index, List axisIndices);

    /**
     * Maps a dataset to a particular range axis.  All data will be plotted
     * against axis zero by default, no mapping is required for this case.
     *
     * @param index  the dataset index (zero-based).
     * @param axisIndex  the axis index.
     *
     * @see #mapDatasetToDomainAxis(int, int)
     */
    void mapDatasetToRangeAxis(int index, int axisIndex);

    /**
     * Maps a dataset to a particular domain axis.  All data will be plotted
     * against axis zero by default, no mapping is required for this case.
     *
     * @param index  the dataset index (zero-based).
     * @param axisIndex  the axis index.
     *
     * @see #mapDatasetToRangeAxis(int, int)
     */
    void mapDatasetToDomainAxis(int index, int axisIndex);

    /**
     * Maps the specified dataset to the axes in the list.  Note that the
     * conversion of data values into Java2D space is always performed using
     * the first axis in the list.
     *
     * @param index  the dataset index (zero-based).
     * @param axisIndices  the axis indices (<code>null</code> permitted).
     *
     * @since 1.0.12
     */
    void mapDatasetToRangeAxes(int index, List axisIndices);

    /**
     * Returns the domain axis for the plot.  If the domain axis for this plot
     * is <code>null</code>, then the method will return the parent plot's
     * domain axis (if there is a parent plot). A more convenient getDomainAxis with a specific type of axis returned
     * is normally provided by implementations of this interface.
     *
     * @return The domain axis (<code>null</code> permitted).
     *
     * @see #setDomainAxis(org.jfree.chart.axis.Axis)
     */
    Axis getBasicDomainAxis();

    /**
     * Returns the range axis for the plot.  If the range axis for this plot is
     * null, then the method will return the parent plot's range axis (if there
     * is a parent plot).
     *
     * @return The range axis (possibly <code>null</code>).
     */
    public ValueAxis getRangeAxis();

    /**
     * Returns a domain axis. A more convenient getDomainAxis with a specific type of axis returned
     * is normally provided by implementations of this interface.
     *
     * @param index  the axis index.
     *
     * @return The axis (<code>null</code> possible).
     *
     * @see #setDomainAxis(int, org.jfree.chart.axis.Axis)
     */
    Axis getBasicDomainAxis(int index);

    /**
     * Returns a range axis.
     *
     * @param index  the axis index.
     *
     * @return The axis (<code>null</code> possible).
     */
    public ValueAxis getRangeAxis(int index);

    /**
     * Sets the domain axis for the plot and sends a {@link org.jfree.chart.event.PlotChangeEvent} to
     * all registered listeners.
     *
     * @param axis  the axis (<code>null</code> permitted).
     *
     * @throws ClassCastException If the axis is not an appropriate type for this plot.
     *
     * @see #getBasicDomainAxis()
     */
    void setDomainAxis(Axis axis) throws ClassCastException;

    /**
     * Sets the range axis for the plot and sends a {@link org.jfree.chart.event.PlotChangeEvent} to
     * all registered listeners.
     *
     * @throws ClassCastException If the axis is not an appropriate type for this plot.
     *
     * @param axis  the axis (<code>null</code> permitted).
     */
    public void setRangeAxis(ValueAxis axis) throws ClassCastException;

    /**
     * Sets a domain axis and sends a {@link org.jfree.chart.event.PlotChangeEvent} to all
     * registered listeners.
     *
     * @param index  the axis index.
     * @param axis  the axis (<code>null</code> permitted).
     *
     * @throws ClassCastException If the axis is not an appropriate type for this plot.
     *
     * @see #getBasicDomainAxis(int)
     */
    void setDomainAxis(int index, Axis axis) throws ClassCastException;

    /**
     * Sets a range axis and sends a {@link org.jfree.chart.event.PlotChangeEvent} to all registered
     * listeners.
     *
     * @param index  the axis index.
     * @param axis  the axis.
     *
     * @throws ClassCastException If the axis is not an appropriate type for this plot.
     */
    public void setRangeAxis(int index, ValueAxis axis) throws ClassCastException;

    /**
     * Sets a domain axis and, if requested, sends a {@link org.jfree.chart.event.PlotChangeEvent} to
     * all registered listeners.
     *
     * @param index  the axis index.
     * @param axis  the axis.
     * @param notify  notify listeners?
     *
     * @throws ClassCastException If the axis is not an appropriate type for this plot.
     *
     * @see #getBasicDomainAxis(int)
     */
    public void setDomainAxis(int index, Axis axis, boolean notify) throws ClassCastException;

    /**
     * Sets a range axis and, if requested, sends a {@link org.jfree.chart.event.PlotChangeEvent} to
     * all registered listeners.
     *
     * @param index  the axis index.
     * @param axis  the axis.
     * @param notify  notify listeners?
     *
     * @throws ClassCastException If the axis is not an appropriate type for this plot.
     */
    public void setRangeAxis(int index, ValueAxis axis, boolean notify);


    /**
     * Sets the domain axes for this plot and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     *
     * @param axes  the axes (<code>null</code> not permitted).
     *
     * @throws ClassCastException If members of the axis array cannot be cast to an appropriate type for this plot.
     *
     * @see #setRangeAxes(ValueAxis[])
     */
    public void setDomainAxes(Axis[] axes) throws ClassCastException;

    /**
     * Sets the range axes for this plot and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     *
     * @param axes  the axes (<code>null</code> not permitted).
     *
     * @see #setDomainAxes(org.jfree.chart.axis.Axis[])
     */
    public void setRangeAxes(ValueAxis[] axes);

    /**
     * Returns the domain axis for a dataset.  You can change the axis for a
     * dataset using the {@link #mapDatasetToDomainAxis(int, int)} method.
     *
     * @param index  the dataset index.
     *
     * @return The domain axis.
     *
     * @see #mapDatasetToDomainAxis(int, int)
     */
    Axis getBasicDomainAxisForDataset(int index);
    
    /**
     * Returns the range axis for a dataset.
     *
     * @param index  the dataset index.
     *
     * @return The axis.
     */
    ValueAxis getRangeAxisForDataset(int index);

    /**
     * Sets the orientation for the plot and sends a {@link org.jfree.chart.event.PlotChangeEvent} to
     * all registered listeners.
     *
     * @param orientation  the orientation (<code>null</code> not permitted).
     *
     * @see #getOrientation()
     */
    public void setOrientation(PlotOrientation orientation);

    /**
     * Returns the orientation of the plot.
     *
     * @return The orientation of the plot (never <code>null</code>).
     *
     * @see #setOrientation(PlotOrientation)
     */
    public PlotOrientation getOrientation();    

}
