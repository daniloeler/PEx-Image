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

package visualizer.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.plaf.basic.BasicComboPopup;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class JExtendedComboBox extends JComboBox {

    public JExtendedComboBox() {
        super();
    }

    public JExtendedComboBox(ComboBoxModel aModel) {
        super(aModel);
    }

    public JExtendedComboBox(Vector vector) {
        super(vector);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        this.resizeComboPopup();
    }

    @Override
    public void firePopupMenuWillBecomeVisible() {
        this.resizeComboPopup();
        super.firePopupMenuWillBecomeVisible();
    }

    private void resizeComboPopup() {
        BasicComboPopup popup = (BasicComboPopup) getUI().getAccessibleChild(this, 0);

        if (popup == null) {
            return;
        }

        JComboBox aux = new JComboBox(this.getModel());
        int width = aux.getPreferredSize().width;
        width = (width < this.getPreferredSize().width) ? this.getPreferredSize().width : width;

        Component comp = popup.getComponent(0);

        FontMetrics fm = getFontMetrics(getFont());
        int height = fm.getHeight() * getItemCount() + 10;
        if (height > fm.getHeight() * getMaximumRowCount() + 10) {
            height = fm.getHeight() * getMaximumRowCount() + 10;
        }

        popup.setPreferredSize(new Dimension(width, height));
        popup.setLayout(new BorderLayout());
        popup.add(comp, BorderLayout.CENTER);
    }

}
