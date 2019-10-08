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

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import visualizer.corpus.Corpus;
import visualizer.corpus.CorpusFactory;
import visualizer.graph.Graph;
import visualizer.graph.Vertex;
import visualizer.util.filefilter.PExFileFilter;
import visualizer.util.filefilter.ZIPFilter;
import visualizer.graph.ImageCollection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class OpenDialog {

   public static int showOpenDialog(PExFileFilter filter, Component parent) {
      if (OpenDialog.dialog == null) {
         OpenDialog.dialog = new javax.swing.JFileChooser();
      }

      _filename = null;

      dialog.resetChoosableFileFilters();
      dialog.setAcceptAllFileFilterUsed(false);
      if (filter != null) {
         dialog.setFileFilter(filter);
      }
      dialog.setMultiSelectionEnabled(false);
      dialog.setDialogTitle("Open file");
      dialog.setSelectedFile(new File(""));

      SystemPropertiesManager m = SystemPropertiesManager.getInstance();
      dialog.setCurrentDirectory(new File(m.getProperty(filter.getProperty())));

      int result = dialog.showOpenDialog(parent);
      if (result == JFileChooser.APPROVE_OPTION) {
         _filename = dialog.getSelectedFile().getAbsolutePath();
         m.setProperty(filter.getProperty(), dialog.getSelectedFile().getParent());
      }

      return result;
   }

   public static boolean forceCheck(Graph graph, java.awt.Component parent) {
      if (OpenDialog.dialog == null) {
         OpenDialog.dialog = new javax.swing.JFileChooser();
      }

      _filename = null;
      String Filename = new String();
      String Extension = ".txt";
      
      java.util.Vector<String> FileUrls = new java.util.Vector<String>();
      int count = 0;
      int valid = 0;

      if (graph != null) {
         dialog.resetChoosableFileFilters();
         dialog.setAcceptAllFileFilterUsed(false);
         dialog.setFileFilter(new ZIPFilter());
         dialog.setMultiSelectionEnabled(false);
         dialog.setSelectedFile(new File(""));
         dialog.setDialogTitle("Force open file");

         SystemPropertiesManager m = SystemPropertiesManager.getInstance();
         dialog.setCurrentDirectory(new File(m.getProperty("ZIP.DIR")));

         int result = dialog.showOpenDialog(parent);
         if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            String fileName = dialog.getSelectedFile().getAbsolutePath();
            m.setProperty("ZIP.DIR", dialog.getSelectedFile().getParent());

            // Reading Files on ZIP
            if (fileName != null) {
               //Capturing the filenames of the zip file
               ZipFile zip = null;
               try {
                  java.util.Vector<String> filenames_aux = new java.util.Vector<String>();

                  zip = new ZipFile(fileName);
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
                  for (int i = 0, j = 0; j < index.length; i++, j++) {
                     int ind = (int) Math.pow(2, i);

                     if (ind >= index_aux.size()) {
                        ind = 0;
                     }

                     index[j] = index_aux.get(ind);
                     index_aux.remove(ind);
                  }

                  for (int i = 0; i < filenames_aux.size(); i++) {
                     FileUrls.add(filenames_aux.elementAt(index[i]));
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
            // End Reading ZIP Files
            
            if (FileUrls.elementAt(0).endsWith(".txt")) {
               Corpus corpus = CorpusFactory.getInstance(fileName, graph.getProjectionData());

               //checking how many documents on the graph are in the corpus
               Extension = ".txt";
               for (Vertex v : graph.getVertex()) {
                  Filename = v.getUrl();
                  if (Filename.lastIndexOf('.') != -1) {
                     Filename = Filename.substring(0, v.getUrl().lastIndexOf('.'));
                  }
                  //v.setUrl(Filename);
                  if (corpus.getIds().contains(Filename + Extension)) {
                     count++;
                  }
                  if (v.isValid()) {
                     valid++;
                    // v.setDrawAs(Vertex.DRAW_AS_CIRCLES);
                  }

               }
            } else if (FileUrls.elementAt(0).endsWith(".jpg")) {
               ImageCollection ic = new ImageCollection(fileName);

               //checking how many documents on the graph are in the corpus
               Extension = ".jpg";
               for (Vertex v : graph.getVertex()) {
                  Filename = v.getUrl().trim();
                  if (Filename.lastIndexOf('.') != -1) {
                     Filename = Filename.substring(0, v.getUrl().lastIndexOf('.'));
                  }
                  //v.setUrl(Filename);
                  if (ic.getImageUrls().contains(Filename + Extension)) {
                     count++;
                  }
                  if (v.isValid()) {
                     valid++;
                  }
               }
            }

            if (count != valid) {
               String message = "Only " + count + " corresponding files were found in the corpus!\n" +
                       "The projection has " + valid + " files. Would like to proceed?";
               int answer = JOptionPane.showOptionDialog(dialog, message, "Openning Warning",
                       JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);

               if (answer == JOptionPane.NO_OPTION) {
                  return false;
               }
            }
            
            for (Vertex v : graph.getVertex()) {
               if (v.isValid()) {
                  Filename = v.getUrl();
                  if(Filename.lastIndexOf('.') != -1)
                     Filename = Filename.substring(0, v.getUrl().lastIndexOf('.'));
                  v.setUrl(Filename + Extension);
               }
            }
            
            if(FileUrls.elementAt(0).endsWith(".jpg")){
               ImageCollection ic = new ImageCollection(fileName);
               graph.setImageCollection(ic);
            }
            else if(FileUrls.elementAt(0).endsWith(".txt")){
               Corpus corpus = CorpusFactory.getInstance(fileName, graph.getProjectionData());
               graph.setCorpus(corpus);
               graph.getProjectionData().setSourceFile(fileName);
            }

            return true;
         } else {
            return false;
         }
      }
      return true;
   }

   public static boolean checkCorpus(Graph graph, java.awt.Component parent) {
      if (OpenDialog.dialog == null) {
         OpenDialog.dialog = new javax.swing.JFileChooser();
      }

      _filename = null;

      if (graph != null && graph.getCorpus() == null) {
         dialog.resetChoosableFileFilters();
         dialog.setAcceptAllFileFilterUsed(false);
         dialog.setFileFilter(new ZIPFilter());
         dialog.setMultiSelectionEnabled(false);
         dialog.setSelectedFile(new File(""));
         dialog.setDialogTitle("Open CORPUS file");

         SystemPropertiesManager m = SystemPropertiesManager.getInstance();
         dialog.setCurrentDirectory(new File(m.getProperty("ZIP.DIR")));

         int result = dialog.showOpenDialog(parent);
         if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            String fileName = dialog.getSelectedFile().getAbsolutePath();
            m.setProperty("ZIP.DIR", dialog.getSelectedFile().getParent());

            Corpus corpus = CorpusFactory.getInstance(fileName, graph.getProjectionData());

            //checking how many documents on the graph are in the corpus
            int count = 0;
            int valid = 0;
            for (Vertex v : graph.getVertex()) {
               String filename = v.getUrl();
               if (corpus.getIds().contains(filename)) {
                  count++;
               }
               if (v.isValid()) {
                  valid++;
               }
            }

            if (count != valid) {
               String message = "Only " + count + " corresponding files were found in the corpus!\n" +
                       "The projection has " + valid + " files. Would like to proceed?";
               int answer = JOptionPane.showOptionDialog(dialog, message, "Openning Warning",
                       JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);

               if (answer == JOptionPane.NO_OPTION) {
                  return false;
               }
            }

            graph.setCorpus(corpus);
            graph.getProjectionData().setSourceFile(fileName);

            return true;
         } else {
            return false;
         }
      }
      return true;
   }

   public static boolean checkImages(Graph graph, java.awt.Component parent) {
      if (OpenDialog.dialog == null) {
         OpenDialog.dialog = new javax.swing.JFileChooser();
      }

      _filename = null;

      if (graph != null && graph.getImageCollection() == null) {
         dialog.resetChoosableFileFilters();
         dialog.setAcceptAllFileFilterUsed(false);
         dialog.setFileFilter(new ZIPFilter());
         dialog.setMultiSelectionEnabled(false);
         dialog.setSelectedFile(new File(""));
         dialog.setDialogTitle("Open IMAGES file");

         SystemPropertiesManager m = SystemPropertiesManager.getInstance();
         dialog.setCurrentDirectory(new File(m.getProperty("ZIP.DIR")));

         int result = dialog.showOpenDialog(parent);
         if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            String fileName = dialog.getSelectedFile().getAbsolutePath();
            m.setProperty("ZIP.DIR", dialog.getSelectedFile().getParent());

            ImageCollection ic = new ImageCollection(fileName);

            //checking how many documents on the graph are in the corpus
            int count = 0;
            int valid = 0;
            for (Vertex v : graph.getVertex()) {
               String filename = v.getUrl().trim();
               if (ic.getImageUrls().contains(filename)) {
                  count++;
               }
               if (v.isValid()) {
                  valid++;
               }
            }

            if (count != valid) {
               String message = "Only " + count + " corresponding files were found in the images file!\n" + "The projection has " + valid + " files. Would like to proceed?";
               int answer = JOptionPane.showOptionDialog(dialog, message, "Openning Warning",
                       JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);

               if (answer == JOptionPane.NO_OPTION) {
                  return false;
               }
            }

            graph.setImageCollection(ic);

            return true;
         } else {
            return false;
         }
      }
      return true;
   }
   
   public static String getFilename() {
      return _filename;
   }

   public static javax.swing.JFileChooser getJFileChooser() {
      if (OpenDialog.dialog == null) {
         OpenDialog.dialog = new javax.swing.JFileChooser();
      }

      return dialog;
   }
   private static String _filename;
   private static javax.swing.JFileChooser dialog;
}
