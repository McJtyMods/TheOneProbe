![The One Probe (TOP) Logo](https://media.forgecdn.net/avatars/41/170/635991514554675962.png)
# The One Probe (TOP) - Minecraft Mod
_More immersive alternative for WAILA_

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

## Hiding block overlays

Register a side-safe predicate with
`probe.registerBlockBlacklist((player, level, pos, state) -> ...)` to hide a
block's entire overlay when it returns `true`, including cached information,
break progress and the tooltip frame. The position and current state are supplied
for the targeted block in the world. For example:

```java
probe.registerBlockBlacklist((player, level, pos, state) ->
        state.is(YourModBlocks.CONTROL_PANEL.get()) && !player.isCrouching());
```

Register during the common `getTheOneProbe` IMC callback on both physical sides.
Predicates are additive, run on the client before cache lookup or network requests
and on the server before information providers, and must not modify game state
or reference client-only classes. Registrations last for the API instance's
lifetime. The interface method has a no-op default for other API implementations.

## Hiding entity overlays

In the common `getTheOneProbe` IMC callback, register a side-safe predicate with
`probe.registerEntityBlacklist((player, entity) -> ...)`. Return `true` to hide an
entity's entire overlay, including cached information and the tooltip frame.
For example, a vehicle mod can hide its control-panel entities and the vehicle
currently ridden by the viewing player.

Predicates are additive and coexist with the configured entity blacklist. They
run on the client before cache lookup or network requests, and on the server
before information providers. Register on both physical sides and avoid client-only
classes or changes to game state in predicates. Registrations last for the API
instance's lifetime. Existing providers and display overrides keep working; the
new interface method has a no-op default for other API implementations.

## Licence

#### MIT

This mod is licenced under the MIT licence. To see the full terms of the licence click [here](https://github.com/McJty/TheOneProbe/blob/1.20/LICENCE).

#### Modpack Permission

You're free to use the mod in your modpack.

***

## Credits

- [McJty](https://twitter.com/McJty) - Project Owner

**Copyright © 2023 McJty**
