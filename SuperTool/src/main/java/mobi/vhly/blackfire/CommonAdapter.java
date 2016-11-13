package mobi.vhly.blackfire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

public abstract class CommonAdapter<D, VH extends CommonAdapter.ViewHolder> extends BaseAdapter{
    public Context mContext;
    private int layoutId;
    public List<D> list;
    private LayoutInflater inflater;

    public CommonAdapter(Context context, int layoutId, List<D> list) {
        this.mContext = context;
        this.layoutId = layoutId;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public D getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        D d = list.get(position);
        Class<?> aClass = d.getClass();
        Field id = null;
        try {
            id = aClass.getField("id");
        } catch (NoSuchFieldException e) {
        }
        if (id == null) {
            try {
                id = aClass.getDeclaredField("id");
            } catch (NoSuchFieldException e) {
            }
        }
        if (id != null) {
            try {
                return (long) id.get(d);
            } catch (IllegalAccessException e) {
            }
        }
        Method getId = null;
        try {
            getId = aClass.getMethod("getId");
        } catch (NoSuchMethodException e) {
        }
        if (getId == null) {
            try {
                getId = aClass.getDeclaredMethod("getId");
            } catch (NoSuchMethodException e) {
            }
        }
        if (getId != null) {
            try {
                return (long) getId.invoke(d);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layoutId, parent, false);
            Class type = (Class) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            try {
                Constructor constructor = type.getConstructor(View.class);
                Object o = constructor.newInstance(convertView);
                convertView.setTag(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        onBindView(list.get(position), (VH) convertView.getTag());
        return convertView;
    }

    public abstract void onBindView(D data, VH holder);

    public void addAll(Collection<? extends D> collection) {
        list.addAll(collection);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void add(D d) {
        list.add(d);
        notifyDataSetChanged();
    }

    public void add(int index, D d) {
        list.add(index, d);
        notifyDataSetChanged();
    }

    public void remove(D d) {
        list.remove(d);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        list.remove(index);
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        private View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }
}
