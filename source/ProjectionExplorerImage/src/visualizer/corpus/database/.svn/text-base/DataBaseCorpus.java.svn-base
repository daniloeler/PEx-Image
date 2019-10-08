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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.corpus.Corpus;
import visualizer.textprocessing.Ngram;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class DataBaseCorpus extends Corpus {

    public DataBaseCorpus(String url, int nrGrams) {
        super(url, nrGrams);

        try {
            this.retrieveCollectionId();

            this.run();

            Logger.getLogger(DataBaseCorpus.class.getName()).
                    log(Level.INFO, "Collection name: " + this.url);
            Logger.getLogger(DataBaseCorpus.class.getName()).
                    log(Level.INFO, "Collection id: " + this.collection_id);
        } catch (IOException ex) {
            Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getFullContent(String id) throws IOException {
        String content = "";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = SqlManager.getInstance().getSqlStatement("SELECT.CONTENT.DOCUMENT");
            stmt.setInt(1, Integer.parseInt(id));
            stmt.setInt(2, this.collection_id);

            //getting the result
            rs = stmt.executeQuery();
            if (rs.next()) {
                content = rs.getString("content");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }
        }

        return content;
    }

    @Override
    public String getFilteredContent(String id) throws IOException {
        return this.getFullContent(id);
    }

    @Override
    public String getViewContent(String id) throws IOException {
        return this.getFullContent(id);
    }

    @Override
    public String getSearchContent(String id) throws IOException {
        return this.getFullContent(id);
    }

    @Override
    public ArrayList<Ngram> getNgrams(String id) throws IOException {
        ArrayList<Ngram> ngrams = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = SqlManager.getInstance().getSqlStatement("SELECT.NGRAMS.DOCUMENT");
            stmt.setInt(1, Integer.parseInt(id));
            stmt.setInt(2, this.collection_id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                InputStream is = rs.getBlob("ngrams").getBinaryStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                ngrams = (ArrayList<Ngram>) ois.readObject();
                ois.close();
                is.close();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }
        }

        return ngrams;
    }

    @Override
    public ArrayList<Ngram> getCorpusNgrams() throws IOException {
        ArrayList<Ngram> ngrams = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = SqlManager.getInstance().getSqlStatement("SELECT.NGRAMS.COLLECTION");
            stmt.setInt(1, this.collection_id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                InputStream is = rs.getBlob("ngrams").getBinaryStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                ngrams = (ArrayList<Ngram>) ois.readObject();
                ois.close();
                is.close();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }
        }

        return ngrams;
    }

    public Date getDate(String id) throws IOException {
        Date date = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = SqlManager.getInstance().getSqlStatement("SELECT.DATE.DOCUMENT");
            stmt.setInt(1, Integer.parseInt(id));
            stmt.setInt(2, this.collection_id);

            //getting the result
            rs = stmt.executeQuery();
            if (rs.next()) {
                date = rs.getDate("date");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }
        }

        return date;
    }

    public int getNrDocuments() throws IOException {
        int nrDocuments = 0;

        if (this.collection_id > 0) {
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                //getting the number of documents on the collection
                stmt = SqlManager.getInstance().getSqlStatement("SELECT.NUMBER.DOCUMENTS");
                stmt.setInt(1, this.collection_id);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    nrDocuments = rs.getInt(1);
                } else {
                    throw new IOException("Problems retrieving the number of documents.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                throw new IOException(ex.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                        throw new IOException(ex.getMessage());
                    }
                }

                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                        throw new IOException(ex.getMessage());
                    }
                }
            }
        }

        return nrDocuments;
    }

    @Override
    protected void run() {
        this.createFilenames();
        this.createCdata();
    }

    private void createFilenames() {
        if (this.url != null) {
            this.ids = new ArrayList<String>();

            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                stmt = SqlManager.getInstance().getSqlStatement("SELECT.ID.DOCUMENTS");
                stmt.setInt(1, this.collection_id);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    this.ids.add(Integer.toString(id));
                }
            } catch (IOException ex) {
                Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    private void createCdata() {
        if (this.url != null && this.ids != null) {
            float[] cdata = new float[this.ids.size()];
            Arrays.fill(cdata, 0.0f);

            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                stmt = SqlManager.getInstance().getSqlStatement("SELECT.CLASS.DOCUMENTS");
                stmt.setInt(1, this.collection_id);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    float value = rs.getFloat("class");
                    String id = Integer.toString(rs.getInt("id"));
                    cdata[this.ids.indexOf(id)] = value;
                }
            } catch (IOException ex) {
                Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            this.cdata = cdata;
        }
    }

    

    private void retrieveCollectionId() throws IOException {
        if (this.url != null) {
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                //getting the collection id
                stmt = SqlManager.getInstance().getSqlStatement("SELECT.COLLECTION.BY.NAME");
                stmt.setString(1, this.url);
                rs = stmt.executeQuery();

                if (rs.next()) {
                    this.collection_id = rs.getInt("id");
                } else {
                    throw new IOException("There is not exist a collection called \"" +
                            this.url + "\"");
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                throw new IOException(ex.getMessage());
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                        throw new IOException(ex.getMessage());
                    }
                }

                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(DataBaseCorpus.class.getName()).log(Level.SEVERE, null, ex);
                        throw new IOException(ex.getMessage());
                    }
                }
            }
        }
    }

    private int collection_id = 0;
}
