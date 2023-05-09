/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package practicauf5;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.*;
//8 * 15
/**
 *
 * @author Baiti
 */
public class MenuPrincipal extends JFrame implements ActionListener, ChangeListener{
    private Connection c;
    private final int butaquesCount = 120;
    private JPanel titolPanel, butacasPanel, dadesPanel;
    private JLabel titolImagen;
    private JButton [] [] butaques;
    private ArrayList<String> butaquesSeleccionades = new ArrayList<>();
    private ArrayList<String> butaquesOcupades = new ArrayList<>();
    private ImageIcon butacaLliure, butacaOcupada, butacaSeleccionada;
    private JTextField nomPersona, dniPersona, preuTotalField;
    private JTextArea entradesReservades;
    private JButton pantallaPagament, pagament;
    private JFrame ventanaPagament;
    private JSpinner entradaGeneralSpinner, entradaJoveSpinner, entradaJubilatSpinner;
    boolean potObrirPagament;
    private int preuTotal;
    public MenuPrincipal(Connection c) throws SQLException{
        potObrirPagament = true;
        this.c = c;
        c.setAutoCommit(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        butacaLliure = new ImageIcon("./recursos/butacaLliure.png");
        Image image = butacaLliure.getImage(); // transform it 
        Image newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        butacaLliure = new ImageIcon(newimg);  // transform it back
        
        butacaSeleccionada = new ImageIcon("./recursos/butacaSeleccionada.png");
        image = butacaSeleccionada.getImage(); // transform it 
        newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        butacaSeleccionada = new ImageIcon(newimg);  // transform it back
        
        butacaOcupada = new ImageIcon("./recursos/ButacaOcupada.png");
        image = butacaOcupada.getImage(); // transform it 
        newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        butacaOcupada = new ImageIcon(newimg);  // transform it back
        
        butaques = new JButton [9][16];
        this.getContentPane().setLayout(new BorderLayout()); 
        setSize(new Dimension(950, 750));
        setResizable(false);
        titolImagen = new JLabel();
        ImageIcon fondo = new ImageIcon("./recursos/titol.png");
        image = fondo.getImage(); // transform it 
        newimg = image.getScaledInstance(650, 160,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
        fondo = new ImageIcon(newimg);  // transform it back
        titolPanel = new JPanel();
        

        titolPanel.setBackground(new Color(104, 103, 117));
        titolImagen.setIcon(fondo);
        titolPanel.add(titolImagen);
        add(titolPanel, BorderLayout.NORTH);


        butacasPanel = new JPanel();
        butacasPanel.setLayout(new GridLayout(8, 15));
        butacasPanel.setBackground(new Color(235, 230, 234));
        butacasPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inicializar_butaques();
        add(butacasPanel, BorderLayout.CENTER);
        
        Border border = BorderFactory.createLineBorder(new Color(203, 185, 200), 5);
        dadesPanel = new JPanel();
        dadesPanel.setLayout(new BoxLayout(dadesPanel,BoxLayout.Y_AXIS));
        dadesPanel.setPreferredSize(new Dimension(334, getHeight() - titolPanel.getHeight()));
        dadesPanel.setBackground(new Color(210, 198, 208));
        dadesPanel.setBorder(border);   
        add(dadesPanel, BorderLayout.EAST);
        dadesPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        JLabel nomLabel = new JLabel("Nom de la persona");
        nomLabel.setBounds(0, 100, 100, 100);
        nomLabel.setAlignmentX(CENTER_ALIGNMENT);
        dadesPanel.add(nomLabel);
        dadesPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        nomPersona = new JTextField();
        nomPersona.setMaximumSize(new Dimension(200, 25));
        nomPersona.setAlignmentX(CENTER_ALIGNMENT);
        dadesPanel.add(nomPersona);
        
        dadesPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        JLabel dniLabel = new JLabel("Dni de la persona");
        dniLabel.setBounds(0, 100, 100, 100);
        dniLabel.setAlignmentX(CENTER_ALIGNMENT);
        dadesPanel.add(dniLabel);
        
        dniPersona = new JTextField();
        dniPersona.setMaximumSize(new Dimension(200, 25));
        dniPersona.setAlignmentX(CENTER_ALIGNMENT);
        dadesPanel.add(dniPersona);
        
        dadesPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel entradesLabel = new JLabel("Entrades reservades");
        entradesLabel.setBounds(0, 100, 100, 100);
        entradesLabel.setAlignmentX(CENTER_ALIGNMENT);
        dadesPanel.add(entradesLabel);
        dadesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        entradesReservades = new JTextArea();
        entradesReservades.setAlignmentX(CENTER_ALIGNMENT);
        entradesReservades.setMaximumSize(new Dimension(200, 300));
        entradesReservades.setEditable(false);


        dadesPanel.add(entradesReservades);
        
        pantallaPagament = new JButton();
        pantallaPagament.setText("Anar a la pantalla de pagament");
        pantallaPagament.addActionListener(this);
        pantallaPagament.setAlignmentX(CENTER_ALIGNMENT);
        dadesPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        dadesPanel.add(pantallaPagament);
        setTitle("CINEMA PARADISO");
        pack();
    }
    
    private void inicializar_butaques() throws SQLException{
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery("select fila, columna from butaques");
        while(rs.next()){
            butaquesOcupades.add(rs.getString(1) + ":" + rs.getString(2));
        }
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 15; j++){
                JButton butaca = new JButton();
                butaca.setBackground(new Color(235, 230, 234));
                butaca.setBorder(null);
                butaca.setCursor(new Cursor(Cursor.HAND_CURSOR));
                if(butaquesOcupades.indexOf(i + ":" + j) != -1){
                    butaca.setIcon(butacaOcupada);
                }else{

                    butaca.setIcon(butacaLliure);
                }

                butaca.setText("" + i + ":" + j);
                butaca.setFont(new Font("Serif", Font.ITALIC, 0));
                butaca.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton source = (JButton) e.getSource();
                        if(source.getIcon().equals(butacaOcupada)){
                            JOptionPane.showMessageDialog(null, "Aquesta butaca ja està ocupada");
                        }else if(source.getIcon().equals(butacaLliure)){
                            Image image = butacaSeleccionada.getImage(); // transform it 
                            Image newimg = image.getScaledInstance(40, 40,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
                            butacaSeleccionada = new ImageIcon(newimg);  // transform it back
                            int fila = Integer.parseInt(source.getText().substring(0, source.getText().indexOf(":")));
                            int columna = Integer.parseInt(source.getText().substring(source.getText().indexOf(":") + 1, source.getText().length()));
                            source.setIcon(butacaSeleccionada);
                            entradesReservades.setText(entradesReservades.getText() + "Fila: " + fila + " Columna: " + columna + "\n");
                            butaquesSeleccionades.add(fila + ":" + columna);
                        }else if(source.getIcon().equals(butacaSeleccionada)){
                            source.setIcon(butacaLliure);
                            int fila = Integer.parseInt(source.getText().substring(0, source.getText().indexOf(":")));
                            int columna = Integer.parseInt(source.getText().substring(source.getText().indexOf(":") + 1, source.getText().length()));
                            source.setIcon(butacaLliure);
                            butaquesSeleccionades.remove(fila + ":" + columna);
                        }
                        if(entradesReservades.getText().length() > 16 * 19){
                           Font f = entradesReservades.getFont();
                            System.out.println(f.getFontName());
                           entradesReservades.setFont(new Font("TimesRoman", Font.PLAIN, 9));
                        }if(entradesReservades.getText().length() > 16 * 25){
                            JOptionPane.showMessageDialog(null, "No pots tenir el cinema només per tu :|");
                        }
                    }
                });
                butaques[i][j] = butaca;
                butacasPanel.add(butaca);
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == pantallaPagament){
            if(butaquesSeleccionades.size() == 0){
                JOptionPane.showMessageDialog(null, "Escolleix alguna butaca");
            }else if(potObrirPagament && !nomPersona.getText().equals("") && !dniPersona.getText().equals("")){
                 //pantallaPagament.setAlignmentX(CENTER_ALIGNMENT);
                preuTotal = 0;
                potObrirPagament = false;
                ventanaPagament = new JFrame();
                ventanaPagament.setLayout(new FlowLayout());
                JPanel titol = new JPanel();
                ventanaPagament.addWindowListener(new WindowAdapter()
                {
                    @Override
                    public void windowClosing(WindowEvent e)
                    {
                      potObrirPagament = true;
                      e.getWindow().dispose();
                    }
                });

                ventanaPagament.setTitle("Pantalla pagament");
                ventanaPagament.setPreferredSize(new Dimension(400, 500));
                
                titol.setLayout(new FlowLayout(FlowLayout.CENTER));
                JLabel totalEntrades = new JLabel();
                totalEntrades.setText("TOTAL ENTRADES: " + String.valueOf(butaquesSeleccionades.size()));
                totalEntrades.setForeground(Color.green);
                totalEntrades.setFont(new Font("TimesRoman", Font.PLAIN, 30));
                titol.add(totalEntrades);
                System.out.println(butaquesSeleccionades.size());
                ventanaPagament.add(titol);

                ventanaPagament.add(Box.createRigidArea(new Dimension(0, 100)));
                
                JLabel entradaGeneral = new JLabel("Entrada General");
                entradaGeneral.setFont(new Font("TimesRoman", Font.PLAIN, 15));
                entradaGeneral.setPreferredSize(new Dimension(120, 20));
                ventanaPagament.add(entradaGeneral);
                entradaGeneralSpinner = new JSpinner();
                entradaGeneralSpinner.setPreferredSize(new Dimension(180, 30));
                entradaGeneralSpinner.addChangeListener(this);
                
                ventanaPagament.add(entradaGeneralSpinner);
                
                ventanaPagament.add(Box.createRigidArea(new Dimension(0, 20)));
                
                JLabel entradaJove = new JLabel("Entrada Jove");
                entradaJove.setFont(new Font("TimesRoman", Font.PLAIN, 15));
                entradaJove.setPreferredSize(new Dimension(120, 20));
                ventanaPagament.add(entradaJove);
                entradaJoveSpinner = new JSpinner();
                entradaJoveSpinner.setPreferredSize(new Dimension(180, 30));
                entradaJoveSpinner.addChangeListener(this);
                ventanaPagament.add(entradaJoveSpinner);
                
                ventanaPagament.add(Box.createRigidArea(new Dimension(0, 100)));
                
                JLabel entradaJubilat = new JLabel("Entrada Jubilat");
                entradaJubilat.setFont(new Font("TimesRoman", Font.PLAIN, 15));
                entradaJubilat.setPreferredSize(new Dimension(120, 20));
                ventanaPagament.add(entradaJubilat);
                entradaJubilatSpinner = new JSpinner();
                entradaJubilatSpinner.setPreferredSize(new Dimension(180, 30));
                entradaJubilatSpinner.addChangeListener(this);
                ventanaPagament.add(entradaJubilatSpinner);
                
                
                JLabel preuTotalLabel = new JLabel("Preu Total");
                preuTotalLabel.setFont(new Font("TimesRoman", Font.PLAIN, 15));
                preuTotalLabel.setPreferredSize(new Dimension(120, 20));
                ventanaPagament.add(preuTotalLabel);
                
                preuTotalField = new JTextField();
                preuTotalField.setPreferredSize(new Dimension(180, 30));
                preuTotalField.setEditable(false);
                preuTotalField.setHorizontalAlignment(JTextField.RIGHT);
                ventanaPagament.add(preuTotalField);
                
                ventanaPagament.add(Box.createRigidArea(new Dimension(0, 100)));
                
                pagament = new JButton("PAGAMENT");
                pagament.setFont(new Font("TimesRoman", Font.BOLD, 23));
                pagament.setEnabled(false);
                pagament.addActionListener(this);
                ventanaPagament.add(pagament);
                JLabel espacio = new JLabel();
                espacio.setPreferredSize(new Dimension(0, 60));
                ventanaPagament.add(espacio);
                ventanaPagament.setVisible(true);
                ventanaPagament.pack();
                ventanaPagament.setResizable(false);
            }else{
                JOptionPane.showMessageDialog(null, "El nom i dni no poden estar buits");
            }
        }
        
        
        if(e.getSource() == pagament){
            Statement st;
            try {
                st = c.createStatement();
                int i = 0;
                while(i < butaquesSeleccionades.size()){
                    for(int generals = 0; generals < (int) entradaGeneralSpinner.getValue(); generals++){
                        String filaColumn = butaquesSeleccionades.get(i);
                        String [] filaColumnV = filaColumn.split(":");
                        System.out.println("insert into butaques values('" + nomPersona.getText() + "','" + dniPersona.getText() + "'," + filaColumnV[0] + "," + filaColumnV[1]);
//                        st.execute("insert into butaques values('" + nomPersona.getText() + "','" + dniPersona + "'," + filaColumnV[0] + "," + filaColumnV[1]);
                        i++;
                    }
                    
                    
                }
                
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() == entradaGeneralSpinner || e.getSource() == entradaJoveSpinner || e.getSource() == entradaJubilatSpinner){
            preuTotal = (int) entradaGeneralSpinner.getValue() * 10 + (int) entradaJoveSpinner.getValue() * 6 + (int) entradaJubilatSpinner.getValue() * 7;
            preuTotalField.setText(String.valueOf(preuTotal));
            if((int) entradaGeneralSpinner.getValue() + (int) entradaJoveSpinner.getValue() + (int) entradaJubilatSpinner.getValue() ==  butaquesSeleccionades.size()){
                pagament.setEnabled(true);
            }else{
                pagament.setEnabled(false);
            }
            
            if(entradaGeneralSpinner.getValue().equals(-1)){
                entradaGeneralSpinner.setValue(0);
            }else if(entradaJoveSpinner.getValue().equals(-1)){
                entradaJoveSpinner.setValue(0);
            }else if(entradaJubilatSpinner.getValue().equals(-1)){
                entradaJubilatSpinner.setValue(0);
            }
        }
    }
    
}
