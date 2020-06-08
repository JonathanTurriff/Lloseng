// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF
{
  //Class variables *************************************************

  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Instance variables **********************************************

  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;


  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port, String loginID)
  {
    try
    {
      client= new ChatClient(host, port, this, loginID);
    }
    catch(IOException exception)
    {
      System.out.println("Cannot open connection.  Awaiting command.");

    }
  }


  //Instance methods ************************************************

  /**
   * This method waits for input from the console.  Once it is
   * received, it sends it to the client's message handler.
   */
  public void accept()
  {
    try
    {
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true)
      {
        message = fromConsole.readLine();
        client.handleMessageFromClientUI(message);
      }
    }
    catch (Exception ex)
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message)
  {
    System.out.println("> " + message);
  }


  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args)
  {
    String host = "";
    int port = 0;  //The port number
    String loginID = "";
    try
    {
       loginID = args[0] ; //Get loginid from command line
    }
    catch(Throwable t)
    {
      System.out.println("Error: No login ID specified.  Connection aborted.");
      System.exit(0);
    }

    try
    {
      port = Integer.parseInt(args[2]); //Get port from command line
    }
    catch(ArrayIndexOutOfBoundsException e)

    {
      port = DEFAULT_PORT; //Set port to 5555
    }

    try
    {
      host = args[3]; //Get HostName from command line
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost"; //sets as default
    }
    ClientConsole chat= new ClientConsole(host, port, loginID);
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
