package command;

import java.io.Serializable;

/**
 * Created by wangye on 17/12/7.
 */
public class RpcRequest implements Serializable{
    private String interfaceFullName;
    private String methodName;

    private String localIp;

    private String remoteIp;
    private int remotePort;
    private Class[] paramTypes;
    private Object[] params;

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    private int opaque;

    public String getInterfaceFullName() {
        return interfaceFullName;
    }

    public void setInterfaceFullName(String interfaceFullName) {
        this.interfaceFullName = interfaceFullName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }



    public int getOpaque() {
        return opaque;
    }

    public void setOpaque(int opaque) {
        this.opaque = opaque;
    }
}
