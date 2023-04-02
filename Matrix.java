// Copyright (c) David Schaefer 2023
class Matrix 
{

    private double[][] matrix;

    public Matrix (int n)                               //creating a quadratic matrix of rank n
    {
        double[][] A = new double[n][n];
        this.matrix = A;
    }

    public Matrix (double[][] matrix)                   //filling the matrix entry by entry with a 2D array
    {
        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix[i].length; j++)
            {
                this.matrix[i][j] = matrix[i][j];
            }
        }
    }

    public static Matrix E(int n)                        //creating the identity matrix of rank n
    {
        Matrix E = new Matrix(n);

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (i == j)
                {
                    E.matrix[i][j] = 1;
                }

                else
                {
                    E.matrix[i][j] = 0;
                }
            }
        }

        return E;
    }

    public boolean isQuadratic()           //checking whether a matrix has as many rows as columns
    {
        boolean temp = true;

        for (int i = 0; i < matrix.length; i++)
        {
            if (matrix.length != matrix[i].length)
            {
                temp = false;
            }
        }

        return temp;
    }

    public int quadraticLength()             //the length of the diagonal in a quadratic matrix
    {
        if (isQuadratic())
        {
            return matrix.length;
        }

        else
        {
            return 0;
        }
    }

    public static Matrix matrixAdd(Matrix A, Matrix B)                  //Matrix addition
    {
        if (!A.isQuadratic() || !B.isQuadratic())
        {
            return E(0);
        }

        else
        {
            int n = A.quadraticLength();
            Matrix sum = new Matrix(n);

            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j++)
                {
                    sum.matrix[i][j] = A.matrix[i][j] + B.matrix[i][j];
                }
            }

            return sum;
        }
    }

    public static Matrix matrixScalar(Matrix A, double scalar)           //matrix scalar multiplication
    {
        Matrix result = new Matrix(A.matrix);

        for (int i = 0; i < A.matrix.length; i++)
        {
            for (int j = 0; j < A.matrix[i].length; j++)
            {
                result.matrix[i][j] = scalar * A.matrix[i][j];
            }
        }

        return result;
    }

    public Matrix matrixTranspose(Matrix A)                          //transposing a matrix
    {
        Matrix AT = new Matrix(A.matrix);

        for (int i = 0; i < A.matrix.length; i++)
        {
            for (int j = 0; j < A.matrix[i].length; j++)
            {
                AT.matrix[i][j] = A.matrix[j][i];
            }
        }

        return AT;
    }

    public Matrix createSubmatrix(int i, int j)            //creating the submatrix by deleting the ith row and jth column
    {
        double[][] submatrix = new double[matrix.length-1][matrix[i].length-1];
        
        for (int k = 0; k < submatrix.length; k++)
        {
            for (int l = 0; l < submatrix[k].length; l++)
            {
                if (k < i && l < j)
                {
                    submatrix[k][l] = matrix[k][l];
                }

                else if (k >= i)
                {
                    if (l < j)
                    {
                        submatrix[k][l] = matrix[k+1][l];
                    }

                    else if (k >= i && l >= j)
                    {
                        submatrix[k][l] = matrix[k+1][l+1];
                    }
                }

                else if (k < i && l >= j)
                {
                    submatrix[k][l] = matrix[k][l+1];
                }
            }
        }

        Matrix Submatrix = new Matrix(submatrix);

        return Submatrix;
    }

    public double determinant()                    //calculating the determinant
    {
        double det = 0;

        if (!isQuadratic())
        {
            return 0;
        }

        int rang = quadraticLength();

        if (rang == 1)
        {
            return matrix[0][0];
        }
        if (rang == 2)
        {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }

        else
        {
            for (int i = 0; i < matrix.length; i++)
            {
                Matrix Submatrix = createSubmatrix(0,i);
                det += matrix[0][i] * Math.pow(-1,i+1+1) * Submatrix.determinant();
            }
        }
        return det;
    }

    public Matrix createAdjunkte()                                                      //calculating the adjugate matrix
    {
        if (!isQuadratic())
        {
            return E(0);
        }

        else
        {
            int n = quadraticLength();
            Matrix cofacotorMatrix = new Matrix(quadraticLength());

            for (int i = 0; i < n; i++)                                                                     //calculating the cofactormatrix
            {
                for (int j = 0; j < n; j++)
                {
                    cofacotorMatrix.matrix[i][j] = Math.pow(-1,i+j+2) * createSubmatrix(i,j).determinant();
                }
            }

            Matrix adjugate = matrixTranspose(cofacotorMatrix);                                                         //calculating the adjugate matrix by inverting the cofactormatrix

            return adjugate;
        }
    }
    
    public Matrix matrixInvert()                                       //finally calculation the inverse matrix
    {
        double det = determinant();

        if (det == 0)
        {
            System.out.println("Error! The inverse matrix doesn't exist");
            return E(0);
        }
        else
        {
            Matrix Inverse = matrixScalar(createAdjunkte(), 1.0/determinant());
            return Inverse;
        }
        
    }
}
