import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient extends Frame implements ActionListener, Runnable {

    TextField tf;
    TextArea ta;
    Button send;

    Socket socket;
    BufferedReader in;
    PrintWriter out;

    String name;

    public ChatClient() {

        // GUI setup
        setTitle("Chat Application");
        setSize(500, 500);
        setLayout(new BorderLayout());

        ta = new TextArea();
        ta.setEditable(false);

        tf = new TextField();
        send = new Button("Send");

        add(ta, BorderLayout.CENTER);

        Panel bottom = new Panel();
        bottom.setLayout(new BorderLayout());
        bottom.add(tf, BorderLayout.CENTER);
        bottom.add(send, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);

        send.addActionListener(this);

        // close window
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setVisible(true);

        connectServer();
    }

    void connectServer() {
        try {
            socket = new Socket("localhost", 5000);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            name = javax.swing.JOptionPane.showInputDialog(this, "Enter your name:");
            out.println(name);

            Thread t = new Thread(this);
            t.start();

        } catch (Exception e) {
            ta.append("Connection failed\n");
        }
    }

    public void run() {
        try {
            String msg;

            while ((msg = in.readLine()) != null) {
                ta.append(msg + "\n");
            }

        } catch (Exception e) {
            ta.append("Disconnected\n");
        }
    }

    public void actionPerformed(ActionEvent e) {
        String msg = tf.getText();

        out.println(msg);

        tf.setText("");
    }

    public static void main(String[] args) {
        new ChatClient();
    }
}