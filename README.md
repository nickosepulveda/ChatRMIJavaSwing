# ChatRMIJavaSwing
Proyecto RMI hecho con JavaSwing

Antes que todo debes compilar los archivos .java del directorio. Para hacer esto mas rapido puedes ubicarte con la terminal en el interior del directorio y ejecuta "javac *.java" para compilar todos los archivos con extension .java.

Tambien no debes olvidar levantar rmiregistry, el cual se levanta de la siguiente forma. Ejecuta el comando "rmiregistry" en la terminal y comenzara a correr. Pero si lo quieres dejar como proceso fantasma (de forma permanente), puede ejecutar "rmiregistry &". 

Una vez teniendo todos los archivos compilados ya puedes probar la aplicación. Lo primero es arrancar el servidor, para eso ejecuta en la terminal el comando java Servidor <Puerto>, donde <puerto> sera el puerto donde quedara corriendo el servidor, normalmente se suele arrancar en el 1099. (Quedaria entonces: java Servidor 1099).

Luego levantamos el cliente, que puede ser arrancado tanto a nivel local o como de red. En nivel local basta con ejecuta el comando java Cliente <ip> <puerto>. donde <ip> es la ip correspondiente al servidor,  a nivel local es 127.0.0.1 y <puerto> es el puerto mencionado anterior. (Quedaria entonces: java Cliente 127.0.0.1 1099). Y con eso podriamos probarlo a nivel local.
A nivel de red solo basta con cambiar la ip:127.0.0.1 por la ip del servidor.
