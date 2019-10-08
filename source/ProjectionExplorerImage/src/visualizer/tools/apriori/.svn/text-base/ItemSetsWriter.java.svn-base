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
 * Fernando Vieira Paulovich <fpaulovich@gmail.com>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

/*
 * ItemSetsWriter.java
 *
 * Created on 6 de Outubro de 2006, 16:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package visualizer.tools.apriori;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import visualizer.corpus.Corpus;

/**
 *
 * @author robertopinho
 */
public class ItemSetsWriter {

    /** Creates a new instance of ItemSetsWriter */
    public ItemSetsWriter() {
    }

    public static void outRulesDocs(String rootFileName, float[][] allPoints, List<ItemSet> rules, Corpus datasource) {

        PrintWriter out;

        try {

            out = new PrintWriter(rootFileName + ".ruledoc.data");
            ZipOutputStream outZip =
                    new ZipOutputStream(new FileOutputStream(rootFileName + ".rules.zip"));

            for (int i = 0; i < rules.size(); i++) {
                List<Integer> items;
                items = rules.get(i).getItems();

                outZip.putNextEntry(new ZipEntry(rules.get(i).getRuleFileName()));
                outZip.write((rules.get(i).getRuleFileName() + "\n").getBytes(), 0, (rules.get(i).getRuleFileName() + "\n").getBytes().length);
                for (int j = 0; j < allPoints.length; j++) {

                    int s = 1;
                    for (int k = 0; k < items.size(); k++) {
                        s = s * ((allPoints[j][items.get(k).intValue()] > 0) ? 1 : 0);
                    }
                    if (s == 1) {
                        out.print("1 ");
                        outZip.write((datasource.getIds().get(j) + "\n").getBytes(), 0, (datasource.getIds().get(j) + "\n").getBytes().length);

                    } else {
                        out.print("0 ");
                    }
                }
                out.println();
                outZip.closeEntry();
            }
            out.close();
            outZip.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ItemSetsWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ItemSetsWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void outRulesNames(String rootFileName, List<ItemSet> rules) {

        PrintWriter out;
        try {

            out = new PrintWriter(rootFileName + ".rules.names");
            for (int i = 0; i < rules.size(); i++) {
                out.println(rules.get(i).getRuleFileName());
            }
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ItemSetsWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
