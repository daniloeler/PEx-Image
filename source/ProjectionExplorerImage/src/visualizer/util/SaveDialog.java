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
import visualizer.util.filefilter.PExFileFilter;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class SaveDialog {

    public static int showSaveDialog(PExFileFilter filter, Component parent,
            String directory, String filename) {
        if (SaveDialog.dialog == null) {
            SaveDialog.createDialog();
        }

        _filename = null;

        dialog.resetChoosableFileFilters();
        dialog.setAcceptAllFileFilterUsed(false);
        if (filter != null) {
            dialog.setFileFilter(filter);
        }
        dialog.setMultiSelectionEnabled(false);
        dialog.setDialogTitle("Save file");
        dialog.setCurrentDirectory(new File(directory));

        if (filename != null && filename.length() > 0) {
            dialog.setSelectedFile(new File(filename));
        } else {
            dialog.setSelectedFile(new File(""));
        }

        int result = dialog.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            _filename = dialog.getSelectedFile().getAbsolutePath();

            //checking if the name finishes with the correct extension
            if (!_filename.toLowerCase().endsWith("." + filter.getFileExtension())) {
                _filename = _filename.concat("." + filter.getFileExtension());
            }
        }

        return result;
    }

    public static int showSaveDialog(PExFileFilter filter, Component parent,
            String filename) {

        if (SaveDialog.dialog == null) {
            SaveDialog.createDialog();
        }

        _filename = null;

        dialog.resetChoosableFileFilters();
        dialog.setAcceptAllFileFilterUsed(false);
        if (filter != null) {
            dialog.setFileFilter(filter);
        }
        dialog.setMultiSelectionEnabled(false);
        dialog.setDialogTitle("Save file");

        if (filename != null && filename.length() > 0) {
            filename = filename.substring(0, filename.lastIndexOf('.')) + "." +
                    filter.getFileExtension().toLowerCase();
            dialog.setSelectedFile(new File(filename));
        } else {
            dialog.setSelectedFile(new File(""));
        }

        SystemPropertiesManager m = SystemPropertiesManager.getInstance();
        dialog.setCurrentDirectory(new File(m.getProperty(filter.getProperty())));

        int result = dialog.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            _filename = dialog.getSelectedFile().getAbsolutePath();
            m.setProperty(filter.getProperty(), dialog.getSelectedFile().getParent());

            //checking if the name finishes with the correct extension
            if (!_filename.toLowerCase().endsWith("." + filter.getFileExtension())) {
                _filename = _filename.concat("." + filter.getFileExtension());
            }
        }

        return result;
    }

    public static int showSaveDialog(PExFileFilter filter, Component parent) {
        if (SaveDialog.dialog == null) {
            SaveDialog.createDialog();
        }

        _filename = null;

        dialog.resetChoosableFileFilters();
        dialog.setAcceptAllFileFilterUsed(false);
        if (filter != null) {
            dialog.setFileFilter(filter);
        }
        dialog.setMultiSelectionEnabled(false);
        dialog.setDialogTitle("Save file");

        SystemPropertiesManager m = SystemPropertiesManager.getInstance();
        dialog.setCurrentDirectory(new File(m.getProperty(filter.getProperty())));

        int result = dialog.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            _filename = dialog.getSelectedFile().getAbsolutePath();
            m.setProperty(filter.getProperty(), dialog.getSelectedFile().getParent());

            //checking if the name finishes with the correct extension
            if (!_filename.toLowerCase().endsWith("." + filter.getFileExtension())) {
                _filename = _filename.concat("." + filter.getFileExtension());
            }
        }

        return result;
    }

    public static String getFilename() {
        return _filename;
    }

    private static void createDialog() {
        dialog = new JFileChooser() {

            @Override
            public void approveSelection() {
                File file = getSelectedFile();
                if (file != null && file.exists()) {
                    String message = "The file \"" + file.getName() + "\" already exists. \n" +
                            "Do you want to replace the existing file?";
                    int answer = JOptionPane.showOptionDialog(this, message, "Save Warning",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);

                    if (answer == JOptionPane.NO_OPTION) {
                        return;
                    }
                }

                super.approveSelection();
            }
        };
    }
    private static String _filename;
    private static javax.swing.JFileChooser dialog;
}
