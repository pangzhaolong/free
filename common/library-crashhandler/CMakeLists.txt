# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             safe-reflection

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             "${src}"
             src/main/jni/cpp/Restriction.cpp )


find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

target_link_libraries( # Specifies the target library.
        safe-reflection
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_include_directories(safe-reflection PRIVATE)