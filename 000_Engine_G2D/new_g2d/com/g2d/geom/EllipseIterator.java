/*
 * @(#)EllipseIterator.java	1.13 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.g2d.geom;

import java.util.NoSuchElementException;

/**
 * A utility class to iterate over the path segments of an ellipse
 * through the PathIterator interface.
 *
 * @version 10 Feb 1997
 * @author	Jim Graham
 */
class EllipseIterator implements PathIterator {
    double x, y, w, h;
    AffineTransform affine;
    int index;

    EllipseIterator(Ellipse2D e, AffineTransform at) {
	this.x = e.getX();
	this.y = e.getY();
	this.w = e.getWidth();
	this.h = e.getHeight();
	this.affine = at;
	if (w < 0 || h < 0) {
	    index = 6;
	}
    }

    /**
     * Return the winding rule for determining the insideness of the
     * path.
     * @see #WIND_EVEN_ODD
     * @see #WIND_NON_ZERO
     */
    public int getWindingRule() {
	return WIND_NON_ZERO;
    }

    /**
     * Tests if there are more points to read.
     * @return true if there are more points to read
     */
    public boolean isDone() {
	return index > 5;
    }

    /**
     * Moves the iterator to the next segment of the path forwards
     * along the primary direction of traversal as long as there are
     * more points in that direction.
     */
    public void next() {
	index++;
    }

    // ArcIterator.btan(Math.PI/2)
    public static final double CtrlVal = 0.5522847498307933;

    /*
     * ctrlpts contains the control points for a set of 4 cubic
     * bezier curves that approximate a circle of radius 0.5
     * centered at 0.5, 0.5
     */
    private static final double pcv = 0.5 + CtrlVal * 0.5;
    private static final double ncv = 0.5 - CtrlVal * 0.5;
    private static double ctrlpts[][] = {
	{  1.0,  pcv,  pcv,  1.0,  0.5,  1.0 },
	{  ncv,  1.0,  0.0,  pcv,  0.0,  0.5 },
	{  0.0,  ncv,  ncv,  0.0,  0.5,  0.0 },
	{  pcv,  0.0,  1.0,  ncv,  1.0,  0.5 }
    };

    /**
     * Returns the coordinates and type of the current path segment in
     * the iteration.
     * The return value is the path segment type:
     * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
     * A float array of length 6 must be passed in and may be used to
     * store the coordinates of the point(s).
     * Each point is stored as a pair of float x,y coordinates.
     * SEG_MOVETO and SEG_LINETO types will return one point,
     * SEG_QUADTO will return two points,
     * SEG_CUBICTO will return 3 points
     * and SEG_CLOSE will not return any points.
     * @see #SEG_MOVETO
     * @see #SEG_LINETO
     * @see #SEG_QUADTO
     * @see #SEG_CUBICTO
     * @see #SEG_CLOSE
     */
    public int currentSegment(float[] coords) {
	if (isDone()) {
	    throw new NoSuchElementException("ellipse iterator out of bounds");
	}
	if (index == 5) {
	    return SEG_CLOSE;
	}
	if (index == 0) {
	    double ctrls[] = ctrlpts[3];
	    coords[0] = (float) (x + ctrls[4] * w);
	    coords[1] = (float) (y + ctrls[5] * h);
	    if (affine != null) {
		affine.transform(coords, 0, coords, 0, 1);
	    }
	    return SEG_MOVETO;
	}
	double ctrls[] = ctrlpts[index - 1];
	coords[0] = (float) (x + ctrls[0] * w);
	coords[1] = (float) (y + ctrls[1] * h);
	coords[2] = (float) (x + ctrls[2] * w);
	coords[3] = (float) (y + ctrls[3] * h);
	coords[4] = (float) (x + ctrls[4] * w);
	coords[5] = (float) (y + ctrls[5] * h);
	if (affine != null) {
	    affine.transform(coords, 0, coords, 0, 3);
	}
	return SEG_CUBICTO;
    }

    /**
     * Returns the coordinates and type of the current path segment in
     * the iteration.
     * The return value is the path segment type:
     * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
     * A double array of length 6 must be passed in and may be used to
     * store the coordinates of the point(s).
     * Each point is stored as a pair of double x,y coordinates.
     * SEG_MOVETO and SEG_LINETO types will return one point,
     * SEG_QUADTO will return two points,
     * SEG_CUBICTO will return 3 points
     * and SEG_CLOSE will not return any points.
     * @see #SEG_MOVETO
     * @see #SEG_LINETO
     * @see #SEG_QUADTO
     * @see #SEG_CUBICTO
     * @see #SEG_CLOSE
     */
    public int currentSegment(double[] coords) {
	if (isDone()) {
	    throw new NoSuchElementException("ellipse iterator out of bounds");
	}
	if (index == 5) {
	    return SEG_CLOSE;
	}
	if (index == 0) {
	    double ctrls[] = ctrlpts[3];
	    coords[0] = x + ctrls[4] * w;
	    coords[1] = y + ctrls[5] * h;
	    if (affine != null) {
		affine.transform(coords, 0, coords, 0, 1);
	    }
	    return SEG_MOVETO;
	}
	double ctrls[] = ctrlpts[index - 1];
	coords[0] = x + ctrls[0] * w;
	coords[1] = y + ctrls[1] * h;
	coords[2] = x + ctrls[2] * w;
	coords[3] = y + ctrls[3] * h;
	coords[4] = x + ctrls[4] * w;
	coords[5] = y + ctrls[5] * h;
	if (affine != null) {
	    affine.transform(coords, 0, coords, 0, 3);
	}
	return SEG_CUBICTO;
    }
}
