cmake_minimum_required(VERSION 3.4.1)

file(GLOB native_files "src/main/cpp/my/*.cpp" ,"src/main/cpp/*.cpp")
file(GLOB native_files2 "src/main/cpp/*.cpp")
file(GLOB native_files3 "src/main/cpp/*.c")
include_directories(src/main/cpp)
include_directories(src/main/cpp/include)
include_directories(src/main/cpp/include2)

add_library(
        media-handle
        SHARED
        ${native_files})
add_library(
        media-handle2
        SHARED
        ${native_files2}
        ${native_files3})

add_library(ffmpeg
        SHARED
        IMPORTED)
set_target_properties(ffmpeg
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libffmpeg.so)

add_library(mp3lame
        SHARED
        IMPORTED)
set_target_properties(mp3lame
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libmp3lame.so)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

if (${CMAKE_ANDROID_ARCH_ABI} MATCHES "armeabi-v7a")
    include_directories(src/main/cpp/include2/armeabi-v7a)
    message("This is armeabi-v7a")
elseif (${CMAKE_ANDROID_ARCH_ABI} MATCHES "arm64-v8a")
    include_directories(src/main/cpp/include2/arm64-v8a)
    message("This is arm64-v8a")
endif ()

find_library(
        log-lib
        log)
target_link_libraries( # Specifies the target library.
        media-handle2
        mp3lame
        ffmpeg
        -landroid #native_window
        -ljnigraphics #bitmap
        -lOpenSLES #openSLES

        ${log-lib})
target_link_libraries(
        media-handle
        ffmpeg
        log
        android
        ${log-lib})