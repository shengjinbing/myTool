package mobi.vhly.blackfire;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class NetWorkTask<T> extends AsyncTask<NetWorkTask.Callback<T>,Void,Object>{
    private  String url;
    private  Callback<T> mCallback;
    private Class<T> t;
    public NetWorkTask(String url, Class<T>t) {
        this.url = url;
        this.t = t;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Callback<T>... params) {
        mCallback = params[0];
        try {
            HttpURLConnection conn = (HttpURLConnection)new URL(url).openConnection();

            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            if (code == 200){
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream baos =  new ByteArrayOutputStream();
                byte[]  buffers = new byte[102400];
                int length;
                while ((length = is.read(buffers))!=-1){
                    baos.write(buffers,0,length);
                }

                is.close();
                baos.close();

                String json = baos.toString("UTF-8");
                Gson gson = new Gson();
                return  gson.fromJson(json, t);

            }else {
                return new RuntimeException("网络出现故障");
            }
        } catch (IOException e) {
            return e;
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Object o) {
        if (t.isInstance(o)){
            mCallback.Successlf((T) o);
        }else {
            mCallback.Failed((Exception) o);
        }

    }

    public  interface  Callback<S> {
        void Successlf(S o);
        void Failed(Exception e);
    }
}
