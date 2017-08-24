import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor extends UnicastRemoteObject
  implements InterfaceServidor
{
  //protected Map<Integer, InterfaceCliente> objetosRemotosUsuarios;
  protected List<InterfaceCliente> objetosUsuarios;
  protected List<String> nombresUsuarios;
  public String mensaje = "";
  static String hostRegistry;


  public Servidor() throws RemoteException
  {
    super();
    //objetosRemotosUsuarios = new TreeMap<>();
    nombresUsuarios = new ArrayList<String>();
    objetosUsuarios = new ArrayList<InterfaceCliente>();
  }

  public String registrar(String n){
    Iterator<String> iter = nombresUsuarios.iterator();
    int i = 0;
    while (iter.hasNext()){
      String elemento = iter.next();
      if(elemento.equals(n))
       return "duplicado";
     i++;
    }

    return "OK";
  }

  public boolean conectar(String n)
  {
    try{
      Registry registry = LocateRegistry.getRegistry(hostRegistry);
      System.out.print("Buscando el objeto remoto de "+n + "...");
      String nombre_objeto_remoto = n;
      
      InterfaceCliente instancia_local = (InterfaceCliente) registry.lookup(nombre_objeto_remoto);
      int id = (nombresUsuarios.size());
      System.out.println(" Objeto remoto encontrado");
      nombresUsuarios.add(id, n);
      objetosUsuarios.add(id, instancia_local);
      
      RefreshListado(" se a conectado", n);
      return true;
    }
    catch(RemoteException ex){System.out.println(ex);}
    catch (NotBoundException ex){System.out.println(ex);}
    return false;
  }

  public void desconectar(String n){
    int id = nombresUsuarios.indexOf(n);

    nombresUsuarios.remove(id);
    objetosUsuarios.remove(id);

    System.out.println("El usuario "+n+" se a desconectado.");

    RefreshListado(" se a desconectado", n);
  }

  private void RefreshListado(String notificacion, String usr){

    Iterator<InterfaceCliente> iter = objetosUsuarios.iterator();

    while (iter.hasNext()){
      InterfaceCliente instancia_local = iter.next();
      
      try{
        instancia_local.refrescarListado();
        instancia_local.publicarNotificacion(notificacion, usr);
      }
      catch(RemoteException ex){System.out.println(ex);}

    }
    
  }

  public List<String> obtenerLista(){
    return nombresUsuarios;
  }

  public boolean pasarMensaje(String m, String usr_em, String usr_re){
    try{
  
      objetosUsuarios.get(nombresUsuarios.indexOf(usr_re)).imprimirMensaje(m, usr_em);
    
      return true;
    }
    catch(RemoteException ex){System.out.println(ex);}
    return false;    
  }
  
  public static void main( String args[] )
  {
    try
    {
      Servidor ds = new Servidor();
      Naming.rebind( "ChatServer", ds );
      System.out.println( "Chat Server esperando usuarios ..." );
    }
    catch ( Exception e ) { System.out.println(e); }
  }

}