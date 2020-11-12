package me.jack.trello.trello;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrelloDelete implements CommandExecutor {
    private void deleter(CommandSender sender, String[] args) throws UnirestException {
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

        // Test if the command is in the right format
        if (args.length == 1) {
            // There is only one argument which is just a word
            String message = args[0];

            // Variable to grab correct JSONObject
            JSONObject correctObject = null;

            // Search through all the cards to check if the card exists
            for(int i = 0; i < cards.length(); i++) {
                // Grab the JSONObject
                JSONObject card = cards.getJSONObject(i);

                // Create regex pattern and matcher
                Pattern pattern = Pattern.compile("\\b" + message + "\\b");
                Matcher matcher = pattern.matcher(card.get("name").toString());

                // Test if message is fully within name
                if (matcher.find()) {
                    // Task found
                    correctObject = cards.getJSONObject(i);
                    break;
                }
            }

            // Check if the correct object has been found
            if (correctObject != null) {
                // Task exists
                sender.sendMessage("Task exists, deleting " + correctObject.get("name").toString());

                // Grab card id
                String cardID = correctObject.get("id").toString();

                // Delete card
                HttpResponse<String> cardDelete = Unirest.delete("https://api.trello.com/1/cards/" + cardID)
                        .queryString("key", key)
                        .queryString("token", token)
                        .asString();
            } else {
                // Task doesn't exist
                sender.sendMessage("Task doesn't exist, try again");
            }
        } else {
            // More than one argument
            sender.sendMessage("Invalid format, try again");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Create a new object and run poster()
        TrelloDelete obj = new TrelloDelete();
        try {
            obj.deleter(sender, args);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return true;
    }
}
