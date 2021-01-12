cmake_minimum_required(VERSION 3.4.1)
project(video_ffmpeg)
file(GLOB native_files1 "src/main/cpp/*.cpp")
file(GLOB native_files2 "src/main/cpp/define/*.cpp")

include_directories(src/main/cpp)
include_directories(src/main/cpp/define)
include_directories(src/main/cpp/ffmpeg)
include_directories(src/main/cpp/include)

add_library(
        media-handle
        SHARED
        ${native_files1}
        ${native_files2})

#add_library(ffmpeg
#        SHARED
#        IMPORTED)
#set_target_properties(ffmpeg
#        PROPERTIES IMPORTED_LOCATION
#        ${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libffmpeg.so)

#添加libavcodec-57.so
add_library( avcodec-57
        SHARED
        IMPORTED)
set_target_properties( avcodec-57
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavcodec-57.so)


#添加libavdevice-57.so
add_library( avdevice-57
        SHARED
        IMPORTED)
set_target_properties( avdevice-57
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavdevice-57.so)

add_library( avfilter-6
        SHARED
        IMPORTED)
set_target_properties( avfilter-6
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavfilter-6.so)

add_library( avformat-57
        SHARED
        IMPORTED)
set_target_properties( avformat-57
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavformat-57.so)

add_library( avutil-55
        SHARED
        IMPORTED)
set_target_properties( avutil-55
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavutil-55.so)

add_library( swresample-2
        SHARED
        IMPORTED)
set_target_properties( swresample-2
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libswresample-2.so)

add_library( swscale-4
        SHARED
        IMPORTED)
set_target_properties( swscale-4
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libswscale-4.so)

add_library( postproc-54
        SHARED
        IMPORTED)
set_target_properties( postproc-54
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libpostproc-54.so)


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
#target_link_libraries(
#        media-handle
#        ffmpeg
#        log
#        android)
target_link_libraries(
        media-handle
        avcodec-57
        avdevice-57
        avfilter-6
        avformat-57
        avutil-55
        swresample-2
        swscale-4
        postproc-54
        log
        android)