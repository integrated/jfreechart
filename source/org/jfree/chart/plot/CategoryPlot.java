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
 * -----------------
 * CategoryPlot.java
 * -----------------
 * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Jeremy Bowman;
 *                   Arnaud Lelievre;
 *                   Richard West, Advanced Micro Devices, Inc.;
 *
 * Changes
 * -------
 * 21-Jun-2001 : Removed redundant JFreeChart parameter from constructors (DG);
 * 21-Aug-2001 : Added standard header. Fixed DOS encoding problem (DG);
 * 18-Sep-2001 : Updated header (DG);
 * 15-Oct-2001 : Data source classes moved to com.jrefinery.data.* (DG);
 * 22-Oct-2001 : Renamed DataSource.java --> Dataset.java etc. (DG);
 * 23-Oct-2001 : Changed intro and trail gaps on bar plots to use percentage of
 *               available space rather than a fixed number of units (DG);
 * 12-Dec-2001 : Changed constructors to protected (DG);
 * 13-Dec-2001 : Added tooltips (DG);
 * 16-Jan-2002 : Increased maximum intro and trail gap percents, plus added
 *               some argument checking code.  Thanks to Taoufik Romdhane for
 *               suggesting this (DG);
 * 05-Feb-2002 : Added accessor methods for the tooltip generator, incorporated
 *               alpha-transparency for Plot and subclasses (DG);
 * 06-Mar-2002 : Updated import statements (DG);
 * 14-Mar-2002 : Renamed BarPlot.java --> CategoryPlot.java, and changed code
 *               to use the CategoryItemRenderer interface (DG);
 * 22-Mar-2002 : Dropped the getCategories() method (DG);
 * 23-Apr-2002 : Moved the dataset from the JFreeChart class to the Plot
 *               class (DG);
 * 29-Apr-2002 : New methods to support printing values at the end of bars,
 *               contributed by Jeremy Bowman (DG);
 * 11-May-2002 : New methods for label visibility and overlaid plot support,
 *               contributed by Jeremy Bowman (DG);
 * 06-Jun-2002 : Removed the tooltip generator, this is now stored with the
 *               renderer.  Moved constants into the CategoryPlotConstants
 *               interface.  Updated Javadoc comments (DG);
 * 10-Jun-2002 : Overridden datasetChanged() method to update the upper and
 *               lower bound on the range axis (if necessary), updated
 *               Javadocs (DG);
 * 25-Jun-2002 : Removed redundant imports (DG);
 * 20-Aug-2002 : Changed the constructor for Marker (DG);
 * 28-Aug-2002 : Added listener notification to setDomainAxis() and
 *               setRangeAxis() (DG);
 * 23-Sep-2002 : Added getLegendItems() method and fixed errors reported by
 *               Checkstyle (DG);
 * 28-Oct-2002 : Changes to the CategoryDataset interface (DG);
 * 05-Nov-2002 : Base dataset is now TableDataset not CategoryDataset (DG);
 * 07-Nov-2002 : Renamed labelXXX as valueLabelXXX (DG);
 * 18-Nov-2002 : Added grid settings for both domain and range axis (previously
 *               these were set in the axes) (DG);
 * 19-Nov-2002 : Added axis location parameters to constructor (DG);
 * 17-Jan-2003 : Moved to com.jrefinery.chart.plot package (DG);
 * 14-Feb-2003 : Fixed bug in auto-range calculation for secondary axis (DG);
 * 26-Mar-2003 : Implemented Serializable (DG);
 * 02-May-2003 : Moved render() method up from subclasses. Added secondary
 *               range markers. Added an attribute to control the dataset
 *               rendering order.  Added a drawAnnotations() method.  Changed
 *               the axis location from an int to an AxisLocation (DG);
 * 07-May-2003 : Merged HorizontalCategoryPlot and VerticalCategoryPlot into
 *               this class (DG);
 * 02-Jun-2003 : Removed check for range axis compatibility (DG);
 * 04-Jul-2003 : Added a domain gridline position attribute (DG);
 * 21-Jul-2003 : Moved DrawingSupplier to Plot superclass (DG);
 * 19-Aug-2003 : Added equals() method and implemented Cloneable (DG);
 * 01-Sep-2003 : Fixed bug 797466 (no change event when secondary dataset
 *               changes) (DG);
 * 02-Sep-2003 : Fixed bug 795209 (wrong dataset checked in render2 method) and
 *               790407 (initialise method) (DG);
 * 08-Sep-2003 : Added internationalization via use of properties
 *               resourceBundle (RFE 690236) (AL);
 * 08-Sep-2003 : Fixed bug (wrong secondary range axis being used).  Changed
 *               ValueAxis API (DG);
 * 10-Sep-2003 : Fixed bug in setRangeAxis() method (DG);
 * 15-Sep-2003 : Fixed two bugs in serialization, implemented
 *               PublicCloneable (DG);
 * 23-Oct-2003 : Added event notification for changes to renderer (DG);
 * 26-Nov-2003 : Fixed bug (849645) in clearRangeMarkers() method (DG);
 * 03-Dec-2003 : Modified draw method to accept anchor (DG);
 * 21-Jan-2004 : Update for renamed method in ValueAxis (DG);
 * 10-Mar-2004 : Fixed bug in axis range calculation when secondary renderer is
 *               stacked (DG);
 * 12-May-2004 : Added fixed legend items (DG);
 * 19-May-2004 : Added check for null legend item from renderer (DG);
 * 02-Jun-2004 : Updated the DatasetRenderingOrder class (DG);
 * 05-Nov-2004 : Renamed getDatasetsMappedToRangeAxis()
 *               --> datasetsMappedToRangeAxis(), and ensured that returned
 *               list doesn't contain null datasets (DG);
 * 12-Nov-2004 : Implemented new Zoomable interface (DG);
 * 07-Jan-2005 : Renamed getRangeExtent() --> findRangeBounds() in
 *               CategoryItemRenderer (DG);
 * 04-May-2005 : Fixed serialization of range markers (DG);
 * 05-May-2005 : Updated draw() method parameters (DG);
 * 20-May-2005 : Added setDomainAxes() and setRangeAxes() methods, as per
 *               RFE 1183100 (DG);
 * 01-Jun-2005 : Upon deserialization, register plot as a listener with its
 *               axes, dataset(s) and renderer(s) - see patch 1209475 (DG);
 * 02-Jun-2005 : Added support for domain markers (DG);
 * 06-Jun-2005 : Fixed equals() method for use with GradientPaint (DG);
 * 09-Jun-2005 : Added setRenderers(), as per RFE 1183100 (DG);
 * 16-Jun-2005 : Added getDomainAxisCount() and getRangeAxisCount() methods, to
 *               match XYPlot (see RFE 1220495) (DG);
 * ------------- JFREECHART 1.0.x ---------------------------------------------
 * 11-Jan-2006 : Added configureRangeAxes() to rendererChanged(), since the
 *               renderer might influence the axis range (DG);
 * 27-Jan-2006 : Added various null argument checks (DG);
 * 18-Aug-2006 : Added getDatasetCount() method, plus a fix for bug drawing
 *               category labels, thanks to Adriaan Joubert (1277726) (DG);
 * 05-Sep-2006 : Added MarkerChangeEvent support (DG);
 * 30-Oct-2006 : Added getDomainAxisIndex(), datasetsMappedToDomainAxis() and
 *               getCategoriesForAxis() methods (DG);
 * 22-Nov-2006 : Fire PlotChangeEvent from setColumnRenderingOrder() and
 *               setRowRenderingOrder() (DG);
 * 29-Nov-2006 : Fix for bug 1605207 (IntervalMarker exceeds bounds of data
 *               area) (DG);
 * 26-Feb-2007 : Fix for bug 1669218 (setDomainAxisLocation() notify argument
 *               ignored) (DG);
 * 13-Mar-2007 : Added null argument checks for setRangeCrosshairPaint() and
 *               setRangeCrosshairStroke(), fixed clipping for
 *               annotations (DG);
 * 07-Jun-2007 : Override drawBackground() for new GradientPaint handling (DG);
 * 10-Jul-2007 : Added getRangeAxisIndex(ValueAxis) method (DG);
 * 24-Sep-2007 : Implemented new zoom methods (DG);
 * 25-Oct-2007 : Added some argument checks (DG);
 * 05-Nov-2007 : Applied patch 1823697, by Richard West, for removal of domain
 *               and range markers (DG);
 * 14-Nov-2007 : Added missing event notifications (DG);
 * 25-Mar-2008 : Added new methods with optional notification - see patch
 *               1913751 (DG);
 * 07-Apr-2008 : Fixed NPE in removeDomainMarker() and
 *               removeRangeMarker() (DG);
 * 23-Apr-2008 : Fixed equals() and clone() methods (DG);
 * 26-Jun-2008 : Fixed crosshair support (DG);
 * 10-Jul-2008 : Fixed outline visibility for 3D renderers (DG);
 * 12-Aug-2008 : Added rendererCount() method (DG);
 * 25-Nov-2008 : Added facility to map datasets to multiples axes (DG);
 * 15-Dec-2008 : Cleaned up grid drawing methods (DG);
 * 18-Dec-2008 : Use ResourceBundleWrapper - see patch 1607918 by
 *               Jess Thrysoee (DG);
 *
 */

package org.jfree.chart.plot;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.CategoryAnnotation;
import org.jfree.chart.annotations.Annotation;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisCollection;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.AxisState;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.axis.ValueTick;
import org.jfree.chart.event.ChartChangeEventType;
import org.jfree.chart.event.PlotChangeEvent;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.LabelRenderer;
import org.jfree.chart.renderer.ItemRenderer;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.PublicCloneable;
import org.jfree.util.ShapeUtilities;
import org.jfree.util.SortOrder;

/**
 * A general plotting class that uses data from a {@link CategoryDataset} and
 * renders each data item using a {@link CategoryItemRenderer}.
 */
public class CategoryPlot extends AbstractDomainRangePlot implements ValueAxisPlot,
        Zoomable, RendererChangeListener, Cloneable, PublicCloneable,
        Serializable {

    /** For serialization. */
    private static final long serialVersionUID = -3537691700434728188L;

    /** The default value label font. */
    public static final Font DEFAULT_VALUE_LABEL_FONT = new Font("SansSerif",
            Font.PLAIN, 10);

    /** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
            "org.jfree.chart.plot.LocalizationBundle");

    /**
     * A flag that controls whether or not the shared domain axis is drawn
     * (only relevant when the plot is being used as a subplot).
     */
    private boolean drawSharedDomainAxis;

    /**
     * Controls the order in which the columns are traversed when rendering the
     * data items.
     */
    private SortOrder columnRenderingOrder = SortOrder.ASCENDING;

    /**
     * Controls the order in which the rows are traversed when rendering the
     * data items.
     */
    private SortOrder rowRenderingOrder = SortOrder.ASCENDING;

    /** The position of the domain gridlines relative to the category. */
    private CategoryAnchor domainGridlinePosition;


    /** The anchor value. */
    private double anchorValue;

    /**
     * The index for the dataset that the crosshairs are linked to (this
     * determines which axes the crosshairs are plotted against).
     *
     * @since 1.0.11
     */
    private int crosshairDatasetIndex;



    /**
     * Whether to draw the axis gridlines above the data.
     *
     * @since custom
     */
    private boolean gridLinesOverData;

    /**
     * The row key for the crosshair point.
     *
     * @since 1.0.11
     */
    private Comparable domainCrosshairRowKey;

    /**
     * The column key for the crosshair point.
     *
     * @since 1.0.11
     */
    private Comparable domainCrosshairColumnKey;

    /** The range crosshair value. */
    private double rangeCrosshairValue;

    /**
     * A flag that controls whether or not the crosshair locks onto actual
     * data points.
     */
    private boolean rangeCrosshairLockedOnData = true;

    /** A map containing lists of markers for the domain axes. */
    private Map foregroundDomainMarkers;

    /** A map containing lists of markers for the domain axes. */
    private Map backgroundDomainMarkers;

    /** A map containing lists of markers for the range axes. */
    private Map foregroundRangeMarkers;

    /** A map containing lists of markers for the range axes. */
    private Map backgroundRangeMarkers;

    /**
     * The weight for the plot (only relevant when the plot is used as a subplot
     * within a combined plot).
     */
    private int weight;

    /** The fixed space for the domain axis. */
    private AxisSpace fixedDomainAxisSpace;

    /** The fixed space for the range axis. */
    private AxisSpace fixedRangeAxisSpace;

    /**
     * Default constructor.
     */
    public CategoryPlot() {
        this(null, null, null, null);
    }

    /**
     * Creates a new plot.
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     * @param domainAxis  the domain axis (<code>null</code> permitted).
     * @param rangeAxis  the range axis (<code>null</code> permitted).
     * @param renderer  the item renderer (<code>null</code> permitted).
     *
     */
    public CategoryPlot(CategoryDataset dataset,
                        CategoryAxis domainAxis,
                        ValueAxis rangeAxis,
                        CategoryItemRenderer renderer) {

        super(dataset, domainAxis, rangeAxis, renderer);

        this.drawSharedDomainAxis = false;
        this.domainGridlinePosition = CategoryAnchor.MIDDLE;

        this.foregroundDomainMarkers = new HashMap();
        this.backgroundDomainMarkers = new HashMap();
        this.foregroundRangeMarkers = new HashMap();
        this.backgroundRangeMarkers = new HashMap();

        // hard-coded - caused so many troubles in other stuff while I figured out what was causing this.
        // replaced with the rangeZeroBaseline settings that are now handled in AbstractDomainRangePlot 02-Dec-2009.
//        Marker baseline = new ValueMarker(0.0, new Color(0.8f, 0.8f, 0.8f,
//                0.5f), new BasicStroke(1.0f), new Color(0.85f, 0.85f, 0.95f,
//                0.5f), new BasicStroke(1.0f), 0.6f);
//        addRangeMarker(baseline, Layer.BACKGROUND);

        this.anchorValue = 0.0;

        this.rangeCrosshairValue = 0.0;

        this.gridLinesOverData = false;
    }

    /**
     * Returns a string describing the type of plot.
     *
     * @return The type.
     */
    public String getPlotType() {
        return localizationResources.getString("Category_Plot");
    }

    /**
     * Returns the domain axis for the plot.  If the domain axis for this plot
     * is <code>null</code>, then the method will return the parent plot's
     * domain axis (if there is a parent plot).
     *
     * @return The domain axis (<code>null</code> permitted).
     *
     * @see #setDomainAxis(CategoryAxis)
     */
    public CategoryAxis getDomainAxis() {
        return getDomainAxis(0);
    }

    /**
     * Returns a domain axis.
     *
     * @param index  the axis index.
     *
     * @return The axis (<code>null</code> possible).
     *
     * @see #setDomainAxis(int, CategoryAxis)
     */
    public CategoryAxis getDomainAxis(int index) {
        return (CategoryAxis) getBasicDomainAxis(index);
    }

    /**
     * Sets the domain axis for the plot and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     *
     * @param axis  the axis (<code>null</code> permitted).
     *
     * @see #getDomainAxis()
     */
    public void setDomainAxis(CategoryAxis axis) {
        setDomainAxis(0, axis);
    }

    /**
     * Sets a domain axis and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param index  the axis index.
     * @param axis  the axis (<code>null</code> permitted).
     *
     * @see #getDomainAxis(int)
     */
    public void setDomainAxis(int index, CategoryAxis axis) {
        setDomainAxis(index, axis, true);
    }

    /**
     * Sets a domain axis and, if requested, sends a {@link PlotChangeEvent} to
     * all registered listeners.
     *
     * @param index  the axis index.
     * @param axis  the axis (<code>null</code> permitted).
     * @param notify  notify listeners?
     */
    public void setDomainAxis(int index, CategoryAxis axis, boolean notify) {
        super.setDomainAxis(index, axis, notify);
    }

    public void setDomainAxis(int index, Axis axis, boolean notify) throws ClassCastException {
        if(!(axis instanceof CategoryAxis)) {
            throw new ClassCastException("Axis cannot be used as domain for this plot, it should be of type CategoryAxis");
        }
        super.setDomainAxis(index, axis, notify);
    }

    /**
     * Returns the index of the specified axis, or <code>-1</code> if the axis
     * is not assigned to the plot.
     *
     * @param axis  the axis (<code>null</code> not permitted).
     *
     * @return The axis index.
     *
     * @see #getDomainAxis(int)
     * @see #getRangeAxisIndex(ValueAxis)
     *
     * @since 1.0.3
     */
    public int getDomainAxisIndex(CategoryAxis axis) {
        if(axis == null) {
            throw new IllegalArgumentException("Null 'axis' argument.");
        }
        return super.getDomainAxisIndex(axis);
    }

    public int getDomainAxisIndex(Axis axis) {
        return getDomainAxisIndex((CategoryAxis) axis);
    }

    /**
     * Returns the index of the specified axis, or <code>-1</code> if the axis
     * is not assigned to the plot.
     *
     * @param axis  the axis (<code>null</code> not permitted).
     *
     * @return The axis index.
     *
     * @see #getRangeAxis(int)
     * @see #getDomainAxisIndex(Axis)
     *
     * @since 1.0.3
     */
    public int getRangeAxisIndex(ValueAxis axis) {
        if(axis == null) {
            throw new IllegalArgumentException("Null 'axis' argument.");
        }
        return super.getRangeAxisIndex(axis);
    }

    public void setDataset(Dataset dataset) throws ClassCastException {
        setDataset(0, dataset);
    }

    public void setDataset(int index, Dataset dataset) throws ClassCastException {
        if(!(dataset instanceof CategoryDataset)) {
            throw new ClassCastException("CategoryDatasets must be used with CategoryPlot");
        }
        super.setDataset(index, dataset);
    }


    /**
     * Returns the primary dataset for the plot.
     *
     * @return The primary dataset (possibly <code>null</code>).
     *
     * @see #setDataset(CategoryDataset)
     */
    public CategoryDataset getDataset() {
        return getDataset(0);
    }

    /**
     * Returns the dataset at the given index.
     *
     * @param index  the dataset index.
     *
     * @return The dataset (possibly <code>null</code>).
     *
     * @see #setDataset(int, CategoryDataset)
     */
    public CategoryDataset getDataset(int index) {
        return (CategoryDataset) getBasicDataset(index);
    }

    /**
     * Sets the dataset for the plot, replacing the existing dataset, if there
     * is one.  This method also calls the
     * {@link #datasetChanged(DatasetChangeEvent)} method, which adjusts the
     * axis ranges if necessary and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @see #getDataset()
     */
    public void setDataset(CategoryDataset dataset) {
        setDataset(0, dataset);
    }

    /**
     * Sets a dataset for the plot.
     *
     * @param index  the dataset index.
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @see #getDataset(int)
     */
    public void setDataset(int index, CategoryDataset dataset) {
        super.setDataset(index, dataset);
    }

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
    public CategoryAxis getDomainAxisForDataset(int index) {
        return (CategoryAxis) getBasicDomainAxisForDataset(index);
    }

    /**
     * Returns a reference to the renderer for the plot.
     *
     * @return The renderer.
     *
     * @see #setRenderer(CategoryItemRenderer)
     */
    public CategoryItemRenderer getRenderer() {
        return getRenderer(0);
    }

    /**
     * Returns the renderer at the given index.
     *
     * @param index  the renderer index.
     *
     * @return The renderer (possibly <code>null</code>).
     *
     * @see #setRenderer(int, CategoryItemRenderer)
     */
    public CategoryItemRenderer getRenderer(int index) {
        return (CategoryItemRenderer) getBasicRenderer(index);
    }

    /**
     * Sets the renderer at index 0 (sometimes referred to as the "primary"
     * renderer) and sends a {@link PlotChangeEvent} to all registered
     * listeners.
     *
     * @param renderer  the renderer (<code>null</code> permitted.
     *
     * @see #getRenderer()
     */
    public void setRenderer(CategoryItemRenderer renderer) {
        setRenderer(0, renderer, true);
    }

    /**
     * Sets the renderer at index 0 (sometimes referred to as the "primary"
     * renderer) and, if requested, sends a {@link PlotChangeEvent} to all
     * registered listeners.
     * <p>
     * You can set the renderer to <code>null</code>, but this is not
     * recommended because:
     * <ul>
     *   <li>no data will be displayed;</li>
     *   <li>the plot background will not be painted;</li>
     * </ul>
     *
     * @param renderer  the renderer (<code>null</code> permitted).
     * @param notify  notify listeners?
     *
     * @see #getRenderer()
     */
    public void setRenderer(CategoryItemRenderer renderer, boolean notify) {
        setRenderer(0, renderer, notify);
    }

    /**
     * Sets the renderer at the specified index and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param index  the index.
     * @param renderer  the renderer (<code>null</code> permitted).
     *
     * @see #getRenderer(int)
     * @see #setRenderer(int, CategoryItemRenderer, boolean)
     */
    public void setRenderer(int index, CategoryItemRenderer renderer) {
        setRenderer(index, renderer, true);
    }

    /**
     * Sets a renderer.  A {@link PlotChangeEvent} is sent to all registered
     * listeners.
     *
     * @param index  the index.
     * @param renderer  the renderer (<code>null</code> permitted).
     * @param notify  notify listeners?
     *
     * @see #getRenderer(int)
     */
    public void setRenderer(int index, CategoryItemRenderer renderer,
                            boolean notify) {

        super.setRenderer(index, renderer, notify);
    }

    public void setRenderer(int index, ItemRenderer renderer,
                            boolean notify) {
        if(!(renderer instanceof CategoryItemRenderer)) {
            throw new ClassCastException("Renderers for this plot must be of type CategoryItemRenderer");
        }
        super.setRenderer(index, renderer, notify);
    }

    /**
     * Returns the renderer for the specified dataset.  If the dataset doesn't
     * belong to the plot, this method will return <code>null</code>.
     *
     * @param dataset  the dataset (<code>null</code> permitted).
     *
     * @return The renderer (possibly <code>null</code>).
     */
    public CategoryItemRenderer getRendererForDataset(CategoryDataset dataset) {
        return (CategoryItemRenderer) getBasicRendererForDataset(dataset);
    }

    /**
     * Returns the order in which the columns are rendered.  The default value
     * is <code>SortOrder.ASCENDING</code>.
     *
     * @return The column rendering order (never <code>null</code).
     *
     * @see #setColumnRenderingOrder(SortOrder)
     */
    public SortOrder getColumnRenderingOrder() {
        return this.columnRenderingOrder;
    }

    /**
     * Sets the column order in which the items in each dataset should be
     * rendered and sends a {@link PlotChangeEvent} to all registered
     * listeners.  Note that this affects the order in which items are drawn,
     * NOT their position in the chart.
     *
     * @param order  the order (<code>null</code> not permitted).
     *
     * @see #getColumnRenderingOrder()
     * @see #setRowRenderingOrder(SortOrder)
     */
    public void setColumnRenderingOrder(SortOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Null 'order' argument.");
        }
        this.columnRenderingOrder = order;
        fireChangeEvent();
    }

    /**
     * Returns the order in which the rows should be rendered.  The default
     * value is <code>SortOrder.ASCENDING</code>.
     *
     * @return The order (never <code>null</code>).
     *
     * @see #setRowRenderingOrder(SortOrder)
     */
    public SortOrder getRowRenderingOrder() {
        return this.rowRenderingOrder;
    }

    /**
     * Sets the row order in which the items in each dataset should be
     * rendered and sends a {@link PlotChangeEvent} to all registered
     * listeners.  Note that this affects the order in which items are drawn,
     * NOT their position in the chart.
     *
     * @param order  the order (<code>null</code> not permitted).
     *
     * @see #getRowRenderingOrder()
     * @see #setColumnRenderingOrder(SortOrder)
     */
    public void setRowRenderingOrder(SortOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Null 'order' argument.");
        }
        this.rowRenderingOrder = order;
        fireChangeEvent();
    }

    /**
     * Returns the position used for the domain gridlines.
     *
     * @return The gridline position (never <code>null</code>).
     *
     * @see #setDomainGridlinePosition(CategoryAnchor)
     */
    public CategoryAnchor getDomainGridlinePosition() {
        return this.domainGridlinePosition;
    }

    /**
     * Sets the position used for the domain gridlines and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param position  the position (<code>null</code> not permitted).
     *
     * @see #getDomainGridlinePosition()
     */
    public void setDomainGridlinePosition(CategoryAnchor position) {
        if (position == null) {
            throw new IllegalArgumentException("Null 'position' argument.");
        }
        this.domainGridlinePosition = position;
        fireChangeEvent();
    }

    /**
     * Returns a flag that controls whether or not the axis gridlines are drawn after the data
     * is rendered.
     *
     * @return A boolean.
     *
     * @see #setGridLinesOverData(boolean)
     */
    public boolean isGridLinesOverData() {
        return gridLinesOverData;
    }

    /**
     * Sets the flag that controls whether or not the gridlines are drawn over the
     * data, and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     *
     * @param gridLinesOverData  the flag.
     *
     * @see #isGridLinesOverData()
     */
    public void setGridLinesOverData(boolean gridLinesOverData) {
        this.gridLinesOverData = gridLinesOverData;
        fireChangeEvent();
    }

    /**
     * Returns the legend items for the plot.  By default, this method creates
     * a legend item for each series in each of the datasets.  You can change
     * this behaviour by overriding this method.
     *
     * @return The legend items.
     */
    public LegendItemCollection getLegendItems() {
        LegendItemCollection result = getFixedLegendItems();
        if (result == null) {
            result = new LegendItemCollection();
            // get the legend items for the datasets...
            int count = getDatasetCount();
            for (int datasetIndex = 0; datasetIndex < count; datasetIndex++) {
                CategoryDataset dataset = getDataset(datasetIndex);
                if (dataset != null) {
                    CategoryItemRenderer renderer = getRenderer(datasetIndex);
                    if (renderer != null) {
                        int seriesCount = dataset.getRowCount();
                        for (int i = 0; i < seriesCount; i++) {
                            LegendItem item = renderer.getLegendItem(
                                    datasetIndex, i);
                            if (item != null) {
                                result.add(item);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Handles a 'click' on the plot by updating the anchor value.
     *
     * @param x  x-coordinate of the click (in Java2D space).
     * @param y  y-coordinate of the click (in Java2D space).
     * @param info  information about the plot's dimensions.
     *
     */
    public void handleClick(int x, int y, PlotRenderingInfo info) {

        Rectangle2D dataArea = info.getDataArea();
        if (dataArea.contains(x, y)) {
            // set the anchor value for the range axis...
            double java2D = 0.0;
            PlotOrientation orientation = getOrientation();
            if (orientation == PlotOrientation.HORIZONTAL) {
                java2D = x;
            }
            else if (orientation == PlotOrientation.VERTICAL) {
                java2D = y;
            }
            RectangleEdge edge = Plot.resolveRangeAxisLocation(
                    getRangeAxisLocation(), orientation);
            double value = getRangeAxis().java2DToValue(
                    java2D, info.getDataArea(), edge);
            setAnchorValue(value);
            setRangeCrosshairValue(value);
        }

    }

    /**
     * Zooms (in or out) on the plot's value axis.
     * <p>
     * If the value 0.0 is passed in as the zoom percent, the auto-range
     * calculation for the axis is restored (which sets the range to include
     * the minimum and maximum data values, thus displaying all the data).
     *
     * @param percent  the zoom amount.
     */
    public void zoom(double percent) {

        if (percent > 0.0) {
            double range = getRangeAxis().getRange().getLength();
            double scaledRange = range * percent;
            getRangeAxis().setRange(this.anchorValue - scaledRange / 2.0,
                    this.anchorValue + scaledRange / 2.0);
        }
        else {
            getRangeAxis().setAutoRange(true);
        }

    }

    /**
     * Receives notification of a change to the plot's dataset.
     * <P>
     * The range axis bounds will be recalculated if necessary.
     *
     * @param event  information about the event (not used here).
     */
    public void datasetChanged(DatasetChangeEvent event) {

        int count = getRangeAxisCount();
        for (int axisIndex = 0; axisIndex < count; axisIndex++) {
            ValueAxis yAxis = getRangeAxis(axisIndex);
            if (yAxis != null) {
                yAxis.configure();
            }
        }
        if (getParent() != null) {
            getParent().datasetChanged(event);
        }
        else {
            PlotChangeEvent e = new PlotChangeEvent(this);
            e.setType(ChartChangeEventType.DATASET_UPDATED);
            notifyListeners(e);
        }

    }

    /**
     * Receives notification of a renderer change event.
     *
     * @param event  the event.
     */
    public void rendererChanged(RendererChangeEvent event) {
        Plot parent = getParent();
        if (parent != null) {
            if (parent instanceof RendererChangeListener) {
                RendererChangeListener rcl = (RendererChangeListener) parent;
                rcl.rendererChanged(event);
            }
            else {
                // this should never happen with the existing code, but throw
                // an exception in case future changes make it possible...
                throw new RuntimeException(
                    "The renderer has changed and I don't know what to do!");
            }
        }
        else {
            configureRangeAxes();
            PlotChangeEvent e = new PlotChangeEvent(this);
            notifyListeners(e);
        }
    }

    /**
     * Adds a marker for display (in the foreground) against the domain axis and
     * sends a {@link PlotChangeEvent} to all registered listeners. Typically a
     * marker will be drawn by the renderer as a line perpendicular to the
     * domain axis, however this is entirely up to the renderer.
     *
     * @param marker  the marker (<code>null</code> not permitted).
     *
     * @see #removeDomainMarker(Marker)
     */
    public void addDomainMarker(CategoryMarker marker) {
        addDomainMarker(marker, Layer.FOREGROUND);
    }

    /**
     * Adds a marker for display against the domain axis and sends a
     * {@link PlotChangeEvent} to all registered listeners.  Typically a marker
     * will be drawn by the renderer as a line perpendicular to the domain
     * axis, however this is entirely up to the renderer.
     *
     * @param marker  the marker (<code>null</code> not permitted).
     * @param layer  the layer (foreground or background) (<code>null</code>
     *               not permitted).
     *
     * @see #removeDomainMarker(Marker, Layer)
     */
    public void addDomainMarker(CategoryMarker marker, Layer layer) {
        addDomainMarker(0, marker, layer);
    }

    /**
     * Adds a marker for display by a particular renderer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * <P>
     * Typically a marker will be drawn by the renderer as a line perpendicular
     * to a domain axis, however this is entirely up to the renderer.
     *
     * @param index  the renderer index.
     * @param marker  the marker (<code>null</code> not permitted).
     * @param layer  the layer (<code>null</code> not permitted).
     *
     * @see #removeDomainMarker(int, Marker, Layer)
     */
    public void addDomainMarker(int index, CategoryMarker marker, Layer layer) {
        addDomainMarker(index, marker, layer, true);
    }

    /**
     * Adds a marker for display by a particular renderer and, if requested,
     * sends a {@link PlotChangeEvent} to all registered listeners.
     * <P>
     * Typically a marker will be drawn by the renderer as a line perpendicular
     * to a domain axis, however this is entirely up to the renderer.
     *
     * @param index  the renderer index.
     * @param marker  the marker (<code>null</code> not permitted).
     * @param layer  the layer (<code>null</code> not permitted).
     * @param notify  notify listeners?
     *
     * @since 1.0.10
     *
     * @see #removeDomainMarker(int, Marker, Layer, boolean)
     */
    public void addDomainMarker(int index, CategoryMarker marker, Layer layer,
            boolean notify) {
        if (marker == null) {
            throw new IllegalArgumentException("Null 'marker' not permitted.");
        }
        if (layer == null) {
            throw new IllegalArgumentException("Null 'layer' not permitted.");
        }
        Collection markers;
        if (layer == Layer.FOREGROUND) {
            markers = (Collection) this.foregroundDomainMarkers.get(
                    new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.foregroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        else if (layer == Layer.BACKGROUND) {
            markers = (Collection) this.backgroundDomainMarkers.get(
                    new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.backgroundDomainMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        marker.addChangeListener(this);
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Clears all the domain markers for the plot and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @see #clearRangeMarkers()
     */
    public void clearDomainMarkers() {
        if (this.backgroundDomainMarkers != null) {
            Set keys = this.backgroundDomainMarkers.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                Integer key = (Integer) iterator.next();
                clearDomainMarkers(key.intValue());
            }
            this.backgroundDomainMarkers.clear();
        }
        if (this.foregroundDomainMarkers != null) {
            Set keys = this.foregroundDomainMarkers.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                Integer key = (Integer) iterator.next();
                clearDomainMarkers(key.intValue());
            }
            this.foregroundDomainMarkers.clear();
        }
        fireChangeEvent();
    }

    /**
     * Returns the list of domain markers (read only) for the specified layer.
     *
     * @param layer  the layer (foreground or background).
     *
     * @return The list of domain markers.
     */
    public Collection getDomainMarkers(Layer layer) {
        return getDomainMarkers(0, layer);
    }

    /**
     * Returns a collection of domain markers for a particular renderer and
     * layer.
     *
     * @param index  the renderer index.
     * @param layer  the layer.
     *
     * @return A collection of markers (possibly <code>null</code>).
     */
    public Collection getDomainMarkers(int index, Layer layer) {
        Collection result = null;
        Integer key = new Integer(index);
        if (layer == Layer.FOREGROUND) {
            result = (Collection) this.foregroundDomainMarkers.get(key);
        }
        else if (layer == Layer.BACKGROUND) {
            result = (Collection) this.backgroundDomainMarkers.get(key);
        }
        if (result != null) {
            result = Collections.unmodifiableCollection(result);
        }
        return result;
    }

    /**
     * Clears all the domain markers for the specified renderer.
     *
     * @param index  the renderer index.
     *
     * @see #clearRangeMarkers(int)
     */
    public void clearDomainMarkers(int index) {
        Integer key = new Integer(index);
        if (this.backgroundDomainMarkers != null) {
            Collection markers
                = (Collection) this.backgroundDomainMarkers.get(key);
            if (markers != null) {
                Iterator iterator = markers.iterator();
                while (iterator.hasNext()) {
                    Marker m = (Marker) iterator.next();
                    m.removeChangeListener(this);
                }
                markers.clear();
            }
        }
        if (this.foregroundDomainMarkers != null) {
            Collection markers
                = (Collection) this.foregroundDomainMarkers.get(key);
            if (markers != null) {
                Iterator iterator = markers.iterator();
                while (iterator.hasNext()) {
                    Marker m = (Marker) iterator.next();
                    m.removeChangeListener(this);
                }
                markers.clear();
            }
        }
        fireChangeEvent();
    }

    /**
     * Removes a marker for the domain axis and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     *
     * @param marker  the marker.
     *
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     *
     * @since 1.0.7
     */
    public boolean removeDomainMarker(Marker marker) {
        return removeDomainMarker(marker, Layer.FOREGROUND);
    }

    /**
     * Removes a marker for the domain axis in the specified layer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param marker the marker (<code>null</code> not permitted).
     * @param layer the layer (foreground or background).
     *
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     *
     * @since 1.0.7
     */
    public boolean removeDomainMarker(Marker marker, Layer layer) {
        return removeDomainMarker(0, marker, layer);
    }

    /**
     * Removes a marker for a specific dataset/renderer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param index the dataset/renderer index.
     * @param marker the marker.
     * @param layer the layer (foreground or background).
     *
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     *
     * @since 1.0.7
     */
    public boolean removeDomainMarker(int index, Marker marker, Layer layer) {
        return removeDomainMarker(index, marker, layer, true);
    }

    /**
     * Removes a marker for a specific dataset/renderer and, if requested,
     * sends a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param index the dataset/renderer index.
     * @param marker the marker.
     * @param layer the layer (foreground or background).
     * @param notify  notify listeners?
     *
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     *
     * @since 1.0.10
     */
    public boolean removeDomainMarker(int index, Marker marker, Layer layer,
            boolean notify) {
        ArrayList markers;
        if (layer == Layer.FOREGROUND) {
            markers = (ArrayList) this.foregroundDomainMarkers.get(new Integer(
                    index));
        }
        else {
            markers = (ArrayList) this.backgroundDomainMarkers.get(new Integer(
                    index));
        }
        if (markers == null) {
            return false;
        }
        boolean removed = markers.remove(marker);
        if (removed && notify) {
            fireChangeEvent();
        }
        return removed;
    }

    /**
     * Adds a marker for display (in the foreground) against the range axis and
     * sends a {@link PlotChangeEvent} to all registered listeners. Typically a
     * marker will be drawn by the renderer as a line perpendicular to the
     * range axis, however this is entirely up to the renderer.
     *
     * @param marker  the marker (<code>null</code> not permitted).
     *
     * @see #removeRangeMarker(Marker)
     */
    public void addRangeMarker(Marker marker) {
        addRangeMarker(marker, Layer.FOREGROUND);
    }

    /**
     * Adds a marker for display against the range axis and sends a
     * {@link PlotChangeEvent} to all registered listeners.  Typically a marker
     * will be drawn by the renderer as a line perpendicular to the range axis,
     * however this is entirely up to the renderer.
     *
     * @param marker  the marker (<code>null</code> not permitted).
     * @param layer  the layer (foreground or background) (<code>null</code>
     *               not permitted).
     *
     * @see #removeRangeMarker(Marker, Layer)
     */
    public void addRangeMarker(Marker marker, Layer layer) {
        addRangeMarker(0, marker, layer);
    }

    /**
     * Adds a marker for display by a particular renderer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * <P>
     * Typically a marker will be drawn by the renderer as a line perpendicular
     * to a range axis, however this is entirely up to the renderer.
     *
     * @param index  the renderer index.
     * @param marker  the marker.
     * @param layer  the layer.
     *
     * @see #removeRangeMarker(int, Marker, Layer)
     */
    public void addRangeMarker(int index, Marker marker, Layer layer) {
        addRangeMarker(index, marker, layer, true);
    }

    /**
     * Adds a marker for display by a particular renderer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     * <P>
     * Typically a marker will be drawn by the renderer as a line perpendicular
     * to a range axis, however this is entirely up to the renderer.
     *
     * @param index  the renderer index.
     * @param marker  the marker.
     * @param layer  the layer.
     * @param notify  notify listeners?
     *
     * @since 1.0.10
     *
     * @see #removeRangeMarker(int, Marker, Layer, boolean)
     */
    public void addRangeMarker(int index, Marker marker, Layer layer,
            boolean notify) {
        Collection markers;
        if (layer == Layer.FOREGROUND) {
            markers = (Collection) this.foregroundRangeMarkers.get(
                    new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.foregroundRangeMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        else if (layer == Layer.BACKGROUND) {
            markers = (Collection) this.backgroundRangeMarkers.get(
                    new Integer(index));
            if (markers == null) {
                markers = new java.util.ArrayList();
                this.backgroundRangeMarkers.put(new Integer(index), markers);
            }
            markers.add(marker);
        }
        marker.addChangeListener(this);
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Clears all the range markers for the plot and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @see #clearDomainMarkers()
     */
    public void clearRangeMarkers() {
        if (this.backgroundRangeMarkers != null) {
            Set keys = this.backgroundRangeMarkers.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                Integer key = (Integer) iterator.next();
                clearRangeMarkers(key.intValue());
            }
            this.backgroundRangeMarkers.clear();
        }
        if (this.foregroundRangeMarkers != null) {
            Set keys = this.foregroundRangeMarkers.keySet();
            Iterator iterator = keys.iterator();
            while (iterator.hasNext()) {
                Integer key = (Integer) iterator.next();
                clearRangeMarkers(key.intValue());
            }
            this.foregroundRangeMarkers.clear();
        }
        fireChangeEvent();
    }

    /**
     * Returns the list of range markers (read only) for the specified layer.
     *
     * @param layer  the layer (foreground or background).
     *
     * @return The list of range markers.
     *
     * @see #getRangeMarkers(int, Layer)
     */
    public Collection getRangeMarkers(Layer layer) {
        return getRangeMarkers(0, layer);
    }

    /**
     * Returns a collection of range markers for a particular renderer and
     * layer.
     *
     * @param index  the renderer index.
     * @param layer  the layer.
     *
     * @return A collection of markers (possibly <code>null</code>).
     */
    public Collection getRangeMarkers(int index, Layer layer) {
        Collection result = null;
        Integer key = new Integer(index);
        if (layer == Layer.FOREGROUND) {
            result = (Collection) this.foregroundRangeMarkers.get(key);
        }
        else if (layer == Layer.BACKGROUND) {
            result = (Collection) this.backgroundRangeMarkers.get(key);
        }
        if (result != null) {
            result = Collections.unmodifiableCollection(result);
        }
        return result;
    }

    /**
     * Clears all the range markers for the specified renderer.
     *
     * @param index  the renderer index.
     *
     * @see #clearDomainMarkers(int)
     */
    public void clearRangeMarkers(int index) {
        Integer key = new Integer(index);
        if (this.backgroundRangeMarkers != null) {
            Collection markers
                = (Collection) this.backgroundRangeMarkers.get(key);
            if (markers != null) {
                Iterator iterator = markers.iterator();
                while (iterator.hasNext()) {
                    Marker m = (Marker) iterator.next();
                    m.removeChangeListener(this);
                }
                markers.clear();
            }
        }
        if (this.foregroundRangeMarkers != null) {
            Collection markers
                = (Collection) this.foregroundRangeMarkers.get(key);
            if (markers != null) {
                Iterator iterator = markers.iterator();
                while (iterator.hasNext()) {
                    Marker m = (Marker) iterator.next();
                    m.removeChangeListener(this);
                }
                markers.clear();
            }
        }
        fireChangeEvent();
    }

    /**
     * Removes a marker for the range axis and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     *
     * @param marker the marker.
     *
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     *
     * @since 1.0.7
     *
     * @see #addRangeMarker(Marker)
     */
    public boolean removeRangeMarker(Marker marker) {
        return removeRangeMarker(marker, Layer.FOREGROUND);
    }

    /**
     * Removes a marker for the range axis in the specified layer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param marker the marker (<code>null</code> not permitted).
     * @param layer the layer (foreground or background).
     *
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     *
     * @since 1.0.7
     *
     * @see #addRangeMarker(Marker, Layer)
     */
    public boolean removeRangeMarker(Marker marker, Layer layer) {
        return removeRangeMarker(0, marker, layer);
    }

    /**
     * Removes a marker for a specific dataset/renderer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param index the dataset/renderer index.
     * @param marker the marker.
     * @param layer the layer (foreground or background).
     *
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     *
     * @since 1.0.7
     *
     * @see #addRangeMarker(int, Marker, Layer)
     */
    public boolean removeRangeMarker(int index, Marker marker, Layer layer) {
        return removeRangeMarker(index, marker, layer, true);
    }

    /**
     * Removes a marker for a specific dataset/renderer and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param index  the dataset/renderer index.
     * @param marker  the marker.
     * @param layer  the layer (foreground or background).
     * @param notify  notify listeners.
     *
     * @return A boolean indicating whether or not the marker was actually
     *         removed.
     *
     * @since 1.0.10
     *
     * @see #addRangeMarker(int, Marker, Layer, boolean)
     */
    public boolean removeRangeMarker(int index, Marker marker, Layer layer,
            boolean notify) {
        if (marker == null) {
            throw new IllegalArgumentException("Null 'marker' argument.");
        }
        ArrayList markers;
        if (layer == Layer.FOREGROUND) {
            markers = (ArrayList) this.foregroundRangeMarkers.get(new Integer(
                    index));
        }
        else {
            markers = (ArrayList) this.backgroundRangeMarkers.get(new Integer(
                    index));
        }
        if (markers == null) {
            return false;
        }
        boolean removed = markers.remove(marker);
        if (removed && notify) {
            fireChangeEvent();
        }
        return removed;
    }

    /**
     * Returns the row key for the domain crosshair.
     *
     * @return The row key.
     *
     * @since 1.0.11
     */
    public Comparable getDomainCrosshairRowKey() {
        return this.domainCrosshairRowKey;
    }

    /**
     * Sets the row key for the domain crosshair and sends a
     * {PlotChangeEvent} to all registered listeners.
     *
     * @param key  the key.
     *
     * @since 1.0.11
     */
    public void setDomainCrosshairRowKey(Comparable key) {
        setDomainCrosshairRowKey(key, true);
    }

    /**
     * Sets the row key for the domain crosshair and, if requested, sends a
     * {PlotChangeEvent} to all registered listeners.
     *
     * @param key  the key.
     * @param notify  notify listeners?
     *
     * @since 1.0.11
     */
    public void setDomainCrosshairRowKey(Comparable key, boolean notify) {
        this.domainCrosshairRowKey = key;
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns the column key for the domain crosshair.
     *
     * @return The column key.
     *
     * @since 1.0.11
     */
    public Comparable getDomainCrosshairColumnKey() {
        return this.domainCrosshairColumnKey;
    }

    /**
     * Sets the column key for the domain crosshair and sends
     * a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param key  the key.
     *
     * @since 1.0.11
     */
    public void setDomainCrosshairColumnKey(Comparable key) {
        setDomainCrosshairColumnKey(key, true);
    }

    /**
     * Sets the column key for the domain crosshair and, if requested, sends
     * a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param key  the key.
     * @param notify  notify listeners?
     *
     * @since 1.0.11
     */
    public void setDomainCrosshairColumnKey(Comparable key, boolean notify) {
        this.domainCrosshairColumnKey = key;
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns the dataset index for the crosshair.
     *
     * @return The dataset index.
     *
     * @since 1.0.11
     */
    public int getCrosshairDatasetIndex() {
        return this.crosshairDatasetIndex;
    }

    /**
     * Sets the dataset index for the crosshair and sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param index  the index.
     *
     * @since 1.0.11
     */
    public void setCrosshairDatasetIndex(int index) {
        setCrosshairDatasetIndex(index, true);
    }

    /**
     * Sets the dataset index for the crosshair and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param index  the index.
     * @param notify  notify listeners?
     *
     * @since 1.0.11
     */
    public void setCrosshairDatasetIndex(int index, boolean notify) {
        this.crosshairDatasetIndex = index;
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns a flag indicating whether or not the crosshair should "lock-on"
     * to actual data values.
     *
     * @return The flag.
     *
     * @see #setRangeCrosshairLockedOnData(boolean)
     */
    public boolean isRangeCrosshairLockedOnData() {
        return this.rangeCrosshairLockedOnData;
    }

    /**
     * Sets the flag indicating whether or not the range crosshair should
     * "lock-on" to actual data values, and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     *
     * @param flag  the flag.
     *
     * @see #isRangeCrosshairLockedOnData()
     */
    public void setRangeCrosshairLockedOnData(boolean flag) {
        if (this.rangeCrosshairLockedOnData != flag) {
            this.rangeCrosshairLockedOnData = flag;
            fireChangeEvent();
        }
    }

    /**
     * Returns the range crosshair value.
     *
     * @return The value.
     *
     * @see #setRangeCrosshairValue(double)
     */
    public double getRangeCrosshairValue() {
        return this.rangeCrosshairValue;
    }

    /**
     * Sets the range crosshair value and, if the crosshair is visible, sends
     * a {@link PlotChangeEvent} to all registered listeners.
     *
     * @param value  the new value.
     *
     * @see #getRangeCrosshairValue()
     */
    public void setRangeCrosshairValue(double value) {
        setRangeCrosshairValue(value, true);
    }

    /**
     * Sets the range crosshair value and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners (but only if the
     * crosshair is visible).
     *
     * @param value  the new value.
     * @param notify  a flag that controls whether or not listeners are
     *                notified.
     *
     * @see #getRangeCrosshairValue()
     */
    public void setRangeCrosshairValue(double value, boolean notify) {
        this.rangeCrosshairValue = value;
        if (isRangeCrosshairVisible() && notify) {
            fireChangeEvent();
        }
    }


    /**
     * Adds an annotation to the plot and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param annotation  the annotation (<code>null</code> not permitted).
     *
     * @see #removeAnnotation(CategoryAnnotation)
     */
    public void addAnnotation(CategoryAnnotation annotation) {
        addAnnotation(annotation, true);
    }

    /**
     * Adds an annotation to the plot and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param annotation  the annotation (<code>null</code> not permitted).
     * @param notify  notify listeners?
     *
     * @since 1.0.10
     */
    public void addAnnotation(CategoryAnnotation annotation, boolean notify) {
        innerAddAnnotation(annotation,  notify);
    }

    /**
     * Removes an annotation from the plot and sends a {@link PlotChangeEvent}
     * to all registered listeners.
     *
     * @param annotation  the annotation (<code>null</code> not permitted).
     *
     * @return A boolean (indicates whether or not the annotation was removed).
     *
     * @see #addAnnotation(CategoryAnnotation)
     */
    public boolean removeAnnotation(CategoryAnnotation annotation) {
        return removeAnnotation(annotation, true);
    }

    /**
     * Removes an annotation from the plot and, if requested, sends a
     * {@link PlotChangeEvent} to all registered listeners.
     *
     * @param annotation  the annotation (<code>null</code> not permitted).
     * @param notify  notify listeners?
     *
     * @return A boolean (indicates whether or not the annotation was removed).
     *
     * @since 1.0.10
     */
    public boolean removeAnnotation(CategoryAnnotation annotation,
            boolean notify) {
        return innerRemoveAnnotation(annotation,  notify);
    }

    public void addAnnotation(Annotation annotation, boolean notify) {
        addAnnotation((CategoryAnnotation) annotation, notify);
    }

    public boolean removeAnnotation(Annotation annotation, boolean notify) {
        return annotation instanceof CategoryAnnotation && removeAnnotation((CategoryAnnotation) annotation, notify);
    }

    /**
     * Calculates the space required for the domain axis/axes.
     *
     * @param g2  the graphics device.
     * @param plotArea  the plot area.
     * @param space  a carrier for the result (<code>null</code> permitted).
     *
     * @return The required space.
     */
    protected AxisSpace calculateDomainAxisSpace(Graphics2D g2,
                                                 Rectangle2D plotArea,
                                                 AxisSpace space) {

        if (space == null) {
            space = new AxisSpace();
        }

        PlotOrientation orientation = getOrientation();

        // reserve some space for the domain axis...
        if (this.fixedDomainAxisSpace != null) {
            if (orientation == PlotOrientation.HORIZONTAL) {
                space.ensureAtLeast(
                    this.fixedDomainAxisSpace.getLeft(), RectangleEdge.LEFT);
                space.ensureAtLeast(this.fixedDomainAxisSpace.getRight(),
                        RectangleEdge.RIGHT);
            }
            else if (orientation == PlotOrientation.VERTICAL) {
                space.ensureAtLeast(this.fixedDomainAxisSpace.getTop(),
                        RectangleEdge.TOP);
                space.ensureAtLeast(this.fixedDomainAxisSpace.getBottom(),
                        RectangleEdge.BOTTOM);
            }
        }
        else {
            // reserve space for the primary domain axis...
            RectangleEdge domainEdge = Plot.resolveDomainAxisLocation(
                    getDomainAxisLocation(), orientation);
            if (this.drawSharedDomainAxis) {
                space = getDomainAxis().reserveSpace(g2, this, plotArea,
                        domainEdge, space);
            }

            // reserve space for any domain axes...
            for (int i = 0; i < getDomainAxisCount(); i++) {
                Axis xAxis = getDomainAxis(i);
                if (xAxis != null) {
                    RectangleEdge edge = getDomainAxisEdge(i);
                    space = xAxis.reserveSpace(g2, this, plotArea, edge, space);
                }
            }
        }

        return space;

    }

    /**
     * Calculates the space required for the range axis/axes.
     *
     * @param g2  the graphics device.
     * @param plotArea  the plot area.
     * @param space  a carrier for the result (<code>null</code> permitted).
     *
     * @return The required space.
     */
    protected AxisSpace calculateRangeAxisSpace(Graphics2D g2,
                                                Rectangle2D plotArea,
                                                AxisSpace space) {

        if (space == null) {
            space = new AxisSpace();
        }
        PlotOrientation orientation = getOrientation();

        // reserve some space for the range axis...
        if (this.fixedRangeAxisSpace != null) {
            if (orientation == PlotOrientation.HORIZONTAL) {
                space.ensureAtLeast(this.fixedRangeAxisSpace.getTop(),
                        RectangleEdge.TOP);
                space.ensureAtLeast(this.fixedRangeAxisSpace.getBottom(),
                        RectangleEdge.BOTTOM);
            }
            else if (orientation == PlotOrientation.VERTICAL) {
                space.ensureAtLeast(this.fixedRangeAxisSpace.getLeft(),
                        RectangleEdge.LEFT);
                space.ensureAtLeast(this.fixedRangeAxisSpace.getRight(),
                        RectangleEdge.RIGHT);
            }
        }
        else {
            // reserve space for the range axes (if any)...
            for (int i = 0; i < getRangeAxisCount(); i++) {
                Axis yAxis = getRangeAxis(i);
                if (yAxis != null) {
                    RectangleEdge edge = getRangeAxisEdge(i);
                    space = yAxis.reserveSpace(g2, this, plotArea, edge, space);
                }
            }
        }
        return space;

    }

    /**
     * Calculates the space required for the axes.
     *
     * @param g2  the graphics device.
     * @param plotArea  the plot area.
     *
     * @return The space required for the axes.
     */
    protected AxisSpace calculateAxisSpace(Graphics2D g2,
                                           Rectangle2D plotArea) {
        AxisSpace space = new AxisSpace();
        space = calculateRangeAxisSpace(g2, plotArea, space);
        space = calculateDomainAxisSpace(g2, plotArea, space);
        return space;
    }

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
    public void draw(Graphics2D g2, Rectangle2D area,
                     Point2D anchor,
                     PlotState parentState,
                     PlotRenderingInfo state) {

        // if the plot area is too small, just return...
        boolean b1 = (area.getWidth() <= MINIMUM_WIDTH_TO_DRAW);
        boolean b2 = (area.getHeight() <= MINIMUM_HEIGHT_TO_DRAW);
        if (b1 || b2) {
            return;
        }

        // record the plot area...
        if (state == null) {
            // if the incoming state is null, no information will be passed
            // back to the caller - but we create a temporary state to record
            // the plot area, since that is used later by the axes
            state = new PlotRenderingInfo(null);
        }
        state.setPlotArea(area);

        // adjust the drawing area for the plot insets (if any)...
        RectangleInsets insets = getInsets();
        insets.trim(area);

        // calculate the data area...
        AxisSpace space = calculateAxisSpace(g2, area);
        Rectangle2D dataArea = space.shrink(area, null);
        getAxisOffset().trim(dataArea);

        state.setDataArea(dataArea);

        // if there is a renderer, it draws the background, otherwise use the
        // default background...
        if (getRenderer() != null) {
            getRenderer().drawBackground(g2, this, dataArea);
        }
        else {
            drawBackground(g2, dataArea);
        }

        Map axisStateMap = drawAxes(g2, area, dataArea, state);

        // the anchor point is typically the point where the mouse last
        // clicked - the crosshairs will be driven off this point...
        if (anchor != null && !dataArea.contains(anchor)) {
            anchor = ShapeUtilities.getPointInRectangle(anchor.getX(),
                    anchor.getY(), dataArea);
        }
        CategoryCrosshairState crosshairState = new CategoryCrosshairState();
        crosshairState.setCrosshairDistance(Double.POSITIVE_INFINITY);
        crosshairState.setAnchor(anchor);

        // specify the anchor X and Y coordinates in Java2D space, for the
        // cases where these are not updated during rendering (i.e. no lock
        // on data)
        crosshairState.setAnchorX(Double.NaN);
        crosshairState.setAnchorY(Double.NaN);
        if (anchor != null) {
            ValueAxis rangeAxis = getRangeAxis();
            if (rangeAxis != null) {
                double y;
                if (getOrientation() == PlotOrientation.VERTICAL) {
                    y = rangeAxis.java2DToValue(anchor.getY(), dataArea,
                            getRangeAxisEdge());
                }
                else {
                    y = rangeAxis.java2DToValue(anchor.getX(), dataArea,
                            getRangeAxisEdge());
                }
                crosshairState.setAnchorY(y);
            }
        }
        crosshairState.setRowKey(getDomainCrosshairRowKey());
        crosshairState.setColumnKey(getDomainCrosshairColumnKey());
        crosshairState.setCrosshairY(getRangeCrosshairValue());

        // don't let anyone draw outside the data area
        Shape savedClip = g2.getClip();
        g2.clip(dataArea);

        // draw the markers...
        int rendererCount = getRendererCount();
        for (int i = 0; i < rendererCount; i++) {
            drawDomainMarkers(g2, dataArea, i, Layer.BACKGROUND);
        }
        for (int i = 0; i < rendererCount; i++) {
            drawRangeMarkers(g2, dataArea, i, Layer.BACKGROUND);
        }

        if(!isGridLinesOverData()) {
            doGridLineDraw(g2, parentState, dataArea, axisStateMap);
            // redraw the axes so they appear over the gridlines.
            drawAxes(g2, area, dataArea, state);
        }

        // now render data items...
        boolean foundData = false;

        // set up the alpha-transparency...
        Composite originalComposite = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, getForegroundAlpha()));

        foundData = doRender(g2, state, dataArea, crosshairState, foundData, false);

        if(isGridLinesOverData()) {
            doGridLineDraw(g2, parentState, dataArea, axisStateMap);
            // redraw the axes so they appear over the gridlines.
            drawAxes(g2, area, dataArea, state);

            foundData = doRender(g2, state, dataArea, crosshairState, foundData, true);
        }

        // draw the foreground markers...
        for (int i = 0; i < rendererCount; i++) {
            drawDomainMarkers(g2, dataArea, i, Layer.FOREGROUND);
        }
        for (int i = 0; i < rendererCount; i++) {
            drawRangeMarkers(g2, dataArea, i, Layer.FOREGROUND);
        }

        // draw the annotations (if any)...
        drawAnnotations(g2, dataArea, null);

        g2.setClip(savedClip);
        g2.setComposite(originalComposite);

        if (!foundData) {
            drawNoDataMessage(g2, dataArea);
        }

        int datasetIndex = crosshairState.getDatasetIndex();
        setCrosshairDatasetIndex(datasetIndex, false);

        // draw domain crosshair if required...
        Comparable rowKey = crosshairState.getRowKey();
        Comparable columnKey = crosshairState.getColumnKey();
        setDomainCrosshairRowKey(rowKey, false);
        setDomainCrosshairColumnKey(columnKey, false);
        if (isDomainCrosshairVisible() && columnKey != null) {
            Paint paint = getDomainCrosshairPaint();
            Stroke stroke = getDomainCrosshairStroke();
            drawDomainCrosshair(g2, dataArea, getOrientation(),
                    datasetIndex, rowKey, columnKey, stroke, paint);
        }

        // draw range crosshair if required...
        ValueAxis yAxis = getRangeAxisForDataset(datasetIndex);
        RectangleEdge yAxisEdge = getRangeAxisEdge();
        if (!this.rangeCrosshairLockedOnData && anchor != null) {
            double yy;
            if (getOrientation() == PlotOrientation.VERTICAL) {
                yy = yAxis.java2DToValue(anchor.getY(), dataArea, yAxisEdge);
            }
            else {
                yy = yAxis.java2DToValue(anchor.getX(), dataArea, yAxisEdge);
            }
            crosshairState.setCrosshairY(yy);
        }
        setRangeCrosshairValue(crosshairState.getCrosshairY(), false);
        if (isRangeCrosshairVisible()) {
            double y = getRangeCrosshairValue();
            Paint paint = getRangeCrosshairPaint();
            Stroke stroke = getRangeCrosshairStroke();
            drawRangeCrosshair(g2, dataArea, getOrientation(), y, yAxis, stroke, paint);
        }

        // draw an outline around the plot area...
        if (isOutlineVisible()) {
            if (getRenderer() != null) {
                getRenderer().drawOutline(g2, this, dataArea);
            }
            else {
                drawOutline(g2, dataArea);
            }
        }

    }

    private boolean doRender(Graphics2D g2, PlotRenderingInfo state, Rectangle2D dataArea,
                             CategoryCrosshairState crosshairState, boolean foundData, boolean justLabel) {
        DatasetRenderingOrder order = getDatasetRenderingOrder();
        if (order == DatasetRenderingOrder.FORWARD) {
            for (int i = 0; i < getDatasetCount(); i++) {
                foundData = render(g2, dataArea, i, state, crosshairState, justLabel)
                    || foundData;
            }
        }
        else {  // DatasetRenderingOrder.REVERSE
            for (int i = getDatasetCount() - 1; i >= 0; i--) {
                foundData = render(g2, dataArea, i, state, crosshairState, justLabel)
                    || foundData;
            }
        }
        return foundData;
    }

    protected void doGridLineDraw(Graphics2D g2, PlotState parentState, Rectangle2D dataArea, Map axisStateMap) {
        drawDomainGridlines(g2, dataArea);

        AxisState rangeAxisState = (AxisState) axisStateMap.get(getRangeAxis());
        if (rangeAxisState == null) {
            if (parentState != null) {
                rangeAxisState = (AxisState) parentState.getSharedAxisStates()
                        .get(getRangeAxis());
            }
        }
        if (rangeAxisState != null) {
            drawRangeGridlines(g2, dataArea, rangeAxisState.getTicks());
            drawZeroRangeBaseline(g2, dataArea);
        }
    }

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
    public void drawBackground(Graphics2D g2, Rectangle2D area) {
        fillBackground(g2, area, getOrientation());
        drawBackgroundImage(g2, area);
    }

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
    public Map drawAxes(Graphics2D g2,
                           Rectangle2D plotArea,
                           Rectangle2D dataArea,
                           PlotRenderingInfo plotState) {

        AxisCollection axisCollection = new AxisCollection();

        // add domain axes to lists...
        for (int index = 0; index < getDomainAxisCount(); index++) {
            CategoryAxis xAxis = getDomainAxis(index);
            if (xAxis != null) {
                axisCollection.add(xAxis, getDomainAxisEdge(index));
            }
        }

        // add range axes to lists...
        for (int index = 0; index < getRangeAxisCount(); index++) {
            ValueAxis yAxis = getRangeAxis(index);
            if (yAxis != null) {
                axisCollection.add(yAxis, getRangeAxisEdge(index));
            }
        }

        Map axisStateMap = new HashMap();

        // draw the top axes
        RectangleInsets axisOffset = getAxisOffset();
        double cursor = dataArea.getMinY() - axisOffset.calculateTopOutset(
                dataArea.getHeight());
        Iterator iterator = axisCollection.getAxesAtTop().iterator();
        while (iterator.hasNext()) {
            Axis axis = (Axis) iterator.next();
            if (axis != null) {
                AxisState axisState = axis.draw(g2, cursor, plotArea, dataArea,
                        RectangleEdge.TOP, plotState);
                cursor = axisState.getCursor();
                axisStateMap.put(axis, axisState);
            }
        }

        // draw the bottom axes
        cursor = dataArea.getMaxY()
                 + axisOffset.calculateBottomOutset(dataArea.getHeight());
        iterator = axisCollection.getAxesAtBottom().iterator();
        while (iterator.hasNext()) {
            Axis axis = (Axis) iterator.next();
            if (axis != null) {
                AxisState axisState = axis.draw(g2, cursor, plotArea, dataArea,
                        RectangleEdge.BOTTOM, plotState);
                cursor = axisState.getCursor();
                axisStateMap.put(axis, axisState);
            }
        }

        // draw the left axes
        cursor = dataArea.getMinX()
                 - axisOffset.calculateLeftOutset(dataArea.getWidth());
        iterator = axisCollection.getAxesAtLeft().iterator();
        while (iterator.hasNext()) {
            Axis axis = (Axis) iterator.next();
            if (axis != null) {
                AxisState axisState = axis.draw(g2, cursor, plotArea, dataArea,
                        RectangleEdge.LEFT, plotState);
                cursor = axisState.getCursor();
                axisStateMap.put(axis, axisState);
            }
        }

        // draw the right axes
        cursor = dataArea.getMaxX()
                 + axisOffset.calculateRightOutset(dataArea.getWidth());
        iterator = axisCollection.getAxesAtRight().iterator();
        while (iterator.hasNext()) {
            Axis axis = (Axis) iterator.next();
            if (axis != null) {
                AxisState axisState = axis.draw(g2, cursor, plotArea, dataArea,
                        RectangleEdge.RIGHT, plotState);
                cursor = axisState.getCursor();
                axisStateMap.put(axis, axisState);
            }
        }

        return axisStateMap;

    }

    /**
     * Draws a representation of a dataset within the dataArea region using the
     * appropriate renderer.
     *
     * @param g2  the graphics device.
     * @param dataArea  the region in which the data is to be drawn.
     * @param index  the dataset and renderer index.
     * @param info  an optional object for collection dimension information.
     * @param crosshairState  a state object for tracking crosshair info
     *        (<code>null</code> permitted).
     * @param justLabel Controls whether the method draws just the labels or everything including the labels.
     *
     * @return A boolean that indicates whether or not real data was found.
     *
     * @since 1.0.11
     */
    public boolean render(Graphics2D g2, Rectangle2D dataArea, int index,
            PlotRenderingInfo info, CategoryCrosshairState crosshairState,
            boolean justLabel) {

        boolean foundData = false;
        CategoryDataset currentDataset = getDataset(index);
        CategoryItemRenderer renderer = getRenderer(index);
        LabelRenderer labelRenderer = renderer instanceof LabelRenderer ? (LabelRenderer) renderer : null;
        CategoryAxis domainAxis = getDomainAxisForDataset(index);
        ValueAxis rangeAxis = getRangeAxisForDataset(index);
        boolean hasData = !DatasetUtilities.isEmptyOrNull(currentDataset);
        if (hasData && renderer != null) {

            foundData = true;
            CategoryItemRendererState state = renderer.initialise(g2, dataArea,
                    this, index, info);
            state.setCrosshairState(crosshairState);
            int columnCount = currentDataset.getColumnCount();
            int rowCount = currentDataset.getRowCount();
            int passCount = renderer.getPassCount();
            for (int pass = 0; pass < passCount; pass++) {
                if (this.columnRenderingOrder == SortOrder.ASCENDING) {
                    for (int column = 0; column < columnCount; column++) {
                        if (this.rowRenderingOrder == SortOrder.ASCENDING) {
                            for (int row = 0; row < rowCount; row++) {
                                if(labelRenderer == null)
                                    renderer.drawItem(g2, state, dataArea, this,
                                            domainAxis, rangeAxis, currentDataset,
                                            row, column, pass);
                                else
                                    labelRenderer.drawItem(g2, state, dataArea, this,
                                            domainAxis, rangeAxis, currentDataset,
                                            row, column, pass, justLabel);
                            }
                        }
                        else {
                            for (int row = rowCount - 1; row >= 0; row--) {
                                if(labelRenderer == null)
                                    renderer.drawItem(g2, state, dataArea, this,
                                            domainAxis, rangeAxis, currentDataset,
                                            row, column, pass);
                                else
                                    labelRenderer.drawItem(g2, state, dataArea, this,
                                            domainAxis, rangeAxis, currentDataset,
                                            row, column, pass, justLabel);
                            }
                        }
                    }
                }
                else {
                    for (int column = columnCount - 1; column >= 0; column--) {
                        if (this.rowRenderingOrder == SortOrder.ASCENDING) {
                            for (int row = 0; row < rowCount; row++) {
                                if(labelRenderer == null)
                                    renderer.drawItem(g2, state, dataArea, this,
                                            domainAxis, rangeAxis, currentDataset,
                                            row, column, pass);
                                else
                                    labelRenderer.drawItem(g2, state, dataArea, this,
                                            domainAxis, rangeAxis, currentDataset,
                                            row, column, pass, justLabel);
                            }
                        }
                        else {
                            for (int row = rowCount - 1; row >= 0; row--) {
                                if(labelRenderer == null)
                                    renderer.drawItem(g2, state, dataArea, this,
                                            domainAxis, rangeAxis, currentDataset,
                                            row, column, pass);
                                else
                                    labelRenderer.drawItem(g2, state, dataArea, this,
                                            domainAxis, rangeAxis, currentDataset,
                                            row, column, pass, justLabel);
                            }
                        }
                    }
                }
            }
        }
        return foundData;

    }

    /**
     * Draws the domain gridlines for the plot, if they are visible.
     *
     * @param g2  the graphics device.
     * @param dataArea  the area inside the axes.
     *
     * @see #drawRangeGridlines(Graphics2D, Rectangle2D, List)
     */
    protected void drawDomainGridlines(Graphics2D g2, Rectangle2D dataArea) {

        if (!isDomainGridlinesVisible()) {
            return;
        }
        CategoryAnchor anchor = getDomainGridlinePosition();
        RectangleEdge domainAxisEdge = getDomainAxisEdge();
        CategoryDataset dataset = getDataset();
        if (dataset == null) {
            return;
        }
        CategoryAxis axis = getDomainAxis();
        if (axis != null) {
            int columnCount = dataset.getColumnCount();
            for (int c = 0; c < columnCount; c++) {
                double xx = axis.getCategoryJava2DCoordinate(anchor, c,
                        columnCount, dataArea, domainAxisEdge);
                CategoryItemRenderer renderer1 = getRenderer();
                if (renderer1 != null) {
                    renderer1.drawDomainGridline(g2, this, dataArea, xx);
                }
            }
        }
    }

    /**
     * Draws the range gridlines for the plot, if they are visible.
     *
     * @param g2  the graphics device.
     * @param dataArea  the area inside the axes.
     * @param ticks  the ticks.
     *
     * @see #drawDomainGridlines(Graphics2D, Rectangle2D)
     */
    protected void drawRangeGridlines(Graphics2D g2, Rectangle2D dataArea,
                                      List ticks) {
        // draw the range grid lines, if any...
        if (!isRangeGridlinesVisible()) {
            return;
        }
        ValueAxis axis = getRangeAxis();
        if (axis == null) {
            return;
        }
        Iterator iterator = ticks.iterator();
        while (iterator.hasNext()) {
            ValueTick tick = (ValueTick) iterator.next();
            CategoryItemRenderer renderer1 = getRenderer();
            if (renderer1 != null) {
                renderer1.drawRangeGridline(g2, this, axis, dataArea,
                        tick.getValue());
            }
        }
    }

    /**
     * Draws the annotations for the plot.
     *
     * @param g2  the graphics device.
     * @param dataArea  the data area.
     * @param info  the chart rendering info. Ignored in this implementation.
     */
    public void drawAnnotations(Graphics2D g2, Rectangle2D dataArea, PlotRenderingInfo info) {

        if (getAnnotations() != null) {
            Iterator iterator = getAnnotations().iterator();
            while (iterator.hasNext()) {
                CategoryAnnotation annotation
                        = (CategoryAnnotation) iterator.next();
                annotation.draw(g2, this, dataArea, getDomainAxis(),
                        getRangeAxis());
            }
        }

    }

    /**
     * Draws the domain markers (if any) for an axis and layer.  This method is
     * typically called from within the draw() method.
     *
     * @param g2  the graphics device.
     * @param dataArea  the data area.
     * @param index  the renderer index.
     * @param layer  the layer (foreground or background).
     *
     * @see #drawRangeMarkers(Graphics2D, Rectangle2D, int, Layer)
     */
    public void drawDomainMarkers(Graphics2D g2, Rectangle2D dataArea,
                                     int index, Layer layer) {

        CategoryItemRenderer r = getRenderer(index);
        if (r == null) {
            return;
        }

        Collection markers = getDomainMarkers(index, layer);
        CategoryAxis axis = getDomainAxisForDataset(index);
        if (markers != null && axis != null) {
            Iterator iterator = markers.iterator();
            while (iterator.hasNext()) {
                CategoryMarker marker = (CategoryMarker) iterator.next();
                r.drawDomainMarker(g2, this, axis, marker, dataArea);
            }
        }

    }

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
    public void drawRangeMarkers(Graphics2D g2, Rectangle2D dataArea,
                                    int index, Layer layer) {

        CategoryItemRenderer r = getRenderer(index);
        if (r == null) {
            return;
        }

        Collection markers = getRangeMarkers(index, layer);
        ValueAxis axis = getRangeAxisForDataset(index);
        if (markers != null && axis != null) {
            Iterator iterator = markers.iterator();
            while (iterator.hasNext()) {
                Marker marker = (Marker) iterator.next();
                r.drawRangeMarker(g2, this, axis, marker, dataArea);
            }
        }

    }

    /**
     * Utility method for drawing a line perpendicular to the range axis (used
     * for crosshairs).
     *
     * @param g2  the graphics device.
     * @param dataArea  the area defined by the axes.
     * @param value  the data value.
     * @param stroke  the line stroke (<code>null</code> not permitted).
     * @param paint  the line paint (<code>null</code> not permitted).
     */
    protected void drawRangeLine(Graphics2D g2, Rectangle2D dataArea,
            double value, Stroke stroke, Paint paint) {

        double java2D = getRangeAxis().valueToJava2D(value, dataArea,
                getRangeAxisEdge());
        Line2D line = null;
        PlotOrientation orientation = getOrientation();
        if (orientation == PlotOrientation.HORIZONTAL) {
            line = new Line2D.Double(java2D, dataArea.getMinY(), java2D,
                    dataArea.getMaxY());
        }
        else if (orientation == PlotOrientation.VERTICAL) {
            line = new Line2D.Double(dataArea.getMinX(), java2D,
                    dataArea.getMaxX(), java2D);
        }
        g2.setStroke(stroke);
        g2.setPaint(paint);
        g2.draw(line);

    }

    /**
     * Draws a base line across the chart at value zero on the range axis.
     *
     * @param g2  the graphics device.
     * @param area  the data area.
     *
     * @see #setRangeZeroBaselineVisible(boolean)
     */
    protected void drawZeroRangeBaseline(Graphics2D g2, Rectangle2D area) {
        if (isRangeZeroBaselineVisible()) {
            drawRangeLine(g2, area, 0.0, getRangeZeroBaselineStroke(), getRangeZeroBaselinePaint());
        }
    }

    /**
     * Draws a domain crosshair.
     *
     * @param g2  the graphics target.
     * @param dataArea  the data area.
     * @param orientation  the plot orientation.
     * @param datasetIndex  the dataset index.
     * @param rowKey  the row key.
     * @param columnKey  the column key.
     * @param stroke  the stroke used to draw the crosshair line.
     * @param paint  the paint used to draw the crosshair line.
     *
     * @see #drawRangeCrosshair(Graphics2D, Rectangle2D, PlotOrientation,
     *     double, ValueAxis, Stroke, Paint)
     *
     * @since 1.0.11
     */
    protected void drawDomainCrosshair(Graphics2D g2, Rectangle2D dataArea,
            PlotOrientation orientation, int datasetIndex,
            Comparable rowKey, Comparable columnKey, Stroke stroke,
            Paint paint) {

        CategoryDataset dataset = getDataset(datasetIndex);
        CategoryAxis axis = getDomainAxisForDataset(datasetIndex);
        CategoryItemRenderer renderer = getRenderer(datasetIndex);
        Line2D line;
        if (orientation == PlotOrientation.VERTICAL) {
            double xx = renderer.getItemMiddle(rowKey, columnKey, dataset, axis,
                    dataArea, RectangleEdge.BOTTOM);
            line = new Line2D.Double(xx, dataArea.getMinY(), xx,
                    dataArea.getMaxY());
        }
        else {
            double yy = renderer.getItemMiddle(rowKey, columnKey, dataset, axis,
                    dataArea, RectangleEdge.LEFT);
            line = new Line2D.Double(dataArea.getMinX(), yy,
                    dataArea.getMaxX(), yy);
        }
        g2.setStroke(stroke);
        g2.setPaint(paint);
        g2.draw(line);

    }

    /**
     * Draws a range crosshair.
     *
     * @param g2  the graphics target.
     * @param dataArea  the data area.
     * @param orientation  the plot orientation.
     * @param value  the crosshair value.
     * @param axis  the axis against which the value is measured.
     * @param stroke  the stroke used to draw the crosshair line.
     * @param paint  the paint used to draw the crosshair line.
     *
     * @see #drawDomainCrosshair(Graphics2D, Rectangle2D, PlotOrientation, int,
     *      Comparable, Comparable, Stroke, Paint)
     *
     * @since 1.0.5
     */
    protected void drawRangeCrosshair(Graphics2D g2, Rectangle2D dataArea,
            PlotOrientation orientation, double value, ValueAxis axis,
            Stroke stroke, Paint paint) {

        if (!axis.getRange().contains(value)) {
            return;
        }
        Line2D line;
        if (orientation == PlotOrientation.HORIZONTAL) {
            double xx = axis.valueToJava2D(value, dataArea,
                    RectangleEdge.BOTTOM);
            line = new Line2D.Double(xx, dataArea.getMinY(), xx,
                    dataArea.getMaxY());
        }
        else {
            double yy = axis.valueToJava2D(value, dataArea,
                    RectangleEdge.LEFT);
            line = new Line2D.Double(dataArea.getMinX(), yy,
                    dataArea.getMaxX(), yy);
        }
        g2.setStroke(stroke);
        g2.setPaint(paint);
        g2.draw(line);

    }

    /**
     * Returns the range of data values that will be plotted against the range
     * axis.  If the dataset is <code>null</code>, this method returns
     * <code>null</code>.
     *
     * @param axis  the axis.
     *
     * @return The data range.
     */
    public Range getDataRange(ValueAxis axis) {

        Range result = null;
        List mappedDatasets = new ArrayList();

        int rangeIndex = getRangeAxisIndex(axis);
        if (rangeIndex >= 0) {
            mappedDatasets.addAll(getDatasetsMappedToRangeAxis(rangeIndex));
        }
        else if (axis == getRangeAxis()) {
            mappedDatasets.addAll(getDatasetsMappedToRangeAxis(0));
        }

        // iterate through the datasets that map to the axis and get the union
        // of the ranges.
        Iterator iterator = mappedDatasets.iterator();
        while (iterator.hasNext()) {
            CategoryDataset d = (CategoryDataset) iterator.next();
            CategoryItemRenderer r = getRendererForDataset(d);
            if (r != null) {
                result = Range.combine(result, r.findRangeBounds(d));
            }
        }
        return result;

    }


    /**
     * Returns the weight for this plot when it is used as a subplot within a
     * combined plot.
     *
     * @return The weight.
     *
     * @see #setWeight(int)
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Sets the weight for the plot and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param weight  the weight.
     *
     * @see #getWeight()
     */
    public void setWeight(int weight) {
        this.weight = weight;
        fireChangeEvent();
    }

    /**
     * Returns the fixed domain axis space.
     *
     * @return The fixed domain axis space (possibly <code>null</code>).
     *
     * @see #setFixedDomainAxisSpace(AxisSpace)
     */
    public AxisSpace getFixedDomainAxisSpace() {
        return this.fixedDomainAxisSpace;
    }

    /**
     * Sets the fixed domain axis space and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     *
     * @param space  the space (<code>null</code> permitted).
     *
     * @see #getFixedDomainAxisSpace()
     */
    public void setFixedDomainAxisSpace(AxisSpace space) {
        setFixedDomainAxisSpace(space, true);
    }

    /**
     * Sets the fixed domain axis space and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     *
     * @param space  the space (<code>null</code> permitted).
     * @param notify  notify listeners?
     *
     * @see #getFixedDomainAxisSpace()
     *
     * @since 1.0.7
     */
    public void setFixedDomainAxisSpace(AxisSpace space, boolean notify) {
        this.fixedDomainAxisSpace = space;
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns the fixed range axis space.
     *
     * @return The fixed range axis space (possibly <code>null</code>).
     *
     * @see #setFixedRangeAxisSpace(AxisSpace)
     */
    public AxisSpace getFixedRangeAxisSpace() {
        return this.fixedRangeAxisSpace;
    }

    /**
     * Sets the fixed range axis space and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     *
     * @param space  the space (<code>null</code> permitted).
     *
     * @see #getFixedRangeAxisSpace()
     */
    public void setFixedRangeAxisSpace(AxisSpace space) {
        setFixedRangeAxisSpace(space, true);
    }

    /**
     * Sets the fixed range axis space and sends a {@link PlotChangeEvent} to
     * all registered listeners.
     *
     * @param space  the space (<code>null</code> permitted).
     * @param notify  notify listeners?
     *
     * @see #getFixedRangeAxisSpace()
     *
     * @since 1.0.7
     */
    public void setFixedRangeAxisSpace(AxisSpace space, boolean notify) {
        this.fixedRangeAxisSpace = space;
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Returns a list of the categories in the plot's primary dataset.
     *
     * @return A list of the categories in the plot's primary dataset.
     *
     * @see #getCategoriesForAxis(CategoryAxis)
     */
    public List getCategories() {
        List result = null;
        if (getDataset() != null) {
            result = Collections.unmodifiableList(getDataset().getColumnKeys());
        }
        return result;
    }

    /**
     * Returns a list of the categories that should be displayed for the
     * specified axis.
     *
     * @param axis  the axis (<code>null</code> not permitted)
     *
     * @return The categories.
     *
     * @since 1.0.3
     */
    public List getCategoriesForAxis(CategoryAxis axis) {
        List result = new ArrayList();
        int axisIndex = getDomainAxisIndex(axis);
        List datasets = getDatasetsMappedToDomainAxis(axisIndex);
        Iterator iterator = datasets.iterator();
        while (iterator.hasNext()) {
            CategoryDataset dataset = (CategoryDataset) iterator.next();
            // add the unique categories from this dataset
            for (int i = 0; i < dataset.getColumnCount(); i++) {
                Comparable category = dataset.getColumnKey(i);
                if (!result.contains(category)) {
                    result.add(category);
                }
            }
        }
        return result;
    }

    /**
     * Returns the flag that controls whether or not the shared domain axis is
     * drawn for each subplot.
     *
     * @return A boolean.
     *
     * @see #setDrawSharedDomainAxis(boolean)
     */
    public boolean getDrawSharedDomainAxis() {
        return this.drawSharedDomainAxis;
    }

    /**
     * Sets the flag that controls whether the shared domain axis is drawn when
     * this plot is being used as a subplot.
     *
     * @param draw  a boolean.
     *
     * @see #getDrawSharedDomainAxis()
     */
    public void setDrawSharedDomainAxis(boolean draw) {
        this.drawSharedDomainAxis = draw;
        fireChangeEvent();
    }

    /**
     * Returns <code>false</code> to indicate that the domain axes are not
     * zoomable.
     *
     * @return A boolean.
     *
     * @see #isRangeZoomable()
     */
    public boolean isDomainZoomable() {
        return false;
    }

    /**
     * Returns <code>true</code> to indicate that the range axes are zoomable.
     *
     * @return A boolean.
     *
     * @see #isDomainZoomable()
     */
    public boolean isRangeZoomable() {
        return true;
    }

    /**
     * This method does nothing, because <code>CategoryPlot</code> doesn't
     * support zooming on the domain.
     *
     * @param factor  the zoom factor.
     * @param state  the plot state.
     * @param source  the source point (in Java2D space) for the zoom.
     */
    public void zoomDomainAxes(double factor, PlotRenderingInfo state,
                               Point2D source) {
        // can't zoom domain axis
    }

    /**
     * This method does nothing, because <code>CategoryPlot</code> doesn't
     * support zooming on the domain.
     *
     * @param lowerPercent  the lower bound.
     * @param upperPercent  the upper bound.
     * @param state  the plot state.
     * @param source  the source point (in Java2D space) for the zoom.
     */
    public void zoomDomainAxes(double lowerPercent, double upperPercent,
                               PlotRenderingInfo state, Point2D source) {
        // can't zoom domain axis
    }

    /**
     * This method does nothing, because <code>CategoryPlot</code> doesn't
     * support zooming on the domain.
     *
     * @param factor  the zoom factor.
     * @param info  the plot rendering info.
     * @param source  the source point (in Java2D space).
     * @param useAnchor  use source point as zoom anchor?
     *
     * @see #zoomRangeAxes(double, PlotRenderingInfo, Point2D, boolean)
     *
     * @since 1.0.7
     */
    public void zoomDomainAxes(double factor, PlotRenderingInfo info,
                               Point2D source, boolean useAnchor) {
        // can't zoom domain axis
    }

    /**
     * Multiplies the range on the range axis/axes by the specified factor.
     *
     * @param factor  the zoom factor.
     * @param state  the plot state.
     * @param source  the source point (in Java2D space) for the zoom.
     */
    public void zoomRangeAxes(double factor, PlotRenderingInfo state,
                              Point2D source) {
        // delegate to other method
        zoomRangeAxes(factor, state, source, false);
    }

    /**
     * Multiplies the range on the range axis/axes by the specified factor.
     *
     * @param factor  the zoom factor.
     * @param info  the plot rendering info.
     * @param source  the source point.
     * @param useAnchor  a flag that controls whether or not the source point
     *         is used for the zoom anchor.
     *
     * @see #zoomDomainAxes(double, PlotRenderingInfo, Point2D, boolean)
     *
     * @since 1.0.7
     */
    public void zoomRangeAxes(double factor, PlotRenderingInfo info,
                              Point2D source, boolean useAnchor) {

        // perform the zoom on each range axis
        for (int i = 0; i < getRangeAxisCount(); i++) {
            ValueAxis rangeAxis = getRangeAxis(i);
            if (rangeAxis != null) {
                if (useAnchor) {
                    // get the relevant source coordinate given the plot
                    // orientation
                    double sourceY = source.getY();
                    if (getOrientation() == PlotOrientation.HORIZONTAL) {
                        sourceY = source.getX();
                    }
                    double anchorY = rangeAxis.java2DToValue(sourceY,
                            info.getDataArea(), getRangeAxisEdge());
                    rangeAxis.resizeRange(factor, anchorY);
                }
                else {
                    rangeAxis.resizeRange(factor);
                }
            }
        }
    }

    /**
     * Zooms in on the range axes.
     *
     * @param lowerPercent  the lower bound.
     * @param upperPercent  the upper bound.
     * @param state  the plot state.
     * @param source  the source point (in Java2D space) for the zoom.
     */
    public void zoomRangeAxes(double lowerPercent, double upperPercent,
                              PlotRenderingInfo state, Point2D source) {
        for (int i = 0; i < getRangeAxisCount(); i++) {
            ValueAxis rangeAxis = getRangeAxis(i);
            if (rangeAxis != null) {
                rangeAxis.zoomRange(lowerPercent, upperPercent);
            }
        }
    }

    /**
     * Returns the anchor value.
     *
     * @return The anchor value.
     *
     * @see #setAnchorValue(double)
     */
    public double getAnchorValue() {
        return this.anchorValue;
    }

    /**
     * Sets the anchor value and sends a {@link PlotChangeEvent} to all
     * registered listeners.
     *
     * @param value  the anchor value.
     *
     * @see #getAnchorValue()
     */
    public void setAnchorValue(double value) {
        setAnchorValue(value, true);
    }

    /**
     * Sets the anchor value and, if requested, sends a {@link PlotChangeEvent}
     * to all registered listeners.
     *
     * @param value  the value.
     * @param notify  notify listeners?
     *
     * @see #getAnchorValue()
     */
    public void setAnchorValue(double value, boolean notify) {
        this.anchorValue = value;
        if (notify) {
            fireChangeEvent();
        }
    }

    /**
     * Tests the plot for equality with an arbitrary object.
     *
     * @param obj  the object to test against (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof CategoryPlot)) {
            return false;
        }
        CategoryPlot that = (CategoryPlot) obj;
        if (this.drawSharedDomainAxis != that.drawSharedDomainAxis) {
            return false;
        }
        if (this.columnRenderingOrder != that.columnRenderingOrder) {
            return false;
        }
        if (this.rowRenderingOrder != that.rowRenderingOrder) {
            return false;
        }
        if (this.domainGridlinePosition != that.domainGridlinePosition) {
            return false;
        }
        if (this.anchorValue != that.anchorValue) {
            return false;
        }
        if (this.rangeCrosshairValue != that.rangeCrosshairValue) {
            return false;
        }
        if (this.rangeCrosshairLockedOnData
                != that.rangeCrosshairLockedOnData) {
            return false;
        }
        if (!ObjectUtilities.equal(this.foregroundDomainMarkers,
                that.foregroundDomainMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.backgroundDomainMarkers,
                that.backgroundDomainMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.foregroundRangeMarkers,
                that.foregroundRangeMarkers)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.backgroundRangeMarkers,
                that.backgroundRangeMarkers)) {
            return false;
        }
        if (this.weight != that.weight) {
            return false;
        }
        if (!ObjectUtilities.equal(this.fixedDomainAxisSpace,
                that.fixedDomainAxisSpace)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.fixedRangeAxisSpace,
                that.fixedRangeAxisSpace)) {
            return false;
        }
        if (this.crosshairDatasetIndex != that.crosshairDatasetIndex) {
            return false;
        }
        if (!ObjectUtilities.equal(this.domainCrosshairColumnKey,
                that.domainCrosshairColumnKey)) {
            return false;
        }
        if (!ObjectUtilities.equal(this.domainCrosshairRowKey,
                that.domainCrosshairRowKey)) {
            return false;
        }

        return super.equals(obj);

    }

    /**
     * Returns a clone of the plot.
     *
     * @return A clone.
     *
     * @throws CloneNotSupportedException  if the cloning is not supported.
     */
    public Object clone() throws CloneNotSupportedException {

        CategoryPlot clone = (CategoryPlot) super.clone();
        if (this.fixedDomainAxisSpace != null) {
            clone.fixedDomainAxisSpace = (AxisSpace) ObjectUtilities.clone(
                    this.fixedDomainAxisSpace);
        }
        if (this.fixedRangeAxisSpace != null) {
            clone.fixedRangeAxisSpace = (AxisSpace) ObjectUtilities.clone(
                    this.fixedRangeAxisSpace);
        }

        clone.foregroundDomainMarkers = cloneMarkerMap(
                this.foregroundDomainMarkers);
        clone.backgroundDomainMarkers = cloneMarkerMap(
                this.backgroundDomainMarkers);
        clone.foregroundRangeMarkers = cloneMarkerMap(
                this.foregroundRangeMarkers);
        clone.backgroundRangeMarkers = cloneMarkerMap(
                this.backgroundRangeMarkers);
        return clone;

    }

    /**
     * A utility method to clone the marker maps.
     *
     * @param map  the map to clone.
     *
     * @return A clone of the map.
     *
     * @throws CloneNotSupportedException if there is some problem cloning the
     *                                    map.
     */
    private Map cloneMarkerMap(Map map) throws CloneNotSupportedException {
        Map clone = new HashMap();
        Set keys = map.keySet();
        Iterator iterator = keys.iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            List entry = (List) map.get(key);
            Object toAdd = ObjectUtilities.deepClone(entry);
            clone.put(key, toAdd);
        }
        return clone;
    }

}
