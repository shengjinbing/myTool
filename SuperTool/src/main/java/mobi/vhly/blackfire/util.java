package mobi.vhly.blackfire;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Locale;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class util {

    public  static<T> T  getInstance(Class<T>type){
        Object o = Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new MyHandler());
        return (T)o;
    }

    static class MyHandler implements InvocationHandler{

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            newproxy annotation = method.getAnnotation(newproxy.class);
            if (annotation !=null){
                Class<?> returnType = method.getReturnType();
                String url = String.format(Locale.CHINA, annotation.value(), args);
                if (returnType.equals(NetWorkTask.class)){
                    ParameterizedType type = (ParameterizedType) method.getGenericReturnType();
                    Type[] actualTypeArguments = type.getActualTypeArguments();
                    Type actualTypeArgument = actualTypeArguments[0];

                    return new NetWorkTask<>(url, (Class) actualTypeArgument);
                }

            }
            return null;
        }
    }
}
