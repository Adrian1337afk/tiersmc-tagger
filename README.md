<img src="common/src/main/resources/assets/tiersmctagger/icon.png" width="96" align="right" alt="TiersMC Tagger logo">

# TiersMC Tagger

**TierMC and SubTiers ranks, right next to a player's name.**

> **A fork of [QTiers](https://github.com/Adrian1337afk/qtiers), made for the [TierMC](https://tiermc.com) tier list.**
> The TierMC team is welcome to use it, change it and publish it as their own tier list mod, under the GPL-3.0 license.

TiersMC Tagger is a Fabric client mod for **Minecraft 1.21.4, 1.21.8, 1.21.11, 26.1.x, 26.2 and 26.3**. It shows players' [TierMC](https://tiermc.com) and [SubTiers](https://subtiers.net) ranks in nametags and in the tab list, each with that site's own gamemode icons. It runs only on your client.

## Features
- **Tiers next to names**: put TierMC and SubTiers on the left or right of the name, or turn either off. Pick a gamemode per site, or show the player's highest tier.
- **Saved between sessions**: tiers show the moment you join and refresh in the background.
- **Your own name in F5**, with your tiers.
- **Opponent HUD**: the tiers of the last player you hit; press **Move** in the settings to drag it anywhere.
- **Player profiles**: `/tiersmc <player>` shows a skin render and every tier, peak, point total, rank and region.
- **Online players**, **leaderboards** and **compare** screens.
- **Clickable chat names** and a **player search** box.
- Works on servers with custom nametags and tab lists, like mcpvp.club.

## TierMC Discord servers
TierMC runs one Discord server per gamemode. Join one to get tested:

| Gamemode | Discord |
|---|---|
| Vanilla | https://discord.gg/VswgH4CT5c |
| Sword | https://discord.gg/6h77S8JmCV |
| Mace | https://discord.gg/XWD5VPwmPe |
| UHC | https://discord.gg/vD9DmsnrM8 |
| Potion | https://discord.gg/87VuPEDxu7 |
| Nethpot | https://discord.gg/kpBB29ZAnG |
| Axe | https://discord.gg/VhsdMPTT5y |
| SMP | https://discord.gg/rYvJZUbX5R |

## Commands
| Command | What it does |
|---|---|
| `/tiersmc <player>` | Opens that player's profile |
| `/tiersmc list` | Shows everyone online with their tiers |
| `/tiersmc top` | Opens the leaderboards |
| `/tiersmc compare <a> <b>` | Compares two players |
| `/tiersmc` | Opens the settings |
| `/tiersmc reload` | Reloads the config and fetches tiers again |

## Install
Choose the jar matching your Minecraft version and put it and the matching [Fabric API](https://modrinth.com/mod/fabric-api) in `.minecraft/mods`. [Mod Menu](https://modrinth.com/mod/modmenu) is optional. Install only one TiersMC Tagger jar.

| Minecraft | Jar suffix | Java |
|---|---|---|
| 1.21.4 | `+1.21.4.jar` | 21+ |
| 1.21.8 | `+1.21.8.jar` | 21+ |
| 1.21.11 | `+1.21.11.jar` | 21+ |
| 26.1.x | `+26.1.jar` | 25+ |
| 26.2 | `+26.2.jar` | 25+ |
| 26.3 | `+26.3.jar` | 25+ |

The 26.1 build targets 26.1.2 and declares compatibility with 26.1.x. Each version has separate Minecraft rendering code; all share the same two tier lists, settings and cache format. Compilation does not replace in-game testing on each patch version.

## Build
```
gradlew collectJars
```
All six jars end up in `build/libs/`. Building requires JDK 25 or newer.

## Credits
- **Tier data:** the [TierMC](https://tiermc.com/api.html) and [SubTiers](https://subtiers.net) APIs.
- **Gamemode icons:** each site's own website. They belong to those sites.
- **Skin renders:** [Visage](https://visage.surgeplay.com), with [mc-heads](https://mc-heads.net) as the fallback.
- Forked from [QTiers](https://github.com/Adrian1337afk/qtiers) by qdran.

## License
GNU GPL v3.0 (see [LICENSE](LICENSE)). The gamemode icons belong to their tier-list websites and are not covered by this license.
