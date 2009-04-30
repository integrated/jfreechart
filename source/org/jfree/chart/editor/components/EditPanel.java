package org.jfree.chart.editor.components;

import org.jfree.chart.util.ResourceBundleWrapper;
import org.jfree.chart.editor.BaseEditor;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 16-Jan-2009
 * Time: 13:27:32
 * Shared functionality for chart editing panels in this package
 */
public class EditPanel extends JPanel {
    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
                    "org.jfree.chart.editor.LocalizationBundle");

    public EditPanel() {
        super();
    }

    public EditPanel(LayoutManager l) {
        super(l);
    }

    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    protected void fireChangeEvent() {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        ChangeEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==ChangeListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new ChangeEvent(this);
                }
                ((ChangeListener)listeners[i+1]).stateChanged(e);
            }
        }
    }

    protected static GridBagConstraints getNewConstraints() {
        return BaseEditor.getNewConstraints();
    }

    protected static void startNewRow(GridBagConstraints c) {
        BaseEditor.startNewRow(c);
    }
}
