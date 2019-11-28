package com.test.jfx.Utils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class ProgressChanger extends Thread {

    private DoubleProperty doubleProperty = new SimpleDoubleProperty();

    @Override
    public void run() {
        for (int i = 0; i <= 100; i += 5) {
            try {
                sleep(150);
                doubleProperty.setValue(i / 100.0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public DoubleProperty getDoubleProperty() {
        return doubleProperty;
    }

    public void setDoubleProperty(DoubleProperty doubleProperty) {
        this.doubleProperty = doubleProperty;
    }
}
