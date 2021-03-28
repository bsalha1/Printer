# Printer
This is a plugin for Minecraft that aids in building, as it gives the player creative mode and charges them for each block they place. All exploits are patched as well.

## Permissions
- /printer on: printer.on
- /printer off: printer.off
- /printer toggle: printer.toggle
- /printer reload: printer.reload

## API
- **Events**:
  - `PrinterOnEvent` right before printer is turned on, cancellable
  - `PrinterOffEvent` right before printer is turned off, cancellable
- **Methods**:
  - `PrinterPlayer.fromPlayer(org.bukkit.entity.Player player)` returns the PrinterPlayer corresponding to the Bukkit player, null if they haven't used Printer yet
  - You also have access to any other public methods; most useful classes are PrinterPlayer.class and Printer.class