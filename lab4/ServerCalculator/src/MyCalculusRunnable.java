import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class MyCalculusRunnable implements Runnable {
    private Socket sock;

    public MyCalculusRunnable(Socket s) {
        sock = s;
    }

    @Override
    public void run() {

        try {
            DataInputStream dis = new DataInputStream(sock.getInputStream());
            DataOutputStream dos = new DataOutputStream(sock.getOutputStream());

            // read length of file send as int to server
            int length = dis.readInt();
            System.out.println(length);
            // define byte array
            byte[] array = new byte[length];
            int index = 0;
            // read all bytes coming from dataStream until none available
            while (dis.available() > 0) {
                byte b = dis.readByte();
                array[index] = b;
                index++;
            }
            // convert byte array to String
            String query = new String(array);

            System.out.println(query);

            float res = CalculusServer.doOp(query);
            System.out.println(res);

            // send back result
            dos.writeFloat(res);

            dis.close();
            dos.close();
            sock.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
