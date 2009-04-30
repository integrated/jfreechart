package org.jfree.chart.editor.components;

import org.jfree.ui.StrokeSample;
import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.chart.editor.components.RectStrokeSample;
import org.jfree.chart.editor.components.NoCircleStrokeSample;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 13-Jan-2009
 * Time: 09:09:55
 * To change this template use File | Settings | File Templates.
 */
public class StrokeEditorPanel extends JPanel {/** The resourceBundle for the localization. */
    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
                    "org.jfree.chart.editor.LocalizationBundle");

    private static final float DEFAULT_DASH_PHASE = 0f;
    private static final float DEFAULT_MITER_LIMIT = 10f;
    private static final int DEFAULT_JOIN = BasicStroke.JOIN_MITER;
    private static final int DEFAULT_CAP = BasicStroke.CAP_SQUARE;
    private static final float DEFAULT_WIDTH = 1f;
    private final StrokeSample[] DASH_PATTERNS = new StrokeSample[] {
            new StrokeSample(new BasicStroke()), // solid (default)
            new StrokeSample(new BasicStroke( // alternating 50% dashed
                    DEFAULT_WIDTH, DEFAULT_CAP, DEFAULT_JOIN, DEFAULT_MITER_LIMIT,
                    new float[] { 10f, 10f }, DEFAULT_DASH_PHASE)),
            new StrokeSample(new BasicStroke( // 75% dashed
                    DEFAULT_WIDTH, DEFAULT_CAP, DEFAULT_JOIN, DEFAULT_MITER_LIMIT,
                    new float[] { 7.5f, 2.5f }, DEFAULT_DASH_PHASE)),
            new StrokeSample(new BasicStroke( // 25% dashed
                    DEFAULT_WIDTH, DEFAULT_CAP, DEFAULT_JOIN, DEFAULT_MITER_LIMIT,
                    new float[] { 2.5f, 7.5f }, DEFAULT_DASH_PHASE))
    };

    private EventHandler handler = new EventHandler();
    private BasicStroke chosenStroke;
    private StrokeSample examplePanel;
    private JComboBox dashBox;
    private JSpinner lineWidth;


    public StrokeEditorPanel() {
        this(new BasicStroke());
    }

    public StrokeEditorPanel(BasicStroke stroke) {
        this.chosenStroke = stroke;

        examplePanel = new RectStrokeSample(chosenStroke);

        Vector patterns = new Vector();
        patterns.add(new StrokeSample(chosenStroke));
        float[] chosenArray = chosenStroke.getDashArray();
        for(int i = 0; i < DASH_PATTERNS.length; i++) {
            BasicStroke basicStroke = (BasicStroke) DASH_PATTERNS[i].getStroke();
            float[] dashArray = basicStroke.getDashArray();
            if(!Arrays.equals(chosenArray, dashArray)) {
                patterns.add(DASH_PATTERNS[i]);
            }
        }
        
        dashBox = new JComboBox(patterns);
        dashBox.addActionListener(handler);
        dashBox.setRenderer(new NoCircleStrokeSample(new BasicStroke()));
        dashBox.setSelectedIndex(0);

        lineWidth = new JSpinner(new SpinnerNumberModel(stroke.getLineWidth(),0,100,0.1));
        lineWidth.addChangeListener(handler);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2,3,2,3);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0; c.gridy = 0;
        add(new JLabel(localizationResources.getString("Line_Pattern")), c);
        c.gridx++;
        add(dashBox, c);
        c.gridx = 0; c.gridy++;
        add(new JLabel(localizationResources.getString("Line_Width")), c);
        c.gridx++;
        add(lineWidth, c);

        c.gridx = 0; c.gridy++; c.gridwidth = 2;
        JPanel border = new JPanel(new BorderLayout());
        border.setBorder(BorderFactory.createTitledBorder(localizationResources.getString("Sample")));
        border.add(examplePanel, BorderLayout.CENTER);
        add(border, c);
    }

    public Stroke getSelectedStroke() {
        return chosenStroke;
    }

    private void sampleChanged() {
        examplePanel.setStroke(chosenStroke);
    }

    private class EventHandler implements ActionListener, ChangeListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == dashBox) {
                BasicStroke selected = (BasicStroke)((StrokeSample)dashBox.getSelectedItem()).getStroke();
                chosenStroke = new BasicStroke(
                        chosenStroke.getLineWidth(), chosenStroke.getEndCap(), chosenStroke.getLineJoin(),
                        chosenStroke.getMiterLimit(), selected.getDashArray(), chosenStroke.getDashPhase()
                );
                sampleChanged();
            }
        }

        public void stateChanged(ChangeEvent e) {
            if(e.getSource() == lineWidth) {
                Number val = (Number)lineWidth.getValue();
                chosenStroke = new BasicStroke(
                        val.floatValue(), chosenStroke.getEndCap(), chosenStroke.getLineJoin(),
                        chosenStroke.getMiterLimit(), chosenStroke.getDashArray(), chosenStroke.getDashPhase()
                );
                sampleChanged();
            }
        }
    }

}
