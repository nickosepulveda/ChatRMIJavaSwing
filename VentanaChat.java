import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaChat extends javax.swing.JFrame implements InterfaceCliente{

  private InterfaceServidor instancia_local;
  private String login;
  private Map<String, JTextArea> chats;
  private JTextArea chatActivo;

  public VentanaChat(Cliente v) throws RemoteException{
    instancia_local = v.instancia_local;
    login = v.login;
    initComponents();
    this.setTitle("Whatsapp ULagos"); //Titulo de la ventana
    this.setLocationRelativeTo(null); //centrar ventana
    JOptionPane.showMessageDialog(null, "Bienvenido(a) "+login+" a Whatsapp ULagos, por favor espere...", "Bienvenida", JOptionPane.WARNING_MESSAGE);
    
    this.publicarObjetoRemoto();
    chats = new TreeMap<>();

    this.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent evt) {
            try {
                onExit();
            } catch (RemoteException ex) {
                
            }
        }
    });
    
    //instancia_local.difundirMensaje("------ "+login+ " ha entrado en la sala ------");
  }

  public final void publicarObjetoRemoto() throws RemoteException{
    try{
      System.setProperty ("java.security.policy", "server.policy");

        if(System.getSecurityManager() == null){
              System.setSecurityManager(new SecurityManager());
        }
            
        InterfaceCliente interfaz = this;
        InterfaceCliente stub = (InterfaceCliente) UnicastRemoteObject.exportObject(interfaz, 0);
        
        Registry registry = LocateRegistry.getRegistry();
        String nombre_objeto_remoto = login;
        registry.rebind(nombre_objeto_remoto, stub);
        System.out.println("Cliente " + nombre_objeto_remoto + " bound");

            
        
      if(instancia_local.conectar( login ))
        nombreLogin.setText(login+" estas conectado(a)");
      else
        nombreLogin.setText(login+" estas desconectado(a)");
    }catch ( Exception e ){JOptionPane.showMessageDialog(null, e, "Error conexión RMI", JOptionPane.WARNING_MESSAGE);}
  }

  public void refrescarListado() {
   DefaultListModel<String> model = new DefaultListModel<>();
    try{
      List<String> listaUsuarios =  instancia_local.obtenerLista();
      if(listaUsuarios.size() > 1){
        Iterator<String> iter = listaUsuarios.iterator();

        int i = 0;
        while (iter.hasNext()){
          String elemento = iter.next();
          if(!elemento.equals(login))
           model.addElement(elemento);
         i++;
        }
      }   
      listaAmigos.setModel(model);
    }catch(Exception e){}  
    
  }

  public void imprimirMensaje(String mensaje, String usr_em){
    String el = usr_em;
    JTextArea jTextArea = null;

     if(!chats.containsKey(el)){
         jTextArea = new javax.swing.JTextArea();
         jTextArea.setEditable(false);
         chats.put(el, jTextArea);
         jTextArea.append(mensaje+"\n");
     }else{
        Iterator it = chats.entrySet().iterator();
        while (it.hasNext()) {
          Map.Entry e = (Map.Entry)it.next();
          String key = (String)e.getKey();
          if(key.equals(el)){
            jTextArea = (JTextArea) e.getValue();
            jTextArea.append(mensaje+"\n");
          }
        }
     }
     if(!jTextArea.equals(chatActivo))
      jTextAreaNotificaciones.append("Tienes un mensaje de ["+el+"].\n");
  }

  public void onExit() throws RemoteException {
    try{    
        instancia_local.desconectar(login);
        System.out.println("Usuario desconectado");
    }catch(RemoteException e){}
  }


  private void botonEnviarMensajeActionPerformed(java.awt.event.ActionEvent evt) {   
    String mensaje = textMensaje.getText().toString();
    if(listaAmigos.getSelectedIndex()!=-1 && !mensaje.equals("")){
      String el = listaAmigos.getSelectedValue().toString();                        
      chatActivo.append("[Yo]: "+mensaje+"\n");
      textMensaje.setText("");
      try{
        if(!instancia_local.pasarMensaje("["+login+"]: "+mensaje, login, el))
            chatActivo.append("Error al enviar mensaje a ["+el+"]");
      }catch(RemoteException ex){}
    }else{
      JOptionPane.showMessageDialog(null, "No tiene ningun contacto seleccionado para enviar su mensaje", "Error", JOptionPane.WARNING_MESSAGE);
    }
  }                                                  

  private void botonSalirActionPerformed(java.awt.event.ActionEvent evt)  {  
  try{    
      onExit();
      System.exit(0);
    }catch(RemoteException e){}                                         
      
  }   

  private void listaAmigosValueChanged(javax.swing.event.ListSelectionEvent evt) {                                         
         String el = listaAmigos.getSelectedValue().toString();

         if(!chats.containsKey(el)){
             JTextArea jTextArea = new javax.swing.JTextArea();
             jTextArea.setEditable(false);
             chats.put(el, jTextArea);
             chatActivo = jTextArea;
             jScrollPane2.setViewportView(jTextArea);
         }else{
            Iterator it = chats.entrySet().iterator();
            while (it.hasNext()) {
              Map.Entry e = (Map.Entry)it.next();
              String key = (String)e.getKey();
              if(key.equals(el)){
                JTextArea jTextArea = (JTextArea) e.getValue();
                chatActivo = jTextArea;
                jScrollPane2.setViewportView(jTextArea);
              }
            }
         }
         jPanelChat.setBorder(javax.swing.BorderFactory.createTitledBorder(el));
  }

  public void publicarNotificacion(String notificacion, String usr){
    if(!usr.equals(login))
      jTextAreaNotificaciones.append("["+usr+"]"+notificacion+".\n");
  }

  private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanelConectados = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        listaAmigos = new javax.swing.JList<>();
        jPanelNotificaciones = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaNotificaciones = new javax.swing.JTextArea();
        jPanelChat = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        textMensaje = new javax.swing.JTextField();
        botonEnviarMensaje = new javax.swing.JButton();
        jPanelPerfil = new javax.swing.JPanel();
        botonSalir = new javax.swing.JButton();
        nombreLogin = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(680, 400));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanelConectados.setBorder(javax.swing.BorderFactory.createTitledBorder("Conectados"));
        jPanelConectados.setToolTipText("");
        jPanelConectados.setName("Lista de amigos"); // NOI18N

        listaAmigos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listaAmigosValueChanged(evt);
            }
        });
        jScrollPane4.setViewportView(listaAmigos);

        javax.swing.GroupLayout jPanelConectadosLayout = new javax.swing.GroupLayout(jPanelConectados);
        jPanelConectados.setLayout(jPanelConectadosLayout);
        jPanelConectadosLayout.setHorizontalGroup(
            jPanelConectadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanelConectadosLayout.setVerticalGroup(
            jPanelConectadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
        );

        jPanelNotificaciones.setBorder(javax.swing.BorderFactory.createTitledBorder("Notificaciones"));

        jTextAreaNotificaciones.setEditable(false);
        jTextAreaNotificaciones.setColumns(12);
        jTextAreaNotificaciones.setRows(5);
        jScrollPane3.setViewportView(jTextAreaNotificaciones);

        javax.swing.GroupLayout jPanelNotificacionesLayout = new javax.swing.GroupLayout(jPanelNotificaciones);
        jPanelNotificaciones.setLayout(jPanelNotificacionesLayout);
        jPanelNotificacionesLayout.setHorizontalGroup(
            jPanelNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
        );
        jPanelNotificacionesLayout.setVerticalGroup(
            jPanelNotificacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelNotificaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelConectados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelConectados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelNotificaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelChat.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanelChatLayout = new javax.swing.GroupLayout(jPanelChat);
        jPanelChat.setLayout(jPanelChatLayout);
        jPanelChatLayout.setHorizontalGroup(
            jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanelChatLayout.setVerticalGroup(
            jPanelChatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        textMensaje.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N

        botonEnviarMensaje.setText("Enviar");
        botonEnviarMensaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEnviarMensajeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(textMensaje)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonEnviarMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonEnviarMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanelPerfil.setBorder(javax.swing.BorderFactory.createTitledBorder("Perfil"));

        botonSalir.setText("Salir");
        botonSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonSalirActionPerformed(evt);
            }
        });

        nombreLogin.setText("Usuario");

        javax.swing.GroupLayout jPanelPerfilLayout = new javax.swing.GroupLayout(jPanelPerfil);
        jPanelPerfil.setLayout(jPanelPerfilLayout);
        jPanelPerfilLayout.setHorizontalGroup(
            jPanelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPerfilLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nombreLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                .addGap(204, 204, 204)
                .addComponent(botonSalir)
                .addContainerGap())
        );
        jPanelPerfilLayout.setVerticalGroup(
            jPanelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPerfilLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(botonSalir)
                    .addComponent(nombreLogin))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelChat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelPerfil, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelChat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
  }

  private javax.swing.JButton botonEnviarMensaje;
  private javax.swing.JButton botonSalir;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanelChat;
  private javax.swing.JPanel jPanelConectados;
  private javax.swing.JPanel jPanelNotificaciones;
  private javax.swing.JPanel jPanelPerfil;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JTextArea jTextArea1;
  private javax.swing.JTextArea jTextAreaNotificaciones;
  private javax.swing.JList<String> listaAmigos;
  private javax.swing.JLabel nombreLogin;
  private javax.swing.JTextField textMensaje;
}