![The One Probe (TOP) Logo](https://media.forgecdn.net/avatars/41/170/635991514554675962.png)
# The One Probe (TOP) - Minecraft Mod
_More immersive alternative for WAILA_

## Fabric 26.2

This branch targets Minecraft 26.2, Fabric Loader 0.19.3 or newer, and Java 25.
Install the mod on both the client and server alongside **Fabric API** and
**Forge Config API Port 26.2.1 or newer**. Team Reborn Energy API is bundled.
The existing `theoneprobe-common.toml` and `theoneprobe-client.toml` configuration
format is preserved. Open the settings by using a probe or running `/top config`;
`/top need` opens the introduction note.

Build with a Java 25 JDK:

```sh
./gradlew build
./gradlew runClient
./gradlew runServer
```

The playable jar and the API jar are written to `build/libs/`.
Run `./gradlew runClientGameTest` to exercise client/server packet exchange,
chest inspection, fluid component serialization, the welcome note, and command
screens in a temporary singleplayer world. This requires a graphical display.
The test mod is kept in `src/gametest` and is excluded from the release jar.

### Fabric addons

Implement `mcjty.theoneprobe.api.TheOneProbePlugin` and declare its class in your
addon's `fabric.mod.json`:

```json
"entrypoints": {
  "theoneprobe": ["your.package.TopPlugin"]
}
```

```java
public final class TopPlugin implements TheOneProbePlugin {
    @Override
    public void register(ITheOneProbe probe) {
        probe.registerProvider(new YourProbeInfoProvider());
    }
}
```

This callback runs after built-in providers are registered. It replaces NeoForge
IMC `getTheOneProbe` messages; addons must be compiled for Fabric.
The fluid API now uses `mcjty.theoneprobe.api.FluidStack`, which holds a Fabric
`FluidVariant` and an amount in millibuckets. `tank` and `tankHandler` accept
Fabric `Storage<FluidVariant>` and convert Fabric's droplets to millibuckets
(81 droplets per mB). Inventories use Fabric Transfer API; energy uses
[Team Reborn Energy API](https://github.com/TechReborn/Energy).
Legacy Baubles, Tesla, and RedstoneFlux integrations are inactive on this branch.
NeoForge's saved player attachment data is not migrated when switching loaders.

## Introduction to The One Probe (TOP)

The One Probe (or TOP in short) is a more immersive version of WAILA. You don't get to see the information tooltip all the time but only when you have the probe in your hand (note that this mod can be configured to show the information all the time just like WAILA).

The purpose of this mod is to show on-screen information about the block you are looking at whenever you hold the probe in your hand (or off-hand). The mod itself will show basic information like the name of the block, the mod for the block and also the tool to use for harvesting the block. In addition this mod will also show the amount of RF energy that is stored in the block (if the block supports RF) and if you are sneaking it will also give a list of all items that are in the block if it is an inventory (like a chest).

This mod is very configurable so you can disable all the features mentioned above if they do not fit your playing style or modpack.

This mod also has a flexible API that other mods can use to add more information. Deep Resonance will use this mod to show information about the crystals and liquids. RFTools will also have support for this mod. The API can be found [here](https://github.com/McJty/TheOneProbe/tree/master/src/main/java/mcjty/theoneprobe/api).

***

## Maven

    repositories {
        maven { // TOP
            url "https://maven.k-4u.nl"
        }

    dependencies {
        implementation "mcjty.theoneprobe:theoneprobe:${top_version}"
    }

## Licence

#### MIT

This mod is licenced under the MIT licence. To see the full terms of the licence click [here](https://github.com/McJty/TheOneProbe/blob/1.20/LICENCE).

#### Modpack Permission

You're free to use the mod in your modpack.

***

## Credits

- [McJty](https://twitter.com/McJty) - Project Owner

**Copyright © 2023 McJty**
