package client;

import java.util.ArrayList;
import java.util.List;

public class JSONDatabase {
    private List<String> data;

    public JSONDatabase() {
        data = new ArrayList<>();
        for (int i =0; i < 100; i++) {
            data.add("");
        }
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
    public void set(int index, String text) {
        try {
            if (index >= 0 && index < data.size()) {
                data.set(index, text);
                System.out.println("OK");
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR");
        }
    }
    public void get(int index) {
        try {
            if (index >= 0 && index < data.size() && !data.get(index).isEmpty()) {
                System.out.println(data.get(index));
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR");
        }
    }
    public void delete(int index) {
        try {
            if (index >= 0 && index < data.size()) {
                data.set(index, "");
                System.out.println("OK");
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR");
        }
    }
}
