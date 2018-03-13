package Selle;

public class Sekantenverfahren_mit_Rekursion {

    //Zaehler fuer Rekursion
    private static int counter = 0;

    //Funktion
    public static double f(double x) {
        return  x*x*x-2*x*x-5*x+6;
    }


    /**
     * Funktion, um Nullstellen mithilfe des Sekantenverfahrens zu bestimmen.
     * @param xn x-Wert (Schritt n)
     * @param xnminus1 x-Wert (Schritt n-1)
     * @return Nullstelle(n)
     */
    public static double Sek(double xn, double xnminus1) {

        //Zaehler fuer Rekursion
        if (counter > 30)
            return xn;
        else
            counter++;
        double xnplus1 = xn - ((xn-xnminus1)/f(xn)-f(xnminus1))*f(xn);

        //Wenn ein Wert fehlerhaft ist werden Schritte vom Newtonverfahren uebernommen.
        while (Double.isNaN(xnplus1) || Double.isInfinite(xnplus1)) {
            xnplus1 = Newtonverfahren_mit_Rekursion.newton(xn);
            xn=xnminus1;
        }
        return Sek(xnplus1, xn);
    }

    public static void main(String[] args) {

        //zwei zufaellige Werte
        double a = 2.0;
        double b = 9.0;
        System.out.println(a);
        System.out.println(Sek(a, b));
    }

}
