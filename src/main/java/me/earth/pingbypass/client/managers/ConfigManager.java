package me.earth.pingbypass.client.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.setting.Setting;
import me.earth.pingbypass.client.modules.servermodule.ServerModule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager
{
    private static final JsonParser PARSER = new JsonParser();

    public void load()
    {
        File file = new File("Server.json");
        if (Files.exists(Paths.get(file.getPath())))
        {
            try
            {
                InputStream stream = new FileInputStream(file);
                JsonObject object = PARSER.parse(new InputStreamReader(stream)).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : object.entrySet())
                {
                    Setting<?> setting = ServerModule.getInstance().getSetting(entry.getKey());
                    if (setting != null)
                    {
                        setting.fromJson(entry.getValue());
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                Files.createFile(Paths.get(file.getPath()));
                save();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void save() throws IOException
    {
        Path file = Paths.get("Server.json");
        if (!Files.exists(file))
        {
            Files.createFile(file);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(writeSettings(ServerModule.getInstance()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(file)));
        writer.write(json);
        writer.close();
    }

    private JsonObject writeSettings(Module module)
    {
        JsonObject object = new JsonObject();
        for (Map.Entry<Setting<?>, JsonElement> entry : mapElements(module).entrySet())
        {
            object.add(entry.getKey().getName(), entry.getValue());
        }

        return object;
    }

    private Map<Setting<?>, JsonElement> mapElements(Module module)
    {
        Map<Setting<?>, JsonElement> elements = new HashMap<>();
        for (Setting<?> setting : module.getSettings())
        {
            elements.put(setting, PARSER.parse(setting.getValue().toString()));
        }

        return elements;
    }

}
