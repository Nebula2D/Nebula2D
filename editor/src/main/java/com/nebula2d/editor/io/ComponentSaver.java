package com.nebula2d.editor.io;

import java.io.IOException;

public interface ComponentSaver {

    void save(FullBufferedWriter fw) throws IOException;
}