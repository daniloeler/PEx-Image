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

package visualizer.graph.listeners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import visualizer.graph.Scalar;
import visualizer.graph.Vertex;
import visualizer.util.PExConstants;
import visualizer.util.OpenDialog;
import visualizer.view.Viewer;
import visualizer.topic.Topic;
import visualizer.topic.TopicData;
import visualizer.topic.TopicFactory;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class CreateTopicSelectionListener extends VertexSelectionListener {

    /** Creates a new instance of CreateTopicSelectionListener
     * @param panel 
     */
    public CreateTopicSelectionListener(Viewer panel) {
        super(panel);
        this.color = java.awt.Color.YELLOW;
    }

    public ArrayList<Vertex> vertexSelected(Object param, ArrayList<Vertex> vertex) {
        if (vertex.size() > 0) {
            Scalar topicScalar = panel.getGraph().addScalar(PExConstants.TOPICS);

            if (panel.getGraph().getTopicData().getTopicType() == TopicData.TopicType.COVARIANCE) {
                if (OpenDialog.checkCorpus(panel.getGraph(), panel.getProjectionExplorerView())) {
                    Topic topic = TopicFactory.getInstance(panel.getGraph(),
                            panel.getGraph().getTopicData(), panel.getGraph().getCorpus(), vertex);
                    panel.addTopic(topic);

                    panel.updateScalars(topicScalar); //panel.getGraph().getScalarByName(PExConstants.TOPICS)
                    panel.updateImage();

                    ///////////////////////COORDINATION
                    //use to coordinate topics between different projections
                    if (topic != null) {
                        CoordinateSelectionListener coord = new CoordinateSelectionListener(this.panel);
                        String query = topic.getQuery();

                        if (query != null) {
                            coord.vertexSelected(query, vertex);
                        }
                    }
                }
            } else {
                if (OpenDialog.checkCorpus(panel.getGraph(), panel.getProjectionExplorerView())) {
                    Topic topic = TopicFactory.getInstance(panel.getGraph(),
                            panel.getGraph().getTopicData(), panel.getGraph().getCorpus(), vertex);
                    createQueryFromTopic(topic);
                    panel.addTopic(topic);
                }
            }
        }

        return null;
    }

    private void createQueryFromTopic(Topic topic) {
        String query = topic.getQuery();

        if (query != null && !query.equals("")) {
            try {
                Scalar s = panel.getGraph().createQueryScalar(query);
                panel.updateScalars(s);
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
