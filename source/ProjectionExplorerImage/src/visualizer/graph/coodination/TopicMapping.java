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
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.graph.Connectivity;
import visualizer.graph.Edge;
import visualizer.graph.Scalar;
import visualizer.graph.Vertex;
import visualizer.view.Viewer;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class TopicMapping extends Mapping {

    /** Creates a new instance of TopicMapping
     * @param local
     * @param outer
     * @throws IOException 
     */
    public TopicMapping(Viewer local, Viewer outer) throws IOException {
        super(local, outer);
        this.local = local;
    }

    @Override
    public String getName() {
        return "topic";
    }

    @Override
    public void coordinate(Object param, ArrayList<Vertex> outer) {
        try {
            this.highlightedVertices.clear();
            if (param != null) {
                Scalar s = this.local.getGraph().createQueryScalar((String) param);
                this.local.updateScalars(s);
                for (int i=0; i<this.local.getGraph().getVertex().size();i++){
                    Vertex v = this.local.getGraph().getVertex().get(i);
                    if (v.getScalar(s) > 0.0f){
                        this.highlightedVertices.add(v);
                    }
                }
                this.local.selectVertices(this.highlightedVertices);
                ////////////////Test Connectivity
        Connectivity con = this.local.getGraph().getConnectivityByName("selection");
        if (con == null) {
            con = new Connectivity("selection");
        }

        ArrayList<Edge> edges = con.getEdges();
        for (int i = 1; i < this.highlightedVertices.size(); i++) {
            edges.add(new Edge(this.highlightedVertices.get(0), this.highlightedVertices.get(i)));
        }
        edges = Connectivity.compress(edges);

        con.setEdges(edges);
        this.local.getGraph().addConnectivity(con);
        this.local.updateConnectivities(con);
        //////////////
            }
        } catch (IOException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void createMapping(Viewer local, Viewer outer) throws IOException {
    //not necessary to create links between the nodes
    }

    private Viewer local;
}
