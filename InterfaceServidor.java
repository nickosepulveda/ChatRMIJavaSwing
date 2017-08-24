import java.rmi.*;
import java.util.*;
import java.io.*;
import java.lang.*;

public interface InterfaceServidor 
                   extends Remote {
  public String registrar(String n)throws 
                  RemoteException;
  public boolean conectar(String n) throws 
                  RemoteException;
  public void desconectar(String n) throws 
                  RemoteException;  
  public boolean pasarMensaje(String m, String usr_em, String usr_re) throws 
                  RemoteException;                  
  public List<String> obtenerLista() throws 
                  RemoteException;      
}
