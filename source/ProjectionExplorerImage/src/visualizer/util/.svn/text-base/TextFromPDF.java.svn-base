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

/*
 * TextFromPDF.java
 *
 * Created on 29 de Julho de 2006, 12:39
 */
package visualizer.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JTextArea;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class TextFromPDF {

    public void extract(JTextArea status, String directory, String corpora) throws java.io.IOException {
        ProcessAll process = new ProcessAll(status, directory, corpora);

        if (process.getPdfFiles(directory).size() > 0) {
            process.start();
        } else {
            throw new java.io.IOException("The directory does not contain PDF files!");
        }
    }

    class ProcessAll extends Thread {

        public ProcessAll(JTextArea status, String directory, String corpora) {
            this.status = status;
            this.directory = directory;
            this.corpora = corpora;
        }

        public ArrayList<File> getPdfFiles(String directory) {
            ArrayList<File> pdfFiles = new ArrayList<File>();

            File f = new File(directory);
            File[] files = f.listFiles();

            for (File file : files) {
                if (file.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
                    pdfFiles.add(file);
                }
            }

            return pdfFiles;
        }

        @Override
        public void run() {
            File f = new File(directory);
            PDFProcessor proc = null;
            ZipOutputStream zout = null;

            try {
                FileOutputStream dest = new FileOutputStream(corpora);
                zout = new ZipOutputStream(new BufferedOutputStream(dest));
                zout.setMethod(ZipOutputStream.DEFLATED);

                ArrayList<File> pdfFiles = getPdfFiles(directory);

                for (File pdf : pdfFiles) {
                    while (proc != null && proc.isAlive()) {
                    }

                    status.append("Converting: " + pdf.getName() + "\n");
                    proc = new PDFProcessor(status, pdf.getAbsolutePath(), zout);
                    proc.start();
                }

            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (zout != null) {
                    try {
                        while (proc != null && proc.isAlive()) {
                        }
                        zout.flush();
                        zout.finish();
                        zout.close();
                        status.append("FINISHED...");
                    } catch (IOException ex) {
                        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        private JTextArea status;
        private String directory;
        private String corpora;
    }

    class PDFProcessor extends Thread implements ActionListener {

        public static final int time = 15000;
        public PDFProcessor(JTextArea status, String pdfFile, ZipOutputStream zout) {
            this.pdfFile = pdfFile;
            this.zout = zout;
            this.status = status;
        }

        @Override
        public void run() {
            PDDocument doc = null;

            try {
                String textFile = pdfFile.substring(pdfFile.lastIndexOf(System.getProperty("file.separator")) + 1, pdfFile.lastIndexOf(".")) + ".txt";
                ZipEntry entry = new ZipEntry(textFile);
                zout.putNextEntry(entry);

                doc = PDDocument.load(pdfFile);
                String txt = this.extract(doc, 1, doc.getNumberOfPages());
                zout.write(txt.getBytes(), 0, txt.length());

            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (this.timer != null) {
                    timer.stop();
                }

                try {
                    if (doc != null) {
                        doc.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        @Override
        public void start() {
            this.timer = new javax.swing.Timer(PDFProcessor.time, this);
            this.timer.start();
            super.start();
        }

        public void actionPerformed(ActionEvent e) {
            this.status.append("Time exceeded: " + this.pdfFile + "\n");
            if (this.timer != null) {
                timer.stop();
            }
            this.interrupt();
        }

        private String extract(PDDocument doc, int startPage, int endPage) throws java.io.IOException {
            PDFTextStripper stripper = new PDFTextStripper();
            if (startPage != 0 && endPage != 0) {
                stripper.setStartPage(startPage);
                stripper.setEndPage(endPage);
            }

            return stripper.getText(doc);
        }

        private String pdfFile;
        private javax.swing.Timer timer;
        private ZipOutputStream zout;
        private JTextArea status;
    }

}
