package serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by wangye on 17/12/7.
 */
public class JavaSerializier implements Serializier {
    public byte[] serilize(Object input) {
        try {

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream out = null;

            out = new ObjectOutputStream(os);


            out.writeObject(input);
            os.flush();
            byte[] bytes = os.toByteArray();
            os.close();
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T deserilize(byte[] input, Class<T> type){
        try {
            if (input == null) throw new NullPointerException();

            ByteArrayInputStream is = new ByteArrayInputStream(input);
            ObjectInputStream in = null;

            in = new ObjectInputStream(is);
            Object object = in.readObject();
            in.close();
            return (T) object;
        } catch (Exception e) {
            return null;
        }
    }
}
