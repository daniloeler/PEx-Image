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

package visualizer.corpus.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.corpus.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import visualizer.textprocessing.Ngram;
import visualizer.textprocessing.TermExtractor;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class InvertedZipCorpus {

    /** Creates a new instance of InvertedFile
     * @param corpus
     * @param nrGrams
     * @param invFilename 
     */
    public InvertedZipCorpus(ZipCorpus corpus, int nrGrams, String invFilename) {
        this.invFilename = invFilename;
        this.corpus = corpus;
        this.nrGrams = nrGrams;

        if (!(new File(this.invFilename).exists()) ||
                corpus.getNumberGrams() != this.getNumberGrams() ||
                !Corpus.getEncoding().toString().equals(this.getEncoding())) {
            try {
                this.removeFile();
                this.processCorpus(corpus, nrGrams, Corpus.getEncoding());
                this.dispose();
            } catch (IOException ex) {
                Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void removeFile() {
        //if the inverted file exists, remove it.
        File f = new File(this.invFilename);

        if (f.exists()) {
            f.delete();
        }
    }

    public ArrayList<Ngram> getNgrams(String filename) throws IOException {
        ArrayList<Ngram> ngrams = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;

        try {
            if (this.zip == null) {
                this.zip = new ZipFile(this.invFilename);
            }

            ZipEntry entry = zip.getEntry(invDir + filename);
            if (entry != null) {
                bis = new BufferedInputStream(zip.getInputStream(entry));
                ois = new ObjectInputStream(bis);
                ngrams = (ArrayList<Ngram>) ois.readObject();
                ois.close();
            }
        } catch (InvalidClassException ex) {
            Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
            this.dispose();
            this.removeFile();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (bis != null) {
                bis.close();
            }

            if (ois != null) {
                ois.close();
            }
        }

        return ngrams;
    }

    public ArrayList<Ngram> getCorpusNgrams() throws IOException {
        ArrayList<Ngram> ngrams = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;

        try {
            if (this.zip == null) {
                this.zip = new ZipFile(this.invFilename);
            }

            ZipEntry entry = zip.getEntry("corpusNgrams.txt");

            if (entry != null) {
                bis = new BufferedInputStream(zip.getInputStream(entry));
                ois = new ObjectInputStream(bis);
                ngrams = (ArrayList<Ngram>) ois.readObject();
            }

        } catch (InvalidClassException ex) {
            Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
            this.dispose();
            this.removeFile();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (bis != null) {
                bis.close();
            }

            if (ois != null) {
                ois.close();
            }
        }

        return ngrams;
    }

    public void dispose() {
        if (this.zip != null) {
            try {
                this.zip.close();
                this.zip = null;
            } catch (IOException ex) {
                Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getInvFilename() {
        return invFilename;
    }

    private void processCorpus(ZipCorpus corpus, int nrGrams, Encoding encoding) throws IOException {
        HashMap<String, Integer> corpusNgrams = new HashMap<String, Integer>();

        ZipOutputStream zout = null;

        try {
            FileOutputStream dest = new FileOutputStream(this.invFilename);
            zout = new ZipOutputStream(new BufferedOutputStream(dest));
            zout.setMethod(ZipOutputStream.DEFLATED);
            zout.setLevel(Deflater.BEST_SPEED);

            //add the number of grams and encoding to the inverted corpus properties file
            ZipEntry entry = new ZipEntry("inverted.properties");
            zout.putNextEntry(entry);
            String prop = "number.grams=" + nrGrams + "\n";
            zout.write(prop.getBytes(), 0, prop.length());
            prop = "char.encoding=" + encoding.toString() + "\n";
            zout.write(prop.getBytes(), 0, prop.length());

            for (int i = 0; i < corpus.getIds().size(); i++) {
                ArrayList<Ngram> ngrams = this.getNgramsFromFile(corpus, corpus.getIds().get(i));
                this.addFile(zout, ngrams, invDir + corpus.getIds().get(i));

                for (int j = 0; j < ngrams.size(); j++) {
                    Ngram n = ngrams.get(j);

                    if (corpusNgrams.containsKey(n.ngram)) {
                        corpusNgrams.put(n.ngram, corpusNgrams.get(n.ngram) + n.frequency);
                    } else {
                        corpusNgrams.put(n.ngram, n.frequency);
                    }
                }
            }

            ArrayList<Ngram> ngrams = new ArrayList<Ngram>();
            for (String key : corpusNgrams.keySet()) {
                ngrams.add(new Ngram(key, corpusNgrams.get(key)));
            }

            Collections.sort(ngrams);
            this.addFile(zout, ngrams, "corpusNgrams.txt");

        } catch (FileNotFoundException ex) {
            Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (zout != null) {
                    zout.flush();
                    zout.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String getEncoding() {
        String encoding = "";
        ZipFile zip_aux = null;

        try {
            zip_aux = new ZipFile(this.invFilename);

            ZipEntry entry = zip_aux.getEntry("inverted.properties");
            if (entry != null) {
                Properties prop = new Properties();
                prop.load(zip_aux.getInputStream(entry));
                encoding = prop.getProperty("char.encoding");
            }

        } catch (NumberFormatException ex) {
            Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                zip_aux.close();
            } catch (IOException ex) {
                Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return encoding;
    }

    private int getNumberGrams() {
        int nrGrams_aux = -1;
        ZipFile zip_aux = null;

        try {
            zip_aux = new ZipFile(this.invFilename);

            ZipEntry entry = zip_aux.getEntry("inverted.properties");
            if (entry != null) {
                Properties prop = new Properties();
                prop.load(zip_aux.getInputStream(entry));
                nrGrams_aux = Integer.parseInt(prop.getProperty("number.grams"));
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                zip_aux.close();
            } catch (IOException ex) {
                Logger.getLogger(InvertedZipCorpus.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return nrGrams_aux;
    }

    private void addFile(ZipOutputStream zout, ArrayList<Ngram> ngrams, String filename) throws IOException {
        Collections.sort(ngrams);

        ZipEntry entry = new ZipEntry(filename);
        zout.putNextEntry(entry);

        ObjectOutputStream oos = new ObjectOutputStream(zout);
        oos.writeObject(ngrams);
        oos.flush();
    }

    private ArrayList<Ngram> getNgramsFromFile(ZipCorpus corpus, String filename) throws IOException {
        HashMap<String, Integer> ngramsTable = new HashMap<String, Integer>();

        Pattern pattern = Pattern.compile(TermExtractor.getRegularExpression());
        String filecontent = corpus.getFullContent(filename);

        if (filecontent != null) {
            Matcher matcher = pattern.matcher(filecontent);

            //create the firt ngram
            String[] ngram = new String[corpus.getNumberGrams()];
            int i = 0, count = 0;
            while (count < corpus.getNumberGrams() && matcher.find()) {
                String term = matcher.group();

                if (term.length() > 0) {
                    String word = term.toLowerCase();

                    if (word.trim().length() > 0) {
                        ngram[count] = word;
                        count++;
                    }
                }

                i++;
            }

            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < ngram.length - 1; j++) {
                sb.append(ngram[j] + "<>");
            }

            sb.append(ngram[ngram.length - 1]);

            //adding to the frequencies table
            ngramsTable.put(sb.toString(), 1);

            //creating the remaining ngrams
            while (matcher.find()) {
                String term = matcher.group();

                if (term.trim().length() > 0) {
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

        return ngrams;
    }

    private String addNextWord(String[] ngram, String word) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ngram.length - 1; i++) {
            ngram[i] = ngram[i + 1];
            sb.append(ngram[i] + "<>");
        }

        ngram[ngram.length - 1] = word;
        sb.append(word);
        return sb.toString();
    }

    private static final String invDir = "inv/";
    private String invFilename;
    private ZipFile zip;
    private ZipCorpus corpus;
    private int nrGrams;
}
