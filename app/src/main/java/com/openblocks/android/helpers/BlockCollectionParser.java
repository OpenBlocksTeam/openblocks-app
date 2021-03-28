package com.openblocks.android.helpers;

import android.util.Pair;

import com.openblocks.moduleinterface.OpenBlocksModule;
import com.openblocks.moduleinterface.models.code.ParseBlockTask;

import java.util.HashMap;

public class BlockCollectionParser {

    /**
     * This function is used to parse blocks that are set by BlocksCollection module
     * @param blocks The blocks got from {@link OpenBlocksModule.BlocksCollection#getBlocks()} or something like this: Object[Object[String opcode, String format, ParseBlockTask]]
     * @return A HashMap of opcode, Pair of format and ParseBlockTask
     */
    public static HashMap<String, Pair<String, ParseBlockTask>> parseBlocks(Object[] blocks) {
        // Object[Object[String opcode, String format, ParseBlockTask]]

        HashMap<String, Pair<String, ParseBlockTask>> result = new HashMap<>();
        
        for (Object block_object : blocks) {
            Object[] block = (Object[]) block_object;

            String opcode = (String) block[0];
            String format = (String) block[1];
            ParseBlockTask parseBlockTask = (ParseBlockTask) block[2];

            result.put(opcode, new Pair<>(format, parseBlockTask));
        }
        
        return result;
    }
}
