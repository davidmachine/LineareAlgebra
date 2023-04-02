// Copyright (c) David Schaefer 2023
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LineareAlgebra 
{
    public static void main(String[] args) throws FileNotFoundException
    {
        final Scanner scanner = new Scanner(new File("input.txt"));

        Locale.setDefault(Locale.US);

        double[][][] list = matrixVonTxt(scanner);
        
        for (int i = 0; i < list.length; i++)
        {
            matrixAusgeben(list[i]);
        }

        matrixAusgeben(list[0]);

        Matrix A = new Matrix(list[0]);     //throws NullPointerException because "this.matrix" is null

        System.out.println("det: " + A.determinant());
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

    public static double[][][] matrixVonTxt(final Scanner scanner)                             //Matrizen werden von einer Textdatei eingelesen
    {                                                                                          //Die erste Ziffer gibt die Anzahl an Matrizen an
        int h = 0;                                                                             //Die zweite Ziffer gibt den Rang der ersten Matrix an, die dann in der Form:
        boolean matrixEingelesen = true;                                                       //a11,a12,...,a1n,a21,...,a2n,...,amn als einzelne Eintraege pro Splate da steht
        int anzahlMatrizen = scanner.nextInt();                                                //Nach Rang*Rang vielen Eintraegen gibt die naeschste Ziffer den Rang der naechsten Matrix an
        double[][][] ausgabe = new double[anzahlMatrizen][][];                                 //Es wiederholt sich also von hier an

        while (scanner.hasNextDouble() && matrixEingelesen == true)
        {
            int rang = scanner.nextInt();
            ausgabe[h] = new double[rang][rang];

            for (int i = 0; i < rang; i++)
            {
                for (int j = 0; j < rang; j++)
                {
                    if (scanner.hasNextDouble())
                    {
                        ausgabe[h][i][j] = scanner.nextDouble();
                    }
                    else
                    {
                        System.out.println("Fehler! Datei input.txt ist falsch formatiert");
                        ausgabe[h][i][j] = 0;
                        System.exit(1);
                    }
                }
            }

            h += 1;
        }

        return ausgabe;
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

        System.out.println("\n");
    }

    public static void vektorAusgeben(double[] vektor)                                                 //Vektor ausgeben
    {
        for (int i = 0; i < vektor.length; i++)
        {
            System.out.printf("%11.3f    ", vektor[i]);
        }

        System.out.println("\n");
    }

    public static double[] LGSeindeutigloesen(double[][] matrix)                   //Damit das LGS eindeutig loesbar ist muss die Eingabematrix quadratisch mit einer Nullzeile sein
    {
        double faktor = 0;
        double faktor2 = 0;

        for (int i = 0; i < matrix.length; i++)                                    //stritke obere Dreiecksmatrix
        {
            faktor = matrix[i][i];

            if (faktor != 0)
            {
                for (int j = 0; j < matrix.length; j++)
                {
                    faktor2 = matrix[j][i];

                    if (i != j && faktor2 != 0)
                    {
                        for (int k = 0; k < matrix.length; k++)
                        {
                            matrix[j][k] = faktor * matrix[j][k] - faktor2 * matrix[i][k];
                        }
                    }
                }
            }

            else
            {
                if (i < matrix.length-1)                                            //Zeilen vertauschen, falls auf der Hauptdiagonalen eine 0 steht
                {
                    double temp;
                
                    for(int j = 0; j < matrix.length; j++)
                    {
                        temp = matrix[i][j];
                        matrix[i][j] = matrix[i+1][j];
                        matrix[i+1][j] = temp;
                    }

                    i--;
                }
            }
        }

        for (int i = 0; i < matrix.length; i++)                                   //Nur auf der Hauptdiagonalen Eintraege
        {
            faktor = matrix[i][i];

            if (faktor != 0)
            {
                for (int j = 0; j < matrix.length; j++)
                {
                    matrix[i][j] = matrix[i][j] / faktor;
                }
            }
        }

        matrixAusgeben(matrix);

        double[] loesung = new double[matrix.length];                             //Loesungsvektor erstellen

        for (int i = 0; i < matrix.length; i++)
        {
            loesung[i] = matrix[i][matrix.length-1];
        }

        return loesung;
    }   
}