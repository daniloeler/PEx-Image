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

package visualizer.graph;

import java.awt.Color;

/**
 * @author Fernando Vieira Paulovich
 *
 * This class represents a edge on the map.
 */
public class Edge implements Comparable, java.io.Serializable {

    public static final float NO_SIZE = -1;
    private static final long serialVersionUID = 1L;
    /**
     * Constructor of the edge
     * 
     * @param length The edge's lenght
     * @param source The first vertex
     * @param target The second vertex
     */
    public Edge(float length, Vertex source, Vertex target) {
        this(source, target);
        this.length = length;
    }

    /**
     * Constructor of the edge
     * 
     * @param source The first vertex
     * @param target The second vertex
     */
    public Edge(Vertex source, Vertex target) {
        this.source = source;
        this.target = target;
    }

    /**
     * Drawn a edge on a graphical device
     * 
     * @param g2 The graphical device
     * @param globalsel Indicates if there is at least one selected vertex on 
     * the graph this vertex belongs to
     */
    public void draw(java.awt.Graphics2D g2, boolean globalsel) {
        //Combines the color of the two vertex to paint the edge
        if (!this.source.isValid() && !this.target.isValid()) {
            this.color = java.awt.Color.BLACK;
        } else {
            if (!globalsel || (this.target.isSelected() && this.source.isSelected())) {
                g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
            } else {
                g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.2f));
            }
        }

        this.color = new Color((this.source.getColor().getRed() + this.target.getColor().getRed()) / 2,
                (this.source.getColor().getGreen() + this.target.getColor().getGreen()) / 2,
                (this.source.getColor().getBlue() + this.target.getColor().getBlue()) / 2);

        g2.setColor(this.color);
        g2.setStroke(new java.awt.BasicStroke(1.3f));
        g2.drawLine(((int) this.source.getX()), ((int) this.source.getY()),
                ((int) this.target.getX()), ((int) this.target.getY()));
        g2.setStroke(new java.awt.BasicStroke(1.0f));
        
        g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));

        if (Edge.showLength) {
            String label = Float.toString(this.length);
            float x = 5 + (float) Math.abs(this.source.getX() - this.target.getX()) / 2 +
                    Math.min(this.source.getX(), this.target.getX());
            float y = (float) Math.abs(this.source.getY() - this.target.getY()) / 2 +
                    Math.min(this.source.getY(), this.target.getY());

            //Getting the font information
            java.awt.FontMetrics metrics = g2.getFontMetrics(g2.getFont());

            //Getting the label size
            int width = metrics.stringWidth(label);
            int height = metrics.getAscent();

            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.75f));
            g2.setPaint(java.awt.Color.WHITE);
            g2.fill(new java.awt.Rectangle((int) x - 2, (int) y - height, width + 4, height + 4));
            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));

            g2.setColor(java.awt.Color.BLACK);
            g2.drawRect((int) x - 2, (int) y - height, width + 4, height + 4);

            g2.drawString(label, x, y);
        }
    }

    /**
     * Return the color of the edge
     * @return The color of the edge
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Changes the color of the edge
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    public Vertex getSource() {
        return this.source;
    }

    public Vertex getTarget() {
        return this.target;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            Edge e = (Edge) obj;
            return (((this.source.getId() == e.source.getId()) &&
                    (this.target.getId() == e.target.getId())) ||
                    ((this.source.getId() == e.target.getId()) &&
                    (this.target.getId() == e.source.getId())));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3 + 5 * ((this.source != null) ? this.source.hashCode() : 0);
        hash += 7 * ((this.target != null) ? this.target.hashCode() : 0);
        return hash;
    }

    @Override
    public int compareTo(Object o) {
        long source_aux = 0;
        long target_aux = 0;

        if (this.source.getId() < this.target.getId()) {
            source_aux = this.source.getId();
            target_aux = this.target.getId();
        } else {
            source_aux = this.target.getId();
            target_aux = this.source.getId();
        }

        long sourceComp = 0;
        long targetComp = 0;
        if (((Edge) o).source.getId() < ((Edge) o).target.getId()) {
            sourceComp = ((Edge) o).source.getId();
            targetComp = ((Edge) o).target.getId();
        } else {
            sourceComp = ((Edge) o).target.getId();
            targetComp = ((Edge) o).source.getId();
        }

        if (source_aux - sourceComp < 0) {
            return -1;
        } else if (source_aux - sourceComp > 0) {
            return 1;
        } else {
            if (target_aux - targetComp < 0) {
                return -1;
            } else if (target_aux - targetComp > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public float getLength() {
        return length;
    }

    public static boolean isShowLength() {
        return showLength;
    }

    public static void setShowLength(boolean aShowLength) {
        showLength = aShowLength;
    }

    private float length = Edge.NO_SIZE;
    private Color color = Color.WHITE; //Color of the edge
    private Vertex source; //The first vertex of the edge
    private Vertex target; //The second vertex of the edge
    private static boolean showLength = false; //to indicate if the lenght is shown
}
