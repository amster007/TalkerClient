/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talkerclientapp;

import common.Contact;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.net.SocketFactory;
import javax.swing.text.Style;

/**
 *
 * @author Amster
 */
public class Main {

    private static final int PORT = 22222;
    private static final String HOST = "localhost";//37.8.204.215
    static Client client;

    static String password;
    static HashMap<Integer, MessageFrame> messageFramesMap = new HashMap<>();
    static Contact myPersonalData = new Contact();
    static HashMap<Integer, Contact> contactsMap = new HashMap<>(); //TODO pobierac kontakty z servera i je zapisywac KONTAKTY TO TAKZE GRUPY WIEC TRZEBA ZMIENIC <INTEGER, OBJECT> ?
    static Contact deserializedPersonalData = null;
    static Style regularStyle;
    static Pattern pattern;
    static final String DIR_TO_SERIALIZED_DATA = "talkerData";
    static final File DIR = new File(DIR_TO_SERIALIZED_DATA);
    static final String EMAIL_PATTERN_REGULAR_EXPRESSION = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static void main(String args[]) {    	
    	
        try {
            final SocketFactory factory = SocketFactory.getDefault();
            final Socket socket = factory.createSocket(HOST, PORT);
            client = new Client(socket);
            Thread t = new Thread(client);
            t.start();

            deserializeData();
            registerOrLogin();

        } catch (IOException ex) { 
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    static void deserializeData() {
        DIR.mkdirs();

        File personal_data = new File(DIR_TO_SERIALIZED_DATA + "/personal_data.dat");
        File contacts = new File(DIR_TO_SERIALIZED_DATA + "/contacts.dat");
        ObjectInput in;

        if (personal_data.exists()) {
//            System.out.println("czy istnieje : " + personal_data.exists());

            try {
                in = new ObjectInputStream(new FileInputStream(personal_data));
                deserializedPersonalData = (Contact) in.readObject();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            myPersonalData = deserializedPersonalData;

            System.out.println(deserializedPersonalData.firstName);
            System.out.println(deserializedPersonalData.lastName);
            System.out.println(deserializedPersonalData.birthDate);
            System.out.println(deserializedPersonalData.displayedName);
            System.out.println(deserializedPersonalData.phoneNumber);
            System.out.println(deserializedPersonalData.talkerNumber);

        }

        if (contacts.exists()) {
            try {
                in = new ObjectInputStream(new FileInputStream(contacts));
                contactsMap = (HashMap) in.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    static void registerOrLogin() {
        if (deserializedPersonalData != null) {
            File passwdFile = new File(DIR_TO_SERIALIZED_DATA + "/passwd.dat");

            if (passwdFile.exists()) {

                HashMap message = new HashMap();
                message.put("action", "LOGIN");
                message.put("receiverNumber", 0);
                message.put("senderNumber", Main.myPersonalData.talkerNumber);

                Main.client.sendMessage(message);

                if (true)//TODO autoryzacja
                {
                    new MainWindowFrame().setVisible(true);
                }

            } else {
                new LoginFrame().setVisible(true);
            }
        } else {
            new RegistrationFrame().setVisible(true);
        }
    }

    public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();

        //FOR BEST QUALITY
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING,
                RenderingHints.VALUE_DITHER_ENABLE);

        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }

}
