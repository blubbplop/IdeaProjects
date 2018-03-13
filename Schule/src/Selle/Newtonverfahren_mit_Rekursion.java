package Selle;


public class Newtonverfahren_mit_Rekursion{

    //zaehler fuer Rekursion
    private static int counter = 0;

    //Funktion
    public static double f(double x) {
        return  x*x*x-2*x*x-5*x+6;
    }

    //Ableitung
    public static double fstrich(double x) {
        return 3*x*x-4*x-5;
    }

    /**
     * Funktion, um Nullstellen mithilfe des Newtonverfahrens zu bestimmen.
     * @param x x-Wert
     * @return Nullstelle(n)
     */
    public static double newton(double x) {
        //Zaehler fuer Rekursion
        if (counter > 30)
            return x;
        else
            counter++;
            return newton(x - (f(x) / fstrich(x)));
    }

    public static void main(String[] args) {

        //zwei zufaellige Werte wurden gewaehlt.
        System.out.println(newton(6.7));

    }

}
