package me.jack.trello.trello;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class TrelloGet implements CommandExecutor {
    private void grabber() throws UnirestException {
        // Grab values from the config
        String boardID = Trello.getPlugin(Trello.class).getConfig().getString("boardID");
        String key = Trello.getPlugin(Trello.class).getConfig().getString("key");
        String token = Trello.getPlugin(Trello.class).getConfig().getString("token");

        // Grab all lists
        JSONArray lists = Unirest.get("https://api.trello.com/1/boards/" + boardID + "/lists")
                .queryString("key", key)
                .queryString("token", token)
                .asJson().getBody().getArray();

        // Convert first list (To Do) to a JSONObject
        JSONObject listObj = lists.getJSONObject(0);

        // Grab all cards
        JSONArray cards = Unirest.get("https://api.trello.com/1/lists/" + listObj.get("id") + "/cards")
                .queryString("key", key)
                .queryString("token", token)
                .asJson().getBody().getArray();

        // Create arrayList to add each 2D string array to so they can be sorted
        ArrayList<String[][]> stringList = new ArrayList<String[][]>();

        // Iterate over all the cards in the first list
        for (var card : cards) {
            // Create 2D array object so strings can be sorted
            String[][] temp = new String[0][2];

            // Setup the objects for manipulation
            JSONObject cardObj = (JSONObject) card;
            JSONArray labels = (JSONArray) cardObj.get("labels");

            // Grab the name and color attributes
            String name = cardObj.get("name").toString();
            String color = labels.getJSONObject(0).get("color").toString();

            // Perform switch case to determine priority
            switch (color) {
                case "red":
                    // 1st priority
                    temp = new String[][]{
                            {"Task: " + name + ". Priority: "},
                            {"1"}
                    };
                    break;
                case "blue":
                    // 2nd priority
                    temp = new String[][]{
                            {"Task: " + name + ". Priority: "},
                            {"2"}
                    };
                    break;
                case "green":
                    // 3rd priority
                    temp = new String[][]{
                            {"Task: " + name + ". Priority: "},
                            {"3"}
                    };
                    break;
                case "yellow":
                    // 4th priority
                    temp = new String[][]{
                            {"Task: " + name + ". Priority: "},
                            {"4"}
                    };
                    break;
                default:
                    // This will never run, but it is needed for the code to work
                    break;
            }
            // Add new temp item to stringList so it can be sorted
            stringList.add(temp);
        }

        // Sort stringList based on the second index. The 'o' variable is the value that is sorted on
        stringList.sort(Comparator.comparing(o -> Arrays.toString(o[1])));

        // Combine and print all strings
        for (var item : stringList) {
            // Create temporary string
            String message = "";
            for (var value : item) {
                // Add current string to the message
                message = message + value[0];
            }
            // Broadcast the message to everyone
            Bukkit.broadcastMessage(message);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Create a new object and run grabber()
        TrelloGet obj = new TrelloGet();
        try {
            obj.grabber();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return true;
    }
}
