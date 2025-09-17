/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.SwingUtilities;
import ui.WelcomePage;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(WelcomePage::new); // Start on the new Index/Welcome page
    }
}


