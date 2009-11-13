package org.jfree.chart.editor.themes;

import org.jfree.chart.block.BlockBorder;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.VerticalAlignment;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 29-Apr-2009
 * Time: 14:55:43
 * Information that a ChartEditor GUI can change about a title object within a chart.
 *
 * Serves as the basis for more specific title themes - such as the main title theme and the
 * legend theme.
 *
 */
public interface TitleTheme extends ChartThemeBasis {
    BlockBorder getTitleFrame();

    void setTitleFrame(BlockBorder titleFrame);

    RectangleInsets getTitlePadding();

    void setTitlePadding(RectangleInsets titlePadding);

    RectangleInsets getTitleMargin();

    void setTitleMargin(RectangleInsets titleMargin);

    RectangleEdge getPosition();

    void setPosition(RectangleEdge position);

    HorizontalAlignment getHorizontalAlignment();

    void setHorizontalAlignment(HorizontalAlignment horizontalAlignment);

    VerticalAlignment getVerticalAlignment();

    void setVerticalAlignment(VerticalAlignment verticalAlignment);
}
