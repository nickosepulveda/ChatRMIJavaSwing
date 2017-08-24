import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCliente extends Remote {
    
    public void imprimirMensaje(String mensaje, String usr_em) throws RemoteException;
    public void refrescarListado() throws RemoteException;
    public void publicarNotificacion(String notificacion, String usr) throws RemoteException;
    
}