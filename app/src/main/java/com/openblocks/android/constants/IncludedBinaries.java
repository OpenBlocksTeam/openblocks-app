package com.openblocks.android.constants;

import android.content.Context;

import com.openblocks.moduleinterface.models.compiler.IncludedBinary;

public class IncludedBinaries {
    public static IncludedBinary[] INCLUDED_BINARIES;

    public static void init(Context context) {
        String nativeLibraryDir = context.getApplicationInfo().nativeLibraryDir;

        INCLUDED_BINARIES = new IncludedBinary[] {
                new IncludedBinary(
                        "aapt",
                        1,
                        nativeLibraryDir.concat("/aapt")
                ),
                new IncludedBinary(
                        "aapt2",
                        1,
                        nativeLibraryDir.concat("/aapt2")
                ),
                new IncludedBinary(
                        "zipalign",
                        1,
                        nativeLibraryDir.concat("/zipalign")
                ),
        };
    }
}
