/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2005-2007 Universidade de Sao Paulo, Sao Carlos/SP, Brazil.
 * All Rights Reserved.
 *
 * This file is part of Projection Explorer for Imaging (PEx-Image).
 *
 * How to cite this work:
 *
 *
 * PEx-Image is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * PEx-Image is distributed in the hope that it will be useful, but WITHOUT 
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
 * with PEx-Image. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

/*
 * ImageCollection.java
 *
 * Created on 12 de Novembro de 2007, 17:05
 *
 */
package visualizer.graph;

import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;


public class ImageCollection {

    /** Creates a new instance of ImageCollection */
    public ImageCollection(String filename) {
        this.filename = filename;
        this.extractImageUrls();
    }

    public Image getImage(String url) throws java.io.IOException {
        ZipFile zip = new ZipFile(this.filename);
        ZipEntry entry = zip.getEntry(url.trim());

        if (entry != null) {
            InputStream is = zip.getInputStream(entry);
            return ImageIO.read(is);
        } else {
            return null;
        }
    }

    public java.util.Vector<String> getImageUrls() {
        return this.imageUrls;
    }

    private void extractImageUrls() {
        if (this.filename != null) {
            this.imageUrls = new java.util.Vector<String>();

            //Capturing the filenames of the zip file
            ZipFile zip = null;
            try {
                java.util.Vector<String> filenames_aux = new java.util.Vector<String>();

                zip = new ZipFile(this.filename);
                java.util.Enumeration entries = zip.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    if (!entry.isDirectory()) {
                        ZipEntry entry2 = zip.getEntry(entry.getName());
                        if (entry2 != null) {
                            filenames_aux.add(entry2.getName().trim());
                        }
                    }
                }

                //deterministically shuffle the files
                java.util.ArrayList<Integer> index_aux = new java.util.ArrayList<Integer>();
                for (int i = 0; i < filenames_aux.size(); i++) {
                    index_aux.add(i);
                }

                int[] index = new int[filenames_aux.size()];
                for (int i = 0,  j = 0; j < index.length; i++, j++) {
                    int ind = (int) Math.pow(2, i);

                    if (ind >= index_aux.size()) {
                        ind = 0;
                    }

                    index[j] = index_aux.get(ind);
                    index_aux.remove(ind);
                }

                for (int i = 0; i < filenames_aux.size(); i++) {
                    this.imageUrls.add(filenames_aux.elementAt(index[i]));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (zip != null) {
                    try {
                        zip.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private String filename;
    private java.util.Vector<String> imageUrls;
}
