/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer (PEx).
 *
 * How to cite this work:
 *  
@inproceedings{paulovich2007pex,
author = {Fernando V. Paulovich and Maria Cristina F. Oliveira and Rosane 
Minghim},
title = {The Projection Explorer: A Flexible Tool for Projection-based 
Multidimensional Visualization},
booktitle = {SIBGRAPI '07: Proceedings of the XX Brazilian Symposium on 
Computer Graphics and Image Processing (SIBGRAPI 2007)},
year = {2007},
isbn = {0-7695-2996-8},
pages = {27--34},
doi = {http://dx.doi.org/10.1109/SIBGRAPI.2007.39},
publisher = {IEEE Computer Society},
address = {Washington, DC, USA},
}
 *  
 * PEx is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * PEx is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * This code was developed by members of Computer Graphics and Image
 * Processing Group (http://www.lcad.icmc.usp.br) at Instituto de Ciencias
 * Matematicas e de Computacao - ICMC - (http://www.icmc.usp.br) of 
 * Universidade de Sao Paulo, Sao Carlos/SP, Brazil. The initial developer 
 * of the original code is Fernando Vieira Paulovich <fpaulovich@gmail.com>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.graph.scalar;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.RefineryUtilities;
import visualizer.graph.Graph;
import visualizer.graph.Scalar;
import visualizer.graph.Vertex;
import visualizer.matrix.Matrix;
import visualizer.matrix.MatrixFactory;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.Dissimilarity;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class DistanceScalar {

    public DistanceScalar(Graph graph) {
        this.graph = graph;
    }

    public Scalar scalarFromPointsFile(String filename, Dissimilarity measure,
            Vertex vertex) throws IOException {
        Matrix matrix = MatrixFactory.getInstance(filename);

        //defining the index
        int index = -1;
        for (int i = 0; i < matrix.getRowCount(); i++) {
            if (matrix.getRow(i).getId().equals(vertex.getUrl())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new IOException("Query data instance not found on the points file.");
        }

        //creating the scalar values
        float[] scalar = new float[matrix.getRowCount()];

        float min = Float.POSITIVE_INFINITY;
        for (int i = 0; i < scalar.length; i++) {
            scalar[i] = measure.calculate(matrix.getRow(i), matrix.getRow(index));

            if (min > scalar[i] && i != index) {
                min = scalar[i];
            }
        }

        scalar[index] = min;

        ArrayList<String> ids = new ArrayList<String>();
        for (int i = 0; i < matrix.getRowCount(); i++) {
            ids.add(matrix.getRow(i).getId());
        }

        return this.createScalar(scalar, ids, vertex);
    }

    public Scalar scalarFromDistanceMarix(String filename, Vertex vertex) throws IOException {
        DistanceMatrix dmat = new DistanceMatrix(filename);
        ArrayList<String> filenames = dmat.getIds();

        //defining the index
        int index = -1;
        for (int i = 0; i < filenames.size(); i++) {
            if (filenames.get(i).equals(vertex.getUrl())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new IOException("Query data instance not found on the points file.");
        }

        //creating the scalar values
        float[] scalar = new float[dmat.getElementCount()];

        float min = Float.POSITIVE_INFINITY;
        for (int i = 0; i < dmat.getElementCount(); i++) {
            scalar[i] = dmat.getDistance(i, index);

            if (min > scalar[i] && i != index) {
                min = scalar[i];
            }
        }

        scalar[index] = min;

        return this.createScalar(scalar, filenames, vertex);
    }

    private Scalar createScalar(float[] scalar, ArrayList<String> ids,
            Vertex v) throws IOException {
        Scalar s = null;

        //Creating the new scalar inside the graph
        HashMap<String, Float> index = new HashMap<String, Float>();
        for (int i = 0; i < ids.size(); i++) {
            index.put(ids.get(i), scalar[i]);
        }

        String scalarname = v.toString();
        scalarname = scalarname.substring(scalarname.lastIndexOf("\\") + 1);

        s = graph.addScalar(scalarname);
        ArrayList<Vertex> vertex = graph.getVertex();

        for (int i = 0; i < vertex.size(); i++) {
            Vertex v_aux = vertex.get(i);

            if (v_aux.isValid()) {
                if (index.get(v_aux.getUrl()) != null) {
                    v_aux.setScalar(s, index.get(v_aux.getUrl()));
                } else {
                    throw new IOException("The ids (file name) of the vertices " +
                            "are not the same of the provided points file or " +
                            "distance matrix: " + v_aux.getUrl());
                }
            }
        }

        //creating the histogram
        DistancesHistogram hist = new DistancesHistogram(scalar, v.toString());
        hist.pack();
        RefineryUtilities.centerFrameOnScreen(hist);
        hist.setVisible(true);

        return s;
    }

    public class DistancesHistogram extends JFrame {

        public DistancesHistogram(float[] scalar, String title) {
            super("Histogram of Distances");

            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            this.setAlwaysOnTop(true);
            this.setResizable(false);

            JFreeChart freechart = this.createChart(title,
                    this.createDataset(scalar, title));
            JPanel panel = new ChartPanel(freechart);

            panel.setPreferredSize(new Dimension(600, 400));
            panel.setSize(new Dimension(600, 400));
            setContentPane(panel);
        }

        private IntervalXYDataset createDataset(float[] scalar, String title) {
            HistogramDataset histogramdataset = new HistogramDataset();

            //extracting negative values
            ArrayList<Float> scalar_aux = new ArrayList<Float>();
            for (int i = 0; i < scalar.length; i++) {
                if (scalar[i] >= 0.0f) {
                    scalar_aux.add(scalar[i]);
                }
            }

            scalar = new float[scalar_aux.size()];
            for (int i = 0; i < scalar.length; i++) {
                scalar[i] = scalar_aux.get(i);
            }

            float max = Float.NEGATIVE_INFINITY;
            float min = Float.POSITIVE_INFINITY;

            for (int i = 0; i < scalar.length; i++) {
                if (max < scalar[i]) {
                    max = scalar[i];
                }

                if (min > scalar[i]) {
                    min = scalar[i];
                }
            }

            double[] ad = new double[scalar.length];
            for (int i = 0; i < scalar.length; i++) {
//                ad[i] = (scalar[i] - min) / (max - min);
                ad[i] = scalar[i];
            }

//            histogramdataset.addSeries(title, ad, 200, 0, 1);
            histogramdataset.addSeries(title, ad, 100, min, max);

            return histogramdataset;
        }

        private JFreeChart createChart(String title, IntervalXYDataset intervalxydataset) {
            JFreeChart jfreechart = ChartFactory.createHistogram(title,
                    "Distances Values", "Occurences", intervalxydataset,
                    PlotOrientation.VERTICAL, true, true, false);
            XYPlot xyplot = (XYPlot) jfreechart.getPlot();
            xyplot.setForegroundAlpha(0.85F);
            XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
            xybarrenderer.setDrawBarOutline(false);
            return jfreechart;
        }

    }

    private Graph graph;
}
