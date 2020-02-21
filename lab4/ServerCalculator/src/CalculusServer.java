import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;

public class CalculusServer {
    private static Deque<String> stackop = new ArrayDeque<>();
    private static Deque<Float> stackva = new ArrayDeque<>();

    public static float doOp(String query) throws Exception
    {
        return sortQuery(query);
    }


    private static float sortQuery(String query) {
        Deque<Character> stackvaPro = new ArrayDeque<>();

        String q = "+-x/";

        int length = query.length();

        for (int i = 0; i < length; i++) {
            char c = query.charAt(i);
            String s = String.valueOf(c);
            if (!q.contains(s))
                stackvaPro.push(c);
            else if (q.contains(s)) {
                stackop.push(s);
                addNumber(stackva, stackvaPro);
            }
            if (i == length -1) {
                addNumber(stackva, stackvaPro);
            }
        }
        return calculator(stackop, stackva, length);
    }

    /**
     * @param stackva final queue of real numbers
     * @param stackvaPro provisional queue of numbers
     */
    private static void addNumber(Deque<Float> stackva, Deque<Character> stackvaPro) {
        Deque<Character> stackvaProv = new ArrayDeque<Character>();
        int n = 0;
        for (char item: stackvaPro) {
            stackvaProv.push(stackvaPro.pop());
            n += 1;
        }
        char newArr[] = new char[n];
        for (int j = 0; j < n; j++) {
            newArr[j] = stackvaProv.pop();
        }
        String b = new String(newArr);
        float value = Float.parseFloat(b);
        stackva.push(value);
    }

    /**
     * @param stackop queue of the different operations
     * @param stackva queue of the different numbers (1 digit numbers)
     * @return result of the query
     */
    private static float calculator(Deque<String> stackop, Deque<Float> stackva, int length) {
        String opeDM = "/x";
        Deque<Float> stackvaPM = new ArrayDeque<>();
        Deque<String> stackopPM = new ArrayDeque<>();

        for(String s : stackop) {
            if (stackvaPM.peek() == null) {
                stackvaPM.push(stackva.pop());
            }
            if (opeDM.contains(s)) {
                String ope = stackop.pop();
                float val = stackvaPM.pop();
                if (ope.equals("x")) {
                    stackvaPM.push(val * stackva.pop());
                } else {
                    stackvaPM.push(val / stackva.pop());
                }
            } else {
                stackopPM.push(stackop.pop());
                stackvaPM.push(stackva.pop());
            }
        }

        float result = 0;
        int first = 0;
        // case no plus +/- operator, then only one value in queue, which is the final result
        if (stackopPM.peek() == null) {
            result = stackvaPM.pop();
        } else {
            // sum up the last numbers left
            for(String s : stackopPM) {
                if (first == 0) {
                    result = stackvaPM.pop();
                }
                if (s.equals("+")) {
                    result = result + stackvaPM.pop();
                } else {
                    result = result - stackvaPM.pop();
                }
                first = 1;
            }
        }
        return result;
    }



    public static void main(String[] args) throws Exception {

        // Example of a distant calculator
        int port = 9876;
        ServerSocket ssock = new ServerSocket(port);

        while (true) { // infinite loop
            System.out.println("Server is listening on port " + port);
            Socket comm = ssock.accept();
            System.out.println(comm);
            System.out.println("connection established");

            new Thread((Runnable) new MyCalculusRunnable(comm)).start();

        }

    }
}
