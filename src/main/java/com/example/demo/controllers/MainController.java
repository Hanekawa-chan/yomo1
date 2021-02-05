package com.example.demo.controllers;

import com.example.demo.dao.DAO;
import com.example.demo.models.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class MainController {

    Session session = new Session();
    public final DAO dao;

    @Autowired
    public MainController(DAO dao) {
        this.dao = dao;
    }

    @GetMapping("/main")
    public String main(Model model){
        model.addAttribute("logout", session.logout);
        return "main";
    }

    @GetMapping("/team")
    public String team(Model model){
        model.addAttribute("logout", session.logout);
        return "team";
    }

    @GetMapping("/stats")
    public String stats(Model model){
        model.addAttribute("logout", session.logout);

        HashMap sortedHashMap = null;
        List list = new LinkedList(dao.main.getInfo().entrySet());
//Custom Comparator
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        Map<Integer, String> map = sortedHashMap;

        List<String> name = new ArrayList<>();
        List<Double> rates = new ArrayList<>();

        Set set2 = map.entrySet();
        Iterator iterator2 = set2.iterator();
        while(iterator2.hasNext())
        {
            Map.Entry me2 = (Map.Entry)iterator2.next();
            name.add((String) me2.getKey());
            Double d = (Double) me2.getValue();
            String r = d.toString().substring(0, 4);
            d = Double.parseDouble(r);
            rates.add(d);
        }

        String[] names = new String[name.size()];
        for(int i = 0; i < name.size(); i++) {
            if(name.get(i).equals("tygagamer")) {
                names[i] = "TYGA";
            }
            else if(name.get(i).equals("sorryihavenoname")) {
                names[i] = "MEON";
            }
            else if(name.get(i).equals("ALExANDROss")) {
                names[i] = "iLYAN";
            }
            else if(name.get(i).equals("76561198138688894")) {
                names[i] = "Salik";
            }
            else {
                names[i] = "Archi";
            }
        }
        Collections.reverse(rates);
        for(int i = 0; i < names.length/2; i++){
            String temp = names[i];
            names[i] = names[names.length - i - 1];
            names[names.length - i - 1] = temp;
        }

        int size = dao.main.getInfo().size();

        model.addAttribute("size", size);
        model.addAttribute("rates", rates);
        model.addAttribute("names", names);

        return "stats";
    }

    @GetMapping("/citats")
    public String citats(Model model){
        model.addAttribute("logout", session.logout);
        return "citats";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("logout", session.logout);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(value="login", required = true) String login,
                        @RequestParam(value="pass", required = true) String pass,
                        @RequestParam(value="auth", required = true) String auth){
        session.setName(login);
        session.setPass(pass);
        session.setAuth(auth);
        session.login(dao);
        return "redirect:/main";
    }

    @GetMapping("/logout")
    public String logout() {
        session.logout();
        return "redirect:/main";
    }

    @GetMapping("/refresh")
    public String refresh(){
        if (session.logout) {
            session.refresh(dao);
            return "redirect:/stats";
        }
        else {
            return "redirect:/main";
        }
    }
}