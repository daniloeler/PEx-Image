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
 * of the original code is Fernando Vieira Paulovich <fpaulovich@gmail.com>, 
 * Roberto Pinho <robertopinho@yahoo.com.br>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.topic;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import visualizer.corpus.Corpus;
import visualizer.graph.Vertex;

/**
 *
 * @author Fernando Vieira Paulovich, Roberto Pinho
 */
public abstract class Topic {

    /** Creates a new instance of Topic
     * @param vertex 
     */
    public Topic(ArrayList<Vertex> vertex) {
        this.calculateRectangle(vertex);

        //removing the non-valid vertex
        for (int i = 0; i < vertex.size(); i++) {
            if (!vertex.get(i).isValid()) {
                vertex.remove(i);
                i--;
            }
        }
    }

    public void drawTopic(Graphics g, java.awt.Font font, boolean selected) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;

        g2.setFont(font);
        g2.setStroke(new BasicStroke(1.3f));
        g2.setColor(java.awt.Color.GRAY);
        g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
        g2.drawRect(this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
        g2.setStroke(new BasicStroke(1.0f));

        if (selected || showTopics || showThisTopic) {
            if (selected) {
                g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.2f));
                g2.setPaint(java.awt.Color.BLUE);
                g2.fillRect(this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
            }

            //draw the first label
            if (this.boxes.size() > 0) {
                java.awt.Point position = new java.awt.Point();
                position.x = this.rectangle.x + this.rectangle.width / 2;
                position.y = this.rectangle.y;
                java.awt.Rectangle rect = this.boxes.get(0).draw(g2, position, font, selected);

                //draw all the other ones
                for (int i = 1; i < this.boxes.size(); i++) {
                    position = new java.awt.Point();
                    position.x = this.rectangle.x + this.rectangle.width / 2;
                    position.y = rect.y + rect.height + 6;
                    rect = this.boxes.get(i).draw(g2, position, font, selected);
                }
            }
        }
    }

    public java.awt.Rectangle getRectangle() {
        return rectangle;
    }

    public float weightDistance(java.awt.Point point) {
        if (this.isInside(point)) {
            int cx = this.rectangle.x + this.rectangle.width / 2;
            int cy = this.rectangle.y + this.rectangle.height / 2;
            return (float) (Math.sqrt((cx - point.x) * (cx - point.x) + (cy - point.y) * 
                    (cy - point.y)) * (this.rectangle.width));
        } else {
            return -1;
        }
    }

    public boolean isInside(java.awt.Point point) {
        return (point.x > this.rectangle.x && point.x < (this.rectangle.x + this.rectangle.width) &&
                point.y > this.rectangle.y && point.y < (this.rectangle.y + this.rectangle.height));
    }

    public static boolean isShowTopics() {
        return showTopics;
    }

    public static void setShowTopics(boolean aShowTopics) {
        showTopics = aShowTopics;
    }

    public boolean isShowThisTopic() {
        return showThisTopic;
    }

    public void setShowThisTopic(boolean showThisTopic) {
        this.showThisTopic = showThisTopic;
    }

    public String getQuery() {
        return null;
    }

    protected void calculateRectangle(ArrayList<Vertex> vertex) {
        this.rectangle = calcRect(vertex);
    }

    protected Rectangle calcRect(ArrayList<Vertex> vertex) {
        Rectangle rect = new java.awt.Rectangle();
        
        if (vertex.size() > 0) {
            int maxX = (int) vertex.get(0).getX();
            int minX = (int) vertex.get(0).getX();
            int maxY = (int) vertex.get(0).getY();
            int minY = (int) vertex.get(0).getY();

            for (int v = 1; v < vertex.size(); v++) {
                int x = (int) vertex.get(v).getX();
                int y = (int) vertex.get(v).getY();

                if (x > maxX) {
                    maxX = x;
                } else if (x < minX) {
                    minX = x;
                }

                if (y > maxY) {
                    maxY = y;
                } else if (y < minY) {
                    minY = y;
                }
            }

            rect.x = minX - vertex.get(0).getRayBase() - 2;
            rect.y = minY - vertex.get(0).getRayBase() - 2;
            rect.width = maxX - minX + vertex.get(0).getRayBase() * 2 + 4;
            rect.height = maxY - minY + vertex.get(0).getRayBase() * 2 + 4;
            return rect;
        } else {
            rect.x = -1;
            rect.y = -1;
            rect.width = 0;
            rect.height = 0;
            return rect;
        }
    }

    protected abstract void createTopic(ArrayList<Vertex> vertex, Corpus datasource);

    public boolean isBoxInside(Point point) {
        return false;
    }

    protected ArrayList<StringBox> boxes = new ArrayList<StringBox>();
    protected java.awt.Rectangle rectangle;
    protected static boolean showTopics = false;
    protected boolean showThisTopic = false;
}
