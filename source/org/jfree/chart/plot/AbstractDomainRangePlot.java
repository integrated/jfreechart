package org.jfree.chart.plot;

import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.annotations.Annotation;
import org.jfree.chart.renderer.ItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.ObjectList;
import org.jfree.util.PublicCloneable;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.Dataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashSet;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 30-Nov-2009
 * Time: 12:17:44
 * Functionality that is common to XYPlot and CategoryPlot.
 */
public abstract class AbstractDomainRangePlot extends Plot implements DomainRangePlot {
    /**
     * A (possibly empty) list of annotations for the plot.  The list should
     * be initialised in the constructor and never allowed to be
     * <code>null</code>.
     */
    private List annotations;

    /** The plot orientation. */
    private PlotOrientation orientation;

    /** The dataset rendering order. */
    private DatasetRenderingOrder renderingOrder
            = DatasetRenderingOrder.REVERSE;


    /** Storage for the datasets. */
    private ObjectList datasets;

    /** Storage for the renderers. */
    private ObjectList renderers;

    /** The offset between the data area and the axes. */
    private RectangleInsets axisOffset;

    /** Storage for the domain axes. */
    private ObjectList domainAxes;

    /** Storage for the domain axis locations. */
    private ObjectList domainAxisLocations;

    /** The range axis (used for the y-values). */
    private ObjectList rangeAxes;

    /** The range axis location. */
    private ObjectList rangeAxisLocations;


    /** Storage for keys that map datasets to domain axes. */
    private TreeMap datasetToDomainAxesMap;

    /** Storage for keys that map datasets to range axes. */
    private TreeMap datasetToRangeAxesMap;

    public AbstractDomainRangePlot() {
        this(null, null, null, null);
    }

    public AbstractDomainRangePlot(Dataset dataset, Axis domainAxis, Axis rangeAxis, ItemRenderer renderer) {
        super();
        annotations = new ArrayList();

        this.orientation = PlotOrientation.VERTICAL;

        this.axisOffset = RectangleInsets.ZERO_INSETS;

        this.datasets = new ObjectList();

        this.datasetToDomainAxesMap = new TreeMap();
        this.datasetToRangeAxesMap = new TreeMap();

        // allocate storage for datasets, axes and renderers (all optional)
        this.domainAxes = new ObjectList();
        this.domainAxisLocations = new ObjectList();
        this.rangeAxes = new ObjectList();
        this.rangeAxisLocations = new ObjectList();


        this.datasets.set(0, dataset);
        if (dataset != null) {
            dataset.addChangeListener(this);
        }

        this.renderers = new ObjectList();

        this.renderers.set(0, renderer);
        if (renderer != null) {
            renderer.setPlot(this);
            renderer.addChangeListener(this);
        }

        this.domainAxes.set(0, domainAxis);
        this.mapDatasetToDomainAxis(0, 0);
        if (domainAxis != null) {
            domainAxis.setPlot(this);
            domainAxis.addChangeListener(this);
        }
        this.domainAxisLocations.set(0, AxisLocation.BOTTOM_OR_LEFT);


        this.rangeAxes.set(0, rangeAxis);
        this.mapDatasetToRangeAxis(0, 0);
        if (rangeAxis != null) {
            rangeAxis.setPlot(this);
            rangeAxis.addChangeListener(this);
        }
        this.rangeAxisLocations.set(0, AxisLocation.BOTTOM_OR_LEFT);

        setDomainAxisLocation(AxisLocation.BOTTOM_OR_LEFT, false);
        setRangeAxisLocation(AxisLocation.TOP_OR_LEFT, false);

        configureDomainAxes();
        configureRangeAxes();
    }

    /**
     * Returns the dataset rendering order.
     *
     * @return The order (never <code>null</code>).
     *
     * @see #setDatasetRenderingOrder(DatasetRenderingOrder)
     */
    public DatasetRenderingOrder getDatasetRenderingOrder() {
        return this.renderingOrder;
    }

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
    public void setDatasetRenderingOrder(DatasetRenderingOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Null 'order' argument.");
        }
        this.renderingOrder = order;
        fireChangeEvent();
    }

    /**
     * Returns the number of renderer slots for this plot.
     *
     * @return The number of renderer slots.
     *
     * @since 1.0.11
     */
    public int getRendererCount() {
        return this.renderers.size();
    }

    /**
     * Returns the renderer for the primary dataset.
     *
     * @return The item renderer (possibly <code>null</code>).
     *
     * @see #setRenderer(ItemRenderer)
     */
    public ItemRenderer getBasicRenderer() {
        return getBasicRenderer(0);
    }

    /**
     * Returns the renderer for a dataset, or <code>null</code>.
     *
     * @param index  the renderer index.
     *
     * @return The renderer (possibly <code>null</code>).
     *
     * @see #setRenderer(int, ItemRenderer)
     */
    public ItemRenderer getBasicRenderer(int index) {
        ItemRenderer result = null;
        if (this.renderers.size() > index) {
            result = (ItemRenderer) this.renderers.get(index);
        }
        return result;
    }

    /**
     * Sets the renderer for the primary dataset and sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.  If the renderer
     * is set to <code>null</code>, no data will be displayed.
     *
     * @param renderer  the renderer (<code>null</code> permitted).
     *
     * @see #getBasicRenderer()
     */
    public void setRenderer(ItemRenderer renderer) {
        setRenderer(0, renderer);
    }


    /**
     * Sets a renderer and sends a {@link org.jfree.chart.event.PlotChangeEvent} to all
     * registered listeners.
     *
     * @param index  the index.
     * @param renderer  the renderer.
     *
     * @see #getBasicRenderer(int)
     */
    public void setRenderer(int index, ItemRenderer renderer) {
        setRenderer(index, renderer, true);
    }

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
                            boolean notify) {
        ItemRenderer existing = getBasicRenderer(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        this.renderers.set(index, renderer);
        if (renderer != null) {
            renderer.setPlot(this);
            renderer.addChangeListener(this);
        }
        configureDomainAxes();
        configureRangeAxes();
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Sets the renderers for this plot and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     *
     * @param renderers  the renderers (<code>null</code> not permitted).
     */
    public void setRenderers(ItemRenderer[] renderers) {
        for (int i = 0; i < renderers.length; i++) {
            setRenderer(i, renderers[i], false);
        }
        fireChangeEvent();
    }

    /**
     * Returns the index of the specified renderer, or <code>-1</code> if the
     * renderer is not assigned to this plot.
     *
     * @param renderer  the renderer (<code>null</code> permitted).
     *
     * @return The renderer index.
     */
    public int getIndexOf(ItemRenderer renderer) {
        return this.renderers.indexOf(renderer);
    }
    
    /**
     * Returns the renderer for the specified dataset.  The code first
     * determines the index of the dataset, then checks if there is a
     * renderer with the same index (if not, the method returns renderer(0).
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @return The renderer (possibly <code>null</code>).
     */
    public ItemRenderer getBasicRendererForDataset(Dataset dataset) {
        ItemRenderer result = null;
        for (int i = 0; i < getDatasetCount(); i++) {
            if (getBasicDataset(i) == dataset) {
                result = (ItemRenderer) this.renderers.get(i);
                if (result == null) {
                    result = getBasicRenderer();
                }
                break;
            }
        }
        return result;
    }


    protected ObjectList getDatasets() {
        return datasets;
    }

    public Dataset getBasicDataset() {
        return getBasicDataset(0);
    }

    public Dataset getBasicDataset(int index) {
        Dataset result = null;
        if (this.datasets.size() > index) {
            result = (Dataset) this.datasets.get(index);
        }
        return result;
    }

    public void setDataset(Dataset dataset) throws ClassCastException {
        setDataset(0, dataset);
    }

    public void setDataset(int index, Dataset dataset) throws ClassCastException {
        Dataset existing = getBasicDataset(index);
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        this.datasets.set(index, dataset);
        if (dataset != null) {
            dataset.addChangeListener(this);
        }

        // send a dataset change event to self...
        DatasetChangeEvent event = new DatasetChangeEvent(this, dataset);
        datasetChanged(event);
    }

    /**
     * Returns the number of datasets.
     *
     * @return The number of datasets.
     */
    public int getDatasetCount() {
        return this.datasets.size();
    }


    /**
     * Returns the index of the specified dataset, or <code>-1</code> if the
     * dataset does not belong to the plot.
     *
     * @param dataset  the dataset (<code>null</code> not permitted).
     *
     * @return The index.
     */
    public int indexOf(Dataset dataset) {
        int result = -1;
        for (int i = 0; i < this.datasets.size(); i++) {
            if (dataset == this.datasets.get(i)) {
                result = i;
                break;
            }
        }
        return result;
    }

    public void addAnnotation(Annotation annotation) throws ClassCastException {
        addAnnotation(annotation, true);
    }

    public boolean removeAnnotation(Annotation annotation) {
        return removeAnnotation(annotation,  true);
    }

    protected boolean innerRemoveAnnotation(Annotation a, boolean notify) {
        if (a == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        boolean removed = this.annotations.remove(a);
        if (removed && notify) {
            fireChangeEvent();
        }
        return removed;
    }

    protected void innerAddAnnotation(Annotation a, boolean notify) {
        if (a == null) {
            throw new IllegalArgumentException("Null 'annotation' argument.");
        }
        this.annotations.add(a);
        if (notify) {
            fireChangeEvent();
        }
    }

    public Axis getBasicDomainAxis() {
        return getBasicDomainAxis(0);
    }

    public Axis getBasicDomainAxis(int index) {
        Axis result = null;
        if (index < this.domainAxes.size()) {
            result = (Axis) this.domainAxes.get(index);
        }
        if (result == null) {
            Plot parent = getParent();
            if (parent instanceof AbstractDomainRangePlot) {
                AbstractDomainRangePlot xy = (AbstractDomainRangePlot) parent;
                result = xy.getBasicDomainAxis(index);
            }
        }
        return result;
    }

    public ValueAxis getRangeAxis() {
        return getRangeAxis(0);
    }

    public ValueAxis getRangeAxis(int index) {
        ValueAxis result = null;
        if (index < this.rangeAxes.size()) {
            result = (ValueAxis) this.rangeAxes.get(index);
        }
        if (result == null) {
            Plot parent = getParent();
            if (parent instanceof AbstractDomainRangePlot) {
                AbstractDomainRangePlot xy = (AbstractDomainRangePlot) parent;
                result = xy.getRangeAxis(index);
            }
        }
        return result;
    }

    public void setDomainAxis(Axis axis) throws ClassCastException {
        setDomainAxis(0, axis);
    }

    public void setDomainAxis(int index, Axis axis) throws ClassCastException {
        setDomainAxis(0, axis, true);
    }

    public void setRangeAxis(ValueAxis axis) throws ClassCastException {
        setRangeAxis(0, axis);
    }

    public void setRangeAxis(int index, ValueAxis axis) throws ClassCastException {
        setRangeAxis(index, axis, true);
    }

    public void setDomainAxis(int index, Axis axis, boolean notify) throws ClassCastException {
        Axis existing = getBasicDomainAxis(index);
        genericSetAxis(index, axis, existing, this.domainAxes, notify);
    }

    /**
     * Sets a range axis and, if requested, sends a {@link org.jfree.chart.event.PlotChangeEvent} to
     * all registered listeners.
     *
     * @param index  the axis index.
     * @param axis  the axis (<code>null</code> permitted).
     * @param notify  notify listeners?
     *
     * @see #getRangeAxis(int)
     */
    public void setRangeAxis(int index, ValueAxis axis, boolean notify) {
        ValueAxis existing = getRangeAxis(index);
        genericSetAxis(index, axis, existing, this.rangeAxes, notify);
    }

    /**
     * Sets the range axes for this plot and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     *
     * @param axes  the axes (<code>null</code> not permitted).
     *
     * @see #setDomainAxes(org.jfree.chart.axis.Axis[])
     */
    public void setRangeAxes(ValueAxis[] axes) {
        for (int i = 0; i < axes.length; i++) {
            setRangeAxis(i, axes[i], false);
        }
        fireChangeEvent();
    }

    /**
     * Sets the domain axes for this plot and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     *
     * @param axes  the axes (<code>null</code> not permitted).
     *
     * @see #setRangeAxes(ValueAxis[])
     */
    public void setDomainAxes(Axis[] axes) {
        for (int i = 0; i < axes.length; i++) {
            setDomainAxis(i, axes[i], false);
        }
        fireChangeEvent();
    }

    protected void genericSetAxis(int index, Axis axis, Axis existing, ObjectList axes, boolean notify) {
        if (existing != null) {
            existing.removeChangeListener(this);
        }
        if (axis != null) {
            axis.setPlot(this);
        }
        axes.set(index, axis);
        if (axis != null) {
            axis.configure();
            axis.addChangeListener(this);
        }
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns the index of the given domain axis.
     *
     * @param axis  the axis.
     *
     * @return The axis index.
     *
     * @see #getRangeAxisIndex(ValueAxis)
     */
    public int getDomainAxisIndex(Axis axis) {
        int result = this.domainAxes.indexOf(axis);
        if (result < 0) {
            // try the parent plot
            Plot parent = getParent();
            if (parent instanceof AbstractDomainRangePlot) {
                AbstractDomainRangePlot p = (AbstractDomainRangePlot) parent;
                result = p.getDomainAxisIndex(axis);
            }
        }
        return result;
    }

    /**
     * Returns the index of the given range axis.
     *
     * @param axis  the axis.
     *
     * @return The axis index.
     *
     * @see #getDomainAxisIndex(Axis)
     */
    public int getRangeAxisIndex(ValueAxis axis) {
        int result = this.rangeAxes.indexOf(axis);
        if (result < 0) {
            // try the parent plot
            Plot parent = getParent();
            if (parent instanceof DomainRangePlot) {
                DomainRangePlot p = (DomainRangePlot) parent;
                result = p.getRangeAxisIndex(axis);
            }
        }
        return result;
    }

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
    public AxisLocation getDomainAxisLocation(int index) {
        AxisLocation result = null;
        if (index < this.domainAxisLocations.size()) {
            result = (AxisLocation) this.domainAxisLocations.get(index);
        }
        if (result == null) {
            result = AxisLocation.getOpposite(getDomainAxisLocation());
        }
        return result;
    }


    /**
     * Sets the location of the primary domain axis and sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param location  the location (<code>null</code> not permitted).
     *
     * @see #getDomainAxisLocation()
     */
    public void setDomainAxisLocation(AxisLocation location) {
        // delegate...
        setDomainAxisLocation(0, location, true);
    }

    /**
     * Sets the location of the domain axis and, if requested, sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param location  the location (<code>null</code> not permitted).
     * @param notify  notify listeners?
     *
     * @see #getDomainAxisLocation()
     */
    public void setDomainAxisLocation(AxisLocation location, boolean notify) {
        // delegate...
        setDomainAxisLocation(0, location, notify);
    }



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
    public void setDomainAxisLocation(int index, AxisLocation location) {
        // delegate...
        setDomainAxisLocation(index, location, true);
    }

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
     * @see #setRangeAxisLocation(int, AxisLocation, boolean)
     */
    public void setDomainAxisLocation(int index, AxisLocation location,
            boolean notify) {
        if (index == 0 && location == null) {
            throw new IllegalArgumentException(
                    "Null 'location' for index 0 not permitted.");
        }
        this.domainAxisLocations.set(index, location);
        if (notify) {
            fireChangeEvent();
        }
    }

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
    public AxisLocation getRangeAxisLocation(int index) {
        AxisLocation result = null;
        if (index < this.rangeAxisLocations.size()) {
            result = (AxisLocation) this.rangeAxisLocations.get(index);
        }
        if (result == null) {
            result = AxisLocation.getOpposite(getRangeAxisLocation());
        }
        return result;
    }


    /**
     * Sets the location of the primary range axis and sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param location  the location (<code>null</code> not permitted).
     *
     * @see #getRangeAxisLocation()
     */
    public void setRangeAxisLocation(AxisLocation location) {
        // delegate...
        setRangeAxisLocation(0, location, true);
    }


    /**
     * Sets the location of the primary range axis and, if requested, sends a
     * {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param location  the location (<code>null</code> not permitted).
     * @param notify  notify listeners?
     *
     * @see #getRangeAxisLocation()
     */
    public void setRangeAxisLocation(AxisLocation location, boolean notify) {
        // delegate...
        setRangeAxisLocation(0, location, notify);
    }

    /**
     * Sets the location for a range axis and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     *
     * @param index  the axis index.
     * @param location  the location (<code>null</code> permitted).
     *
     * @see #getRangeAxisLocation(int)
     */
    public void setRangeAxisLocation(int index, AxisLocation location) {
        // delegate...
        setRangeAxisLocation(index, location, true);
    }

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
     * @see #getRangeAxisLocation(int)
     * @see #setDomainAxisLocation(int, AxisLocation, boolean)
     */
    public void setRangeAxisLocation(int index, AxisLocation location,
            boolean notify) {

        if (index == 0 && location == null) {
            throw new IllegalArgumentException(
                    "Null 'location' for index 0 not permitted.");
        }
        this.rangeAxisLocations.set(index, location);
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns the edge where the primary range axis is located.
     *
     * @return The edge (never <code>null</code>).
     */
    public RectangleEdge getRangeAxisEdge() {
        return Plot.resolveRangeAxisLocation(getRangeAxisLocation(),
                this.orientation);
    }

    /**
     * Returns the edge for a range axis.
     *
     * @param index  the axis index.
     *
     * @return The edge.
     *
     * @see #getRangeAxisLocation(int)
     * @see #getOrientation()
     */
    public RectangleEdge getRangeAxisEdge(int index) {
        AxisLocation location = getRangeAxisLocation(index);
        RectangleEdge result = Plot.resolveRangeAxisLocation(location,
                this.orientation);
        if (result == null) {
            result = RectangleEdge.opposite(getRangeAxisEdge());
        }
        return result;
    }

    /**
     * Returns the list of annotations.
     *
     * @return The list of annotations.
     *
     * @since 1.0.1
     *
     * @see #addAnnotation(org.jfree.chart.annotations.Annotation)
     */
    public List getAnnotations() {
        return new ArrayList(this.annotations);
    }
    
    /**
     * Clears all the annotations and sends a {@link org.jfree.chart.event.PlotChangeEvent} to all
     * registered listeners.
     */
    public void clearAnnotations() {
        this.annotations.clear();
        fireChangeEvent();
    }


    /**
     * Maps a dataset to a particular domain axis.  All data will be plotted
     * against axis zero by default, no mapping is required for this case.
     *
     * @param index  the dataset index (zero-based).
     * @param axisIndex  the axis index.
     *
     * @see #mapDatasetToRangeAxis(int, int)
     */
    public void mapDatasetToDomainAxis(int index, int axisIndex) {
        List axisIndices = new java.util.ArrayList(1);
        axisIndices.add(new Integer(axisIndex));
        mapDatasetToDomainAxes(index, axisIndices);
    }

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
    public void mapDatasetToDomainAxes(int index, List axisIndices) {
        if (index < 0) {
            throw new IllegalArgumentException("Requires 'index' >= 0.");
        }
        checkAxisIndices(axisIndices);
        Integer key = new Integer(index);
        this.datasetToDomainAxesMap.put(key, new ArrayList(axisIndices));
        // fake a dataset change event to update axes...
        datasetChanged(new DatasetChangeEvent(this, getBasicDataset(index)));
    }


    /**
     * Returns the location of the primary domain axis.
     *
     * @return The location (never <code>null</code>).
     *
     * @see #setDomainAxisLocation(AxisLocation)
     */
    public AxisLocation getDomainAxisLocation() {
        return (AxisLocation) this.domainAxisLocations.get(0);
    }

    /**
     * Returns the location of the primary domain axis.
     *
     * @return The location (never <code>null</code>).
     *
     * @see #setRangeAxisLocation(AxisLocation)
     */
    public AxisLocation getRangeAxisLocation() {
        return (AxisLocation) this.rangeAxisLocations.get(0);
    }


    /**
     * Returns the edge for the primary domain axis (taking into account the
     * plot's orientation).
     *
     * @return The edge.
     *
     * @see #getDomainAxisLocation()
     * @see #getOrientation()
     */
    public RectangleEdge getDomainAxisEdge() {
        return Plot.resolveDomainAxisLocation(getDomainAxisLocation(),
                this.orientation);
    }



    /**
     * Returns the edge for a domain axis.
     *
     * @param index  the axis index.
     *
     * @return The edge.
     *
     * @see #getRangeAxisEdge(int)
     */
    public RectangleEdge getDomainAxisEdge(int index) {
        AxisLocation location = getDomainAxisLocation(index);
        RectangleEdge result = Plot.resolveDomainAxisLocation(location,
                this.orientation);
        if (result == null) {
            result = RectangleEdge.opposite(getDomainAxisEdge());
        }
        return result;
    }

    /**
     * Returns the number of domain axes.
     *
     * @return The axis count.
     *
     * @see #getRangeAxisCount()
     */
    public int getDomainAxisCount() {
        return this.domainAxes.size();
    }

    /**
     * Clears the domain axes from the plot and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     *
     * @see #clearRangeAxes()
     */
    public void clearDomainAxes() {
        for (int i = 0; i < this.domainAxes.size(); i++) {
            Axis axis = (Axis) this.domainAxes.get(i);
            if (axis != null) {
                axis.removeChangeListener(this);
            }
        }
        this.domainAxes.clear();
        fireChangeEvent();
    }

    /**
     * Configures the domain axes.
     */
    public void configureDomainAxes() {
        for (int i = 0; i < this.domainAxes.size(); i++) {
            Axis axis = (Axis) this.domainAxes.get(i);
            if (axis != null) {
                axis.configure();
            }
        }
    }

    /**
     * Returns the number of range axes.
     *
     * @return The axis count.
     */
    public int getRangeAxisCount() {
        return this.rangeAxes.size();
    }

    /**
     * Clears the range axes from the plot and sends a {@link org.jfree.chart.event.PlotChangeEvent}
     * to all registered listeners.
     */
    public void clearRangeAxes() {
        for (int i = 0; i < this.rangeAxes.size(); i++) {
            Axis axis = (Axis) this.rangeAxes.get(i);
            if (axis != null) {
                axis.removeChangeListener(this);
            }
        }
        this.rangeAxes.clear();
        fireChangeEvent();
    }

    /**
     * Configures the range axes.
     */
    public void configureRangeAxes() {
        for (int i = 0; i < this.rangeAxes.size(); i++) {
            Axis axis = (Axis) this.rangeAxes.get(i);
            if (axis != null) {
                axis.configure();
            }
        }
    }

    /**
     * Maps a dataset to a particular range axis.  All data will be plotted
     * against axis zero by default, no mapping is required for this case.
     *
     * @param index  the dataset index (zero-based).
     * @param axisIndex  the axis index.
     *
     * @see #mapDatasetToDomainAxis(int, int)
     */
    public void mapDatasetToRangeAxis(int index, int axisIndex) {
        List axisIndices = new java.util.ArrayList(1);
        axisIndices.add(new Integer(axisIndex));
        mapDatasetToRangeAxes(index, axisIndices);
    }

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
    public void mapDatasetToRangeAxes(int index, List axisIndices) {
        if (index < 0) {
            throw new IllegalArgumentException("Requires 'index' >= 0.");
        }
        checkAxisIndices(axisIndices);
        Integer key = new Integer(index);
        this.datasetToRangeAxesMap.put(key, new ArrayList(axisIndices));
        // fake a dataset change event to update axes...
        datasetChanged(new DatasetChangeEvent(this, getBasicDataset(index)));
    }

    /**
     * This method is used to perform argument checking on the list of
     * axis indices passed to mapDatasetToDomainAxes() and
     * mapDatasetToRangeAxes().
     *
     * @param indices  the list of indices (<code>null</code> permitted).
     */
    private void checkAxisIndices(List indices) {
        // axisIndices can be:
        // 1.  null;
        // 2.  non-empty, containing only Integer objects that are unique.
        if (indices == null) {
            return;  // OK
        }
        int count = indices.size();
        if (count == 0) {
            throw new IllegalArgumentException("Empty list not permitted.");
        }
        HashSet set = new HashSet();
        for (int i = 0; i < count; i++) {
            Object item = indices.get(i);
            if (!(item instanceof Integer)) {
                throw new IllegalArgumentException(
                        "Indices must be Integer instances.");
            }
            if (set.contains(item)) {
                throw new IllegalArgumentException("Indices must be unique.");
            }
            set.add(item);
        }
    }

    /**
     * Returns a list of the datasets that are mapped to the axis with the
     * specified index.
     *
     * @param axisIndex  the axis index.
     *
     * @return The list (possibly empty, but never <code>null</code>).
     *
     * @since 1.0.3
     */
    protected List getDatasetsMappedToDomainAxis(int axisIndex) {
        Integer key = new Integer(axisIndex);
        List result = new ArrayList();
        ObjectList datasets = getDatasets();
        for (int i = 0; i < datasets.size(); i++) {
            List mappedAxes = (List) this.datasetToDomainAxesMap.get(
                    new Integer(i));
            Dataset dataset = (Dataset) datasets.get(i);
            if (mappedAxes == null) {
                if (key.equals(ZERO)) {
                    if (dataset != null) {
                        result.add(dataset);
                    }
                }
            }
            else {
                if (mappedAxes.contains(key)) {
                    if (dataset != null) {
                        result.add(dataset);
                    }
                }
            }
        }
        return result;
    }

    /**
     * A utility method that returns a list of datasets that are mapped to a
     * given range axis.
     *
     * @param index  the axis index.
     *
     * @return A list of datasets.
     */
    protected List getDatasetsMappedToRangeAxis(int index) {
        Integer key = new Integer(index);
        List result = new ArrayList();
        ObjectList datasets = getDatasets();
        for (int i = 0; i < datasets.size(); i++) {
            List mappedAxes = (List) this.datasetToRangeAxesMap.get(
                    new Integer(i));
            if (mappedAxes == null) {
                if (key.equals(ZERO)) {
                    result.add(datasets.get(i));
                }
            }
            else {
                if (mappedAxes.contains(key)) {
                    result.add(datasets.get(i));
                }
            }
        }
        return result;
    }

    /**
     * Returns the domain axis for a dataset.
     *
     * @param index  the dataset index.
     *
     * @return The axis.
     */
    public Axis getBasicDomainAxisForDataset(int index) {
        int upper = Math.max(getDatasetCount(), getRendererCount());
        if (index < 0 || index >= upper) {
            throw new IllegalArgumentException("Index " + index
                    + " out of bounds.");
        }
        Axis valueAxis;
        List axisIndices = (List) this.datasetToDomainAxesMap.get(
                new Integer(index));
        if (axisIndices != null) {
            // the first axis in the list is used for data <--> Java2D
            Integer axisIndex = (Integer) axisIndices.get(0);
            valueAxis = getBasicDomainAxis(axisIndex.intValue());
        }
        else {
            valueAxis = getBasicDomainAxis(0);
        }
        return valueAxis;
    }

    /**
     * Returns the range axis for a dataset.
     *
     * @param index  the dataset index.
     *
     * @return The axis.
     */
    public ValueAxis getRangeAxisForDataset(int index) {
        int upper = Math.max(getDatasetCount(), getRendererCount());
        if (index < 0 || index >= upper) {
            throw new IllegalArgumentException("Index " + index
                    + " out of bounds.");
        }
        ValueAxis valueAxis;
        List axisIndices = (List) this.datasetToRangeAxesMap.get(
                new Integer(index));
        if (axisIndices != null) {
            // the first axis in the list is used for data <--> Java2D
            Integer axisIndex = (Integer) axisIndices.get(0);
            valueAxis = getRangeAxis(axisIndex.intValue());
        }
        else {
            valueAxis = getRangeAxis(0);
        }
        return valueAxis;
    }


    /**
     * Returns the orientation of the plot.
     *
     * @return The orientation of the plot (never <code>null</code>).
     *
     * @see #setOrientation(PlotOrientation)
     */
    public PlotOrientation getOrientation() {
        return this.orientation;
    }

    /**
     * Sets the orientation for the plot and sends a {@link org.jfree.chart.event.PlotChangeEvent} to
     * all registered listeners.
     *
     * @param orientation  the orientation (<code>null</code> not permitted).
     *
     * @see #getOrientation()
     */
    public void setOrientation(PlotOrientation orientation) {
        if (orientation == null) {
            throw new IllegalArgumentException("Null 'orientation' argument.");
        }
        this.orientation = orientation;
        fireChangeEvent();
    }

    /**
     * Returns the axis offset.
     *
     * @return The axis offset (never <code>null</code>).
     *
     * @see #setAxisOffset(org.jfree.ui.RectangleInsets)
     */
    public RectangleInsets getAxisOffset() {
        return this.axisOffset;
    }

    /**
     * Sets the axis offsets (gap between the data area and the axes) and
     * sends a {@link org.jfree.chart.event.PlotChangeEvent} to all registered listeners.
     *
     * @param offset  the offset (<code>null</code> not permitted).
     *
     * @see #getAxisOffset()
     */
    public void setAxisOffset(RectangleInsets offset) {
        if (offset == null) {
            throw new IllegalArgumentException("Null 'offset' argument.");
        }
        this.axisOffset = offset;
        fireChangeEvent();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AbstractDomainRangePlot)) {
            return false;
        }
        AbstractDomainRangePlot that = (AbstractDomainRangePlot) obj;
        
        if (!ObjectUtilities.equal(this.axisOffset, that.axisOffset)) {
            return false;
        }
        if (this.orientation != that.orientation) {
            return false;
        }
        if (this.renderingOrder != that.renderingOrder) {
            return false;
        }
        if (!ObjectUtilities.equal(this.annotations, that.annotations)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.datasetToDomainAxesMap,
                that.datasetToDomainAxesMap)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.datasetToRangeAxesMap,
                that.datasetToRangeAxesMap)) {
            return false;
        }
        if (!this.domainAxes.equals(that.domainAxes)) {
            return false;
        }
        if (!this.domainAxisLocations.equals(that.domainAxisLocations)) {
            return false;
        }
        if (!this.rangeAxes.equals(that.rangeAxes)) {
            return false;
        }
        if (!this.rangeAxisLocations.equals(that.rangeAxisLocations)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.renderers, that.renderers)) {
            return false;
        }
        return super.equals(obj);
    }

    public Object clone() throws CloneNotSupportedException {
        AbstractDomainRangePlot clone = (AbstractDomainRangePlot) super.clone();

        clone.annotations = (List) ObjectUtilities.deepClone(this.annotations);

        clone.datasetToDomainAxesMap = new TreeMap();
        clone.datasetToDomainAxesMap.putAll(this.datasetToDomainAxesMap);
        clone.datasetToRangeAxesMap = new TreeMap();
        clone.datasetToRangeAxesMap.putAll(this.datasetToRangeAxesMap);



        // the datasets are not cloned, but listeners need to be added...
        clone.datasets = (ObjectList) ObjectUtilities.clone(this.datasets);
        for (int i = 0; i < clone.datasets.size(); ++i) {
            Dataset d = getBasicDataset(i);
            if (d != null) {
                d.addChangeListener(clone);
            }
        }

        clone.renderers = (ObjectList) ObjectUtilities.clone(this.renderers);
        for (int i = 0; i < this.renderers.size(); i++) {
            ItemRenderer renderer2 = (ItemRenderer) this.renderers.get(i);
            if (renderer2 instanceof PublicCloneable) {
                PublicCloneable pc = (PublicCloneable) renderer2;
                clone.renderers.set(i, pc.clone());
            }
        }

        clone.domainAxes = (ObjectList) ObjectUtilities.clone(this.domainAxes);
        for (int i = 0; i < this.domainAxes.size(); i++) {
            Axis axis = (Axis) this.domainAxes.get(i);
            if (axis != null) {
                Axis clonedAxis = (Axis) axis.clone();
                clone.domainAxes.set(i, clonedAxis);
                clonedAxis.setPlot(clone);
                clonedAxis.addChangeListener(clone);
            }
        }
        clone.domainAxisLocations = (ObjectList)
                this.domainAxisLocations.clone();

        clone.rangeAxes = new ObjectList();
        for (int i = 0; i < this.rangeAxes.size(); i++) {
            ValueAxis yAxis = (ValueAxis) this.rangeAxes.get(i);
            if (yAxis != null) {
                ValueAxis clonedAxis = (ValueAxis) yAxis.clone();
                clone.setRangeAxis(i, clonedAxis);
            }
        }
        clone.rangeAxisLocations = (ObjectList) this.rangeAxisLocations.clone();

        return clone;
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws java.io.IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(ObjectInputStream stream)
        throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        // register the plot as a listener with its axes, datasets, and
        // renderers...
        int datasetCount = this.datasets.size();
        for (int i = 0; i < datasetCount; i++) {
            Dataset dataset = (Dataset) this.datasets.get(i);
            if (dataset != null) {
                dataset.addChangeListener(this);
            }
        }
        
        int domainAxisCount = this.domainAxes.size();
        for (int i = 0; i < domainAxisCount; i++) {
            Axis axis = (Axis) this.domainAxes.get(i);
            if (axis != null) {
                axis.setPlot(this);
                axis.addChangeListener(this);
            }
        }

        for (int i = 0; i < this.rangeAxes.size(); i++) {
            Axis yAxis = (Axis) this.rangeAxes.get(i);
            if (yAxis != null) {
                yAxis.setPlot(this);
                yAxis.addChangeListener(this);
            }
        }

        // register the plot as a listener with its axes, datasets, and
        // renderers...
        int rendererCount = this.renderers.size();
        for (int i = 0; i < rendererCount; i++) {
            ItemRenderer renderer = (ItemRenderer) this.renderers.get(i);
            if (renderer != null) {
                renderer.addChangeListener(this);
            }
        }
    }
}
