//
// Created by Ghost on 07/03/2018.
//

#include "jni_DataSource.h"
#include "__index_search_tree.h"

JNIEXPORT jlong JNICALL Java_jni_DataSource_alloc(JNIEnv *, jobject) {
    return reinterpret_cast<jlong>(new __index_search_tree());
}

JNIEXPORT void JNICALL Java_jni_DataSource_dealloc(JNIEnv *, jobject, jlong ptr) {
    delete (__index_search_tree *) ptr;
}

JNIEXPORT jint JNICALL Java_jni_DataSource_getIndex(JNIEnv *, jobject, jlong ptr, jint book_index) {
    return ((__index_search_tree *) ptr)->__get(book_index);
}


JNIEXPORT jint JNICALL Java_jni_DataSource_setIndex
        (JNIEnv *, jobject, jlong ptr, jint old_index, jint new_index) {
    return ((__index_search_tree *) ptr)->__put(old_index, new_index);
}

JNIEXPORT jint JNICALL Java_jni_DataSource_updateArrayIndex
        (JNIEnv *, jobject, jlong ptr, jint book_index, jint arr_index) {
    return ((__index_search_tree *) ptr)->__update(book_index, arr_index);
}

JNIEXPORT jint JNICALL Java_jni_DataSource_deleteIndex(JNIEnv *, jobject, jlong ptr, jint book_index) {
    return ((__index_search_tree *) ptr)->__delete(book_index);
}

JNIEXPORT jintArray JNICALL Java_jni_DataSource_exportIndexList(JNIEnv *env, jobject, jlong ptr) {
    __index_search_tree *p = ((__index_search_tree *) ptr);
    jintArray newIntArray = env->NewIntArray(p->__size());
    int *arr = p->__export();
    env->SetIntArrayRegion(newIntArray, 0, p->__size(), arr);
    env->ReleaseIntArrayElements(newIntArray, arr, 0);
    return newIntArray;
}
