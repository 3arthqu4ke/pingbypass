package me.earth.pingbypass.api.input;

public class DummyKeyboardAndMouse extends AbstractKeyboardAndMouse {
    public static final DummyKeyboardAndMouse INSTANCE = new DummyKeyboardAndMouse();

    private DummyKeyboardAndMouse() {
        super(new KeyRegistry().loadKeyDataBase());
    }

    @Override
    public boolean isPressed(Key.Type keyType, int keyCode) {
        return false;
    }

}
