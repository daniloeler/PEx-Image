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

package visualizer.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.matrix.DenseMatrix;
import visualizer.matrix.DenseVector;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class Converter {

    public void data(String datafile, String namesfile) throws IOException {
        ArrayList<ArrayList<Float>> points = new ArrayList<ArrayList<Float>>();
        ArrayList<String> attributes = new ArrayList<String>();
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(datafile));

            String line = null;

            //Read the attributes
            while ((line = in.readLine()) != null && line.trim().length() > 0) {
                //Ignore comments
                if (line.lastIndexOf('#') == -1) {
                    StringTokenizer t = new StringTokenizer(line, ";");

                    while (t.hasMoreTokens()) {
                        String token = t.nextToken();
                        attributes.add(token.trim());
                    }

                    break;
                }
            }

            //read the points
            while ((line = in.readLine()) != null && line.trim().length() > 1) {
                //Ignore comments

                if (line.lastIndexOf('#') == -1) {
                    StringTokenizer t = new StringTokenizer(line, ";");

                    ArrayList<Float> point = new ArrayList<Float>();

                    while (t.hasMoreTokens()) {
                        String token = t.nextToken();
                        point.add(Float.parseFloat(token));
                    }

                    points.add(point);
                }
            }
        } catch (FileNotFoundException e) {
            throw new IOException("File " + datafile + " does not exist!");
        } catch (java.io.IOException e) {
            throw new IOException("Problems reading the file " + datafile + " : " + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        ArrayList<String> names = null;
        if (namesfile != null) {
            names = this.readNames(namesfile);
        }
        
        if (attributes.size() > points.get(0).size() - 1) {
            attributes.remove(attributes.size() - 1);
        }

        DenseMatrix matrix = new DenseMatrix();
        matrix.setAttributes(attributes);

        for (int i = 0; i < points.size(); i++) {
            float[] point = new float[points.get(i).size() - 1];

            for (int j = 0; j < points.get(i).size() - 1; j++) {
                point[j] = points.get(i).get(j);
            }

            float cdata = points.get(i).get(points.get(i).size() - 1);

            if (names != null) {
                matrix.addRow(new DenseVector(point, names.get(i), cdata));
            } else {
                matrix.addRow(new DenseVector(point, Integer.toString(i), cdata));
            }
        }

        matrix.save(datafile.replaceAll(".data", "") + "_new.data");
    }

    private ArrayList<String> readNames(String namesfile) throws IOException {
        ArrayList<String> names = new ArrayList<String>();

        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(namesfile));
            String line = null;

            while ((line = in.readLine()) != null) {
                if (line.trim().length() > 0) {
                    names.add(line.trim());
                }
            }

        } catch (FileNotFoundException e) {
            throw new IOException("File \"" + namesfile + "\" was not found!");
        } catch (IOException e) {
            throw new IOException("Problems reading the file \"" + namesfile + "\"");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        return names;
    }

    public static void main(String[] args) {
        try {
            String datafile = "D:\\My Documents\\FERNANDO\\development2_Normalized.data";
            String namesfile = "D:\\My Documents\\FERNANDO\\development2_Normalized.names";
            Converter conv = new Converter();
            conv.data(datafile, namesfile);
        } catch (IOException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
