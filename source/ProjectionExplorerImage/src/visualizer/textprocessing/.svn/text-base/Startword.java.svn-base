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
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *                 Fernando Vieira Paulovich <fpaulovich@gmail.com>  
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
 * @author Roberto Pinho
 */
public class Startword {

    /*Note that the words on the startwords list must be on different lines without spaces*/
    private Startword(String filename) throws java.io.IOException {
        this.readStartwordList(filename);
    }

    public static Startword getInstance() throws java.io.IOException {
        if (instance == null) {
            SystemPropertiesManager m = SystemPropertiesManager.getInstance();
            String staFilename = m.getProperty("STW.FILE");

            //Test if the stowords file exist
            File f = new File(staFilename);
            if (!f.exists() || m.getProperty("STW.FILE").trim().length() < 1) {
                staFilename = "config/startwords.stw";
                m.setProperty("STW.FILE", staFilename);
            }

            instance = new Startword(staFilename);
        }
        return instance;
    }

    public void changeStartwordList(String staFilename) throws java.io.IOException {
        this.readStartwordList(staFilename);
    }

    public List<WeightedWord> getStartwordList() {
        return this.startwords;
    }

    public void addStartwords(List<String> newStartwords) {
        for (String newStartword : newStartwords) {
            if (!isStartWord(newStartword)) {
                this.startwords.add(new WeightedWord(newStartword.toLowerCase(), new Double(1.0)));
            }
        }

        Collections.sort(this.startwords);
    }

    public void addStartword(String newStartword, Double weight) {
        if (!isStartWord(newStartword)) {
            this.startwords.add(new WeightedWord(newStartword.toLowerCase(), weight));
        }

        Collections.sort(this.startwords);
    }

    public void removeStartword(String startword) {
        this.startwords.remove(startword);
    }

    public void saveStartwordsList(String filename) throws java.io.IOException {
        this.filename = filename;
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new java.io.FileWriter(filename));

            for (WeightedWord startword : this.startwords) {
                out.write(startword.word + " ; " + startword.weight);
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

    public boolean isStartWord(String word) {
        return (Collections.binarySearch(this.startwords, WeightedWord.newInstance(word, 1.0)) >= 0);
    }

    public Double getWeight(String word) {
        int index = Collections.binarySearch(this.startwords, WeightedWord.newInstance(word, 1.0));
        if (index >= 0) {
            return startwords.get(index).weight;
        } else {
            return 0.0;
        }
    }

    private void readStartwordList(String filename) throws java.io.IOException {
        this.filename = filename;
        BufferedReader in = null;
        this.startwords = new ArrayList<WeightedWord>();

        try {
            in = new BufferedReader(new java.io.FileReader(filename));
            String line = null; //armazena a linha lida
            java.lang.String word;
            Double weight;

            while ((line = in.readLine()) != null && line.trim().length() > 0) {
                if (line.contains(";")) {
                    word = line.split(";")[0].trim();

                    weight = new Double(line.split(";")[1]);
                    this.startwords.add(new WeightedWord(word.toLowerCase(), weight));

                } else {
                    this.startwords.add(new WeightedWord(line.toLowerCase(), new Double(1.0)));
                }
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

        Collections.sort(this.startwords);
    }

    private List<WeightedWord> startwords;
    private static Startword instance;
    private String filename;
}
