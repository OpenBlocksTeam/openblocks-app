#/bin/sh

git submodule update --init

dest="app/src/main/jniLibs"
mkdir -p $dest

# Simply copies aapt, aapt2, and zipalign on android-build-tools into app/src/main/resources/lib
cp -r android-build-tools/aapt/arm64-v8a android-build-tools/aapt/armeabi-v7a android-build-tools/aapt/x86 android-build-tools/aapt/x86_64  $dest
cp -r android-build-tools/aapt2/arm64-v8a android-build-tools/aapt2/armeabi-v7a android-build-tools/aapt2/x86 android-build-tools/aapt2/x86_64  $dest
cp -r android-build-tools/zipalign/arm64-v8a android-build-tools/zipalign/armeabi-v7a android-build-tools/zipalign/x86 android-build-tools/zipalign/x86_64  $dest
