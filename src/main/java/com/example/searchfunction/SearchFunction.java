package com.example.searchfunction;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

public class SearchFunction {

    static Scanner sc;
    static JSONArray jsonArrOrganizations;
    static JSONArray jsonArrTickets;
    static JSONArray jsonArrUsers;

    public static void printOptions() {
        System.out.println("Choose search type then Enter: ");
        System.out.println(" 1. Organizations");
        System.out.println(" 2. Tickets");
        System.out.println(" 3. Users");
        System.out.println(" 0. Exit");
    }

    public static void searchOrganizations() {
        sc.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
        System.out.print("Search Organizations. Enter query : ");
        String query = sc.nextLine();
        String[] tokens = query.trim().split("[\\s:=]+");
        // tickets' subject
        // users' name
        // example : search Organizations by name
        if (true) {
            for (Object objorg : jsonArrOrganizations) {
                JSONObject org = (JSONObject) objorg;
                String propVal = org.get(tokens[0]).toString().toLowerCase();
                if (propVal.contains(tokens[1].toLowerCase()) ||
                        tokens[1].toLowerCase().contains(propVal)) {
                    System.out.println(org.get("name"));

                    System.out.println("All tickets' subjects:");
                    for (Object objticket : jsonArrTickets) {
                        JSONObject ticket = (JSONObject) objticket;
                        if (org.get("_id").equals(ticket.get("organization_id"))) {
                            System.out.print("\t");
                            System.out.println(ticket.get("_id") + " " +
                                    ticket.get("subject").toString());
                        }
                    }

                    System.out.println("All users' names:");
                    for (Object objUser : jsonArrUsers) {
                        JSONObject user = (JSONObject) objUser;
                        if (org.get("_id").equals(user.get("organization_id"))) {
                            System.out.print("\t");
                            System.out.println(user.get("_id") + " " +
                                    user.get("name").toString());
                        }
                    }
                }
            }
            System.out.println();
        }
    }

    public static void searchTickets() {

    }

    public static void searchUsers() {

    }


    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        try {
            String cwd = System.getProperty("user.dir");
            Object objOrganizations = parser.parse(new FileReader(cwd + "/raw/organizations.json"));
            Object objTickets = parser.parse(new FileReader(cwd + "/raw/tickets.json"));
            Object objUsers = parser.parse(new FileReader(cwd + "/raw/users.json"));

            jsonArrOrganizations = (JSONArray) objOrganizations;
            jsonArrTickets = (JSONArray) objTickets;
            jsonArrUsers = (JSONArray) objUsers;

            sc = new Scanner(System.in);
            boolean exit = false;

            while (true) {
                printOptions();
                int opt = sc.nextInt();
                switch (opt) {
                    case 0:
                        exit = true;
                        break;
                    case 1:
                        searchOrganizations();
                        break;
                    case 2:
                        searchTickets();
                        break;
                    case 3:
                        searchUsers();
                        break;
                }
                if (exit) break;
            }

            // An iterator over a collection. Iterator takes the place of Enumeration in the Java Collections Framework.
            // Iterators differ from enumerations in two ways:
            // 1. Iterators allow the caller to remove elements from the underlying collection during the iteration with well-defined semantics.
            // 2. Method names have been improved.
//            Iterator<JSONObject> iterator = companyList.iterator();
//            while (iterator.hasNext()) {
//                System.out.println(iterator.next());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
