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

package visualizer.graph.coodination;

import java.io.IOException;
import java.util.ArrayList;
import visualizer.graph.*;
import visualizer.view.Viewer;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public abstract class Mapping {

    /** Creates a new instance of CoordinationMap */
    public Mapping() {
    }

    public Mapping(Viewer local, Viewer outer) throws IOException {
        this.outer = outer;
        this.createMapping(local, outer);
    }

    protected abstract void createMapping(Viewer local, Viewer outer) throws IOException;

    public abstract String getName();

    public void coordinate(Object param, ArrayList<Vertex> outer) {
        for (int i = 0; i < outer.size(); i++) {
            Vertex v_outer = outer.get(i);

            for (int j = 0; j < this.mapping.size(); j++) {
                if (this.mapping.get(j).outer == v_outer) {
                    for (int k = 0; k < this.mapping.get(j).local.size(); k++) {
                        this.mapping.get(j).local.get(k).setSelected(true);
                    }
                }
            }
        }
    }

    public Viewer getOuterGraphViewer() {
        return this.outer;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public static final Mapping OFF = new Mapping() {

        @Override
        protected void createMapping(Viewer local, Viewer outer) throws IOException {
        }

        @Override
        public String getName() {
            return "off";
        }

    };

    public class Map {

        public Vertex outer;
        public ArrayList<Vertex> local = new ArrayList<Vertex>();
    }

    protected ArrayList<Vertex> highlightedVertices = new ArrayList<Vertex>();
    protected Viewer outer;
    protected ArrayList<Map> mapping = new ArrayList<Map>();
}
