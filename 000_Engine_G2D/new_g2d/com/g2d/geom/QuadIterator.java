/*
 * @(#)QuadIterator.java	1.10 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.g2d.geom;

import java.util.NoSuchElementException;

/**
 * A utility class to iterate over the path segments of a quadratic curve
 * segment through the PathIterator interface.
 *
 * @version 10 Feb 1997
 * @author	Jim Graham
 */
class QuadIterator implements PathIterator {
    QuadCurve2D quad;
    AffineTransform affine;
    int index;

    QuadIterator(QuadCurve2D q, AffineTransform at) {
	this.quad = q;
	this.affine = at;
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
	return (index > 1);
    }

    /**
     * Moves the iterator to the next segment of the path forwards
     * along the primary direction of traversal as long as there are
     * more points in that direction.
     */
    public void next() {
	index++;
    }

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
	    throw new NoSuchElementException("quad iterator iterator out of bounds");
	}
	int type;
	if (index == 0) {
	    coords[0] = (float) quad.getX1();
	    coords[1] = (float) quad.getY1();
	    type = SEG_MOVETO;
	} else {
	    coords[0] = (float) quad.getCtrlX();
	    coords[1] = (float) quad.getCtrlY();
	    coords[2] = (float) quad.getX2();
	    coords[3] = (float) quad.getY2();
	    type = SEG_QUADTO;
	}
	if (affine != null) {
	    affine.transform(coords, 0, coords, 0, index == 0 ? 1 : 2);
	}
	return type;
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
	    throw new NoSuchElementException("quad iterator iterator out of bounds");
	}
	int type;
	if (index == 0) {
	    coords[0] = quad.getX1();
	    coords[1] = quad.getY1();
	    type = SEG_MOVETO;
	} else {
	    coords[0] = quad.getCtrlX();
	    coords[1] = quad.getCtrlY();
	    coords[2] = quad.getX2();
	    coords[3] = quad.getY2();
	    type = SEG_QUADTO;
	}
	if (affine != null) {
	    affine.transform(coords, 0, coords, 0, index == 0 ? 1 : 2);
	}
	return type;
    }
}
