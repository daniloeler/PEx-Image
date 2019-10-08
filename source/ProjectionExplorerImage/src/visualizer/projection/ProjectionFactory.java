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

import visualizer.projection.idmap.IDMAPProjection;
import visualizer.projection.isomap.ISOMAPProjection;
import visualizer.projection.lle.LLEProjection;
import visualizer.projection.lsp.LSPProjection2D;
import visualizer.projection.mds.ClassicalScalingProjection;
import visualizer.projection.mstprojection.MSTProjection;
import visualizer.projection.nj.NJProjection;
import visualizer.projection.pca.PCAProjection;
import visualizer.projection.projclus.ProjClusProjection;
import visualizer.projection.sammon.SammonMappingProjection;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class ProjectionFactory {

    public static Projection getInstance(ProjectionType type) {

        if (type == ProjectionType.IDMAP) {
            return new IDMAPProjection();
        } else if (type == ProjectionType.LSP) {
            return new LSPProjection2D();
        } else if (type == ProjectionType.PROJCLUS) {
            return new ProjClusProjection();
        } else if (type == ProjectionType.NJ) {
            return new NJProjection();
        } else if (type == ProjectionType.SAMMON) {
            return new SammonMappingProjection();
        } else if (type == ProjectionType.PCA) {
            return new PCAProjection();
        } else if (type == ProjectionType.LLE) {
            return new LLEProjection();
        } else if (type == ProjectionType.CLASSICAL_SCALING) {
            return new ClassicalScalingProjection();
        } else if (type == ProjectionType.ISOMAP) {
            return new ISOMAPProjection();
        } else if (type == ProjectionType.MST) {
            return new MSTProjection();
        }

        return null;
    }

}
