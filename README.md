# Printer
This is a plugin for Minecraft that aids in building, as it gives the player creative mode and charges them for each block they place. All exploits are patched as well.

## Building
To build Printer, execute `mvn -Dmaven.repo.local=./.m2/repo clean install`. We specify a local repository since remote repositories may break and in that case we still want to be able to build Printer.

## Permissions
- /printer on: printer.on
- /printer off: printer.off
- /printer toggle: printer.toggle
- /printer reload: printer.reload
- printer.hide: allows admins to not deactivate nearby players' printers when monitoring them

## API
- **Events**:
  - `PrinterOnEvent` right before printer is turned on, cancellable
  - `PrinterOffEvent` right before printer is turned off, cancellable
- **Methods**:
  - `PrinterPlayer.fromPlayer(org.bukkit.entity.Player player)` returns the PrinterPlayer corresponding to the Bukkit player, null if they haven't used Printer yet
  - You also have access to any other public methods; most useful classes are PrinterPlayer.class and Printer.class