package talkerclientapp;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLDocument;

/**
 *
 * @author amster
 */
public class Client implements Runnable
{

	Socket socket;
	static ObjectOutput objectOutput;
	ObjectInput objectInput;
	MessageFrame messageFrame;
	Boolean goOutFromMainLoop = false;
	HTMLDocument incomingMessageHTMLDocument;
	SimpleAttributeSet incomingMessageStyle;

	WindowAdapter wa = new java.awt.event.WindowAdapter()
	{
		@Override
		public void windowClosing(WindowEvent winEv)
		{

			System.out.println("zamknieto okno");
			Main.messageFramesMap.remove(messageFrame.contact.talkerNumber);
		}
	};

	Client(Socket socket) throws IOException
	{

		this.socket = socket;

		// out = new PrintWriter(socket.getOutputStream());
		objectOutput = new ObjectOutputStream(socket.getOutputStream());
		objectInput = new ObjectInputStream(socket.getInputStream());

		// in = new Scanner(socket.getInputStream(),
		// StandardCharsets.UTF_8.toString());
		// sendMessage(number.toString());
	}

	public static void sendMessage(String message, int receiverNumber)
	{

		try
		{
			Map messageMap = new HashMap();
			messageMap.put("receiverNumber", receiverNumber);
			messageMap.put("action", "MESSAGE");
			messageMap.put("content", message);

			System.out.println("obiekt messageMap z Client: " + messageMap);

			objectOutput.writeObject(messageMap);
			objectOutput.flush();
		} catch (IOException ex)
		{
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void sendMessage(Object message)
	{
		try
		{
			System.out.println("obiekt message z Client: " + message);
			objectOutput.writeObject(message);
			objectOutput.flush();
		} catch (IOException ex)
		{
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public static void sendExitMessage()
	{

		try
		{
			Map messageMap = new HashMap();
			messageMap.put("receiverNumber", 0);
			messageMap.put("senderNumber", Main.myPersonalData.talkerNumber);
			messageMap.put("action", "EXIT");

			objectOutput.writeObject(messageMap);
			objectOutput.flush();
		} catch (IOException ex)
		{
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void exit()
	{
		goOutFromMainLoop = true;

	}

	@Override
	public void run()
	{

		HashMap requestObject = null;
		int senderNumber = -1;
		Object request;

		try
		{
			while (!goOutFromMainLoop)
			{
				// objectInput = new ObjectInputStream(socket.getInputStream());
				if ((request = objectInput.readObject()) != null)
				{

					System.out.println("co przyszlo z servera: " + request);

					if (request instanceof HashMap)
					{
						requestObject = (HashMap) request;
					}
					if (requestObject.get("senderNumber") != null)
					{
						senderNumber = (int) requestObject.get("senderNumber");
					}
					if (senderNumber == 0)
					{

						if (requestObject.get("action").equals("REGISTRATION"))
						{

							if (((Boolean) requestObject.get("status")) == true)
							{
								Main.myPersonalData.talkerNumber = (Integer) requestObject.get("newNumber");
								System.out.println("number: " + Main.myPersonalData.talkerNumber);
							} else
							{

							}
						}

						if (requestObject.get("action").equals("LOGIN"))
						{

							if ((Boolean) requestObject.get("status") == true)
							{
								// TODO logowanie
								System.out.println("zalogowano pomyslnie");
							}
						}
					}

					if (requestObject.get("action").equals("MESSAGE"))
					{
						System.out.println("requestObject: " + requestObject);

						// String content =
						// Main.contactsMap.get(senderNumber).displayedName +
						// ":\n" + (String) requestObject.get("content") +
						// "\n\n";
						if ((messageFrame = Main.messageFramesMap.get(senderNumber)) == null)
						{

							System.out.println("nie ma takiego okna otwartego");

							messageFrame = new MessageFrame();// TODO nadac oknu
																// wartosci ?
							if (Main.contactsMap.get(senderNumber) != null)
								messageFrame.contact = Main.contactsMap.get(senderNumber);

							if (messageFrame.contact.displayedName != null)
							{
								messageFrame.setTitle(messageFrame.contact.displayedName + " (" + senderNumber + ")");
							} else
							{
								messageFrame.setTitle("nieznajomy (" + senderNumber + ")");
							}

							messageFrame.addWindowListener(wa);
							messageFrame.setVisible(true);

							// messageFrame.incomingMessageTextPane.append(content);
							// //TODO poprawic
							if(messageFrame.contact.talkerNumber != null)
							Main.messageFramesMap.put(messageFrame.contact.talkerNumber, messageFrame);
						}

						incomingMessageHTMLDocument = messageFrame.incomingMessageHTMLDocument;
						incomingMessageStyle = messageFrame.incomingMessageStyle;

						// incomingMessageStyle =
						// incomingMessageStyledDocument.addStyle("regular",
						// null);
						// System.out.println("incomingMessageDocument.getLength() : "
						// + incomingMessageDocument.getLength());
						System.out.println("messageFrame.incomingMessageTextPane.getText().length() : " + messageFrame.incomingMessageTextPane.getText().length());

						StyleConstants.setBold(incomingMessageStyle, true);
						StyleConstants.setForeground(incomingMessageStyle, new Color(0, 155, 0));

						if (Main.contactsMap.get(senderNumber).displayedName != null)
							incomingMessageHTMLDocument.insertString(incomingMessageHTMLDocument.getLength(), Main.contactsMap.get(senderNumber).displayedName + ":",
									incomingMessageStyle);
						else
							incomingMessageHTMLDocument.insertString(incomingMessageHTMLDocument.getLength(), "nieznajomy (" + senderNumber + "):", incomingMessageStyle);

						incomingMessageHTMLDocument.insertString(incomingMessageHTMLDocument.getLength(), "\n", Main.regularStyle);
						//
						// StyleConstants.setForeground(incomingMessageStyle,
						// Color.BLACK);
						// StyleConstants.setBold(incomingMessageStyle, false);

						messageFrame.incomingEditorKit.insertHTML(incomingMessageHTMLDocument, incomingMessageHTMLDocument.getLength(), (String) requestObject.get("content"), 0,
								0, null);

						// incomingMessageStyledDocument.insertString(incomingMessageStyledDocument.getLength(),
						// (String) requestObject.get("content"),
						// incomingMessageStyle);
						incomingMessageHTMLDocument.insertString(incomingMessageHTMLDocument.getLength(), "\n", Main.regularStyle);

						messageFrame.incomingMessageTextPane.setCaretPosition(incomingMessageHTMLDocument.getLength());

					}
				}
				// if (in.hasNext()) {
				// String input = in.nextLine();
				// System.out.println(input);
				// MessageFrame.incomingMessageTextArea.append("he said: " +
				// input + "\n");
				// }
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			try
			{
				sendExitMessage();
				// objectInput.close();
				// objectOutput.close();
				// socket.close();
			} catch (Exception ex)
			{
				Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

}
