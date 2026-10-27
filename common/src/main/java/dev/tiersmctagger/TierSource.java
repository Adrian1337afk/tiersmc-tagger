package dev.tiersmctagger;

import java.util.UUID;
import java.util.function.Function;

/** The tier-list websites, where to look players up, and how to read each site's answer. */
public enum TierSource {
    TIERMC("TierMC", "TMC", 0x5EC8E5,
            uuid -> "https://tiermc.com/api/v2/profile/" + uuid,
            name -> "https://tiermc.com/api/v2/profile/by-name/" + name,
            TierParsers::tiermc),
    SUBTIERS("SubTiers", "SUB", 0x7ED957,
            uuid -> "https://subtiers.net/api/v2/profile/" + undashed(uuid),
            name -> "https://subtiers.net/api/v2/profile/by-name/" + name,
            TierParsers::tierList);

    public final String displayName;
    public final String shortName;
    public final int color;
    private final Function<UUID, String> uuidUrl;
    private final Function<String, String> nameUrl;
    final TierParsers.Parser parser;

    TierSource(String displayName, String shortName, int color,
               Function<UUID, String> uuidUrl, Function<String, String> nameUrl, TierParsers.Parser parser) {
        this.displayName = displayName;
        this.shortName = shortName;
        this.color = color;
        this.uuidUrl = uuidUrl;
        this.nameUrl = nameUrl;
        this.parser = parser;
    }

    public String urlFor(UUID uuid) {
        return uuidUrl.apply(uuid);
    }

    /** {@code playerName} must already be URL-encoded. */
    public String urlFor(String playerName) {
        return nameUrl.apply(playerName);
    }

    private static String undashed(UUID uuid) {
        return uuid.toString().replace("-", "");
    }
}
