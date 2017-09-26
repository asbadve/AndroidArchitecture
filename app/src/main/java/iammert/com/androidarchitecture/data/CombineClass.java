package iammert.com.androidarchitecture.data;

import java.util.List;

/**
 * Created by ajinkyabadve on 26/9/17.
 */

public class CombineClass<T> {
    List<T> list;

    public CombineClass(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
