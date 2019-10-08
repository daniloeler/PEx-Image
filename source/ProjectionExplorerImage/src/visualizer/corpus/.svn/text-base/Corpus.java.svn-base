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
 * of the original code is Fernando Vieira Paulovich <fpaulovich@gmail.com>, 
 * Roberto Pinho <robertopinho@yahoo.com.br>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.corpus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import visualizer.textprocessing.Ngram;

/**
 * This class represents a Corpus (a set of documents).
 * 
 * @author Fernando Vieira Paulovich, Roberto Pinho
 */
public abstract class Corpus {

    /**
     * Creates a new instance of Corpus
     * @param url
     * @param nrGrams 
     */
    public Corpus(String url, int nrGrams) {
        this.url = url;
        this.nrGrams = nrGrams;
    }

    /**
     * Return the raw file content
     * @param id The file id
     * @return File content
     * @throws IOException 
     */
    public abstract String getFullContent(String id) throws IOException;

    /**
     * Return the modified file content
     * @param id The file id
     * @return File content
     * @throws IOException 
     */
    public abstract String getFilteredContent(String id) throws IOException;

    /**
     * Return the modified file content - Custom for viewing
     * @param id The file id
     * @return File content
     * @throws IOException 
     */
    public abstract String getViewContent(String id) throws IOException;

    /**
     * Return the modified file content - Custom for searching
     * @param id The file id
     * @return File content
     * @throws IOException 
     */
    public abstract String getSearchContent(String id) throws IOException;

    /**
     * Return the ngrams associated with a file
     * @param id The file id
     * @throws IOException Throws an exception with a problem occurs
     * @return Return a list with the ngrams
     */
    public abstract ArrayList<Ngram> getNgrams(String id) throws IOException;

    /**
     * Return the ngrams of this corpus
     * @return
     * @throws IOException
     */
    public abstract ArrayList<Ngram> getCorpusNgrams() throws IOException;

    /**
     * This method must be implemented to fill all attributes (urls, and 
     * cdata) of a corpus.
     */
    protected abstract void run();

    /**
     * Get the corpus url.
     * @return The corpus url.
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Get the cdata of the documents belonging to this corpus.
     * @return The cdata of the corpus documents.
     */
    public float[] getClassData() {
        return this.cdata;
    }

    /**
     * Get the ids of the documents belonging to this corpus.
     * @return The ids of the corpus documents.
     */
    public ArrayList<String> getIds() {
        return this.ids;
    }

    /**
     * Return the encoding of this corpus. 
     * @return The corpus enconding.
     */
    public static Encoding getEncoding() {
        return encoding;
    }

    /**
     * Returns the number of grams used to create this data set.
     * @return The number of grams.
     */
    public int getNumberGrams() {
        return this.nrGrams;
    }

    /**
     * Get the title of a document belonging to this corpus.
     * @param nrLines The number of lines used to construct the title.
     * @param id The document id
     * @return The title of a documents.
     * @throws java.io.IOException 
     */
    public String getTitle(int nrLines, String id) throws IOException {
        String title = "";

        if (nrLines > 0) {
            String content = this.getFullContent(id);
            StringTokenizer tokenizer = new StringTokenizer(content, "\r\n");

            int i = 0;
            while (i < nrLines && tokenizer.hasMoreTokens()) {
                String line = tokenizer.nextToken();
                line = line.replaceAll("<.*?>", "");

                if (line.trim().length() > 0) {
                    title += line.trim() + " ";
                    i++;
                }
            }

            title = title.trim();
        }

        return title;
    }

    /**
     * Changes the corpus encoding.
     * @param aEncoding The new encoding.
     */
    public static void setEncoding(Encoding aEncoding) {
        encoding = aEncoding;
    }

    protected float[] cdata;
    protected ArrayList<String> ids;
    protected String url;
    protected int nrGrams;
    protected static Encoding encoding = Encoding.ASCII;
}
