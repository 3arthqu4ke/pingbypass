# Pingbypass-Server
This module contains the code for the PingBypass server.

## The PingBypass Protocol
* Client sends a *ClientIntentionPacket* with the intention to login
* Client sends a *ServerboundHelloPacket* with username and uuid
    * If Packet encryption is enabled the Server sends back a *ClientboundHelloPacket* with its public key
    * Client then answers with a *ServerboundKeyPacket*
* Server might send a *ClientboundLoginCompressionPacket* to enable compression
* ~~Server then sends a first *ClientboundGameProfilePacket* to enable the PLAY Protocol~~

Up to now this was pretty much the standard Minecraft protocol for joining a server.
Communication with the PingBypass-Server now happens via *ServerboundCustomQueryPacket* and
*ClientboundCustomQueryPacket*:

The following steps happen if Security is enabled on the server:
* Server sends a *S2CValidateClientPacket* containing its server id and a challenge (byte array).
* Client uses its private key to encrypt the challenge and sends back a *C2SValidateServerPacket* containing its
id, the encrypted challenge and a challenge for the server to solve.
* The Server will verify the challenge using the public key with the clients' id. If the challenge has been solved
correctly the Server will encrypt the clients' challenge with its own private key and send it back via a 
*S2CSolvedChallengePacket*.
* Client verifies the challenge with the public key with the id of the server. If everything is ok 
* 
