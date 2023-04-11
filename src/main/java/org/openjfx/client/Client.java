package org.openjfx.client;

import java.util.*;
import java.io.*;
import java.net.Socket;

import org.openjfx.server.Server;
import org.openjfx.server.models.*;

public class Client {
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public void connect(String ip, int port){
        try {
            client = new Socket(ip, port);
            output = new ObjectOutputStream(client.getOutputStream());
            input = new ObjectInputStream(client.getInputStream());
        }
        catch (IOException e) {
            System.out.println("Client.connect(): IOException: " + e);
        }
    }

    public ArrayList<Course> getCourses(String session) {
        try {
            if (client != null) {
                output.writeObject(Server.LOAD_COMMAND + " " + session);
                return (ArrayList<Course>) input.readObject();
            }
            else {
                return null;
            }
        } catch (IOException e){
            System.out.println("Client.getCourses(): IOException: " + e);
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Client.getCourses(): ClassNotFoundException: " + e);
            return null;
        }
    }
    public String registerToCourse(RegistrationForm form) {
        try{
            if (client != null) {
                output.writeObject(Server.REGISTER_COMMAND);
                output.writeObject(form);
                return (String) input.readObject();
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            System.out.println("Client.registerToCourse(): IOException: " + e);
            return null;
        }
        catch (ClassNotFoundException e) {
            System.out.println("Client.registerToCourse(): ClassNotFoundException: " + e);
            return null;
        }
    }

    public void stop() {
        try {
            input.close();
            output.close();
            client.close();
        } catch (IOException e) {
            System.out.println("Client.stop(): IOException: " + e);
        }

    }
}