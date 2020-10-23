package me.earth.earthhack.api.event.events;

public abstract class StageEvent extends Event
{
    private final Stage stage;

    public StageEvent(Stage stage)
    {
        this.stage = stage;
    }

    public Stage getStage()
    {
        return stage;
    }

}
