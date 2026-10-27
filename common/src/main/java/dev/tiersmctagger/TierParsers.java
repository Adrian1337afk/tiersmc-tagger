package dev.tiersmctagger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.tiersmctagger.TierProfile.Ranking;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Turns each site's response into a {@link TierProfile}. Each parser returns empty when the player
 * isn't listed, and throws when the response isn't what the site normally sends.
 */
final class TierParsers {
    private TierParsers() {}

    @FunctionalInterface
    interface Parser {
        Optional<TierProfile> parse(int status, URI finalUri, String body);
    }

    private static void requireOk(int status, URI uri) {
        if (status != 200) throw new IllegalStateException("HTTP " + status + " from " + uri);
    }

    // ---------- PvPTiers / SubTiers: {"name", "region", "points", "overall", "rankings": {mode: {tier, pos, ...}}} ----------

    static Optional<TierProfile> tierList(int status, URI uri, String body) {
        if (status == 404) return Optional.empty();
        requireOk(status, uri);
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (json.has("error")) return Optional.empty();

        Map<String, Ranking> rankings = new LinkedHashMap<>();
        if (json.has("rankings") && json.get("rankings").isJsonObject()) {
            for (Map.Entry<String, JsonElement> e : json.getAsJsonObject("rankings").entrySet()) {
                if (!e.getValue().isJsonObject()) continue;
                JsonObject r = e.getValue().getAsJsonObject();
                int tier = intOr(r, "tier", 0);
                if (tier < 1 || tier > 5) continue;
                // These sites use pos 0 = High, 1 = Low
                int pos = intOr(r, "pos", 1) == 0 ? TierProfile.HIGH : TierProfile.LOW;
                int peakTier = intOr(r, "peak_tier", tier);
                int peakPos = intOr(r, "peak_pos", intOr(r, "pos", 1)) == 0 ? TierProfile.HIGH : TierProfile.LOW;
                rankings.put(e.getKey(), new Ranking(e.getKey(), tier, pos, peakTier, peakPos, boolOr(r, "retired")));
            }
        }
        String summary = "#" + intOr(json, "overall", 0) + " · " + intOr(json, "points", 0) + " pts";
        return Optional.of(new TierProfile(strOr(json, "name", ""), summary, region(strOr(json, "region", "")), rankings));
    }

    // ---------- TierMC: {"name", "region", "points", "position", "rankings": [{gamemode, tier: "HT2", peak_tier, retired}]} ----------

    static Optional<TierProfile> tiermc(int status, URI uri, String body) {
        if (status == 404) return Optional.empty();
        requireOk(status, uri);
        JsonObject json = JsonParser.parseString(body).getAsJsonObject();
        if (json.has("error")) return Optional.empty();

        Map<String, Ranking> rankings = new LinkedHashMap<>();
        if (json.has("rankings") && json.get("rankings").isJsonArray()) {
            for (JsonElement el : json.getAsJsonArray("rankings")) {
                if (!el.isJsonObject()) continue;
                JsonObject r = el.getAsJsonObject();
                int[] code = Ranking.parseCode(strOr(r, "tier", null));
                String mode = strOr(r, "gamemode", null);
                if (code == null || mode == null) continue;
                int[] peak = Ranking.parseCode(strOr(r, "peak_tier", null));
                if (peak == null) peak = code;
                rankings.put(mode, new Ranking(mode, code[0], code[1], peak[0], peak[1], boolOr(r, "retired")));
            }
        }
        int position = intOr(json, "position", 0);
        String summary = (position > 0 ? "#" + position + " · " : "") + intOr(json, "points", 0) + " pts";
        return Optional.of(new TierProfile(strOr(json, "name", ""), summary, region(strOr(json, "region", "")), rankings));
    }

    // ---------- helpers ----------

    private static String region(String region) {
        return region == null || region.isBlank() || region.equals("??") ? "" : region;
    }

    private static int intOr(JsonObject o, String key, int fallback) {
        JsonElement e = o.get(key);
        return e == null || e.isJsonNull() ? fallback : e.getAsInt();
    }

    private static String strOr(JsonObject o, String key, String fallback) {
        JsonElement e = o.get(key);
        return e == null || e.isJsonNull() ? fallback : e.getAsString();
    }

    private static boolean boolOr(JsonObject o, String key) {
        JsonElement e = o.get(key);
        return e != null && !e.isJsonNull() && e.getAsBoolean();
    }
}
