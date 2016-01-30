package com.github.liaoheng.album.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 相册
 * @author liaoheng
 * @version 2015-04-18 01:42
 */
public class Album implements Serializable {
    public Album() {
    }

    public Album(String name, String url) {
        this.name = name;
        this.url = url;
    }

    private String      name;
    private String      url;
    private List<Album> items;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Album> getItems() {
        return items;
    }

    public void setItem(String name, String url) {
        if (items == null)
            items = new ArrayList<>();
        items.add(new Album(name, url));
    }

    public void setItems(List<Album> items) {
        this.items = items;
    }
}
