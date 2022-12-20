import java.net.*;
import java.util.Scanner;
import java.io.*;

public class Main {
	static Scanner receiver1;
	static BufferedWriter sender1;
	static Scanner receiver2;
	static BufferedWriter sender2;
	static Socket player1;
	static Socket player2;
	static InputStream input1;
	static InputStream input2;
	public static void main(String[] args) throws UnknownHostException, IOException {
		@SuppressWarnings("resource")
		ServerSocket ssocket = new ServerSocket(1400);
		while (true) {
			System.out.println("\nWaiting for players to connect");
			player1 = ssocket.accept();
			System.out.println("Player 1 connected");
			receiver1 = new Scanner(new InputStreamReader(player1.getInputStream()));
			sender1 = new BufferedWriter(new OutputStreamWriter(player1.getOutputStream()));
			player2 = ssocket.accept();
			System.out.println("Player 2 connected");
			receiver2 = new Scanner(new InputStreamReader(player2.getInputStream()));
			sender2 = new BufferedWriter(new OutputStreamWriter(player2.getOutputStream()));
			send1("player1");
			send2("player2");
			//send1(System.currentTimeMillis()+"");
			System.out.println("Game initialized");
			try {
				input1 = player1.getInputStream();
				input2 = player2.getInputStream();
			} catch (Exception e) {
				System.out.println("Player 1 disconnected, game aborted");
				e.printStackTrace();
				continue;
			}
			while (true) {
				String received1 = receive1();
				String received2 = receive2();
				if (received1!=null) {
					send2(received1);
					if (received1.charAt(0)=='F') {
						System.out.println("Game finished\nResults:\nPlayer 1 wins "+received1.substring(2));
						player1.close();
						player2.close();
						break;
					}
				}
				if (received2!=null) {
					if (received2.charAt(0)=='F') {
						System.out.println("Game finished\nResults:\nPlayer 2 wins "+received2.substring(2));
						player1.close();
						player2.close();
						break;
					}
					send1(received2);
				}
				if (player1==null) {
					System.out.println("Player 1 disconnected, game aborted");
					player2.close();
					break;
				}
				if (player2==null) {
					System.out.println("Player 2 disconnected, game aborted");
					player1.close();
					break;
				}
			}
		}
	}
	public static String receive1() {
		try {
			if (input1.available()>0) {
				return receiver1.nextLine();
			} else {
				return null;
			}
		} catch (IOException e) {
			player1 = null;
			System.exit(0);
			return null;
		}
	}
	public static String receive2() {
		try {
			if (input2.available()>0) {
				return receiver2.nextLine();
			} else {
				return null;
			}
		} catch (IOException e) {
			player2 = null;
			System.exit(0);
			return null;
		}
	}
	public static void send1(String data) {
		try {
			sender1.write(data);
			sender1.newLine();
			sender1.flush();
		} catch (Exception e) {
			player1 = null;
		}
	}
	public static void send2(String data) {
		try {
			sender2.write(data);
			sender2.newLine();
			sender2.flush();
		} catch (Exception e) {
			player2 = null;
		}
	}
}