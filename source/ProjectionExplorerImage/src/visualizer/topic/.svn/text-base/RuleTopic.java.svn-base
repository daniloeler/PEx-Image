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
 * of the original code is Roberto Pinho <robertopinho@yahoo.com.br>, 
 * Fernando Vieira Paulovich <fpaulovich@gmail.com>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.topic;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import visualizer.corpus.Corpus;
import visualizer.graph.Vertex;
import visualizer.matrix.Matrix;
import visualizer.textprocessing.transformation.MatrixTransformation;
import visualizer.textprocessing.transformation.MatrixTransformationFactory;
import visualizer.textprocessing.Ngram;
import visualizer.textprocessing.Preprocessor;
import visualizer.textprocessing.stemmer.StemmerType;
import visualizer.tools.apriori.ItemSet;
import visualizer.tools.apriori.RuleSet;
import visualizer.tools.apriori.SelectedApriori;

/**
 *
 * @author robertopinho e Fernando Vieira Paulovich
 */
public class RuleTopic extends Topic {

    /** Creates a new instance of RuleTopic
     * @param vertex
     * @param corpus
     * @param tdata 
     */
    public RuleTopic(ArrayList<Vertex> vertex, Corpus corpus, TopicData tdata) {
        super(vertex);
        this.tdata = tdata;

        //removing invalid vertices
        for (int i = 0; i < vertex.size(); i++) {
            if (!vertex.get(i).isValid()) {
                vertex.remove(i);
                i--;
            }
        }

        this.createTopic(vertex, corpus);
    }

    @Override
    protected void createTopic(ArrayList<Vertex> vertex, Corpus corpus) {
        float[][] points;
        float[][] selectedPoints;
        ArrayList<Ngram> corporaNgrams = null;

        try {
            //Get Matrix
            //should change to use inverted files
            if (tdata.getMatrix() == null) {
                Preprocessor pp = new Preprocessor(corpus);
                Matrix matrix = pp.getMatrix(tdata.getLunhLowerCut(),
                        tdata.getLunhUpperCut(), tdata.getNumberGrams(),
                        tdata.getStemmer(), tdata.isUseStopword());

                MatrixTransformation transf = MatrixTransformationFactory.getInstance(tdata.getMatrixTransformationType());
                matrix = transf.tranform(matrix, null);

                corporaNgrams = pp.getNgrams();
                points = matrix.toMatrix();
                tdata.setMatrix(matrix);
                tdata.setCorporaNgrams(corporaNgrams);
            } else {
                points = tdata.getMatrix().toMatrix();
                corporaNgrams = tdata.getCorporaNgrams();
            }

            tdata.setCorpus(corpus);


            ruleSetList = null;
            if (tdata.isGroupTopics()) {
                ruleSetList = new ArrayList<RuleSet>();
            }

            //Loop for Remainder Covering
            ArrayList<Vertex> vertexUsed = new ArrayList<Vertex>();
            ArrayList<Vertex> relatedVertex = new ArrayList<Vertex>();

            this.itemSets = new ArrayList<ItemSet>();
            this.selectedItemSets = new ArrayList<ItemSet>();
            System.out.println("**********Vertex: " + vertex.size());
            while (vertex != null && vertex.size() > 0) {

                selectedPoints = new float[vertex.size()][];
                int[] vertexIndex = new int[vertex.size()];

                //For each vertex
                // Find line in Matrix for each Vertex
                int i = 0;
                for (int j = 0; j < vertex.size(); j++) {
                    Vertex v = vertex.get(j);

                    if (corpus.getIds().get((int) v.getId()).equals(v.getUrl())) {
                        selectedPoints[i] = points[(int) v.getId()];
                        vertexIndex[i] = j;
                    } else {
                        System.err.println("Vertex not alligned to points matrix");
                        for (int k = 0; k < corpus.getIds().size(); k++) {
                            if (corpus.getIds().get(k).equals(v.getUrl())) {
                                selectedPoints[i] = points[k];
                                vertexIndex[i] = j;
                                break;
                            }
                        }
                    }
                    i++;
                }

                Object[] ngrams = new Object[corporaNgrams.size()];
                for (int k = 0; k < corporaNgrams.size(); k++) {
                    ngrams[k] = corporaNgrams.get(k).ngram;
                }

                SelectedApriori sApriori = new SelectedApriori(selectedPoints, points, ngrams);
                sApriori.setRuleSetList(ruleSetList);
                //sApriori.setMinSup(tdata.getMinSup());
                sApriori.setMinConf(90.0f);
                sApriori.setBeta(tdata.getWeightBeta().floatValue());
                sApriori.setLdata(this.tdata);
                sApriori.run();
                //Accumulates TermSet
                tdata.getTermSetAccum().addAll(sApriori.getTermSet());
                tdata.getTermSetRun().addAll(sApriori.getTermSet());
                tdata.getTermSetAccumW().addAll(sApriori.getTermSetW());
                tdata.getTermSetRunW().addAll(sApriori.getTermSetW());

                //build Remainder vertex list
                vertexRemainder = new ArrayList<Vertex>();
                this.itemSets.addAll(sApriori.getItemSets());
                this.selectedItemSets.addAll(sApriori.getSelectedItemSets());
                this.ruleDump = sApriori.ruleDump;

                for (int j = 0; j < sApriori.coveredPoints.length; j++) {
                    if (!sApriori.coveredPoints[j]) {
                        vertexRemainder.add(vertex.get(vertexIndex[j]));
                    } else {
                        vertexUsed.add(vertex.get(vertexIndex[j]));

                    }
                }




                if (sApriori.getSelectedItemSets().size() > 0) {
                    vertex = vertexRemainder;
                    System.out.println("**********Remainder: " + vertexRemainder.size());
                } else {
                    vertex = null;
                }
            }


            //Select Topic type
            if (!tdata.isGroupTopics()) {
                this.setRuleGroup(false);
                //Normal Topic
                for (int k = 0; k < this.selectedItemSets.size(); k++) {
                    String msg = this.selectedItemSets.get(k).getTopic(tdata);

                    ArrayList<Vertex> relatedVertices = new ArrayList<Vertex>();

                    for (Integer index : this.selectedItemSets.get(k).getGlobalCoveredAllPoints()) {
                        relatedVertices.add(getVertexbyId(index));

                    }
                    this.selectedItemSets.get(k).setRelatedVertices(relatedVertices);
                    if (tdata.getStemmer() != StemmerType.NONE) {
                        msg = msg.replaceAll("<>", "");
                    }
                    StringBox box = new StringBox(msg, relatedVertices, this.selectedItemSets.get(k));

                    this.boxes.add(box);
                    relatedVertex.addAll(relatedVertices);

                }
            } else {

                this.setRuleGroup(true);
                //RuleSet Topic
                for (int k = 0; k < ruleSetList.size(); k++) {
                    //if(tdata.isPhrase()|| tdata.isShortPhrase()){
                    ruleSetList.get(k).computeStrongPhrase(points, tdata, true);
                    ruleSetList.get(k).computeStrongPhrase(points, tdata, false);
                    //}



                    String msg = ruleSetList.get(k).getTopic(tdata);

                    ArrayList<Vertex> relatedVertices = new ArrayList<Vertex>();

                    for (int i = 0; i < ruleSetList.get(k).coveredPoints.length; i++) {
                        if (ruleSetList.get(k).coveredPoints[i]) {
                            relatedVertices.add(getVertexbyId(i));
                        }
                    }

                    ruleSetList.get(k).setRelatedVertices(relatedVertices);

                    if (tdata.getStemmer() != StemmerType.NONE) {
                        msg = msg.replaceAll("<>", "");
                    }
                    StringBox box = new StringBox(msg, relatedVertices, ruleSetList.get(k));
                    this.boxes.add(box);
                    relatedVertex.addAll(relatedVertices);
                //tdata.getTermSetAccum().addAll(ruleSetList.get(k).getTermSet());
                }
            }



        //this.calculateRectangle(relatedVertex);


        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getQuery() {
        if (this.boxes.size() < 1) {
            return "";
        }
        try {
            String query = "";
            Pattern p = Pattern.compile("\\[.*?\\]");

            Matcher m = p.matcher(this.boxes.get(0).getMsg());
            while (m.find()) {
                query = query + " and " + m.group().replaceAll("\\[|\\]", "");
            }
            if (!query.equals("")) {
                query = query.replaceFirst(" and ", "");
                query = query.replaceAll("<>", "");
                return query;
            } else {
                return null;
            }

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<ItemSet> getItemSets() {
        return itemSets;
    }

    public void setItemSets(List<ItemSet> itemSets) {
        this.itemSets = itemSets;
    }

    public List<ItemSet> getSelectedItemSets() {
        return selectedItemSets;
    }

    public void setSelectedItemSets(List<ItemSet> selectedItemSets) {
        this.selectedItemSets = selectedItemSets;
    }

    public ArrayList<Vertex> getVertexRemainder() {
        return vertexRemainder;
    }

    @Override
    public void drawTopic(Graphics g, java.awt.Font font, boolean selected) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;

        g.setFont(font);
        java.awt.FontMetrics metrics = g.getFontMetrics(g.getFont());

        g2.setStroke(new BasicStroke(1.3f));
        //g2.setColor(java.awt.Color.BLACK);
        g2.setColor(java.awt.Color.GRAY);
        g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));

        g2.drawRect(this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);

        g2.setStroke(new BasicStroke(1.0f));

        if (selected || showTopics || showThisTopic) {
            if (selected) {
                g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.2f));
                g2.setPaint(java.awt.Color.BLUE);
                g.fillRect(this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
            }

            //draw the first label
            if (this.boxes.size() > 0) {
                java.awt.Point position = new java.awt.Point();


                position.x = this.rectangle.x + this.rectangle.width;
                position.y = this.rectangle.y;
                java.awt.Rectangle rect = drawBox(g2, font, position, this.boxes.get(0), selected);
                //draw all the other ones
                for (int i = 1; i < this.boxes.size(); i++) {
                    position = new java.awt.Point();
                    position.x = this.rectangle.x + this.rectangle.width;
                    position.y = rect.y + rect.height + 6;
                    rect = drawBox(g2, font, position, this.boxes.get(i), selected);

                }
            }
        }
    }

    private Rectangle drawBox(Graphics2D g2, java.awt.Font font, java.awt.Point position, StringBox box, boolean selected) {
        Rectangle rect;
        Color c;
        if (selected || box == this.selectedBox) {
            rect = box.draw(g2, position, font, true);
            c = Color.BLUE;
            for (Vertex v : box.getRelatedVertices()) {
                v.setSelected(true);
                v.draw(g2, false);
            }
        } else {
            rect = box.draw(g2, position, font, false);
            c = Color.GREEN;

        }
        java.awt.Point setPosition;
        setPosition = calcVertexSetPosition(box.getRelatedVertices());
        drawDiamond(g2, setPosition, c);
        drawTopicLine(g2, position, setPosition, c);
        return rect;
    }

    private Vertex getVertexbyId(Integer index) {

        for (Vertex v : tdata.getGraph().getVertex()) {
            if (v.getId() == index) {
                return v;
            }
        }

        return null;
    }

    private void drawDiamond(Graphics2D g2, Point p, Color color) {

        float halfSize = 2.0f;

        int x[] = new int[4];
        int y[] = new int[4];
        x[0] = (int) (p.x - halfSize);
        x[1] = p.x;
        x[2] = (int) (p.x + halfSize);
        x[3] = p.x;

        y[0] = p.y;
        y[1] = (int) (p.y - halfSize);
        y[2] = p.y;
        y[3] = (int) (p.y + halfSize);


        g2.setColor(color);
        g2.drawPolygon(x, y, 4);


    }

    private Point calcVertexSetPosition(List<Vertex> vertex) {
        Point p = new java.awt.Point();

        p.x = 0;
        p.y = 0;

        for (Vertex v : vertex) {
            p.x += (int) v.getX();
            p.y += (int) v.getY();
        }
        p.x = (int) (p.x / (1f * vertex.size()));
        p.y = (int) (p.y / (1f * vertex.size()));

        return p;
    }

    private void drawTopicLine(Graphics2D g2, Point p1, Point p2, Color color) {
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL, 10, new float[]{9}, 0f));
        g2.setColor(color);
        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
        g2.setStroke(new BasicStroke());
    }

    @Override
    public boolean isBoxInside(Point point) {
        for (StringBox box : this.boxes) {
            if (box.isInside(point)) {
                this.selectedBox = box;
                return true;
            }
        }

        return false;
    }

    public TopicData getTopicData() {
        return tdata;
    }

    public boolean isRuleGroup() {
        return ruleGroup;
    }

    public void setRuleGroup(boolean ruleGroup) {
        this.ruleGroup = ruleGroup;
    }

    public List<RuleSet> getRuleSetList() {
        return ruleSetList;
    }

    public void setRuleSetList(List<RuleSet> ruleSetList) {
        this.ruleSetList = ruleSetList;
    }

    private TopicData tdata;
    private List<ItemSet> itemSets = null;
    private List<ItemSet> selectedItemSets = null;
    private List<RuleSet> ruleSetList = null;
    private ArrayList<Vertex> vertexRemainder = null;
    public String ruleDump = "";
    private StringBox selectedBox = null;
    private boolean ruleGroup = false;
}
