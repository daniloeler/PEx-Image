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
 * Contributor(s): Roberto Pinho <robertopinho@yahoo.com.br>
 *                 Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.textprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.util.SystemPropertiesManager;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class Stopword {

    /*Note that the words on the stopwords list must be on different lines without spaces*/
    private Stopword(String filename) throws java.io.IOException {
        this.readStopwordList(filename);
    }

    public static Stopword getInstance() throws java.io.IOException {
        if (instance == null) {
            SystemPropertiesManager m = SystemPropertiesManager.getInstance();
            String stpFilename = m.getProperty("SPW.FILE");

            //Test if the stowords file exist
            File f = new File(stpFilename);
            if (!f.exists() || m.getProperty("SPW.FILE").trim().length() < 1) {
                stpFilename = "config/stopwords_eng.spw";
                m.setProperty("SPW.FILE", stpFilename);
            }

            instance = new Stopword(stpFilename);
        }
        return instance;
    }

    public void changeStopwordList(String stpFilename) throws java.io.IOException {
        this.readStopwordList(stpFilename);
    }

    public List<String> getStopwordList() {
        return this.stopwords;
    }

    public void addStopwords(List<String> stopwords) {
        for (String stopword : stopwords) {
            if (!this.stopwords.contains(stopword.toLowerCase())) {
                this.stopwords.add(stopword.toLowerCase());
            }
        }
        Collections.sort(this.stopwords);
    }

    public void removeStopword(String stopword) {
        this.stopwords.remove(stopword);
    }

    public void saveStopwordsList(String filename) throws java.io.IOException {
        this.filename = filename;
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new java.io.FileWriter(filename));

            for (String stopword : this.stopwords) {
                out.write(stopword);
                out.write("\n");
            }
        } catch (IOException ex) {
            throw new java.io.IOException("Problems saving \"" + filename + "\" file!");
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    public String getFilename() {
        return filename;
    }

    public boolean isStopWord(String word) {
        return (Collections.binarySearch(this.stopwords, word) >= 0);
    }

    private void readStopwordList(String filename) throws java.io.IOException {
        this.filename = filename;
        BufferedReader in = null;
        this.stopwords = new ArrayList<String>();

        try {
            in = new BufferedReader(new java.io.FileReader(filename));
            String line = null; //armazena a linha lida

            while ((line = in.readLine()) != null && line.trim().length() > 0) {
                this.stopwords.add(line.toLowerCase());
            }

        } catch (FileNotFoundException e) {
            throw new java.io.IOException("File \"" + filename + "\" was not found!");
        } catch (IOException e) {
            throw new java.io.IOException("Problems reading the file \"" + filename + "\"");
        } finally {
            //fechar o arquivo
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        Collections.sort(this.stopwords);
    }

    private List<String> stopwords;
    private static Stopword instance;
    private String filename;
}
