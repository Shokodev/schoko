package com.bacnetbrowser.schoko.bacnetutils.structure;

import com.bacnetbrowser.schoko.bacnetutils.structure.exceptions.StructureBuildException;

public interface Structurable {

    void build() throws StructureBuildException;

    void delete();

}
