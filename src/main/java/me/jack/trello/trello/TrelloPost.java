package me.jack.trello.trello;

import com.mashape.unirest.http.JsonNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class TrelloPost implements CommandExecutor {
    // Function to determine if a string is a number
    public static boolean isNumeric(String str) {
        return str.matches("[0-9]");
    }

    private void poster(CommandSender sender, String[] args) throws UnirestException {
        // Grab values from the config
        String boardID = Trello.getPlugin(Trello.class).getConfig().getString("boardID");
        String key = Trello.getPlugin(Trello.class).getConfig().getString("key");
        String token = Trello.getPlugin(Trello.class).getConfig().getString("token");

        // Test if the last index is a number
        if (isNumeric(args[args.length-1])) {
            // Turn all of the arguments except from the last index into one string
            String message = "";
            for (int i = 0; i < args.length - 1; i++) {
                if (i == 0) {
                    // Loop is on the first index, so skip adding a space
                    message = args[i];
                } else {
                    // Loop is not on the first index, so add a space
                    message = message + " " + args[i];
                }
            }

            // Convert the priority string to an integer
            int priority = Integer.parseInt(args[args.length - 1]);

            // Create label variables
            String color;

            // Test if the priority is valid
            Hashtable<String, String> colorLabel;
            String jsonString;
            if (priority > 0 && priority < 5) {
                // Priority is valid
                switch (priority) {
                    case 1:
                        // Priority is red
                        color = "red";
                        break;
                    case 2:
                        // Priority is green
                        color = "blue";
                    case 3:
                        // Priority is yellow
                        color = "green";
                    case 4:
                        // Priority is not important
                        color = "yellow";
                        break;
                    default:
                        // This will never run, but it is needed for the hashtable to work
                        color = "";
                        break;
                }
            } else {
                // Priority is not valid
                sender.sendMessage("Invalid priority, try again");
                return;
            }

            // Grab all lists
            JSONArray lists = Unirest.get("https://api.trello.com/1/boards/" + boardID + "/lists")
                    .queryString("key", key)
                    .queryString("token", token)
                    .asJson().getBody().getArray();

            // Convert first list (To Do) to a JSONObject
            JSONObject listObj = lists.getJSONObject(0);

            // Create card with task
            JsonNode newCard = Unirest.post("https://api.trello.com/1/cards")
                    .queryString("key", key)
                    .queryString("token", token)
                    .queryString("idList", listObj.get("id"))
                    .queryString("name", message)
                    .asJson().getBody();

            // Grab the card id
            String id = newCard.getObject().get("id").toString();

            // Change the color
            HttpResponse<String> changeLabel = Unirest.post("https://api.trello.com/1/cards/" + id + "/labels")
                    .queryString("key", key)
                    .queryString("token", token)
                    .queryString("color", color)
                    .asString();
        } else {
            // Last index is not a number
            sender.sendMessage("Invalid format, try again");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Create a new object and run poster()
        TrelloPost obj = new TrelloPost();
        try {
            obj.poster(sender, args);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return true;
    }
}
