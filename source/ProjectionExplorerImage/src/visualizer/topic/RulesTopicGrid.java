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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.corpus.Corpus;
import visualizer.graph.Scalar;
import visualizer.graph.Vertex;
import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;
import visualizer.matrix.Matrix;
import visualizer.projection.distance.Euclidean;
import visualizer.textprocessing.transformation.MatrixTransformation;
import visualizer.textprocessing.transformation.MatrixTransformationFactory;
import visualizer.textprocessing.Preprocessor;
import visualizer.tools.apriori.ItemSet;
import visualizer.tools.apriori.ItemSetsWriter;
import visualizer.tools.apriori.RuleSet;
import visualizer.util.PExConstants;
import visualizer.datamining.clustering.Kmeans;
import visualizer.view.Viewer;
import visualizer.view.ProjectionExplorerView;
import visualizer.view.tools.RuleCoverOptions;

/**
 *
 * @author Fernando Vieira Paulovich, Roberto Pinho
 */
public class RulesTopicGrid {

    /** Creates a new instance of RulesTopicGrid */
    public RulesTopicGrid(Viewer panel, ProjectionExplorerView parent) {
        this.panel = panel;
        this.parent = parent;
    }

    public void execute() {
        if (this.panel.getGraph().getCorpus() != null) {
            RuleCoverOptions rulCovOpt = RuleCoverOptions.getInstance(parent).display();

            try {
                out = new PrintWriter(new FileWriter("dumpRuleMetaData.txt"));
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }

            TopicData tdata = this.panel.getGraph().getTopicData();

            //Choose CSV dump file

            javax.swing.JFileChooser file = new javax.swing.JFileChooser();
            file.resetChoosableFileFilters();
            file.setAcceptAllFileFilterUsed(false);
            file.setMultiSelectionEnabled(false);
            //file.setCurrentDirectory(new File("."));
            file.setSelectedFile(new File(tdata.getCsvFileName()));

            int result = file.showSaveDialog(parent);
            if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
                tdata.setCsvFileName(file.getSelectedFile().getAbsolutePath());
            }

            //end Choose CSV dump file

            //build VSM
            Corpus corpora = panel.getGraph().getCorpus();
            if (tdata.getMatrix() == null) {
                Preprocessor pp = new Preprocessor(corpora);
                try {
                    Matrix matrix = pp.getMatrix(tdata.getLunhLowerCut(),
                            tdata.getLunhUpperCut(), tdata.getNumberGrams(),
                            tdata.getStemmer(), tdata.isUseStopword());

                    MatrixTransformation transf = MatrixTransformationFactory.getInstance(tdata.getMatrixTransformationType());
                    matrix = transf.tranform(matrix, null);

                    tdata.setMatrix(matrix);
                    tdata.setCorporaNgrams(pp.getNgrams());
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }

            try {
                float[][] points = tdata.getMatrix().toMatrix();
                PrintWriter outPoints = new PrintWriter(
                        new FileWriter("pointsDump.txt"));
                for (int i = 0; i < points.length; i++) {
                    for (int j = 0; j < points[0].length; j++) {
                        outPoints.print(points[i][j] + "\t");

                    }
                    outPoints.println();
                }
                outPoints.close();
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }




            //Clean TermSet for Run
            tdata.getTermSetRun().clear();
            tdata.getTermSetRunW().clear();

            String allDump = "";
            Set<ItemSet> ruleSet = new HashSet<ItemSet>();
            Set<ItemSet> selectedRuleSet = new HashSet<ItemSet>();

            boolean useInc = true;


            //Topic Run
            if (rulCovOpt.getStrategy() == RuleCoverOptions.StrategyType.TOPIC_ST) { //using Clusters
                RemainderOverRuns = panel.getGraph().getVertex();
                runOverTopic(allDump, ruleSet, selectedRuleSet, 0);
            }




            //Cluster Run
            if (rulCovOpt.getStrategy() == RuleCoverOptions.StrategyType.CLUSTER_ST) { //using Clusters

                RemainderOverRuns = panel.getGraph().getVertex();
                System.out.println("Max Clusters:" + Math.sqrt((double) RemainderOverRuns.size()));
                int maxClusters = rulCovOpt.getMax(); //(int)Math.sqrt((double)RemainderOverRuns.size());
                int nclusters = rulCovOpt.getMin();
                int incClusters = rulCovOpt.getInc(); //(maxClusters-nclusters)/3;

                //if(incClusters==0) incClusters = 1;

                int currentRun = 0;

                if (incClusters > 0) {
                    for (nclusters = rulCovOpt.getMin(); nclusters <= maxClusters; nclusters += incClusters) {
                        System.out.println("%%%%%%%%%%%N. Clusters:" + nclusters);
                        runOverClusters(nclusters, allDump, ruleSet, selectedRuleSet, 0, 0, currentRun++);

                    }
                } else if (incClusters < 0) {
                    for (nclusters = rulCovOpt.getMin(); nclusters >= maxClusters; nclusters += incClusters) {
                        System.out.println("%%%%%%%%%%%N. Clusters:" + nclusters);
                        runOverClusters(nclusters, allDump, ruleSet, selectedRuleSet, 0, 0, currentRun++);
                    }
                } else {  // Run same number of clusters ntimes
                    nclusters = rulCovOpt.getMin();

                    for (int count = 0; count < maxClusters; count++) {
                        System.out.println("%%%%%%%%%%%N. Clusters:" + nclusters + "Run:" + count);
                        runOverClusters(nclusters, allDump, ruleSet, selectedRuleSet, 0, 0, currentRun++);

                    }
                }

            }




            //Resize Run
            if (rulCovOpt.getStrategy() == RuleCoverOptions.StrategyType.RESIZE_ST) { //Multiple Restarts - Resize Grid
                RemainderOverRuns = panel.getGraph().getVertex();
                for (int slices = rulCovOpt.getMin(); slices <= rulCovOpt.getMax(); slices++) {
                    if (useInc) {
                        runOverGridIncCover(slices, allDump, ruleSet, selectedRuleSet, 0, 0);
                    } else {
                        runOverGrid(slices, allDump, ruleSet, selectedRuleSet, 0, 0);
                    }

                } //End Multiple Restart Loop
            }

            //Cell #,Cell I, Cell J,
            //Selected Docs
            //Generated Rules, New Rules Over Runs
            //Covered Docs Local, Covered Docs Accum.
            //Covered Docs Over Runs Accum.


            //Move/Slide Run
            if (rulCovOpt.getStrategy() == RuleCoverOptions.StrategyType.MOVE_ST) { //Multiple Restarts -  Move Grid

                int slices = rulCovOpt.getSlices();

                int subSlices = rulCovOpt.getSubSlices();

                int xSubSlice = (int) (panel.getSize().getWidth() / slices / (1.0 * subSlices));
                int ySubSlice = (int) (panel.getSize().getHeight() / slices / (1.0 * subSlices));
                int xDelta, yDelta;
                RemainderOverRuns = panel.getGraph().getVertex();
                for (int i = 0; i < subSlices; i++) {
                    //out.print(i + ","); //Run
                    xDelta = -i * xSubSlice;
                    yDelta = -i * ySubSlice;
                    if (useInc) {
                        runOverGridIncCover(slices, allDump, ruleSet, selectedRuleSet, xDelta, yDelta);
                    } else {
                        runOverGrid(slices, allDump, ruleSet, selectedRuleSet, xDelta, yDelta);
                    }
                }


            } //End Multiple Restart Loop

            out.close();

            System.out.println("All Rules:" + ruleSet.size());
            System.out.println("Selected Rules:" + selectedRuleSet.size());
            List selectedRuleSetList = new ArrayList();
            selectedRuleSetList.addAll(selectedRuleSet);
            ItemSetsWriter.outRulesDocs("SelectedRules",
                    this.panel.getGraph().getTopicData().getMatrix().toMatrix(),
                    selectedRuleSetList, panel.getGraph().getCorpus());
            ItemSetsWriter.outRulesNames("SelectedRules", selectedRuleSetList);

        }


    }

    private ArrayList<ArrayList<Integer>> createClusters(int nclusters) {
        //Create the clusters
        float[][] points = new float[this.panel.getGraph().getVertex().size()][];

        Matrix matrix = new DenseMatrix();
        for (int i = 0; i < points.length; i++) {
            float point[] = new float[2];
            point[0] = this.panel.getGraph().getVertex().get(i).getX();
            point[1] = this.panel.getGraph().getVertex().get(i).getY();
            matrix.addRow(new DenseVector(point));
        }

        ArrayList<ArrayList<Integer>> clusters;

        try {
            Kmeans km = new Kmeans(nclusters);
            clusters = km.execute(new Euclidean(), matrix);
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        Scalar s = this.panel.getGraph().addScalar(PExConstants.KMEANS + clusters.size());
        for (int c = 0; c < clusters.size(); c++) {
            for (int v = 0; v < clusters.get(c).size(); v++) {
                this.panel.getGraph().getVertex().get(clusters.get(c).get(v)).setScalar(s, c);
            }
        }

        this.panel.updateScalars(null);

        return clusters;
    }

    private void runOverClusters(int nclusters, String allDump, Set<ItemSet> ruleSet,
            Set<ItemSet> selectedRuleSet, int xDelta, int yDelta, int currentRun) {
        ArrayList<ArrayList<Integer>> clusters = createClusters(nclusters);

        HashMap<String, Integer> pointLine = new HashMap<String, Integer>();
        ArrayList<Vertex> vertex = panel.getGraph().getVertex();
        ArrayList<Vertex> RemainderVertex = vertex;


        Corpus corpora = panel.getGraph().getCorpus();
        float[][] points;
        for (int i = 0; i < corpora.getIds().size(); i++) {
            pointLine.put(corpora.getIds().get(i), new Integer(i));
        }

        //begin loop
        for (int c = 0; c < clusters.size(); c++) {


            out.print(c + "," + c + "," + 0 + ","); // Cell #,i,j

            ArrayList<Vertex> selectedVertices = new ArrayList<Vertex>();

            for (int v = 0; v < clusters.get(c).size(); v++) {
                selectedVertices.add(this.panel.getGraph().getVertex().get(clusters.get(c).get(v)));
            }

            out.print(selectedVertices.size() + ","); //Selected Vertices

            if (selectedVertices.size() == 0) {
                out.println();
                continue;
            }
            TopicData ldata = this.panel.getGraph().getTopicData();

            //multiple re-start statistics
            ldata.setCurrentRun(currentRun);
            ldata.setCurrentSelection(c);

            RuleTopic ruleTopic = new RuleTopic(selectedVertices, panel.getGraph().getCorpus(), ldata);
            panel.addTopic(ruleTopic);



            ruleSet.addAll(ruleTopic.getItemSets());

            int sizeBefore = selectedRuleSet.size();
            selectedRuleSet.addAll(ruleTopic.getSelectedItemSets());

            out.print(ruleTopic.getSelectedItemSets().size() + ","); // Generated Rules
            out.print((sizeBefore - selectedRuleSet.size()) + ","); // New Rules
            out.print((selectedVertices.size() -
                    ruleTopic.getVertexRemainder().size()) + ",");// Covered Docs Local



            allDump += "==========\n" + ruleTopic.ruleDump;

            if (ruleTopic.getSelectedItemSets().size() == 0) {
                out.println();
                continue;
            }
            //find Remainder set
            points = ldata.getMatrix().toMatrix();
            int lineIndex;


            ArrayList<Vertex> PreviousVertex = RemainderVertex;
            RemainderVertex = new ArrayList<Vertex>();

            lineIndex = -1;
            for (int m = 0; m < PreviousVertex.size(); m++) { // For each Vertex
                Vertex v = PreviousVertex.get(m);
                if (corpora.getIds().get((int) v.getId()).equals(v.getUrl())) {
                    lineIndex = (int) v.getId();

                } else {
                    for (int k = 0; k < corpora.getIds().size(); k++) {
                        if (corpora.getIds().get(k).equals(v.getUrl())) {
                            lineIndex = k;
                            break;
                        }
                    }
                }
                boolean covered = false;
                for (ItemSet iS : ruleTopic.getSelectedItemSets()) { //For each Item Set
                    boolean found = true;
                    for (Integer I : iS.getItems()) {
                        found = found && (points[lineIndex][I.intValue()] > 0 ? true : false);
                    }
                    if (found) {
                        covered = true;
                        break;
                    }
                } //End For each Item Set
                if (!covered) {
                    RemainderVertex.add(v);
                }
            } // End For each Vertex
            System.out.println("==========\nVertex Size:" + vertex.size());
            System.out.println("Selected Size:" + selectedVertices.size());
            System.out.println("Previous Size:" + PreviousVertex.size());
            System.out.println("Remainder Size:" + RemainderVertex.size() + "\n==========");
            out.print((vertex.size() - RemainderVertex.size()) + ",");//Covered Docs Accum.

            //Count Accum. Coverage Over Runs
            int coveredCount = 0;
            lineIndex = -1;
            ArrayList<Vertex> PreviousOverRuns = RemainderOverRuns;
            RemainderOverRuns = new ArrayList<Vertex>();
            for (int m = 0; m < PreviousOverRuns.size(); m++) { // For each Vertex
                Vertex v = PreviousOverRuns.get(m);
                if (corpora.getIds().get((int) v.getId()).equals(v.getUrl())) {
                    lineIndex = (int) v.getId();

                } else {
                    for (int k = 0; k < corpora.getIds().size(); k++) {
                        if (corpora.getIds().get(k).equals(v.getUrl())) {
                            lineIndex = k;
                            break;
                        }
                    }
                }
                boolean covered = false;
                for (ItemSet iS : ruleTopic.getSelectedItemSets()) { //For each Item Set
                    boolean found = true;
                    for (Integer I : iS.getItems()) {
                        found = found && (points[lineIndex][I.intValue()] > 0 ? true : false);
                    }
                    if (found) {
                        covered = true;
                        break;
                    }
                } //End For each Item Set
                if (!covered) {
                    RemainderOverRuns.add(v);
                }
            } // End For each Vertex

            out.println(RemainderOverRuns.size());//Covered Docs Over Runs Accum.
            PreviousVertex = null;
        } //end cluster loop

    }

    private void runOverGrid(int slices, String allDump, Set<ItemSet> ruleSet,
            Set<ItemSet> selectedRuleSet, int xDelta, int yDelta) {
        int xSize = (int) panel.getSize().getWidth() / slices;
        int ySize = (int) panel.getSize().getHeight() / slices;
        java.awt.Point source = null;
        java.awt.Point target = null;



        //begin loop
        for (int i = 0; i < slices; i++) {
            for (int j = 0; j < slices; j++) {



                source = new java.awt.Point(i * xSize + xDelta, j * ySize + yDelta);
                target = new java.awt.Point((i + 1) * xSize - 1 + xDelta, (j + 1) * ySize - 1 + yDelta);
                System.out.println(i + "," + j + ": Source" + source + "\tTarget:" + target);
                java.awt.Rectangle subPos = new java.awt.Rectangle();
                ArrayList<Vertex> selectedVertices = panel.getSelectedVertex(source, target);
                System.out.println("Vertices:" + selectedVertices.size());
                if (selectedVertices.size() == 0) {
                    continue;
                }

                TopicData ldata = this.panel.getGraph().getTopicData();

                RuleTopic ruleTopic = new RuleTopic(selectedVertices, panel.getGraph().getCorpus(), ldata);
                panel.addTopic(ruleTopic);

                ruleSet.addAll(ruleTopic.getItemSets());
                selectedRuleSet.addAll(ruleTopic.getSelectedItemSets());
                allDump += "==========\n" + ruleTopic.ruleDump;


            }
        } //End slices loop
    }

    private void runOverTopic(String allDump, Set<ItemSet> ruleSet,
            Set<ItemSet> selectedRuleSet, int currentRun) {


        float[][] points;


        HashMap<String, Integer> pointLine = new HashMap<String, Integer>();
        ArrayList<Vertex> vertex = panel.getGraph().getVertex();
        ArrayList<Vertex> RemainderVertex = vertex;


        Corpus corpora = panel.getGraph().getCorpus();
        //float[][] points;
        //for(int i=0; i < corpora.getIds().size(); i++) {
        //    pointLine.put(corpora.getIds().get(i),new Integer(i));
        //}

        Scalar s = this.panel.getGraph().getScalarByName("TOPIC");

        Set<Float> scalarValueSet = new HashSet<Float>();

        for (int v = 0; v < this.panel.getGraph().getVertex().size(); v++) {
            Float f = this.panel.getGraph().getVertex().get(v).getScalar(s);
            scalarValueSet.add(f);
        }
        int c = 0;
        //begin loop
        for (Float scalarValue : scalarValueSet) {
            c++;

            out.print("::" + scalarValue + "::");


            //out.print(c+","+c+","+0+","); // Cell #,i,j

            ArrayList<Vertex> selectedVertices = new ArrayList<Vertex>();

            //change here
            for (int v = 0; v < this.panel.getGraph().getVertex().size(); v++) {
                if (this.panel.getGraph().getVertex().get(v).getScalar(s) == scalarValue) {
                    selectedVertices.add(this.panel.getGraph().getVertex().get(v));
                }
            }

            if (!ducOut) {
                out.print(selectedVertices.size() + ",");
            } //Selected Vertices

            if (selectedVertices.size() == 0) {
                out.println();
                continue;
            }
            TopicData ldata = this.panel.getGraph().getTopicData();

            //multiple re-start statistics
            ldata.setCurrentRun(currentRun);
            ldata.setCurrentSelection(c);

            RuleTopic ruleTopic = (RuleTopic) TopicFactory.getInstance(panel.getGraph(), ldata, panel.getGraph().getCorpus(), selectedVertices);



            ruleSet.addAll(ruleTopic.getItemSets());

            int sizeBefore = selectedRuleSet.size();
            selectedRuleSet.addAll(ruleTopic.getSelectedItemSets());
            if (!(ducOut)) {
                out.print(ruleTopic.getSelectedItemSets().size() + ","); // Generated Rules
                out.print((sizeBefore - selectedRuleSet.size()) + ","); // New Rules
                out.print((selectedVertices.size() -
                        ruleTopic.getVertexRemainder().size()) + ",");// Covered Docs Local

            }

            allDump += "==========\n" + ruleTopic.ruleDump;

            if (ruleTopic.getSelectedItemSets().size() == 0) {
                out.println();
                continue;
            }



            if (ducOut) {
                File dirROUGE = new File("C:/Users/robertopinho/Documents/edu_what/research_what/corpora_what/DUC_where/temp");
                String rougeFileName = "D" + (scalarValue.intValue()) + ".M.100.T.202";
                TopicData shortTopic = new TopicData(null);
                shortTopic.setRuleTopic(false);
                shortTopic.setShortPhrase(true);
                try {
                    PrintWriter outROUGE = new PrintWriter(new File(dirROUGE, rougeFileName));
                    for (StringBox box : ruleTopic.boxes) {
                        out.println(box.getMsg());
                        outROUGE.println(box.getMsg());
                        RuleSet rs = (RuleSet) box.getUserObject();
                        for (ItemSet is : rs.rules) {
                            outROUGE.println(is.getTopic(shortTopic));
                        }
                    }
                    outROUGE.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }


            }

            //Add to panel
            panel.addTopic(ruleTopic);


            //find Remainder set
            points = ldata.getMatrix().toMatrix();
            int lineIndex;


            ArrayList<Vertex> PreviousVertex = RemainderVertex;
            RemainderVertex = new ArrayList<Vertex>();

            lineIndex = -1;
            for (int m = 0; m < PreviousVertex.size(); m++) { // For each Vertex
                Vertex v = PreviousVertex.get(m);
                if (corpora.getIds().get((int) v.getId()).equals(v.getUrl())) {
                    lineIndex = (int) v.getId();

                } else {
                    for (int k = 0; k < corpora.getIds().size(); k++) {
                        if (corpora.getIds().get(k).equals(v.getUrl())) {
                            lineIndex = k;
                            break;
                        }
                    }
                }
                boolean covered = false;
                for (ItemSet iS : ruleTopic.getSelectedItemSets()) { //For each Item Set
                    boolean found = true;
                    for (Integer I : iS.getItems()) {
                        found = found && (points[lineIndex][I.intValue()] > 0 ? true : false);
                    }
                    if (found) {
                        covered = true;
                        break;
                    }
                } //End For each Item Set
                if (!covered) {
                    RemainderVertex.add(v);
                }
            } // End For each Vertex
            System.out.println("==========\nVertex Size:" + vertex.size());
            System.out.println("Selected Size:" + selectedVertices.size());
            System.out.println("Previous Size:" + PreviousVertex.size());
            System.out.println("Remainder Size:" + RemainderVertex.size() + "\n==========");
            if (!ducOut) {
                out.print((vertex.size() - RemainderVertex.size()) + ",");
            }//Covered Docs Accum.

            //Count Accum. Coverage Over Runs
            int coveredCount = 0;
            lineIndex = -1;
            ArrayList<Vertex> PreviousOverRuns = RemainderOverRuns;
            RemainderOverRuns = new ArrayList<Vertex>();
            for (int m = 0; m < PreviousOverRuns.size(); m++) { // For each Vertex
                Vertex v = PreviousOverRuns.get(m);
                if (corpora.getIds().get((int) v.getId()).equals(v.getUrl())) {
                    lineIndex = (int) v.getId();

                } else {
                    for (int k = 0; k < corpora.getIds().size(); k++) {
                        if (corpora.getIds().get(k).equals(v.getUrl())) {
                            lineIndex = k;
                            break;
                        }
                    }
                }
                boolean covered = false;
                for (ItemSet iS : ruleTopic.getSelectedItemSets()) { //For each Item Set
                    boolean found = true;
                    for (Integer I : iS.getItems()) {
                        found = found && (points[lineIndex][I.intValue()] > 0 ? true : false);
                    }
                    if (found) {
                        covered = true;
                        break;
                    }
                } //End For each Item Set
                if (!covered) {
                    RemainderOverRuns.add(v);
                }
            } // End For each Vertex

            if (!ducOut) {
                out.println(RemainderOverRuns.size());
            }//Covered Docs Over Runs Accum.
            PreviousVertex = null;
        } //end cluster loop

    }

    private void runOverGridIncCover(int slices, String allDump, Set<ItemSet> ruleSet,
            Set<ItemSet> selectedRuleSet, int xDelta, int yDelta) {
        int xSize = (int) panel.getSize().getWidth() / slices;
        int ySize = (int) panel.getSize().getHeight() / slices;
        java.awt.Point source = null;
        java.awt.Point target = null;

        ArrayList<Vertex> vertex = panel.getGraph().getVertex();
        ArrayList<Vertex> RemainderVertex = vertex;

        HashMap<String, Integer> pointLine = new HashMap<String, Integer>();


        Corpus corpora = panel.getGraph().getCorpus();
        float[][] points;
        for (int i = 0; i < corpora.getIds().size(); i++) {
            pointLine.put(corpora.getIds().get(i), new Integer(i));
        }

        //begin loop
        for (int i = 0; i < slices; i++) {
            for (int j = 0; j < slices; j++) {

                out.print((i * slices + j) + "," + i + "," + j + ","); // Cell #,i,j

                source = new java.awt.Point(i * xSize + xDelta, j * ySize + yDelta);
                target = new java.awt.Point((i + 1) * xSize - 1 + xDelta, (j + 1) * ySize - 1 + yDelta);
                System.out.println(i + "," + j + ": Source" + source + "\tTarget:" + target);
                java.awt.Rectangle subPos = new java.awt.Rectangle();

                ArrayList<Vertex> selectedVertices = getSelectedVertex(RemainderVertex, source, target);

                out.print(selectedVertices.size() + ","); //Selected Vertices




                System.out.println("Vertices:" + selectedVertices.size());
                if (selectedVertices.size() == 0) {
                    out.println();
                    continue;
                }

                TopicData ldata = this.panel.getGraph().getTopicData();

                RuleTopic ruleTopic = new RuleTopic(selectedVertices, panel.getGraph().getCorpus(), ldata);
                panel.addTopic(ruleTopic);



                ruleSet.addAll(ruleTopic.getItemSets());
                int sizeBefore = selectedRuleSet.size();
                selectedRuleSet.addAll(ruleTopic.getSelectedItemSets());

                out.print(ruleTopic.getSelectedItemSets().size() + ","); // Generated Rules
                out.print((sizeBefore - selectedRuleSet.size()) + ","); // New Rules
                out.print((selectedVertices.size() -
                        ruleTopic.getVertexRemainder().size()) + ",");// Covered Docs Local



                allDump += "==========\n" + ruleTopic.ruleDump;

                if (ruleTopic.getSelectedItemSets().size() == 0) {
                    out.println();
                    continue;
                }

                //find Remainder set
                points = ldata.getMatrix().toMatrix();
                int lineIndex;


                ArrayList<Vertex> PreviousVertex = RemainderVertex;
                RemainderVertex = new ArrayList<Vertex>();

                lineIndex = -1;
                for (int m = 0; m < PreviousVertex.size(); m++) { // For each Vertex
                    Vertex v = PreviousVertex.get(m);
                    if (corpora.getIds().get((int) v.getId()).equals(v.getUrl())) {
                        lineIndex = (int) v.getId();

                    } else {
                        for (int k = 0; k < corpora.getIds().size(); k++) {
                            if (corpora.getIds().get(k).equals(v.getUrl())) {
                                lineIndex = k;
                                break;
                            }
                        }
                    }
                    boolean covered = false;
                    for (ItemSet iS : ruleTopic.getSelectedItemSets()) { //For each Item Set
                        boolean found = true;
                        for (Integer I : iS.getItems()) {
                            found = found && (points[lineIndex][I.intValue()] > 0 ? true : false);
                        }
                        if (found) {
                            covered = true;
                            break;
                        }
                    } //End For each Item Set
                    if (!covered) {
                        RemainderVertex.add(v);
                    }
                } // End For each Vertex
                System.out.println("==========\nVertex Size:" + vertex.size());
                System.out.println("Selected Size:" + selectedVertices.size());
                System.out.println("Previous Size:" + PreviousVertex.size());
                System.out.println("Remainder Size:" + RemainderVertex.size() + "\n==========");
                out.print((vertex.size() - RemainderVertex.size()) + ",");//Covered Docs Accum.

                //Count Accum. Coverage Over Runs
                int coveredCount = 0;
                lineIndex = -1;
                ArrayList<Vertex> PreviousOverRuns = RemainderOverRuns;
                RemainderOverRuns = new ArrayList<Vertex>();
                for (int m = 0; m < PreviousOverRuns.size(); m++) { // For each Vertex
                    Vertex v = PreviousOverRuns.get(m);
                    if (corpora.getIds().get((int) v.getId()).equals(v.getUrl())) {
                        lineIndex = (int) v.getId();

                    } else {
                        for (int k = 0; k < corpora.getIds().size(); k++) {
                            if (corpora.getIds().get(k).equals(v.getUrl())) {
                                lineIndex = k;
                                break;
                            }
                        }
                    }
                    boolean covered = false;
                    for (ItemSet iS : ruleTopic.getSelectedItemSets()) { //For each Item Set
                        boolean found = true;
                        for (Integer I : iS.getItems()) {
                            found = found && (points[lineIndex][I.intValue()] > 0 ? true : false);
                        }
                        if (found) {
                            covered = true;
                            break;
                        }
                    } //End For each Item Set
                    if (!covered) {
                        RemainderOverRuns.add(v);
                    }
                } // End For each Vertex

                out.println(RemainderOverRuns.size());//Covered Docs Over Runs Accum.
                PreviousVertex = null;
            }
        } //End slices loop
    }

    private ArrayList<Vertex> getSelectedVertex(ArrayList<Vertex> vertex,
            java.awt.Point localSource, java.awt.Point localTarget) {
        ArrayList<Vertex> selectedVertex = new ArrayList<Vertex>();

        int x = localSource.x;
        int width = localTarget.x - localSource.x;

        int y = localSource.y;
        int height = localTarget.y - localSource.y;

        if (localSource.x > localTarget.x) {
            x = localTarget.x;
            width = localSource.x - localTarget.x;
        }

        if (localSource.y > localTarget.y) {
            y = localTarget.y;
            height = localSource.y - localTarget.y;
        }

        java.awt.Rectangle rect = new java.awt.Rectangle(x, y, width, height);

        for (Vertex v : vertex) {
            if (v.isInside(rect)) {
                selectedVertex.add(v);
            }
        }
        return selectedVertex;
    }

    private PrintWriter out;
    private boolean ducOut = true;
    private ArrayList<Vertex> RemainderOverRuns;
    private Viewer panel;
    private ProjectionExplorerView parent;
}
