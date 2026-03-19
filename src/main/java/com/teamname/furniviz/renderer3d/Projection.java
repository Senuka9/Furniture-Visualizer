package com.teamname.furniviz.renderer3d;

/**
 * Projection - Mathematical utilities for 3D transformations and conversions
 *
 * Provides:
 * - Vector3f: 3D vector with basic operations
 * - Matrix4f: 4x4 transformation matrix
 * - Coordinate conversion (2D room meters ↔ 3D world space)
 * - Transformation helpers (translate, rotate, scale)
 */
public class Projection {

    /**
     * Vector3f - Represents a 3D point or direction
     *
     * In furniture visualization:
     * - Represents position of camera or furniture
     * - Represents direction of light or camera view
     */
    public static class Vector3f {
        public float x, y, z;

        public Vector3f(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vector3f(Vector3f v) {
            this.x = v.x;
            this.y = v.y;
            this.z = v.z;
        }

        // ============ Vector Operations ============

        /**
         * Add another vector to this one
         * Used for: cumulative transformations
         */
        public Vector3f add(Vector3f other) {
            return new Vector3f(this.x + other.x, this.y + other.y, this.z + other.z);
        }

        /**
         * Subtract another vector from this one
         * Used for: calculating direction vectors, distances
         */
        public Vector3f subtract(Vector3f other) {
            return new Vector3f(this.x - other.x, this.y - other.y, this.z - other.z);
        }

        /**
         * Multiply vector by scalar
         * Used for: scaling movements, normalizing
         */
        public Vector3f multiply(float scalar) {
            return new Vector3f(this.x * scalar, this.y * scalar, this.z * scalar);
        }

        /**
         * Dot product with another vector
         * Used for: angle calculations, visibility checks
         */
        public float dot(Vector3f other) {
            return this.x * other.x + this.y * other.y + this.z * other.z;
        }

        /**
         * Cross product with another vector (returns new vector perpendicular to both)
         * Used for: calculating surface normals (for lighting)
         */
        public Vector3f cross(Vector3f other) {
            return new Vector3f(
                this.y * other.z - this.z * other.y,
                this.z * other.x - this.x * other.z,
                this.x * other.y - this.y * other.x
            );
        }

        /**
         * Length (magnitude) of the vector
         * Used for: distance calculations
         */
        public float length() {
            return (float) Math.sqrt(x * x + y * y + z * z);
        }

        /**
         * Normalize: convert to unit vector (length = 1) pointing in same direction
         * Used for: lighting calculations, direction vectors
         */
        public Vector3f normalize() {
            float len = length();
            if (len == 0) return new Vector3f(0, 0, 0);
            return new Vector3f(x / len, y / len, z / len);
        }

        /**
         * Distance to another point
         * Used for: visibility culling, measurements
         */
        public float distance(Vector3f other) {
            return subtract(other).length();
        }

        @Override
        public String toString() {
            return String.format("Vector3f(%.2f, %.2f, %.2f)", x, y, z);
        }
    }

    // ============================================================================

    /**
     * Matrix4f - 4x4 Transformation Matrix
     *
     * Used for: position, rotation, scale transformations
     * Why 4x4? Allows combining translation + rotation + scale in one operation
     *
     * Layout:
     * | m[0][0]  m[0][1]  m[0][2]  m[0][3] |
     * | m[1][0]  m[1][1]  m[1][2]  m[1][3] |
     * | m[2][0]  m[2][1]  m[2][2]  m[2][3] |
     * | m[3][0]  m[3][1]  m[3][2]  m[3][3] |
     *
     * For transformations, mostly use 3x3 upper-left + translation column
     */
    public static class Matrix4f {
        public float[][] m = new float[4][4];

        public Matrix4f() {
            identity();
        }

        public Matrix4f(Matrix4f other) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    this.m[i][j] = other.m[i][j];
                }
            }
        }

        /**
         * Identity matrix: no transformation (multiplying by this does nothing)
         * Used as: starting point for building transformations
         */
        public void identity() {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    m[i][j] = (i == j) ? 1.0f : 0.0f;
                }
            }
        }

        /**
         * Multiply two matrices
         * Used for: combining transformations
         * Example: (rotate 45°) × (translate 5 units) = rotate then translate
         */
        public Matrix4f multiply(Matrix4f other) {
            Matrix4f result = new Matrix4f();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    float sum = 0;
                    for (int k = 0; k < 4; k++) {
                        sum += this.m[i][k] * other.m[k][j];
                    }
                    result.m[i][j] = sum;
                }
            }
            return result;
        }

        /**
         * Transform a vector by this matrix
         * Used for: applying transformations to points/directions
         */
        public Vector3f transform(Vector3f v) {
            float x = m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z + m[0][3];
            float y = m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z + m[1][3];
            float z = m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z + m[2][3];
            return new Vector3f(x, y, z);
        }

        /**
         * Translation matrix: moves things by (tx, ty, tz)
         * Used for: positioning furniture at x,y coordinates
         */
        public static Matrix4f translation(float tx, float ty, float tz) {
            Matrix4f result = new Matrix4f();
            result.m[0][3] = tx;
            result.m[1][3] = ty;
            result.m[2][3] = tz;
            return result;
        }

        /**
         * Rotation around Z-axis (for furniture rotation in room)
         * Used for: rotating furniture by angle in 2D plane
         */
        public static Matrix4f rotationZ(float angleRadians) {
            Matrix4f result = new Matrix4f();
            float cos = (float) Math.cos(angleRadians);
            float sin = (float) Math.sin(angleRadians);

            result.m[0][0] = cos;  result.m[0][1] = -sin;
            result.m[1][0] = sin;  result.m[1][1] = cos;
            result.m[2][2] = 1.0f;
            result.m[3][3] = 1.0f;

            return result;
        }

        /**
         * Rotation around X-axis (for camera tilt)
         * Used for: tilting camera to look down at room
         */
        public static Matrix4f rotationX(float angleRadians) {
            Matrix4f result = new Matrix4f();
            float cos = (float) Math.cos(angleRadians);
            float sin = (float) Math.sin(angleRadians);

            result.m[0][0] = 1.0f;
            result.m[1][1] = cos;  result.m[1][2] = -sin;
            result.m[2][1] = sin;  result.m[2][2] = cos;
            result.m[3][3] = 1.0f;

            return result;
        }

        /**
         * Rotation around Y-axis (for camera orbit)
         * Used for: rotating camera around room
         */
        public static Matrix4f rotationY(float angleRadians) {
            Matrix4f result = new Matrix4f();
            float cos = (float) Math.cos(angleRadians);
            float sin = (float) Math.sin(angleRadians);

            result.m[0][0] = cos;  result.m[0][2] = sin;
            result.m[1][1] = 1.0f;
            result.m[2][0] = -sin; result.m[2][2] = cos;
            result.m[3][3] = 1.0f;

            return result;
        }

        /**
         * Scale matrix: multiplies coordinates by factors
         * Used for: zooming, resizing objects
         */
        public static Matrix4f scale(float sx, float sy, float sz) {
            Matrix4f result = new Matrix4f();
            result.m[0][0] = sx;
            result.m[1][1] = sy;
            result.m[2][2] = sz;
            result.m[3][3] = 1.0f;
            return result;
        }

        /**
         * Perspective projection matrix
         * Used for: converting 3D world to 2D screen
         */
        public static Matrix4f perspective(float fov, float aspect, float near, float far) {
            Matrix4f result = new Matrix4f();

            float f = (float) (1.0 / Math.tan(fov / 2.0));
            result.m[0][0] = f / aspect;
            result.m[1][1] = f;
            result.m[2][2] = (far + near) / (near - far);
            result.m[2][3] = (2 * far * near) / (near - far);
            result.m[3][2] = -1.0f;

            return result;
        }

        /**
         * Orthographic projection matrix (no perspective)
         * Used for: top-down 2D-like view of room
         */
        public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
            Matrix4f result = new Matrix4f();

            result.m[0][0] = 2.0f / (right - left);
            result.m[1][1] = 2.0f / (top - bottom);
            result.m[2][2] = -2.0f / (far - near);
            result.m[0][3] = -(right + left) / (right - left);
            result.m[1][3] = -(top + bottom) / (top - bottom);
            result.m[2][3] = -(far + near) / (far - near);
            result.m[3][3] = 1.0f;

            return result;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    sb.append(String.format("%7.2f ", m[i][j]));
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    // ============================================================================
    // COORDINATE CONVERSION HELPERS
    // ============================================================================

    /**
     * Convert 2D room coordinates (meters) to 3D world space
     *
     * Room is in XZ plane (horizontal):
     * 2D: (x_meters, y_meters) in room
     * 3D: (x_world, 0, z_world) where y_world is height
     *
     * Used for: positioning furniture from 2D editor data into 3D space
     */
    public static Vector3f roomToWorld(double roomX, double roomY, double height) {
        return new Vector3f(
            (float) roomX,              // X: same as room X
            (float) height,             // Y: height (vertical in 3D)
            (float) roomY               // Z: room Y becomes Z depth
        );
    }

    /**
     * Convert 3D world coordinates back to 2D room coordinates
     *
     * Reverse of roomToWorld()
     */
    public static double[] worldToRoom(Vector3f worldPos) {
        return new double[]{
            worldPos.x,                 // Room X from world X
            worldPos.z                  // Room Y from world Z
        };
    }

    /**
     * Convert rotation angle for proper orientation
     *
     * 2D editor: rotation in degrees, 0° = right, 90° = down
     * 3D: rotation in radians, 0° = right, 90° = back
     */
    public static float convertRotation(double degrees2D) {
        return (float) Math.toRadians(degrees2D);
    }

    /**
     * Test helper: print a vector nicely
     */
    public static String format(Vector3f v) {
        return String.format("[%.2f, %.2f, %.2f]", v.x, v.y, v.z);
    }
}
