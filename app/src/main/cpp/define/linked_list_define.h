//
// Created by Void on 2020/12/16.
//

#ifndef TESTEXAMPLE_LINKED_LIST_DEFINE_H
#define TESTEXAMPLE_LINKED_LIST_DEFINE_H

#include<stdio.h>
#include<stdlib.h>
#include<memory.h>

struct BaseNode {
    struct BaseNode *next;
};

class LinkedList {
private:
    BaseNode header;
    int length;
public:
    inline LinkedList();

    inline int Size();

    inline bool add(BaseNode *node);

    inline bool add(BaseNode *node, int index);

    inline BaseNode *get(int index);

    inline void set(int index, BaseNode *node);

    inline bool remove(BaseNode *node);

    inline BaseNode *removeAt(int index);

    inline void clear();

    inline void release();
};

#endif //TESTEXAMPLE_LINKED_LIST_DEFINE_H