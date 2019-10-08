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

package visualizer.corpus.database;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class removes and creates a data base.
 * 
 * @author Fernando Vieira Paulovich
 */
public class CreateDataBase {

    /** Creates a new instance of CreateDatabase.
     */
    public CreateDataBase() {
    }

    public void execute() throws IOException {
        this.removeTables();
        this.createTables();
        ConnectionManager.getInstance().dispose();
    }

    /** Create the tables of this data base. */
    private void createTables() throws IOException {
        PreparedStatement stmt = null;

        try {
            stmt = SqlManager.getInstance().getSqlStatement("CREATE.TABLE.COLLECTIONS");
            stmt.executeUpdate();

            stmt = SqlManager.getInstance().getSqlStatement("CREATE.TABLE.DOCUMENTS");
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    stmt = null;
                } catch (SQLException ex) {
                    Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }
        }

    }

    /** Remove the tables of this data base. */
    private void removeTables() throws IOException {
        PreparedStatement stmt = null;

        try {
            stmt = SqlManager.getInstance().getSqlStatement("DROP.TABLE.DOCUMENTS");
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    stmt = null;
                } catch (SQLException ex) {
                    Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }
        }

        try {
            stmt = SqlManager.getInstance().getSqlStatement("DROP.TABLE.COLLECTIONS");
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    stmt = null;
                } catch (SQLException ex) {
                    Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }
        }

    }

    public static void main(String[] args) {
        try {
            CreateDataBase cdb = new CreateDataBase();
            cdb.execute();
        } catch (IOException ex) {
            Logger.getLogger(CreateDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
