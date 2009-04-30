package org.jfree.chart.editor.components;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 20-Jan-2009
 * Time: 15:14:03
 * Base functionality added to JPanel so that components can be made from this class, where
 * changes to the objects being edited can be notified through a ChangeListener list.
 * Classes offering extensions to this class should call fireChangeEvent() to set off change
 * notifications.
 *
 * Classes utilising components descended from this class should register change listeners using
 * the addChangeListener method.
 */
public abstract class ChangeNotifyingComponent extends JPanel {
    private EventListenerList listeners = new EventListenerList();

    protected ChangeNotifyingComponent() {
        super();
    }

    protected ChangeNotifyingComponent(LayoutManager l) {
        super(l);
    }

    public void addChangeListener(ChangeListener l) {
        listeners.add(ChangeListener.class, l);
    }

    public void removeChangeListener(ChangeListener l) {
        listeners.remove(ChangeListener.class, l);
    }

    private ChangeEvent changeEvent = null;
    protected void fireChangeEvent() {
        // Guaranteed to return a non-null array
        Object[] listenersList = listeners.getListenerList();
        // Process the listenersList last to first, notifying
        // those that are interested in this event
        for (int i = listenersList.length-2; i>=0; i-=2) {
            if (listenersList[i]==ChangeListener.class) {
                // Lazily create the event:
                if (changeEvent == null) {
                    changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listenersList[i+1]).stateChanged(changeEvent);
            }
        }
    }
}
