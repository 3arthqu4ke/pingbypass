# PingBypass

PingBypass is a proxy server/client aimed at 1.12.2 Anarchy PvP. It can be used as a (slow) VPN and more importantly runs its own AutoCrystal and AutoTotem.

## Installation
- Get a server, preferably hosted as close to the server you want to play on as possible.
- Install Java-8, the Minecraft 1.12.2 client and forge for that version on the server.
- Drag the PingBypass.jar into the mods folder on the server.
- Run the game once then close it, this should create the Server.json file in your servers minecraft folder which is used to configure the proxy.
- Open the file, type in IP and port.
- The IP should be the internal IP of your server (not 127.0.0.0), the port should be a forwarded port (not 25565 (minecraft) or other important ports).
- Now run the game, everything else is done from your pc.

## Usage
- PingBypass requires the PingBypassClient on your pc to connect to it. Just drag the latest PingBypassClient.jar into your mods folder.
- In the MultiPlayer gui you are going to notice 2 new buttons in the right corner.
- The one with the book is used to set ip and port for PingBypass you want to connect to, the other one turns PingBypass on and off.
- Type in the external ip of your PingBypass server and the open port, then click done.
- Then connect to any server you want to, you are going to be redirected via the PingBypass, if its on.
- Ingame just use PingBypassClients command system to toggle modules like AutoCrystal (S-AutoCrystal), AutoTotem (S-AutoTotem) or FakePlayer.
- AutoCrystal can be on at all times, it only breaks and places if you hold an endcrystal.
- Note that PingBypass was made for the purpose of CrystalPvP. I wouldn't use it for anything else (travelling, building etc.).
- https://github.com/3arthqu4ke/PingBypassClient

## Credits
- https://github.com/Steveice10/MCProtocolLib

## License 
The content of this project is licensed under the [MIT license](LICENSE).
