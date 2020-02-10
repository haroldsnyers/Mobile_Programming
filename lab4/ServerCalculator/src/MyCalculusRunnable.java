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

            // read op1, op2 and the opreation to make
            Double op1 = dis.readDouble();
            char op = dis.readChar();
            Double op2 = dis.readDouble();

            Double res = CalculusServer.doOp(op1, op2, op);

            // send back result
            dos.writeDouble(res);

            dis.close();
            dos.close();
            sock.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
