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
 * Contributor(s): Roberto Pinho <robertopinho@yahoo.com.br>, 
 *                 Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.projection.distance.kolmogorov;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import visualizer.util.LZOCompressor;
import visualizer.corpus.Corpus;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class NormalCompressDistance {

   public NormalCompressDistance(CompressorType comptype, Corpus corpus) {
      this.comptype = comptype;
      this.corpus = corpus;
      this.filenames = this.corpus.getIds();
   }

   public float calculateNewNCD(int filenameIndex1, int filenameIndex2) {
      if (this.c.isEmpty() && this.ncdXX.isEmpty()) {
         this.calculateAllC();
         this.calculateAllNcdXX();
      }

      float ncd = this.calculateNCD(filenameIndex1, filenameIndex2);
      float cxx = this.ncdXX.get(filenameIndex1);
      float cyy = this.ncdXX.get(filenameIndex2);

      return (ncd - ((cxx + cyy) / 2));
   }

   public float calculateNewNCD(String filename1, String filename2) {
      float ncd = this.calculateNCD(filename1, filename2);
      float cxx = this.calculateNCD(filename1, filename1);
      float cyy = this.calculateNCD(filename2, filename2);

      return (ncd - ((cxx + cyy) / 2));
   }

   public float calculateNCD(int filenameIndex1, int filenameIndex2) {
      if (this.c.isEmpty()) {
         this.calculateAllC();
      }

      long cx = this.c.get(filenameIndex1);
      long cy = this.c.get(filenameIndex2);
      long cxy = this.calculateCombinedC(this.filenames.get(filenameIndex1),
              this.filenames.get(filenameIndex2));
      return (cxy - Math.min(cx, cy)) / (float) (Math.max(cx, cy));
   }

   public float calculateNCD(String filename1, String filename2) {
      long cx = this.calculateC(filename1);
      long cy = this.calculateC(filename2);
      long cxy = this.calculateCombinedC(filename1, filename2);
      return (cxy - Math.min(cx, cy)) / (float) (Math.max(cx, cy));
   }

   protected long calculateC(String filename) {
      try {
         ByteArrayOutputStream dest = new ByteArrayOutputStream();
         //zip.DeflaterOutputStream out=null;
         OutputStream out = null;

         if (this.comptype == CompressorType.BZIP2) {
            out = this.compressUsingZip(dest);
         } else {
            out = this.compressUsingGzip(dest);
         }

         String tmp = "";
         tmp += this.corpus.getFullContent(filename);
         System.out.println(filename);

         if (out != null) {
            out.write(tmp.getBytes(), 0, tmp.length());
            //out.finish();
            out.flush();
            out.close();
         }
         if (this.comptype == CompressorType.LZO1) {
            return (long) this.compressUsingLzo(tmp);
         } else {
            return (long) dest.size();
         }
      } catch (IOException ex) {
         Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
      }

      return -1;
   }

   protected long calculateCombinedC(String filename1, String filename2) {
      try {
         ByteArrayOutputStream dest = new ByteArrayOutputStream();
         //zip.DeflaterOutputStream out=null;
         OutputStream out = null;

         if (this.comptype == CompressorType.BZIP2) {
            out = this.compressUsingZip(dest);
         } else {
            out = this.compressUsingGzip(dest);
         }

         String tmp = this.corpus.getFullContent(filename1) + " " +
                 this.corpus.getFullContent(filename2);

         if (out != null) {
            out.write(tmp.getBytes(), 0, tmp.length());
            out.flush();
            out.close();
         }

         if (this.comptype == CompressorType.LZO1) {
            return (long) this.compressUsingLzo(tmp);
         } else {
            return (long) dest.size();
         }

      } catch (IOException ex) {
         Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
      }

      return -1;
   }

   protected void calculateAllC() {
      try {
         for (String filename : this.filenames) {
            ByteArrayOutputStream dest = new ByteArrayOutputStream();
            //zip.DeflaterOutputStream out=null;
            OutputStream out = null;

            if (this.comptype == CompressorType.BZIP2) {
               out = this.compressUsingZip(dest);
            } else {
               out = this.compressUsingGzip(dest);
            }

            String tmp = this.corpus.getFullContent(filename);
            if (out != null) {
               out.write(tmp.getBytes(), 0, tmp.length());
               out.flush();
               out.close();
            }

            if (this.comptype == CompressorType.LZO1) {
               this.c.add((long) this.compressUsingLzo(tmp));
            } else {
               this.c.add((long) dest.size());
            }
         }
      } catch (IOException ex) {
         Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
      }
   }

   private void calculateAllNcdXX() {
      for (String filename : this.filenames) {
         float xx = this.calculateNCD(filename, filename);
         this.ncdXX.add(xx);
      }
   }

   protected OutputStream compressUsingGzip(ByteArrayOutputStream dest) throws IOException {
      return new GZIPOutputStream(new BufferedOutputStream(dest));
   }

   protected OutputStream compressUsingZip(ByteArrayOutputStream dest) throws IOException {
      ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
      out.setMethod(ZipOutputStream.DEFLATED);
      ZipEntry entry = new ZipEntry("");
      out.putNextEntry(entry);
      return out;
   }

   protected long compressUsingLzo(String dest) throws IOException {
      LZOCompressor LZO = new LZOCompressor();
      return LZO.compressUsingLZO(dest);
   }
   
   private Corpus corpus;
   private ArrayList<String> filenames = new ArrayList<String>();
   private CompressorType comptype = CompressorType.BZIP2;
   private ArrayList<Long> c = new ArrayList<Long>();
   private ArrayList<Float> ncdXX = new ArrayList<Float>();
}
