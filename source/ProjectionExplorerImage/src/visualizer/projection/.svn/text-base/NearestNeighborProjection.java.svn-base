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

package visualizer.projection;

import visualizer.projection.distance.DistanceMatrix;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public class NearestNeighborProjection extends Projector {

    public float[][] project(DistanceMatrix dmat) {

        float[][] projection = new float[dmat.getElementCount()][];
        for (int i = 0; i < dmat.getElementCount(); i++) {
            projection[i] = new float[2];
        }

        //Project the fisrt two data points such that the distance between them in the bidimensional
        //subspace is equal to the distance in the original multidimensional space.
        projection[0][0] = 0.0f;
        projection[0][1] = 0.0f;
        projection[1][0] = 0.0f;
        projection[1][1] = dmat.getDistance(0, 1);

        //For each other point (x)
        for (int x = 2; x < dmat.getElementCount(); x++) {
            int q = 0, r = 1;

            //Perform a kNN query in the subset of the projected point X'
            //returning the two nearest neighborr q and r of x
            float minDistance1 = dmat.getMaxDistance();
            float minDistance2 = dmat.getMaxDistance();

            for (int _ins = 0; _ins < x; _ins++) {
                float distance = dmat.getDistance(x, _ins);

                if (minDistance1 > distance) {
                    r = q;
                    minDistance2 = minDistance1;

                    q = _ins;
                    minDistance1 = distance;
                } else if (minDistance2 > distance) {
                    minDistance2 = distance;
                    r = _ins;
                }
            }

            //Find the intersection points of the two bidimensional circles with center
            //in the projected data points q' and r' and radii equal to d(x,q) and
            //d(x,r) respectively
            Circle circle1 = new Circle();
            Circle circle2 = new Circle();

            circle1.center.x = projection[q][0];
            circle1.center.y = projection[q][1];
            circle1.radius = dmat.getDistance(q, x);

            circle2.center.x = projection[r][0];
            circle2.center.y = projection[r][1];
            circle2.radius = dmat.getDistance(r, x);

            if (circle1.center.x - circle2.center.x < EPSILON) {
                circle1.center.x += EPSILON;
            }

            if (circle1.center.y - circle2.center.y < EPSILON) {
                circle1.center.y += EPSILON;
            }

            //the resulting intersect
            Pair resultingIntersections = new Pair();

            //calculates the intersect and returns the number of intersect found
            int number = intersect(circle1, circle2, resultingIntersections);

            //there is one intersection or none intersection
            if (number == 1) {
                projection[x][0] = resultingIntersections.first.x;
                projection[x][1] = resultingIntersections.first.y;
            } else { //there are two intersections

                //choice the point that minimize the error
                float distanceQX = dmat.getDistance(q, x);
                float distanceRX = dmat.getDistance(r, x);

                float distanceQX1 = (float) Math.sqrt(((projection[q][0] - resultingIntersections.first.x) * (projection[q][0] - resultingIntersections.first.x)) +
                        ((projection[q][1] - resultingIntersections.first.y) * (projection[q][1] - resultingIntersections.first.y)));

                float distanceQX2 = (float) Math.sqrt(((projection[q][0] - resultingIntersections.second.x) * (projection[q][0] - resultingIntersections.second.x)) +
                        ((projection[q][1] - resultingIntersections.second.y) * (projection[q][1] - resultingIntersections.second.y)));

                float distanceRX1 = (float) Math.sqrt(((projection[r][0] - resultingIntersections.first.x) * (projection[r][0] - resultingIntersections.first.x)) +
                        ((projection[r][1] - resultingIntersections.first.y) * (projection[r][1] - resultingIntersections.first.y)));

                float distanceRX2 = (float) Math.sqrt(((projection[r][0] - resultingIntersections.second.x) * (projection[r][0] - resultingIntersections.second.x)) +
                        ((projection[r][1] - resultingIntersections.second.y) * (projection[r][1] - resultingIntersections.second.y)));

                if (Math.abs(((distanceQX / distanceQX1) - 1)) + Math.abs(((distanceRX / distanceRX1) - 1)) <
                        Math.abs(((distanceQX / distanceQX2) - 1)) + Math.abs(((distanceRX / distanceRX2) - 1))) {
                    projection[x][0] = resultingIntersections.first.x;
                    projection[x][1] = resultingIntersections.first.y;
                } else {
                    projection[x][0] = resultingIntersections.second.x;
                    projection[x][1] = resultingIntersections.second.y;
                }
            }
        }

        this.normalize2D(projection);

        return projection;
    }

    private int intersect(Circle circle1, Circle circle2, Pair intersect) {
        float lvdistance = (float) Math.sqrt(((circle1.center.x - circle2.center.x) * (circle1.center.x - circle2.center.x)) +
                ((circle1.center.y - circle2.center.y) * (circle1.center.y - circle2.center.y)));

        if (lvdistance > (circle1.radius + circle2.radius)) {
            float lvdifference = lvdistance - (circle1.radius + circle2.radius);

            if (circle1.center.x < circle2.center.x) {
                float m = (circle2.center.y - circle1.center.y) / (circle2.center.x - circle1.center.x);
                intersect.first.x = circle1.center.x;
                intersect.first.y = circle1.center.y;

                float lvdeloc = circle1.radius;
                lvdeloc += lvdifference / 2;

                intersect.first.x += Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                intersect.first.y += m * Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
            } else {
                float m = (circle1.center.y - circle2.center.y) / (circle1.center.x - circle2.center.x);
                intersect.first.x = circle2.center.x;
                intersect.first.y = circle2.center.y;

                float lvdeloc = circle2.radius;
                lvdeloc += lvdifference / 2;

                intersect.first.x += Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                intersect.first.y += m * Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
            }

            return 1;
        }

        if (lvdistance < Math.abs(circle1.radius - circle2.radius)) {
            float lvdeloc;
            if (circle1.radius > circle2.radius) {
                lvdeloc = circle2.radius + lvdistance + (circle1.radius - circle2.radius - lvdistance) / 2;
                float m = (circle2.center.y - circle1.center.y) / (circle2.center.x - circle1.center.x);

                intersect.first.x = circle1.center.x;
                intersect.first.y = circle1.center.y;

                if ((circle2.center.y >= circle1.center.y) && (circle2.center.x >= circle1.center.x)) {
                    intersect.first.x += Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                    intersect.first.y += Math.abs(m) * Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                } else if ((circle2.center.y >= circle1.center.y) && (circle2.center.x <= circle1.center.x)) {
                    intersect.first.x -= Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                    intersect.first.y += Math.abs(m) * Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                } else if ((circle2.center.y <= circle1.center.y) && (circle2.center.x <= circle1.center.x)) {
                    intersect.first.x -= Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                    intersect.first.y -= Math.abs(m) * Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                } else {
                    intersect.first.x += Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                    intersect.first.y -= Math.abs(m) * Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                }
            } else {
                lvdeloc = circle1.radius + lvdistance + (circle2.radius - circle1.radius - lvdistance) / 2;
                float m = (circle1.center.y - circle2.center.y) / (circle1.center.x - circle2.center.x);

                intersect.first.x = circle2.center.x;
                intersect.first.y = circle2.center.y;

                if ((circle1.center.y >= circle2.center.y) && (circle1.center.x >= circle2.center.x)) {
                    intersect.first.x += Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                    intersect.first.y += Math.abs(m) * Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                } else if ((circle1.center.y >= circle2.center.y) && (circle1.center.x <= circle2.center.x)) {
                    intersect.first.x -= Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                    intersect.first.y += Math.abs(m) * Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                } else if ((circle1.center.y <= circle2.center.y) && (circle1.center.x <= circle2.center.x)) {
                    intersect.first.x -= Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                    intersect.first.y -= Math.abs(m) * Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                } else {
                    intersect.first.x += Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                    intersect.first.y -= Math.abs(m) * Math.sqrt(lvdeloc * lvdeloc / (1 + m * m));
                }
            }

            return 1;
        }

        float a = (circle1.radius * circle1.radius - circle2.radius * circle2.radius + lvdistance * lvdistance) / (2 * lvdistance);

        float h = (float) Math.sqrt(circle1.radius * circle1.radius - a * a);

        float x2 = circle1.center.x + a * (circle2.center.x - circle1.center.x) / lvdistance;

        float y2 = circle1.center.y + a * (circle2.center.y - circle1.center.y) / lvdistance;

        float x31 = x2 + h * (circle2.center.y - circle1.center.y) / lvdistance;

        float x32 = x2 - h * (circle2.center.y - circle1.center.y) / lvdistance;

        float y31 = y2 - h * (circle2.center.x - circle1.center.x) / lvdistance;

        float y32 = y2 + h * (circle2.center.x - circle1.center.x) / lvdistance;

        intersect.first.x = x31;
        intersect.first.y = y31;

        intersect.second.x = x32;
        intersect.second.y = y32;

        return 2;
    }

    class Point {

        float x;
        float y;
    }

    class Pair {

        Point first = new Point();
        Point second = new Point();
    }

    class Circle {

        @Override
        public String toString() {
            return "center: (" + center.x + ", " + center.y + ") and radius: " + radius;
        }

        Point center = new Point();
        float radius;
    }

    private static final float EPSILON = 0.00001f;
}
