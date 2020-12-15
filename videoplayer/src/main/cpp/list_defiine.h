//
// Created by Void on 2020/12/11.
//

#ifndef TESTEXAMPLE_LIST_DEFINE_H
#define TESTEXAMPLE_LIST_DEFIINE_H

#include <stdio.h>
#include <stdlib.h>
#include <string>

//代表列表的结构体
struct List {
    void **data;
    int size;
    int capacity;
    int elementSize;
};

//操作列表的方法
struct List *initList(int capacity, int elementSize);

void destroyList(struct List *list);

void *listGet(struct List *list, int index);

int listSet(struct List *list, int index, void *element);

struct List *initList(int capacity, int elementSize) {
    if (capacity <= 0 || elementSize <= 0) {
        //参数不合法，简单返回NULL
        return NULL;
    }
    struct List *list = (List *) malloc(sizeof(struct List));
    list->capacity = capacity;
    list->data = (void **) malloc(capacity * sizeof(size_t));
    int i;
    for (i = 0; i < capacity; i++) {
        list->data[i] = NULL;
    }
    list->size = 0;
    list->elementSize = elementSize;
    return list;
}

void destroyList(struct List *list) {
    if (list == NULL) {
        return;
    }
    if (list->data != NULL) {
        int i;
        for (i = 0; i < list->size; i++) {
            if (list->data[i] != NULL) {
                free(list->data[i]);
            }
        }
        free(list->data);
    }
    free(list);
}

void *listGet(struct List *list, int index) {
    if (list == NULL) {
        return NULL;
    }
    if (index < 0 || index >= list->capacity) {
        return NULL;
    }
    return list->data[index];
}

int listSet(struct List *list, int index, void *element) {
    if (list == NULL) {
        return -1;
    }
    if (index < 0 || index >= list->capacity) {
        return -1;
    }
    //复制element，否则如果element来自局部变量，会有生命周期问题
    void *e = malloc(list->elementSize);
    memcpy(e, element, list->elementSize);
    list->data[index] = e;
    list->size++;
    return 0;
}

int main(void) {
    struct List *intList = initList(50, sizeof(int));
    int num = 100;
    listSet(intList, 0, (void *) &num);
    int i = *((int *) listGet(intList, 0));
    printf("element in index 0 is %d", i);
    destroyList(intList);
}

#endif //TESTEXAMPLE_LIST_DEFINE_H
