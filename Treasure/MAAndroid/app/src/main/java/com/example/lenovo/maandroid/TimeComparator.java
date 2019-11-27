package com.example.lenovo.maandroid;

import java.util.Comparator;

public class TimeComparator implements Comparator<Dates> {
    @Override
    public int compare(Dates o1, Dates o2) {
        return o2.getTime().compareTo(o1.getTime());
    }
}
