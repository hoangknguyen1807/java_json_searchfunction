package com.example.searchfunction;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Scanner;

public class SearchFunction {

    static Scanner sc;
    static JSONArray jsonArrOrganizations;
    static JSONArray jsonArrTickets;
    static JSONArray jsonArrUsers;

    public static void printOptions() {
        System.out.println("1. Organizations");
        System.out.println("2. Tickets");
        System.out.println("3. Users");
        System.out.println("0. Exit");
        System.out.print("Choose search type then Enter: ");
    }


    public static String[] getQuery() {
        sc.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
        String query = sc.nextLine();
        System.out.println("\"" + query + "\"");
        String[] tokens = query.trim().split("[\\s:=]+");
        String[] ret = new String[2];
        ret[0] = tokens[0];
        ret[1] = "";
        for (int i = 1; i < tokens.length; i++) {
            ret[1] += tokens[i];
            if (i != tokens.length - 1) {
                ret[1] += " ";
            }
        }
        return ret;
    }

    public static void searchOrganizations() {
        System.out.print("Search Organizations. Query = ");
        // qr[0] : key , qr[1] : value to search
        String[] qr = getQuery();
        // Show tickets' subjects
        // and users' names
        System.out.println("Result:");
        for (Object objorg : jsonArrOrganizations) {
            JSONObject org = (JSONObject) objorg;
            String propVal;
            try {
                propVal = org.get(qr[0]).toString().toLowerCase();
            } catch (Exception e) {
                System.out.println("Exception caught! The queried property does not exist in the type of search you chose.");
                return;
            }
            if (propVal.contains(qr[1].toLowerCase()) ||
                    qr[1].toLowerCase().contains(propVal)) {
                System.out.print("OrganizationID:" + org.get("_id"));
                System.out.println("\tName:" + org.get("name"));

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
                System.out.println();
            }
        }
    }

    public static void searchTickets() {
        System.out.print("Search Tickets. Query = ");
        // qr[0] : key , qr[1] : value to search
        String[] qr = getQuery();
        // submitted name
        // assignee name
        // name of organization
        System.out.println("Result:");
        for (Object objticket : jsonArrTickets) {
            JSONObject ticket = (JSONObject) objticket;
            String propVal;
            try {
                propVal = ticket.get(qr[0]).toString().toLowerCase();
            } catch (Exception e) {
                System.out.println("Exception caught! The queried property does not exist in the type of search you chose.");
                return;
            }
            if (propVal.contains(qr[1].toLowerCase()) ||
                    qr[1].toLowerCase().contains(propVal)) {
                System.out.println("TicketID:" + ticket.get("_id"));
                System.out.println("\tSubject: " + ticket.get("subject"));

                Object submitterID = ticket.get("submitter_id");
                Object assigneeID = ticket.get("assignee_id");
                try {
                    for (Object objuser : jsonArrUsers) {
                        JSONObject user = (JSONObject) objuser;
                        if (submitterID.equals(user.get("_id"))) {
                            System.out.print("\tSubmitter name: ");
                            System.out.println(user.get("name").toString());
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("\tSubmitter not found!");
                }
                try {
                    for (Object objuser : jsonArrUsers) {
                        JSONObject user = (JSONObject) objuser;
                        if (assigneeID.equals(user.get("_id"))) {
                            System.out.print("\tAssignee name: ");
                            System.out.println(user.get("name").toString());
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("\tAssignee not found!");
                }

                try {
                    Object orgID = ticket.get("organization_id");
                    for (Object objOrg : jsonArrOrganizations) {
                        JSONObject org = (JSONObject) objOrg;

                        if (orgID.equals(org.get("_id"))) {
                            System.out.print("\tOrganization name: ");
                            System.out.println(org.get("name").toString());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("\tTicket does not belong to any organizations");
                }
                System.out.println();
            }
        }
    }

    public static void searchUsers() {
        System.out.print("Search Users. Query = ");
        // qr[0] : key , qr[1] : value to search
        String[] qr = getQuery();
        // submitted tickets' subjects
        // assigned tickets' subjects
        // name of organization
        System.out.println("Result:");
        for (Object objuser : jsonArrUsers) {
            JSONObject user = (JSONObject) objuser;
            String propVal;
            try {
                propVal = user.get(qr[0]).toString().toLowerCase();
            } catch (Exception e) {
                System.out.println("Exception caught! The queried property does not exist in the type of search you chose.");
                return;
            }
            if (propVal.contains(qr[1].toLowerCase()) ||
                    qr[1].toLowerCase().contains(propVal)) {
                System.out.print("UserID:" + user.get("_id"));
                System.out.println("Name:" + user.get("name"));

                Object userID = user.get("_id");
                System.out.println(" Submitted tickets' subjects:");
                for (Object objticket : jsonArrTickets) {
                    JSONObject ticket = (JSONObject) objticket;
                    if (userID.equals(ticket.get("submitter_id"))) {
                        System.out.print("\t");
                        System.out.println(ticket.get("_id") + " " + ticket.get("subject").toString());
                    }
                }
                System.out.println(" Assigned ticket's subjects:");
                for (Object objticket : jsonArrTickets) {
                    JSONObject ticket = (JSONObject) objticket;
                    if (userID.equals(ticket.get("assignee_id"))) {
                        System.out.print("\t");
                        System.out.println(ticket.get("_id") + " " + ticket.get("subject").toString());
                    }
                }
                try {
                    Object orgID = user.get("organization_id");
                    for (Object objOrg : jsonArrOrganizations) {
                        JSONObject org = (JSONObject) objOrg;
                        if (orgID.equals(org.get("_id"))) {
                            System.out.print(" Organization: ");
                            System.out.println(orgID + " " + org.get("name"));
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("\tUser is not associated with any organizations");
                }
                System.out.println();
            }
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        PrintStream outStream = new PrintStream("README.md");
        System.setOut(outStream);

        JSONParser parser = new JSONParser();
        try {
            String cwd = System.getProperty("user.dir");
            Object objOrganizations = parser.parse(new FileReader(cwd + "/raw/organizations.json"));
            Object objTickets = parser.parse(new FileReader(cwd + "/raw/tickets.json"));
            Object objUsers = parser.parse(new FileReader(cwd + "/raw/users.json"));

            jsonArrOrganizations = (JSONArray) objOrganizations;
            jsonArrTickets = (JSONArray) objTickets;
            jsonArrUsers = (JSONArray) objUsers;

            FileInputStream fis = new FileInputStream(cwd + "/inp.txt");
            sc = new Scanner(fis);
            boolean exit = false;

            while (true) {
                //printOptions();
                String line;
                if (sc.hasNextLine()) {
                    line = sc.nextLine();
                } else {
                    break;
                }

                int opt = Integer.parseInt(line);
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

            sc.close();
            fis.close();

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
