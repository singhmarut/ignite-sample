package com.marut.pagecount;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by marutsingh on 12/25/16.
 */
public class MainApp {
    public static void main(String[] args){
        PageCountSimulator pageCountSimulator = new PageCountSimulator();
        pageCountSimulator.simulate();
    }
}
