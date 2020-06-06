# AntiSkid

This plugin allows players to protect their cannons from being "skidded" or stolen by other players. Upon toggling antiskid via `/antiskid on`, all the repeaters on their cannon (determined by the faction territory or by using the region selector via `/antiskid tool`) will be replaced with comparators, while you and anyone else on your whitelist will be able to see the repeaters just fine. 

Anyone who tries to use Schematica or any other mod to capture the cannon will receive the protected cannon, and thus they will not be able to see any of your repeater timings!


## Region Selection

There are currently two methods of region selection to select the cannon: Factions-based and non-Factions-based. You can configure which worlds on your server are considered "Factions", and every other world will then not be considered "Factions".

**Factions:** On the Factions worlds, upon executing `/antiskid on`, the plugin will scan the entire connected chunk group and protect all repeaters in the claims.

**Non-Factions:** On the other worlds, when `/antiskid on` is entered, the plugin will use the region selected by the selection tool, obtained from `/antiskid tool`. The region selection process is identical to world edit; you select the two corners of the region you seek to protect - left click for position 1 and right click for position 2.

## Config

There are two config files in this plugin: `config.yml` and `messages.yml`. `config.yml` allows configuration of the functionalities of the plugin while `messages.yml` provides customization of the messages distributed by the plugin.

**config.yml** 
||Default Value|Meaning
|-|-|-|
|fast-scan|`false`|If true, will only protect repeaters in chunks with dispensers|
|factions.support|`true`|If true, will attempt to hook into Factions plugin|
|factions.minimum-rank|`moderator`|The minimum rank of the Faction member to use antiskid commands. Valid values: admin, coleader, moderator, normal, recruit|
|factions.worlds|`all worlds`|The list of worlds in which Factions-based region selection will occur|

## Support

For any support regarding this plugin (anything from bugs to suggestions) please contact `Reachy#8217` on Discord or email `bsalha1@gmail.com`.