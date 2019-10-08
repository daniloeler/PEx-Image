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

import java.util.ArrayList;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class ProjectionType {

    static {
        ProjectionType.types = new ArrayList<ProjectionType>();
    }
    
    public static final ProjectionType IDMAP = new ProjectionType("Interactive Document Map (IDMAP)", true, true, false);
    public static final ProjectionType LSP = new ProjectionType("Least Square Projection (LSP)", true, false, false);
    public static final ProjectionType PROJCLUS = new ProjectionType("Projection by Clustering (ProjCus)", true, false, false);
    public static final ProjectionType NJ = new ProjectionType("Neighbor Joining (NJ)", true, true, false);
    public static final ProjectionType SAMMON = new ProjectionType("Sammon's Mapping", true, true, false);
    public static final ProjectionType PCA = new ProjectionType("Principal Components Analysis (PCA)", false, false, true);
    public static final ProjectionType LLE = new ProjectionType("Local Linear Embedding (LLE)", true, false, true);
    public static final ProjectionType CLASSICAL_SCALING = new ProjectionType("Classical Scaling", true, true, false);
    public static final ProjectionType ISOMAP = new ProjectionType("Isometric Feature Mapping (ISOMAP)", true, true, false);
    public static final ProjectionType MST = new ProjectionType("Minimum Spanning Tree Projection (MSTProj)", true, true, false);
    public static final ProjectionType NONE = new ProjectionType("None", false, false, false);

    /** 
     * Creates a new instance of Encoding 
     */
    private ProjectionType(String name, boolean distanceBased, 
            boolean generateDistanceMatrix, boolean attributesNeeded) {
        this.name = name;
        this.distanceBased = distanceBased;
        this.generateDistanceMatrix = generateDistanceMatrix;
        this.attributesNeeded = attributesNeeded;

        ProjectionType.types.add(this);
    }

    public static ArrayList<ProjectionType> getTypes() {
        return ProjectionType.types;
    }

    public static ProjectionType retrieve(String name) {
        for (ProjectionType type : types) {
            if (type.name.equals(name)) {
                return type;
            }
        }

        return null;
    }

    public boolean isDistanceBased() {
        return distanceBased;
    }

    public boolean isGenerateDistanceMatrix() {
        return generateDistanceMatrix;
    }

    public boolean isAttributesNeeded() {
        return attributesNeeded;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final ProjectionType other = (ProjectionType) obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return 29 + (this.name != null ? this.name.hashCode() : 0);
    }
    
    private static ArrayList<ProjectionType> types;
    private String name;
    private boolean distanceBased;
    private boolean generateDistanceMatrix;
    private boolean attributesNeeded;
}
