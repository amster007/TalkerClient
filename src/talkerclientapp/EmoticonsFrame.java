/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talkerclientapp;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Amster
 */
public class EmoticonsFrame extends JFrame implements Runnable {

    String clickedEmoticonPath = "";

    MouseAdapter ma = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
//            System.out.println("kliknieto forme");
            //TODO pobierz klikniety obrazek
            if (isVisible()) {
                setVisible(false);
            } else {
                setVisible(true);
            }
        }

    };

    WindowAdapter EmoticonsFrameWindowAdapter = new WindowAdapter() {

        @Override
        public void windowLostFocus(WindowEvent e) {
            super.windowLostFocus(e);
//            System.out.println("lost focus");

            setVisible(false);
        }

    };

    public EmoticonsFrame() {
//        System.out.println("emoticonsFrame constructor");
        setUndecorated(true);
        addMouseListener(ma);
        addWindowFocusListener(EmoticonsFrameWindowAdapter);
        getContentPane().setBackground(Color.WHITE);

        GridLayout emoticonsGrid = new GridLayout(0, 10);
        setLayout(emoticonsGrid);

        int i, emoticonSize = 36;
        for (i = 1; i < 81; i++) {

            BufferedImage image = null;
            try {
                image = ImageIO.read(Main.class.getResource("/resources/images/emoticons/blue/" + i + ".png"));//TODO poprawic na wczytywanie wszystkich emotek z foldera
            } catch (IOException ex) {
                Logger.getLogger(MessageFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            BufferedImage resizedImage = Main.resizeImage(image, emoticonSize, emoticonSize);//may be havy operation

            JLabelWithImagePath emoticon = new JLabelWithImagePath();
            emoticon.setBounds(0, 0, emoticonSize, emoticonSize);
            emoticon.setIcon(new ImageIcon(resizedImage));
            emoticon.path = "resources/images/emoticons/blue/" + i + ".png";
//            emoticon.setBackground(Color.red);

//            getRootPane().setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(122, 138, 153)));

            emoticon.addMouseListener(new MouseAdapter() {
                @Override
                synchronized public void mousePressed(MouseEvent e) {

                    super.mousePressed(e);
//                    System.out.println("kliknieto emotke na formie emotek: " + ((JLabelWithImagePath) e.getComponent()).path);
//                    System.out.println("4: " + Thread.currentThread().getName());
                    clickedEmoticonPath = ((JLabelWithImagePath) e.getComponent()).path;
                    notifyAll();
                    setVisible(false);

                }

            });

            add(emoticon);
        }
//        System.out.println("koniec constructora");
    }

    String getClickedEmoticonPath() {
        return clickedEmoticonPath;

    }

    @Override
    public void run() {
        setVisible(true);
    }

    class JLabelWithImagePath extends JLabel {

        String path = "";
    }

}
