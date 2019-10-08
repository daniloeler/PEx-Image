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

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class GoogleSearch {

    /** Creates a new instance of GoogleSearch */
    public GoogleSearch(String searchKey, GoogleView googleView) {
        this.googleView = googleView;
        this.searchKey = searchKey;
    }

    public void search(final String query, int numberDocuments) {
        this.numberRequestedDocs = numberDocuments;
        this.query = query;

        this.t = new Thread() {

            @Override
            public void run() {
                String googleCorpus = "google/google_" + Calendar.getInstance().getTimeInMillis() + ".zip";
                GoogleGetter getter = new GoogleGetter(GoogleSearch.this);
                getter.search(query, searchKey, numberRequestedDocs / 10, googleCorpus);

                //Salva no arquivo de buscas, essa busca para depois
                //disponibilizar para o usuário
                Calendar calendar = GregorianCalendar.getInstance();
                int day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
                int month = calendar.get(GregorianCalendar.MONTH) + 1;
                int year = calendar.get(GregorianCalendar.YEAR);
                int hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
                int minute = calendar.get(GregorianCalendar.MINUTE);
                int second = calendar.get(GregorianCalendar.SECOND);
                int millisecond = calendar.get(GregorianCalendar.MILLISECOND);

                String queryId = query + " [" + day + "/" + month + "/" + year + " - " + hour + ":" + minute + ":" + second + ":" + millisecond + "]";
                GoogleSearch.this.query = queryId;
                GoogleSearchManager manager = GoogleSearchManager.getInstance();
                manager.newQuery(queryId, googleCorpus);

                googleView.queryFinished(GoogleSearch.this);
            }

        };
        t.start();
    }

    public void stopSearch() {
        if (this.t != null && this.t.isAlive()) {
            this.t.interrupt();
            this.googleView.removeQuery(this);
        }
    }

    public int getNumberRequestedDocuments() {
        return this.numberRequestedDocs;
    }

    public int getNumberFetchedDocuments() {
        return this.numberFetchedDocs;
    }

    public void setNumberFetchedDocuments(int numberDocuments) {
        this.numberFetchedDocs = numberDocuments;
        this.googleView.updateTable();
    }

    public String getQuery() {
        return this.query;
    }

    private GoogleView googleView;
    private Thread t;
    private String searchKey;
    private String query;
    private int numberRequestedDocs;
    private int numberFetchedDocs;
}
