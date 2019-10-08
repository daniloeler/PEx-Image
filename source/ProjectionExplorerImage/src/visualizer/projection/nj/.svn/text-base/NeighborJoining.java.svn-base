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
 * of the original code is Ana Maria Cuadros Valdivia,
 * Fernando Vieira Paulovich <fpaulovich@gmail.com>.
 *
 * Contributor(s): Rosane Minghim <rminghim@icmc.usp.br>
 *
 * You should have received a copy of the GNU General Public License along 
 * with PEx. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package visualizer.projection.nj;

import java.util.ArrayList;
import visualizer.projection.distance.DistanceMatrix;
import static java.lang.Math.*;

/**
 *
 * @author Ana Maria, Fernando Vieira Paulovich
 */
public class NeighborJoining extends Content {

    /** Creates a new instance of NeighborJoining */
    public NeighborJoining(int numberPoints) {
        this.numberPoints = numberPoints;
        m_PosJun = numberPoints;
        m_pCluster = numberPoints;
        m_Fil = numberPoints + (numberPoints - 2);
    }

    public void doNeighbor(DistanceMatrix dmat) {
        m_pTempMatH = new Content[m_Fil];

        for (int k = 0; k < m_Fil; k++) {
            m_pTempMatH[k] = new Content();
        }

        this.createDistanceMatrix(dmat);

        do {
            Divergence_V = this.calDivergence(m_PosJun);
            this.matrixDistance(Divergence_V, m_PosJun, m_pCluster);
            this.branchLengths(Divergence_V, m_pCluster);
            this.nodeJoin(m_PosJun);
            this.distNewNode(m_PosJun);
            m_pCluster--;
            m_PosJun++;
        } while (m_pCluster > 2);

        this.getarestas();
        this.radialLayout(m_PosJun - 1);
    }

    public float[] calDivergence(int m_PosJun) {
        float m_pSum;
        int m_pSizeV = m_PosJun;
        float[] m_vDivergence = new float[m_pSizeV];
        int p = m_Fil - 1;

        for (int i = 0; i < m_pSizeV; i++) {
            if (m_pTempMatH[i].m_pFlag >= 0) {
                m_pSum = 0;
                for (int j = 0; (j < i); j++) {
                    if (m_pTempMatH[j].m_pFlag >= 0) {
                        m_pSum += this.matriz[j][p - i];
                    }
                }
                for (int k = i + 1; (k < m_pSizeV); k++) {
                    if (m_pTempMatH[k].m_pFlag >= 0) {
                        m_pSum += this.matriz[i][p - k];
                    }
                }

                m_vDivergence[i] = m_pSum;
            } else {
                m_vDivergence[i] = 0;
            }
        }

        return m_vDivergence;
    }

    public void matrixDistance(float[] r, int m_PosJun, int m_PCluster) {
        float m_pValue;
        float m_pMinDistance = 0;

        int m_pSizeV = m_PosJun;
        int p = m_Fil - 1;


        for (int i = 0; i < m_pSizeV - 1; i++) {
            if (m_pTempMatH[i].m_pFlag >= 0) {
                for (int j = i + 1; (j < m_pSizeV); j++) {
                    if (m_pTempMatH[j].m_pFlag >= 0) {
                        m_pValue = matriz[i][p - j] - ((r[i] + r[j]) / (m_PCluster - 2));

                        if ((m_pValue < m_pMinDistance) || m_pMinDistance == 0) {
                            m_pMinDistance = m_pValue;
                            this.m_Posx = i;
                            this.m_Posy = j;

                        }
                    }
                }
            }
        }
    }

    public void branchLengths(float[] r, int m_PCluster) {
        int A = this.m_Posx;
        int B = this.m_Posy;
        int p = m_Fil - 1;

        this.m_Dx = (0.5f * matriz[A][p - B] + (r[A] - r[B]) / (2 * (m_PCluster - 2)));
        this.m_Dy = matriz[A][p - B] - this.m_Dx;
    }

    void nodeJoin(int m_PosJun) {
        m_pTempMatH[m_PosJun].m_pFolha1 = this.m_Posx;
        m_pTempMatH[m_PosJun].m_pFolha2 = this.m_Posy;
        m_pTempMatH[m_PosJun].m_pFlag = 1;
        m_pTempMatH[m_PosJun].m_pFlagP = 0;
        m_pTempMatH[m_PosJun].m_pDis_F = 0;
        m_pTempMatH[m_PosJun].m_pFlagV = 0;
        m_pTempMatH[m_PosJun].m_pFlagT = 0;

        m_pTempMatH[m_PosJun].t = 0;
        m_pTempMatH[m_PosJun].x = 0;
        m_pTempMatH[m_PosJun].y = 0;
        m_pTempMatH[m_PosJun].m_pInter = -3; //it haven't intern node

        if (m_pTempMatH[m_Posx].m_pFlag == 0) {
            m_pTempMatH[m_Posx].m_pFolha1 = m_PosJun;
            m_pTempMatH[m_Posx].m_pDis_F = this.m_Dx;
            m_pTempMatH[m_Posx].m_pFlag = -1;
        } else {
            m_pTempMatH[m_Posx].m_pInter = m_PosJun;
            m_pTempMatH[m_Posx].m_pDis_F = this.m_Dx;
            m_pTempMatH[m_Posx].m_pFlag = -2;
        }

        if (m_pTempMatH[m_Posy].m_pFlag == 0) {
            m_pTempMatH[m_Posy].m_pFolha1 = m_PosJun;
            m_pTempMatH[m_Posy].m_pDis_F = this.m_Dy;
            m_pTempMatH[m_Posy].m_pFlag = -1;
        } else {
            m_pTempMatH[m_Posy].m_pInter = m_PosJun;
            m_pTempMatH[m_Posy].m_pDis_F = this.m_Dy;
            m_pTempMatH[m_Posy].m_pFlag = -2;
        }
    }

    public void distNewNode(int m_PosJun) {
        float m_pMinDistance = 0;
        float m_pTempX, m_pTempY;
        int p = m_Fil - 1;
        int m_pInt = 0;
        int i;
        int m_pPosX = this.m_Posx;
        int m_pPosY = this.m_Posy;

        for (i = 0; i < m_PosJun; i++) {

            if (m_pTempMatH[i].m_pFlag >= 0) {

                if (m_pPosX > i) {
                    m_pTempX = matriz[i][p - m_pPosX];
                } else {
                    m_pTempX = matriz[m_pPosX][p - i];
                }
                if (m_pPosY > i) {
                    m_pTempY = matriz[i][p - m_pPosY];
                } else {
                    m_pTempY = matriz[m_pPosY][p - i];
                }

                m_pMinDistance = 0.5f * (m_pTempX + m_pTempY - matriz[m_pPosX][p - m_pPosY]);
                matriz[i][p - m_PosJun] = m_pMinDistance;
            }
        }

        if ((m_pCluster - 1) == 2) {
            for (int a = 0; a < p; a++) {
                if (m_pTempMatH[a].m_pInter == -3) {
                    m_pInt = a + m_pInt;
                }
            }

            m_pTempMatH[m_PosJun].m_pInter = m_pInt;
            m_pTempMatH[m_pInt].m_pInter = m_PosJun;
            m_pTempMatH[m_PosJun].m_pDis_F = m_pMinDistance;
            m_pTempMatH[m_pInt].m_pDis_F = m_pMinDistance;
            m_pTempMatH[m_PosJun].m_pFlag = -2;
            m_pTempMatH[m_pInt].m_pFlag = -2;
        }
    }

    public void createDistanceMatrix(DistanceMatrix dmat) {
        matriz = new float[m_Fil][];
        for (int i = 0, f = m_Fil; i < m_Fil; i++, f--) {
            matriz[i] = new float[f];
            for (int k = 0; k < f; k++) {
                matriz[i][k] = 0.0f;
            }
        }

        int f = m_Fil - 1;
        for (int l = 0; l < this.numberPoints - 1; l++, f--) {
            for (int k = this.numberPoints - 2, j = 0; k < f; k++, j++) {
                matriz[l][k] = dmat.getDistance(l, (this.numberPoints - 1) - j);
            }
        }
    }

    public void getarestas() {
        int m_temp;
        for (int b = 0; b < m_Fil; b++) {

            if (m_pTempMatH[b].m_pFlagP != 1) {
                m_temp = m_pTempMatH[b].m_pFolha1;
                newickSon(m_temp);
            }
        }
    }

    public void newickSon(int i) {
        int m_pSon1 = m_pTempMatH[i].m_pFolha1;
        int m_pSon2 = m_pTempMatH[i].m_pFolha2;
        int m_pSon3 = m_pTempMatH[i].m_pInter;


        m_pTempMatH[i].m_pFlagP = 1;
        if ((m_pTempMatH[m_pSon1].m_pFlag == -1) && (m_pTempMatH[m_pSon1].m_pFlagP != 1)) {
            A_Arestas aux = new A_Arestas();
            aux.addiele(m_pTempMatH[m_pSon1].m_pDis_F, i, m_pSon1);
            Varesta.add(aux);
            m_pTempMatH[m_pSon1].m_pFlagP = 1;
        }

        if ((m_pTempMatH[m_pSon1].m_pFlag == -2) && (m_pTempMatH[m_pSon1].m_pFlagP != 1)) {
            newickSon(m_pSon1);
            A_Arestas aux = new A_Arestas();
            aux.addiele(m_pTempMatH[m_pSon1].m_pDis_F, i, m_pSon1);
            Varesta.add(aux);
            m_pTempMatH[m_pSon1].m_pFlagP = 1;
        }


        if ((m_pTempMatH[m_pSon2].m_pFlag == -1) && (m_pTempMatH[m_pSon2].m_pFlagP != 1)) {
            A_Arestas aux = new A_Arestas();
            aux.addiele(m_pTempMatH[m_pSon2].m_pDis_F, i, m_pSon2);
            Varesta.add(aux);

            m_pTempMatH[m_pSon2].m_pFlagP = 1;
        }
        if ((m_pTempMatH[m_pSon2].m_pFlag == -2) && (m_pTempMatH[m_pSon2].m_pFlagP != 1)) {
            newickSon(m_pSon2);
            A_Arestas aux = new A_Arestas();
            aux.addiele(m_pTempMatH[m_pSon2].m_pDis_F, i, m_pSon2);
            Varesta.add(aux);
            m_pTempMatH[m_pSon2].m_pFlagP = 1;
        }

        if ((m_pSon3 > 0) && (m_pTempMatH[m_pSon3].m_pFlagP != 1)) {
            newickSon(m_pSon3);
            A_Arestas aux = new A_Arestas();
            aux.addiele(m_pTempMatH[i].m_pDis_F, i, m_pSon3);
            Varesta.add(aux);
            m_pTempMatH[m_pSon3].m_pFlagP = 1;

        }
        m_pTempMatH[i].m_pFlagP = 1;
    }

    public float get_dis_are(int i) {
        //ArrayList<A_Arestas> aux = new ArrayList<A_Arestas>();
        A_Arestas aresta = Varesta.get(i);
        float a = aresta.dist;
        return a;

    }

    public int get_source_are(int i) {
        //ArrayList<A_Arestas> aux = new ArrayList<A_Arestas>();
        A_Arestas aresta = Varesta.get(i);
        int a = aresta.source;
        return a;

    }

    public int get_target_are(int i) {
        //ArrayList<A_Arestas> aux = new ArrayList<A_Arestas>();
        A_Arestas aresta = Varesta.get(i);
        int a = aresta.target;
        return a;

    }

    public int get_size() {
        return Varesta.size();

    }

    public void radialLayout(int pos) {
        for (int i = 0; i < m_pTempMatH.length; i++) {
            m_pTempMatH[i].m_pDis_F = 1.0f / (1.0f + m_pTempMatH[i].m_pDis_F);
        }

        postorderTraversal(pos);
        m_pTempMatH[pos].m_pFlagV = -2;
        m_pTempMatH[pos].w = 2 * (float) Math.PI;
        m_pTempMatH[pos].t = 0;
        preorderTraversal(pos, pos);
    }

    private void postorderTraversal(int i) {
        if ((m_pTempMatH[i].m_pFlag == -1) && (m_pTempMatH[i].m_pFlagT != 1)) {
            m_pTempMatH[i].m_pFlagT = 1;
            m_pTempMatH[i].lv++;
        } else {
            int m_pSon1 = m_pTempMatH[i].m_pFolha1;
            int m_pSon2 = m_pTempMatH[i].m_pFolha2;
            int m_pSon3 = m_pTempMatH[i].m_pInter;

            if (m_pSon3 == -3) {
                int a = 1;
            }

            m_pTempMatH[i].lv = 0;
            m_pTempMatH[i].m_pFlagT = 1;

            if (m_pTempMatH[m_pSon1].m_pFlagT != 1) {
                postorderTraversal(m_pSon1);
                m_pTempMatH[i].lv = m_pTempMatH[i].lv + m_pTempMatH[m_pSon1].lv;
            }
            if (m_pTempMatH[m_pSon2].m_pFlagT != 1) {
                postorderTraversal(m_pSon2);
                m_pTempMatH[i].lv = m_pTempMatH[i].lv + m_pTempMatH[m_pSon2].lv;
            }

            if (m_pTempMatH[m_pSon3].m_pFlagT != 1) {
                postorderTraversal(m_pSon3);
                m_pTempMatH[i].lv = m_pTempMatH[i].lv + m_pTempMatH[m_pSon3].lv;
            }
        }
    }

    private void preorderTraversal(int i, int p) {
        int m_pSon1 = m_pTempMatH[i].m_pFolha1;
        int m_pSon2 = m_pTempMatH[i].m_pFolha2;
        int m_pSon3 = m_pTempMatH[i].m_pInter;
        float np;

        if ((m_pTempMatH[i].m_pFlagV != -2) && (m_pTempMatH[i].m_pFlagP != 2)) {
            if ((m_pTempMatH[i].m_pFlag == -1) || (i < p)) {
                float m_pDis;
                if (m_pTempMatH[i].m_pFlag == -1) {
                    m_pDis = m_pTempMatH[i].m_pDis_F / 10;
                } else {
                    m_pDis = m_pTempMatH[i].m_pDis_F;
                }

                m_pTempMatH[i].x = (float) (m_pTempMatH[p].x + (m_pDis) * ((float) Math.cos(m_pTempMatH[i].t + (m_pTempMatH[i].w / 2))));
                m_pTempMatH[i].y = (float) (m_pTempMatH[p].y + (m_pDis) * ((float) Math.sin(m_pTempMatH[i].t + (m_pTempMatH[i].w / 2))));
            } else {
                m_pTempMatH[i].x = (float) (m_pTempMatH[p].x + ((m_pTempMatH[p].m_pDis_F) * ((float) cos(m_pTempMatH[i].t + (m_pTempMatH[i].w / 2)))));
                m_pTempMatH[i].y = (float) (m_pTempMatH[p].y + ((m_pTempMatH[p].m_pDis_F) * ((float) sin(m_pTempMatH[i].t + (m_pTempMatH[i].w / 2)))));
            }

        } else {
            T = m_pTempMatH[i].lv;

        }

        m_pTempMatH[i].m_pFlagP = 2;
        np = m_pTempMatH[i].t;

        int parent = i;
        if ((m_pTempMatH[m_pSon1].m_pFlagV != 1) && (m_pTempMatH[m_pSon1].m_pFlagP != 2)) {
            m_pTempMatH[m_pSon1].w = (m_pTempMatH[m_pSon1].lv / T) * (float) (2 * Math.PI);
            m_pTempMatH[m_pSon1].t = np;
            np = np + m_pTempMatH[m_pSon1].w;
            preorderTraversal(m_pSon1, parent);
        }

        if ((m_pTempMatH[m_pSon2].m_pFlagV != 1) && (m_pTempMatH[m_pSon2].m_pFlagP != 2) && (m_pTempMatH[i].m_pFlag != -1)) {
            m_pTempMatH[m_pSon2].w = (m_pTempMatH[m_pSon2].lv / T) * (float) (2 * Math.PI);
            m_pTempMatH[m_pSon2].t = np;
            np = np + m_pTempMatH[m_pSon2].w;
            preorderTraversal(m_pSon2, parent);
        }

        if ((m_pTempMatH[m_pSon3].m_pFlagV != 1) && (m_pTempMatH[m_pSon3].m_pFlagP != 2) && (m_pTempMatH[i].m_pFlag != -1)) {
            m_pTempMatH[m_pSon3].w = (m_pTempMatH[m_pSon3].lv / T) * (float) (2 * Math.PI);
            m_pTempMatH[m_pSon3].t = np;
            np = np + m_pTempMatH[m_pSon3].w;
            preorderTraversal(m_pSon3, parent);
        }

    }

    public float getX(int i) {
        return m_pTempMatH[i].x;
    }

    public float getY(int i) {
        return m_pTempMatH[i].y;
    }

    private int numberPoints;
    private int m_pCluster;
    private int m_Fil;
    private float T;
    private Content[] m_pTempMatH;
    private float Divergence_V[];
    private float[][] matriz;
    private int m_Posx;
    private int m_Posy;
    private float m_Dx;
    private float m_Dy;
    private int m_PosJun;
    private ArrayList<A_Arestas> Varesta = new ArrayList<A_Arestas>();
}
