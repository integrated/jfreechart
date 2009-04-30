package org.jfree.chart.editor.themes;

import org.jfree.ui.HorizontalAlignment;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 29-Apr-2009
 * Time: 14:58:28
 * To change this template use File | Settings | File Templates.
 */
public interface iPlusChartTitleTheme extends iPlusTitleTheme {
    Font getTitleFont();

    void setTitleFont(Font font);

    Paint getTitlePaint();

    void setTitlePaint(Paint paint);

    String getChartTitle();

    void setChartTitle(String chartTitle);

    Paint getTitleBackground();

    void setTitleBackground(Paint titleBackground);

    boolean isTitleVisible();

    void setTitleVisible(boolean titleVisible);

    boolean isExpandTitle();

    void setExpandTitle(boolean expandTitle);

    HorizontalAlignment getTextAlignment();

    void setTextAlignment(HorizontalAlignment textAlignment);
}
