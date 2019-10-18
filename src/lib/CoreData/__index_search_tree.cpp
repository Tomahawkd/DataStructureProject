//
// Created by Ghost on 29/01/2018.
//

#include "__index_search_tree.h"

__index_search_tree::__index_search_tree() {
    ___root_index_node = nullptr;
    ___size = 0;
}

__index_search_tree::~__index_search_tree() {
    clear(___root_index_node);
}

void __index_search_tree::clear(__index_node *_current) {
    if (!_current) return;
    clear(_current->___children[LEFT]);
    clear(_current->___children[RIGHT]);
    delete _current;
}

__index_node *__index_search_tree::__search(int _index) {
    __index_node *___ptr = ___root_index_node;
    while (___ptr) {
        if (_index < ___ptr->___segment_index) ___ptr = ___ptr->___children[LEFT];
        else if (_index > ___ptr->___segment_index) ___ptr = ___ptr->___children[RIGHT];
        else return ___ptr;
    }

    return nullptr;
}

/*
 * The __add method is private in the tree.
 *
 * Potential error
 * DUPLICATED(new index error)
 */
int __index_search_tree::__add(int _new_segment_index) {
    __index_node *__ptr = ___root_index_node;
    __index_node *__parent_ptr = nullptr;
    while (__ptr) {
        __parent_ptr = __ptr;
        if (_new_segment_index < __ptr->___segment_index) {
            __ptr = __ptr->___children[LEFT];
        } else if (_new_segment_index > __ptr->___segment_index) {
            __ptr = __ptr->___children[RIGHT];
        } else {

            // index exist
            return DUPLICATED;
        }
    }

    auto *__new_node = new __index_node(_new_segment_index);

    // equals if(___root_index_node)
    if (__parent_ptr) {
        if (_new_segment_index < __parent_ptr->___segment_index) {
            __parent_ptr->___children[LEFT] = __new_node;
        } else {
            __parent_ptr->___children[RIGHT] = __new_node;
        }
    } else {
        ___root_index_node = __new_node;
    }
    ___size++;
    return SUCCESS;
}

/* Potential error
 * DUPLICATED(new index error)
 * NOT_FOUND(old index error)
 * PARAM_ERROR(old/new)
 */
int __index_search_tree::__put(int _old_segment_index, int _new_segment_index) {

    // check validity
    if (_new_segment_index == NO_RECORD) return PARAM_ERROR;
    if (__search(_new_segment_index)) return DUPLICATED;

    // add index
    if (_old_segment_index == NO_RECORD) return __add(_new_segment_index);

        // edit index
    else {
        __index_node *__target = __search(_old_segment_index);
        if (!__target) return NOT_FOUND;

        int __list_index = __target->___array_list_index;

        // check result code
        int __result_code = 0;
        __result_code = __delete(_old_segment_index);

        // Undo
        if (__result_code != SUCCESS) {
            __add(_old_segment_index);
            __update(_old_segment_index, __list_index);
            return __result_code;
        }

        __result_code = __add(_new_segment_index);

        // Undo
        if (__result_code != SUCCESS) {
            __delete(_new_segment_index);
            __add(_old_segment_index);
            __update(_old_segment_index, __list_index);
            return __result_code;
        }

        __result_code = __update(_new_segment_index, __list_index);

        //Undo
        if (__result_code != SUCCESS) {
            __delete(_new_segment_index);
            __add(_old_segment_index);
            __update(_old_segment_index, __list_index);
            return __result_code;
        }

        return SUCCESS;
    }
}

/*
 * Returns: -1(no record)/real value
 */

int __index_search_tree::__get(int _index) {
    __index_node *__target = __search(_index);
    if (!__target) return NO_RECORD;
    else return __target->___array_list_index;
}

/* Potential error
 * NOT_FOUND(book index error)
 */
int __index_search_tree::__delete(int _index) {

    __index_node *__ptr = ___root_index_node;
    __index_node *__parent_ptr = nullptr;
    while (__ptr && __ptr->___segment_index != _index) {
        __parent_ptr = __ptr;

        if (_index < __ptr->___segment_index) __ptr = __ptr->___children[LEFT];
        else __ptr = __ptr->___children[RIGHT];
    }

    if (!__ptr) return NOT_FOUND;

    // two children (convert into one child)
    if (__ptr->___children[LEFT] && __ptr->___children[RIGHT]) {
        __index_node *__child = __ptr->___children[LEFT];
        __index_node *__parent = __ptr;

        while (__child->___children[RIGHT]) {
            __parent = __child;
            __child = __child->___children[RIGHT];
        }

        __ptr->___segment_index = __child->___segment_index;
        __ptr->___array_list_index = __child->___array_list_index;

        auto *__new_node = new __index_node(__child);
        __new_node->___children[LEFT] = __ptr->___children[LEFT];
        __new_node->___children[RIGHT] = __ptr->___children[RIGHT];

        if (!__parent_ptr) {
            ___root_index_node = __new_node;
        } else {
            if (__ptr == __parent_ptr->___children[LEFT])
                __parent_ptr->___children[LEFT] = __new_node;
            else __parent_ptr->___children[RIGHT] = __new_node;
        }

        if (__parent == __ptr) __parent_ptr = __new_node;
        else __parent_ptr = __parent;

        delete __ptr;
        __ptr = __child;
    }

    // at most one child
    __index_node *__ptr_left = nullptr;
    if (__ptr->___children[LEFT]) __ptr_left = __ptr->___children[LEFT];
    else __ptr_left = __ptr->___children[RIGHT];

    // equals if(__ptr == ___root_index_node)
    if (!__parent_ptr) ___root_index_node = __ptr_left;
    else {
        if (__ptr == __parent_ptr->___children[LEFT])
            __parent_ptr->___children[LEFT] = __ptr_left;
        else __parent_ptr->___children[RIGHT] = __ptr_left;
    }

    ___size--;
    delete __ptr;

    return SUCCESS;
}

/* Potential error
 * NOT_FOUND(book index error)
 * PARAM_ERROR(array index error)
 */
int __index_search_tree::__update(int _segment_index, int _array_index) {
    if (_array_index < 0) return PARAM_ERROR;

    __index_node *__target = __search(_segment_index);
    if (!__target) return NOT_FOUND;

    __target->___array_list_index = _array_index;
    return SUCCESS;
}

int __index_search_tree::__size() {
    return ___size;
}

int *__index_search_tree::__export() {
    auto *__index_array = new int[__size()];
    int _temp_count = 0;
    __foreach_in_order(___root_index_node,
                       [&__index_array, &_temp_count](__index_node *index_node) {
                           __index_array[_temp_count] = index_node->___segment_index;
                           _temp_count++;
                       });

    return __index_array;
}

void __index_search_tree::__foreach_in_order(
        __index_node *_current,
        const std::function<void(__index_node *)> &_operation) {
    if (!_current) return;
    __foreach_in_order(_current->___children[LEFT], _operation);
    _operation(_current);
    __foreach_in_order(_current->___children[RIGHT], _operation);
}


