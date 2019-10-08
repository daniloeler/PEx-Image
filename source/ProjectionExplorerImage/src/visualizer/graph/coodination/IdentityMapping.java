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
import visualizer.graph.Vertex;
import visualizer.graph.coodination.Mapping.Map;
import visualizer.view.Viewer;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class IdentityMapping extends Mapping {

    /** Creates a new instance of EqualCoordinationMap
     * @param local
     * @param outer
     * @throws java.io.IOException 
     */
    public IdentityMapping(Viewer local, Viewer outer) throws IOException {
        super(local, outer);
    }

    @Override
    public String getName() {
        return "identity";
    }

    @Override
    protected void createMapping(Viewer local, Viewer outer) throws IOException {
      for (int i = 0; i < outer.getGraph().getVertex().size(); i++) {
         Vertex v_outer = outer.getGraph().getVertex().get(i);

         Map m = new Map();
         m.outer = v_outer;

         for (int j = 0; j < local.getGraph().getVertex().size(); j++) {
            Vertex v_local = local.getGraph().getVertex().get(j);

            //extracting just the filename - if URL is a filename
            String v_outerNoExt, v_localNoExt;
            if (v_outer.getUrl().lastIndexOf('.') != -1) {
               v_outerNoExt = v_outer.getUrl().substring(0, v_outer.getUrl().lastIndexOf('.'));
            } else {
               v_outerNoExt = v_outer.getUrl();
            }
            if (v_local.getUrl().lastIndexOf('.') != -1) {
               v_localNoExt = v_local.getUrl().substring(0, v_local.getUrl().lastIndexOf('.'));
            } else {
               v_localNoExt = v_local.getUrl();
            }

            if (v_outerNoExt.equals(v_localNoExt)) {
               m.local.add(v_local);
            }
         }

         this.mapping.add(m);
      }
    }

}
