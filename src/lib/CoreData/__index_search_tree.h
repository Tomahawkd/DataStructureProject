//
// Created by Ghost on 29/01/2018.
//

#ifndef COREDATA_SEARCH_TREE_LIST_H
#define COREDATA_SEARCH_TREE_LIST_H

#include <functional>

/*
 * Error codes
 */
#define SUCCESS 0
#define DUPLICATED 1
#define NOT_FOUND 2
#define PARAM_ERROR 3

/*
 * Child index
 */
#define LEFT 0
#define RIGHT 1

#define NO_RECORD (-1)

/**
 * Store index data include book index and array list index in java VM.
 * Be advised, the array list index need to be update after initialized
 * or it has a default value -1 which indicate there is no array list index
 * with the specific book index.
 */
struct __index_node {
    int ___segment_index;
    int ___array_list_index = NO_RECORD;

    __index_node *___children[2] = {nullptr};

    explicit __index_node(int _segment_index) {
        this->___segment_index = _segment_index;
    }

    explicit __index_node(__index_node * _index_node) {
        this->___segment_index = _index_node->___segment_index;
        this->___array_list_index = _index_node->___array_list_index;
        this->___children[LEFT] = _index_node->___children[LEFT];
        this->___children[RIGHT] = _index_node->___children[RIGHT];
    }

    ~__index_node() = default;
};


/**
 * Core class to store index data.
 */
class __index_search_tree {

private:

    // Properties
    __index_node *___root_index_node;
    int ___size;

    // Private Functions
    __index_node *__search(int);

    void __foreach_in_order(__index_node *, const std::function<void(__index_node *)> &);

    void clear(__index_node *);

    int __add(int);

public:

    __index_search_tree();

    ~__index_search_tree();

    /**
     * Set book index.
     *
     * @param oldIndex previous book index, -1 if there is no record
     * @param newIndex new book index to be set, must check negative number.
     * @return <p>0 for successfully changed</p>
     * <p>1 for duplication</p>
     * <p>2 for record not found</p>
     * <p>3 for parameter error</p>
     */
    int __put(int, int);

    /**
     * Get array index to get specific segment
     *
     * @param bookIndex specific book index
     * @return book information index in array, -1 for not found
     */
    int __get(int);

    /**
	 * Delete book index.
	 *
	 * @param bookIndex new book index to be deleted, must check negative number.
	 * @return <p>0 for successfully changed</p>
	 * <p>1 for duplication</p>
	 * <p>2 for record not found</p>
	 * <p>3 for parameter error</p>
	 */
    int __delete(int);

    /**
	 * Update array index storing in native block. Using in add function.
	 *
	 * @param bookIndex  book Index
	 * @param arrayIndex the new array index
	 * @return <p>0 for successfully changed</p>
	 * <p>1 for duplication</p>
	 * <p>2 for record not found</p>
	 * <p>3 for parameter error</p>
     *
     * @see __index_node
	 */
    int __update(int, int);

    /**
     * Get the array length.
     * @return array length of the class exported.
     */
    int __size();

    /**
	 * Export native index block
	 *
	 * @return book index list
	 */
    int *__export();
};

#endif //COREDATA_SEARCH_TREE_LIST_H
