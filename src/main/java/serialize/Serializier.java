package serialize;

/**
 * Created by wangye on 17/12/7.
 */
public interface Serializier {
    public byte[] serilize(Object input);


    <T> T deserilize(byte[] input, Class<T> type);
}
