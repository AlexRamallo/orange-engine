package com.snakeinalake.engine.utilities;

import com.snakeinalake.engine.core.vector2df;
import com.snakeinalake.engine.core.vector3df;

/**
 * Simple Geometric methods
 * */
public class OBasicCollision{
	/**
	 * Get the distance between two points in 2D
	 * @param p1 The first point
	 * @param p2 The second point
	 * */
	public static float distance2D(vector3df p1, vector3df p2){		
		return (float)Math.sqrt(Math.abs((p2.x-p1.x)*(p2.x-p1.x))+Math.abs((p2.y-p1.y)*(p2.y-p1.y)));
	}
	/**
	 * Get the distance between two points in 2D
	 * @param p1 The first point
	 * @param p2 The second point
	 * */
	public static float distance2DKeepSign(vector3df p1, vector3df p2){		
		return (float)Math.sqrt((p2.x-p1.x)*(p2.x-p1.x)+(p2.y-p1.y)*(p2.y-p1.y));
	}
	/**
	 * Get the distance between two points in 2D
	 * @param p1 The first point
	 * @param p2 The second point
	 * */
	public static float distance2D(float x1, float y1, float x2, float y2){		
		return (float)Math.sqrt(Math.abs((x2-x1)*(x2-x1))+Math.abs((y2-y1)*(y2-y1)));
	}
	/**
	 * Same as distance2D but use z axis instead of y
	 * @param p1 The first point
	 * @param p2 The second point
	 * */
	public static float distance2Dz(vector3df p1, vector3df p2){		
		return (float)Math.sqrt(Math.abs((p2.x-p1.x)*(p2.x-p1.x))+Math.abs((p2.z-p1.z)*(p2.z-p1.z)));
	}
	/**
	 * Get the distance between two points in 1D
	 * @param p1 The first point
	 * @param p2 The second point
	 * */
	public static float distance1D(float p1, float p2){		
		return Math.abs(p2-p1);
	} 
	public static float clamp(float val, float min, float max){
		if(val<min)
			return min;
		if(val>max)
			return max;
		return val;
	}
	/**
	 * Test if a point is inside of a circle
	 * @param radius The radius of the circle
	 * @param center The origin of the circle
	 * @param point The point to check if its in the sphere
	 **/
	public static boolean pointInCircle(float radius, vector3df center, vector3df point){
		if(distance2D(center,point)<=radius)
			return true;
		else
			return false;
	}
	/**
	 * Test if a point is inside of a circle
	 * @param radius The radius of the circle
	 * @param cX The X position of the center of the circle
	 * @param cZ The Z position of the center of the circle (Z if using 3D coordinates, Y if 2D)
	 * @param pX The X position of the point to check
	 * @param pZ The Z position of the point to check (Z if using 3D coordinates, Y if 2D)
	 **/
	public static boolean pointInCircle(float radius, float cX, float cZ, float pX, float pZ){
		if(distance2D(cX,cZ,pX,pZ)<=radius)
			return true;
		else
			return false;
	}
	/**
	 * Get the angle between two 2D vectors (angle made if a laser were to be pointed from one to the other)
	 * @param x1 X of first vector
	 * @param y1 Y of first vector
	 * @param x2 X of second vector
	 * @param y2 Y of second vector
	 * @return Angle in radians
	 * */
	public static float vectorDirection(float x1, float y1, float x2, float y2){
		return (float)(Math.atan2(x1-x2,y1-y2))+1.57f;
	}
	
	/**
	 * Return the midpoint of two vectors.
	 * @param p1 First vector
	 * @param p2 Second vector
	 * @param fill vector to be filled (if null, new one will be created)
	 * @return fill
	 * */
	public static vector3df midpoint(vector3df p1, vector3df p2, vector3df fill){
		fill.x = (p1.x+p2.x)/2;
		fill.y = (p1.y+p2.y)/2;
		fill.z = (p1.z+p2.z)/2;
		return fill;
	}
	/**
	 * Returns the midpoint of two coordinates (basically the equivalent of calculating the average of two arbitrary floats)
	 * @param C First coordinate
	 * @param C2 Second coordinate
	 * @return Midpoint between both
	 * */
	public static float midpoint(float C, float C2){
		return (C+C2)/2;
	}
	/**
	 * Returns a <b>new</b> normalized vector3df
	 * @param v The vector to normalize
	 * @return The new normalized vector
	 * */
	public vector3df normalize(vector3df v){
		float mg = (float)Math.sqrt((v.x*v.x)+(v.y*v.y)+(v.z*v.z));
		return new vector3df(v.x/mg, v.y/mg, v.z/mg);
	}
	/**
	 * Returns a <b>new</b> normalized vector2df
	 * @param v The vector to normalize
	 * @return The new normalized vector
	 * */
	public vector2df normalize(vector2df v){
		float mg = (float)Math.sqrt((v.x*v.x)+(v.y*v.y));
		return new vector2df(v.x/mg, v.y/mg);
	}
	/**
	 * Returns the dot product of two vectors
	 * @param p1 First vector
	 * @param p2 Second vector
	 * @return p1•p2
	 * */
	public float dot(vector2df p1, vector2df p2){
		return (p1.x*p2.x) + (p1.y*p2.y);
	}
	/**
	 * Returns the dot product of two vectors
	 * @param p1 First vector
	 * @param p2 Second vector
	 * @return p1•p2
	 * */
	public float dot(vector3df p1, vector3df p2){
		return (p1.x*p2.x) + (p1.y*p2.y) + (p1.z*p2.z);
	}
}
