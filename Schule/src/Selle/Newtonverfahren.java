package Selle;

public class Newtonverfahren {


    //Ableitung
    public static double fstrich(double x) {
        return 3*x*x-4*x-5;
    }

    //Funktion
    public static double f(double x) {
        return x*x*x-2*x*x-5*x+6;
    }

    public static void main(String[] args) {
        //zufaelligen Wert waehlen, sollte das Ergebnis nicht veraendern
        double x = 6.23;
        while (Math.abs(f(x)) > 0.01) {
            //Rechnung des Newtonverfahrens
            x = x - (f(x)/fstrich(x));
        }
        System.out.println(x);
    }

}