package talkerclientapp;

import common.Contact;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;

import static java.lang.Thread.sleep;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.jfree.ui.FontChooserDialog;

/**
 *
 * @author amster
 */
public class MessageFrame extends javax.swing.JFrame {

    Integer senderNumber = Main.myPersonalData.talkerNumber;
    Contact contact;
    ImageIcon icon;
    int styleCounter = 0;
    SimpleAttributeSet outcomingMessageStyle;
    SimpleAttributeSet incomingMessageStyle;

    StyledDocument outcomingMessageStyledDocument;

    HTMLEditorKit incomingEditorKit;
//    HTMLEditorKit outcomingEditorKit;
    HTMLDocument incomingMessageHTMLDocument;
//    HTMLDocument outcomingMessageHTMLDocument;

    WindowAdapter messageFrameWindowAdapter = new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent winEv) {

            System.out.println("zamknieto okno wiadomosci");
            Main.messageFramesMap.remove(contact.talkerNumber);
        }
    };

    public MessageFrame() {
        initComponents();

        outcomingMessageTextPane.setContentType("text/html");
        incomingMessageTextPane.setContentType("text/html");

        outcomingMessageStyledDocument = outcomingMessageTextPane.getStyledDocument();
        incomingMessageHTMLDocument = new HTMLDocument();
//        outcomingMessageHTMLDocument = new HTMLDocument();

        outcomingMessageStyle = new SimpleAttributeSet(outcomingMessageTextPane.getInputAttributes().copyAttributes());
        incomingMessageStyle = new SimpleAttributeSet(incomingMessageTextPane.getInputAttributes().copyAttributes());

        incomingEditorKit = new HTMLEditorKit();
//        outcomingEditorKit = new HTMLEditorKit();

        incomingMessageTextPane.setEditorKit(incomingEditorKit);
        incomingMessageTextPane.setDocument(incomingMessageHTMLDocument);

//        outcomingMessageTextPane.setEditorKit(outcomingEditorKit);
//        outcomingMessageTextPane.setDocument(outcomingMessageHTMLDocument);
        Main.regularStyle = outcomingMessageStyledDocument.addStyle("regular", null);

        this.setLocationRelativeTo(null);
        outcomingMessageTextPane.requestFocus();

        BufferedImage image = null;
        try {
            image = ImageIO.read(Main.class.getResource("/resources/images/emoticons/blue/40.png"));
        } catch (IOException ex) {
            Logger.getLogger(MessageFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedImage resizedImage = Main.resizeImage(image, 36, 36);// emoticonsLabel.getWidth(), getHeight());

        emoticonsLabel.setIcon(new ImageIcon(resizedImage));

        try {
            image = ImageIO.read(Main.class.getResource("/resources/images/colorChooser.png"));
        } catch (IOException ex) {
            Logger.getLogger(MessageFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        resizedImage = Main.resizeImage(image, 28, 28);

        colorChooserLabel.setIcon(new ImageIcon(resizedImage));

        addWindowListener(messageFrameWindowAdapter);

        outcomingMessageTextPane.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getExtendedKeyCode() == 10) {
                    if (e.isShiftDown()) {
//                                System.out.println("shit + enter");
//                        outcomingMessageTextPane.append("\n"); //TODO poprawic
//                                caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);
                    } else {
                        e.consume();
                        sendMessage();

                    }
                }
            }
        });

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sendMessageButton = new javax.swing.JButton();
        emoticonsLabel = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        incomingMessageTextPane = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        outcomingMessageTextPane = new javax.swing.JTextPane();
        changeFontLabel = new javax.swing.JLabel();
        colorChooserLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        sendMessageButton.setText("Wyślij wiadomość");
        sendMessageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessageButtonActionPerformed(evt);
            }
        });

        emoticonsLabel.setPreferredSize(new java.awt.Dimension(36, 36));
        emoticonsLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                emoticonsLabelMousePressed(evt);
            }
        });

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setDividerSize(15);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setOneTouchExpandable(true);

        incomingMessageTextPane.setEditable(false);
        incomingMessageTextPane.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane4.setViewportView(incomingMessageTextPane);

        jSplitPane1.setLeftComponent(jScrollPane4);

        jScrollPane3.setViewportView(outcomingMessageTextPane);

        jSplitPane1.setRightComponent(jScrollPane3);

        changeFontLabel.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        changeFontLabel.setText(" A");
        changeFontLabel.setPreferredSize(new java.awt.Dimension(36, 36));
        changeFontLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                changeFontLabelMousePressed(evt);
            }
        });

        colorChooserLabel.setPreferredSize(new java.awt.Dimension(36, 36));
        colorChooserLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                colorChooserLabelMousePressed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jSplitPane1)
                    .add(layout.createSequentialGroup()
                        .add(sendMessageButton)
                        .add(18, 18, 18)
                        .add(emoticonsLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(changeFontLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(colorChooserLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 142, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(changeFontLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(colorChooserLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, sendMessageButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, emoticonsLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sendMessageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendMessageButtonActionPerformed

        sendMessage();
        outcomingMessageTextPane.requestFocus();
    }//GEN-LAST:event_sendMessageButtonActionPerformed

    private void emoticonsLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_emoticonsLabelMousePressed

        Style s = outcomingMessageStyledDocument.addStyle("icon", null);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);

        Thread addEmoticonToTextPaneThread = new Thread(new Runnable() {

            @Override
            public void run() {
                EmoticonsFrame emoticonsFrame = new EmoticonsFrame();

                if (!emoticonsFrame.isVisible()) {
                    emoticonsFrame.setBounds((int) emoticonsLabel.getLocationOnScreen().getX(), (int) emoticonsLabel.getLocationOnScreen().getY() - 288, 360, 288);
                    emoticonsFrame.setVisible(true);
                }

                while (emoticonsFrame.clickedEmoticonPath.equals("")) {

                    try {
                        sleep(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MessageFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

//                icon = new ImageIcon(Main.class.getResource(emoticonsFrame.clickedEmoticonPath));
////                emoticonsLabel.setIcon(icon);
//                System.out.println("sciezka do emota: " + emoticonsFrame.clickedEmoticonPath);
//
//                if (icon != null) {
//                    StyleConstants.setIcon(s, icon);
//                }

                try {
                    incomingEditorKit.insertHTML((HTMLDocument) outcomingMessageStyledDocument, outcomingMessageTextPane.getCaretPosition(), 
                            "<img src=\"" + Main.class.getResource("/" + emoticonsFrame.clickedEmoticonPath) + "\"/>", 0, 0, HTML.Tag.IMG);
                    //"<img src=\"file:" + emoticonsFrame.clickedEmoticonPath + "\"/>", 0, 0, HTML.Tag.IMG);
                    outcomingMessageTextPane.setCaretPosition(outcomingMessageTextPane.getCaretPosition() + 1);
                } catch (IOException ex) {
                    Logger.getLogger(MessageFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BadLocationException ex) {
                    Logger.getLogger(MessageFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

                emoticonsFrame.clickedEmoticonPath = "";
            }
        });

        addEmoticonToTextPaneThread.start();

    }//GEN-LAST:event_emoticonsLabelMousePressed

    private void changeFontLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_changeFontLabelMousePressed

        FontChooserDialog fcd = new org.jfree.ui.FontChooserDialog(this, "Wybierz czcionkę", true, outcomingMessageTextPane.getFont());

        fcd.setMinimumSize(new Dimension(350, 350));
        fcd.setBounds((int) changeFontLabel.getLocationOnScreen().getX(), (int) changeFontLabel.getLocationOnScreen().getY() - 350, 350, 350);
        fcd.setVisible(true);

        if (!fcd.isCancelled()) {//TODO poprawic warunek
            System.out.println("ustawiono czcionke");

            Font selectedFont = fcd.getSelectedFont();

            SimpleAttributeSet newFont = new SimpleAttributeSet();
            StyleConstants.setFontFamily(newFont, selectedFont.getFamily());
            StyleConstants.setFontSize(newFont, selectedFont.getSize());
            StyleConstants.setBold(newFont, selectedFont.isBold());
            StyleConstants.setItalic(newFont, selectedFont.isItalic());

            System.out.println(outcomingMessageTextPane.getSelectionEnd());

            if (outcomingMessageTextPane.getSelectedText() == null) {
                outcomingMessageStyledDocument.setCharacterAttributes(0, outcomingMessageTextPane.getText().length(), newFont, false);
            } else {
                outcomingMessageStyledDocument.setCharacterAttributes(outcomingMessageTextPane.getSelectionStart(), outcomingMessageTextPane.getSelectedText().length(), newFont, false);
            }
        }

    }//GEN-LAST:event_changeFontLabelMousePressed

    private void colorChooserLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_colorChooserLabelMousePressed

//        outcomingMessageStyle = new SimpleAttributeSet(outcomingMessageTextPane.getInputAttributes().copyAttributes());
//        SimpleAttributeSet BOLD = new SimpleAttributeSet();
//        StyleConstants.setBold(BOLD, true);
//        StyleConstants.setForeground(BOLD, Color.BLUE);
//        StyleConstants.setFontSize(BOLD, 48);
//
//        previousAttribs.addAttributes(BOLD);
        Color selectedColor = JColorChooser.showDialog(this, "Wybierz kolor", Color.BLACK);
        System.out.println(selectedColor);

        if (selectedColor != null) {

            SimpleAttributeSet newColor = new SimpleAttributeSet();
            StyleConstants.setForeground(newColor, selectedColor);
//            outcomingMessageStyle.addAttributes(newColor);

            if (outcomingMessageTextPane.getSelectedText() == null) {
                outcomingMessageStyledDocument.setCharacterAttributes(0, outcomingMessageTextPane.getText().length(), newColor, false);
            } else {
                outcomingMessageStyledDocument.setCharacterAttributes(outcomingMessageTextPane.getSelectionStart(), outcomingMessageTextPane.getSelectedText().length(), newColor, false);
            }

        }

        System.out.println("text po zmianie koloru --------->  " + outcomingMessageTextPane.getText());

    }//GEN-LAST:event_colorChooserLabelMousePressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel changeFontLabel;
    private javax.swing.JLabel colorChooserLabel;
    private javax.swing.JLabel emoticonsLabel;
    public javax.swing.JTextPane incomingMessageTextPane;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSplitPane jSplitPane1;
    public javax.swing.JTextPane outcomingMessageTextPane;
    public static javax.swing.JButton sendMessageButton;
    // End of variables declaration//GEN-END:variables

    void sendMessage() {

        String outcomingText = null;
        try {
            outcomingText = outcomingMessageStyledDocument.getText(0, outcomingMessageStyledDocument.getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(MessageFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!outcomingText.equals("")) {
            Map messageMap = new HashMap();
            messageMap.put("receiverNumber", contact.talkerNumber);
            messageMap.put("senderNumber", senderNumber);
            messageMap.put("action", "MESSAGE");
            messageMap.put("content", outcomingMessageTextPane.getText());

            StyleConstants.setBold(incomingMessageStyle, true);
            StyleConstants.setForeground(incomingMessageStyle, new Color(0, 0, 255));

//            outcomingMessageTextPane.setContentType("text/html");
            try {
                incomingMessageHTMLDocument.insertString(incomingMessageHTMLDocument.getLength(), Main.myPersonalData.displayedName, incomingMessageStyle);
                incomingMessageHTMLDocument.insertString(incomingMessageHTMLDocument.getLength(), "\n", Main.regularStyle);

                incomingEditorKit.insertHTML(incomingMessageHTMLDocument, incomingMessageHTMLDocument.getLength(), outcomingMessageTextPane.getText(), 0, 0, null);
                incomingMessageHTMLDocument.insertString(incomingMessageHTMLDocument.getLength(), "\n", Main.regularStyle);

                incomingMessageTextPane.setCaretPosition(incomingMessageHTMLDocument.getLength());

            } catch (BadLocationException ex) {
                Logger.getLogger(MessageFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MessageFrame.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("=================== " + outcomingMessageTextPane.getText());

            outcomingMessageTextPane.setText("");
            Main.client.sendMessage(messageMap);

        } else {
            outcomingMessageTextPane.setText("");
        }

    }

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     */
    public static void main(String args[]) throws IOException, InterruptedException {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        }


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            {
                MessageFrame messageFrame = new MessageFrame();
                messageFrame.setVisible(true);

            }
        });

    }

}
