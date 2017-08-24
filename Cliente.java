import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.*;


public class Cliente extends javax.swing.JFrame{

  public InterfaceServidor instancia_local;
  public String login;

  public Cliente(){
    super();
    initComponents(); 
    this.setTitle("Login Whatsapp ULagos"); //Titulo de la ventana
    //this.setMinimumSize(new Dimension(300, 400)); //Tamaño de ventana
    this.setLocationRelativeTo(null); //centrar ventana
    this.setLayout(null);
    botonEntrar.setEnabled(false);
    botonEntrar.setText("Entrar");
    ImageIcon fot = new ImageIcon("img/Ulagos.png");
    Icon icono = new ImageIcon(fot.getImage().getScaledInstance(jLabelLogo.getWidth(), jLabelLogo.getHeight(), Image.SCALE_DEFAULT));
    jLabelLogo.setIcon(icono);
    this.repaint();

    this.setVisible(true);

  }

  public void invocarObjetoRemoto(String ip, String puerto) {
    jLabelEstado.setText("Esperando conexión al servidor...");
    try{
      instancia_local = (InterfaceServidor)Naming.lookup(
          "rmi://"+ip+":"+puerto+"/ChatServer");
      //JOptionPane.showMessageDialog(null, "Conexión.", "La conexion con el servidor se a realizado de forma correcta.", JOptionPane.WARNING_MESSAGE);
      jLabelEstado.setText("Listo.");
      botonEntrar.setEnabled(true);
    }catch(Exception e){
      JOptionPane.showMessageDialog(null, e, "invocarObjetoRemoto", JOptionPane.WARNING_MESSAGE);
    }

  }

  public static void main( String args[] ){
  
    if(args.length==2){
      Cliente vc = new Cliente();
      vc.setVisible(true);
      vc.invocarObjetoRemoto(args[0], args[1]);
    }
    else{
	   System.out.println("USO: java DateCliente <ip> <port>");
    } 
  }

  private void botonEntrarActionPerformed(java.awt.event.ActionEvent evt){
    login = textLogin.getText();
    textLogin.setEnabled(false);
    jLabelEstado.setText("Cargando...");
    System.out.println("Cargando...");
    //JOptionPane.showMessageDialog(null, login+" conectando...", "Verificando", JOptionPane.WARNING_MESSAGE);
    if(!login.equals("")){
      try{
        String respuesta = instancia_local.registrar(login);
    
        if(respuesta.equals("duplicado")) {
          jLabelEstado.setText("Listo.");
          textLogin.setEnabled(true);
          JOptionPane.showMessageDialog(null, "El usuario que usted a ingresado ya esta en uso.", "Duplicado", JOptionPane.WARNING_MESSAGE);
        }else {
            //JOptionPane.showMessageDialog(null, "Ingreso.", "Ingreso", JOptionPane.WARNING_MESSAGE);
            VentanaChat vchat = new VentanaChat(this);
            vchat.setVisible(true);
            this.setVisible(false);
        }
      }catch(RemoteException ex){}
    }
    else{
      textLogin.setEnabled(true);
      jLabelEstado.setText("Listo.");
      JOptionPane.showMessageDialog(null, "No ha ingresado ningun usuario", "Vacio", JOptionPane.WARNING_MESSAGE);
    }

  }

  private void initComponents() {
    jLabelLogo = new javax.swing.JLabel();
    jPanel1 = new javax.swing.JPanel();
    textLogin = new javax.swing.JTextField();
    botonEntrar = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    jLabelEstado = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setMaximumSize(new java.awt.Dimension(300, 400));
    setMinimumSize(new java.awt.Dimension(300, 400));
    setResizable(false);

    jLabelLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabelLogo.setText("jLabelLogo");
    jLabelLogo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

    textLogin.setToolTipText("Usuario");
    textLogin.setBounds(new java.awt.Rectangle(30, 150, 240, 40));

    botonEntrar.setText("Entrar");
    botonEntrar.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            botonEntrarActionPerformed(evt);
        }
    });

    jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
    jLabel1.setText("USUARIO");

    jLabelEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(80, 80, 80)
                            .addComponent(botonEntrar, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel1)))
                    .addGap(0, 74, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(textLogin)
                        .addComponent(jLabelEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
        jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(36, 36, 36)
            .addComponent(jLabel1)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(textLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabelEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(botonEntrar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabelLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addComponent(jLabelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap())
    );

    getAccessibleContext().setAccessibleDescription("");

    pack();
  }

  private javax.swing.JButton botonEntrar;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabelEstado;
  private javax.swing.JLabel jLabelLogo;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JTextField textLogin;

}