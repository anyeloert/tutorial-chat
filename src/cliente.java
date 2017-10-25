import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class cliente {
	
	JFrame ventana_chat = null;
	JButton btn_enviar = null;
	JTextField txt_mensaje = null;
	JTextArea area_chat = null;
	JPanel contenedor_areachat = null;
	JPanel contenedor_btntxt = null;
	Socket socket = null;
	DataInputStream lector = null;
	DataOutputStream escritor = null;
	
	public cliente() {
		hacerInterfaz();
	}
	
	public void hacerInterfaz() {
		ventana_chat=new JFrame("Cliente");
		btn_enviar=new JButton("Enviar");
		txt_mensaje=new JTextField(4);
		area_chat=new JTextArea(10, 12);
		contenedor_areachat=new JPanel();
		contenedor_areachat.setLayout(new GridLayout(1, 1));
		contenedor_areachat.add(area_chat);
		contenedor_btntxt=new JPanel();
		contenedor_btntxt.setLayout(new GridLayout(1,2));
		contenedor_btntxt.add(txt_mensaje);
		contenedor_btntxt.add(btn_enviar);
		ventana_chat.setLayout(new BorderLayout());
		ventana_chat.add(contenedor_areachat,BorderLayout.NORTH);
		ventana_chat.add(contenedor_btntxt,BorderLayout.SOUTH);
		ventana_chat.setSize(300, 220);
		ventana_chat.setVisible(true);
		ventana_chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Thread hiloPrincipal;
		hiloPrincipal = new Thread(() -> {
			try {				
				socket = new Socket("localhost",9000);		
				leer();
				escribir();
				
			} catch (IOException e) {
				System.out.println("error");
			}
		});
		hiloPrincipal.start();	
		
	}
	
	
	public void leer() {
		Thread hiloLeer;
		hiloLeer = new Thread(() -> {
			try {
				lector = new DataInputStream(socket.getInputStream());
				
				while (true) {
					
					String mensajeRecibido = lector.readUTF();
					area_chat.append("Cliente escribe:" + mensajeRecibido + "\n");
					
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		});
		hiloLeer.start();
	}
	
	public void escribir() {
		Thread hiloEscribir;
		hiloEscribir = new Thread(() -> {
			try {
				escritor = new DataOutputStream(socket.getOutputStream());
				btn_enviar.addActionListener((ActionEvent e) -> {
					String enviarMensaje = txt_mensaje.getText();
					
					try {
						escritor.writeUTF(enviarMensaje);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					txt_mensaje.setText("");
				});
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		});
		hiloEscribir.start();
	}


	
	public static void main(String[] args) {
		new cliente();

	}

}
