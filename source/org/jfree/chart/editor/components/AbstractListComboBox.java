package org.jfree.chart.editor.components;

import org.jfree.chart.util.ResourceBundleWrapper;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: Dan
 * Date: 19-Jan-2009
 * Time: 14:28:32
 * Basis for combo boxes with hard-coded list of options. Useful to cut down on duplicated functionality.
 * The class is obviously quite well-suited to generics if/when Java 1.5 becomes the minimum supported version
 * or there is an automatic way to compile Java 1.5 code for earlier JVM's.
 */
public abstract class AbstractListComboBox extends JComboBox {
    protected static ResourceBundle localizationResources
            = ResourceBundleWrapper.getBundle(
                    "org.jfree.chart.editor.LocalizationBundle");

    public AbstractListComboBox() {
        setModel(new DefaultComboBoxModel(getLabels()));
    }

    protected abstract Object[] getObjects();
    protected abstract String[] getLabels();

    public Object getSelectedObject() {
        return getObjects()[getSelectedIndex()];
    }

    public void setSelectedObject(Object o) {
        Object[] objects = getObjects();
        for(int i = 0; i < objects.length; i++) {
            if(objects[i].equals(o)) {
                setSelectedItem(getLabels()[i]);
            }
        }
    }
}
