package me.earth.pingbypass.server.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServerStatusServiceTest {
    @Test
    public void testThatFaviconMultiLineStringDidNotGetFormattedWrongly() {
        //noinspection TextBlockMigration
        String faviconNoMultiLine =
            "data:image/png;base64," +
            "iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAIAA" +
            "AAlC+aJAAABhUlEQVR42u3WMUgCUQDG8bc0OFhk\n" +
            "UBE1KRFZg9LgIMUFBi2JSSlUQyY0VBiElFs0" +
            "OAlNDQWFYUMFQVDU0BBtNoQRjVFLQdAS1NB2bR+v\n" +
            "4eCuw3wX38dvetyd93cQRcbEbo9mQD5//9wD" +
            "q+f6dT2YuddoggEM+IOAu/0oCGnyh8nnun4AwmBy\n" +
            "gNEzGcAAVQLKOxE4yQ/Bc2UFrooTIL/E2+sG" +
            "VM5moVxMwP15GuRzBjBAlYAfN1R5lt+HAQxgAAMY\n" +
            "ULMA3WCGf+BMXM8ABjgpgL9CDGAAAxjg/ACr" +
            "6+9uADsv2uXzwFg0DNX+ghjAALt7OZwEO8/JpkJQ\n" +
            "yA4AAxigesBCzA8JzQtm7u0LtMNyOgRBrwcY" +
            "wADVA+TdbMbBzPXTI35YmwuDqNUYwACbS2o+yIz2\n" +
            "gHxNsLcNCrkIdDS5gAEMcGqAvNhgJxzvTsHq" +
            "kgatzW4Qqo0BDPjF3K46KOU0eLxchK+PLUiNB4AB\n" +
            "DPgPAS2NLngoJeE0PwxPF/OwvR4HBjBAkYBv" +
            "eo6/J887s58AAAAASUVORK5CYII=";

        assertEquals(faviconNoMultiLine, ServerStatusService.FAVICON);
    }

}
