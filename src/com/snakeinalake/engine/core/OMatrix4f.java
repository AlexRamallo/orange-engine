package com.snakeinalake.engine.core;

public class OMatrix4f {
	public float[][] m;
	public static float results[] = {0,0,0};
	
	public OMatrix4f(){
		m = new float[][]{
				new float[]{0, 0, 0, 0},
				new float[]{0, 0, 0, 0},
				new float[]{0, 0, 0, 0},
				new float[]{0, 0, 0, 0}
		};
	}
	
	public void multiply(vector3df v, vector3df result){
		result.x = (v.x*m[0][0]) + (v.y*m[0][1]) + (v.z*m[0][2]) + m[0][3];
		result.y = (v.x*m[1][0]) + (v.y*m[1][1]) + (v.z*m[1][2]) + m[1][3];
		result.z = (v.x*m[2][0]) + (v.y*m[2][1]) + (v.z*m[2][2]) + m[2][3];
	}
	/**
	 * Multiplies vector coordinates with 3 rotation matrices
	 * @param vec The 3D coordinate vector to perform the operations on
	 * @param RotX the rotation on the X axis (if 0 not multiplication will be performed)
	 * @param RotY the rotation on the Y axis (if 0 not multiplication will be performed)
	 * @param RotZ the rotation on the Z axis (if 0 not multiplication will be performed)
	 * */
	public static void rotvec(vector3df vec, float RotX, float RotY, float RotZ){
		float[][] mX = {
				{1,0,0,0},
				{0,(float)Math.cos(RotX),(float)Math.sin(RotX),0},
				{0,(float)-Math.sin(RotX),(float)Math.cos(RotX),0},
				{0,0,0,1},
			};
		float[][] mY = {
				{(float)Math.cos(RotY),0,(float)-Math.sin(RotY),0},
				{0,1,0,0},
				{(float)Math.sin(RotY),0,(float)Math.cos(RotY),0},
				{0,0,0,1},
		};
		float[][] mZ = {
				{(float)Math.cos(RotZ),(float)Math.sin(RotZ),0,0},
				{(float)-Math.sin(RotZ),(float)Math.cos(RotZ),0,0},
				{0,0,1,0},
				{0,0,0,1},
		};
		if(RotX!=0){
			vec.x = (mX[0][0]*vec.x) + (mX[0][1]*vec.y) + (mX[0][2]*vec.z) + mX[0][3];
			vec.y = (mX[1][0]*vec.x) + (mX[1][1]*vec.y) + (mX[1][2]*vec.z) + mX[1][3];
			vec.z = (mX[2][0]*vec.x) + (mX[2][1]*vec.y) + (mX[2][2]*vec.z) + mX[2][3];
		}
		if(RotY!=0){
			vec.x = (mY[0][0]*vec.x) + (mY[0][1]*vec.y) + (mY[0][2]*vec.z) + mY[0][3];
			vec.y = (mY[1][0]*vec.x) + (mY[1][1]*vec.y) + (mY[1][2]*vec.z) + mY[1][3];
			vec.z = (mY[2][0]*vec.x) + (mY[2][1]*vec.y) + (mY[2][2]*vec.z) + mY[2][3];
		}
		if(RotZ!=0){
			vec.x = (mZ[0][0]*vec.x) + (mZ[0][1]*vec.y) + (mZ[0][2]*vec.z) + mZ[0][3];
			vec.y = (mZ[1][0]*vec.x) + (mZ[1][1]*vec.y) + (mZ[1][2]*vec.z) + mZ[1][3];
			vec.z = (mZ[2][0]*vec.x) + (mZ[2][1]*vec.y) + (mZ[2][2]*vec.z) + mZ[2][3];
		}
	}
	/**
	 * Multiplies vector coordinates with 3 rotation matrices
	 * @param vec The 3D coordinate vector to perform the operations on
	 * @param RotX the rotation on the X axis (if 0 not multiplication will be performed)
	 * @param RotY the rotation on the Y axis (if 0 not multiplication will be performed)
	 * @param RotZ the rotation on the Z axis (if 0 not multiplication will be performed)
	 * */
	public static void rotvec(float vx, float vy, float vz, float RotX, float RotY, float RotZ){
		float[][] mX = {
				{1,0,0,0},
				{0,(float)Math.cos(RotX),(float)Math.sin(RotX),0},
				{0,(float)-Math.sin(RotX),(float)Math.cos(RotX),0},
				{0,0,0,1},
			};
		float[][] mY = {
				{(float)Math.cos(RotY),0,(float)-Math.sin(RotY),0},
				{0,1,0,0},
				{(float)Math.sin(RotY),0,(float)Math.cos(RotY),0},
				{0,0,0,1},
		};
		float[][] mZ = {
				{(float)Math.cos(RotZ),(float)Math.sin(RotZ),0,0},
				{(float)-Math.sin(RotZ),(float)Math.cos(RotZ),0,0},
				{0,0,1,0},
				{0,0,0,1},
		};
		if(RotX!=0){
			results[0] = (mX[0][0]*vx) + (mX[0][1]*vy) + (mX[0][2]*vz) + mX[0][3];
			results[1] = (mX[1][0]*vx) + (mX[1][1]*vy) + (mX[1][2]*vz) + mX[1][3];
			results[2] = (mX[2][0]*vx) + (mX[2][1]*vy) + (mX[2][2]*vz) + mX[2][3];
			vx = results[0];
			vy = results[1];
			vz = results[2];
		}
		if(RotY!=0){
			results[0] = (mY[0][0]*vx) + (mY[0][1]*vy) + (mY[0][2]*vz) + mY[0][3];
			results[1] = (mY[1][0]*vx) + (mY[1][1]*vy) + (mY[1][2]*vz) + mY[1][3];
			results[2] = (mY[2][0]*vx) + (mY[2][1]*vy) + (mY[2][2]*vz) + mY[2][3];
			vx = results[0];
			vy = results[1];
			vz = results[2];
		}
		if(RotZ!=0){
			results[0] = (mZ[0][0]*vx) + (mZ[0][1]*vy) + (mZ[0][2]*vz) + mZ[0][3];
			results[1] = (mZ[1][0]*vx) + (mZ[1][1]*vy) + (mZ[1][2]*vz) + mZ[1][3];
			results[2] = (mZ[2][0]*vx) + (mZ[2][1]*vy) + (mZ[2][2]*vz) + mZ[2][3];
		}
	}
	/**
	 * Multiplies vector coordinates with 3 translation matrices
	 * @param vec The 3D coordinate vector to perform the operations on
	 * @param TranX the translation on the X axis (if 0 not multiplication will be performed)
	 * @param TranY(haha very funny) the translation on the Y axis (if 0 not multiplication will be performed)
	 * @param TranZ the translation on the Z axis (if 0 not multiplication will be performed)
	 * */
	public static void transvec(float vX, float vY, float vZ, float TranX, float TranY, float TranZ){
		float[][] mX = {
				{1,0,0,0},
				{0,1,0,0},
				{0,0,1,0},
				{TranX,TranY,TranZ,1},
			};
			vX = (mX[0][0]*vX) + (mX[1][0]*vY) + (mX[2][0]*vZ) + mX[3][0];
			vY = (mX[0][1]*vX) + (mX[1][1]*vY) + (mX[2][1]*vZ) + mX[3][1];
			vZ = (mX[0][2]*vX) + (mX[1][2]*vY) + (mX[2][2]*vZ) + mX[3][2];
			
			results[0] = vX;
			results[1] = vY;
			results[2] = vZ;
	}
	/**
	 * Multiply this matrix with another one
	 * @param mX the array of the matrix to multiply with
	 * @return Sets the values of this matrix to the result of the multiplication
	 * */
	public void multiply(float[][] mX){
		float[][] ret = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};		
			ret[0][0] = (m[0][0]*mX[0][0])+(m[0][1]*mX[1][0])+(m[0][2]*mX[2][0])+(m[0][3]*mX[3][0]);
			ret[0][1] = (m[0][0]*mX[0][1])+(m[0][1]*mX[1][1])+(m[0][2]*mX[2][1])+(m[0][3]*mX[3][1]);
			ret[0][2] = (m[0][0]*mX[0][2])+(m[0][1]*mX[1][2])+(m[0][2]*mX[2][2])+(m[0][3]*mX[3][2]);
			ret[0][3] = (m[0][0]*mX[0][3])+(m[0][1]*mX[1][3])+(m[0][2]*mX[2][3])+(m[0][3]*mX[3][3]);
			
			ret[1][0] = (m[1][0]*mX[0][0])+(m[1][1]*mX[1][0])+(m[1][2]*mX[2][0])+(m[1][3]*mX[3][0]);
			ret[1][1] = (m[1][0]*mX[0][1])+(m[1][1]*mX[1][1])+(m[1][2]*mX[2][1])+(m[1][3]*mX[3][1]);
			ret[1][2] = (m[1][0]*mX[0][2])+(m[1][1]*mX[1][2])+(m[1][2]*mX[2][2])+(m[1][3]*mX[3][2]);
			ret[1][3] = (m[1][0]*mX[0][3])+(m[1][1]*mX[1][3])+(m[1][2]*mX[2][3])+(m[1][3]*mX[3][3]);
			
			ret[2][0] = (m[2][0]*mX[0][0])+(m[2][1]*mX[1][0])+(m[2][2]*mX[2][0])+(m[2][3]*mX[3][0]);
			ret[2][1] = (m[2][0]*mX[0][1])+(m[2][1]*mX[1][1])+(m[2][2]*mX[2][1])+(m[2][3]*mX[3][1]);
			ret[2][2] = (m[2][0]*mX[0][2])+(m[2][1]*mX[1][2])+(m[2][2]*mX[2][2])+(m[2][3]*mX[3][2]);
			ret[2][3] = (m[2][0]*mX[0][3])+(m[2][1]*mX[1][3])+(m[2][2]*mX[2][3])+(m[2][3]*mX[3][3]);
			
			ret[3][0] = (m[3][0]*mX[0][0])+(m[3][1]*mX[1][0])+(m[3][2]*mX[2][0])+(m[3][3]*mX[3][0]);
			ret[3][1] = (m[3][0]*mX[0][1])+(m[3][1]*mX[1][1])+(m[3][2]*mX[2][1])+(m[3][3]*mX[3][1]);
			ret[3][2] = (m[3][0]*mX[0][2])+(m[3][1]*mX[1][2])+(m[3][2]*mX[2][2])+(m[3][3]*mX[3][2]);
			ret[3][3] = (m[3][0]*mX[0][3])+(m[3][1]*mX[1][3])+(m[3][2]*mX[2][3])+(m[3][3]*mX[3][3]);
		m = ret;
	}
	
	/**
	 * Multiply this matrix 4x4 with another one
	 * @param mX the array of the matrix to multiply with
	 * @return Sets the values of this matrix to the result of the multiplication
	 * */
	public static float[][] multiply44(float[][] tr, float[][] mX){
		float[][] ret = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};		
			ret[0][0] = (tr[0][0]*mX[0][0])+(tr[0][1]*mX[1][0])+(tr[0][2]*mX[2][0])+(tr[0][3]*mX[3][0]);
			ret[0][1] = (tr[0][0]*mX[0][1])+(tr[0][1]*mX[1][1])+(tr[0][2]*mX[2][1])+(tr[0][3]*mX[3][1]);
			ret[0][2] = (tr[0][0]*mX[0][2])+(tr[0][1]*mX[1][2])+(tr[0][2]*mX[2][2])+(tr[0][3]*mX[3][2]);
			ret[0][3] = (tr[0][0]*mX[0][3])+(tr[0][1]*mX[1][3])+(tr[0][2]*mX[2][3])+(tr[0][3]*mX[3][3]);
			
			ret[1][0] = (tr[1][0]*mX[0][0])+(tr[1][1]*mX[1][0])+(tr[1][2]*mX[2][0])+(tr[1][3]*mX[3][0]);
			ret[1][1] = (tr[1][0]*mX[0][1])+(tr[1][1]*mX[1][1])+(tr[1][2]*mX[2][1])+(tr[1][3]*mX[3][1]);
			ret[1][2] = (tr[1][0]*mX[0][2])+(tr[1][1]*mX[1][2])+(tr[1][2]*mX[2][2])+(tr[1][3]*mX[3][2]);
			ret[1][3] = (tr[1][0]*mX[0][3])+(tr[1][1]*mX[1][3])+(tr[1][2]*mX[2][3])+(tr[1][3]*mX[3][3]);
			
			ret[2][0] = (tr[2][0]*mX[0][0])+(tr[2][1]*mX[1][0])+(tr[2][2]*mX[2][0])+(tr[2][3]*mX[3][0]);
			ret[2][1] = (tr[2][0]*mX[0][1])+(tr[2][1]*mX[1][1])+(tr[2][2]*mX[2][1])+(tr[2][3]*mX[3][1]);
			ret[2][2] = (tr[2][0]*mX[0][2])+(tr[2][1]*mX[1][2])+(tr[2][2]*mX[2][2])+(tr[2][3]*mX[3][2]);
			ret[2][3] = (tr[2][0]*mX[0][3])+(tr[2][1]*mX[1][3])+(tr[2][2]*mX[2][3])+(tr[2][3]*mX[3][3]);
			
			ret[3][0] = (tr[3][0]*mX[0][0])+(tr[3][1]*mX[1][0])+(tr[3][2]*mX[2][0])+(tr[3][3]*mX[3][0]);
			ret[3][1] = (tr[3][0]*mX[0][1])+(tr[3][1]*mX[1][1])+(tr[3][2]*mX[2][1])+(tr[3][3]*mX[3][1]);
			ret[3][2] = (tr[3][0]*mX[0][2])+(tr[3][1]*mX[1][2])+(tr[3][2]*mX[2][2])+(tr[3][3]*mX[3][2]);
			ret[3][3] = (tr[3][0]*mX[0][3])+(tr[3][1]*mX[1][3])+(tr[3][2]*mX[2][3])+(tr[3][3]*mX[3][3]);
		return ret;
	}
}
