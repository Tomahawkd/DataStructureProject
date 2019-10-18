//
// Created by Ghost on 12/03/2018.
//

#include <iostream>
#include "__index_search_tree.h"

int main() {
    __index_search_tree tree = __index_search_tree();

    /**
     * Correction test
     */

    // add & update
    for (int i = 0; i < 100; ++i) {
        int index = 100 - i;
        std::cout << index << ": ";
        tree.__put(-1, index);
        int value = abs((int) arc4random());
        std::cout << value << " "; // 1
        tree.__update(index, value);
    }

    int size = tree.__size();
    std::cout << std::endl << size << std::endl; // 2
    int * content = tree.__export();

    for (int j = 0; j < size; ++j) {
        std::cout << content[j] << ": " << tree.__get(content[j]) << " "; // 3
    }
    std::cout << std::endl;

    // edit
    for (int m = 1; m <= size; ++m) {
        tree.__put(m, m+100);
    }

    int * edit_content = tree.__export();

    for (int j = 0; j < size; ++j) {
        std::cout << edit_content[j] << ": " << tree.__get(edit_content[j]) << " "; // 4
    }
    std::cout << std::endl;

    // delete
    int deletion = abs((int) arc4random()) % 100;
    for (int k = 0; k < deletion; ++k) {
        int delete_index = abs((int) arc4random()) % 100 + 100;
        tree.__delete(delete_index);
    }
    int size_deletion = tree.__size();
    std::cout << size_deletion << std::endl; // 5
    int * content_deletion = tree.__export();

    for (int j = 0; j < size_deletion; ++j) {
        std::cout << content_deletion[j] << ": " << tree.__get(content_deletion[j]) << " "; // 6
    }

    std::cout << std::endl;

    // update
    for (int l = 0; l < size_deletion; ++l) {
        tree.__update(content_deletion[l], l);
    }

    for (int j = 0; j < size_deletion; ++j) {
        std::cout << content_deletion[j] << ": " << tree.__get(content_deletion[j]) << " "; // 7
    }

    std::cout << "\n\n";

    /**
     * Error Code test
     */

    std::cout << tree.__get(-1) << " "; // -1 NO_RECORD

    std::cout << tree.__put(-1, content_deletion[0]) << " "; // 1 DUPLICATED
    std::cout << tree.__put(500, 3) << " "; // 2 NOT_FOUND
    std::cout << tree.__put(1, -1) << " "; // 3 PARAM_ERROR

    std::cout << tree.__delete(4000) << " "; // 2 NOT_FOUND

    std::cout << tree.__update(-1, 1) << " "; // 2 NOT_FOUND
    std::cout << tree.__update(105, -21) << " "; // 3 PARAM_ERROR
}