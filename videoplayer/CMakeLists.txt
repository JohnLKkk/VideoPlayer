cmake_minimum_required(VERSION 3.4.1)
project(video_ffmpeg)
file(GLOB native_files "src/main/cpp/*.cpp","src/main/cpp/define/*.cpp")

include_directories(src/main/cpp)
include_directories(src/main/cpp/define)
include_directories(src/main/cpp/ffmpeg)
include_directories(src/main/cpp/include)

add_library(
        media-handle
        SHARED
        ${native_files})

add_library(ffmpeg
        SHARED
        IMPORTED)
set_target_properties(ffmpeg
        PROPERTIES IMPORTED_LOCATION
        ../../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libffmpeg.so)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

if (${CMAKE_ANDROID_ARCH_ABI} MATCHES "armeabi-v7a")
    include_directories(src/main/cpp/abiFilters/armeabi-v7a)
    message("This is armeabi-v7a")
elseif (${CMAKE_ANDROID_ARCH_ABI} MATCHES "arm64-v8a")
    include_directories(src/main/cpp/abiFilters/arm64-v8a)
    message("This is arm64-v8a")
endif ()

find_library(
        log-lib
        log)
target_link_libraries(
        media-handle
        ffmpeg
        log
        android)