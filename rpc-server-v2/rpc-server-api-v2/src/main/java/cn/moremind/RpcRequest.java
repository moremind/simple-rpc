package cn.moremind;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1041322970832269647L;
    private String className;
    private String methodName;
    private Object[] parameters;

    private String version;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpcRequest that = (RpcRequest) o;
        return className.equals(that.className) && methodName.equals(that.methodName) && Arrays.equals(parameters, that.parameters) && version.equals(that.version);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(className, methodName, version);
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }
}
