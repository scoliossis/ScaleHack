package github.com.scoliossis.Util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;

public class SessionUtil {
    public static void setSession(Session session) {
        Field sessionField = ReflectionHelper.findField(Minecraft.class, "session", "field_71449_j");
        ReflectionHelper.setPrivateValue(Field.class, sessionField, sessionField.getModifiers() & ~Modifier.FINAL, "modifiers");
        ReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), session, "session", "field_71449_j");
    }


    public static String[] getProfileInfo(String token) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet("https://api.minecraftservices.com/minecraft/profile");
            request.setHeader("Authorization", "Bearer " + token);
            CloseableHttpResponse response = client.execute(request);
            String jsonString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
            String IGN = jsonObject.get("name").getAsString();
            String UUID = jsonObject.get("id").getAsString();
            return new String[]{IGN, UUID};
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Boolean validateSession(String token) {
        try {
            String[] profileInfo = getProfileInfo(token);
            String IGN = profileInfo[0];
            String UUID = profileInfo[1];
            return IGN != null && UUID != null;
        } catch (Exception e) {
            return false;
        }
    }
    public static int changeName(String newName,String token) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPut request = new HttpPut("https://api.minecraftservices.com/minecraft/profile/name/" + newName);
            request.setHeader("Authorization", "Bearer " + token);
            CloseableHttpResponse response = client.execute(request);
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static int changeSkin(String url,String token) {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost request = new HttpPost("https://api.minecraftservices.com/minecraft/profile/skins");
            request.setHeader("Authorization", "Bearer " + token);
            request.setHeader("Content-Type", "application/json");
            String jsonString = String.format("{ \"variant\": \"slim\", \"url\": \"%s\"}", url);
            request.setEntity(new StringEntity(jsonString));
            CloseableHttpResponse response = client.execute(request);
            return response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static void sessionLogin(String session) {
        if (validateSession(session)) {
            String[] playerInfo = getProfileInfo(session);
            setSession(new Session(playerInfo[0], playerInfo[1], session, "mojang"));
            System.out.println("LOGGED INTO ACCOUNT: " + playerInfo[0]);
        } else {
            System.out.println("invalid token");
        }
    }
}
