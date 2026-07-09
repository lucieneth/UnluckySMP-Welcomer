# UnluckySMP Welcomer

A tiny server-side [Fabric](https://fabricmc.net/) mod for Minecraft **26.2** that replaces the
default join/leave messages with custom, configurable ones — and, unlike vanilla, shows the join
message to the player who just connected.

No Fabric API dependency; only Fabric Loader is required. Vanilla clients can join a server running
this mod without installing anything.

## Features

- Replaces the vanilla `<name> joined the game` / `<name> left the game` broadcasts.
- The connecting player also sees their own join message (vanilla broadcasts it before they are added
  to the player list, so normally they miss it).
- Fully configurable via a JSON file, with an in-game reload.

## Requirements

| | |
|---|---|
| Minecraft | 26.2 |
| Java | 25+ |
| Fabric Loader | 0.19.3+ |

## Installation

Drop the built jar (`build/libs/unluckysmp-welcomer-1.0.0.jar`) into your server's `mods/` folder
alongside Fabric Loader. The mod runs server-side only.

## Configuration

On first launch the mod writes `config/unluckysmp-welcomer.json`:

```json
{
  "join_message": "&7[&a+&7] &f%name% joined the Unlucky SMP!",
  "leave_message": "&7[&c-&7] &f%name% left the Unlucky SMP :("
}
```

- `&` color codes are supported (`&7` gray, `&a` green, `&c` red, `&f` white, …).
- `%name%` is replaced with the player's display name and inherits the color active at that point.
- If the file contains invalid JSON, the mod logs an error and falls back to defaults **without**
  overwriting your file.

## Commands

| Command | Permission | Description |
|---|---|---|
| `/welcomer` | everyone | Shows mod info, authors, and config location. |
| `/welcomer reload` | operators (level 2+) | Reloads the config file with no restart. |

## Building

Requires JDK 25.

**Windows (easiest):** double-click `build.bat`.

**Any platform, from the project root:**

```sh
./gradlew build      # Linux/macOS
.\gradlew.bat build  # Windows
```

The finished jar lands in `build/libs/` (use the one *without* the `-sources` suffix).

## Authors

Made by Lucien & Claude.

## License

Released under [CC0-1.0](LICENSE).
