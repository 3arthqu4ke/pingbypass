package me.earth.pingbypass.client.commands;


public abstract class AbstractCommand
{
    private final String name;

    public AbstractCommand(String name)
    {
        this.name = name;
    }

    public abstract void execute(String[] args);

    public String getName()
    {
        return name;
    }

}
