cmake_minimum_required(VERSION 3.9)
project(CoreData)

set(CMAKE_CXX_STANDARD 11)

add_library(CoreData SHARED jni_md.h jni.h jni_DataSource.h __index_search_tree.cpp __index_search_tree.h jni_DataSource.cpp TreeTest.cpp)

add_executable(Core TreeTest.cpp __index_search_tree.cpp __index_search_tree.h)