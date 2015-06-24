/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talkerclientapp;

/**
 *
 * @author Amster
 */
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
 
 
public class Test extends JFrame implements ActionListener {
 
    private JPanel control;
    private JScrollPane editorScrollPane;
    private JButton bold, italic;
    private JButton code, clear;
    private JTextPane p;
    private Style bolder;
 
    public Test() {
        p=new JTextPane();
         
        bolder=p.addStyle("bold",null);
        StyleConstants.setBold(bolder,true);
 
        p.setContentType("text/html");
        p.setEditable(true);
        p.setText("<h1>Hello</h1><i>this is italic</i><br>this is a <a href='123'>referenz</a><br>the end");
        editorScrollPane = new JScrollPane(p);
 
        control = new JPanel();
        bold = new JButton("<html><b>B");
        bold.addActionListener(this);
        control.add(bold);
        italic = new JButton("<html><i>I");
        italic.addActionListener(this);
        control.add(italic);
        code = new JButton("code");
        code.addActionListener(this);
        control.add(code);
        clear = new JButton("clear");
        clear.addActionListener(this);
        control.add(clear);
 
        setSize(300, 300);
        setLayout(new BorderLayout());
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(editorScrollPane, BorderLayout.CENTER);
        add(control, BorderLayout.SOUTH);
    }
 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(bold)) {
            makeBold();
        }
         
    }
    public void makeBold(){
        //System.out.println("here");
      int len=p.getSelectedText().length();
      p.getStyledDocument().setCharacterAttributes(p.getSelectionStart(),len,bolder,true);
    }
     
    public static void main(String[] args) {
        new Test();
    }
}