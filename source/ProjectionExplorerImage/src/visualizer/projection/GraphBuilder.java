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

package visualizer.projection;

import visualizer.matrix.MatrixFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.corpus.Corpus;
import visualizer.corpus.CorpusFactory;
import visualizer.dimensionreduction.DimensionalityReduction;
import visualizer.dimensionreduction.DimensionalityReductionFactory;
import visualizer.dimensionreduction.DimensionalityReductionType;
import visualizer.graph.Connectivity;
import visualizer.graph.Graph;
import visualizer.graph.Scalar;
import visualizer.graph.Vertex;
import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;
import visualizer.matrix.Matrix;
import visualizer.matrix.normalization.Normalization;
import visualizer.matrix.normalization.NormalizationFactory;
import visualizer.projection.distance.DissimilarityFactory;
import visualizer.projection.distance.DistanceMatrix;
import visualizer.projection.distance.DistanceMatrixFactory;
import visualizer.projection.distance.Dissimilarity;
import visualizer.projection.distance.DissimilarityType;
import visualizer.projection.distance.Euclidean;
import visualizer.util.PExConstants;
import visualizer.datamining.clustering.HierarchicalClustering;
import visualizer.datamining.clustering.HierarchicalClusteringType;
import visualizer.featureextraction.ExtractionManager;
import visualizer.graph.ImageCollection;
import visualizer.util.Util;
import visualizer.util.ScilabConnection;
import visualizer.util.SystemPropertiesManager;
import visualizer.util.PointsReader;
import visualizer.wizard.ProjectionView;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class GraphBuilder {

    /** Creates a new instance of GraphBuilder
     * @param view
     * @param graph 
     */
    public GraphBuilder(ProjectionView view, Graph graph) {
        this.view = view;
        this.graph = graph;
        this.exception = null;
    }

    public void start() {
        this.t = new Thread() {

            @Override
            public void run() {
                try {
                    GraphBuilder.this.createGraph(graph.getProjectionData());
                    GraphBuilder.this.dispose();
                    Runtime.getRuntime().gc();
                } catch (IOException ex) {
                    exception = ex;
                } finally {
                    view.finished(exception);
                }
            }

        };

        this.t.start();
    }

    public void stop() {
        if (this.t.isAlive()) {
            this.t.interrupt();
            this.dispose();
            Runtime.getRuntime().gc();
        }
    }

    private void createGraph(ProjectionData pdata) throws IOException {
        float[] cdata = null;
        ArrayList<String> ids = null;
        float[][] projection = null;
        Projection proj = null;

        if (pdata.getSourceType() == SourceType.DISTANCE_MATRIX ||
                pdata.getDissimilarityType() == DissimilarityType.KOLMOGOROV) {

            //projecting using distance matrix
            dmat = DistanceMatrixFactory.getInstance(view, pdata);
            cdata = dmat.getClassData();
            ids = dmat.getIds();

            //saving the data
            this.saveData(pdata, dmat, null);

            proj = ProjectionFactory.getInstance(pdata.getProjectionType());
            projection = proj.project(dmat, pdata, view);

            pdata.setNumberObjects(dmat.getElementCount());

        } else if (pdata.getSourceType() == SourceType.IMAGES) {
           matrix = new DenseMatrix();            
           String unzipDir = SystemPropertiesManager.getInstance().getProperty("UNZIP.DIR");
           
           Util.deleteFiles(unzipDir);
           this.view.setStatus("Unziping Images..." + pdata.getSourceFile(), 5);
           Util.unzipImages(pdata.getSourceFile());
           
           //ImageUtil.convertDirToGray(unzipDir);
           
           this.view.setStatus("Extracting Features..." + pdata.getSourceFile(), 15);
           ExtractionManager EM = new ExtractionManager(pdata, unzipDir);           
           
           //PointsReader reader = new PointsReader(pdata.getDocsTermsFilename());
           float points[][] = EM.getMatrixPoints();//reader.getPoints();
           cdata = EM.getClassData();// EM.getClasses();// reader.getCdata();
           ids = EM.getNameImages();           

           //Construir a DenseMatrix aqui
           for (int i = 0; i < points.length; i++) {
              matrix.addRow(new DenseVector(points[i], ids.get(i), cdata[i]));
           }

           //normalization
            Normalization norm = NormalizationFactory.getInstance(pdata.getNormalization());
            matrix = norm.execute(matrix);

            this.graph.setImageCollection(new ImageCollection(pdata.getSourceFile()));

            //saving the data
            this.saveData(pdata, null, matrix);

            proj = ProjectionFactory.getInstance(pdata.getProjectionType());
            projection = proj.project(matrix, pdata, view);

            pdata.setNumberObjects(matrix.getRowCount());
            pdata.setNumberDimensions(matrix.getDimensions());

            dmat = proj.getDistanceMatrix();

            //saving the data
            this.saveData(pdata, dmat, null);
           
        } else {
            //projecting using points/corpus pre-processing
            matrix = MatrixFactory.getInstance(view, pdata);
            cdata = matrix.getClassData();
            ids = matrix.getIds();

            //normalization
            Normalization norm = NormalizationFactory.getInstance(pdata.getNormalization());
            matrix = norm.execute(matrix);

            //dimensionality reduction
            if (pdata.getDimensionReductionType() != DimensionalityReductionType.NONE) {
                if (matrix.getDimensions() > pdata.getTargetDimension()) {
                    this.view.setStatus("Reducing the dimensions to " +
                            pdata.getTargetDimension() + " dimensions...", 10);

                    DimensionalityReduction dr = DimensionalityReductionFactory.getInstance(pdata.getDimensionReductionType(), pdata.getTargetDimension());
                    Dissimilarity diss = DissimilarityFactory.getInstance(pdata.getDissimilarityType());
                    matrix = dr.reduce(matrix, diss);
                }
            }

            //saving the data
            this.saveData(pdata, null, matrix);

            proj = ProjectionFactory.getInstance(pdata.getProjectionType());
            projection = proj.project(matrix, pdata, view);

            pdata.setNumberObjects(matrix.getRowCount());
            pdata.setNumberDimensions(matrix.getDimensions());

            dmat = proj.getDistanceMatrix();

            //saving the data
            this.saveData(pdata, dmat, null);
        }

        //Creating the vertex list
        ArrayList<Vertex> vertex = new ArrayList<Vertex>();
        for (int i = 0; i < projection.length; i++) {
            Vertex v = new Vertex((long) i, projection[i][0], projection[i][1]);

            if (ids.size() > i) {
                v.setUrl(ids.get(i));
            } else {
                v.setUrl("");
            }

            vertex.add(v);
        }

        //Set the vertices
        graph.setVertex(vertex);

        if (pdata.getSourceType() == SourceType.CORPUS) {
            //setting the corpus
            Corpus cp = CorpusFactory.getInstance(pdata.getSourceFile(), pdata);
            graph.setCorpus(cp);

            //creating the titles from the corpus
            int title = graph.addTitle(PExConstants.TITLE);

            for (int i = 0; i < vertex.size(); i++) {
                if (ids.size() > i) {
                    String id = vertex.get(i).getUrl();
                    vertex.get(i).setTitle(title, cp.getTitle(pdata.getNumberLines(), id));
                } else {
                    vertex.get(i).setTitle(title, "");
                }
            }
        }

        //creating file names titles
        int fname = graph.addTitle(PExConstants.FNAME);

        for (Vertex v : vertex) {
            String id = v.getUrl();
            v.setTitle(fname, id);
        }

        //import other titles if available
        if (pdata.getTitlesFile() != null && pdata.getTitlesFile().trim().length() > 0) {
            Util.importTitles(graph, pdata.getTitlesFile());
        }

        //Storing the description of the graph
        graph.setDescription(pdata.getDescription());

        //creating the basic scalars
        Scalar sdots = graph.addScalar(PExConstants.DOTS);
        Scalar scdata = graph.addScalar(PExConstants.CDATA);

        for (int i = 0; i < projection.length; i++) {
            vertex.get(i).setScalar(sdots, 0.0f);

            if (cdata.length > i) {
                vertex.get(i).setScalar(scdata, cdata[i]);
            } else {
                vertex.get(i).setScalar(scdata, 0.0f);
            }
        }

        //creating scalars null vectors
        if (matrix != null) {
            //checking if there exist null vectors
            boolean hasNullVectors = false;
            for (int i = 0; i < matrix.getRowCount(); i++) {
                if (matrix.getRow(i).isNull()) {
                    hasNullVectors = true;
                    break;
                }
            }

            if (hasNullVectors) {
                Scalar snull = graph.addScalar(PExConstants.NULLV);

                for (int i = 0; i < projection.length; i++) {
                    if (matrix.getRowCount() > i && matrix.getRow(i).isNull()) {
                        vertex.get(i).setScalar(snull, 1.0f);
                    } else {
                        vertex.get(i).setScalar(snull, 0.0f);
                    }
                }
            }
        }

        //Creating the Hierarchical Clustering scalar
        if (pdata.getHierarchicalClusteringType() != HierarchicalClusteringType.NONE) {
            if (view != null) {
                this.view.setStatus("Creating the Hierarchical Clustering...", 85);
            }

            DenseMatrix dproj = new DenseMatrix();
            for (int i = 0; i < projection.length; i++) {
                dproj.addRow(new DenseVector(projection[i]));
            }

            HierarchicalClusteringType type = pdata.getHierarchicalClusteringType();
            float[] hcScalars = this.createHC(dproj, type);

            Scalar shc = null;
            if (type == HierarchicalClusteringType.ALINK) {
                shc = graph.addScalar(PExConstants.ALINK);
            } else if (type == HierarchicalClusteringType.CLINK) {
                shc = graph.addScalar(PExConstants.CLINK);
            } else if (type == HierarchicalClusteringType.SLINK) {
                shc = graph.addScalar(PExConstants.SLINK);
            }

            for (int i = 0; i < hcScalars.length; i++) {
                vertex.get(i).setScalar(shc, hcScalars[i]);
            }
        }

        //adding the connectivities
        if (view != null) {
            view.setStatus("Creating the connectivities...", 90);
        }

        //adding an empty connectivity
        Connectivity dotsCon = new Connectivity(PExConstants.DOTS);
        graph.addConnectivity(dotsCon);

        proj.createConnectivities(graph, pdata, view);

        //post processing
        proj.postProcessing(graph);

        //saving the titles if requested
        this.saveTitles(pdata, graph);

        view.setStatus("Done!", 100);
    }

    public void dispose() {
        this.dmat = null;
        this.matrix = null;
    }

    private float[] createHC(Matrix matrix, HierarchicalClusteringType type) throws IOException {
        HierarchicalClustering hc = new HierarchicalClustering(type);
        return hc.getPointsHeight(matrix, new Euclidean());
    }

    private void saveTitles(ProjectionData pdata, Graph graph) {
        //saving the titles
        if (pdata.getDocsTermsFilename().trim().length() > 0 ||
                pdata.getDistanceMatrixFilename().trim().length() > 0) {
            try {
                String filename = pdata.getDocsTermsFilename();
                if (filename.trim().length() == 0) {
                    filename = pdata.getDistanceMatrixFilename();
                }

                filename = filename.substring(0, filename.length() - 4) + "titles";
                Util.exportTitles(graph, filename);
            } catch (IOException ex) {
                Logger.getLogger(GraphBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void saveData(ProjectionData pdata, DistanceMatrix dmat, Matrix matrix) throws IOException {
        //saving the points matrix
        if (matrix != null && pdata.getDocsTermsFilename().trim().length() > 0) {
            String filename = pdata.getDocsTermsFilename();
            matrix.save(filename);
        }

        //saving distance matrix
        if (dmat != null && pdata.getDistanceMatrixFilename().trim().length() > 0) {
            String filename = pdata.getDistanceMatrixFilename();
            dmat.save(filename);
        }
    }

    private IOException exception;
    private Thread t;
    private Graph graph;
    private ProjectionView view;
    private DistanceMatrix dmat;
    private Matrix matrix;
}
