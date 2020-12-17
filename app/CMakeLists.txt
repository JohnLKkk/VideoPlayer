cmake_minimum_required(VERSION 3.4.1)
project(test_Example)
file(GLOB native_files "src/main/cpp/*.cpp","src/main/cpp/define/*.cpp")

include_directories(src/main/cpp)
include_directories(src/main/cpp/define)

add_library(
        my-handle
        SHARED
        ${native_files})

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

find_library(
        log-lib
        log)
target_link_libraries(
        my-handle
        log
        android)