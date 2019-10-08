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
 * of the original code is Roberto Pinho <robertopinho@yahoo.com.br>,
 * Fernando Vieira Paulovich <fpaulovich@gmail.com>
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.google;

import com.google.soap.search.GoogleSearch;
import com.google.soap.search.GoogleSearchFault;
import com.google.soap.search.GoogleSearchResult;
import com.google.soap.search.GoogleSearchResultElement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author rpinho & Fernando Vieira Paulovich
 */
public class GoogleGetter {

    public GoogleGetter(visualizer.google.GoogleSearch gSearch) {
        this.gSearch = gSearch;
    }

    public void search(String query, String key, int numberPages, String corpusFile) {
        List<GoogleSearchResultElement> results = new ArrayList<GoogleSearchResultElement>();
        GoogleSearch s = new GoogleSearch();
        s.setKey(key);
        s.setQueryString(query);
        s.setMaxResults(10);
        s.setSafeSearch(false);

        for (int i = 0,  count = 0,  countAnt = -1; count < numberPages * 10; i++) {
            s.setStartResult(10 * i);
            try {
                GoogleSearchResult r = s.doSearch();

                for (GoogleSearchResultElement elem : r.getResultElements()) {
                    if (this.addElem(elem, results)) {
                        count++;
                    }
                    this.gSearch.setNumberFetchedDocuments(count);
                }

                if (countAnt == count) {
                    break;
                }
                countAnt = count;
            } catch (GoogleSearchFault ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.results2Zip(results, corpusFile);
    }

    private List<File> results2Files(List<GoogleSearchResultElement> Rs, String corpusFile) throws FileNotFoundException {
        BufferedReader in;
        Reader input;
        PrintWriter out;
        String line;
        File dir = new File(corpusFile.replaceFirst(".zip", "Full"));
        File outFile;
        List<File> files = new ArrayList<File>();

        if (dir.exists()) {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = (new File(dir, children[i])).delete();
                }
            }
        } else {
            dir.mkdir();
        }

        System.out.println("Retrieving results...");
        int count = -1;
        for (GoogleSearchResultElement r : Rs) {
            try {
                System.out.println("Retrieving " + ++count + " of " + Rs.size() + ":\t" + r.getURL());
                input = new InputStreamReader(loadURL(r.getURL()));
                in = new BufferedReader(input);

                String output = url2FileName(r.getURL());

                outFile = new File(dir, output);

                out = new PrintWriter(outFile);
                while ((line = in.readLine()) != null) {
                    out.println(line);
                }
                out.close();
                files.add(outFile);
            //outNames.println(r.getTitle()+"\t"+r.getURL());
            } catch (Exception e) {

                System.err.println("Could not retreive " + r.getURL());
                System.err.println(e.getMessage());
                String text = snipet2Text(r, count);
                String output = url2FileName(r.getURL());
                outFile = new File(dir, output);
                out = new PrintWriter(outFile);
                out.println(text);
                out.close();
                System.err.println("Used snipet ");

            }

        }
//		outNames.close();
        return files;
    }

    private InputStream loadURL(String url) throws Exception {


        // Determine whether URL or local path

        // Try to parse the document source as URL

        URL sourceUrl = null;
        boolean validUrlFormat = true;

        try {
            sourceUrl = new URL(url);
        } catch (MalformedURLException e) {
            validUrlFormat = false;
        }

        // If in URL format, open a connection
        if (validUrlFormat) {

            URLConnection connection = sourceUrl.openConnection();
            connection.setReadTimeout(10000);
            return connection.getInputStream();

        } else {
            return null;
        }


    }

    private boolean addElem(GoogleSearchResultElement elem, List<GoogleSearchResultElement> results) {
        boolean found = false;

        for (GoogleSearchResultElement e : results) {
            if (e.getURL().equals(elem.getURL())) {
                found = true;
                break;
            }
        }

        if (!found) {
            results.add(elem);
        }
        return !found;
    }

    private String url2FileName(String url) {
        String output = url;
        output = output.replaceAll("://", "_");
        output = output.replace('/', '_');
        output = output.replace(':', '_');
        output = output.replace('?', '_');
        output = output.replace(';', '_');
        output = output.replace('.', '_');
        output = output.replace('=', '_');
        output = output.replace('~', '_');
        output = "GOOGLE_" + output + ".html";
        return output;
    }

    private String snipet2Text(GoogleSearchResultElement re, long i) {
        DecimalFormat myFormatter2 = new DecimalFormat("0000");
        String text = myFormatter2.format(i) + " - " + re.getTitle() + "\n" +
                "<br><p>" + re.getSnippet() + "</p><br>\n" +
                "<p>" + re.getHostName() + "</p>\n" +
                "<br><p><a href=\"" + re.getURL() + "\">" + re.getURL() + "</a></p><br>";
        return text;
    }

    private void results2Zip(List<GoogleSearchResultElement> rs, String filename) {
        ZipOutputStream zos = null;

        try {
            File zipFile = new File(filename);
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            //DecimalFormat myFormatter1 = new DecimalFormat("00");
            DecimalFormat myFormatter2 = new DecimalFormat("0000");

            //int nPages=rs.size()/10;

            //loop through dirList, and zip the files
            for (int i = 0; i < rs.size(); i++) {
                //String output = myFormatter1.format(nPages) + "-" +myFormatter2.format(i)+".html";
                //if((i+1)%10==0) nPages--;


                String output = url2FileName(rs.get(i).getURL());



                ZipEntry anEntry = new ZipEntry(output);
                zos.putNextEntry(anEntry);

                String text = snipet2Text(rs.get(i), i);


                zos.write(text.getBytes());
                zos.closeEntry();
            }
        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static final int SUCCESS = 0;
    public static final int INVALID_KEY = 1;
    private visualizer.google.GoogleSearch gSearch;
}
