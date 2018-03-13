package Selle;

import java.util.Scanner;

public class Caesar {



    /**
     * Funktion, um eine String mit dem Caesarverfahren zu verschluesseln.
     * @param klartext unverschluesselter Text
     * @param schluessel Schluessel
     * @return verschluesselten text
     */

    public static String verschluesseln(String klartext, int schluessel)
    {
        String verschluesselt = "";
        klartext = klartext.toUpperCase();
        for (int i = 0; i < klartext.length(); ++i) {
            verschluesselt += (char) ( ( klartext.charAt( i ) - 65 + schluessel ) % 26 + 65 );
        }
        return verschluesselt;
    }


    /**
     * Funtion, um einen Text mit dem Caesarverfahren zu entschluesseln.
     * @param verschText verschluesselter Text
     * @param schluessel Schluessel
     * @return Klartext
     */

    public static String entschluesseln(String verschText, int schluessel)
    {
        return verschluesseln( verschText, -schluessel );
    }


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String txt = "";
        int verschiebung = 0;

        while (1 == 1) {
            System.out.println("Geben sie den zu (ver-)/entschluesselden Text ein!");
            txt = sc.next();
            System.out.println("Geben sie die Verschiebung ein! (Entschluesselung = 27 - Verschluesselung)");
            verschiebung = sc.nextInt();
            if (verschiebung < 1) {
                System.out.println("Geben sie eine Ganze Zahl groesser als 0 ein!");
            } else if (verschiebung > 26) {
                System.out.println("Geben sie eine Zahl kleiner als 27 ein!");
            }
            System.out.println("Wollen Sie verschluesseln(v) oder entschluesseln(e)?");
            String entscheidung = sc.next();
            if (entscheidung.equals("v")) {
                System.out.println(verschluesseln(txt, verschiebung));
            } else if (entscheidung.equals("e")) {
                System.out.println(entschluesseln(txt, verschiebung));
            }
        }

    }


}
