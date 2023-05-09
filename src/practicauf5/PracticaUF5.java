/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package practicauf5;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Baiti
 */
public class PracticaUF5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
          Properties p = new Properties();
        try{
            p.load (new FileReader("oracle.properties")); //leo el archivo que contiene el usuario, conexion y contraseña.
        }catch(IOException e){
            System.out.println("No sha pogut llegir");
            System.out.println("" + e.getMessage());
            System.exit(1);
        }
        
        Connection c = null;
        try{
            //cojo cada parametro del archivo properties para establecer la conexion.
            String url = p.getProperty("url"); 
            String usuari  = p.getProperty("usuari");
            String password = p.getProperty("password");
            //establezco la conexion.
            c = DriverManager.getConnection(url, usuari, password);
            c.setAutoCommit(false);
            System.out.println("Connexio establerta!");
            System.out.println("Classe que implementa el connection: " + c.getClass().getName());
        }catch(SQLException e){
                System.out.println(e.getMessage());
        }
        MenuPrincipal mp = new MenuPrincipal(c);
        mp.setVisible(true);
    }
    
}
