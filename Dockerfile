FROM openjdk:17-alpine

WORKDIR /pingbypass
COPY . /pingbypass

RUN chmod +x gradlew
RUN ./gradlew -Phmc.specifics=true -Phmc.lwjgl=true :pb-server-fabric:build

# prepare everything for runClientWithoutDependencies
RUN ./gradlew -Phmc.specifics=true -Phmc.lwjgl=true :pb-server-fabric:preRunClient

EXPOSE 25565

# TODO: Copy Config
# TODO: Minimal Minecraft config with lowest graphic settings
# TODO: unimined still runs configuration steps for each gradle module before this
ENTRYPOINT sh -c "./gradlew -Phmc.specifics=true -Phmc.lwjgl=true :pb-server-fabric:runClientWithoutDependencies"
