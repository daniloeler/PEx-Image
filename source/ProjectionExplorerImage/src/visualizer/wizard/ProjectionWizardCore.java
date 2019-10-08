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
package visualizer.wizard;

import visualizer.graph.Graph;
import visualizer.projection.GraphBuilder;
import visualizer.projection.ProjectionData;
import visualizer.projection.ProjectionFactory;
import visualizer.projection.SourceType;
import visualizer.projection.distance.DissimilarityType;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class ProjectionWizardCore {

    public ProjectionWizardCore(Graph graph) {
        this.graph = graph;

        this.preprocessView = new Preprocessing(this.graph.getProjectionData());
        this.projDistView = new ProjectionDistanceChoice(this.graph.getProjectionData());
        this.sourceView = new DataSourceChoice(this.graph.getProjectionData());
        this.generalParametersView = new GeneralParameters(this.graph.getProjectionData());
        this.dimensionReductionView = new DimensionReduction(this.graph.getProjectionData());
    }

    public WizardPanel getNextPanel(int direction) {
        ProjectionData pdata = this.graph.getProjectionData();

        switch (this.currentState) {
            case ProjectionWizardCore.INITIAL_STATE:
                //initial -> source
                this.currentState = ProjectionWizardCore.SOURCE_STATE;
                return this.sourceView.reset();

            case ProjectionWizardCore.SOURCE_STATE:
                //source -> projection+distance
                if (direction == ProjectionWizardCore.NEXT_STATE) {
                    if (pdata.getSourceType() == SourceType.IMAGES) {
                        // janela de configuracao de parametro de extracao
                        //this.currentState = ProjectionWizardCore.EXTRACTION_STATE;
                        //return this.projDistView.reset();
                    } else {
                        this.currentState = ProjectionWizardCore.PROJ_DIST_STATE;
                        return this.projDistView.reset();
                    }

                }
                break;

            case ProjectionWizardCore.EXTRACTION_STATE:
                //source -> projection+distance
                if (direction == ProjectionWizardCore.NEXT_STATE) {

                        this.currentState = ProjectionWizardCore.PROJ_DIST_STATE;
                        return this.projDistView.reset();

                }
                else {
                        this.currentState = ProjectionWizardCore.SOURCE_STATE;
                        return this.sourceView.reset();
                    }
               // break;

            case ProjectionWizardCore.PROJ_DIST_STATE:
                //projection+distance -> source
                if (direction == ProjectionWizardCore.PREVIOUS_STATE) {
                    if (pdata.getSourceType() == SourceType.IMAGES) {
                        this.currentState = ProjectionWizardCore.EXTRACTION_STATE;
                        //return visaoExtraction
                    } else {
                        this.currentState = ProjectionWizardCore.SOURCE_STATE;
                        return this.sourceView.reset();
                    }
                } else {
                    if (pdata.getSourceType() == SourceType.CORPUS &&
                            pdata.getDissimilarityType() != DissimilarityType.KOLMOGOROV) {
                        //projection+distance -> pre-processing
                        this.currentState = ProjectionWizardCore.PRE_PROC_STATE;
                        return this.preprocessView;
                    } else if (pdata.getSourceType() == SourceType.POINTS) {
                        //projection+distance -> dimension reduction
                        this.currentState = ProjectionWizardCore.DIMEN_RED_STATE;
                        return this.dimensionReductionView.reset();
                    } else if (pdata.getSourceType() == SourceType.IMAGES) {
                        this.currentState = ProjectionWizardCore.DIMEN_RED_STATE;
                        return this.dimensionReductionView.reset();
                    } else {
                        //projection+distance -> general parameters
                        this.currentState = ProjectionWizardCore.GEN_PARAM_STATE;
                        return this.generalParametersView.reset();
                    }
                }

            case ProjectionWizardCore.PRE_PROC_STATE:
                //pre-processing -> projection+distance
                if (direction == ProjectionWizardCore.PREVIOUS_STATE) {
                    this.currentState = ProjectionWizardCore.PROJ_DIST_STATE;
                    return this.projDistView.reset();
                } else {
                    //pre-processing -> dimension reduction
                    this.currentState = ProjectionWizardCore.DIMEN_RED_STATE;
                    return this.dimensionReductionView.reset();
                }

            case ProjectionWizardCore.DIMEN_RED_STATE:
                if (direction == ProjectionWizardCore.PREVIOUS_STATE) {
                    if (pdata.getSourceType() == SourceType.CORPUS &&
                            pdata.getDissimilarityType() != DissimilarityType.KOLMOGOROV) {
                        //dimension reduction -> pre-processing
                        this.currentState = ProjectionWizardCore.PRE_PROC_STATE;
                        return this.preprocessView;
                    } else {
                        //dimension reduction -> projection+distance
                        this.currentState = ProjectionWizardCore.PROJ_DIST_STATE;
                        return this.projDistView.reset();
                    }
                } else {
                    //dimension reduction -> general parameters
                    this.currentState = ProjectionWizardCore.GEN_PARAM_STATE;
                    return this.generalParametersView.reset();
                }

            case ProjectionWizardCore.GEN_PARAM_STATE:
                if (direction == ProjectionWizardCore.PREVIOUS_STATE) {

                    if ((pdata.getSourceType() == SourceType.CORPUS &&
                            pdata.getDissimilarityType() != DissimilarityType.KOLMOGOROV) ||
                            pdata.getSourceType() == SourceType.POINTS) {
                        //general parameters -> dimension reduction
                        this.currentState = ProjectionWizardCore.DIMEN_RED_STATE;
                        return this.dimensionReductionView.reset();
                    } else {
                        if (pdata.getSourceType() == SourceType.IMAGES) {
                            this.currentState = ProjectionWizardCore.DIMEN_RED_STATE;
                            return this.dimensionReductionView.reset();
                        } else {
                            //general parameters -> projection+distance
                            this.currentState = ProjectionWizardCore.PROJ_DIST_STATE;
                            return this.projDistView.reset();
                        }
                    }

                } else {
                    projView = ProjectionFactory.getInstance(pdata.getProjectionType()).getProjectionView(pdata);
                    projView.reset();

                    this.currentState = ProjectionWizardCore.PROJECT_STATE;

                    return projView;
                }

            case ProjectionWizardCore.PROJECT_STATE:
                if (direction == ProjectionWizardCore.PREVIOUS_STATE) {
                    //idmap -> general parameters
                    this.currentState = ProjectionWizardCore.GEN_PARAM_STATE;
                    return this.generalParametersView.reset();
                } else {
                    if (projView != null) {
                        builder = new GraphBuilder(projView, this.graph);
                        builder.start();
                    }
                }
                break;
        }

        return null;
    }

    public void stopProcess() {
        if (this.builder != null) {
            this.builder.stop();
        }
    }    //Possible directions to follow in the wizard
    public static final int NEXT_STATE = 0;
    public static final int PREVIOUS_STATE = 1;
    //States of create projection wizard
    private static final int INITIAL_STATE = 0;
    private static final int SOURCE_STATE = 1;
    private static final int PROJ_DIST_STATE = 2;
    private static final int PRE_PROC_STATE = 3;
    private static final int DIMEN_RED_STATE = 4;
    private static final int GEN_PARAM_STATE = 5;
    private static final int PROJECT_STATE = 6;
    private static final int EXTRACTION_STATE = 7;
    //Views of each state
    private ProjectionDistanceChoice projDistView;
    private DataSourceChoice sourceView;
    private ProjectionView projView;
    private Preprocessing preprocessView;
    private GeneralParameters generalParametersView;
    private DimensionReduction dimensionReductionView;
    //Current state of wizard
    private int currentState = INITIAL_STATE;
    private Graph graph;
    private GraphBuilder builder;
}
