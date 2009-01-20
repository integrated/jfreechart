package org.jfree.chart.editor.components;

import org.jfree.chart.EditableBackground;
import org.jfree.chart.plot.Plot;
import org.jfree.ui.Align;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 19-Jan-2009
 * Time: 15:53:48
 * Allows editing of the common interace for background properties in JFreeChart and Plot objects.
 */
public class BackgroundEditingPanel extends EditPanel {
    /** The icon size used for the background image icon */
    private final static int ICON_HEIGHT = 50;
    /** The icon size used for the background image icon */
    private final static int ICON_WIDTH = 50;

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

    private JCheckBox imageCheck;

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

    /** Control to change the alpha-value for the background color (only applicable to Plot objects at the moment) */
    private JSlider backgroundAlpha;

    /** Whether a background image is in use for this chart */
    private boolean backImageSelected;

    /** Button that loads the file chooser for changing the background image */
    private JButton imageButton;

    private final EventHandler updateHandler = new EventHandler();


    public BackgroundEditingPanel(EditableBackground background) {
        setLayout(new BorderLayout());
        JPanel interior = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        interior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        c.gridx = 0; c.gridy = 0; c.fill = GridBagConstraints.HORIZONTAL; c.insets = new Insets(2,3,2,3);
        c.anchor = GridBagConstraints.CENTER;
        interior.add(new JLabel(localizationResources.getString(
                "Background_paint")),c);
        c.gridx++; c.weightx = 1.0; c.gridwidth = 3;
        this.backgroundPaint = new PaintControl(background.getBackgroundPaint(), true);
        this.backgroundPaint.addChangeListener(updateHandler);
        interior.add(this.backgroundPaint,c);

        if(background instanceof Plot) {
            backgroundAlpha = new JSlider(0,100);
            backgroundAlpha.addChangeListener(updateHandler);
            backgroundAlpha.setValue((int)(((Plot)background).getBackgroundAlpha()*100));

            c.gridy++; c.gridx=0; c.weightx = 0; c.gridwidth = 1;
            interior.add(new JLabel(localizationResources.getString("Background_Alpha")+":"),c);
            c.gridx++; c.weightx = 1; c.gridwidth = 3;
            interior.add(backgroundAlpha, c);
        }

        c.gridy++; c.gridx=0; c.weightx = 0; c.gridwidth = 1;
        backImage = background.getBackgroundImage();
        backImageSelected = backImage != null;
        if(backImageSelected) {
            backImageIcon = backImage.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(backImageIcon));
        } else {
            imageLabel = new JLabel(localizationResources.getString("None"));
            imageLabel.setEnabled(false);
        }
        imageCheck = new JCheckBox();
        imageCheck.setSelected(backImageSelected);
        imageCheck.addActionListener(updateHandler);

        interior.add(new JLabel(localizationResources.getString("Background_image")),c);
        c.gridx++;
        interior.add(imageCheck,c);
        c.gridx++; c.weightx = 1;
        interior.add(imageLabel,c);
        c.gridx++; c.weightx = 0;
        imageButton = new JButton(localizationResources.getString("Select..."));
        imageButton.setActionCommand(IMAGE_FILE_CHOOSE);
        imageButton.addActionListener(updateHandler);
        imageButton.setEnabled(backImageSelected);
        interior.add(imageButton,c);

        c.gridy++; c.gridx=0; c.weightx = 0; c.gridwidth = 1;
        imageAlign = new JComboBox(ALIGNMENT_TEXT);
        imageAlign.setEnabled(backImageSelected);
        int imageAlignment = background.getBackgroundImageAlignment();
        for(int i = 0; i < ALIGNMENT_VALS.length; i++) {
            if(imageAlignment == ALIGNMENT_VALS[i]) {
                imageAlign.setSelectedIndex(i);
                break;
            }
        }
        imageAlign.addActionListener(updateHandler);
        interior.add(new JLabel(localizationResources.getString("Back_image_align")),c);
        c.gridx++; c.gridwidth=3; c.weightx = 1;
        interior.add(imageAlign,c);

        c.gridy++; c.gridx=0; c.weightx = 0; c.gridwidth = 1;
        imageAlpha = new JSlider(0,100);
        imageAlpha.addChangeListener(updateHandler);
        imageAlpha.setValue((int)(background.getBackgroundImageAlpha()*100));
        imageAlpha.setEnabled(backImageSelected);
        interior.add(new JLabel(localizationResources.getString("Back_image_alpha")),c);
        c.gridx++; c.gridwidth=3; c.weightx = 1;
        interior.add(imageAlpha, c);

        add(interior, BorderLayout.NORTH);
    }

    public float getBackgroundAlpha() {
        return (float)backgroundAlpha.getValue()/100;
    }

    public Image getBackgroundImage() {
        return backImageSelected ? backImage : null;
    }

    public Paint getBackgroundPaint() {
        return backgroundPaint.getChosenPaint();
    }

    public int getBackgroundImageAlignment() {
        return ALIGNMENT_VALS[imageAlign.getSelectedIndex()];
    }

    public float getBackgroundImageAlpha() {
        return (float)imageAlpha.getValue()/100;
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

                updateControls();

                fireChangeEvent();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void updateControls() {
        imageLabel.setEnabled(backImageSelected);
        imageAlign.setEnabled(backImageSelected);
        imageAlpha.setEnabled(backImageSelected);
    }

    private class EventHandler implements ChangeListener, ActionListener {

        public void stateChanged(ChangeEvent e) {
            fireChangeEvent();
        }

        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == imageCheck) {

                boolean iCheckSelected = imageCheck.isSelected();
                backImageSelected = iCheckSelected && backImage != null;
                updateControls();
                imageButton.setEnabled(iCheckSelected);
                
                fireChangeEvent();
            } if(IMAGE_FILE_CHOOSE.equals(e.getActionCommand())) {
                attemptModifyBackgroundImage();
            } else if (e.getSource() == imageAlign) {
                fireChangeEvent();
            }
        }
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
