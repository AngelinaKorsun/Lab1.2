package com.company;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class AutoCrossCorrelation {
    public static double mX = 0;
    public static double mY = 0;
    public static long time = 0;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Charts");

                frame.setSize(600, 400);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

                XYDataset ds = createDataset();

                JFreeChart chart = ChartFactory.createXYLineChart("Auto and Cross Correlation",
                        "tt", "AC, CC", ds, PlotOrientation.VERTICAL, true, true,
                        false);


                ChartPanel cp = new ChartPanel(chart);
                JPanel data = new JPanel();

                JLabel label = new JLabel("Time: " + time);
                data.add(label);

                Container pane = frame.getContentPane();

                pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));

                pane.add(cp);
                pane.add(data);

            }
        });

    }

    private static XYDataset createDataset() {
        long start, end;

        int n = 10;
        int w = 1500;
        int nn = 256;
        double sum = 0;
        double sum1 = 0;

        double a;
        double f;
        double[] dataN = new double[nn];
        double[] dataX = new double[nn];
        double[] dataY = new double[nn];
        double[] dataAC = new double[nn / 2];
        double[] dataCC = new double[nn / 2];

        // long[] time = new long[1024];
        start = System.nanoTime();
        for (int t = 0; t < nn; t++) {
            sum = 0;
            a = (Math.random());
            f = (Math.random());
            for (int x = 0; x < n; x++) {
                sum += a * Math.sin(w * t + f);
            }
            dataN[t] = t;
            dataX[t] = sum;
        }

        for (int t = 0; t < nn; t++) {
            sum = 0;
            a = (Math.random());
            f = (Math.random());
            for (int y = 0; y < n; y++) {
                sum += a * Math.sin(w * t + f);
            }
            dataY[t] = sum;
        }

        for (int t = 0; t < nn; t++) {
            mX += dataX[t] / nn;
            mY += dataY[t] / nn;
        }

        for (int tt = 0; tt < nn / 2; tt++) {
            sum = 0;
            sum1 = 0;
            for (int t = 0; t < nn / 2; t++) {
                sum += ((dataX[t] - mX) * (dataX[t + tt] - mX)) / (nn - 1);
                sum1 += ((dataX[t] - mX) * (dataY[t + tt] - mY)) / (nn - 1);
            }
            dataAC[tt] = sum;
            dataCC[tt] = sum1;
        }
        end = System.nanoTime();
        time = end - start;

        XYSeriesCollection ds = new XYSeriesCollection();

        XYSeries s1 = new XYSeries("s1");
        XYSeries s2 = new XYSeries("s2");

        for (int i = 0; i < nn / 2; i++) {
            s1.add(i, dataAC[i]);
            s2.add(i, dataCC[i]);
        }

        ds.addSeries(s1);
        ds.addSeries(s2);

        return ds;
    }
}

