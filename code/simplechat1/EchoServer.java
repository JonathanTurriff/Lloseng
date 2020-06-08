// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer
{
  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  //I couldn't figure out how to determine if the server is closed or not because isClosed() isnt working so i just used booleans
  private boolean stopped;
  private boolean closed;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port)
  {
    super(port);
    stopped = false;
    closed = false;
  }


  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  { String message  = (String) msg;
    String[] command  = message.split(" ");
    try{
      if(client.getInfo("loginID") == null){
        client.setInfo("loginID", command[1]);
        sendToAllClients(client.getInfo("loginID") +" Has Logged in.");
        System.out.println("New Connection: " +client.getInfo("loginID"));
      }else if(command[0].equals("#login")){
        client.sendToClient("Error: You have already logged in");
        System.out.println("New Disconnection: " +client.getInfo("loginID"));
        sendToAllClients(client.getInfo("loginID")+" Has Disconnected.");
        client.close();
      }else{
      System.out.println("Message received: " + msg + " from " + client+", loginID: "+client.getInfo("loginID"));
      this.sendToAllClients(client.getInfo("loginID")+": "+msg);
      }
  }
    catch(IOException e){
      System.out.println("Error: Could not send message to server.");
    }
    }
  /**
   * This method handles any messages received from the Server Console.
   *
   * @param msg The message received from the client.
   */
  public void handleMessageFromServer(String msg ){
    if(msg.length() ==0){
      return;
    }
    try{
    if(msg.substring(0,1).equals("#")){
      handleServerCommands(msg);//new command i made to handle the # commands to have the code clean
    }else{
      System.out.println("SERVER MSG> "+msg);
      sendToAllClients("SERVER MSG> "+ msg);
    }
    }catch(IOException e){
      System.out.println("Error: Could not send message to server.");

    }
  }

  /**
   * This method handles any command received from the Server Console.
   *
   * @param msg The message received from the client.
   */
  public void handleServerCommands(String message) throws IOException{

    String[] command = message.split(" ");

    if(command[0].equals("#quit")){
      System.out.println("Closing ServerConsole...");
      sendToAllClients("SERVER MSG> Server is closing...");
      close();
      System.exit(0);
    }else if (command[0].equals("#stop")){
      stopped = true;
      stopListening();
    }else if (command[0].equals("#close")){
      sendToAllClients("SERVER MSG> Server is closing...");
      System.out.println("Server is now closed.");
      closed = true;
      stopped = true;
      close();
    }else if (command[0].equals("#setport")){
      if(!closed){
        System.out.println("Error: You can't change hosts if you're already connected to port " + getPort());
      }else{
        try{
        setPort(Integer.parseInt(command[1]));
        System.out.println("You are now set to: "+ getPort());
      }catch(Exception e){
        System.out.println("Error: Invalid arguments. Try again.");
      }
    }
    }else if (command[0].equals("#getport")){
      System.out.println("You're connected to host " + getPort());
    }else if(command[0].equals("#start")){
      if(stopped){
        listen();
        stopped = false;
        closed = false;
      }else{
        System.out.println("Server is already started.");
      }
    }else{
      System.out.println("SERVER MSG> "+message);
      sendToAllClients("SERVER MSG> "+ message);
    }
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  //Class methods ***************************************************


  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   **/
  // protected void clientConnected(ConnectionToClient client) {
  //   System.out.println("New Connection: "+ client.getInfo("loginID"));
  // }

  /* Hook method called each time an exception is thrown in a
  * ConnectionToClient thread.
  * The method may be overridden by subclasses but should remains
  * synchronized.
  *
  * @param client the client that raised the exception.
  * @param Throwable the exception thrown.
   */
  synchronized protected void clientException(
     ConnectionToClient client, Throwable exception) {
       sendToAllClients("New Disconnection: "+ client.getInfo("loginID"));
       System.out.println( client.getInfo("loginID")+ "Has disconnected.");
    }
    synchronized protected void clientDisconnected(
       ConnectionToClient client) {
         System.out.println("New Disconnection: "+ client.getInfo("loginID"));
      }
//Got rid of Main in this class to implement it in ServerConsole.java
}
//End of EchoServer class
