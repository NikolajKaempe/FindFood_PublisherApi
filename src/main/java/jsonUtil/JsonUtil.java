package jsonUtil;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public class JsonUtil {
    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static <T extends Object> T  fromJson(String json, Class<T> classe) {
        return new Gson().fromJson(json, classe);
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }
}