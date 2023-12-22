## PingBypass Security
Originally, the idea was to make each PingBypass Server/Client have keys to verify that server/client are correct.
Since generating keys requires bouncy castle, a large dependency, I put this into a separate plugin,
but this should just be part of the common code.
