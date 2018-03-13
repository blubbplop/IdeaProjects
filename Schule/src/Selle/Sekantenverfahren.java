package Selle;

public class Sekantenverfahren {

    //Funktion
    public static double f(double x) {
        return x*x*x-2*x*x-5*x+6;
    }

    public static void main(String[] args) {

        //drei beliebige Variablen zum Starten.
        double xn = 2.3;
        double xnminus1 = 2.7;
        double xnplus1 = 7;

        //Schleife mit Abbruchbedingung als Genauigkeit
        while (Math.abs(xnplus1) > 0.01) {
            //Sekantenverfahren
            xnplus1 = xn - (xn-xnminus1)/(f(xn)-f(xnminus1))*f(xn);
            xnminus1=xn;
            xn=xnplus1;
        }

        //Ausgabe
        System.out.println(xnplus1);
    }
}
