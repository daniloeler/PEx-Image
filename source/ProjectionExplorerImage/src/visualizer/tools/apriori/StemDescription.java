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
 * of the original code is Roberto Pinho <robertopinho@yahoo.com.br>.
 *
 * Contributor(s): 
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

/**
 * @(#)StemDescription.java
 *
 * Stores the definitions of a stem, ie. which keys hash into it.
 *
 * @author Adrian Bright
 */
package visualizer.tools.apriori;

import java.util.ArrayList;

public class StemDescription {

    private Character stemID;
    /* The keys which hash into this stem */
    private ArrayList keys;
    /**
     * StemDescription Initialises the StemDescription.
     * @param ID The identifier for the stem.
     */
    public StemDescription(Character ID) {
        keys = new ArrayList();
        stemID = ID;
    }

    /**
     * getID Returns the ID of the stem.
     * @return The ID for the stem.
     */
    public Character getID() {
        return stemID;
    }

    /**
     * addKey Adds a key to the stem.
     * @param newKey The new key to be added.
     */
    public void addKey(Integer newKey) {
        keys.add(newKey);
    }

    /**
     * keyExists Identifies whether or not the specified key exists on this stem.
     * @param keyToCheck The key which needs checking.
     * @return Whether or not the key exists on this stem.
     */
    public boolean keyExists(Integer keyToCheck) {
        if (keys.contains(keyToCheck)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * getKeys Returns a list of all the keys on this stem.
     * @return A String containing all the keys on this stem.
     */
    public String getKeys() {
        String keyList = new String();
        for (int count = 0; count < keys.size(); count++) {
            keyList += (String) keys.get(count);
            if (count < (keys.size() - 1)) {
                keyList += ",";
            }
        }
        return keyList;
    }

}
