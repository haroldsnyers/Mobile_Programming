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

            // read string in dataInputStream
            String query = dis.readUTF();

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
