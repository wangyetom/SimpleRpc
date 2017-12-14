package serialize;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by wangye on 17/12/7.
 */
public class JsonSerializier implements Serializier {
    @Override
    public byte[] serilize(Object input) {
        return JSON.toJSONBytes(input);
    }

    @Override
    public <T> T deserilize(byte[] input,Class<T> type) {
        return JSON.parseObject(input,type);
    }
}
