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
 * -----------------------
 * DefaultChartEditor.java
 * -----------------------
 * (C) Copyright 2000-2008, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Arnaud Lelievre;
 *                   Daniel Gredler;
 *
 * Changes
 * -------
 * 24-Nov-2005 : New class, based on ChartPropertyEditPanel.java (DG);
 * 18-Dec-2008 : Use ResourceBundleWrapper - see patch 1607918 by
 *               Jess Thrysoee (DG);
 *
 */

package org.jfree.chart.editor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.imageio.ImageIO;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.editor.components.InsetPanel;
import org.jfree.chart.editor.components.BorderPanel;
import org.jfree.chart.editor.components.PaintControl;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.title.Title;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.ui.PaintSample;
import org.jfree.ui.Align;

/**
 * A panel for editing chart properties (includes subpanels for the title,
 * legend and plot).
 */
public class DefaultChartEditor extends BaseEditor implements ActionListener, ChartEditor {
    /** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
                    "org.jfree.chart.editor.LocalizationBundle");

    /** Options for image alignment */
    private static final String[] ALIGNMENT_TEXT = new String[] {
            localizationResources.getString("North"),
            localizationResources.getString("North-East"),
            localizationResources.getString("East"),
            localizationResources.getString("South-East"),
            localizationResources.getString("South"),
            localizationResources.getString("South-West"),
            localizationResources.getString("West"),
            localizationResources.getString("North-West"),
            localizationResources.getString("Centre"),
            localizationResources.getString("Stretch"),
            localizationResources.getString("Stretch_Horizontal"),
            localizationResources.getString("Stretch_Vertical")
    };

    /** Action Commands */
    private static final String BACKGROUND_PAINT = "BackgroundPaint";
    private static final String IMAGE_FILE_CHOOSE = "ImageFileChoose";

    /** Align class constants in the same order as the ALIGNMENT_TEXT array */
    private static final int[] ALIGNMENT_VALS = new int[] {
            Align.NORTH,
            Align.NORTH_EAST,
            Align.EAST,
            Align.SOUTH_EAST,
            Align.SOUTH,
            Align.SOUTH_WEST,
            Align.WEST,
            Align.NORTH_WEST,
            Align.CENTER,
            Align.FIT,
            Align.FIT_HORIZONTAL,
            Align.FIT_VERTICAL
    };


    /** A panel for displaying/editing the properties of the title. */
    private DefaultTitleEditor titleEditor;

    /** A panel for displaying/editing the properties of the plot. */
    private DefaultPlotEditor plotEditor;

    /**
     * A checkbox indicating whether or not the chart is drawn with
     * anti-aliasing.
     */
    private JCheckBox antialias;

    /**
     * A checkbox indicating whether or not the chart's text is drawn with
     * anti-aliasing.
     */
    private JCheckBox textAntialias;

    /** The chart background color. */
    private PaintControl backgroundPaint;

    /** The label displaying the location of the image file */
    private JLabel imageLabel;

    /** Image for the chart's background */
    private Image backImage, backImageIcon;

    /** Combo-box to determine how background image is aligned */
    private JComboBox imageAlign;

    /** Control to change alpha-value for background image of chart */
    private JSlider imageAlpha;

    /** Panel to edit border properties */
    private BorderPanel borderPanel;

    /** Component to edit the chart padding values */
    private InsetPanel chartPadding;

    /** The icon size used for the background image icon */
    private final static int ICON_HEIGHT = 50;
    /** The icon size used for the background image icon */
    private final static int ICON_WIDTH = 50;
    /** Whether a background image is in use for this chart */
    private boolean backImageSelected;


    /**
     * Standard constructor - the property panel is made up of a number of
     * sub-panels that are displayed in the tabbed pane.
     *
     * @param chart  the chart, whichs properties should be changed.
     */
    public DefaultChartEditor(JFreeChart chart) {
        this(chart, false);
    }

    /**
     * Standard constructor - the property panel is made up of a number of
     * sub-panels that are displayed in the tabbed pane.
     *
     * @param chart  the chart, whichs properties should be changed.
     * @param immediateUpdate If true, changes are applied to the chart as they are made without waiting for the OK button.
     */
    public DefaultChartEditor(JFreeChart chart, boolean immediateUpdate) {
        super(chart, immediateUpdate);
        setLayout(new BorderLayout());

        // background tab
        JPanel background = new JPanel(new BorderLayout());
        background.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        JPanel backgroundInterior = new JPanel(new BorderLayout());
        backgroundInterior.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            localizationResources.getString("General")));
        JPanel interior = new JPanel(new GridBagLayout());
        GridBagConstraints c = getNewConstraints();
        interior.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        interior.add(new JLabel(localizationResources.getString(
                "Background_paint")),c);
        c.gridx++; c.weightx = 1.0; c.gridwidth = 2;
        this.backgroundPaint = new PaintControl(chart.getBackgroundPaint(), true);
        this.backgroundPaint.addChangeListener(updateHandler);
        interior.add(this.backgroundPaint,c);

        startNewRow(c);
        backImage = chart.getBackgroundImage();
        backImageSelected = backImage != null;
        if(backImageSelected) {
            backImageIcon = backImage.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(backImageIcon));
        } else {
            imageLabel = new JLabel(localizationResources.getString("None"));
            imageLabel.setEnabled(false);
        }
        interior.add(new JLabel(localizationResources.getString("Background_image")),c);
        c.gridx++;
        interior.add(imageLabel,c);
        c.gridx++;
        JButton button = new JButton(localizationResources.getString("Select..."));
        button.setActionCommand(IMAGE_FILE_CHOOSE);
        button.addActionListener(updateHandler);
        button.addActionListener(this);
        interior.add(button,c);

        startNewRow(c);
        imageAlign = new JComboBox(ALIGNMENT_TEXT);
        imageAlign.setEnabled(backImageSelected);
        int imageAlignment = chart.getBackgroundImageAlignment();
        for(int i = 0; i < ALIGNMENT_VALS.length; i++) {
            if(imageAlignment == ALIGNMENT_VALS[i]) {
                imageAlign.setSelectedIndex(i);
                break;
            }
        }
        imageAlign.addActionListener(updateHandler);
        interior.add(new JLabel(localizationResources.getString("Back_image_align")),c);
        c.gridx++; c.gridwidth=2;
        interior.add(imageAlign,c);

        startNewRow(c);
        imageAlpha = new JSlider(0,100);
        imageAlpha.addChangeListener(updateHandler);
        imageAlpha.setValue((int)(chart.getBackgroundImageAlpha()*100));
        imageAlpha.setEnabled(backImageSelected);
        interior.add(new JLabel(localizationResources.getString("Back_image_alpha")),c);
        c.gridx++; c.gridwidth=2;
        interior.add(imageAlpha, c);

        backgroundInterior.add(interior, BorderLayout.NORTH);
        background.add(backgroundInterior, BorderLayout.CENTER);


        // other tab
        JPanel other = new JPanel(new BorderLayout());
        other.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        JPanel general = new JPanel(new BorderLayout());

        interior = new JPanel(new GridBagLayout());
        c = getNewConstraints();
        interior.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        this.antialias = new JCheckBox(localizationResources.getString(
                "Draw_anti-aliased"));
        this.antialias.setSelected(chart.getAntiAlias());
        antialias.addActionListener(updateHandler);
        c.gridwidth = 3;
        interior.add(this.antialias,c);

        startNewRow(c);
        Object anti = chart.getTextAntiAlias();
        this.textAntialias = new JCheckBox(localizationResources.getString("Draw_anti-aliased_text"),
                anti == null || RenderingHints.VALUE_TEXT_ANTIALIAS_ON.equals(anti));
        this.textAntialias.addActionListener(updateHandler);
        c.gridwidth = 3;
        interior.add(this.textAntialias, c);

        startNewRow(c);
        borderPanel = new BorderPanel(localizationResources.getString("Border"),
                chart.isBorderVisible(), (BasicStroke) chart.getBorderStroke(), chart.getBorderPaint());
        borderPanel.addChangeListener(updateHandler);
        c.gridwidth = 3;
        interior.add(borderPanel, c);

        startNewRow(c);
        chartPadding = new InsetPanel(localizationResources.getString("Padding"), chart.getPadding());
        chartPadding.addChangeListener(updateHandler);
        c.gridwidth = 3;
        interior.add(chartPadding, c);

        startNewRow(c);
        interior.add(new JLabel(localizationResources.getString(
                "Series_Paint")),c);
        c.gridx++; c.weightx=1;
        JTextField info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(false);
        interior.add(info,c);
        c.gridx++;
        button = new JButton(localizationResources.getString("Edit..."));
        button.setEnabled(false);
        interior.add(button,c);

        startNewRow(c);
        interior.add(new JLabel(localizationResources.getString(
                "Series_Stroke")),c);
        c.gridx++; c.weightx=1;
        info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(false);
        interior.add(info,c);
        c.gridx++;
        button = new JButton(localizationResources.getString("Edit..."));
        button.setEnabled(false);
        interior.add(button,c);

        startNewRow(c);
        interior.add(new JLabel(localizationResources.getString(
                "Series_Outline_Paint")),c);
        c.gridx++; c.weightx=1;
        info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(false);
        interior.add(info,c);
        c.gridx++;
        button = new JButton(localizationResources.getString("Edit..."));
        button.setEnabled(false);
        interior.add(button,c);

        startNewRow(c);
        interior.add(new JLabel(localizationResources.getString(
                "Series_Outline_Stroke")),c);
        c.gridx++; c.weightx=1;
        info = new JTextField(localizationResources.getString(
                "No_editor_implemented"));
        info.setEnabled(false);
        interior.add(info,c);
        c.gridx++;
        button = new JButton(localizationResources.getString("Edit..."));
        button.setEnabled(false);
        interior.add(button,c);

        general.add(interior, BorderLayout.NORTH);
        other.add(general, BorderLayout.CENTER);

        JPanel parts = new JPanel(new BorderLayout());

        Title title = chart.getTitle();
        Plot plot = chart.getPlot();

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab(localizationResources.getString("Background"), background);
        tabs.addTab(localizationResources.getString("Chart"), other);
        this.titleEditor = new DefaultTitleEditor(chart, title, this.immediateUpdate);
        this.titleEditor.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        tabs.addTab(localizationResources.getString("Title"), this.titleEditor);

        this.plotEditor = new DefaultPlotEditor(chart, plot, this.immediateUpdate);
        this.plotEditor.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        tabs.addTab(localizationResources.getString("Plot"), this.plotEditor);

        parts.add(tabs, BorderLayout.CENTER);
        add(parts);
    }

    /**
     * Returns a reference to the title editor.
     *
     * @return A panel for editing the title.
     */
    public DefaultTitleEditor getTitleEditor() {
      return this.titleEditor;
    }

    /**
     * Returns a reference to the plot property sub-panel.
     *
     * @return A panel for editing the plot properties.
     */
    public DefaultPlotEditor getPlotEditor() {
        return this.plotEditor;
    }

    /**
     * Returns a reference to the background image of the chart.
     * @return An Image for the chart background.
     */
    public Image getBackgroundImage() {
        return this.backImage;
    }

    /**
     * Returns the current setting of the anti-alias flag.
     *
     * @return <code>true</code> if anti-aliasing is enabled.
     */
    public boolean getAntiAlias() {
        return this.antialias.isSelected();
    }

    /**
     * Returns the current background paint.
     *
     * @return The current background paint.
     */
    public Paint getBackgroundPaint() {
        return this.backgroundPaint.getChosenPaint();
    }

    /**
     * Handles user interactions with the panel.
     *
     * @param event  a BackgroundPaint action.
     */
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (IMAGE_FILE_CHOOSE.equals(command)) {
            attemptModifyBackgroundImage();
        }
    }

    /**
     * Allows the user the opportunity to select a new background image from a file.
     */
    private void attemptModifyBackgroundImage() {
        File f;
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new ImageFilter());
        chooser.setDialogTitle(localizationResources.getString("Background_image"));

        int retVal = chooser.showOpenDialog(this);
        if(retVal == JFileChooser.APPROVE_OPTION) {
            f = chooser.getSelectedFile();
            Image i;
            try {
                i = ImageIO.read(f);
                this.backImage = i;
                backImageSelected = backImage != null;
                if(backImageSelected) {
                    backImageIcon = backImage.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(backImageIcon));
                    imageLabel.setText("");
                }

                imageLabel.setEnabled(backImageSelected);
                imageAlign.setEnabled(backImageSelected);
                imageAlpha.setEnabled(backImageSelected);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Updates the properties of a chart to match the properties defined on the
     * panel.
     *
     * @param chart  the chart.
     */
    public void updateChart(JFreeChart chart) {

        this.titleEditor.updateChart(chart);
        this.plotEditor.updateChart(chart);
        

        chart.setAntiAlias(getAntiAlias());
        chart.setTextAntiAlias(textAntialias.isSelected());
        chart.setBackgroundPaint(getBackgroundPaint());
        chart.setBackgroundImage(getBackgroundImage());
        chart.setBackgroundImageAlignment(ALIGNMENT_VALS[imageAlign.getSelectedIndex()]);
        chart.setBackgroundImageAlpha((float)imageAlpha.getValue()/100);
        chart.setBorderPaint(borderPanel.getBorderPaint());
        chart.setBorderStroke(borderPanel.getBorderStroke());
        chart.setBorderVisible(borderPanel.isBorderVisible());
        chart.setPadding(chartPadding.getSelectedInsets());
//        chart.setSubtitles(null);
    }

    private class ImageFilter extends FileFilter {
        public final static String jpeg = "jpeg";
        public final static String jpg = "jpg";
        public final static String gif = "gif";
        public final static String tiff = "tiff";
        public final static String tif = "tif";
        public final static String png = "png";

        public final String[] ALLOWED_EXTENSIONS = new String[] {
                jpeg, jpg, gif, tiff, tif, png
        };

        public boolean accept(File f) {
            if(f.isDirectory()) {
                return true;
            }

            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 &&  i < s.length() - 1) {
                ext = s.substring(i+1).toLowerCase();
            }

            for(i = 0; i < ALLOWED_EXTENSIONS.length; i++) {
                if(ALLOWED_EXTENSIONS[i].equals(ext)) {
                    return true;
                }
            }

            return false;
        }

        public String getDescription() {
            return localizationResources.getString("Image_files");
        }
    }
}
