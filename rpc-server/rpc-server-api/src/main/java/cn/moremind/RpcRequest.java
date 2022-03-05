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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpcRequest that = (RpcRequest) o;
        return Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName) && Arrays.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(className, methodName);
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }
}
