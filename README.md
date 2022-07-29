# RandomTP

A plugin that allows players to easily teleport to a random location.

## Requirements

This plugin requires [MeepCore](https://github.com/Meeples10/MeepCore/releases).

## Commands

|Command|Description|Permission|
|-------|-----------|----------|
|`/rtp`|Teleports the user to a random location.|`randomtp.use`|
|`/rtp reload`|Reloads the plugin.|`randomtp.reload`|
|`/rtp debug`|Shows debug information.|`randomtp.debug`|

## Configuration

The default configuration file can be found [here](https://github.com/Meeples10/RandomTP/blob/master/src/main/resources/config.yml).

|Key|Description|
|---|-----------|
|`enabled-worlds`|The names of the worlds in which `/randomtp` should function.|
|`maximum-distance`|The maximum distance for `/randomtp` if no argument is supplied. This value is capped at 30,000,000 blocks.|
|`cooldown`|The cooldown before players are allowed to use the command again, in milliseconds. Set to `0` for no cooldown.|
