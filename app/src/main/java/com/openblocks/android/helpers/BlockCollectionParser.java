package com.openblocks.android.helpers;

import android.util.Pair;

import com.openblocks.moduleinterface.OpenBlocksModule;
import com.openblocks.moduleinterface.models.code.BlockCode;
import com.openblocks.moduleinterface.models.code.BlockCodeNest;
import com.openblocks.moduleinterface.models.code.ParseBlockTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class BlockCollectionParser {

    /**
     * This function is used to parse blocks that are set by BlocksCollection module
     * @param blocks The blocks got from {@link OpenBlocksModule.BlocksCollection#getBlocks()} or something like this: Object[Object[Class block_type, int color, String opcode, String format, ParseBlockTask]]
     * @return A HashMap of opcode, Pair of format and ParseBlockTask
     */
    public static ArrayList<BlockCode> parseBlocks(Object[] blocks) throws ClassCastException {
        // Object[Object[String opcode, String format, ParseBlockTask]]

        ArrayList<BlockCode> result = new ArrayList<>();
        
        for (Object block_object : blocks) {
            Object[] block = (Object[]) block_object;

            BlockCode b_result = null;

            Class<BlockCode> block_type = (Class<BlockCode>) block[0];
            
            int color = (int) block[1];
            String opcode = (String) block[2];
            String format = (String) block[3];
            ParseBlockTask parseBlockTask = (ParseBlockTask) block[4];
            
            if (block_type.isAssignableFrom(BlockCode.class)) {
                b_result = new BlockCode(opcode, format, color, parseBlockTask, new ArrayList<>());

            } else if (block_type.isAssignableFrom(BlockCodeNest.class)) {
                b_result = new BlockCodeNest(opcode, format, color, parseBlockTask, new ArrayList<>(), new ArrayList<>());
            }

            result.add(b_result);
        }
        
        return result;
    }

    /**
     * A simple wrapper function that gets and parses the blocks of the specified blocks
     * collection module
     * @param blocks_collection The blocks collection module
     * @return The parsed blocks of that blocks collection
     */
    public static ArrayList<BlockCode> getBlocks(OpenBlocksModule.BlocksCollection blocks_collection) {
        Object[] blocks = blocks_collection.getBlocks();

        return parseBlocks(blocks);
    }
}
