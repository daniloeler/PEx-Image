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

package visualizer.view.color;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class UndefinedScale extends ColorScale {

    /**
     * Creates a new instance of UndefinedCS
     */
    public UndefinedScale() {
        colors = new java.awt.Color[256];

        for (int i = 0; i < colors.length; i++) {
            float aux = (float) i / colors.length;

            if (aux < 0.25f) {
                //red - > yellow
                this.colors[i] = new java.awt.Color(1.0f, 4 * aux, 0.0f);
                float[] comp = java.awt.Color.RGBtoHSB(this.colors[i].getRed(), this.colors[i].getGreen(), this.colors[i].getBlue(), null);
                this.colors[i] = new java.awt.Color(java.awt.Color.HSBtoRGB(comp[0], 0.8f, 0.8f));
            } else if (aux >= 0.25f && aux < 0.5f) {
                //green -> cyan
                this.colors[i] = new java.awt.Color((-4 * aux + 2), 1.0f, 0.0f);
                float[] comp = java.awt.Color.RGBtoHSB(this.colors[i].getRed(), this.colors[i].getGreen(), this.colors[i].getBlue(), null);
                this.colors[i] = new java.awt.Color(java.awt.Color.HSBtoRGB(comp[0], 0.8f, 0.8f));
            } else if (aux >= 0.5f && aux < 0.75f) {
                //bulue -> cyan
                this.colors[i] = new java.awt.Color(0.0f, 1.0f, (4 * aux - 2));
                float[] comp = java.awt.Color.RGBtoHSB(this.colors[i].getRed(), this.colors[i].getGreen(), this.colors[i].getBlue(), null);
                this.colors[i] = new java.awt.Color(java.awt.Color.HSBtoRGB(comp[0], 0.8f, 0.8f));
            } else {
                //blue -> pink
                this.colors[i] = new java.awt.Color(0.0f, (-4 * aux + 4), 1.0f);
                float[] comp = java.awt.Color.RGBtoHSB(this.colors[i].getRed(), this.colors[i].getGreen(), this.colors[i].getBlue(), null);
                this.colors[i] = new java.awt.Color(java.awt.Color.HSBtoRGB(comp[0], 0.8f, 0.8f));
            }
        }
    }

    public String getName() {
        return "Undefined...";
    }

}
