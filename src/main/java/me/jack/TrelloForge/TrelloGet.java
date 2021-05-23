package me.jack.TrelloForge;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class TrelloGet extends CommandBase {

    @Override
    public String getName() {
        return "trelloget";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "Get all cards from a trello board";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // Test if there are any arguments
        if(args.length < 1) return;

        // There are arguments so continue and get all cards
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get("https://api.trello.com/1/lists/601c07b9e89e52760c533aef/cards")
                        .queryString("key", "5b31318de528bfca09167727712be8cc")
                        .queryString("token", "c3b55b026dceadc1a925197d6a13f6b4bd0c053c6da29bfce5e0753b4108cc4a")
                        .asJson();
        } catch (UnirestException e) {
            sender.sendMessage(new TextComponentString(response.toString()));
            e.printStackTrace();
        }
    }
}
