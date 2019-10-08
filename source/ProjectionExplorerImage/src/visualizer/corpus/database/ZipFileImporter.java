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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import visualizer.corpus.Encoding;
import visualizer.textprocessing.Ngram;
import visualizer.textprocessing.TermExtractor;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class ZipFileImporter {

    /** Creates a new instance of CreateDatabase
     * 
     * @param filename The zip file containing the documents.
     */
    public ZipFileImporter(String filename) {
        this.filename = filename;
        this.klasses = new ArrayList<String>();
    }

    public void execute(String collection, int nrLines, int nrGrams,
            Encoding encoding) throws IOException {
        HashMap<String, Integer> corpusNgrams = new HashMap<String, Integer>();

        //checking if the collection nam already exist
        if (!this.uniqueName(collection)) {
            throw new IOException("A collection intitled \"" + collection +
                    "\" already exists. Please choose another name.");
        }

        //creating the collection
        int id_collection = this.getNewCollectionId();
        PreparedStatement stmt = null;

        try {
            stmt = SqlManager.getInstance().getSqlStatement("INSERT.COLLECTION");
            stmt.setInt(1, id_collection);
            stmt.setString(2, collection);
            stmt.setInt(3, nrGrams);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ZipFileImporter.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                    stmt = null;
                } catch (SQLException ex) {
                    Logger.getLogger(ZipFileImporter.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }
        }

        //adding the documents
        ZipFile zip = new ZipFile(this.filename);
        Enumeration entries = zip.entries();
        for (int i = 0; entries.hasMoreElements(); i++) {
            ZipEntry entry = (ZipEntry) entries.nextElement();

            if (entry != null && !entry.isDirectory()) {
                String content = this.getFileContent(zip, entry, encoding);
                ArrayList<Ngram> fngrams = this.getNgramsFromFile(content, nrGrams);
                String title = this.getFileTitle(zip, entry, nrLines, encoding);
                float klass = this.getKlass(entry.getName());
                Date date = this.getFileDate(zip, entry, encoding);

                this.saveToDataBase(i, id_collection, title, content, date,
                        fngrams, klass);

                for (int j = 0; j < fngrams.size(); j++) {
                    Ngram n = fngrams.get(j);
                    if (corpusNgrams.containsKey(n.ngram)) {
                        corpusNgrams.put(n.ngram, corpusNgrams.get(n.ngram) + n.frequency);
                    } else {
                        corpusNgrams.put(n.ngram, n.frequency);
                    }
                }
            }
        }

        ArrayList<Ngram> ngrams = new ArrayList<Ngram>();
        for (String key : corpusNgrams.keySet()) {
            ngrams.add(new Ngram(key, corpusNgrams.get(key)));
        }

        Collections.sort(ngrams);

        //add the ngrams to the collection
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(ngrams);
            oos.flush();
            oos.close();

            stmt = SqlManager.getInstance().getSqlStatement("UPDATE.NGRAMS.COLLECTION");
            stmt.setBytes(1, baos.toByteArray());
            stmt.setInt(2, id_collection);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ZipFileImporter.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ZipFileImporter.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }
        }

        //closing the data base connection
        ConnectionManager.getInstance().dispose();
    }

    private boolean uniqueName(String collection) throws IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = SqlManager.getInstance().getSqlStatement("SELECT.COLLECTION.BY.NAME");
            stmt.setString(1, collection);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZipFileImporter.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ZipFileImporter.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }
        }
    }

    private int getNewCollectionId() throws IOException {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = SqlManager.getInstance().getSqlStatement("SELECT.COLLECTION.ID");
            rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) + 1;
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZipFileImporter.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ZipFileImporter.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }
        }
    }

    private void saveToDataBase(int id, int id_collection, String title, String content,
            Date date, ArrayList<Ngram> ngrams, float klass) throws IOException {

        PreparedStatement stmt = null;

        try {
            //creating the ngrams stream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(ngrams);
            oos.flush();
            oos.close();

            //creating and executing the sql
            stmt = SqlManager.getInstance().getSqlStatement("INSERT.DOCUMENT");
            stmt.setInt(1, id);
            stmt.setInt(2, id_collection);
            stmt.setString(3, title);
            stmt.setString(4, content);
            stmt.setBytes(5, baos.toByteArray());
            stmt.setFloat(6, klass);
            stmt.setDate(7, date);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ZipFileImporter.class.getName()).log(Level.SEVERE, null, ex);
            throw new IOException(ex.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    Logger.getLogger(ZipFileImporter.class.getName()).log(Level.SEVERE, null, ex);
                    throw new IOException(ex.getMessage());
                }
            }
        }
    }

    /** Get the date of a file. */
    private Date getFileDate(ZipFile zip, ZipEntry entry, Encoding encoding) throws IOException {
        Date date = new Date(System.currentTimeMillis());

        BufferedReader in = new BufferedReader(new InputStreamReader(zip.getInputStream(entry),
                encoding.toString()));

        //getting the date
        String aux = null;
        while ((aux = in.readLine()) != null) {
            if (aux.trim().startsWith("#:")) {
                aux = aux.replaceAll("#:", "").trim();

                if (this.isInteger(aux)) {
                    GregorianCalendar gc = new GregorianCalendar();
                    gc.set(GregorianCalendar.YEAR, Integer.parseInt(aux));
                    gc.set(GregorianCalendar.MONTH, GregorianCalendar.JANUARY);
                    gc.set(GregorianCalendar.DAY_OF_MONTH, 1);
                    date = new Date(gc.getTimeInMillis());
                }
                break;
            }
        }

        return date;
    }

    /** check if a string is an integer */
    private boolean isInteger(String s) {
        return s.matches("[0-9]*");
    }

    /** Get the title of a file. */
    private String getFileTitle(ZipFile zip, ZipEntry entry, int nrLines,
            Encoding encoding) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(zip.getInputStream(entry),
                encoding.toString()));

        String aux = null;
        StringBuffer text = new StringBuffer();
        for (int i = 0; i < nrLines && (aux = in.readLine()) != null; i++) {
            if (aux.trim().length() > 0) {
                text.append(aux + " ");
            } else {
                i--;
            }
        }

        return text.toString();
    }

    /** Get the content of a file. */
    private String getFileContent(ZipFile zip, ZipEntry entry,
            Encoding encoding) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(zip.getInputStream(entry),
                encoding.toString()));

        String aux = null;
        StringBuffer text = new StringBuffer();
        while ((aux = in.readLine()) != null) {
            text.append(aux + "\r\n");
        }

        return text.toString();
    }

    /** 
     * Returns the ngrams of a given content. 
     * 
     * @param The content.
     * @return The found ngrams.
     */
    private ArrayList<Ngram> getNgramsFromFile(String content, int nrGrams) throws IOException {
        HashMap<String, Integer> ngramsTable = new HashMap<String, Integer>();

        Pattern pattern = Pattern.compile(TermExtractor.getRegularExpression());

        if (content != null) {
            Matcher matcher = pattern.matcher(content);

            //create the firt ngram
            String[] ngram = new String[nrGrams];
            int i = 0, count = 0;
            while (count < nrGrams && matcher.find()) {
                String term = matcher.group();

                if (term.trim().length() > 0) {
                    String word = term.toLowerCase();

                    if (word.trim().length() > 0) {
                        ngram[count] = word;
                        count++;
                    }
                }

                i++;
            }

            StringBuffer sb = new StringBuffer();
            for (int j = 0; j <
                    ngram.length - 1; j++) {
                sb.append(ngram[j] + "<>");
            }

            sb.append(ngram[ngram.length - 1]);

            //adding to the frequencies table
            ngramsTable.put(sb.toString(), 1);

            //creating the remaining ngrams
            while (matcher.find()) {
                String term = matcher.group();

                if (term.length() > 0) {
                    String word = term.toLowerCase();

                    if (word.trim().length() > 0) {
                        String ng = this.addNextWord(ngram, word);

                        //verify if the ngram already exist on the document
                        if (ngramsTable.containsKey(ng)) {
                            ngramsTable.put(ng, ngramsTable.get(ng) + 1);
                        } else {
                            ngramsTable.put(ng, 1);
                        }
                    }
                }

                i++;
            }

        }

        ArrayList<Ngram> ngrams = new ArrayList<Ngram>();

        for (String n : ngramsTable.keySet()) {
            ngrams.add(new Ngram(n, ngramsTable.get(n)));
        }

        Collections.sort(ngrams);

        return ngrams;
    }

    /** Add a word at the end of a words sequency (ngram) in order to
     * compose a new ngram.
     * 
     * @return Return the composed ngram.
     */
    private String addNextWord(String[] ngram, String word) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i <
                ngram.length - 1; i++) {
            ngram[i] = ngram[i + 1];
            sb.append(ngram[i] + "<>");
        }

        ngram[ngram.length - 1] = word;
        sb.append(word);
        return sb.toString();
    }

    private float getKlass(String filename) {
        int begin = filename.lastIndexOf("/");
        if (begin > -1) {
            filename = filename.substring(begin + 1);
        } else {
            begin = filename.lastIndexOf("\\");
            if (begin > -1) {
                filename = filename.substring(begin + 1);
            }
        }

        String ini = filename;
        if (filename.length() > 2) {
            ini = filename.substring(0, 2);
        }

        if (!this.klasses.contains(ini)) {
            this.klasses.add(ini);
        }

        return this.klasses.indexOf(ini);
    }

    public static void main(String[] args) {
        try {
            String filename = "G:\\User\\users\\Documents\\FERNANDO\\Codigo\\java\\ExtractArticles\\ExtractArticles.zip";
            ZipFileImporter zfi = new ZipFileImporter(filename);
            zfi.execute("Infovis 2004 contest IV", 1, 1, Encoding.ASCII);
        } catch (IOException ex) {
            Logger.getLogger(ZipFileImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String filename;
    private ArrayList<String> klasses;
}
