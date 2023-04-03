// Copyright (c) David Schaefer 2023
class Matrix 
{

    private double[][] m_elements;

    public Matrix (int n)                               //creating a quadratic matrix of rank n
    {
        double[][] A = new double[n][n];
        this.m_elements = A;
    }

    public Matrix (double[][] matrix)                   //filling the matrix entry by entry with a 2D array
    {                                                   //the setter is an easier version of this, might change later
        this.m_elements = new double[matrix.length][];

        for (int i = 0; i < matrix.length; i++)
        {
            this.m_elements[i] = new double[matrix[i].length];

            for (int j = 0; j < matrix[i].length; j++)
            {
                this.m_elements[i][j] = matrix[i][j];
            }
        }
    }

    public void set_m_elements(double[][] matrix)    //setter
    {
        m_elements = matrix;
    }

    public double[][] get_m_elements()          //getter
    {
        return m_elements;
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
                    E.m_elements[i][j] = 1;
                }

                else
                {
                    E.m_elements[i][j] = 0;
                }
            }
        }

        return E;
    }

    public boolean isQuadratic()           //checking whether a matrix has as many rows as columns
    {
        boolean temp = true;

        for (int i = 0; i < m_elements.length; i++)
        {
            if (m_elements.length != m_elements[i].length)
            {
                temp = false;
            }
        }

        return temp;
    }
        {
            if (m_elements[i].length != m_elements[i+1].length)
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
            return m_elements.length;
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
                    sum.m_elements[i][j] = A.m_elements[i][j] + B.m_elements[i][j];
                }
            }

            return sum;
        }
    }

    public static Matrix matrixScalar(Matrix A, double scalar)           //matrix scalar multiplication
    {
        Matrix result = new Matrix(A.m_elements);

        for (int i = 0; i < A.m_elements.length; i++)
        {
            for (int j = 0; j < A.m_elements[i].length; j++)
            {
                result.m_elements[i][j] = scalar * A.m_elements[i][j];
            }
        }

        return result;
    }

    public Matrix matrixTranspose(Matrix A)                          //transposing a matrix
    {
        Matrix AT = new Matrix(A.m_elements);

        for (int i = 0; i < A.m_elements.length; i++)
        {
            for (int j = 0; j < A.m_elements[i].length; j++)
            {
                AT.m_elements[i][j] = A.m_elements[j][i];
            }
        }

        return AT;
    }

    public Matrix createSubmatrix(int i, int j)            //creating the submatrix by deleting the ith row and jth column
    {
        double[][] submatrix = new double[m_elements.length-1][m_elements[i].length-1];
        
        for (int k = 0; k < submatrix.length; k++)
        {
            for (int l = 0; l < submatrix[k].length; l++)
            {
                if (k < i && l < j)
                {
                    submatrix[k][l] = m_elements[k][l];
                }

                else if (k >= i)
                {
                    if (l < j)
                    {
                        submatrix[k][l] = m_elements[k+1][l];
                    }

                    else if (k >= i && l >= j)
                    {
                        submatrix[k][l] = m_elements[k+1][l+1];
                    }
                }

                else if (k < i && l >= j)
                {
                    submatrix[k][l] = m_elements[k][l+1];
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
            return m_elements[0][0];
        }
        if (rang == 2)
        {
            return m_elements[0][0] * m_elements[1][1] - m_elements[0][1] * m_elements[1][0];
        }

        else
        {
            for (int i = 0; i < m_elements.length; i++)
            {
                Matrix Submatrix = createSubmatrix(0,i);
                det += m_elements[0][i] * Math.pow(-1,i+1+1) * Submatrix.determinant();
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
                    cofacotorMatrix.m_elements[i][j] = Math.pow(-1,i+j+2) * createSubmatrix(i,j).determinant();
                }
            }

            Matrix adjugate = matrixTranspose(cofacotorMatrix);                                                         //calculating the adjugate matrix by inverting the cofactormatrix

            return adjugate;
        }
    }
    
    public Matrix Inverse()                                       //finally calculation the inverse matrix
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
