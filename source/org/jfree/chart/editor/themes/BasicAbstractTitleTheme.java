package org.jfree.chart.editor.themes;

import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.Title;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 13-Nov-2009
 * Time: 11:36:39
 * Implementation of the iPlusTitleTheme interface. Designed to be extended by legend and chart title themes.
 */
public abstract class BasicAbstractTitleTheme extends BasicAbstractChartTheme implements TitleTheme {

    public static final BlockBorder DEFAULT_TITLE_BORDER = BlockBorder.NONE;
    public static final RectangleInsets DEFAULT_PADDING = RectangleInsets.ZERO_INSETS;
    public static final RectangleInsets DEFAULT_MARGIN = RectangleInsets.ZERO_INSETS;
    public static final RectangleEdge DEFAULT_POSITION = RectangleEdge.TOP;
    public static final HorizontalAlignment DEFAULT_HORIZONTAL_ALIGNMENT = HorizontalAlignment.CENTER;
    public static final VerticalAlignment DEFAULT_VERTICAL_ALIGNMENT = VerticalAlignment.CENTER;

    /** The coloured border around the title */
    private BlockBorder titleFrame;

    /** The padding around the title */
    private RectangleInsets titlePadding;

    /** The margin inside the title */
    private RectangleInsets titleMargin;

    /** The edge of the chart that will hold this title */
    private RectangleEdge position;

    /* How the title's frame will be aligned along horizontal edges */
    private HorizontalAlignment horizontalAlignment;

    /* How the title's frame will be aligned along vertical edges */
    private VerticalAlignment verticalAlignment;

    public BasicAbstractTitleTheme(String name, String defaultName) {
        super(name, defaultName);
    }

    public BasicAbstractTitleTheme(JFreeChart chart, String name, String defaultName) {
        super(chart, name, defaultName);
    }

    protected void initDefaults() {
        this.titleFrame = DEFAULT_TITLE_BORDER;
        this.titlePadding = DEFAULT_PADDING;
        this.titleMargin = DEFAULT_MARGIN;
        this.position = DEFAULT_POSITION;
        this.horizontalAlignment = DEFAULT_HORIZONTAL_ALIGNMENT;
        this.verticalAlignment = DEFAULT_VERTICAL_ALIGNMENT;
    }

    public void readSettingsFromChart(JFreeChart chart) {
        Title title = getTitleObject(chart);
        if(title != null) {
            this.titleFrame = (BlockBorder) title.getFrame();
            this.titlePadding = title.getPadding();
            this.titleMargin = title.getMargin();
            this.position = title.getPosition();
            this.horizontalAlignment = title.getHorizontalAlignment();
            this.verticalAlignment = title.getVerticalAlignment();
        }
    }

    protected abstract Title getTitleObject(JFreeChart chart);

    public void apply(JFreeChart chart) {
        Title title = getTitleObject(chart);
        if(title != null) {
            title.setFrame(titleFrame);
            title.setPadding(titlePadding);
            title.setMargin(titleMargin);
            title.setPosition(position);
            title.setHorizontalAlignment(horizontalAlignment);
            title.setVerticalAlignment(verticalAlignment);
        }
    }



    public BlockBorder getTitleFrame() {
        return titleFrame;
    }

    public void setTitleFrame(BlockBorder titleFrame) {
        this.titleFrame = titleFrame;
    }

    public RectangleInsets getTitlePadding() {
        return titlePadding;
    }

    public void setTitlePadding(RectangleInsets titlePadding) {
        this.titlePadding = titlePadding;
    }

    public RectangleInsets getTitleMargin() {
        return titleMargin;
    }

    public void setTitleMargin(RectangleInsets titleMargin) {
        this.titleMargin = titleMargin;
    }

    public RectangleEdge getPosition() {
        return position;
    }

    public void setPosition(RectangleEdge position) {
        this.position = position;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }
}
