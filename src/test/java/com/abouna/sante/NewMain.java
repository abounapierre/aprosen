/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abouna.sante;

import com.abouna.sante.util.Util;
import java.util.Scanner;

/**
 *
 * @author Emmanuel ABOUNA <eabouna@gmail.com>
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        System.out.println("Entrer un texte");
        String mot = sc.nextLine();
        String res = Util.hacherMotDepasse("SHA1", mot);
        System.out.println(res);
    }
}
