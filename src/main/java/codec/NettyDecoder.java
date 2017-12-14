/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import serialize.JsonSerializier;
import serialize.Serializier;

import java.nio.ByteBuffer;

public class NettyDecoder extends LengthFieldBasedFrameDecoder {

    private static final int FRAME_MAX_LENGTH =
            Integer.parseInt(System.getProperty("com.rocketmq.remoting.frameMaxLength", "16777216"));
    private Class jsonType;
    public NettyDecoder(Class jsonType) {
        super(FRAME_MAX_LENGTH, 0, 4, 0, 4);
        this.jsonType = jsonType;
    }

    private Serializier serializier = new JsonSerializier();

    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = null;
        try {

            frame = (ByteBuf) super.decode(ctx, in);
            if (null == frame) {
                return null;
            }
            byte[] result = new byte[frame.readableBytes()];
            frame.getBytes(0,result);


            return serializier.deserilize(result,jsonType);
        } catch (Exception e) {

        } finally {
            if (null != frame) {
                frame.release();
            }
        }

        return null;
    }
}
