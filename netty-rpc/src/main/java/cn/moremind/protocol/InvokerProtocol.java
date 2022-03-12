package cn.moremind.protocol;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class InvokerProtocol implements Serializable {

    private static final long serialVersionUID = 3692229671153583770L;
    private String className;//类名
    private String methodName;//函数名称
    private Class<?>[] parames;//形参列表
    private Object[] values;//实参列表

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public Class<?>[] getParames() {
        return parames;
    }

    public void setParames(Class<?>[] parames) {
        this.parames = parames;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvokerProtocol)) return false;
        InvokerProtocol that = (InvokerProtocol) o;
        return getClassName().equals(that.getClassName()) && getMethodName().equals(that.getMethodName()) && Arrays.equals(getParames(), that.getParames()) && Arrays.equals(getValues(), that.getValues());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getClassName(), getMethodName());
        result = 31 * result + Arrays.hashCode(getParames());
        result = 31 * result + Arrays.hashCode(getValues());
        return result;
    }
}

