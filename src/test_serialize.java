import java.io.*;
import java.util.ArrayList;

public class test_serialize {
    public static void main(String[] args) {
        try {
            System.out.println("hi");

        } catch (Exception e) {

        }

        ObjectInputStream is;
        try {
            is = new ObjectInputStream(new FileInputStream("MyObject.ser"));
            ArrayList test2 = (ArrayList) is.readObject();
            System.out.println(test2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.exit(0);

        ArrayList test = new ArrayList();
        test.add(3);
        test.add(5);
        test.add(7);

        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new FileOutputStream("MyObject.ser"));
            os.writeObject(test);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
