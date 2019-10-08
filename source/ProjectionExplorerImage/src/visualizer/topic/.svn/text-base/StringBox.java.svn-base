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

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
import visualizer.graph.Vertex;

/**
 *
 * @author Fernando Vieira Paulovich, Roberto Pinho
 */
public class StringBox {

    /** Creates a new instance of StringBox
     * @param msg 
     */
    public StringBox(String msg) {
        this.msg = msg;
    }

    public StringBox(String msg, List<Vertex> relatedVertices) {
        this.msg = msg;
        this.relatedVertices = relatedVertices;
    }

    public StringBox(String msg, List<Vertex> relatedVertices, Object userObject) {
        this.msg = msg;
        this.relatedVertices = relatedVertices;
        this.userObject = userObject;
    }

    public java.awt.Rectangle draw(Graphics g, java.awt.Point position, java.awt.Font font, boolean selected) {
        java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;

        g2.setFont(font);
        java.awt.FontMetrics metrics = g2.getFontMetrics(g2.getFont());

        //Getting the label size
        int width = metrics.stringWidth(this.msg);
        int height = metrics.getAscent();

        //Creating the rectangle to be drawn
        java.awt.Rectangle rect = new java.awt.Rectangle(position.x - 2, position.y - 2, width + 4, height + 4);

        if (selected) {
            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
            g2.setPaint(java.awt.Color.BLUE);
        } else {
            g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 0.85f));
            //g2.setPaint(java.awt.Color.YELLOW);
            g2.setPaint(new java.awt.Color(255, 255, 204));
        }

        g2.drawRect(rect.x - 1, rect.y - 1, rect.width + 2, rect.height + 2);
        g2.fill(rect);

        if (selected) {
            g2.setPaint(java.awt.Color.WHITE);
        } else {
            g2.setPaint(java.awt.Color.BLACK);
        }
        
        g2.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, 1.0f));
        g2.drawRect(rect.x, rect.y, rect.width, rect.height);
        g2.drawString(this.msg, position.x, position.y + height - 2);

        this.rectangle = rect;

        return rect;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return this.getMsg();
    }

    public List<Vertex> getRelatedVertices() {
        return relatedVertices;
    }

    public void setRelatedVertices(List<Vertex> relatedVertices) {
        this.relatedVertices = relatedVertices;
    }

    public boolean isInside(java.awt.Point point) {
        return (point.x > this.rectangle.x && point.x < (this.rectangle.x + this.rectangle.width) &&
                point.y > this.rectangle.y && point.y < (this.rectangle.y + this.rectangle.height));
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    private String msg = "";
    private List<Vertex> relatedVertices;
    private Object userObject;
    private Rectangle rectangle;
}
