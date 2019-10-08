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

package visualizer.topic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import visualizer.graph.Scalar;
import visualizer.graph.Vertex;
import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;
import visualizer.matrix.Matrix;
import visualizer.projection.distance.Euclidean;
import visualizer.util.PExConstants;
import visualizer.datamining.clustering.Jdbscan2D;
import visualizer.util.OpenDialog;
import visualizer.view.Viewer;
import visualizer.topic.TopicData.ClusteringType;
import visualizer.datamining.clustering.BKmeans;

/**
 *
 * @author Fernando Vieira Paulovich, Roberto Pinho
 */
public class TopicClusters {

    /** Creates a new instance of TopicClusters
     * @param gv 
     */
    public TopicClusters(Viewer gv) {
        this.gv = gv;
    }

    public void execute(javax.swing.JFrame parent) throws IOException {
        this.gv.getGraph().getTopicData().getTermSetRun().clear();
        this.gv.getGraph().getTopicData().getTermSetRunW().clear();
        this.gv.getGraph().addScalar(PExConstants.TOPICS);

        //Create the clusters
        Matrix matrix = new DenseMatrix();
        for (int i = 0; i < this.gv.getGraph().getVertex().size(); i++) {
            float[] point = new float[2];
            point[0] = this.gv.getGraph().getVertex().get(i).getX();
            point[1] = this.gv.getGraph().getVertex().get(i).getY();
            matrix.addRow(new DenseVector(point));
        }

        ArrayList<ArrayList<Integer>> clusters = null;
        String scalarName = null;

        if (this.gv.getGraph().getTopicData().getClusteringType() == ClusteringType.KMEANS) {
            String inputValue = (String) JOptionPane.showInputDialog(null,
                    "Choose the number of clusters:", "Defining the Number of Clusters",
                    JOptionPane.QUESTION_MESSAGE, null, null,
                    (Object) Integer.toString((int) Math.sqrt(this.gv.getGraph().getVertex().size())));

            if (inputValue == null) {
                return;
            }

            int nclusters = Integer.parseInt(inputValue);

            try {
                BKmeans km = new BKmeans(nclusters);
                clusters = km.execute(new Euclidean(), matrix);
                scalarName = PExConstants.KMEANS + clusters.size();
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Jdbscan2D dbscan = new Jdbscan2D();
            clusters = dbscan.execute(new Euclidean(), matrix);
            scalarName = PExConstants.DBSCAN + clusters.size();
        }

        Scalar s = this.gv.getGraph().addScalar(scalarName);
        for (int c = 0; c < clusters.size(); c++) {
            for (int v = 0; v < clusters.get(c).size(); v++) {
                this.gv.getGraph().getVertex().get(clusters.get(c).get(v)).setScalar(s, c);
            }
        }

        if (OpenDialog.checkCorpus(this.gv.getGraph(), parent)) {
            //for each cluster
            for (int c = 0; c < clusters.size(); c++) {
                ArrayList<Vertex> vertex = new ArrayList<Vertex>();

                for (int v = 0; v < clusters.get(c).size(); v++) {
                    vertex.add(this.gv.getGraph().getVertex().get(clusters.get(c).get(v)));
                }

                Topic topic = TopicFactory.getInstance(this.gv.getGraph(),
                        this.gv.getGraph().getTopicData(),
                        this.gv.getGraph().getCorpus(), vertex);

                this.gv.addTopic(topic);
            }
        }

        this.gv.updateScalars(this.gv.getGraph().getScalarByName(PExConstants.TOPICS));
        this.gv.updateImage();
    }

    private Viewer gv;
}
