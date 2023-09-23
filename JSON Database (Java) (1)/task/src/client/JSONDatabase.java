package client;

import java.util.ArrayList;
import java.util.List;

public class JSONDatabase {
    private static final String ERROR = "ERROR";
    private List<String> data;

    public JSONDatabase() {
        data = new ArrayList<>();
        for (int i =0; i < 1000; i++) {
            data.add("");
        }
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
    public String set(int index, String text) {
        try {
            if (index >= 0 && index < data.size() && text != null) {
                data.set(index, text);
                return "OK";
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return ERROR;
        }
    }
    public String get(int index) {
        try {
            if (index >= 0 && index < data.size() && !data.get(index).isEmpty()) {
                return data.get(index);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return ERROR;
        }
    }
    public String delete(int index) {
        try {
            if (index >= 0 && index < data.size()) {
                data.set(index, "");
                return "OK";
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            return ERROR;
        }
    }
}