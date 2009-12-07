package org.jfree.chart.editor.components;

import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.editor.themes.BasicPlotTheme;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.SortedMap;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 07-Dec-2009
 * Time: 10:35:14
 * Controls for the position of an item label. Broadly applicable to both XY and Category plots.
 */
public class ItemLabelPositionPanel extends EditPanel {
    private final static SortedMap POSITIONS = new TreeMap();
    private final static Map REVERSE_POSITIONS = new HashMap();
    private final static Map ANCHORS = new HashMap();

    static {
        String north = localizationResources.getString("North");
        POSITIONS.put(north, ItemLabelAnchor.OUTSIDE12);
        REVERSE_POSITIONS.put(ItemLabelAnchor.OUTSIDE12, north);
        ANCHORS.put(north, TextAnchor.BOTTOM_CENTER);

        String east = localizationResources.getString("East");
        POSITIONS.put(east, ItemLabelAnchor.OUTSIDE3);
        REVERSE_POSITIONS.put(ItemLabelAnchor.OUTSIDE3, east);
        ANCHORS.put(east, TextAnchor.CENTER_LEFT);

        String south = localizationResources.getString("South");
        POSITIONS.put(south, ItemLabelAnchor.OUTSIDE6);
        REVERSE_POSITIONS.put(ItemLabelAnchor.OUTSIDE6, south);
        ANCHORS.put(south, TextAnchor.TOP_CENTER);

        String west = localizationResources.getString("West");
        POSITIONS.put(west, ItemLabelAnchor.OUTSIDE9);
        REVERSE_POSITIONS.put(ItemLabelAnchor.OUTSIDE9, west);
        ANCHORS.put(west, TextAnchor.CENTER_RIGHT);

        String centre = localizationResources.getString("Centre");
        POSITIONS.put(centre, ItemLabelAnchor.CENTER);
        REVERSE_POSITIONS.put(ItemLabelAnchor.CENTER, centre);
        ANCHORS.put(centre, TextAnchor.CENTER);

        //assert POSITIONS.size() == ANCHORS.size();
        //assert POSITIONS.size() == REVERSE_POSITIONS.size();
    }


    private JComboBox posCombo;
    private JSpinner rotation;

    private boolean fireEvents = true;

    public ItemLabelPositionPanel(String title) {
        this(title, BasicPlotTheme.DEFAULT_POSITIVE_POSITION);
    }

    public ItemLabelPositionPanel(String title, ItemLabelPosition pos) {

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder(title));
        EventHandler handler = new EventHandler();

        posCombo = new JComboBox(POSITIONS.keySet().toArray());
        posCombo.setSelectedItem(REVERSE_POSITIONS.get(pos.getItemLabelAnchor()));
        posCombo.addActionListener(handler);
        rotation = new JSpinner(new SpinnerNumberModel(Math.toDegrees(pos.getAngle()), 0, 360, 1));
        rotation.addChangeListener(handler);

        GridBagConstraints c = getNewConstraints();

        add(new JLabel(localizationResources.getString("Position")), c);
        c.gridx++; c.weightx = 1;
        add(posCombo, c);

        startNewRow(c);
        add(new JLabel(localizationResources.getString("Label_angle")), c);
        c.gridx++; c.weightx = 1;
        add(rotation, c);
    }

    public ItemLabelPosition getItemLabelPosition() {
        String edgeKey = (String) posCombo.getSelectedItem();
        double angle = Math.toRadians(((Double) rotation.getValue()).doubleValue());

        return new ItemLabelPosition(
                (ItemLabelAnchor) POSITIONS.get(edgeKey), (TextAnchor) ANCHORS.get(edgeKey),
                TextAnchor.CENTER, angle);
    }

    public void setItemLabelPosition(ItemLabelPosition pos) {
        if(pos == null) {
            throw new IllegalArgumentException("ItemLabelPosition parameter cannot be null");
        }
        try {
            fireEvents = false;
            posCombo.setSelectedItem(REVERSE_POSITIONS.get(pos.getItemLabelAnchor()));
            rotation.setValue(new Double(Math.toDegrees(pos.getAngle())));
        } finally {
            fireEvents = true;
            fireChangeEvent();
        }

    }

    public void setEnabled(boolean e) {
        super.setEnabled(e);

        posCombo.setEnabled(e);
        rotation.setEnabled(e);
    }

    private class EventHandler implements ActionListener, ChangeListener {
        public void actionPerformed(ActionEvent e) {
            if(fireEvents) { fireChangeEvent(); }
        }

        public void stateChanged(ChangeEvent e) {
            if(fireEvents) { fireChangeEvent(); }
        }
    }
}
