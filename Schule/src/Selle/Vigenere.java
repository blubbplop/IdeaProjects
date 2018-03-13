package Selle;

import java.util.Scanner;

public class Vigenere {


    /**
     * Funktion, um eine String mit dem Vigenereverfahren zu verschluesseln.
     * @param klartext unverschluesselter Text
     * @param schluessel Schluessel
     * @return verschluesselten text
     */

    public static String verschluesseln(String klartext, String schluessel) {
        String verschluesselt = "";
        klartext = klartext.toUpperCase();
        char[] charSchluessel = schluessel.toCharArray();
        for (int i = 0; i < klartext.length(); ++i) {
            verschluesselt += (char)(((klartext.charAt(i) + charSchluessel[i % charSchluessel.length]) % 26)+65);
        }
        return verschluesselt;
    }


    /**
     * Funtion, um einen Text mit dem Vigenereverfahren zu entschluesseln.
     * @param geheimText verschluesselter Text
     * @param schluessel Schluessel
     * @return Klartext
     */

    public static String entschluesseln(String geheimText, String schluessel) {
        String verschluesselt = "";
        geheimText = geheimText.toUpperCase();
        char[] charSchluessel = schluessel.toCharArray();
        for (int i = 0; i < geheimText.length(); i++) {
            verschluesselt += (char)(((geheimText.charAt(i) - charSchluessel[i % charSchluessel.length]) % 26)+65);
        }
        return verschluesselt;
    }


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String txt = "";
        String schluessel = "";

        while (1 == 1) {
            System.out.println("Geben sie den zu (ver-)/entschluesselden Text ein!");
            txt = sc.next();
            System.out.println("Geben sie den Schluessel ein");
            schluessel = sc.next();
            if (schluessel.equals(""))
                System.out.println("Error!");
            System.out.println("Wollen Sie verschluesseln(v) oder entschluesseln(e)?");
            String entscheidung = sc.next();
            if (entscheidung.equals("v")) {
                System.out.println(verschluesseln(txt, schluessel));
            } else if (entscheidung.equals("e")) {
                System.out.println(entschluesseln(txt, schluessel));
            }
        }

    }


}
