// Copyright (c) David Schaefer 2023
import java.util.*;

public class LineareAlgebra 
{
    public static void main(String[] args)
    {
        Locale.setDefault(Locale.US);

        double[][] A = matrixEinlesen();

        double det = determinante(A);

        System.out.println(det);

        matrixAusgeben(A);
    }

    public static double[][] matrixEinlesen()                                                       //Matrix einlesen
    {
        Scanner sc = new Scanner(System.in);

        System.out.print("Dimension der Matrix: ");
        final int DIM = sc.nextInt();

        double[][] matrix = new double[DIM][DIM];

        for (int i = 0; i < DIM; i++)
        {
            for (int j = 0; j < DIM; j++)
            {
                System.out.printf("\nMatrix[%d][%d]: ",(i+1),(j+1));
                matrix[i][j] = sc.nextDouble();
            }
        }

        sc.close();

        return matrix;
    }

    public static void matrixAusgeben(double[][] matrix)                                             //Matrix ausgeben
    {
        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix[i].length; j++)
            {
                System.out.printf("%11.3f    ", matrix[i][j]);
            }

            System.out.println("\n");
        }

        /*
        Alternative Form der Ausgabe einer Matrix

        for (int i = 0; i < matrix.length; i++)
        {
            System.out.println(Arrays.toString(matrix[i]));            
        }
        */
    }

    public static double[][] skalarMalMatrix(double skalar, double[][] matrix)                     //Skalare Multiplikation einer Matrix
    {
        double[][] ergebnisMatrix = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix[i].length; j++)
            {
                ergebnisMatrix[i][j] = skalar * matrix[i][j];
            }
        }

        return ergebnisMatrix;
    }

    public static double[][] matrixTransponieren(double[][] matrix)                                  //Matrix transponieren
    {
        double[][] transponierte_matrix = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix[i].length; j++)
            {
                transponierte_matrix[i][j] = matrix[j][i];               
            }
        }

        return transponierte_matrix;
    }

    public static double[][] createSubmatrix(double[][] matrix, int i, int j)                   //Submatrix erstellen zur Berechnung der Unterdeterminanten
    {
        double [][] submatrix = new double [matrix.length-1][matrix[i].length-1];

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
        return submatrix;
    }

    public static double determinante(double[][] matrix)                    //Determinante berechnen
    {
        double det = 0;

        if (matrix.length != matrix[0].length)
        {
            return 0;
        }

        int rang = matrix.length;

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
                double [][] submatrix = createSubmatrix(matrix,0,i);
                det += matrix[0][i] * Math.pow(-1,i+1+1) * determinante(submatrix);
            }
        }
        return det;
    }

    public static double[][] createAdjunkte(double[][] matrix)                                                      //Berechnung der Adjunkten Matrix
    {
        double[][] kofaktormatrix, adjunkte = new double[matrix.length][matrix[0].length];
        kofaktormatrix = Null;

        for (int i = 0; i < matrix.length; i++)                                                                     //Berechnung der Kofaktormatrix
        {
            for (int j = 0; j < matrix[i].length; j++)
            {
                kofaktormatrix[i][j] = Math.pow(-1,i+j+2) * determinante(createSubmatrix(matrix, i, j));
            }
        }

        adjunkte = matrixTransponieren(kofaktormatrix);                                                         //Berechnung der Adjunkte, durch das Invertieren der Kofaktormatrix

        return adjunkte;
    } 

    public static double[][] matrixInvertieren(double[][] matrix)                                       //Matrix invertieren
    {
        double[][] invertierte_matrix = new double[matrix.length][matrix[0].length];

        double det = determinante(matrix);

        if (det == 0)
        {
            System.out.println("Fehler! Matrix nicht invertierbar");
            return Null;
        }
        if (det != 0)
        {
            invertierte_matrix = skalarMalMatrix((1.0/determinante(matrix)), createAdjunkte(matrix));

            return invertierte_matrix;
        }
        
    }
}