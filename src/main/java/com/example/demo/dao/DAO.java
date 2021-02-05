package com.example.demo.dao;

import com.example.demo.models.Stat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DAO {

    public static final List<String> TEAM = Arrays.asList(new String[] { "sorryihavenoname", "tygagamer", "ALExANDROss", "76561198138688894", "76561198160302549"});
    private List<Stat> list = new ArrayList<>();
    public Stat main = new Stat();

    public void add(Stat stat) {
        list.add(stat);
        for(String id : stat.getInfo().keySet()) {
            if (main.notEmpty(id)) {
                main.merge(id, stat.getInfo().get(id));
            }
            else {
                main.add(id, stat.getInfo().get(id));
            }
        }
    }

    public boolean id(String id) {
        return TEAM.contains(id);
    }

    public boolean has(String time) {
        for(Stat stata : list) {
            if(stata.compare(time)) {
                return true;
            }
        }
        return false;
    }
}