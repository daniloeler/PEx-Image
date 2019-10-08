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

package visualizer.google;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class GoogleSearchTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    /** Creates a new instance of GoogleSearchTableModel */
    public GoogleSearchTableModel(String[] colunmNames) {
        this.colunmNames = colunmNames;
    }

    public GoogleSearchTableModel() {
        this.colunmNames = new String[]{"Queries", "Request Documents", "Fetched Documents"};
    }

    @Override
    public String getColumnName(int column) {
        return this.colunmNames[column];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        GoogleSearch gsearch = this.searches.get(rowIndex);
        if (columnIndex == 0) {
            return gsearch.getQuery();
        } else if (columnIndex == 1) {
            return Integer.toString(gsearch.getNumberRequestedDocuments());
        } else if (columnIndex == 2) {
            return Integer.toString(gsearch.getNumberFetchedDocuments());
        } else {
            return null;
        }
    }

    public int getRowCount() {
        return this.searches.size();
    }

    public int getColumnCount() {
        return this.colunmNames.length;
    }

    public void addRow(GoogleSearch gsearch) {
        this.searches.add(gsearch);
        this.fireTableRowsInserted(0, this.searches.size());
    }

    public void removeRow(int index) {
        this.searches.remove(index);
        this.fireTableRowsDeleted(0, this.searches.size());
    }

    public void update() {
        this.fireTableRowsUpdated(0, this.searches.size());
    }

    private String[] colunmNames;
    private ArrayList<GoogleSearch> searches = new ArrayList<GoogleSearch>();
}
