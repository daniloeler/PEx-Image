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

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import visualizer.graph.Connectivity;
import visualizer.graph.Graph;
import visualizer.graph.Scalar;
import visualizer.graph.Vertex;
import visualizer.graph.coodination.Coordination;
import visualizer.graph.coodination.Mapping;
import visualizer.graph.listeners.VertexSelectionFactory.SelectionType;
import visualizer.datamining.clustering.HierarchicalClusteringType;
import visualizer.view.color.ColorTable;
import visualizer.topic.Topic;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public abstract class Viewer extends javax.swing.JInternalFrame {

    public Viewer(ProjectionExplorerView pexview) {
        this.pexview = pexview;

        this.id = Viewer.avaiableId;
        Viewer.avaiableId++;
    }

    /**
     * Updates the list of scalars on the graphical interface. Also
     * selects the scalar passed as argument. If null is passed as argument,
     * only the list of scalars is update.
     * @param scalar The scalar to be selected.
     */
    public abstract void updateScalars(Scalar scalar);

    /**
     * Updates the list of connectivities on the graphical interface. Also 
     * selects the connectivity passed as argument. If null is 
     * passed as argument, only the list of connectivities is update.
     * @param connectivity  The connectivity to be selcted.
     */
    public abstract void updateConnectivities(Connectivity connectivity);

    /**
     * Updates list of coodinations on the graphical interface. Also selects 
     * the mapping passed as argument. If null is passed as argument, only the 
     * list of coordinations is update.
     * @param mapping  The mapping to be selected.
     */
    public abstract void updateCoordinations(Mapping mapping);

    /**
     * Updates the list of tiles on the graphical interface. Also selects the 
     * title passed as argument. If null is passed as argument, only the 
     * list of titles is update.
     * @param name The title to be selected.
     */
    public abstract void updateTitles(String name);

    /**
     * Returns the connectivity currently used on the graphical.
     * @return The current connectivity.
     */
    public abstract Connectivity getCurrentConnectivity();

    /**
     * Returns the scalar currently used on the graphical.
     * @return The current scalar.
     */
    public abstract Scalar getCurrentScalar();

    /**
     * Returns the depth (number of slopes on the graph) to find the neighbors
     * according to a connectivity.
     * @return The depht of a neighborhood.
     */
    public abstract int getNeighborhoodDepth();

    public abstract void setNeighborhoodDepth(int depth);

    /**
     * Saves the projection when this windows is closed.
     * @return Returns JOptionPane.OK_OPTION if the projection was saved,
     * returns JOptionPane.NO_OPTION if it was not saved, and returns
     * JOptionPane.CANCEL_OPTION if this window was not closed.
     */
    public abstract int saveOnClosing();

    /**
     * Zooming in the projection.
     */
    public abstract void zoomIn();

    /**
     * Zooming out the projection.
     */
    public abstract void zoomOut();

    /**
     * Runs the force algorithm to repositioning the nodes according to the
     * selected connectivity.
     * @return Returns true if it stop moving the nodes, and false it it is
     * start moving the nodes.
     */
    public abstract boolean runForce();

    /**
     * Clean the topics on the projection.
     */
    public abstract void cleanTopics();

    /**
     * Add a new topic to the projetion.
     * @param topic The new topic.
     */
    public abstract void addTopic(Topic topic);

    /**
     * Clean the selected nodes.
     * @param cleanVertex Set if to true to clean the vertices topics, 
     * and false otherwise.
     */
    public abstract void cleanSelection(boolean cleanVertex);

    /**
     * Selects a list of vertices.
     * @param vertices The list of vertices to be selected.
     */
    public abstract void selectVertices(ArrayList<Vertex> vertices);

    /**
     * Returns the color table associated with this window.
     * @return The color table.
     */
    public abstract ColorTable getColorTable();

    /**
     * Clean the image and re-create it.
     */
    public abstract void updateImage();

    
    /**
     * Color the nodes according to a scalar.
     * @param scalar The scalar used to color the nodes.
     */
    public abstract void colorAs(Scalar scalar);

    /**
     * Returns the vertex selected.
     * @return The selected vertex.
     */
    public abstract Vertex getSelectedVertex();

    public abstract Font getViewerFont();

    public abstract void setViewerFont(Font font);

    /**
     * Changes the viewer background.
     * @param bg The new color.
     */
    public abstract void setViewerBackground(Color bg);

    /**
     * Removes from the graph the selected vertices.
     */
    public abstract void deleteSelectedVertices();

    /**
     * Saves the projection as a PNG file.
     * @param filename The file name.
     * @throws java.io.IOException Throws an exception if something goes wrong.
     */
    public abstract void saveToPngImageFile(String filename) throws IOException;

    /**
     * Saves the projection as a EPS file.
     * @param filename The file name.
     * @throws java.io.IOException Throws an exception if something goes wrong.
     */
    public abstract void saveToEpsImageFile(String filename) throws IOException;

    public abstract ArrayList<Vertex> getSelectedVertex(Point source, Point target);

    public abstract Graph getGraph();

    public abstract String getCurrentTitle();

    public abstract void markNeighbors(Vertex vertex);

    public static boolean isMovePoints() {
        return movePoints;
    }

    public static void setMovePoints(boolean movePoints) {
        Viewer.movePoints = movePoints;
    }

    public static SelectionType getType() {
        return type;
    }

    public static void setType(SelectionType type) {
        Viewer.type = type;
    }

    public static boolean isHighlightTopic() {
        return highlightTopic;
    }

    public static void setHighlightTopic(boolean highlightTopic) {
        Viewer.highlightTopic = highlightTopic;
    }

    public boolean isHighQualityRender() {
        return highQualityRender;
    }

    public void setHighQualityRender(boolean highQualityRender) {
        this.highQualityRender = highQualityRender;
    }

    public boolean isVertexLabelVisible() {
        return vertexLabelVisible;
    }

    public void setVertexLabelVisible(boolean isVertexLabelVisible) {
        this.vertexLabelVisible = isVertexLabelVisible;
    }

    public ProjectionExplorerView getProjectionExplorerView() {
        return this.pexview;
    }

    public int getId() {
        return this.id;
    }

    public Coordination getCoordination() {
        return this.coord;
    }

    public void createHCScalars(HierarchicalClusteringType type) {
        if (this.getGraph() != null) {
            try {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "The Hierachical Clustering is a very expensive process." +
                        "\nIt can take several minutes!",
                        "WARNING", javax.swing.JOptionPane.WARNING_MESSAGE);
                Scalar s = this.getGraph().createHC(type);
                this.updateScalars(s);
                this.setGraphChanged(true);
            } catch (IOException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setGraphChanged(boolean graphChanged) {
        if (graphChanged) {
            if (!this.getTitle().endsWith("*")) {
                this.setTitle(this.getTitle() + "*");
            }
        } else {
            if (this.getTitle().endsWith("*")) {
                this.setTitle(this.getTitle().substring(0, this.getTitle().length() - 1));
            }
        }

        this.graphChanged = graphChanged;
    }
    
   public void setShowImages(boolean show) {
      Vertex.setShowTitle(false);
      Vertex.setShowContent(false);
      Vertex.setShowImage(show);
      Vertex.setShowLabel(false);

      JInternalFrame[] frames = this.pexview.getDesktop().getAllFrames();

      for (JInternalFrame ifrm : frames) {
         if (ifrm instanceof Viewer) {
            ((Viewer) ifrm).getProjectionExplorerView().refreshLists();
            ((Viewer) ifrm).updateImage();
         }
      }
   }

   public void setShowVertexContent(boolean show)
   {
      Vertex.setShowContent(show);
      Vertex.setShowTitle(false);
      Vertex.setShowImage(false);
      Vertex.setShowLabel(false);
   
      JInternalFrame[] frames = this.pexview.getDesktop().getAllFrames();

      for (JInternalFrame ifrm : frames) {
         if (ifrm instanceof Viewer) {
            ((Viewer) ifrm).getProjectionExplorerView().refreshLists();
            ((Viewer) ifrm).updateImage();
         }
      }
   }
   
   public void setShowVertexTitle(boolean show) {
      Vertex.setShowTitle(show);
      Vertex.setShowContent(false);
      Vertex.setShowImage(false);
      Vertex.setShowLabel(false);

      JInternalFrame[] frames = this.pexview.getDesktop().getAllFrames();

      for (JInternalFrame ifrm : frames) {
         if (ifrm instanceof Viewer) {
            ((Viewer) ifrm).getProjectionExplorerView().refreshLists();
            ((Viewer) ifrm).updateImage();
         }
      }
   }

    protected boolean graphChanged = false;
    protected static boolean movePoints = false;
    protected static SelectionType type = SelectionType.CREATE_TOPIC;
    protected boolean vertexLabelVisible = true;
    protected static boolean highlightTopic = false;
    protected boolean highQualityRender = true;
    protected ProjectionExplorerView pexview;
    protected int id;
    protected static int avaiableId = 1;
    protected Coordination coord;
}
